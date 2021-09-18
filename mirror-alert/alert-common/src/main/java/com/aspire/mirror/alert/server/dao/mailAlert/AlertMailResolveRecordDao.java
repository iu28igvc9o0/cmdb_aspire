package com.aspire.mirror.alert.server.dao.mailAlert;

import com.aspire.mirror.alert.server.dao.mailAlert.po.AlertMailResolveRecord;
import com.aspire.mirror.common.entity.Page;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AlertMailResolveRecordDao {
    int countResolveRecords(Page page);
    List<AlertMailResolveRecord> resolveRecordsList(Page page);
    void insertResolveRecords(AlertMailResolveRecord resolveRecord);
}
