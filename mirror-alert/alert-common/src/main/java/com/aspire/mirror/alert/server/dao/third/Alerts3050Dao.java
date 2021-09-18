package com.aspire.mirror.alert.server.dao.third;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


/**
 * 告警DAO
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.alert.server.dao
 * 类名称:    AlertsDao.java
 * 类描述:    告警DAO
 * 创建人:    JinSu
 * 创建时间:  2018/9/18 16:16
 * 版本:      v1.0
 */
@Mapper
public interface Alerts3050Dao {

	 List<Map<String, Object>> getAlerts(@Param("roomId") String roomId,
			 @Param("endTime")String endTime,@Param("startTime")String startTime);
	 
	 List<Map<String, Object>> getAlertsHis(@Param("roomId") String roomId,
			 @Param("endTime")String endTime,@Param("startTime")String startTime);
	 
	 

	 
	 List<Map<String, String>> getRestfulAlerts(@Param("conditions") String conditions,
             @Param("intervalMinute") int intervalMinute,
             @Param("cols")String cols,@Param("endTime")String endTime,@Param("startTime")String startTime);
	 
	 List<Map<String, String>> getRestfulAlertsHis(@Param("conditions") String conditions,
             @Param("intervalMinute") int intervalMinute,
             @Param("cols")String cols,@Param("endTime")String endTime,@Param("startTime")String startTime);
}
