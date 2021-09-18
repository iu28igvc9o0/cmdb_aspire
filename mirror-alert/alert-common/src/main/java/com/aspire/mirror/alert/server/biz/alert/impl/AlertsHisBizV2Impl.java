package com.aspire.mirror.alert.server.biz.alert.impl;

import com.aspire.mirror.alert.server.annotation.DataToKafka;
import com.aspire.mirror.alert.server.biz.helper.AlertScheduleIndexHelper;
import com.aspire.mirror.alert.server.biz.alert.AlertsHisBizV2;
import com.aspire.mirror.alert.server.constant.AlertConfigConstants;
import com.aspire.mirror.alert.server.dao.alert.AlertsHisV2Dao;
import com.aspire.mirror.alert.server.util.Criteria;
import com.aspire.mirror.common.entity.PageResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @BelongsProject: mirror-alert
 * @BelongsPackage: com.aspire.mirror.alert.server.v2.biz.impl
 * @Author: baiwenping
 * @CreateTime: 2020-02-26 14:55
 * @Description: ${Description}
 */
@Slf4j
@Service
public class AlertsHisBizV2Impl implements AlertsHisBizV2{

    @Autowired
    private AlertsHisV2Dao alertsHisV2Dao;
    @Autowired
    private AlertScheduleIndexHelper alertScheduleIndexHelper;
    /**
     * 新增告警历史数据
     *
     * @param map 告警修改对象
     * @return 影响数据条数
     */
    @DataToKafka(index = "alert_alerts_his")
    public String insert (Map<String, Object> map) {
        String alertId = MapUtils.getString(map, AlertConfigConstants.ALERT_ID);
        if (StringUtils.isEmpty(alertId)) {
            alertId = UUID.randomUUID().toString();
            map.put(AlertConfigConstants.ALERT_ID, alertId);
        }
        alertsHisV2Dao.insert(map);
        return alertId;
    }

    /**
     * 根据条件获取所有实例
     *
     * @param example
     * @return
     */
    public List<Map<String, Object>> list(Criteria example) {
        return alertsHisV2Dao.listByEntity(example);
    }

    /**
     * 分页查询
     *
     * @param example
     * @return
     */
    public PageResponse<Map<String, Object>> findPage(Criteria example) {
        List<Map<String, Object>> pageWithResult = alertsHisV2Dao.findPageWithResult(example);
        Integer pageWithCount = alertsHisV2Dao.findPageWithCount(example);
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
        Map<String, Object> alert = alertsHisV2Dao.detailById(alertId);
//        alertScheduleIndexHelper.pushDictAlert(alert);
        return alert;
    }

    /**
     * 根据Id查询告警全部字段数据
     * @param alertIds
     * @return
     */
    public List<Map<String, Object>> selectByIds (List<String> alertIds) {
        List<Map<String, Object>> mapList = alertsHisV2Dao.selectByIds(alertIds);
        for (Map<String, Object> map:  mapList) {
            alertScheduleIndexHelper.pushDictAlert(map);
        }
        return mapList;
    }

    @Override
    public void updateAlertHisByOrderId(String orderId,String orderStatus) {
        alertsHisV2Dao.updateAlertHisByOrderId(orderId,orderStatus);
    }
}
