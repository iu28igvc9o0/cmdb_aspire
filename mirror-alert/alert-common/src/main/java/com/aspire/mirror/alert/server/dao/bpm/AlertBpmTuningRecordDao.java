package com.aspire.mirror.alert.server.dao.bpm;

import com.aspire.mirror.alert.server.dao.bpm.po.AlertTuningRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface AlertBpmTuningRecordDao {
    void insert(AlertTuningRecord record);
    AlertTuningRecord select(@Param(value = "alertId") String alertId);
}
