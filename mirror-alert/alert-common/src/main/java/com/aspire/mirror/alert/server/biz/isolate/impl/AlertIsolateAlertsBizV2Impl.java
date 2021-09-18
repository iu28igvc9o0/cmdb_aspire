package com.aspire.mirror.alert.server.biz.isolate.impl;

import com.aspire.mirror.alert.server.biz.bpm.IBpmTaskService;
import com.aspire.mirror.alert.server.biz.isolate.IAlertIsolateAlertsHisBizV2;
import com.aspire.mirror.alert.server.constant.AlertCommonConstant;
import com.aspire.mirror.alert.server.constant.Constants;
import com.aspire.mirror.alert.server.biz.model.AlertFieldBiz;
import com.aspire.mirror.alert.server.biz.alert.AlertsBizV2;
import com.aspire.mirror.alert.server.biz.isolate.IAlertIsolateAlertsBizV2;
import com.aspire.mirror.alert.server.constant.AlertConfigConstants;
import com.aspire.mirror.alert.server.dao.isolate.AlertIsolateAlertsV2Mapper;
import com.aspire.mirror.alert.server.vo.alert.AlertsOperationRequestVo;
import com.aspire.mirror.alert.server.vo.model.AlertFieldVo;
import com.aspire.mirror.alert.server.util.AlertModelCommonUtil;
import com.aspire.mirror.alert.server.util.Criteria;
import com.aspire.mirror.common.constant.Constant;
import com.aspire.mirror.common.entity.PageResponse;
import com.aspire.mirror.common.util.DateUtil;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @BelongsProject: mirror-alert
 * @BelongsPackage: com.aspire.mirror.alert.server.v2.biz.impl
 * @Author: baiwenping
 * @CreateTime: 2020-02-29 01:03
 * @Description: ${Description}
 */
@Slf4j
@Service
public class AlertIsolateAlertsBizV2Impl implements IAlertIsolateAlertsBizV2 {
    @Autowired
    private AlertIsolateAlertsV2Mapper alertIsolateAlertsV2Mapper;
    @Autowired
    private IBpmTaskService iBpmTaskService;
    @Autowired
    private AlertFieldBiz alertFieldBiz;
    @Autowired
    private AlertsBizV2 alertsBizV2;
    @Autowired
    private IAlertIsolateAlertsHisBizV2 alertIsolateAlertsHisBizV2;
    /**
     * 根据条件获取所有实例
     *
     * @param example
     * @return
     */
    public List<Map<String, Object>> list(Criteria example) {
        return alertIsolateAlertsV2Mapper.listByEntity(example);
    }

    /**
     * 分页查询
     *
     * @param example
     * @return
     */
    public PageResponse<Map<String, Object>> findPage(Criteria example) {
        List<Map<String, Object>> pageWithResult = alertIsolateAlertsV2Mapper.findPageWithResult(example);
        Integer pageWithCount = alertIsolateAlertsV2Mapper.findPageWithCount(example);
        PageResponse<Map<String, Object>> page = new PageResponse<>();
        page.setCount(pageWithCount);
        page.setResult(pageWithResult);
        return page;
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
        List<Map<String, Object>> list = alertIsolateAlertsV2Mapper.selectByPrimaryKeyArrays(alertIdArrays);
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
        String message = iBpmTaskService.isolateHandleBpmResult(newList, AlertCommonConstant.NUM.ONE, namespace,orderType);
        return message;
    }

    /**
     * 屏蔽恢复告警
     * @param alertIds
     * @return
     */
    public Map<String, Object> alertRecovery(String[] alertIds) {
        Map<String, Object> map = Maps.newHashMap();
        List<Map<String, Object>> list = alertIsolateAlertsV2Mapper.selectByPrimaryKeyArrays(alertIds);
        List<AlertFieldVo> modelFieldList = alertFieldBiz.getModelField(AlertConfigConstants.REDIS_MODEL_ALERT);
        for (Map<String, Object> alert: list) {
            for (Map.Entry<String, Object> entry: alert.entrySet()) {
                Object value = entry.getValue();
                if (value == null) {
                    continue;
                }
                if (value instanceof Timestamp) {
                    alert.put(entry.getKey(), DateUtil.format(new Date(((Timestamp) value).getTime()), "yyyy-MM-dd HH:mm:ss"));
                }
            }
            alertsBizV2.insert(AlertModelCommonUtil.generateAlerts(alert, modelFieldList));
            alertIsolateAlertsV2Mapper.deleteById(MapUtils.getString(alert, "alert_id"));
        }
        map.put("code", "0000");
        return map;
    }

    /**
     * 手工清除指定告警id的告警. <br/>
     * <p>
     *
     */
    @Transactional
    public void remove(AlertsOperationRequestVo request) {
        String[] alertIdArrays = request.getAlertIds().split(",");
        if (alertIdArrays.length < 0) {
            return;
        }

        List<Map<String, Object>> queryList = alertIsolateAlertsV2Mapper.selectByPrimaryKeyArrays(alertIdArrays);
        if (CollectionUtils.isNotEmpty(queryList)) {
            for (int i = 0; i < queryList.size(); i++) {
                Map<String, Object> alertIsolateVo = queryList.get(i);
                alertIsolateVo.put("alert_count", org.apache.commons.collections.MapUtils.getIntValue(alertIsolateVo, "alert_count") + 1);
                alertIsolateVo.put("alert_end_time", DateUtil.formatDate(DateUtil.DATE_TIME_CH_FORMAT));
                alertIsolateVo.put("clear_user", request.getUserName());
                alertIsolateVo.put("clear_content", request.getContent());
                for (Map.Entry<String, Object> entry: alertIsolateVo.entrySet()) {
                    Object value = entry.getValue();
                    if (value == null) {
                        continue;
                    }
                    if (value instanceof Timestamp) {
                        alertIsolateVo.put(entry.getKey(), DateUtil.format(new Date(((Timestamp) value).getTime()), "yyyy-MM-dd HH:mm:ss"));
                    }
                }

                // 消警
                alertIsolateAlertsHisBizV2.insert(AlertModelCommonUtil.generateAlerts(alertIsolateVo, alertFieldBiz.getModelField(AlertConfigConstants.REDIS_MODEL_ISOLATE_ALERT_HIS)));
                String alertId = MapUtils.getString(alertIsolateVo, "alert_id");
                if (StringUtils.isNotEmpty(alertId)) {
                    alertIsolateAlertsV2Mapper.deleteById(alertId);
                }
                // 关闭工单
                String orderId = MapUtils.getString(alertIsolateVo, "order_id");
                if (StringUtils.isNotEmpty(orderId)) {
                    iBpmTaskService.closeBpm(orderId, "isolate");
                }
            }

        }
    }
}
