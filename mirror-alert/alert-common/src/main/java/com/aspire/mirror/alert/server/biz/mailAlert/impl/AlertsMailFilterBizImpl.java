package com.aspire.mirror.alert.server.biz.mailAlert.impl;

import com.aspire.mirror.alert.server.biz.mailAlert.AlertsMailFilterBiz;
import com.aspire.mirror.alert.server.dao.mailAlert.AlertMailFilterDao;
import com.aspire.mirror.alert.server.dao.mailAlert.po.AlertMailFilter;
import com.aspire.mirror.common.entity.Page;
import com.aspire.mirror.common.entity.PageRequest;
import com.aspire.mirror.common.entity.PageResponse;
import com.aspire.mirror.common.util.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AlertsMailFilterBizImpl implements AlertsMailFilterBiz {
    @Autowired
    private AlertMailFilterDao alertMailFilterDao;

    @Override
    public AlertMailFilter selectById(String id) {
        return alertMailFilterDao.selectMailFilterById(id);
    }

    @Override
    public void insertAlertsMailFilter(AlertMailFilter mailFilter) {
        alertMailFilterDao.insertMailFilter(mailFilter);
    }

    @Override
    public void updateAlertsMailFilter(AlertMailFilter mailFilter) {
        alertMailFilterDao.updateMailFilter(mailFilter);
    }

    @Override
    public void removeAlertsMailFilter(List<String> idList) {
        alertMailFilterDao.deleteMailFilter(idList);
    }

    @Override
    public PageResponse<AlertMailFilter> select(PageRequest pageRequest) {
        Page page = PageUtil.convert(pageRequest);
        int count = alertMailFilterDao.countMailFilterList(page);
        PageResponse<AlertMailFilter> pageResponse = new PageResponse<>();
        pageResponse.setCount(count);
        if (count > 0) {
            List<AlertMailFilter> filterList = alertMailFilterDao.selectMailFilterList(page);
            pageResponse.setResult(filterList);
        }
        return pageResponse;
    }
    
    @Override
    public AlertMailFilter selectMailFilterByRecipientId(String recipientId,String name,String id) {
        return alertMailFilterDao.selectMailFilterByRecipientId(recipientId, name,id);
    }
}
