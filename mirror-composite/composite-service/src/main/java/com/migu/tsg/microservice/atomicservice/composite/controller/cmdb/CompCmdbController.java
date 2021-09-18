package com.migu.tsg.microservice.atomicservice.composite.controller.cmdb;

import com.aspire.mirror.composite.service.cmdb.ICompCmdbService;
import com.aspire.mirror.composite.service.inspection.payload.CompDeviceDetailResponse;
import com.aspire.mirror.composite.service.inspection.payload.CompDeviceListResponse;
import com.aspire.mirror.composite.service.inspection.payload.CompDevicePageRequest;
import com.aspire.mirror.template.api.dto.TemplateObjectDetailResponse;
import com.aspire.ums.cmdb.common.Result;
import com.aspire.ums.cmdb.instance.payload.CmdbQueryInstance;
import com.migu.tsg.microservice.atomicservice.composite.Constants;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb.CmdbServiceClient;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.template.TemplateObjectServiceClient;
import com.migu.tsg.microservice.atomicservice.composite.controller.CommonResourceController;
import com.migu.tsg.microservice.atomicservice.composite.helper.CmdbHelper;
import org.assertj.core.util.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * cmdb服务控制层
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.migu.tsg.microservice.atomicservice.composite.controller.cmdb
 * 类名称:    CompCmdbController.java
 * 类描述:    cmdb服务控制层
 * 创建人:    JinSu
 * 创建时间:  2018/9/20 10:17
 * 版本:      v1.0
 */
@RestController
public class CompCmdbController extends CommonResourceController implements ICompCmdbService {

    private Logger logger = LoggerFactory.getLogger(CompCmdbController.class);

    @Autowired
    private CmdbServiceClient cmdbService;

    @Autowired
    private CmdbHelper cmdbHelper;

    @Autowired
    private TemplateObjectServiceClient templateObjectService;

    /**
     * 获取设备列表
     *
     * @param devicePageRequest 获取设备列表请求
     * @return 返回结果
     */
    @Override
    public CompDeviceListResponse deviceList(@RequestBody final CompDevicePageRequest devicePageRequest) {
//        RestTemplate template = new RestTemplate();
//        ResponseEntity<Object> response = template.postForEntity(devceListUrl, devicePageRequest, Object.class);
//        if (response.getStatusCode().value() == 200) {
//            Object obj = response.getBody();
//
//            Map<String, Object> map = jacksonBaseParse(Map.class, obj);
//            CompDeviceListResponse compDeviceListResponse = new CompDeviceListResponse();
//            List<CompDeviceDetailResponse> dataList = Lists.newArrayList();
//            List<Map> mapList = (List<Map>) map.get("dataList");
//            for (Map m : mapList) {
//                CompDeviceDetailResponse compDeviceDetailResponse = jacksonBaseParse(CompDeviceDetailResponse
// .class, m);
//                dataList.add(compDeviceDetailResponse);
//            }
//            compDeviceListResponse.setDataList(dataList);
//            compDeviceListResponse.setTotal((Integer) map.get("total"));
//            logger.debug("method[deivceList] response,now{}", System.currentTimeMillis());
//            return compDeviceListResponse;
//        }
        if (devicePageRequest != null && !StringUtils.isEmpty(devicePageRequest.getTemplateId())) {
            List<TemplateObjectDetailResponse> listTemplateObject = templateObjectService.findByTemplateId
                    (devicePageRequest.getTemplateId());
            List<String> deviceIds = Lists.newArrayList();
            for (TemplateObjectDetailResponse templateObjectDetailResponse : listTemplateObject) {
                if (templateObjectDetailResponse.getObjectType().equals(Constants.Template.DEVICE_TYPE)) {
                    deviceIds.add(templateObjectDetailResponse.getObjectId());
                }
            }
            if (!CollectionUtils.isEmpty(deviceIds)) {
                devicePageRequest.setInstanceList(deviceIds);
            }
        }
        CmdbQueryInstance queryInstance = new CmdbQueryInstance();
        BeanUtils.copyProperties(devicePageRequest, queryInstance);
        queryInstance.setRoom(devicePageRequest.getRoomId());
        queryInstance.setDeviceName(devicePageRequest.getName());
        queryInstance.setQueryType("module");
        queryInstance.setIp(devicePageRequest.getIp());
        CompDeviceListResponse compDeviceListResponse = new CompDeviceListResponse();
        Result<Map<String, Object>> result = cmdbService.listDeviceDetails(queryInstance);
        compDeviceListResponse.setTotal(result.getTotalSize());
        List<CompDeviceDetailResponse> listDevice = Lists.newArrayList();
        for (Map<String, Object> map : result.getData()) {
            CompDeviceDetailResponse compDeviceDetailResponse = new CompDeviceDetailResponse();
            compDeviceDetailResponse.setInstanceId((String) map.get("id"));
            compDeviceDetailResponse.setIp((String)map.get("ip"));
            compDeviceDetailResponse.setModuleId((String)map.get("module_id"));
            compDeviceDetailResponse.setBizSystem((String)map.get("bizSystem"));
            compDeviceDetailResponse.setRoomId((String)map.get("roomId"));
            compDeviceDetailResponse.setRoomName((String)map.get("roomId"));
            compDeviceDetailResponse.setModuleName((String)map.get("device_class"));
            compDeviceDetailResponse.setBizSystemName((String)map.get("bizSystem"));
            compDeviceDetailResponse.setName((String)map.get("device_name"));
            listDevice.add(compDeviceDetailResponse);
        }
        compDeviceListResponse.setDataList(listDevice);
        return compDeviceListResponse;
    }

    /**
     * 获取设备模型
     *
     * @return
     */
    @Override
    public Object getModuleTree() {
        return cmdbService.getModuleTree();
    }

    /**
     * 获取机房列表
     *
     * @return
     */
    @Override
    public Object listRoom() {
//        Map request = Maps.newHashMap();
//        //设置查询条件为公共模型
//        request.put("id", "0");
//        List<Map> listRoom = Lists.newArrayList();
//        Map<String, Object> form = cmdbService.getForms(request);
//        List<Map> formList = (List<Map>) form.get("formData");
//        if (!CollectionUtils.isEmpty(formList)) {
//            for (Map map : formList) {
//                if (map.get("code").equals("roomId")) {
//                    listRoom = (List<Map>) map.get("formOptions");
//                }
//            }
//        }
        return cmdbHelper.listRoom();
    }

    /**
     * 获取业务系统列表
     *
     * @return
     */
    @Override
    public Object listBizSys() {
//        Map request = Maps.newHashMap();
//        //设置查询条件为公共模型
//        request.put("id", "0");
//        List<Map> listBizSys = Lists.newArrayList();
//        Map<String, Object> form = cmdbService.getForms(request);
//        List<Map> formList = (List<Map>) form.get("formData");
//        if (!CollectionUtils.isEmpty(formList)) {
//            for (Map map : formList) {
//                if (map.get("code").equals("bizSystem")) {
//                    listBizSys = (List<Map>) map.get("formOptions");
//                }
//            }
//        }
        return cmdbHelper.listBizSys();
    }

    /**
     * 设备查询条件获取
     */
//    @Override
//    public Object deivceSelectConditionList() {
////        RestTemplate template = new RestTemplate();
////        ResponseEntity<Object> response = template.getForEntity(moduleTreeUrl, Object.class);
//        List<Map> listModule = cmdbService.getModuleTree();
////        if (response.getStatusCode().value() == 200) {
////            listModule = (List<Map>) response.getBody();
////        }
//
//        Map request = Maps.newHashMap();
//        //设置查询条件为公共模型
//        request.put("id", "0");
////        ResponseEntity<Object> responseForm = template.postForEntity(formUrl, request, Object.class);
////        Map<String,Object> listForm = Lists.newArrayList();
//        List<Map> listRoom = Lists.newArrayList();
////        if (response.getStatusCode().value() == 200) {
//        Map<String, Object> form = cmdbService.getForms(request);
//        List<Map> formList = (List<Map>) form.get("formData");
//        if (!CollectionUtils.isEmpty(formList)) {
//            for (Map map : formList) {
//                if (map.get("code").equals("roomId")) {
//                    listRoom = (List<Map>) map.get("formOptions");
//                }
//            }
//
//        }
////        }
//        Map result = Maps.newHashMap();
//        result.put("listModule", listModule);
//        result.put("listRoom", listRoom);
//        return result;
//    }
}
