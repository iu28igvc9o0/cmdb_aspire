package com.aspire.mirror.alert.server.biz.mailAlert;

import com.aspire.mirror.alert.server.dao.mailAlert.po.AlertMailFilter;
import com.aspire.mirror.common.entity.PageRequest;
import com.aspire.mirror.common.entity.PageResponse;

import java.util.List;

public interface AlertsMailFilterBiz {
    AlertMailFilter selectById(String id);
    void insertAlertsMailFilter(AlertMailFilter mailFilter);
    void updateAlertsMailFilter(AlertMailFilter mailFilter);
    void removeAlertsMailFilter(List<String> idList);
    PageResponse<AlertMailFilter> select(PageRequest pageRequest);
	AlertMailFilter selectMailFilterByRecipientId(String recipientId, String name,String id);
}
