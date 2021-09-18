package com.aspire.mirror.mail.recipient.dao;

import com.aspire.mirror.mail.recipient.po.AlertMailFailed;
import com.aspire.mirror.mail.recipient.po.AlertMailRecipient;
import com.aspire.mirror.mail.recipient.po.AlertMailSubstance;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AlertMailRecipientDao {
    List<AlertMailRecipient> selectActiveRecipients();
    void insertActiveRecipient(AlertMailRecipient recipient);
    void updateActiveRecipient(AlertMailRecipient recipient);
    void deleteActiveRecipient(@Param(value = "id") Integer id);
    int countSubstance(@Param("receiver") String receiver, @Param("uid") String uid);
    void saveSubstance(@Param("receiver") String receiver, @Param("uid") String uid);
    void batchSaveSubstance(@Param("list") List<AlertMailSubstance> substanceList);
    void insertFailedRecord(AlertMailFailed alertMailFailed);
}
