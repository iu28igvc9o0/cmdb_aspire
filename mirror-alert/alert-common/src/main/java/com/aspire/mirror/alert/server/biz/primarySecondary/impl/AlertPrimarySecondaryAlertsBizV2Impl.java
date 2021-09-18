package com.aspire.mirror.alert.server.biz.primarySecondary.impl;

import com.aspire.mirror.alert.server.biz.primarySecondary.IAlertPrimarySecondaryAlertsBizV2;
import com.aspire.mirror.alert.server.dao.primarySecondary.AlertPrimarySecondaryAlertsV2Mapper;
import com.aspire.mirror.alert.server.util.Criteria;
import com.aspire.mirror.common.entity.PageResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
public class AlertPrimarySecondaryAlertsBizV2Impl implements IAlertPrimarySecondaryAlertsBizV2 {
    @Autowired
    private AlertPrimarySecondaryAlertsV2Mapper alertPrimarySecondaryAlertsV2Mapper;
    /**
     * 根据条件获取所有实例
     *
     * @param example
     * @return
     */
    public List<Map<String, Object>> list(Criteria example) {
        return alertPrimarySecondaryAlertsV2Mapper.listByEntity(example);
    }

    /**
     * 分页查询
     *
     * @param example
     * @return
     */
    public PageResponse<Map<String, Object>> findPage(Criteria example) {
        List<Map<String, Object>> pageWithResult = alertPrimarySecondaryAlertsV2Mapper.findPageWithResult(example);
        Integer pageWithCount = alertPrimarySecondaryAlertsV2Mapper.findPageWithCount(example);
        PageResponse<Map<String, Object>> page = new PageResponse<>();
        page.setCount(pageWithCount);
        page.setResult(pageWithResult);
        return page;
    }
}
