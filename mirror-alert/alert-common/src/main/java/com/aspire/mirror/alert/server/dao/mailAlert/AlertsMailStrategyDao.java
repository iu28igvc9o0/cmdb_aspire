package com.aspire.mirror.alert.server.dao.mailAlert;

import com.aspire.mirror.alert.server.dao.mailAlert.po.AlertMailFilterStrategy;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AlertsMailStrategyDao {
    void batchInsertStrategies(@Param("list") List<AlertMailFilterStrategy> strategies);
    List<AlertMailFilterStrategy> selectStrategiesByFilterId(@Param("filterId") String filterId);
    void updateStrategy(AlertMailFilterStrategy strategy);
    void removeStrategyByFilterIds(@Param("ids") List<String> ids);
}
