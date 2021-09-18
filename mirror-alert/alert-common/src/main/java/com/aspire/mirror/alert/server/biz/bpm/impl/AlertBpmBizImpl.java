package com.aspire.mirror.alert.server.biz.bpm.impl;

import com.aspire.mirror.alert.server.dao.alert.AlertsRecordDao;
import com.aspire.mirror.alert.server.dao.alert.po.AlertsRecord;
import com.aspire.mirror.alert.server.vo.alert.AlertsOperationRequestVo;
import com.aspire.mirror.alert.server.biz.bpm.AlertBpmBiz;
import com.aspire.mirror.alert.server.biz.model.AlertFieldBiz;
import com.aspire.mirror.alert.server.biz.alert.AlertsBizV2;
import com.aspire.mirror.alert.server.constant.AlertConfigConstants;
import com.aspire.mirror.alert.server.dao.alert.AlertsV2Dao;
import com.aspire.mirror.alert.server.dao.alert.po.AlertsV2;
import com.aspire.mirror.alert.server.util.AlertModelCommonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.aspire.mirror.alert.server.util.PayloadParseUtil.jacksonBaseParse;

@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class AlertBpmBizImpl implements AlertBpmBiz {

    @Autowired
    private AlertsV2Dao alertsV2Dao;
    @Autowired
    private AlertFieldBiz alertFieldBiz;
    @Autowired
    private AlertsRecordDao alertsRecordDao;
    @Autowired
    private AlertsBizV2 alertsBizV2;

    @Override
    public ResponseEntity<String> alertConfirmByOrderId(String username, String orderId, String content) {
        AlertsV2 alerts = alertsV2Dao.selectByOrderId(orderId);
        alerts.setOperateStatus(1);
        int index = alertsV2Dao.updateByPrimaryKey(AlertModelCommonUtil.generateAlerts(alerts, alertFieldBiz.getModelFromRedis(AlertConfigConstants.REDIS_MODEL_ALERT, null)));
        AlertsRecord alertsRecord = new AlertsRecord();
        alertsRecord.setAlertId(alerts.getAlertId());
        alertsRecord.setUserName(username);
        alertsRecord.setOperationType("1");
        alertsRecord.setContent(content);
        if (index == 1) {
            alertsRecord.setOperationStatus("1");
        } else {
            alertsRecord.setOperationStatus("0");
        }
        alertsRecordDao.insert(alertsRecord);
        return new ResponseEntity<String>("success", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> alertRemoveByOrderId(String username, String orderId, String content) {
        AlertsV2 alerts = alertsV2Dao.selectByOrderId(orderId);
        AlertsOperationRequestVo alertsOperationRequestVo = new AlertsOperationRequestVo();
        alertsOperationRequestVo.setAlertIds(alerts.getAlertId());
        alertsOperationRequestVo.setAutoType(-1);
        alertsOperationRequestVo.setContent(content);
        alertsOperationRequestVo.setUserName(username);
        alertsBizV2.manualClear(jacksonBaseParse(AlertsOperationRequestVo.class, alertsOperationRequestVo));
        return new ResponseEntity<String>("success", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> updateOrderByOrderId(String username,String oldOrderId,String newOrderId) {
        alertsV2Dao.updateOrderByOrderId(oldOrderId,newOrderId);
        AlertsV2 alerts = alertsV2Dao.selectByOrderId(newOrderId);
        AlertsRecord alertsRecord = new AlertsRecord();
        alertsRecord.setAlertId(alerts.getAlertId());
        alertsRecord.setUserName(username);
        alertsRecord.setOperationType("2");
        alertsRecord.setContent("升级为调优工单");
        alertsRecord.setOperationStatus("1");
        alertsRecordDao.insert(alertsRecord);
        return null;
    }
}
