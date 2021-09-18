package com.aspire.mirror.alert.server.biz.mailAlert.impl;

import com.aspire.mirror.alert.server.biz.mailAlert.AlertsMailStrategyBiz;
import com.aspire.mirror.alert.server.dao.mailAlert.AlertMailResolveRecordDao;
import com.aspire.mirror.alert.server.dao.mailAlert.AlertsMailStrategyDao;
import com.aspire.mirror.alert.server.dao.mailAlert.po.AlertMailFilterStrategy;
import com.aspire.mirror.alert.server.dao.mailAlert.po.AlertMailResolveRecord;
import com.aspire.mirror.common.entity.Page;
import com.aspire.mirror.common.entity.PageRequest;
import com.aspire.mirror.common.entity.PageResponse;
import com.aspire.mirror.common.util.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class AlertsMailStrategyBizImpl implements AlertsMailStrategyBiz {
    @Autowired
    private AlertsMailStrategyDao alertsMailStrategyDao;
    @Autowired
    private AlertMailResolveRecordDao alertMailResolveRecordDao;

    @Override
    public List<AlertMailFilterStrategy> selectStrategiesByFilterId(String filterId) {
        return alertsMailStrategyDao.selectStrategiesByFilterId(filterId);
    }

    @Override
    public void batchInsertStrategies(List<AlertMailFilterStrategy> strategies) {
        alertsMailStrategyDao.batchInsertStrategies(strategies);
    }

    @Override
    public void removeStrategiesByFilterIds(List<String> filterids) {
        alertsMailStrategyDao.removeStrategyByFilterIds(filterids);
    }

    @Override
    public void updateStrategy(AlertMailFilterStrategy strategy) {
        alertsMailStrategyDao.updateStrategy(strategy);
    }

    @Override
    public PageResponse<AlertMailResolveRecord> selectResolveRecords(PageRequest pageRequest) {
        Page page = PageUtil.convert(pageRequest);
        int count = alertMailResolveRecordDao.countResolveRecords(page);
        PageResponse<AlertMailResolveRecord> pageResponse = new PageResponse<>();
        pageResponse.setCount(count);
        if (count > 0) {
            List<AlertMailResolveRecord> list = alertMailResolveRecordDao.resolveRecordsList(page);
            pageResponse.setResult(list);
        }
        return pageResponse;
    }
}
