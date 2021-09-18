package com.aspire.mirror.alert.server.dao.alert;

import com.aspire.mirror.alert.server.dao.alert.po.AlertReportOperateRecord;
import com.aspire.mirror.alert.server.dao.alert.po.AlertsRecord;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;


/**
 * 告警操作记录DAO
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.alert.server.dao
 * 类名称:    AlertsDao.java
 * 类描述:    告警操作记录DAO
 * 创建人:    JinSu
 * 创建时间:  2018/9/18 16:16
 * 版本:      v1.0
 */
@Mapper
public interface AlertsRecordDao {
	
    /**
     * 新增操作记录
     * @param alertsRecord 告警操作数据
     */
    void insert(AlertsRecord alertsRecord);
    
    
    
    /**
     * 查询分页数量
     * @param   
     */
    int getAlertRecordCount(Map<String, Object> hashmap);
    
   
    /**
     * 查询分页数据
     * @param   
     */
    List<AlertsRecord> getAlertRecordByPage(Map<String, Object> hashmap);
    
    
    
    /**
     * 查询操作记录
     * @param alertId snmp告警数据
     */

    List<AlertsRecord> selectRecordByPrimaryKey(String alertId);
    
    
    
    
    
    List<AlertReportOperateRecord> selectReportOperationRecordByAlertKey(String alertId);
    

}
