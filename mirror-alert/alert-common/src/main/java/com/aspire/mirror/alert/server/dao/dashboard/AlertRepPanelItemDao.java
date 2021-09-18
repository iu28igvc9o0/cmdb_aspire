package com.aspire.mirror.alert.server.dao.dashboard;

import com.aspire.mirror.alert.server.dao.dashboard.po.AlertRepPanelItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * 
 * @author lf
 *
 */
@Mapper
public interface AlertRepPanelItemDao {


    int batchInsert(List<AlertRepPanelItem> list);

    /**
     * 根据主键查询
     *
     * @param alertId 主键
     * @return AlertsHis 返回对象
     */
    AlertRepPanelItem selectByPrimaryKey(@Param(value = "id") String id);
    
     
    

    
    
    /**
     * 根据主键更新数据
     *
     * @param alerts 警告PO对象
     * @return int 数据条数
     */
    int update(AlertRepPanelItem panel);
    
    
    int deleteByPrimaryKey(String id);
    
    int deleteByPanelId(@Param(value = "panelId") String panelId);
    
    List<AlertRepPanelItem> getMonitersByPanelId(@Param(value = "id") String panelId);
}
