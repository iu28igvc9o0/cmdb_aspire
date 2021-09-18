package com.aspire.mirror.alert.server.controller.alert;

import com.alibaba.fastjson.JSONObject;
import com.aspire.mirror.alert.api.dto.alert.*;
import com.aspire.mirror.alert.api.service.alert.AlertHomeService;
import com.aspire.mirror.alert.server.biz.alert.AlertHomeBiz;
import com.aspire.mirror.alert.server.dao.alert.po.Alerts;
import com.aspire.mirror.alert.server.dao.alert.po.AlertsHis;
import com.aspire.mirror.alert.server.util.PayloadParseUtil;
import com.aspire.mirror.alert.server.vo.alert.AlertStatisticSummaryVo;
import com.aspire.mirror.alert.server.vo.alert.AlertsQueryVo;
import com.aspire.mirror.alert.server.vo.alert.AlertsVo;
import com.aspire.mirror.alert.server.vo.alert.AlertsHisVo;
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
import org.springframework.util.ReflectionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@Slf4j
public class AlertHomeController implements AlertHomeService {
    @Autowired
    private AlertHomeBiz alertHomeBiz;

    @Override
    public PageResponse<AlertsDetailResponse> getAlertData(@RequestBody AlertsQueryRequest queryRequest,
                                                           @RequestParam("alertType") String alertType) throws ParseException {
        if (queryRequest == null) {
            log.error("Alert query param pageRequset is null or query type is empty !");
            return null;
        }
        log.info("#=====> query alert data request: {}", JSONObject.toJSONString(queryRequest));
        long start1 = System.currentTimeMillis();
        PageResponse<AlertsVo> pageResult = alertHomeBiz.select(preparePageRequest(queryRequest),alertType);
        long end1 = System.currentTimeMillis();
        log.debug("显示出查询告警的时间"+(end1-start1)+"ms");
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
        SimpleDateFormat f_sdf =   new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // 告警生效时间区间
        if (StringUtils.isNotEmpty(queryRequest.getAlertCreateTimeRangeStart())) {
            queryRequest.setAlertCreateStartTime(f_sdf.parse(queryRequest.getAlertCreateTimeRangeStart()));
        }
        if (StringUtils.isNotEmpty(queryRequest.getAlertCreateTimeRangeEnd())) {
            queryRequest.setAlertCreateEndTime(f_sdf.parse(queryRequest.getAlertCreateTimeRangeEnd()));
        }
        // 通知方式
        if (StringUtils.isNotEmpty(queryRequest.getNotifyType())) {
            queryRequest.setNotifyTypeList(Arrays.asList(queryRequest.getNotifyType().split(",")));
        }
        // 通知操作时间区间
        if (StringUtils.isNotEmpty(queryRequest.getNotifyTimeRangeStart())) {
            queryRequest.setNotifyStartTime((f_sdf.parse(queryRequest.getNotifyTimeRangeStart())));
        }
        if (StringUtils.isNotEmpty(queryRequest.getNotifyTimeRangeEnd())) {
            queryRequest.setNotifyEndTime((f_sdf.parse(queryRequest.getNotifyTimeRangeEnd())));
        }
        // 告警转派时间区间
        if (StringUtils.isNotEmpty(queryRequest.getTransferTimeRangeStart())) {
            queryRequest.setTransferStartTime(f_sdf.parse(queryRequest.getTransferTimeRangeStart()));
        }
        if (StringUtils.isNotEmpty(queryRequest.getTransferTimeRangeEnd())) {
            queryRequest.setTransferEndTime(f_sdf.parse(queryRequest.getTransferTimeRangeEnd()));
        }
        // 告警确认操作时间区间
        if (StringUtils.isNotEmpty(queryRequest.getConfirmTimeRangeStart())) {
            queryRequest.setConfirmStartTime((f_sdf.parse(queryRequest.getConfirmTimeRangeStart())));
        }
        if (StringUtils.isNotEmpty(queryRequest.getConfirmTimeRangeEnd())) {
            queryRequest.setConfirmEndTime(f_sdf.parse(queryRequest.getConfirmTimeRangeEnd()));
        }
        // 派单操作时间区间
        if (StringUtils.isNotEmpty(queryRequest.getDeliverTimeRangeStart())) {
            queryRequest.setDeliverStartTime(f_sdf.parse(queryRequest.getDeliverTimeRangeStart()));
        }
        if (StringUtils.isNotEmpty(queryRequest.getDeliverTimeRangeEnd())) {
            queryRequest.setDeliverEndTime(f_sdf.parse(queryRequest.getDeliverTimeRangeEnd()));
        }
        // 清除操作时间区间
        if (StringUtils.isNotEmpty(queryRequest.getClearTimeRangeStart())) {
            queryRequest.setClearStartTime(f_sdf.parse(queryRequest.getClearTimeRangeStart()));
        }
        if (StringUtils.isNotEmpty(queryRequest.getClearTimeRangeEnd())) {
            queryRequest.setClearEndTime(f_sdf.parse(queryRequest.getClearTimeRangeEnd()));
        }
        // 过滤操作时间区间
        if (StringUtils.isNotEmpty(queryRequest.getFilterTimeRangeStart())) {
            queryRequest.setFilterStartTime(f_sdf.parse(queryRequest.getFilterTimeRangeStart()));
        }
        if (StringUtils.isNotEmpty(queryRequest.getFilterTimeRangeEnd())) {
            queryRequest.setFilterEndTime(f_sdf.parse(queryRequest.getFilterTimeRangeEnd()));
        }
        // 工程操作时间区间
        if (StringUtils.isNotEmpty(queryRequest.getProjectTimeRangeStart())) {
            queryRequest.setProjectStartTime(f_sdf.parse(queryRequest.getProjectTimeRangeStart()));
        }
        if (StringUtils.isNotEmpty(queryRequest.getMaintainTimeRangeStart())) {
            queryRequest.setMaintainStartTime(f_sdf.parse(queryRequest.getMaintainTimeRangeStart()));
        }
        // 维护 操作时间区间
        if (StringUtils.isNotEmpty(queryRequest.getMaintainTimeRangeEnd())) {
            queryRequest.setMaintainEndTime(f_sdf.parse(queryRequest.getMaintainTimeRangeEnd()));
        }
        Map<String, Object> map = FieldUtil.getFiledMap(queryRequest);
        map.put("authBizSystemIdList",queryRequest.getAuthBizSystemIdList());
        map.put("authPrecinctList",queryRequest.getAuthPrecinctList());
        map.put("authDeviceTypeList",queryRequest.getAuthDeviceTypeList());
        map.put("authDeviceIdList",queryRequest.getAuthDeviceIdList());
        map.put("authIdcIdList",queryRequest.getAuthIdcIdList());
        log.info("#=====> query map: {}" , map);
        for (String key : map.keySet()) {
            pageRequest.addFields(key, map.get(key));
        }
        log.info("#=====> query params: {}", pageRequest);
        return pageRequest;
    }
    private Map getAllFiledMap(Object o) {
        if(null == o) {
            return new HashMap<String, Object>();
        }
        net.sf.json.JSONObject jsonObject = net.sf.json.JSONObject.fromObject( o );
        Map fieldMap = jsonObject;
        return fieldMap;
    }
    
    @Override
    public List<String> activeAlertSourceList() {
    	return alertHomeBiz.activeAlertSourceList();
    }
    
    @Override
    public AlertsOverViewResponse overview(@RequestBody AlertsQueryRequest queryRequest,
                                           @RequestParam("alertType") String alertType) throws ParseException {
        AlertStatisticSummaryVo overview = alertHomeBiz.getOverview(preparePageRequest(queryRequest),alertType);
        AlertsOverViewResponse response = new AlertsOverViewResponse();
        BeanUtils.copyProperties(overview, response);
        return response;
    }

    @Override
    public PageResponse<AlertsHisDetailResponse> queryHis(@RequestBody AlertsQueryRequest queryRequest) throws ParseException {
        if (queryRequest == null) {
            return null;
        }
        log.info("#=====> query alert his data request: {}", JSONObject.toJSONString(queryRequest));
        String isClear = null;
        if ("1".equals(queryRequest.getIsClear())) {
            isClear = "clearTime";
        } else if ("2".equals(queryRequest.getIsClear())) {
            isClear = "alertTime";
        }
        queryRequest.setIsClear(isClear);
        PageResponse<AlertsHisVo> pageResult = alertHomeBiz.selectHis(preparePageRequest(queryRequest));
        PageResponse<AlertsHisDetailResponse> result = new PageResponse<>();
        result.setCount(pageResult.getCount());
        result.setResult( TransformUtils.transform(AlertsHisDetailResponse.class, pageResult.getResult()));
        return result;
    }

    @Override
    public List<Map<String, Object>> export(@RequestBody AlertsQueryRequest queryRequest,
                                            @RequestParam ("alertType") String alertType) throws Exception {
        List<Map<String, Object>> dataLists = Lists.newArrayList();
        if (queryRequest == null) {
            log.error("Alert query param pageRequset is null or query type is empty !");
            return dataLists;
        }
        String isClear = null;
        if ("1".equals(queryRequest.getIsClear())) {
            isClear = "clearTime";
        } else if ("2".equals(queryRequest.getIsClear())) {
            isClear = "alertTime";
        }
        queryRequest.setIsClear(isClear);
        List<Alerts> pageResult = alertHomeBiz.export(preparePageRequest(queryRequest),alertType);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (Alerts alertsDTO : pageResult) {
            Map<String, Object> stringObjectMap = objectToMap(alertsDTO);
            Date alertStartTime = alertsDTO.getAlertStartTime();
            Date curMoniTime=alertsDTO.getCurMoniTime();
             String alertLevel = alertsDTO.getAlertLevel();
             if (curMoniTime != null || alertStartTime !=null || alertLevel !=null) {
                String curMoniTimeStr = df.format(curMoniTime);
                String format = df.format(alertStartTime);
                String alertLevelType = transAlertLevel(alertLevel);
                stringObjectMap.put("curMoniTime", curMoniTimeStr);
                stringObjectMap.put("alertLevel", alertLevelType);
                stringObjectMap.put("alertStartTime", format);
            }
             dataLists.add(stringObjectMap);
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
            ReflectionUtils.makeAccessible(field);
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

    @Override
    public List<Map<String, Object>> exportHis(@RequestBody AlertsQueryRequest queryRequest) throws Exception {
        List<Map<String, Object>> dataLists = Lists.newArrayList();
        List<AlertsHis> pageResult = alertHomeBiz.exportHis(preparePageRequest(queryRequest));
        if (queryRequest == null) {
            log.error("Alert query param pageRequset is null or query type is empty !");
            return dataLists;
        }
        for (AlertsHis alertsHisDTO : pageResult) {
            dataLists.add(objectToMap(alertsHisDTO));
        }
        return dataLists;
    }

    @Override
    public ResponseEntity<String> getHomeAlertVoiceContent(@RequestParam ("isUandS") boolean isUandS,
                                                           @RequestBody AlertsQueryRequest queryRequest) {
        ResponseEntity<String> pageResult = new ResponseEntity<String>("", HttpStatus.OK);
        try {
            pageResult =
                    alertHomeBiz.getHomeAlertVoiceContent(isUandS, PayloadParseUtil.jacksonBaseParse(AlertsQueryVo.class, queryRequest));
        } catch (Exception e) {
            log.error("get Home Alert Voice Content  Error is {}", e);
        }
        return pageResult;
    }

    @Override
    public AlertsOverViewResponse hisOverview(
            @RequestBody AlertsQueryRequest queryRequest) throws ParseException {
        AlertStatisticSummaryVo overview = alertHomeBiz.hisOverview(preparePageRequest(queryRequest));
        AlertsOverViewResponse response = new AlertsOverViewResponse();
        BeanUtils.copyProperties(overview, response);
        return response;
    }
}
