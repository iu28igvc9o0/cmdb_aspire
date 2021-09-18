package com.aspire.mirror.mail.recipient.service;

import com.aspire.mirror.mail.recipient.vo.AlertMailRecipientTaskRule;

public interface AlertMailRecipientService {
    void addTriggerTask(AlertMailRecipientTaskRule taskRule);
    void cancelTriggerTask(Integer ruleId);
    void resetTriggerTask(AlertMailRecipientTaskRule taskRule);
}
