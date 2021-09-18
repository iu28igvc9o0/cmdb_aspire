package com.aspire.mirror.alert.server.biz.derive.impl;

import com.aspire.mirror.alert.server.annotation.DataToKafka;
import com.aspire.mirror.alert.server.biz.helper.AlertScheduleIndexHelper;
import com.aspire.mirror.alert.server.util.StringUtils;
import com.aspire.mirror.alert.server.biz.derive.IAlertDeriveAlertsHisBizV2;
import com.aspire.mirror.alert.server.constant.AlertConfigConstants;
import com.aspire.mirror.alert.server.dao.derive.AlertDeriveAlertsHisV2Mapper;
import com.aspire.mirror.alert.server.util.Criteria;
import com.aspire.mirror.common.entity.PageResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @BelongsProject: mirror-alert
 * @BelongsPackage: com.aspire.mirror.alert.server.v2.biz.impl
 * @Author: baiwenping
 * @CreateTime: 2020-02-29 01:03
 * @Description: ${Description}
 */
@Slf4j
@Service
public class AlertDeriveAlertsHisBizV2Impl implements IAlertDeriveAlertsHisBizV2 {
    @Autowired
    private AlertDeriveAlertsHisV2Mapper alertDeriveAlertsV2Mapper;
    @Autowired
    private AlertScheduleIndexHelper alertScheduleIndexHelper;
    /**
     * 根据条件获取所有实例
     *
     * @param example
     * @return
     */
    public List<Map<String, Object>> list(Criteria example) {
        List<Map<String, Object>> list = alertDeriveAlertsV2Mapper.listByEntity(example);
        for (Map<String, Object> map:  list) {
            alertScheduleIndexHelper.pushDictAlert(map);
        }
        return list;
    }

    /**
     * 分页查询
     *
     * @param example
     * @return
     */
    public PageResponse<Map<String, Object>> findPage(Criteria example) {
        List<Map<String, Object>> pageWithResult = alertDeriveAlertsV2Mapper.findPageWithResult(example);
        Integer pageWithCount = alertDeriveAlertsV2Mapper.findPageWithCount(example);
        PageResponse<Map<String, Object>> page = new PageResponse<>();
        page.setCount(pageWithCount);
        page.setResult(pageWithResult);
        return page;
    }

    /**
     * 新增衍生告警历史数据
     *
     * @param map 告警修改对象
     * @return 影响数据条数
     */
    @DataToKafka(index = "alert_derive_alerts_his")
    public String insert (Map<String, Object> map) {
        String alertId = MapUtils.getString(map, AlertConfigConstants.ALERT_ID);
        if (StringUtils.isEmpty(alertId)) {
            alertId = UUID.randomUUID().toString();
            map.put(AlertConfigConstants.ALERT_ID, alertId);
        }
        alertDeriveAlertsV2Mapper.insert(map);
        return alertId;
    }
}
