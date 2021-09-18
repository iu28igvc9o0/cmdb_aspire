package com.aspire.mirror.alert.server.dao.monthReport;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AlertsPoorEfficiencyDeviceDao {

	/*
	 * List<Map<String,Object>> getDeviceData(Map<String,Object> param);
	 * 
	 *
	 */
	 int getDeviceDataCount(Map<String,Object> param);
	 
	 List<Map<String,Object>> getDeviceDataPageList(Map<String,Object> param);
   
	Map<String,Object> getDeviceDataLatestDate(Map<String,Object> param);

   List<Map<String,Object>> getDeviceDataByDate(Map<String,Object> param);

    void insertPoorEfficiencyDeviceData(List<Map<String,Object>> dataList);
    
    void insertDeviceLogData(Map<String,Object> log);


}
