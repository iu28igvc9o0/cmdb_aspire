package com.aspire.mirror.alert.server.dao.mailAlert;


import com.aspire.mirror.alert.server.dao.mailAlert.po.AlertMailFilterStrategy;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AlertMailFilterStrategyDao {
    List<AlertMailFilterStrategy> selectStrategiesByFilterId(@Param("filterId") String filterId);
}
