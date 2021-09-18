package com.migu.tsg.microservice.atomicservice.composite.controller.cmdb.instance;

import com.aspire.mirror.composite.service.cmdb.instance.IInstanceAPI;
import com.aspire.mirror.composite.service.inspection.payload.ConfigDict;
import com.aspire.ums.cmdb.code.payload.CmdbCode;
import com.aspire.ums.cmdb.code.payload.CmdbSimpleCode;
import com.aspire.ums.cmdb.common.Result;
import com.aspire.ums.cmdb.instance.payload.CmdbDeleteInstance;
import com.aspire.ums.cmdb.instance.payload.CmdbDeviceTypeCount;
import com.aspire.ums.cmdb.instance.payload.CmdbInstance;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb.ConfigDictClient;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb.collect.CmdbCollectClient;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb.instance.InstanceClient;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb.module.ModuleClient;
import com.migu.tsg.microservice.atomicservice.composite.controller.CommonResourceController;
import com.migu.tsg.microservice.atomicservice.composite.controller.authcontext.RequestAuthContext;
import com.migu.tsg.microservice.atomicservice.composite.controller.authcontext.ResAction;
import com.migu.tsg.microservice.atomicservice.composite.controller.util.ExportExcelUtil;
import com.migu.tsg.microservice.atomicservice.composite.service.rbac.ICompPermissionService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司 FileName: InstanceController Author:
 * zhu.juwang Date: 2019/5/21 14:56 Description: ${DESCRIPTION} History:
 * <author> <time> <version> <desc> 作者姓名 修改时间 版本号 描述
 */
@RestController
@Slf4j
public class InstanceController extends CommonResourceController implements IInstanceAPI {

	@Autowired
	private InstanceClient instanceClient;
	@Autowired
	private ModuleClient moduleClient;
	@Autowired
	private CmdbCollectClient collectClient;

	@Autowired
	private ICompPermissionService  permissionService;
	
	@Value("${indexPage.count.realDataFlag:false}")
	private boolean  realDataFlag;

	@Autowired
	private ConfigDictClient configDictClient;
	
	@Override
	public List<CmdbSimpleCode> getInstanceHeader(@RequestParam(value = "moduleId",required = false) String moduleId,
												  @RequestParam(value = "moduleType", required = false) String moduleType) {
		return instanceClient.getInstanceHeader(moduleId, moduleType);
	}

	@Override
	public Result<Map<String, Object>> getInstanceList(@RequestBody Map<String, Object> params) {
		return instanceClient.getInstanceList(params);
	}

	@Override
	@ResAction(action = "view", resType = "cmdb", loadResFilter=true)
	public Result<Map<String, Object>> getInstanceListV3(@RequestBody Map<String, Object> params,
														 @RequestParam(value = "moduleType", required = false) String moduleType) {
		RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
		RequestAuthContext.RequestHeadUser user = authCtx.getUser();
		// 互联网的IP地址库权限没有加进来, 需要先屏蔽. modify by zhujuwang 2020.06.18
//		if (!user.isAdmin() && !user.isSuperUser()) {
//			Map<String, Set<String>> totalConstraints = resAuthHelper.verifyActionAndGetResource(
//					user, authCtx.getResAction(), authCtx.getFlattenConstraints());
//			// 将参数处理成字符串,添加到请求参数中
//			if (!super.applyGeneralAuthConstraintsWithMap(totalConstraints, params)){
//				return new Result<>();
//			}
//		}
		List<String> permissionList = permissionService.listUserActions(user.getNamespace(), user.getUsername(), null);
		if (!permissionList.contains("res:ipAdmin")) {
			params.put("update_person", user.getUsername());
		}
		return instanceClient.getInstanceListV3(params, moduleType);
	}

	@Override
	public Result<Map<String, Object>> getInstanceOsListV3(@RequestBody Map<String, Object> params,
														   @RequestParam(value = "moduleType", required = false) String moduleType) {
		RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
		if (!authCtx.getUser().isAdmin() && !authCtx.getUser().isSuperUser()) {
			Map<String, Set<String>> totalConstraints = resAuthHelper.verifyActionAndGetResource(
					authCtx.getUser(), authCtx.getResAction(), authCtx.getFlattenConstraints());
			// 将参数处理成字符串,添加到请求参数中
			if (!super.applyGeneralAuthConstraintsWithMap(totalConstraints, params)){
				return new Result<>();
			}
		}
		Result<Map<String, Object>> instanceListV3 = instanceClient.getInstanceOsListV3(params, moduleType);
		return instanceListV3;
	}

	@Override
	public void exportOsListV3(@RequestBody Map<String, Object> params,
							   @RequestParam(value = "moduleType", required = false) String moduleType,
							   HttpServletResponse response) throws Exception {
		RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
		if (!authCtx.getUser().isAdmin() && !authCtx.getUser().isSuperUser()) {
			Map<String, Set<String>> totalConstraints = resAuthHelper.verifyActionAndGetResource(
					authCtx.getUser(), authCtx.getResAction(), authCtx.getFlattenConstraints());
			// 将参数处理成字符串,添加到请求参数中
			if (!super.applyGeneralAuthConstraintsWithMap(totalConstraints, params)){
				throw new RuntimeException("鉴权失败");
			}
		}
		params.put("currentPage", null);
		params.put("pageSize", null);
		Result<Map<String, Object>> instanceListV3 = instanceClient.getInstanceListV3(params, moduleType);
		String[] headerList = {"管理IP","设备名称","创建时间","最后更新时间","os详细版本","POD名称","机房位置","设备分类","设备类型"};
		String[] keyList = {"ip","device_name","insert_time","update_time","os_detail_version","pod_name","roomId","device_class","device_type"};
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		String title = "主机列表_" + dateFormat.format(new Date());
		String fileName = title+".xlsx";
		OutputStream os = response.getOutputStream();// 取得输出流
		response.setHeader("Content-Disposition", "attachment;filename=".concat(String.valueOf(URLEncoder.encode(fileName, "UTF-8"))));
		response.setHeader("Connection", "close");
		response.setHeader("Content-Type", "application/vnd.ms-excel");
		//excel constuct
		ExportExcelUtil eeu = new ExportExcelUtil();
		Workbook book = new SXSSFWorkbook(128);
		eeu.exportExcel(book, 0, title, headerList, instanceListV3.getData(), keyList);
		book.write(os);
	}

	@Override
	@ResAction(action = "view", resType = "cmdb", loadResFilter=true)
	public Map<String, Object> getInstanceDetail(@RequestParam("module_id") String moduleId,
												 @RequestParam(value = "instance_id") String instanceId) {
		return instanceClient.getInstanceDetail(moduleId, instanceId);
	}

	@Override
	public Map<String, Object> addInstance(@RequestBody Map<String, Object> instanceData) {
		RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
		String userName = authCtx.getUser().getUsername();
		return instanceClient.addInstance(userName, instanceData);
	}

	@Override
	public Map<String, Object> updateInstance(@PathVariable("id") String id,
			@RequestBody Map<String, Object> instanceData) {
		RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
		String userName = authCtx.getUser().getUsername();
		return instanceClient.updateInstance(id, userName, instanceData);
	}

	@Override
	public Integer batchUpdateCount( @RequestParam("moduleId") String moduleId,
									 @RequestBody Map<String, Object> batchUpdate) {
		return instanceClient.batchUpdateCount(moduleId, batchUpdate);
	}

	/**
	 * 根据资源池及IP地址查询设备信息
	 * @param params 查询入参
	 * @return
	 */
	@Override
	@ResAction(action = "view", resType = "cmdb", loadResFilter=true)
	public Map<String, Object> queryInstanceDetail(@RequestBody Map<String, Object> params) {
		return instanceClient.queryInstanceDetail(params);
	}

	@Override
	@ResAction(action = "view", resType = "cmdb", loadResFilter=true)
	public Map<String, String> exportInstance(HttpServletResponse response,
											  @RequestBody Map<String, Object> params,
											  @RequestParam(value = "moduleType", required = false) String moduleType) {
		Map<String, String> resultMap = instanceClient.exportInstance(params, moduleType);
		if (resultMap.containsKey("exportType") && ("stream").equals(resultMap.get("exportType")) && resultMap.containsKey("moduleName")) {
			return toHandleStreamExport(response, resultMap.get("moduleName"),params, moduleType);
		}
		return resultMap;
	}
	private Map<String, String> toHandleStreamExport(HttpServletResponse response,String moduleName, Map<String, Object> params, String moduleType ) {
		Map<String, String> returnMap = new HashMap<>();
		try {
			params.put("currentPage", null);
			params.put("pageSize", null);
			// 新增导出数量阈值限制 fanwenhui 20200724
			Integer integer = instanceClient.listV3Count(params, moduleType);
			boolean checkFlag = checkExportLimit(integer);
			if (!checkFlag) {
				returnMap.put("code", "error");
				returnMap.put("message", "导出数量过大，请重新选择");
				response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
				return returnMap;
			}
			List<CmdbSimpleCode> headerList;
			String moduleId = !params.containsKey("module_id") ? "": params.get("module_id").toString();
			headerList = getInstanceHeader(moduleId, moduleType);
			Result<Map<String, Object>> pageResult = instanceClient.getInstanceListV3(params, moduleType);
			List<Map<String, Object>> dataList = pageResult.getData();
			log.info("Export instance data size -> {}", dataList.size());
			Map<String, Map<String, String>> columnMap = pageResult.getColumns();
			final List<String> header = new LinkedList<>();
			final List<String> keys = new LinkedList<>();
			if (headerList != null && headerList.size() > 0) {
				List<String> filterCodeList = Arrays.asList("id", "module_id", "insert_person", "insert_time", "update_person", "update_time");
				headerList.stream().forEach((code) -> {
					// 过滤掉固定的列
					if (!filterCodeList.contains(code.getFiledCode())) {
						Map<String, String> codeInfo = columnMap.get(code.getFiledCode());
						String headerKey = code.getFiledCode();
						if (("ref").equals(codeInfo.get("type"))) {
							headerKey = codeInfo.get("ref_name");
						}
						header.add(codeInfo.get("filed_name"));
						keys.add(headerKey);
					}
				});
				ExportExcelUtil eeu = new ExportExcelUtil();
				Workbook book = new SXSSFWorkbook(128);
				eeu.exportExcel(book, 0, moduleName, header.toArray(new String[header.size()]),
						dataList, keys.toArray(new String[keys.size()]));
				OutputStream os = null;
				try {
					response.setHeader("Content-Disposition", "attachment;filename=".concat(String.valueOf(URLEncoder.encode(moduleName + ".xlsx", "UTF-8"))));
					response.setHeader("Connection", "close");
					response.setHeader("Content-Type", "application/vnd.ms-excel");
					os = response.getOutputStream();// 取得输出流
					book.write(os);
					os.flush();
					os.close();
					returnMap.put("code", "success");
					response.setStatus(HttpStatus.NO_CONTENT.value());
					return returnMap;
				} catch (Exception e) {
					log.error("导出Excel数据失败!", e);
					returnMap.put("code", "error");
					returnMap.put("message", e.getMessage());
					response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
				}
			}
		} catch (Exception e) {
			log.error("导出Excel数据失败!", e);
			returnMap.put("code", "error");
			returnMap.put("message", e.getMessage());
		}
		return returnMap;
	}

	@Override
	public Map<String, Object> deleteInstance(@RequestBody CmdbDeleteInstance deleteInstance) {
		RequestAuthContext authContext = RequestAuthContext.currentRequestAuthContext();
		return instanceClient.deleteInstance(authContext.getUser().getUsername(), deleteInstance);
	}

	@Override
	public List<CmdbInstance> getInstanceByDeviceIds(@RequestBody String deviceIds) {
		return instanceClient.getInstanceByDeviceIds(deviceIds);
	}

	@Override
	public List<Map> getIdcTree() {
		return instanceClient.getIdcTree();
	}

	@Override
	public List<Map> getDeviceClassTree() {
		return instanceClient.getDeviceClassTree();
	}

	@Override
	public List<String> getDepartmentsByIDC(String idcType) {
		return instanceClient.getDepartmentsByIDC(idcType);
	}

	@Override
	public List<Map<String, String>> getIdcByIds(@RequestParam("ids") String ids) {
		return instanceClient.getIdcByIds(ids);
	}

	@Override
	public List<Map<String, String>> getPodByIds(@RequestParam("ids") String ids) {
		return instanceClient.getPodByIds(ids);
	}

	@Override
	public List<Map<String, String>> getRoomByIds(@RequestParam("ids") String ids) {
		return instanceClient.getRoomByIds(ids);
	}

	@Override
	public CmdbInstance queryDeviceByDeviceSn(@RequestParam(value = "deviceSn") String deviceSn,
			@RequestParam(value = "deviceArea") String deviceArea) {
		return instanceClient.queryDeviceByDeviceSn(deviceSn, deviceArea);
	}

	@Override
	public List<CmdbDeviceTypeCount> queryServiceCount(
			@RequestParam(value = "bizSystem", required = false) String bizSystem) {
		List<CmdbDeviceTypeCount> list = instanceClient.queryServiceCount(bizSystem);
		for (CmdbDeviceTypeCount c : list) {
			String cpu = c.getCpuCoreNumber();
			String mem = c.getMemorySize();
			//cpu = "548197.2730194267";
			//mem = "2694298622.7";
			if (StringUtils.isNotBlank(cpu)) {
				if (cpu.contains(".")) {
					double cpuNum = Double.parseDouble(cpu);
					Double cpu1 = Math.round(cpuNum*100)/100.0 ;
					if (cpu1.toString().contains("E")) {
						Double dd = (Double) cpu1;
						BigDecimal bd1 = new BigDecimal(dd);
						cpu = bd1.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString();
						c.setCpuCoreNumber(cpu);
					}else {
						c.setCpuCoreNumber(cpu1+"");
					}
					
				}
				
			}
			if (StringUtils.isNotBlank(mem)) {
					//mem = (Double.parseDouble(mem)/(1024*1024))+"";
				if (mem.contains(".")) {
					double memNum = Double.parseDouble(mem);
					Double mem1 = Math.round(memNum*100)/100.0;
					if (mem1.toString().contains("E")) {
						Double dd = (Double) mem1;
						BigDecimal bd1 = new BigDecimal(dd);
						mem = bd1.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString();
						c.setMemorySize(mem);
					}else {
						c.setMemorySize(mem1+"");
					}
					
				}
			}
			//this.realDataFlag = false;
			if(!this.realDataFlag) {
				if(c.getDeviceType().equals("云主机")) {
					c.setCpuCoreNumber(198692+"");
					c.setMemorySize(1414812+"");
				}else {
					c.setMemorySize(2460384+"");
					c.setCpuCoreNumber(366618+"");
				}
			}else {
				if(null==c.getMemorySize()) {
					c.setMemorySize(0+"");
				}
				if(null==c.getCpuCoreNumber()) {
					c.setCpuCoreNumber(0+"");
				}
			}

		}
		return list;
	}

	@Override
	public Map<String, Object> queryDeviceByIdcTypeAndIP(@RequestBody Map<String, Object> params) {
		return instanceClient.queryDeviceByIdcTypeAndIP(params);
	}

	@Override
	public CmdbInstance queryDeviceByRoomIdAndIP(@RequestParam(value = "idc", required = false) String idc,
												 @RequestParam("deviceIp") String deviceIp) {
		return instanceClient.queryDeviceByRoomIdAndIP(idc, deviceIp);
	}

	@Override
	public CmdbInstance queryDeviceByRoomIdAndIP2(@RequestParam(value = "idc", required = false) String idc,
												 @RequestParam("deviceIp") String deviceIp) {
		return instanceClient.queryDeviceByRoomIdAndIP2(idc, deviceIp);
	}

	@Override
	public List<Map<String, Object>> getInstanceBaseInfo(@RequestBody Map<String, Object> param) {
		return instanceClient.getInstanceBaseInfo(param);
	}

	@Override
	public Integer listV3Count(@RequestBody Map<String, Object> params,
							   @RequestParam(value = "moduleType", required = false) String moduleType) {
		RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
		RequestAuthContext.RequestHeadUser user = authCtx.getUser();
		List<String> permissionList = permissionService.listUserActions(user.getNamespace(), user.getUsername(), null);
		if (!permissionList.contains("res:ipAdmin")) {
			params.put("update_person", user.getUsername());
		}
		return instanceClient.listV3Count(params, moduleType);
	}

	/**
	 * 校验导出数量是否超过限制
	 * @param total 查询到的导出数量
	 * @return true - 允许导出
	 */
	private boolean checkExportLimit(int total) {
		boolean checkFlag = true;
		List<ConfigDict> exportCountLimit = configDictClient.getDictsByType("exportCountLimit", null, null, null);
		if (!exportCountLimit.isEmpty()) {
			ConfigDict configDict = exportCountLimit.get(0);
			String value = configDict.getValue();
			int exportLimit = 5000;
			if (StringUtils.isNotEmpty(value)) {
				exportLimit = Integer.parseInt(value);
			}
			if (total > exportLimit) {
				checkFlag = false;
			}
		}
		return checkFlag;
	}
}
