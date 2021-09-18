package com.aspire.mirror.alert.server.task.bpm;

import com.aspire.mirror.alert.server.biz.bpm.IBpmTaskService;
import com.aspire.mirror.alert.server.constant.AlertCommonConstant;
import com.aspire.mirror.alert.server.vo.alert.AlertsOperationRequestVo;
import com.aspire.mirror.alert.server.util.StringUtils;
import com.aspire.mirror.alert.server.biz.alert.AlertsBizV2;
import com.aspire.mirror.alert.server.biz.bpm.IAlertOrderHandleBiz;
import com.aspire.mirror.alert.server.util.Criteria;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
@ConditionalOnProperty(value = "bpm.closing.isOpen", havingValue = "true")
public class AlertOrderClosedCheckSchedule {

    @Autowired
    private IBpmTaskService bpmTaskService;
    @Autowired
    private AlertsBizV2 alertsBizV2;
    @Autowired
    private IAlertOrderHandleBiz alertOrderHandleBiz;

    @Scheduled(cron = "${bpm.closing.cron:0 0 */1 * * ?}")
    public void checkOrder () {
        log.info("工单一致性校验任务开始！");
        Criteria example = new Criteria();
        Criteria.Condition conditon = example.createConditon();
        conditon.andIsNotNull("order_id");
        example.setExportField("alert_id,order_id");
        List<Map<String, Object>> list = alertsBizV2.list(example);
        Map<String, String> orderMap = Maps.newHashMap();
        List<String> orderList = Lists.newArrayList();
//        List<String> alertIdList = Lists.newArrayList();
        AlertsOperationRequestVo request = new AlertsOperationRequestVo();
        request.setContent(AlertCommonConstant.CLEAR_ALERT_CONTENT_BYORDER);
        request.setUserName("alauda");
        request.setAutoType(-1);
        for (int i = 0; i < list.size(); i++) {
            Map<String, Object> map = list.get(i);
            String orderId = MapUtils.getString(map, "order_id");
            String alertId = MapUtils.getString(map, "alert_id");
            if (StringUtils.isEmpty(orderId)) {
                continue;
            }
            orderList.add(orderId);
            orderMap.put(orderId, alertId);
            if (i != 0 && i % 200 == 0) {
                List<String> closedOrderList = bpmTaskService.checkOrderIsEnd(orderList);
                if (!CollectionUtils.isEmpty(closedOrderList)) {
                    for (String closedOrderId: closedOrderList) {
//                        String closingAlertId = orderMap.get(closedOrderId);
//                        if (!StringUtils.isEmpty(closingAlertId)) {
//                            alertIdList.add(closingAlertId);
//                        }
                        if (StringUtils.isNotEmpty(closedOrderId)) {
                            alertOrderHandleBiz.deleteByOrderId(closedOrderId);
                        }
                    }
//                    request.setAlertIds(Joiner.on(",").join(alertIdList));
//                    alertIdList.clear();
//                    alertsBizV2.manualClear(request);
                }
                orderList.clear();
                orderMap.clear();
            }
        }

        if (!orderList.isEmpty()) {
            List<String> closedOrderList = bpmTaskService.checkOrderIsEnd(orderList);
            if (!CollectionUtils.isEmpty(closedOrderList)) {
                for (String closedOrderId: closedOrderList) {
//                    String closingAlertId = orderMap.get(closedOrderId);
//                    if (!StringUtils.isEmpty(closingAlertId)) {
//                        alertIdList.add(closingAlertId);
//                    }
                    if (StringUtils.isNotEmpty(closedOrderId)) {
                        alertOrderHandleBiz.deleteByOrderId(closedOrderId);
                    }
                }
//                request.setAlertIds(Joiner.on(",").join(alertIdList));
//                alertsBizV2.manualClear(request);
            }
        }
        log.info("工单一致性校验任务结束！");
    }
}
