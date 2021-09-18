package com.aspire.mirror.mail.recipient.dao;

import com.aspire.mirror.mail.recipient.po.AlertMailFilterStrategy;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AlertMailFilterStrategyDao {
    List<AlertMailFilterStrategy> selectStrategiesByFilterId(@Param("filterId") String filterId);
}
