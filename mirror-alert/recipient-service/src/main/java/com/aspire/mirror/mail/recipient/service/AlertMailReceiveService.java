package com.aspire.mirror.mail.recipient.service;

import com.aspire.mirror.mail.recipient.po.AlertMailRecipient;
import com.aspire.mirror.mail.recipient.po.AlertMailSubstance;

import java.util.List;

public interface AlertMailReceiveService {

    List<AlertMailSubstance> pop(AlertMailRecipient recipient);

    List<AlertMailSubstance> imap(AlertMailRecipient recipient);

    boolean submitMailSubstance(List<AlertMailSubstance> list);
}
