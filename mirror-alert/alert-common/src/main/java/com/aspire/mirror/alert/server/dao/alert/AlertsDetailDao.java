package com.aspire.mirror.alert.server.dao.alert;

import com.aspire.mirror.alert.server.dao.alert.po.AlertsDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author baiwp
 * @title: AlertsDetailDao
 * @projectName mirror-alert
 * @description: TODO
 * @date 2019/6/1716:14
 */
@Mapper
public interface AlertsDetailDao {
    void insert(AlertsDetail alertsDetail);

    void deleteByAlertId(@Param("alertId") String alertId);

    List<AlertsDetail> selectByAlertId(Map<String, Object> map);

    int countByAlertId(Map<String, Object> map);
}
