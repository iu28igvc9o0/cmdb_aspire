package com.aspire.mirror.mail.recipient.service.impl;

import com.aspire.mirror.mail.recipient.dao.AlertMailRecipientDao;
import com.aspire.mirror.mail.recipient.po.AlertMailRecipient;
import com.aspire.mirror.mail.recipient.service.AlertMailReceiverService;
import com.aspire.mirror.mail.recipient.vo.AlertMailRecipientTaskRule;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AlertMailReceiverServiceImpl implements AlertMailReceiverService {

    @Autowired
    private AlertMailRecipientDao recipientDao;

    @Override
    public void insert(AlertMailRecipientTaskRule taskRule) {
        recipientDao.insertActiveRecipient(toAlertMailRecipient(taskRule));
    }

    @Override
    public void update(AlertMailRecipientTaskRule taskRule) {
        recipientDao.updateActiveRecipient(toAlertMailRecipient(taskRule));
    }

    @Override
    public void delete(Integer id) {
        recipientDao.deleteActiveRecipient(id);
    }

    private AlertMailRecipient toAlertMailRecipient(AlertMailRecipientTaskRule taskRule) {
        AlertMailRecipient recipient =  new AlertMailRecipient();
        if (taskRule.getId() != null) {
            recipient.setId(taskRule.getId());
        }
        if (taskRule.getActive() != null) {
            recipient.setActive(taskRule.getActive());
        }
        if (StringUtils.isNotEmpty(taskRule.getDescription())) {
            recipient.setDescription(taskRule.getDescription());
        }
        if (StringUtils.isNotEmpty(taskRule.getMailServer())) {
            recipient.setMailServer(taskRule.getMailServer());
        }
        if (StringUtils.isNotEmpty(taskRule.getPassword())) {
            recipient.setPassword(taskRule.getPassword());
        }
        if (taskRule.getReceivePort() != null) {
            recipient.setReceivePort(taskRule.getReceivePort());
        }
        if (taskRule.getReceiveProtocal() != null) {
            recipient.setReceiveProtocal(taskRule.getReceiveProtocal());
        }
        if (StringUtils.isNotEmpty(taskRule.getReceiver())) {
            recipient.setReceiver(taskRule.getReceiver());
        }
        if (taskRule.getPeriod() != null) {
            recipient.setPeriod(taskRule.getPeriod());
        }
        if (taskRule.getUnit() != null) {
            recipient.setUnit(taskRule.getUnit());
        }
        return recipient;
    }
}
