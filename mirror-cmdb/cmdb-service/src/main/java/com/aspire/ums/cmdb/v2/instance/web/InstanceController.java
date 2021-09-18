package com.aspire.ums.cmdb.v2.instance.web;

import com.aspire.ums.cmdb.client.CmdbApprovalESClient;
import com.aspire.ums.cmdb.code.payload.CmdbSimpleCode;
import com.aspire.ums.cmdb.collectApproval.payload.CmdbCollectApproval;
import com.aspire.ums.cmdb.collectApproval.payload.CmdbCollectApprovalQuery;
import com.aspire.ums.cmdb.common.Result;
import com.aspire.ums.cmdb.deviceStatistic.util.POIModuleUtils;
import com.aspire.ums.cmdb.dict.payload.Dict;
import com.aspire.ums.cmdb.dict.service.ConfigDictService;
import com.aspire.ums.cmdb.ftp.service.FtpService;
import com.aspire.ums.cmdb.helper.JDBCHelper;
import com.aspire.ums.cmdb.instance.IInstanceAPI;
import com.aspire.ums.cmdb.instance.payload.*;
import com.aspire.ums.cmdb.module.payload.Module;
import com.aspire.ums.cmdb.util.StringUtils;
import com.aspire.ums.cmdb.v2.code.service.ICmdbCodeService;
import com.aspire.ums.cmdb.v2.instance.service.ICmdbInstanceService;
import com.aspire.ums.cmdb.v2.module.service.ModuleService;
import com.aspire.ums.cmdb.v2.process.export.service.IProcessService;
import com.aspire.ums.cmdb.v3.redis.service.IRedisService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.io.IOUtils;
import org.apache.ibatis.annotations.Param;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.*;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: InstanceController
 * Author:   zhu.juwang
 * Date:     2019/5/21 14:56
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@RestController
@Slf4j
public class InstanceController implements IInstanceAPI {

    @Autowired
    private ICmdbInstanceService instanceService;
    @Autowired
    private ModuleService moduleService;
    @Autowired
    private FtpService ftpService;
    @Autowired
    private ConfigDictService configDictService;
    @Autowired
    private ICmdbCodeService cmdbCodeService;
    @Autowired
    private JDBCHelper jdbcHelper;
    @Autowired
    private POIModuleUtils poiModuleUtils;
    @Autowired
    private IRedisService redisService;
    @Autowired
    private IProcessService processService;

    @Override
    public List<CmdbSimpleCode> getInstanceHeader(@RequestParam(value = "moduleId",required = false) String moduleId,
                                                  @RequestParam(value = "moduleType", required = false) String moduleType) {
        return instanceService.getInstanceHeader(moduleId, moduleType);
    }

    /**
     * 根据条件获取ci列表
     *  @param params
     *  @return
     */
    @Override
    public Result<Map<String, Object>> getInstanceListV3(@RequestBody Map<String, Object> params,
                                                         @RequestParam(value = "moduleType", required = false) String moduleType) {
        return instanceService.getInstanceList(params, moduleType);
    }

    @Override
    public Result<Map<String, Object>> getAllIPInstance(@RequestBody Map<String, Object> params) {
        return instanceService.getAllIPInstance(params);
    }

    @Autowired
    private CmdbApprovalESClient approvalESClient;
    @Override
    public Result<Map<String, Object>> getInstanceOsListV3(@RequestBody Map<String, Object> params,
                                                           @RequestParam(value = "moduleType", required = false) String moduleType) {
        Result<Map<String, Object>> instanceList = instanceService.getInstanceList(params, moduleType);
        List<Map<String, Object>> data = instanceList.getData();
		for (Map<String, Object> item : data) {
            CmdbCollectApprovalQuery cmdbCollectApprovalQuery = new CmdbCollectApprovalQuery();
			cmdbCollectApprovalQuery.setInstanceId(String.valueOf(item.get("id")));
			cmdbCollectApprovalQuery.setApprovalStatus("1");
			cmdbCollectApprovalQuery.setType("detail");
			cmdbCollectApprovalQuery.setPageNum(1);
			cmdbCollectApprovalQuery.setPageSize(5);
            Map<String, Object> query = new ObjectMapper().convertValue(cmdbCollectApprovalQuery, new TypeReference<Map<String, Object>>() {});
			Map<String, Object> list = approvalESClient.query(query);
			item.put("approvalCount",list.get("count"));
		}
        instanceList.setData(data);
        return instanceList;
    }

    @Override
    public Map<String, Object> getInstanceDetail(@RequestParam("module_id") String moduleId,
                                                 @RequestParam(value = "instance_id") String instanceId) {
        return instanceService.getInstanceDetail(moduleId, instanceId);
    }

    @Override
    public Map<String, Object> addInstance(@RequestParam("username") String username,
                                           @RequestBody Map<String, Object> instanceData) {
        Map<String, Object> map = new HashMap<>();
        try {
            String msg = instanceService.addInstance(username, instanceData, "手动新增");
            map.put("message", msg);
            map.put("success", true);
        } catch (Exception e) {
            log.error("新增失败：", e);
            map.put("success", false);
            map.put("message", "新增失败, error: " + e.getMessage());
        }
        return map;
    }

    @Override
    public Map<String, Object> updateInstance(@PathVariable("id") String id, @RequestParam("username") String username, @RequestBody Map<String, Object> instanceData) {
        Map<String,Object> map = new HashMap<>();
        try {
            log.info("开始更新实例");
            String msg = instanceService.updateInstance(id, username, instanceData,"手动更新");
            map.put("success", true);
            map.put("message", msg);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("更新失败："+e);
            map.put("success", false);
            map.put("message", "更新失败" + e.getMessage());
        }
        return map;
    }

    @Override
    public Map<String, Object> queryInstanceDetail(@RequestBody Map<String, Object> params,
                                                   @RequestParam(value = "moduleType", required = false) String moduleType) {
        if (MapUtils.isEmpty(params)) {
            throw new RuntimeException("Request params is empty. Request failed.");
        }
        return instanceService.queryInstanceDetail(params, moduleType);
    }

    @Override
    public Map<String, Object> queryDeviceByIdcTypeAndIP(@RequestBody Map<String, Object> params) {
        return instanceService.queryDeviceByIdcTypeAndIP(params);
    }

    @Override
    public CmdbInstance queryDeviceByRoomIdAndIP2(
            @RequestParam(value = "idc", required = false) String idc, @RequestParam("deviceIp") String deviceIp) {
        Map<String, Object> param = new HashMap<String, Object>();
        Locale.setDefault(Locale.ENGLISH);
        if (!StringUtils.isEmpty(idc) && !("null").equals(String.valueOf(idc).toLowerCase())) {
            param.put("idcType", idc);
        }
        param.put("ip", deviceIp);
        List<CmdbInstance> clist = instanceService.getInstanceByIp(param);
        if (CollectionUtils.isEmpty(clist)) {
            return null;
        }
        return clist.get(0);
    }

    @Override
    public CmdbInstance queryDeviceByRoomIdAndIP(
            @RequestParam(value = "idc", required = false) String idc, @RequestParam("deviceIp") String deviceIp) {
        Map<String, Object> param = new HashMap<String, Object>();
        Locale.setDefault(Locale.ENGLISH);
        if (!StringUtils.isEmpty(idc) && !("null").equals(String.valueOf(idc).toLowerCase())) {
            param.put("idcType", idc);
        }
        param.put("ip", deviceIp);
        List<CmdbInstance> clist = instanceService.getInstanceByIp(param);
        if (CollectionUtils.isEmpty(clist)) {
            return null;
        }
        return clist.get(0);
    }

    @Override
    public Integer batchUpdateCount(@RequestParam("moduleId") String moduleId,
                                    @RequestBody Map<String, Object> batchUpdate) {
        return instanceService.batchUpdateCount(moduleId, batchUpdate);
    }

    @Override
    public CmdbInstance queryDeviceByDeviceSn(@RequestParam(value = "deviceSn") String deviceSn, @RequestParam(value = "deviceArea") String deviceArea) {
        log.info("Request InstanceController.queryDeviceByDeviceSn deviceSn -> {}", deviceSn);
        try {
            CmdbInstance queryInstance = new CmdbInstance();
            CmdbInstance instanceRs = null;
            queryInstance.setDeviceSn(deviceSn);
            for(String item : deviceArea.split(",")){
                queryInstance.setIdcType(item);
                instanceRs = instanceService.get(queryInstance);
                if(instanceRs != null) {
                    break;
                }
            }
            return instanceRs;
        } catch (Exception e) {
            log.error("Query cmdb_instance entity error.", e);
        }
        return null;
    }

    @Override
    public Map<String, Object> deleteInstance(@RequestParam("userName") String userName,
                                              @RequestBody CmdbDeleteInstance deleteInstance) {
        Map<String,Object> map = new HashMap<>();
        try {
            String msg = instanceService.deleteInstance(userName, deleteInstance.getInstanceList(), "手动删除");
            map.put("success", true);
            map.put("message", msg);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("配置更新失败："+e);
            map.put("success", false);
            map.put("message", "删除失败" + e.getMessage());
        }
        return map;
    }

    @Override
    public List<CmdbInstance> getInstanceByDeviceIds(@RequestBody String deviceIds) {
        List<CmdbInstance> returnList = new LinkedList<>();
        for (String deviceId : deviceIds.split(",")) {
            CmdbInstance queryInstance = new CmdbInstance();
            queryInstance.setId(deviceId);
            CmdbInstance instance = instanceService.get(queryInstance);
            if (instance != null) {
                returnList.add(instance);
            }
        }
        return returnList;
    }

    /**
     * 获取资源池树
     */
    @Override
    public List<Map> getIdcTree() {
        return instanceService.getIdcTree();
    }

    /**
     * 获取设备类型树
     */
    @Override
    public List<Map> getDeviceClassTree() {
        return instanceService.getDeviceClassTree();
    }

    @Override
    public List<String> getDepartmentsByIDC(@Param("idcType") String idcType) {
        return instanceService.getDepartmentsByIDC(idcType);
    }

    @Override
    public List<Map<String, String>> getIdcByIds(@RequestParam("ids") String ids) {
        return instanceService.getIdcByIds(ids);
    }

    @Override
    public List<Map<String, String>> getPodByIds(@RequestParam("ids") String ids) {
        return instanceService.getPodByIds(ids);
    }

    @Override
    public List<Map<String, String>> getRoomByIds(@RequestParam("ids") String ids) {
        return instanceService.getRoomByIds(ids);
    }

    @Override
    public List<CmdbDeviceTypeCount> queryServiceCount(@RequestParam(value = "bizSystem", required = false) String bizSystem) {
        return instanceService.queryServiceCount(bizSystem);
    }

    @Override
    public List<CmdbDeviceTypeCount> queryServiceCountForKG() {
        return instanceService.queryServiceCountForKG();
    }

    @Override
    public List<Map<String,Object>> getNetworkAndSafetyDeivce(@RequestBody CmdbQueryInstance cmdbQueryInstance) {
        return instanceService.getNetworkAndSafetyDeivce(cmdbQueryInstance);
    }

    @Override
    public Map<String, String> exportInstance(@RequestBody Map<String, Object> params,
                                              @RequestParam(value = "moduleType", required = false) String moduleType) {
        Map<String, String> returnMap = new HashMap<>();
        Long startData = new Date().getTime();
        try {
            String moduleName;

            String moduleId = "";
            Module module;
            // modify by zhujuwang 2020-11-27 取消moduleType方式. 要求必须传入module_id作为统一的模型获取标识
            try {
                if (params.containsKey("module_id") && StringUtils.isNotEmpty(params.get("module_id"))) {
                    module = moduleService.getModuleDetail(params.get("module_id").toString());
                } else {
                    module = moduleService.getDefaultModule(moduleType);
                }
            } catch (Exception e) {
                returnMap.put("code", "error");
                returnMap.put("message", e.getMessage());
                return returnMap;
            }
            moduleId = module.getId();
            moduleName = module.getName();
            if (("stream").equals(instanceService.getDownloadType())) {
                returnMap.put("exportType", instanceService.getDownloadType());
                returnMap.put("moduleName", moduleName);
                return returnMap;
            }
//            params.put("module_id", moduleId);
            return processService.exportInstance(params, moduleId);
        } catch (Exception e) {
            log.error("导出Excel数据失败!", e);
            returnMap.put("code", "error");
            returnMap.put("message", e.getMessage());
        }
        return returnMap;
    }


    private Map<String, String> toHandleStreamExport(HttpServletResponse response,String fileName, Workbook book, Map<String, String> returnMap) {
        OutputStream os = null;
        try {
            response.setHeader("Content-Disposition", "attachment;filename=".concat(String.valueOf(URLEncoder.encode(fileName, "UTF-8"))));
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
        } finally {
            IOUtils.closeQuietly(book);
            IOUtils.closeQuietly(os);
        }
        return returnMap;
    }

    @Override
    public List<CmdbDeviceTypeByConditonCount> queryDeviceCountByIdctype(@RequestParam(value = "idcType", required = false)String idcType
    		,@RequestParam(value = "deviceType", required = false)String deviceType,
    		@RequestParam(value = "startTime", required = false)String startTime
    		,@RequestParam(value = "endTime", required = false)String endTime) {
        return instanceService.queryDeviceCountByIdctype(idcType, deviceType, startTime, endTime);
    }

    @Override
    public Map<String, Map<String, Map<String, Integer>>> filterEmptyCiItem(@RequestParam("ciItem") String ciItem) {
        return instanceService.filterEmptyCiItem(ciItem);
    }

    @Override
    public List<CmdbDeviceTypeByConditonCount> queryDeviceCountByBizsystem(@RequestParam(value = "bizSystem", required = false)String bizSystem,
    		@RequestParam(value = "idcType", required = false)String idcType,
    		@RequestParam(value = "deviceType", required = false)String deviceType,
    		@RequestParam(value = "startTime", required = false)String startTime,
    		@RequestParam(value = "endTime", required = false)String endTime,
    		@RequestParam(value = "sourceType", required = false)String sourceType) {
        return instanceService.queryDeviceCountByBizsystem(bizSystem, idcType, deviceType, startTime, endTime, sourceType);
    }

    @Override
    public List<Map<String, Object>> getInstanceBaseInfo(@RequestBody Map<String, Object>param) {
        return instanceService.getInstanceBaseInfo(param);
    }


    @Override
    public List<Map<String, Object>> deviceCountByDeviceClass(@RequestParam("deviceClass") String deviceClass) {
        return instanceService.deviceCountByDeviceClass(deviceClass);
    }

    @Override
    public Map<String, Object> deviceCountByDeviceType(@RequestParam("deviceClass") String deviceClass,
                                                        @RequestParam(value = "deviceType",required = false) String deviceType) {
        return instanceService.deviceCountByDeviceType(deviceClass, deviceType);
    }

    @Override
    public Object getBlockSize() {
        return instanceService.getBlockSize();
    }

    @Override
    public Integer listV3Count(@RequestBody Map<String, Object> params,
            @RequestParam(value = "moduleType", required = false) String moduleType) {
        return instanceService.listV3Count(params, moduleType);
    }

    @Override
    public Map<String, Object> addInstanceNoApprove(@RequestParam("userName") String username,
            @RequestBody Map<String, Object> instanceData, @RequestParam("operateType") String operateType) {
        Map<String, Object> map = new HashMap<>();
        try {
            String msg = instanceService.addInstanceNoApprove(username, instanceData, operateType);
            map.put("message", msg);
            map.put("success", true);
        } catch (Exception e) {
            log.error("新增失败：", e);
            map.put("success", false);
            map.put("message", "新增失败, error: " + e.getMessage());
        }
        return map;
    }

    @Override
    public Map<String, Object> deleteInstanceNoApprove(@RequestParam("userName") String userName,
            @RequestBody List<Map<String, Object>> instanceList, @RequestParam("operateType") String operateType) {
        Map<String, Object> map = new HashMap<>();
        try {
            String msg = instanceService.deleteInstanceNoApprove(userName, instanceList, operateType);
            map.put("message", msg);
            map.put("success", true);
        } catch (Exception e) {
            log.error("新增失败：", e);
            map.put("success", false);
            map.put("message", "新增失败, error: " + e.getMessage());
        }
        return map;
    }
}
