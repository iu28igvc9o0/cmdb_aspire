package com.aspire.mirror.alert.server.controller.third;

import com.aspire.mirror.alert.api.dto.third.CommonResp;
import com.aspire.mirror.alert.api.dto.third.ThirdCreateAlertReq;
import com.aspire.mirror.alert.api.service.third.ThirdAlertService;
import com.aspire.mirror.alert.server.biz.helper.CmdbHelper;
import com.aspire.mirror.alert.server.util.StringUtils;
import com.aspire.mirror.alert.server.biz.helper.AlertsHandleV2Helper;
import com.aspire.mirror.alert.server.vo.alert.AlertsV2Vo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

@RestController
@Slf4j
public class ThirdAlertController implements ThirdAlertService {

    @Autowired
    private AlertsHandleV2Helper alertHandleHelper;
    @Autowired
    private CmdbHelper cmdbHelper;

    /**
     * 告警上报（第三方）
     *
     * @param thirdCreateAlertReq
     * @return
     */
    public CommonResp createdAlert(@RequestBody ThirdCreateAlertReq thirdCreateAlertReq) {
        CommonResp result = new CommonResp();

        AlertsV2Vo dto = new AlertsV2Vo();
        dto.setRAlertId(thirdCreateAlertReq.getAlertId());
        dto.setAlertLevel(thirdCreateAlertReq.getAlertLevel());
        if (StringUtils.isEmpty(dto.getAlertLevel())) {
            log.error("cancel insert alert of the null AlertLevel: {} ", thirdCreateAlertReq.toString());
            result.setCode("0002");
            result.setMessage("the required parameter device_level is blank.");
            return result;
        }
        dto.setMoniIndex(thirdCreateAlertReq.getMonitorIndex());
        if (StringUtils.isEmpty(dto.getMoniIndex())) {
            log.error("cancel insert alert of the null MonitorIndex: {} ", thirdCreateAlertReq.toString());
            result.setCode("0002");
            result.setMessage("the required parameter monitor_index is blank.");
            return result;
        }
        dto.setItemKey(thirdCreateAlertReq.getItemKey());
        dto.setKeyComment(thirdCreateAlertReq.getKeyComment());
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            if (StringUtils.isNotEmpty(thirdCreateAlertReq.getCurMoniTime())) {
                dto.setCurMoniTime(format.parse(thirdCreateAlertReq.getCurMoniTime()));
            }
        } catch (ParseException e) {
            log.error("tranform CurMoniTime error: {}", thirdCreateAlertReq.getCurMoniTime());
            result.setCode("0001");
            result.setMessage("incorrect time format");
            return result;
        }
        try {
            if (StringUtils.isNotEmpty(thirdCreateAlertReq.getAlertStartTime())) {
                dto.setAlertStartTime(format.parse(thirdCreateAlertReq.getAlertStartTime()));
            }
        } catch (ParseException e) {
            log.error("tranform AlertStartTime error: {}", thirdCreateAlertReq.getAlertStartTime());
            result.setCode("0001");
            result.setMessage("incorrect time format");
            return result;
        }
        dto.setCurMoniValue(thirdCreateAlertReq.getCurMoniValue());
        dto.setDeviceIp(thirdCreateAlertReq.getDeviceIP());
        dto.setBizSys(thirdCreateAlertReq.getServSystem());
        dto.setAlertType(thirdCreateAlertReq.getMoniResult());
        dto.setItemId(thirdCreateAlertReq.getItemId());        // 后续均为保存微服务中定义的itemid
        dto.setMoniObject(thirdCreateAlertReq.getMonitorObject());
        dto.setSource(thirdCreateAlertReq.getSource());
        dto.setRemark(thirdCreateAlertReq.getAlertDesc());
        String idcType = cmdbHelper.getIdc(thirdCreateAlertReq.getBusinessSystem());
        dto.setIdcType(idcType);
        if (StringUtils.isNotEmpty(thirdCreateAlertReq.getObjectType())) {
            dto.setObjectType(thirdCreateAlertReq.getObjectType());
        } else {
            dto.setObjectType(AlertsV2Vo.OBJECT_TYPE_DEVICE);
        }

//        ObjectMapper objectMapper = new ObjectMapper();
//        String jsonString = "{}";
//        try {
//            jsonString = objectMapper.writeValueAsString(dto);
//        } catch (JsonProcessingException e) {
//        }
//        JSONObject alertJson = JSONObject.parseObject(jsonString);
//        List<AlertFieldRequestDTO> alertFieldList = alertHandleHelper.getModelField(AlertConfigConstants.REDIS_MODEL_ALERT);
//        Map<String, Object> ext = thirdCreateAlertReq.getExt();
//        cmdbHelper.doExt (alertJson, alertFieldList, ext);
//        if (dto.getObjectType().equals(AlertsDTOV2.OBJECT_TYPE_BIZ)) {
//            if (StringUtils.isEmpty(dto.getMoniObject())) {
//                dto.setMoniObject("Application");
//            }
//            if (StringUtils.isEmpty(dto.getBizSys())) {
//                log.error("cancel insert alert of the null ServSystem: {} ", thirdCreateAlertReq.toString());
//                result.setCode("0002");
//                result.setMessage("the required parameter biz_sys is blank.");
//                return result;
//            }
//        } else {
//            //处理cmdb数据
//            if (StringUtils.isNotEmpty(dto.getDeviceIp()) && CollectionUtils.isEmpty(ext)){
//                // 根据  机房 + IP, 查找设备
//                cmdbHelper.queryDeviceForAlertV2(alertJson, alertFieldList,alertHandleHelper.getModelField(AlertConfigConstants.REDIS_MODEL_DEVICE_INSTANCE));
//            } else {
//                log.error("cancel insert alert of the null deviceIp: {} ", thirdCreateAlertReq.toString());
//                result.setCode("0002");
//                result.setMessage("the required parameter device_ip is blank.");
//                return result;
//            }
//        }

        try {
            if (alertHandleHelper.check(dto)) {
                alertHandleHelper.handleAlert(dto);
            } else {
                result.setCode("0002");
                result.setMessage("the alert is not legal.");
                return result;
            }

        } catch (Exception e) {
            log.error("error {}", e);
            result.setCode("9999");
            result.setMessage(e.getMessage());
            return result;
        }

        return result;
    }


    /**
     * 批量告警上报（第三方）
     *
     * @param thirdCreateAlertReqList
     * @return
     */
    public CommonResp createdAlerts(@RequestBody List<ThirdCreateAlertReq> thirdCreateAlertReqList) {
        for (ThirdCreateAlertReq thirdCreateAlertReq :
                thirdCreateAlertReqList) {
            AlertsV2Vo dto = new AlertsV2Vo();
            dto.setRAlertId(thirdCreateAlertReq.getAlertId());
            dto.setAlertLevel(thirdCreateAlertReq.getAlertLevel());

            DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                if (StringUtils.isNotEmpty(thirdCreateAlertReq.getCurMoniTime())) {
                    dto.setCurMoniTime(format.parse(thirdCreateAlertReq.getCurMoniTime()));
                }
            } catch (ParseException e) {
                log.error("tranform CurMoniTime error: {}", thirdCreateAlertReq.getCurMoniTime());
            }
            try {
                if (StringUtils.isNotEmpty(thirdCreateAlertReq.getAlertStartTime())) {
                    dto.setAlertStartTime(format.parse(thirdCreateAlertReq.getAlertStartTime()));
                }
            } catch (ParseException e) {
                log.error("tranform AlertStartTime error: {}", thirdCreateAlertReq.getAlertStartTime());
            }
            dto.setCurMoniValue(thirdCreateAlertReq.getCurMoniValue());
            dto.setDeviceIp(thirdCreateAlertReq.getDeviceIP());
            dto.setBizSys(thirdCreateAlertReq.getServSystem());
            dto.setAlertType(thirdCreateAlertReq.getMoniResult());
            dto.setItemId(thirdCreateAlertReq.getItemId());        // 后续均为保存微服务中定义的itemid
            dto.setMoniIndex(thirdCreateAlertReq.getMonitorIndex());
            dto.setItemKey(thirdCreateAlertReq.getItemKey());
            dto.setKeyComment(thirdCreateAlertReq.getKeyComment());
            dto.setMoniObject(thirdCreateAlertReq.getMonitorObject());
            dto.setSource(thirdCreateAlertReq.getSource());
            dto.setRemark(thirdCreateAlertReq.getAlertDesc());
            String idcType = cmdbHelper.getIdc(thirdCreateAlertReq.getBusinessSystem());
            dto.setIdcType(idcType);
            if (StringUtils.isNotEmpty(thirdCreateAlertReq.getObjectType())) {
                dto.setObjectType(thirdCreateAlertReq.getObjectType());
            } else {
                dto.setObjectType(AlertsV2Vo.OBJECT_TYPE_DEVICE);
            }

//            ObjectMapper objectMapper = new ObjectMapper();
//            String jsonString = "{}";
//            try {
//                jsonString = objectMapper.writeValueAsString(dto);
//            } catch (JsonProcessingException e) {
//            }
//            JSONObject alertJson = JSONObject.parseObject(jsonString);
//            List<AlertFieldRequestDTO> alertFieldList = alertHandleHelper.getModelField(AlertConfigConstants.REDIS_MODEL_ALERT);
//            Map<String, Object> ext = thirdCreateAlertReq.getExt();
//            cmdbHelper.doExt (alertJson, alertFieldList, ext);
//            if (dto.getObjectType().equals(AlertsDTOV2.OBJECT_TYPE_BIZ)) {
//                if (StringUtils.isEmpty(dto.getMoniObject())) {
//                    dto.setMoniObject("Application");
//                }
//                if (StringUtils.isEmpty(dto.getBizSys())) {
//                    log.error("cancel insert alert of the null ServSystem: {} ", thirdCreateAlertReq.toString());
//                    break;
//                }
//            } else {
//                //处理cmdb数据
//                if (StringUtils.isNotEmpty(dto.getDeviceIp()) && CollectionUtils.isEmpty(ext)){
//                    // 根据  机房 + IP, 查找设备
//                    cmdbHelper.queryDeviceForAlertV2(alertJson, alertFieldList,alertHandleHelper.getModelField(AlertConfigConstants.REDIS_MODEL_DEVICE_INSTANCE));
//                } else {
//                    log.error("cancel insert alert of the null deviceIp: {} ", thirdCreateAlertReq.toString());
//                    break;
//                }
//            }
            alertHandleHelper.handleAlert(dto);
        }

        return new CommonResp();
    }
}
