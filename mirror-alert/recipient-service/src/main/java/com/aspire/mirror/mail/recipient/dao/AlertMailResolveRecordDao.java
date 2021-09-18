package com.aspire.mirror.mail.recipient.dao;

import com.aspire.mirror.mail.recipient.po.AlertMailResolveRecord;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AlertMailResolveRecordDao {
    void insertResolveRecords(AlertMailResolveRecord resolveRecord);
}
