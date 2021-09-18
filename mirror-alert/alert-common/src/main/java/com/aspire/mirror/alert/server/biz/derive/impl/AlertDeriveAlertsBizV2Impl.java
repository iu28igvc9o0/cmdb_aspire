package com.aspire.mirror.alert.server.biz.derive.impl;

import com.aspire.mirror.alert.server.biz.helper.AlertScheduleIndexHelper;
import com.aspire.mirror.alert.server.util.StringUtils;
import com.aspire.mirror.alert.server.biz.derive.IAlertDeriveAlertsBizV2;
import com.aspire.mirror.alert.server.biz.derive.IAlertDeriveAlertsHisBizV2;
import com.aspire.mirror.alert.server.dao.derive.AlertDeriveAlertsV2Mapper;
import com.aspire.mirror.alert.server.vo.model.AlertFieldVo;
import com.aspire.mirror.alert.server.util.AlertModelCommonUtil;
import com.aspire.mirror.alert.server.util.Criteria;
import com.aspire.mirror.common.entity.PageResponse;
import com.aspire.mirror.common.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @BelongsProject: mirror-alert
 * @BelongsPackage: com.aspire.mirror.alert.server.v2.biz.impl
 * @Author: baiwenping
 * @CreateTime: 2020-02-29 01:03
 * @Description: ${Description}
 */
@Slf4j
@Service
public class AlertDeriveAlertsBizV2Impl implements IAlertDeriveAlertsBizV2 {
    @Autowired
    private AlertDeriveAlertsV2Mapper alertDeriveAlertsV2Mapper;
    @Autowired
    private AlertScheduleIndexHelper alertScheduleIndexHelper;
    @Autowired
    private IAlertDeriveAlertsHisBizV2 alertDeriveAlertsHisBizV2;
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
     * 根据衍生告警id，清除衍生告警日志
     * @param alertId
     */
    @Async
    @Transactional
    public void deleteByAlertId (String alertId, List<AlertFieldVo> deriveHisFieldList) {
        if (StringUtils.isEmpty(alertId) || CollectionUtils.isEmpty(deriveHisFieldList)) {
            return;
        }
        log.info("clear derive alerts, derive alert id is {}", alertId);
        List<Map<String, Object>> list = alertDeriveAlertsV2Mapper.selectDerivesByAlertId(alertId);
        for (Map<String, Object> deriveAlertMap: list) {
            for (Map.Entry<String, Object> entry: deriveAlertMap.entrySet()) {
                Object value = entry.getValue();
                if (value == null) {
                    continue;
                }
                if (value instanceof Timestamp) {
                    deriveAlertMap.put(entry.getKey(), DateUtil.format(new Date(((Timestamp) value).getTime()), "yyyy-MM-dd HH:mm:ss"));
                }
            }
            alertDeriveAlertsHisBizV2.insert(AlertModelCommonUtil.generateAlerts(deriveAlertMap, deriveHisFieldList));
        }
        alertDeriveAlertsV2Mapper.deleteByAlertId(alertId);
    }

    /**
     * 查询衍生告警已消除的id
     * @return
     */
    public List<Map<String, Object>> selectAlertHisIds() {
        return alertDeriveAlertsV2Mapper.selectAlertHisIds();
    }
}
