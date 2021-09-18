package com.aspire.mirror.alert.server.dao.alert;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.aspire.mirror.alert.server.dao.alert.po.AlertStandardization;
import com.aspire.mirror.alert.server.dao.alert.po.AlertStandardizationLog;
import com.aspire.mirror.alert.server.dao.alert.po.AuthFieldConfig;
import com.aspire.mirror.alert.server.dao.alert.po.KpiBook;
import com.aspire.mirror.alert.server.dao.alert.po.KpiListData;

@Mapper
public interface AlertRestfulDao {
   
    void insertBookAlerts(AlertStandardization stand);
    
    List<AlertStandardization> getBookAlerts(@Param("source")String source);
    
    int insertKpiList(List<KpiListData> list);
    
    void insertKpiBook(KpiBook kpi);
    
    List<KpiBook> getKpiBook(@Param("source")String source);
    
    AlertStandardizationLog getLastStandardizationLog(@Param("type")int type, @Param("configId")int configId);
    //插入标准化日志
    int insertStandardizationLog(AlertStandardizationLog log);
    
    List<AuthFieldConfig> selectAuthField(@Param("type") Integer type);
    
    //资源池的设备性能分布
    List<Map<String,Object>> getIdcTypePerformanceData(@Param("idcType") String idcType,@Param("startTime") String startTime
    		,@Param("endTime") String endTime,@Param("item") String item,@Param("deviceType") String deviceType);
    
    
    int batchInsertIdcTypePerformance(List<Map<String,Object>> data);
    
    //资源池的设备性能分布:查所有资源池数据
    List<Map<String,Object>> getAllIdcTypePerformanceData(@Param("day") String day);
    
    int insertDeviceAlerts(List<Map<String,Object>> list);
}
