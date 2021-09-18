package com.aspire.mirror.alert.server.biz.mailAlert;

import com.aspire.mirror.alert.server.dao.mailAlert.po.AlertMailFilterStrategy;
import com.aspire.mirror.alert.server.dao.mailAlert.po.AlertMailResolveRecord;
import com.aspire.mirror.common.entity.PageRequest;
import com.aspire.mirror.common.entity.PageResponse;

import java.util.List;

public interface AlertsMailStrategyBiz {
    List<AlertMailFilterStrategy> selectStrategiesByFilterId(String filterId);
    void batchInsertStrategies(List<AlertMailFilterStrategy> strategies);
    void removeStrategiesByFilterIds(List<String> filterids);
    void updateStrategy(AlertMailFilterStrategy strategy);
    PageResponse<AlertMailResolveRecord> selectResolveRecords(PageRequest pageRequest);
}
