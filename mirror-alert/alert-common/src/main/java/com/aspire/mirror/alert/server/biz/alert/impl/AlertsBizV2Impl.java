package com.aspire.mirror.alert.server.biz.alert.impl;

import com.alibaba.fastjson.JSON;
import com.aspire.mirror.alert.server.annotation.DataToKafka;
import com.aspire.mirror.alert.server.biz.bpm.IBpmTaskService;
import com.aspire.mirror.alert.server.biz.helper.AlertScheduleIndexHelper;
import com.aspire.mirror.alert.server.dao.alert.*;
import com.aspire.mirror.alert.server.dao.alert.po.AlertsDetail;
import com.aspire.mirror.alert.server.dao.alert.po.AlertsRecord;
import com.aspire.mirror.alert.server.dao.alert.po.AlertsTransfer;
import com.aspire.mirror.alert.server.dao.bpm.AlertBpmTuningRecordDao;
import com.aspire.mirror.alert.server.dao.bpm.po.AlertTuningRecord;
import com.aspire.mirror.alert.server.dao.notify.AlertsNotifyDao;
import com.aspire.mirror.alert.server.dao.notify.po.AlertsNotify;
import com.aspire.mirror.alert.server.vo.bpm.AlertBpmCallBack;
import com.aspire.mirror.alert.server.vo.bpm.AlertBpmStartCallBack;
import com.aspire.mirror.alert.server.config.properties.AlertPingStatusProperties;
import com.aspire.mirror.alert.server.constant.AlertCommonConstant;
import com.aspire.mirror.alert.server.constant.Constants;
import com.aspire.mirror.alert.server.vo.alert.AlertsOperationRequestVo;
import com.aspire.mirror.alert.server.vo.alert.AutoConfirmClearVo;
import com.aspire.mirror.alert.server.vo.alert.AlertMonitorObjectVo;
import com.aspire.mirror.alert.server.util.TransformUtils;
import com.aspire.mirror.alert.server.biz.model.AlertFieldBiz;
import com.aspire.mirror.alert.server.biz.alert.AlertsBizV2;
import com.aspire.mirror.alert.server.biz.alert.AlertsHisBizV2;
import com.aspire.mirror.alert.server.biz.derive.IAlertDeriveAlertsBizV2;
import com.aspire.mirror.alert.server.constant.AlertConfigConstants;
import com.aspire.mirror.alert.server.dao.isolate.AlertIsolateAlertsV2Mapper;
import com.aspire.mirror.alert.server.vo.model.AlertFieldVo;
import com.aspire.mirror.alert.server.dao.alert.po.AlertsV2;
import com.aspire.mirror.alert.server.vo.alert.AlertsV2Vo;
import com.aspire.mirror.alert.server.util.AlertModelCommonUtil;
import com.aspire.mirror.alert.server.util.Criteria;
import com.aspire.mirror.common.constant.Constant;
import com.aspire.mirror.common.entity.PageResponse;
import com.aspire.mirror.common.util.DateUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @BelongsProject: mirror-alert
 * @BelongsPackage: com.aspire.mirror.alert.server.v2.biz.impl
 * @Author: baiwenping
 * @CreateTime: 2020-02-25 15:22
 * @Description: ${Description}
 */
@Slf4j
@Service
public class AlertsBizV2Impl implements AlertsBizV2 {
    @Autowired
    private AlertsV2Dao alertsV2Dao;
    @Autowired
    private AlertsHisBizV2 alertsHisBizV2;
    @Autowired
    private AlertsDetailDao alertsDetailDao;

    @Autowired
    private IBpmTaskService iBpmTaskService;

    @Autowired
    private AutoConfirmClearDao autoConfirmClearDao;

    @Autowired
    private AlertsRecordDao alertsRecordDao;

    @Autowired
    private AlertsTransferDao alertsTransferDao;

    @Autowired
    private AlertBpmTuningRecordDao alertBpmTuningRecordDao;

    @Autowired
    private AlertFieldBiz alertFieldBiz;

    @Autowired
    private AlertsNotifyDao alertsNotifyDao;

    @Autowired
    private AlertScheduleIndexHelper alertScheduleIndexHelper;

    @Autowired
    private AlertIsolateAlertsV2Mapper alertIsolateAlertsV2Mapper;

    @Autowired
    private IAlertDeriveAlertsBizV2 alertDeriveAlertsBizV2;

    @Autowired
    private AlertPingStatusProperties alertPingStatusConfig;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    /**
     * 根据条件查询
     * @param alertQuery
     * @return
     */
    public List<AlertsV2Vo> select(AlertsV2 alertQuery) {
        List<AlertsV2> list = alertsV2Dao.select(alertQuery);
        return TransformUtils.transform(AlertsV2Vo.class, list);
    }

    /**
     * 修改告警数据
     *
     * @param map 告警修改对象
     * @return 影响数据条数
     */
    public int updateByPrimaryKey (Map<String, Object> map) {
        return alertsV2Dao.updateByPrimaryKey(map);
    }

    /**
     * 新增告警数据
     *
     * @param map 告警修改对象
     * @return 影响数据条数
     */
    @DataToKafka(index = "alert_alerts")
    public String insert (Map<String, Object> map) {
        String alertId = MapUtils.getString(map, AlertConfigConstants.ALERT_ID);
        if (StringUtils.isEmpty(alertId)) {
            alertId = UUID.randomUUID().toString();
            map.put(AlertConfigConstants.ALERT_ID, alertId);
        }
        alertsV2Dao.insert(map);
        return alertId;
    }

    /**
     * @param alertIdArrays 告警ID集合
     * @return
     */
    @Override
    public int deleteByPrimaryKeyArrays(String[] alertIdArrays) {
        if (ArrayUtils.isEmpty(alertIdArrays)) {
            log.error("method[deleteByPrimaryKeyArrays] param[alertIdArrays] is null");
            throw new RuntimeException("param[alertIdArrays] is null");
        }
        return alertsV2Dao.deleteByPrimaryKeyArrays(alertIdArrays);
    }

    /**
     * 根据告警id删除上报记录
     *
     * @param alertId
     */
    public void deleteAlertsDetail(String alertId) {
        alertsDetailDao.deleteByAlertId(alertId);
    }

    /**
     * 新增告警上报记录
     *
     * @param alertDetail
     */
    public void insertAlertsDetail(AlertsDetail alertDetail) {
        alertsDetailDao.insert(alertDetail);
    }

    /**
     * 根据主键查询alert
     *
     * @param alertId 告警ID
     * @return AlertsDTO 告警对象
     */
    public AlertsV2Vo selectAlertByPrimaryKey(String alertId) {
        if (StringUtils.isEmpty(alertId)) {
            log.warn("method[selectByPrimaryKey] param[alertId] is null");
            return null;
        }
        AlertsV2 alerts = alertsV2Dao.selectByPrimaryKey(alertId);

        if (alerts == null) {
            return null;
        }
        AlertsV2Vo alertsDTO = TransformUtils.transform(AlertsV2Vo.class, alerts);
        return alertsDTO;
    }

    /**
     * 根据id更新告警监控时间
     * @param alertId
     * @param curMoniTime
     */
    public void updateCurMoniTime(String alertId, Date curMoniTime) {
        Map<String, Object> map = Maps.newHashMap();
        map.put("alertId", alertId);
        map.put("curMoniTime", curMoniTime);
        alertsV2Dao.updateCurMoniTime(map);
    }

    /**
     * 根据Id查询告警全部字段数据
     * @param alertIds
     * @return
     */
    public List<Map<String, Object>> selectByIds (List<String> alertIds) {
        List<Map<String, Object>> mapList = alertsV2Dao.selectByIds(alertIds);
        for (Map<String, Object> map:  mapList) {
            alertScheduleIndexHelper.pushDictAlert(map);
        }
        return mapList;
    }

    /**
     * 根据条件获取所有实例
     *
     * @param example
     * @return
     */
    public List<Map<String, Object>> list(Criteria example) {
        return alertsV2Dao.listByEntity(example);
    }

    /**
     * 分页查询
     *
     * @param example
     * @return
     */
    public PageResponse<Map<String, Object>> findPage(Criteria example) {
        List<Map<String, Object>> pageWithResult = alertsV2Dao.findPageWithResult(example);
        Integer pageWithCount = alertsV2Dao.findPageWithCount(example);
        PageResponse<Map<String, Object>> page = new PageResponse<>();
        page.setCount(pageWithCount);
        page.setResult(pageWithResult);
        return page;
    }

    /**
     * 查询详情
     * @auther baiwenping
     * @Description
     * @Date 14:58 2020/3/12
     * @Param [alertId]
     * @return java.util.Map<java.lang.String,java.lang.Object>
     **/
    public Map<String, Object> detailById(String alertId) {
        Map<String, Object> alert = alertsV2Dao.detailById(alertId);
//        alertScheduleIndexHelper.pushDictAlert(alert);
        return alert;
    }

    @Override
    public List<AlertMonitorObjectVo> getMonitObjectList() {
        return alertsV2Dao.getMonitObjectList();
    }

    /**
     * 告警转派
     *
     * @param alertIds 派单的告警ID列表
     */
    @Override
    @Transactional
    public void alertTransfer(String namespace, String alertIds, String userIds) {
        String[] alertIdArrays = alertIds.split(",");
        String[] userIdArrays = userIds.split(",");

        if (alertIdArrays.length > 0 && userIdArrays.length > 0) {
            for (int i = 0; i < alertIdArrays.length; i++) {
                String alertId = alertIdArrays[i];

                for (int j = 0; j < userIdArrays.length; j++) {
                    String userId = userIdArrays[j];
                    AlertsTransfer alertsTransfer = new AlertsTransfer();
                    alertsTransfer.setAlertId(alertId);
                    alertsTransfer.setUserName(namespace);
                    alertsTransfer.setConfirmUserName(userId);
                    alertsTransferDao.insert(alertsTransfer);
                }

                AlertsRecord alertsRecord = new AlertsRecord();
                alertsRecord.setAlertId(alertId);
                alertsRecord.setUserName(namespace);
                alertsRecord.setOperationType("0");
                String content = "转派给" + userIds;
                alertsRecord.setContent(content);

                AlertsV2 alerts = alertsV2Dao.selectByPrimaryKey(alertId);
                if (alerts.getOperateStatus() == 0) {
                    alertsRecord.setOperationStatus("1");
                } else {

                    alerts.setOperateStatus(0);
                    int index = alertsV2Dao.updateByPrimaryKey(AlertModelCommonUtil.generateAlerts(alerts, alertFieldBiz.getModelFromRedis(AlertConfigConstants.REDIS_MODEL_ALERT, null)));

                    if (index == 1) {
                        alertsRecord.setOperationStatus("1");
                    } else {
                        alertsRecord.setOperationStatus("0");
                    }

                }

                //设置为待确认状态

                alertsRecordDao.insert(alertsRecord);

            }

        }


    }

    /**
     * 告警确认
     */
    @Override
    @Transactional
    public void alertConfirm(AlertsOperationRequestVo request) {
        String[] alertIdArrays = request.getAlertIds().split(",");
        if (alertIdArrays.length > 0) {
            for (String alertId : alertIdArrays) {
                AlertsV2 alerts = alertsV2Dao.selectByPrimaryKey(alertId);
                //设置为已确认状态
                alerts.setOperateStatus(1);
                Map<String, Object> p = Maps.newHashMap();
                p.put("alert_id",alerts.getAlertId());
                p.put("operate_status",alerts.getOperateStatus());
                int index = alertsV2Dao.updateByPrimaryKey(p);
//                int index = alertsV2Dao.updateByPrimaryKey(AlertV2CommonUtils.generateAlerts(alerts, alertFieldBiz.getModelFromRedis(AlertConfigConstants.REDIS_MODEL_ALERT, null)));
                AlertsRecord alertsRecord = new AlertsRecord();
                alertsRecord.setAlertId(alertId);
                alertsRecord.setUserName(request.getUserName());
                alertsRecord.setOperationType("1");
                alertsRecord.setContent(request.getContent());
                if (index == 1) {
                    alertsRecord.setOperationStatus("1");
                } else {
                    alertsRecord.setOperationStatus("0");
                }
                alertsRecordDao.insert(alertsRecord);
                if (request.getAutoType() != -1) {
                    AutoConfirmClearVo autoConfirmClearId = autoConfirmClearDao.getAutoConfirmClearId(
                            alerts.getDeviceIp(),
                            alerts.getIdcType(),
                            alerts.getBizSys(),
                            alerts.getAlertLevel(),
                            alerts.getSource(),
                            alerts.getItemId(),
                            request.getAutoType(),
                            null);
                    if (null == autoConfirmClearId) {
                        AutoConfirmClearVo autoConfirmClearVo = new AutoConfirmClearVo();
                        autoConfirmClearVo.setUuid(UUID.randomUUID().toString());
                        autoConfirmClearVo.setDeviceIp(alerts.getDeviceIp());
                        autoConfirmClearVo.setIdcType(alerts.getIdcType());
                        autoConfirmClearVo.setBizSys(alerts.getBizSys());
                        autoConfirmClearVo.setAlertLevel(alerts.getAlertLevel());
                        autoConfirmClearVo.setSource(alerts.getSource());
                        autoConfirmClearVo.setItemId(alerts.getItemId());
                        autoConfirmClearVo.setAutoType(request.getAutoType());
                        autoConfirmClearVo.setContent(request.getContent());
                        autoConfirmClearVo.setStartTime(request.getStartTime());
                        autoConfirmClearVo.setEndTime(request.getEndTime());
                        autoConfirmClearVo.setOperator(request.getUserName());
                        autoConfirmClearDao.insert(autoConfirmClearVo);
                    }
                }
            }
        }

    }

    @Override
    public void alertObserve(Map<String, Object> request) {
        String[] alertIdArrays = String.valueOf(request.get("alertIds")).split(",");
        if (alertIdArrays.length > 0) {
            for (String alertId : alertIdArrays) {
                AlertsV2 alerts = alertsV2Dao.selectByPrimaryKey(alertId);
                //设置为待观察状态
                alerts.setOperateStatus(4);
                Map<String, Object> p = Maps.newHashMap();
                p.put("alert_id",alerts.getAlertId());
                p.put("operate_status",alerts.getOperateStatus());
                int index = alertsV2Dao.updateByPrimaryKey(p);
//                int index = alertsV2Dao.updateByPrimaryKey(AlertV2CommonUtils.generateAlerts(alerts, alertFieldBiz.getModelFromRedis(AlertConfigConstants.REDIS_MODEL_ALERT, null)));
                AlertsRecord alertsRecord = new AlertsRecord();
                alertsRecord.setAlertId(alertId);
                alertsRecord.setUserName(String.valueOf(request.get("username")));
                alertsRecord.setOperationType("1");
                alertsRecord.setContent("活动告警转待观察");
                if (index == 1) {
                    alertsRecord.setOperationStatus("1");
                } else {
                    alertsRecord.setOperationStatus("0");
                }
                alertsRecordDao.insert(alertsRecord);
            }
        }
    }

    /**
     * 派单分流
     *
     * @param namespace
     * @param alertIds
     * @param orderType
     * @return
     */
    @Override
    public AlertBpmStartCallBack switchOrder(String namespace, String alertIds, Integer orderType) {
        AlertBpmStartCallBack message = null;
        String orderTpye = orderType.toString();
        switch (orderTpye) {
            case Constants.ORDER_TUNING: // 告警调优工单
                message = genTuningOder(namespace, alertIds, orderTpye);
                break;
            default: // 其它工单类型
                String genMessage = genOrder(namespace, alertIds, orderType);
                message = new AlertBpmStartCallBack();
                if (genMessage.length()>8 && genMessage.substring(0,8).equals("success:")) {
                    String successNum = genMessage.substring(8,9);
                    if (StringUtils.isNotEmpty(successNum)) {
                        message.setSuccess(Integer.valueOf(successNum));
                    }
                    if (genMessage.contains("_")) {
                        message.setOrderIdList(genMessage.split("_")[1]);
                    }
                } else { // 失败
                    message.setStatus(false);
                    message.setMessage(genMessage);
                }
                break;
        }
        return message;
    }

    /**
     * 手动派单-调优工单
     * @param namespace
     * @param alertIds
     * @param orderType
     * @return
     */
    private AlertBpmStartCallBack genTuningOder(String namespace, String alertIds, String orderType) {
        AlertBpmStartCallBack callBack = new AlertBpmStartCallBack();
        int successNum = 0;
        String[] alertIdArrays = alertIds.split(",");
        List<String> orderIdList = Lists.newArrayList();
        for (String alertId : alertIdArrays) {
            Map param = Maps.newHashMap();
            param.put("alertIdArrays", new String[]{alertId});
            List<Map<String, Object>> list = alertsV2Dao.selectOrderParam1(param);
            if (CollectionUtils.isEmpty(list)) {
                continue;
            }
            AlertTuningRecord recordInDB = alertBpmTuningRecordDao.select(alertId);
            if (recordInDB != null) {
                log.info("此告警已发起过调优工单，跳过!");
                successNum++;
                continue;
            }
            Map<String, Object> alertMap = list.get(0);
            if ("1".equals(alertMap.get("object_type"))) { // 设备类型告警
                AlertBpmCallBack tuningCallBack = iBpmTaskService.callBpmFlowStart(namespace, alertMap, orderType);
                if ("1".equals(tuningCallBack.getStatus())) { // 发起调优工单成功
                    successNum++;
                    String runId = tuningCallBack.getRunId(); // 工单实例ID
                    orderIdList.add(runId);
                    // 录入调优工单记录表
                    AlertTuningRecord record = new AlertTuningRecord();
                    record.setAlertId(alertId);
                    record.setOrderId(runId); // 工单实例ID
                    record.setOrderType(orderType); // 工单类型
                    record.setOrderStatus(Constant.ORDER_DEALING); // 处理中
                    alertBpmTuningRecordDao.insert(record);
                    // 录入告警操作记录表
                    AlertsRecord alertsRecord = new AlertsRecord();
                    alertsRecord.setAlertId(alertId);
                    alertsRecord.setUserName(namespace);
                    alertsRecord.setOperationType("2"); // 派发工单
                    alertsRecord.setContent(Constant.TUNING_ORDER);
                    alertsRecord.setOperationStatus("1");
                    alertsRecord.setOperationTime(new Date());
                    alertsRecordDao.insert(alertsRecord);
                } else {
                    log.error("告警调优工单发起失败, alertId: {}, {}! ", alertId, tuningCallBack.getMessage());
                    continue;
                }
            } else {
                log.info("非设备类型告警，不能发起告警调优工单，跳过！");
                continue;
            }
        }
        callBack.setTotal(alertIdArrays.length);
        callBack.setSuccess(successNum);
        if (successNum == 0) {
            callBack.setStatus(false);
            callBack.setMessage("工单生成失败");
        }
        if (!CollectionUtils.isEmpty(orderIdList)) callBack.setOrderIdList(String.join(",", orderIdList));
        return callBack;
    }

    /**
     * 手动派单
     *
     * @param alertIds 派单的告警ID列表
     * @param orderType
     */
    @Override
    public String genOrder(String namespace, String alertIds, Integer orderType) {

        String[] alertIdArrays = alertIds.split(",");

        Map paramMap = Maps.newHashMap();
        // 工单状态：未派单
//        paramMap.put("orderStatus", Constant.ORDER_BEFOR);
        // 告警ID列表
        paramMap.put("alertIdArrays", alertIdArrays);
        List<Map<String, Object>> list = alertsV2Dao.selectOrderParam1(paramMap);
        List<Map<String, Object>> unSend = list.stream().filter(p->p.get("order_status").equals(Constant.ORDER_BEFOR)
                                            ||p.get("order_status").equals("4")).collect(Collectors.toList());
        //根据orderType过滤需要派单的告警 如果orderType=1，则只给未派单的告警派单；
        // 如果orderType=2，则只给未派单的告警或派单类型为告警工单的告警事件派单；
        // 如果orderType=3，则只给未派单的告警或派单类型不是维保工单的告警事件派单。
        List<Map<String, Object>> newList = new ArrayList<>();
        newList.addAll(unSend);
        log.info("#=====> orderType: {}" , orderType);
        if (orderType.toString().equals(Constants.ORDER_HITCH)){
            List<Map<String, Object>> list1 = list.stream().filter(p->p.containsKey("order_type"))
                    .filter(p->p.get("order_type").equals(Constants.ORDER_ALERT)).collect(Collectors.toList());
            newList.addAll(list1);
        }else if (orderType.toString().equals(Constants.ORDER_MAINTENANCE)){
            List<Map<String, Object>> list2 = list.stream().filter(p->p.containsKey("order_type"))
                    .filter(p->!p.get("order_type").equals(Constants.ORDER_MAINTENANCE)).collect(Collectors.toList());
            newList.addAll(list2);
        }
        String message = iBpmTaskService.alertHandleBpmResult(newList, AlertCommonConstant.NUM.ONE, namespace,orderType);
        return message;
    }

    /**
     * 修改通知状态
     * @auther baiwenping
     * @Description
     * @Date 18:52 2020/3/23
     * @Param [unserName, alertIds]
     * @return void
     **/
    public void notifyStatus(String status, List<String> alertIds) {
        if (StringUtils.isEmpty(status)) {
            return;
        }
        List<AlertsV2> alertList = alertsV2Dao.selectByPrimaryKeyArrays(alertIds.toArray(new String[0]));
        List<String> ids = Lists.newArrayList();
        for (AlertsV2 alert: alertList) {
            if (!status.equals(alert.getNotifyStatus())) {
                ids.add(alert.getAlertId());
            }
        }
        if (ids.size() > 0) {
            alertsV2Dao.updateNotifyStatus(ids, status);
            for (String alertId: ids) {
                AlertsNotify alertsNotify = new AlertsNotify();
                alertsNotify.setAlertId(alertId);
                alertsNotify.setUserName("无");
                alertsNotify.setReportType("3");     //其他途径
//            alertsNotify.setDestination(dest);
                alertsNotify.setMessage("其他途径");
                alertsNotify.setStatus(Constants.ISOLATE_STATUS_OPEN); //成功

                alertsNotifyDao.insert(alertsNotify);
            }
        }

    }

    /**
     * 手工清除指定告警id的告警. <br/>
     * <p>
     *
     */
    @Transactional
    public void manualClear(AlertsOperationRequestVo request) {
        String[] alertIdArrays = request.getAlertIds().split(",");
        if (alertIdArrays.length > 0) {
            List<AlertsV2> alertList = alertsV2Dao.selectByPrimaryKeyArrays(alertIdArrays);
            this.manualClear(alertList, request.getUserName(), request.getContent());
            for (AlertsV2 alertsDTO : alertList) {
                AlertsRecord alertsRecord = new AlertsRecord();
                alertsRecord.setAlertId(alertsDTO.getAlertId());
                alertsRecord.setUserName(request.getUserName());
                alertsRecord.setOperationType("3");
                alertsRecord.setContent(request.getContent());
                alertsRecord.setOperationStatus("1");
                alertsRecordDao.insert(alertsRecord);
                if (request.getAutoType() != null && request.getAutoType() != -1) {
                    AutoConfirmClearVo autoConfirmClearId = autoConfirmClearDao.getAutoConfirmClearId(
                            alertsDTO.getDeviceIp(),
                            alertsDTO.getIdcType(),
                            alertsDTO.getBizSys(),
                            alertsDTO.getAlertLevel(),
                            alertsDTO.getSource(),
                            alertsDTO.getItemId(),
                            request.getAutoType(),
                            null);
                    if (null == autoConfirmClearId) {
                        AutoConfirmClearVo autoConfirmClearVo = new AutoConfirmClearVo();
                        autoConfirmClearVo.setUuid(UUID.randomUUID().toString());
                        autoConfirmClearVo.setDeviceIp(alertsDTO.getDeviceIp());
                        autoConfirmClearVo.setIdcType(alertsDTO.getIdcType());
                        autoConfirmClearVo.setBizSys(alertsDTO.getBizSys());
                        autoConfirmClearVo.setAlertLevel(alertsDTO.getAlertLevel());
                        autoConfirmClearVo.setSource(alertsDTO.getSource());
                        autoConfirmClearVo.setItemId(alertsDTO.getItemId());
                        autoConfirmClearVo.setAutoType(request.getAutoType());
                        autoConfirmClearVo.setContent(request.getContent());
                        autoConfirmClearVo.setStartTime(request.getStartTime());
                        autoConfirmClearVo.setEndTime(request.getEndTime());
                        autoConfirmClearVo.setOperator(request.getUserName());
                        autoConfirmClearDao.insert(autoConfirmClearVo);
                    }
                }
            }
        }

    }

    /**
     * 手工清除告警.  <br/>
     * <p>
     *
     * @param alertList
     */
    private void manualClear(final List<AlertsV2> alertList, String namespace, String content) {

        Date nowTime = new Date();
        String newStr = DateUtil.format(nowTime, "yyyy-MM-dd HH:mm:ss");
        String[] ids = new String[alertList.size()];
        List<AlertFieldVo> modelFromRedis = alertFieldBiz.getModelFromRedis(AlertConfigConstants.REDIS_MODEL_ALERT_HIS, null);
        for (int i = 0;i < alertList.size(); i++) {
            AlertsV2 alert = alertList.get( i );
            Map<String, Object> map = alertsV2Dao.detailById(alert.getAlertId());
            for (Map.Entry<String, Object> entry: map.entrySet()) {
                Object value = entry.getValue();
                if (value == null) {
                    continue;
                }
                if (value instanceof Timestamp) {
                    map.put(entry.getKey(), DateUtil.format(new Date(((Timestamp) value).getTime()), "yyyy-MM-dd HH:mm:ss"));
                }
            }
            if (StringUtils.isNotEmpty(alert.getOrderId())) {
                map.put("order_status", Constants.ORDER_END);
                if (!AlertCommonConstant.CLEAR_ALERT_CONTENT_BYORDER.equalsIgnoreCase(content)) {
                    String message = iBpmTaskService.closeBpmInstance(alert.getOrderId(), content);
                    if ("ERROR".equals(message)) {
                        map.put("order_status", Constants.ORDER_ERROR);
                    }
                }
            }

            map.put("alert_end_time", newStr);
            map.put("clear_user", namespace);
            map.put("clear_content", content);
            alertsHisBizV2.insert(AlertModelCommonUtil.generateAlerts(map, modelFromRedis));
            ids[i] = alert.getAlertId();
            if (AlertConfigConstants.SOURCE_DERIVE.equals(alert.getSource())) {
                // 删除衍生日志
                alertDeriveAlertsBizV2.deleteByAlertId(alert.getAlertId(), alertFieldBiz.getModelField(AlertConfigConstants.REDIS_MODEL_DERIVE_ALERT_HIS));
            }
        }
        deleteByPrimaryKeyArrays(ids);
    }

    /**
     * 告警通知记录日志
     * @param type
     * @param namespace
     * @param alertIds
     * @param destination
     * @param message
     * @param status
     */
    @Override
    public void recordNotifyLog(String type, String namespace, String alertIds, List<String> destination, String message, String status) {
        //邮件类型
        String reportType = "1";
        String contentRecord = "告警邮件";
        if (AlertConfigConstants.MESSAGE_TYPE_SMS.equals(type)) {
            reportType = "0"; //短信类型
            contentRecord = "告警短信";
        }
        String[] alertIdArrays = alertIds.split(",");

        if (alertIdArrays.length > 0 && destination.size() > 0) {
            for (int i = 0; i < alertIdArrays.length; i++) {
                String alertId = alertIdArrays[i];
                for (String dest: destination) {
                    AlertsNotify alertsNotify = new AlertsNotify();
                    alertsNotify.setAlertId(alertId);
                    alertsNotify.setUserName(namespace);
                    alertsNotify.setReportType(reportType);    //短信
                    alertsNotify.setDestination(dest);
                    alertsNotify.setMessage(message);
                    alertsNotify.setStatus(status);
                    alertsNotifyDao.insert(alertsNotify);
                }

                AlertsRecord alertsRecord = new AlertsRecord();
                alertsRecord.setAlertId(alertId);
                alertsRecord.setUserName(namespace);
                alertsRecord.setOperationType("4");
                alertsRecord.setContent(contentRecord);
                alertsRecord.setOperationStatus(status);
                alertsRecordDao.insert(alertsRecord);

            }
            alertsV2Dao.updateNotifyStatus(Arrays.asList(alertIdArrays), AlertConfigConstants.YES);
        }
    }

    public void updateNotifyStatus (List<String> alertIds, String status) {
        alertsV2Dao.updateNotifyStatus(alertIds, status);
    }

    /**
     *
     * @param deviceIds
     * @return
     */
    public List<Map<String, Object>> getDeviceNewestAlertLevelList(List<String> deviceIds) {
        if (deviceIds == null) {
            deviceIds = Lists.newArrayList();
        }
        return alertsV2Dao.getDeviceNewestAlertLevelList(deviceIds);
    }

    /**
     *
     * @param itemIds
     * @return
     */
    public List<Map<String, Object>> getItemNewestAlertLevelList(String prefix, List<String> itemIds) {
        if (itemIds == null) {
            itemIds = Lists.newArrayList();
        }
        return alertsV2Dao.getItemNewestAlertLevelList(prefix, itemIds);
    }

    /**
     * 分页查询
     *
     * @param example
     * @return
     */
    public PageResponse<Map<String, Object>> queryDeviceAlertList(Criteria example) {
        List<Map<String, Object>> pageWithResult = alertsV2Dao.queryDeviceAlertList(example);
        Integer pageWithCount = alertsV2Dao.queryDeviceAlertCount(example);
        PageResponse<Map<String, Object>> page = new PageResponse<>();
        page.setCount(pageWithCount);
        page.setResult(pageWithResult);
        return page;
    }

    /**
     * 根据级别统计告警所属设备数
     * @param example
     * @return
     */
    public List<Map<String, Object>> summaryDeviceAlertsByLevel(Criteria example) {
        return alertsV2Dao.summaryDeviceAlertsByLevel(example);
    }

    /**
     *
     * @param list
     * @return
     */
    public List<String> checkOrderStatus(List<String> list) {
        List<Map<String, Object>> alertOrderList = alertsV2Dao.checkOrderStatus(list);
        List<String> result = alertOrderList.stream().map(item -> {
            return MapUtils.getString(item, "order_id");
        }).collect(Collectors.toList());
        List<Map<String, Object>> isolateOrderList = alertIsolateAlertsV2Mapper.checkOrderStatus(list);
        result.addAll(isolateOrderList.stream().map(item -> {
            return MapUtils.getString(item, "order_id");
        }).collect(Collectors.toList()));
        return result;
    }

    /**
     * 发送ping状态到kafka，用于cmdb消费
     * @param alert
     */
    @Async
    public void putPingStatusToKafka(AlertsV2Vo alert, String deviceType) {
        if (!alertPingStatusConfig.isFlag() || alert == null) {
            return;
        }
        String keyComment = alert.getKeyComment();
        if (StringUtils.isEmpty(keyComment)) {
            return;
        }
        if (keyComment.equals(alertPingStatusConfig.getManageIpTitle())) {
            sendPingStatus("status[manageIp]", deviceType, alert);
        } else if (keyComment.equals(alertPingStatusConfig.getIpmiIpTitle())) {
            sendPingStatus("status[ipmiIp]", deviceType, alert);
        } else if (keyComment.equals(alertPingStatusConfig.getServiceIpTitle())) {
            sendPingStatus("status[serviceIp]", deviceType, alert);
        }
    }

    /**
     * 发送ping状态
     * @param key
     * @param alert
     */
    private void sendPingStatus (String key, String deviceType, AlertsV2Vo alert) {
        String alertType = alert.getAlertType();
        // 如果不是告警或者消警，则不处理
        if (!AlertsV2Vo.ALERT_ACTIVE.equals(alertType) && !AlertsV2Vo.ALERT_REVOKE.equals(alertType)) {
            return;
        }

        Map map = Maps.newHashMap();
        if (StringUtils.isNotEmpty(deviceType)) {
            map.put("device_type", deviceType);
        }
        map.put("ip", alert.getDeviceIp());
        map.put("pool", alert.getIdcType());
        map.put("source", "zbx");
        map.put("key_", key);
        // 判断是还是否
        if (AlertsV2Vo.ALERT_ACTIVE.equals(alertType)) {
            map.put("lastvalue",alertPingStatusConfig.getCmdbYes());
        } else if (AlertsV2Vo.ALERT_REVOKE.equals(alertType)) {
            map.put("lastvalue",alertPingStatusConfig.getCmdbNo());
        }
        String pingStatusString = JSON.toJSONString(map);
        kafkaTemplate.send(alertPingStatusConfig.getTopic(), pingStatusString);
        log.info("send ping status message to kafka, topic is: {}, message is : {}", alertPingStatusConfig.getTopic(), pingStatusString);
        map.put("key_", "latest_ping_time");
        Date curMoniTime = alert.getCurMoniTime();
        if (curMoniTime == null) {
            curMoniTime = new Date();
        }
        map.put("lastvalue",DateUtil.format(curMoniTime, DateUtil.DATE_TIME_CH_FORMAT));
        String lastTimeString = JSON.toJSONString(map);
        kafkaTemplate.send(alertPingStatusConfig.getTopic(), lastTimeString);
        log.info("send ping status message to kafka, topic is: {}, message is : {}", alertPingStatusConfig.getTopic(), lastTimeString);
    }

}
