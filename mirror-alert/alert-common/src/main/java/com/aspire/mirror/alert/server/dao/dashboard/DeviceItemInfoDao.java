package com.aspire.mirror.alert.server.dao.dashboard;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.aspire.mirror.alert.server.dao.dashboard.po.DeviceItemInfo;


/**
 * 告警DAO
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.alert.server.dao
 * 类名称:    AlertsHisDao.java
 * 类描述:    告警DAO
 * 创建人:    JinSu
 * 创建时间:  2018/9/18 16:16
 * 版本:      v1.0
 */
@Mapper
public interface DeviceItemInfoDao {
    
    void batchInsert(List<DeviceItemInfo> list);
    
    List<String> getDeviceClass();
    
    List<String> getDeviceType(@Param(value = "deviceClass")String deviceClass);
    
    List<String> getSubclass(@Param(value = "deviceClass")String deviceClass,@Param(value = "deviceType")String deviceType);
    
    List<DeviceItemInfo> getMonitor(@Param(value = "deviceClass")String deviceClass,
    		@Param(value = "deviceType")String deviceType,@Param(value = "subclass")String subclass);
    
    List<DeviceItemInfo> getMonitorAll();
    
    List<DeviceItemInfo> getAll();
    
    List<DeviceItemInfo> getSunMonitors(@Param(value = "deviceClass")String deviceClass,@Param(value = "deviceType")String deviceType);

    List<DeviceItemInfo> getMonitorByKey(@Param(value = "key")String key);
    
    
    List<HashMap<String,String>> getMonitorList(@Param(value = "deviceClass")String deviceClass,@Param(value = "deviceType")String deviceType);

}
