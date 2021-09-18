package com.aspire.mirror.mail.recipient.service;

import com.aspire.mirror.mail.recipient.vo.AlertMailRecipientTaskRule;

public interface AlertMailReceiverService {
    void insert(AlertMailRecipientTaskRule taskRule);
    void update(AlertMailRecipientTaskRule taskRule);
    void delete(Integer id);
}
