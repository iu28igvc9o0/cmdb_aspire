package com.aspire.mirror.alert.server.biz.mailAlert.impl;

import com.alibaba.fastjson.JSON;
import com.aspire.mirror.alert.server.biz.mailAlert.AlertsMailResolveBiz;
import com.aspire.mirror.alert.server.dao.mailAlert.AlertsMailResolveDao;
import com.aspire.mirror.alert.server.dao.mailAlert.po.AlertMailRecipient;
import com.aspire.mirror.alert.server.util.HttpUtils;
import com.aspire.mirror.common.entity.Page;
import com.aspire.mirror.common.entity.PageRequest;
import com.aspire.mirror.common.entity.PageResponse;
import com.aspire.mirror.common.util.PageUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AlertsMailResolveBizImpl implements AlertsMailResolveBiz {
    private static final Logger logger = LoggerFactory.getLogger(AlertsMailResolveBizImpl.class);
    @Value(value = "${recipient.client.url}")
    private String RECIPIENT_CLIENT_URL;
    @Autowired
    private AlertsMailResolveDao alertsMailResolveDao;

    @Override
    public void insertRecipient(AlertMailRecipient recipient) {
        alertsMailResolveDao.insertRecipient(recipient);
        logger.info("mail recipient id: ----> {}", recipient.getId());
        new Thread(() -> HttpUtils.httpPost(JSON.toJSONString(recipient), RECIPIENT_CLIENT_URL, null)).start();
    }

    @Override
    public List<AlertMailRecipient> selectRecipientByAccount(String account) {
        return alertsMailResolveDao.selectRecipientByAccount(account);
    }

    @Override
    public AlertMailRecipient selectRecipientById(Integer id) {
        return alertsMailResolveDao.selectRecipientById(id);
    }

    @Override
    public void updateRecipientById(AlertMailRecipient recipient) {
        alertsMailResolveDao.updateRecipient(recipient);
        new Thread(() -> HttpUtils.httpPut(JSON.toJSONString(recipient), RECIPIENT_CLIENT_URL, null)).start();
    }

    @Override
    public void removeRecipientById(Integer id) {
        alertsMailResolveDao.deleteRecipient(id);
        new Thread(() -> HttpUtils.httpDelete(RECIPIENT_CLIENT_URL + "/" + id, null)).start();
    }

    @Override
    public PageResponse<AlertMailRecipient> selectRecipients(PageRequest pageRequest) {
        Page page = PageUtil.convert(pageRequest);
        int count = alertsMailResolveDao.recipientsCount(page);
        PageResponse<AlertMailRecipient> pageResponse = new PageResponse<>();
        pageResponse.setCount(count);
        if (count > 0) {
            List<AlertMailRecipient> recipientList = alertsMailResolveDao.recipientsPageList(page);
            pageResponse.setResult(recipientList);
        }
        return pageResponse;
    }
}
