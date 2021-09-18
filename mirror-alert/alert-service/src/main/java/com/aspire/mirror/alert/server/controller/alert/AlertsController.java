package com.aspire.mirror.alert.server.controller.alert;

import com.alibaba.fastjson.JSONArray;
import com.aspire.mirror.alert.api.dto.*;
import com.aspire.mirror.alert.api.dto.alert.*;
import com.aspire.mirror.alert.api.dto.notify.AlertNotifyResp;
import com.aspire.mirror.alert.api.dto.notify.NotifyPageResponse;
import com.aspire.mirror.alert.api.service.alert.AlertsService;
import com.aspire.mirror.alert.server.biz.alert.AlertsBiz;
import com.aspire.mirror.alert.server.biz.helper.SmSendHelper;
import com.aspire.mirror.alert.server.constant.AlertCommonConstant;
import com.aspire.mirror.alert.server.dao.alert.po.AlertsDetail;
import com.aspire.mirror.alert.server.dao.notify.po.AlertsNotify;
import com.aspire.mirror.alert.server.dao.alert.po.AlertsRecord;
import com.aspire.mirror.alert.server.vo.alert.AlertDeviceInformationVo;
import com.aspire.mirror.alert.server.vo.alert.AlertsVo;
import com.aspire.mirror.alert.server.vo.notify.NotifyPageVo;
import com.aspire.mirror.alert.server.util.TransformUtils;
import com.aspire.mirror.common.entity.PageRequest;
import com.aspire.mirror.common.entity.PageResponse;
import com.aspire.mirror.common.util.FieldUtil;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.*;

/**
 * 告警控制层
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.alert.server.controller
 * 类名称:    AlertsController.java
 * 类描述:    告警控制层
 * 创建人:    JinSu
 * 创建时间:  2018/9/14 17:51
 * 版本:      v1.0
 */
@RestController
public class AlertsController implements AlertsService {


    private static final Logger LOGGER = LoggerFactory.getLogger(AlertsController.class);

    @Autowired
    private AlertsBiz alertsBiz;
    @Value("${AlertCabinetColumnTask.itemInfo:}")
    private String itemInfo;
    @Autowired
    private  SmSendHelper sendHelper;

    @Override
    public String upgrade(@RequestParam("old_order_id") String oldOrderId,
                          @RequestParam("order_id") String orderId,
                          @RequestParam("type") String type,
                          @RequestParam("order_status") String orderStatus,
                          @RequestParam("user_name") String userName) {
        LOGGER.info("call upgrade oldOrderId is {}, orderId is {}, orderStatus is {}",oldOrderId,orderId,orderStatus);
        if (!org.apache.commons.lang3.StringUtils.isNumeric(type)) {
            return "{\"status\":\"error\",\"message\":\"type is not number\"}";
        }
        String status = alertsBiz.upgrade(oldOrderId, orderId, type,orderStatus,userName);
        return "{\"status\":\"" + status + "\"}";
    }

    /**
     * 告警列表
     *
     * @param pageRequset 查询page对象
     * @return PageResponse 列表返回对象
     */
    @Override
    public PageResponse<AlertsDetailResponse> pageList(@RequestBody AlertsPageRequset pageRequset) {
        if (pageRequset == null) {
            LOGGER.warn("pageList param pageRequset is null");
            return null;
        }
        PageRequest page = new PageRequest();
        BeanUtils.copyProperties(pageRequset, page);
        Map<String, Object> map = FieldUtil.getFiledMap(pageRequset);
        for (String key : map.keySet()) {
            page.addFields(key, map.get(key));
        }
        PageResponse<AlertsVo> pageResult = alertsBiz.pageList(page);
        List<AlertsDetailResponse> listAlert = Lists.newArrayList();
        if (!CollectionUtils.isEmpty(pageResult.getResult())) {
            for (AlertsVo alertsVo : pageResult.getResult()) {
                AlertsDetailResponse alertsDetailResponse = new AlertsDetailResponse();
                BeanUtils.copyProperties(alertsVo, alertsDetailResponse);
                listAlert.add(alertsDetailResponse);
            }
        }
        PageResponse<AlertsDetailResponse> result = new PageResponse<AlertsDetailResponse>();
        result.setCount(pageResult.getCount());
        result.setResult(listAlert);
        return result;
    }

    /**
     * 告警详情
     *
     * @param alertId 告警Id
     * @return
     */
    @Override
    public AlertSecondDetailResp findAlertByPrimaryKey(@PathVariable("alert_id") String alertId) {
        if (StringUtils.isEmpty(alertId)) {
            LOGGER.warn("findByPrimaryKey param alertId is null");
            return null;
        }

        LOGGER.info("alertId is {} ", alertId);

        String[] alertIdarray = alertId.split(",");

        int length = alertIdarray.length;
        String alertIda = alertIdarray[length - 1];

        AlertsVo alertsVo = alertsBiz.selectAlertByPrimaryKey(alertIda);

        AlertSecondDetailResp alertSecondDetailResp = new AlertSecondDetailResp();

        if (null == alertsVo) {

            return null;
        }

        BeanUtils.copyProperties(alertsVo, alertSecondDetailResp);
        return alertSecondDetailResp;
    }

    /**
     * 告警上报分页
     *
     * @param alertId 告警Id
     * @return
     */
    @Override
    public PageResponse<AlertGenResp> alertGenerateList(@RequestParam("alert_id") String alertId,
                                                        @RequestParam("page_no") String pageNo,
                                                        @RequestParam("page_size") String pageSize) {

        LOGGER.info("alertId is {} ", alertId);
        LOGGER.info("alertId is {} ", pageNo);
        LOGGER.info("alertId is {} ", pageSize);

        PageResponse<AlertsDetail> alertGenPage = alertsBiz.alertGenerateListByPage(alertId, pageNo, pageSize);

        List<AlertGenResp> alertGenRespList = new ArrayList<AlertGenResp>();
        for (AlertsDetail alertsDetail : alertGenPage.getResult()) {

            AlertGenResp alertGenResp = new AlertGenResp();

            BeanUtils.copyProperties(alertsDetail, alertGenResp);

            alertGenRespList.add(alertGenResp);
        }
        PageResponse<AlertGenResp> alertGenRespPageResponse = new PageResponse<>();
        alertGenRespPageResponse.setCount(alertGenPage.getCount());
        alertGenRespPageResponse.setResult(alertGenRespList);
        return alertGenRespPageResponse;
    }

    /**
     * 告警操作分页
     *
     * @param alertId 告警Id
     * @return
     */
    @Override
    public PageResponse<AlertRecordResp> alertRecordList(@RequestParam("alert_id") String alertId,
                                                         @RequestParam("page_no") String pageNo,
                                                         @RequestParam("page_size") String pageSize) {
        LOGGER.info("alertId is {} ", alertId);
        LOGGER.info("alertId is {} ", pageNo);
        LOGGER.info("alertId is {} ", pageSize);

        PageResponse<AlertsRecord> alertRecordPage = alertsBiz.alertRecordListByPage(alertId, pageNo, pageSize);

        List<AlertRecordResp> alertRecordRespList = new ArrayList<AlertRecordResp>();

        for (AlertsRecord alertsRecord : alertRecordPage.getResult()) {

            AlertRecordResp alertRecordResp = new AlertRecordResp();

            BeanUtils.copyProperties(alertsRecord, alertRecordResp);

            alertRecordRespList.add(alertRecordResp);
        }
        PageResponse<AlertRecordResp> alertRecordRespPageResponse = new PageResponse<>();
        alertRecordRespPageResponse.setCount(alertRecordPage.getCount());
        alertRecordRespPageResponse.setResult(alertRecordRespList);
        return alertRecordRespPageResponse;


    }

    /**
     * 告警通知分页
     *
     * @param alertId 告警Id
     * @return
     */
    @Override
    public NotifyPageResponse<AlertNotifyResp> alertNotifyList(@RequestParam("alert_id") String alertId,
                                                               @RequestParam("page_no") String pageNo,
                                                               @RequestParam("page_size") String pageSize,
                                                               @RequestParam("report_type") String reportType) {
        LOGGER.info("alertId is {} ", alertId);
        LOGGER.info("alertId is {} ", pageNo);
        LOGGER.info("alertId is {} ", pageSize);
        LOGGER.info("reportType is {} ", reportType);


        NotifyPageVo<AlertsNotify> alertsNotifyNotifyPageVo = alertsBiz.alertNotifyListByPage(alertId, pageNo, pageSize, reportType);

        List<AlertNotifyResp> alertNotifyRespList = new ArrayList<AlertNotifyResp>();
        for (AlertsNotify alertsNotify : alertsNotifyNotifyPageVo.getResult()) {
            AlertNotifyResp alertNotifyResp = new AlertNotifyResp();
            BeanUtils.copyProperties(alertsNotify, alertNotifyResp);
            alertNotifyRespList.add(alertNotifyResp);
        }
        NotifyPageResponse<AlertNotifyResp> notifyRespNotifyPageResponse = new NotifyPageResponse<>();
        notifyRespNotifyPageResponse.setCount(alertsNotifyNotifyPageVo.getCount());
        notifyRespNotifyPageResponse.setFallCount(alertsNotifyNotifyPageVo.getFallCount());
        notifyRespNotifyPageResponse.setSuccessCount(alertsNotifyNotifyPageVo.getSuccessCount());
        notifyRespNotifyPageResponse.setResult(alertNotifyRespList);
        return notifyRespNotifyPageResponse;


    }

    //告警上报记录excel 下载
    @Override
    public List<AlertGenResp> alertGenerateDownload(@RequestParam("alert_id") String alertId) {

        LOGGER.info("alertId is {} ", alertId);

        List<AlertsVo> alertsList = alertsBiz.selectAlertGenerateList(alertId);

        List<AlertGenResp> alertGenRespList = new ArrayList<AlertGenResp>();
        for (AlertsVo alertsVo : alertsList) {
            AlertGenResp alertGenResp = new AlertGenResp();
            BeanUtils.copyProperties(alertsVo, alertGenResp);

            alertGenRespList.add(alertGenResp);
        }

        return alertGenRespList;
    }

    //告警操作记录excel 下载
    @Override
    public List<AlertRecordResp> alertRecordDownload(@RequestParam("alert_id") String alertId) {

        LOGGER.info("alertId is {} ", alertId);

        List<AlertsRecord> alertsRecordList = alertsBiz.selectAlertRecordByPrimaryKey(alertId);
        List<AlertRecordResp> alertRecordRespList = new ArrayList<AlertRecordResp>();
        for (AlertsRecord alertsRecord : alertsRecordList) {
            AlertRecordResp alertRecordResp = new AlertRecordResp();
            BeanUtils.copyProperties(alertsRecord, alertRecordResp);

            alertRecordRespList.add(alertRecordResp);
        }

        return alertRecordRespList;
    }


    //告警通知记录excel 下载
    @Override
    public List<AlertNotifyResp> alertNotifyDownload(@RequestParam("alert_id") String alertId) {

        LOGGER.info("alertId is {} ", alertId);

        List<AlertsNotify> AlertsNotifyList = alertsBiz.selectalertNotifyByPrimaryKey(alertId);
        List<AlertNotifyResp> alertNotifyRespList = new ArrayList<AlertNotifyResp>();
        for (AlertsNotify alertsNotify : AlertsNotifyList) {

            AlertNotifyResp alertNotifyResp = new AlertNotifyResp();
            BeanUtils.copyProperties(alertsNotify, alertNotifyResp);
            alertNotifyRespList.add(alertNotifyResp);
        }
        return alertNotifyRespList;
    }

    //修改告警备注
    @Override
    public ResponseEntity<String> updateNote(@PathVariable("alert_id") String alertId, @RequestParam("note") String note) {

        if (StringUtils.isEmpty(alertId)) {
            LOGGER.warn("updateNote param alertId is null");
            return null;
        }
        if (StringUtils.isEmpty(note)) {
            LOGGER.warn("updateNote param note is null");
            return null;
        }

        alertsBiz.updateNote(alertId, note);

        return new ResponseEntity<String>("success", HttpStatus.OK);
    }

    /**
     * 根据告警ID集合查询结果
     *
     * @param alertIds 模板主键(多个以逗号分隔)
     * @return
     */
    @Override
    public List<AlertsDetailResponse> listByPrimaryKeyArrays(@PathVariable("alert_ids") String alertIds) {
        if (StringUtils.isEmpty(alertIds)) {
            LOGGER.error("listByPrimaryKeyArrays param alertIds is null");
            return Lists.newArrayList();
        }
        String[] alertIdArrays = alertIds.split(",");
        return TransformUtils.transform(AlertsDetailResponse.class, alertsBiz.selectByPrimaryKeyArrays(alertIdArrays));
    }

    /**
     * 根据工单ID修改状态
     *
     * @param orderId     工单ID
     * @param orderStatus 工单状态
     */
    @Override
    public void modOrderStatusByOrderId(@PathVariable("order_id") String orderId,
                                        @PathVariable("status") String orderStatus) {
        LOGGER.info("call modOrderStatusByOrderId order_id is {}, status is {}!",orderId, orderStatus);
        if (StringUtils.isEmpty(orderId) || StringUtils.isEmpty(orderStatus)) {
            LOGGER.warn("AlertsController[modOrderStatusByOrderId] param validation is failed");
        } else {
            alertsBiz.modOrderStatusByOrderId(orderId, orderStatus);
        }
    }

    @Override
    public int getAlertValue(@RequestBody @Validated AlertValueSearchRequest alertValueSearchRequest) {
        int result = alertsBiz.getAlertValue(alertValueSearchRequest.getIpMap(), alertValueSearchRequest
                .getAlertLevel(), alertValueSearchRequest.getItemIdList());
        return result;
    }

    @Override
    public AlertDeviceInformation alertDeviceInformation(@RequestParam("alert_id")String alertId) {
        if (org.apache.commons.lang.StringUtils.isBlank(alertId)) {
            throw new RuntimeException("alertId is null");
        }
        AlertDeviceInformation deviceInformation = new AlertDeviceInformation();
        Map<String, Object> map = new HashMap<>();
        Map<String,Object> alerts= alertsBiz.selectAlertByAlertId(alertId);
        JSONArray array = JSONArray.parseArray(itemInfo);
        map.put("source", alerts.get("source"));
        map.put("sourceRoom", alerts.get("sourceRoom"));
        map.put("keyComment", alerts.get("keyComment"));
        map.put("cabinet",alerts.get("cabinet"));
        map.put("cabinetCount",alerts.get("cabinetCount"));
        map.put("keyCommentColumn", AlertCommonConstant.CABINETCOLUMN_ALERT_TITLE);
        map.put("idcType",alerts.get("idcType"));
        map.put("deviceItem", array);
        if (map.get("keyComment").equals(AlertCommonConstant.CABINETCOLUMN_ALERT_TITLE)) {
            //列头柜
            AlertDeviceInformationVo dice =alertsBiz.queryAlertHList(map);
            BeanUtils.copyProperties(dice,deviceInformation);
            return deviceInformation;
        } else if (map.get("keyComment").equals(AlertCommonConstant.CABINET_ALERT_TITLE)) {
            //机柜
            AlertDeviceInformationVo dice=alertsBiz.queryAlertMList(map);
            BeanUtils.copyProperties(dice,deviceInformation);
            return deviceInformation;
        } else {
            return deviceInformation;
        }
    }

    @Override
    public Boolean smsTenantNotify(@RequestBody Map<String, Object> request) {
        try {
            String mobile = (String) request.get("mobiles");
            String message = (String) request.get("message");
            List<String> mobiles = new ArrayList<>();
            if (org.apache.commons.lang3.StringUtils.isNotBlank(mobile)) {
                mobiles= Arrays.asList(mobile.split(","));
            }
            if (CollectionUtils.isEmpty(mobiles)) {
                LOGGER.info("phone number is empty");
                return false;
            }
            return sendHelper.send(mobiles, message);
        } catch (Exception e) {
            LOGGER.error("message failed to send ：" + e.getMessage(), e);
            return false;
        }
    }

}
