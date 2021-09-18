package com.aspire.mirror.alert.server.dao.notify;

import com.aspire.mirror.alert.server.dao.notify.po.AlertsNotify;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface AlertsNotifyDao {
	
	
    void insert(AlertsNotify record);
    
    
    /**
     * 查询分页数量
     * @param   
     */
    List<Integer> getAlertNotifyCount(Map<String, Object> hashmap);
    
   
    /**
     * 查询分页数据
     * @param   
     */
    List<AlertsNotify> getAlertNotifyByPage(Map<String, Object> hashmap);
    
    
    
    /**
     * 查询成功数量
     * @param   
     */
    List<Integer> getSuccessAlertNotifyCount(Map<String, Object> hashmap);


    /**
     * 查询所有
     * @param
     */
    List<AlertsNotify> selectNotifyByPrimaryKey(String alertId);
    
    
     
    
    
    
}
