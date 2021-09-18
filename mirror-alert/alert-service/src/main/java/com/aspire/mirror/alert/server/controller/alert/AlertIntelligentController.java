package com.aspire.mirror.alert.server.controller.alert;

import com.alibaba.fastjson.JSONObject;
import com.aspire.mirror.alert.api.dto.alert.AlertsDetailResponse;
import com.aspire.mirror.alert.api.dto.alert.AlertsOverViewResponse;
import com.aspire.mirror.alert.api.dto.alert.AlertsQueryRequest;
import com.aspire.mirror.alert.api.dto.alert.AlertStatisticSummaryDTO;
import com.aspire.mirror.alert.api.dto.AlertsExportDTO;
import com.aspire.mirror.alert.api.service.alert.AlertIntelligentService;
import com.aspire.mirror.alert.server.biz.alert.AlertIntelligentBiz;
import com.aspire.mirror.alert.server.vo.alert.AlertStatisticSummaryVo;
import com.aspire.mirror.alert.server.vo.alert.AlertsVo;
import com.aspire.mirror.alert.server.util.DateUtils;
import com.aspire.mirror.alert.server.util.TransformUtils;
import com.aspire.mirror.common.entity.PageRequest;
import com.aspire.mirror.common.entity.PageResponse;
import com.aspire.mirror.common.util.FieldUtil;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@Slf4j
public class AlertIntelligentController implements AlertIntelligentService {

//    private static final SimpleDateFormat f_sdf =   new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    @Autowired
    private AlertIntelligentBiz alertIntelligentBiz;

    @Override
    public PageResponse<AlertsDetailResponse> queryAlertIntelligent(@RequestBody AlertsQueryRequest queryRequest,
                                                                    @RequestParam(value = "alertId", required = false) String alertId) throws ParseException {
        if (queryRequest == null) {
            log.error("Alert query param pageRequset is null or query type is empty !");
            return null;
        }
        PageResponse<AlertsVo> pageResult = alertIntelligentBiz.queryAlertIntelligent(preparePageRequest(queryRequest),alertId);
        PageResponse<AlertsDetailResponse> result = new PageResponse<>();
        result.setCount(pageResult.getCount());
        result.setResult( TransformUtils.transform(AlertsDetailResponse.class, pageResult.getResult()));
        return result;
    }

    /**
     * 组装查询数据
     */
    private static final String CONTAIN_FLAG_INCLUDE = "include";
    private static final String CONTAIN_FLAG_EXCLUDE = "exclude";
//    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    private PageRequest preparePageRequest(AlertsQueryRequest queryRequest) throws ParseException {
        PageRequest pageRequest = new PageRequest();
        BeanUtils.copyProperties(queryRequest, pageRequest);
        if (StringUtils.isEmpty(queryRequest.getDeviceOp())
                || !StringUtils.equalsIgnoreCase(CONTAIN_FLAG_INCLUDE, queryRequest.getDeviceOp())
                || !StringUtils.equalsIgnoreCase(CONTAIN_FLAG_EXCLUDE, queryRequest.getDeviceOp())) {
            queryRequest.setDeviceOp(CONTAIN_FLAG_INCLUDE);
        }
        // 时间区间
        String span = queryRequest.getSpan();
        if (StringUtils.isNotEmpty(span)) {
            log.info("#=====> query span: " + span);
            Date startDate = DateUtils.getDateBySpan(span.toLowerCase());
            Date endDate = DateUtils.getTimesmorning();
            queryRequest.setAlertCreateStartTime(startDate);
            queryRequest.setAlertCreateEndTime(endDate);
        }
        // 业务系统
        if (StringUtils.isNotEmpty(queryRequest.getBizSys())) {
            queryRequest.setBizSysList( Arrays.asList(queryRequest.getBizSys().split(",")));
        }
        // 监控项
        if (StringUtils.isNotEmpty(queryRequest.getMonitItems())) {
            queryRequest.setMonitItemList(Arrays.asList(queryRequest.getMonitItems().split(",")));
        }
        // 告警来源
        if (StringUtils.isNotEmpty(queryRequest.getSource())) {
            queryRequest.setSourceList(Arrays.asList(queryRequest.getSource().split(",")));
        }
        if (StringUtils.isEmpty(queryRequest.getMonitOp())
                || !StringUtils.equalsIgnoreCase(CONTAIN_FLAG_INCLUDE, queryRequest.getMonitOp())
                || !StringUtils.equalsIgnoreCase(CONTAIN_FLAG_EXCLUDE, queryRequest.getMonitOp())) {
            queryRequest.setMonitOp(CONTAIN_FLAG_INCLUDE);
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        // 告警生效时间区间
        if (StringUtils.isNotEmpty(queryRequest.getAlertCreateTimeRangeStart())) {
            queryRequest.setAlertCreateStartTime(sdf.parse(queryRequest.getAlertCreateTimeRangeStart()));
        }
        if (StringUtils.isNotEmpty(queryRequest.getAlertCreateTimeRangeEnd())) {
            queryRequest.setAlertCreateEndTime(sdf.parse(queryRequest.getAlertCreateTimeRangeEnd()));
        }
        // 通知方式
        if (StringUtils.isNotEmpty(queryRequest.getNotifyType())) {
            queryRequest.setNotifyTypeList(Arrays.asList(queryRequest.getNotifyType().split(",")));
        }
        // 通知操作时间区间
        if (StringUtils.isNotEmpty(queryRequest.getNotifyTimeRangeStart())) {
            queryRequest.setNotifyStartTime((sdf.parse(queryRequest.getNotifyTimeRangeStart())));
        }
        if (StringUtils.isNotEmpty(queryRequest.getNotifyTimeRangeEnd())) {
            queryRequest.setNotifyEndTime((sdf.parse(queryRequest.getNotifyTimeRangeEnd())));
        }
        // 告警转派时间区间
        if (StringUtils.isNotEmpty(queryRequest.getTransferTimeRangeStart())) {
            queryRequest.setTransferStartTime(sdf.parse(queryRequest.getTransferTimeRangeStart()));
        }
        if (StringUtils.isNotEmpty(queryRequest.getTransferTimeRangeEnd())) {
            queryRequest.setTransferEndTime(sdf.parse(queryRequest.getTransferTimeRangeEnd()));
        }
        // 告警确认操作时间区间
        if (StringUtils.isNotEmpty(queryRequest.getConfirmTimeRangeStart())) {
            queryRequest.setConfirmStartTime((sdf.parse(queryRequest.getConfirmTimeRangeStart())));
        }
        if (StringUtils.isNotEmpty(queryRequest.getConfirmTimeRangeEnd())) {
            queryRequest.setConfirmEndTime(sdf.parse(queryRequest.getConfirmTimeRangeEnd()));
        }
        // 派单操作时间区间
        if (StringUtils.isNotEmpty(queryRequest.getDeliverTimeRangeStart())) {
            queryRequest.setDeliverStartTime(sdf.parse(queryRequest.getDeliverTimeRangeStart()));
        }
        if (StringUtils.isNotEmpty(queryRequest.getDeliverTimeRangeEnd())) {
            queryRequest.setDeliverEndTime(sdf.parse(queryRequest.getDeliverTimeRangeEnd()));
        }
        // 清除操作时间区间
        if (StringUtils.isNotEmpty(queryRequest.getClearTimeRangeStart())) {
            queryRequest.setClearStartTime(sdf.parse(queryRequest.getClearTimeRangeStart()));
        }
        if (StringUtils.isNotEmpty(queryRequest.getClearTimeRangeEnd())) {
            queryRequest.setClearEndTime(sdf.parse(queryRequest.getClearTimeRangeEnd()));
        }
        // 过滤操作时间区间
        if (StringUtils.isNotEmpty(queryRequest.getFilterTimeRangeStart())) {
            queryRequest.setFilterStartTime(sdf.parse(queryRequest.getFilterTimeRangeStart()));
        }
        if (StringUtils.isNotEmpty(queryRequest.getFilterTimeRangeEnd())) {
            queryRequest.setFilterEndTime(sdf.parse(queryRequest.getFilterTimeRangeEnd()));
        }
        // 工程操作时间区间
        if (StringUtils.isNotEmpty(queryRequest.getProjectTimeRangeStart())) {
            queryRequest.setProjectStartTime(sdf.parse(queryRequest.getProjectTimeRangeStart()));
        }
        if (StringUtils.isNotEmpty(queryRequest.getMaintainTimeRangeStart())) {
            queryRequest.setMaintainStartTime(sdf.parse(queryRequest.getMaintainTimeRangeStart()));
        }
        // 维护 操作时间区间
        if (StringUtils.isNotEmpty(queryRequest.getMaintainTimeRangeEnd())) {
            queryRequest.setMaintainEndTime(sdf.parse(queryRequest.getMaintainTimeRangeEnd()));
        }
        Map<String, Object> map = FieldUtil.getFiledMap(queryRequest);
        log.info("#=====> query map: " + JSONObject.toJSONString(map));
        for (String key : map.keySet()) {
            pageRequest.addFields(key, map.get(key));
        }
        log.info("#=====> query params: " + JSONObject.toJSONString(pageRequest));
        return pageRequest;
    }

    @Override
    public AlertsOverViewResponse alertIntelligentOverview(@RequestBody AlertsQueryRequest queryRequest) throws ParseException {
        AlertStatisticSummaryVo overview = alertIntelligentBiz.alertIntelligentOverview(preparePageRequest(queryRequest));
        AlertsOverViewResponse response = new AlertsOverViewResponse();
        BeanUtils.copyProperties(overview, response);
        return response;
    }

    @Override
    public List<Map<String, Object>> exportAlertIntelligentData(@RequestBody AlertsQueryRequest queryRequest) throws Exception {
        List<Map<String, Object>> dataLists = Lists.newArrayList();
        if (queryRequest == null) {
            log.error("Alert query param pageRequset is null or query type is empty !");
            return dataLists;
        }
        List<AlertsVo> pageResult = alertIntelligentBiz.exportAlertIntelligentData(preparePageRequest(queryRequest));
        SimpleDateFormat f_sdf =   new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (AlertsVo alertsVo : pageResult) {
            AlertsExportDTO exportDTO = new AlertsExportDTO();
            exportDTO.setAlertId(alertsVo.getAlertId());
            exportDTO.setAlertLevel(transAlertLevel(alertsVo.getAlertLevel()));
            exportDTO.setOrderStatus(transOrderStatus(alertsVo.getOrderStatus()));
            exportDTO.setObjectType(transAlertObjectType(alertsVo.getObjectType()));
            exportDTO.setDeviceIp(alertsVo.getDeviceIp());
            exportDTO.setDeviceId(alertsVo.getDeviceId());
            exportDTO.setMoniObject(alertsVo.getMoniObject());
            exportDTO.setCurMoniValue(alertsVo.getCurMoniValue());
            exportDTO.setMoniIndex(alertsVo.getMoniIndex());
            exportDTO.setAlertStartTime(alertsVo.getAlertStartTime() == null ? "" : f_sdf.format(alertsVo.getAlertStartTime()));
            exportDTO.setCurMoniTime(alertsVo.getCurMoniTime() == null ? "" : f_sdf.format(alertsVo.getCurMoniTime()));
            exportDTO.setIdcType(alertsVo.getIdcType());
            exportDTO.setSourceRoom(alertsVo.getSourceRoom());
            exportDTO.setSource(alertsVo.getSource());
            exportDTO.setReportType(transAlertReportType(alertsVo.getReportType()));
            exportDTO.setReportStatus(transOperateStatus(alertsVo.getReportStatus()));
            exportDTO.setReportTime(alertsVo.getReportTime() == null ? "" : f_sdf.format(alertsVo.getReportTime()));
            exportDTO.setTransUser(alertsVo.getTransUser());
            exportDTO.setTransStatus(transOperateStatus(alertsVo.getTransStatus()));
            exportDTO.setTransTime(alertsVo.getTransTime() == null ? "" : f_sdf.format(alertsVo.getTransTime()));
            exportDTO.setToConfirmUser(alertsVo.getToConfirmUser());
            exportDTO.setConfirmedUser(alertsVo.getConfirmedUser());
            exportDTO.setConfirmedTime(alertsVo.getConfirmedTime() == null ? "" : f_sdf.format(alertsVo.getConfirmedTime()));
            exportDTO.setConfirmedContent(alertsVo.getConfirmedContent());
            exportDTO.setDeliverStatus(transOperateStatus(alertsVo.getDeliverStatus()));
            exportDTO.setDeliverTime(alertsVo.getDeliverTime() == null ? "" : f_sdf.format(alertsVo.getDeliverTime()));
            exportDTO.setOrderType(transOrderType(alertsVo.getOrderType()));
            exportDTO.setAlertCount(alertsVo.getAlertCount() == null ? 0 : alertsVo.getAlertCount());
            dataLists.add(objectToMap(exportDTO));
        }
        return dataLists;
    }
    private Map<String, Object> objectToMap(Object obj) throws Exception {
        if(obj == null){
            return null;
        }
        Map<String, Object> map = new HashMap<String, Object>();
        Field[] declaredFields = obj.getClass().getDeclaredFields();
        for (Field field : declaredFields) {
            field.setAccessible(true);
            map.put(field.getName(), field.get(obj));
        }
        return map;
    }

    private String transAlertLevel(String origin) {
        if (StringUtils.isEmpty(origin)) {
            return "";
        }
        String alertLevel;
        switch (StringUtils.trim(origin)) {
            case "1":
                alertLevel = "提示";
                break;
            case "2":
                alertLevel = "低";
                break;
            case "3":
                alertLevel = "中";
                break;
            case "4":
                alertLevel = "高";
                break;
            case "5":
                alertLevel = "严重";
                break;
            default:
                alertLevel = origin;
                break;
        }
        return alertLevel;
    }

    private String transOrderStatus(String origin) {
        if (StringUtils.isEmpty(origin)) {
            return "";
        }
        String orderStatus;
        switch (StringUtils.trim(origin)) {
            case "1":
                orderStatus = "未派单";
                break;
            case "2":
                orderStatus = "处理中";
                break;
            case "3":
                orderStatus = "已完成";
                break;
            default:
                orderStatus = origin;
                break;
        }
        return orderStatus;
    }

    private String transAlertObjectType(String origin) {
        if (StringUtils.isEmpty(origin)) {
            return "";
        }
        String objectType;
        switch (StringUtils.trim(origin)) {
            case "1":
                objectType = "设备";
                break;
            case "2":
                objectType = "业务系统";
                break;
            default:
                objectType = origin;
                break;
        }
        return objectType;
    }

    private String transAlertReportType(Integer reportType) {
        if (reportType == null) {
            return "";
        }
        String type;
        switch (reportType) {
            case 0:
                type = "短信";
                break;
            case 1:
                type = "邮件";
                break;
            default:
                type = String.valueOf(reportType);
                break;
        }
        return type;
    }

    private String transOperateStatus(Integer reportStaus) {
        if (reportStaus == null) {
            return "";
        }
        String status;
        switch (reportStaus) {
            case 0:
                status = "失败";
                break;
            case 1:
                status = "成功";
                break;
            default:
                status = String.valueOf(reportStaus);
                break;
        }
        return status;
    }

    private String transOrderType(String origin) {
        if (StringUtils.isEmpty(origin)) {
            return "";
        }
        String type;
        switch (origin) {
            case "1":
                type = "告警工单";
                break;
            case "2":
                type = "故障工单";
                break;
            default:
                type = origin;
                break;
        }
        return type;
    }

}
