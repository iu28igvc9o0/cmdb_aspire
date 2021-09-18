package com.migu.tsg.microservice.atomicservice.composite.controller.desk;

import com.aspire.mirror.composite.service.desk.DeskStaffAPI;
import com.aspire.mirror.composite.service.desk.bpm.payload.CheckInReq;
import com.aspire.mirror.composite.service.desk.bpm.payload.DeskLogs;
import com.aspire.mirror.composite.service.desk.bpm.payload.PublicNoticeVo;
import com.aspire.mirror.composite.service.desk.bpm.payload.ServiceDeskWithZxgllcVo;
import com.aspire.ums.cmdb.common.ResultVo;
import com.fasterxml.jackson.databind.JsonNode;
import com.migu.tsg.microservice.atomicservice.composite.config.OrderConfig;
import com.migu.tsg.microservice.atomicservice.composite.controller.authcontext.DeskLogsAnnotation;
import com.migu.tsg.microservice.atomicservice.composite.controller.authcontext.RequestAuthContext;
import com.migu.tsg.microservice.atomicservice.composite.controller.util.BeanUtils;
import com.migu.tsg.microservice.atomicservice.composite.controller.util.ExportExcelUtil;
import com.migu.tsg.microservice.atomicservice.composite.controller.util.JsonUtil2;
import com.migu.tsg.microservice.atomicservice.composite.controller.util.WebUtil;
import com.migu.tsg.microservice.atomicservice.composite.controller.util.es.util.DateUtils;
import com.migu.tsg.microservice.atomicservice.composite.helper.BpmCallHelper;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @projectName: DeskStaffAPI
 * @description: 接口
 * @author: menglinjie
 * @create: 2020-10-28 10:37
 **/
@RestController
public class DeskStaffController implements DeskStaffAPI {
	
	@Autowired
	OrderConfig orderConfig;
	
	@Autowired
	BpmCallHelper bpmCallHelper;

	@Value("${spring.application.name}")
	public String moduleType;

	@Value("${deskLogs.status:enable}")
	protected String deskLogsStatus;

	@Override
	@DeskLogsAnnotation(value = "根据流程key获取最新的流程定义id")
	public Object getBpmDefId(@RequestParam String defKey) throws Exception {
		Map<String,Object> params = new HashMap<>();
		params.put("defKey",defKey);
		ResponseEntity callUrl = (ResponseEntity)bpmCallHelper.getCallUrl(orderConfig.getPublicNoticeCreateUrl(), params);
		Map<String,String> mp = new HashMap<>();
		mp.put("defId",String.valueOf(callUrl.getBody()));
		return ResultVo.success(mp);
	}

	@Override
    @DeskLogsAnnotation(value = "查询公告列表",httpMethod = "POST")
	public Object findList(@RequestBody PublicNoticeVo vo) throws Exception {
		return bpmCallHelper.postCallUrl(orderConfig.getPublicNoticeFindListUrl(), vo);
	}

	@Override
    @DeskLogsAnnotation(value = "公告通知操作",OpType = "Add")
	public Object sendNotice(@PathVariable("id") String id) throws Exception {
		String url = orderConfig.getPublicNoticeNoticeUrl() + "/" + id;
		return bpmCallHelper.getCallUrl(url,new HashMap<>());
	}

	@Override
    @DeskLogsAnnotation(value = "公告类型下拉框")
	public Object getNoticeTypeList() {
		List<Map<String,String>> noticeTypes = new ArrayList<>();
		String[] types = new String[]{"普通公告","割接公告","新闻公告"};
		for(String item : types) {
			Map<String,String> tmp = new HashMap<>();
			tmp.put("name",item);
			tmp.put("value",item);
			noticeTypes.add(tmp);
		}
		return ResultVo.success(noticeTypes);
	}

	@Override
	@DeskLogsAnnotation(value = "首页查询公告列表",httpMethod = "POST")
	public Object findListHomePage(@RequestBody PublicNoticeVo vo) throws Exception {
		return bpmCallHelper.postCallUrl(orderConfig.getPublicNoticeFindListHomePageUrl(), vo);
	}

    @Override
    @DeskLogsAnnotation(value = "值班签到签退",OpType = "Save",httpMethod = "POST")
	public Object dutyCheckIn(@RequestBody CheckInReq req) {
		return bpmCallHelper.postCallUrl(orderConfig.getDutyCheckInUrl(), req);
	}

	@Override
	public Object saveDeskLogs(@RequestBody DeskLogs req) {
		String account = "test";
		RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
		account = authCtx.getUser().getUsername();
		HttpServletRequest request = this.getRequest();
		Date date = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateString = formatter.format(date);
		req.setLogTime(dateString);
		req.setCreateTime(date);
		req.setApp("服务台");
		req.setSip(WebUtil.getIpAddr(request));
		req.setSubUser(account);
		req.setAppModule(moduleType);
		if ("enable".equals(deskLogsStatus)){
			return bpmCallHelper.postCallUrl(orderConfig.getDeskLogsUrl(), req);
		}else {
			Map<String,Object> map = new HashMap<>();
			map.put("state",true);
			map.put("message","未开启系统操作日志保存开关");
			return map;
		}

	}

	@Override
    @DeskLogsAnnotation(value = "公告列表_操作状态变更",OpType = "Update",httpMethod = "POST")
	public Object updateOperateStatus(@RequestBody Map<String,List<String>> mp,
									  @PathVariable("status") String status) throws Exception {
		String url = orderConfig.getPublicNoticeUpdateStatusUrl() + "/" + status;
		return bpmCallHelper.postCallUrl(url,mp);
	}

	@Override
    @DeskLogsAnnotation(value = "服务事件工单-启动流程",OpType = "Add",httpMethod = "POST")
    public Object startWithZxbzdx(@RequestBody ServiceDeskWithZxgllcVo vo) throws Exception {
		return bpmCallHelper.postCallUrl(orderConfig.getStartWithZxgllcUrl(),vo);
	}

	@Override
    @DeskLogsAnnotation(value = "服务事件工单-保存草稿",OpType = "Add",httpMethod = "POST")
	public Object startDraftWithZxbzdx(@RequestBody ServiceDeskWithZxgllcVo vo) throws Exception {
		return bpmCallHelper.postCallUrl(orderConfig.getStartDraftWithzxgllc(),vo);
	}

	@Override
    @DeskLogsAnnotation(value = "统计服务事件工单相关数量")
	public Object statistWithZxbzdx() throws Exception {
		return bpmCallHelper.getCallUrl(orderConfig.getStatistWithZxgllcUrl(),new HashMap<>());
	}

	@Override
    @DeskLogsAnnotation(value = "服务事件工单列表",httpMethod = "POST")
	public Object listWithZxbzdx(@RequestBody Map<String,Object> req) throws Exception {
		return bpmCallHelper.postCallUrl(orderConfig.getListWithZxgllcUrl(),req);
	}

	@Override
    @DeskLogsAnnotation(value = "查询流程实例列表",httpMethod = "POST")
	public Object getInstDetailListForDesk(@RequestBody PublicNoticeVo vo) throws Exception {
		vo.setPage(vo.getPageNo());
		vo.setSubjet(vo.getSubject());
		return bpmCallHelper.postCallUrl(orderConfig.getGetInstDetailListForDeskUrl(), vo);
	}

	@Override
    @DeskLogsAnnotation(value = "催办工单列表",httpMethod = "POST")
	public Object listWithRemind(@RequestBody Map<String, Object> req){
		return bpmCallHelper.postCallUrl(orderConfig.getRemindListUrl(), req);
	}

	@Override
    @DeskLogsAnnotation(value = "服务事件工单的下拉框数据")
	public Object listDropDataWithZxbzdx(@RequestParam("aliasName") String aliasName,
										 @RequestParam("pageNo") Integer pageNo,
										 @RequestParam("pageSize") Integer pageSize) {
		Map<String,Object> params = new HashMap<>();
		params.put("aliasName",aliasName);
		params.put("pageNo",pageNo);params.put("pageSize",pageSize);
		return bpmCallHelper.getCallUrl(orderConfig.getDropDataWithZxgllcUrl(),params);
	}

	@Override
    @DeskLogsAnnotation(value = "服务事件工单的下拉框数据(针对部门信息)")
	public Object listDropDataToDepWithZxbzdx(@RequestParam("parentId") String parentId) {
		Map<String,Object> params = new HashMap<>();
		params.put("parentId",parentId);
		return bpmCallHelper.getCallUrl(orderConfig.getDropDataToDepWithZxgllcUrl(),params);
	}

	@Override
    @DeskLogsAnnotation(value = "服务事件工单的下拉框数据(针对用户信息)")
	public Object listDropToUserWithZxbzdx(@RequestParam(value = "search",required = false) String search,
										   @RequestParam(value = "pageNo",required = false) Integer pageNo,
										   @RequestParam(value = "pageSize",required = false) Integer pageSize) {
		Map<String,Object> params = new HashMap<>();
		params.put("search", search);params.put("pageNo",pageNo);params.put("pageSize",pageSize);
		return bpmCallHelper.getCallUrl(orderConfig.getDropDataToUserWithZxgllcUrl(),params);
	}

	@Override
    @DeskLogsAnnotation(value = "导出公告列表数据",httpMethod = "POST")
	public void exportNoticeList(@RequestBody PublicNoticeVo vo) throws Exception {
		ResponseEntity o = (ResponseEntity) bpmCallHelper.postCallUrl(orderConfig.getPublicNoticeFindListUrl(), vo);
		Map<String, Object> mapData = (Map<String, Object>) o.getBody();
		List<Map<String, Object>> dataList = (List<Map<String, Object>>) mapData.get("result");
		for(Map<String, Object> item : dataList) {
			item.put("startTime", DateUtils.timeStamp2Date(item.get("startTime").toString()));
			item.put("endTime", DateUtils.timeStamp2Date(item.get("endTime").toString()));
		}
		String[] headerList = new String[]{"主题","状态","公告范围","生效日期","失效日期","通知内容","工单号","公告类型","操作状态"};
		String[] keyList = new String[]{"subject","status","range","startTime","endTime","content","instId","noticeType","operateStatus"};
		String title = "notice_list";
		localExport(title,headerList,dataList,keyList);
	}

	@Override
    @DeskLogsAnnotation(value = "催办工单列表数据导出",httpMethod = "POST")
	public void exportRemindList(@RequestBody Map<String, Object> req) {
		ResponseEntity o = (ResponseEntity) bpmCallHelper.postCallUrl(orderConfig.getExportRemindListUrl(), req);
		List<Map<String, Object>> dataList = (List<Map<String, Object>>) o.getBody();
		String[] headerList = null;
		String[] keyList = null;
		String title = null;
		if("gjcllc".equals(req.get("defKey"))) {
			headerList = new String[]{"工单号","告警等级","设备IP","资源池","告警分类","告警时间","告警内容","审批意见","当前处理人","当前环节"};
			keyList = new String[]{"id","alertLevel","sourceIP","zyc","sbfl","starttime","gjms","opinion","qualfiedNames","taskName"};
			title = "gjcllc";
		} else if("zxgllc".equals(req.get("defKey"))) {
			headerList = new String[]{"工单号","请求描述","请求分类","请求子类","客户名称","联系电话","请求来源","资源池","邮箱","一级部门","二级部门","请求等级","审批意见","当前处理人","当前环节"};
			keyList = new String[]{"id","reqDesc","reqType","reqSubType","customerName","concatPhone","reqSource","resourcePool","concatMail","yjbm","ejbm","reqLevel","opinion","qualfiedNames","taskName"};
			title = "zxgllc";
		} else if("fwqqlc".equals(req.get("defKey"))) {
			headerList = new String[]{"工单号","流程名称","流程标题","资源池","发起时间","审批意见","当前处理人","当前环节"};
			keyList = new String[]{"id","procDefName","subject","resourcePool","createTime","opinion","qualfiedNames","taskName"};
			title = "fwqqlc";
		}
		localExport(title,headerList,dataList,keyList);
	}

	private void localExport(String sheetTitle,String[] headers, List<Map<String, Object>> result, String[] keys) {
		ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		HttpServletResponse response = servletRequestAttributes.getResponse();
		OutputStream os = null;
		String fileName = sheetTitle + ".xlsx";
		try {
			os = response.getOutputStream();// 取得输出流
			response.setHeader("Content-Disposition", "attachment;filename=".concat(String.valueOf(URLEncoder.encode(fileName, "UTF-8"))));
			response.setHeader("Connection", "close");
			response.setHeader("Content-Type", "application/vnd.ms-excel");
			//excel constuct
			ExportExcelUtil eeu = new ExportExcelUtil();
			Workbook book = new SXSSFWorkbook(128);
			eeu.exportExcel(book, 0, sheetTitle, headers, result, keys);
			book.write(os);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		} finally {
			if(null != os) {
				try {
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	public HttpServletRequest getRequest() {
		RequestAttributes requestAttributes = null;
		try{
			requestAttributes = RequestContextHolder.currentRequestAttributes();
		}catch (IllegalStateException e){
			return null;
		}
		return ((ServletRequestAttributes) requestAttributes).getRequest();
	}

	/**
	 * 判断字符串非空
	 *
	 * @param str
	 * @return
	 */
	public boolean isNotEmpty(String str) {
		return !isEmpty(str);
	}

	/**
	 * 判断字符串是否为空
	 *
	 * @param str
	 * @return
	 */
	public boolean isEmpty(String str) {
		if (str == null){
			return true;
		}
		if (str.trim().equals("")){
			return true;
		}
		return false;
	}
}
