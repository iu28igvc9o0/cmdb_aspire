package com.aspire.mirror.alert.server.dao.alert;

import com.aspire.mirror.alert.server.dao.alert.po.Alerts;
import com.aspire.mirror.common.entity.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AlertIntelligentDao {

    List<Alerts> queryAlertIntelligentCount(Page page);

    List<Alerts> queryAlertIntelligentData(Page page);

    Integer getIntelligentCount(@Param("alertId") String alertId);

    List<Alerts> queryAlertCountByIntelligentAlertId(Page page);

    List<Alerts> queryAlertDataByIntelligentAlertId(Page page);

}
