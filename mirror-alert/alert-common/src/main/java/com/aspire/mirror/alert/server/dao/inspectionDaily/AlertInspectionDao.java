package com.aspire.mirror.alert.server.dao.inspectionDaily;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface AlertInspectionDao {

    List<Map<String,Object>> getAlertDataByDeviceType(@Param("dateTime") String dateTime);

    Map<String,Object> getAlertDataByDeviceClass(@Param("dateTime") String dateTime,
                                                 @Param("deviceClass") String deviceClass);

    void insertDeviceInspection(Map<String,Object> map);

    void deleteDeviceInspection(@Param("dateTime") String dateTime);

    List<String> getBizSystemData(@Param("indexType") String indexType);

    void insertBizSystemInspection(Map<String,Object> map);

    int getAlertDataByBizSystem(@Param("dateTime") String dateTime,
                                @Param("bizSystemList") String bizSystemList,
                                @Param("indexType") String indexType);

    void deleteBizSystemInspection(@Param("dateTime") String dateTime);
}
