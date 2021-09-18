package com.aspire.mirror.alert.server.dao.dashboard;

import com.aspire.mirror.alert.server.dao.dashboard.po.AlertRepPanelItem;
import com.aspire.mirror.alert.server.dao.dashboard.po.AlertRepPanelMoniterItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


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
public interface AlertRepPanelMoniterItemDao {


    int batchInsert(List<AlertRepPanelMoniterItem> list);

    /**
     * 根据主键查询
     *
     * @param alertId 主键
     * @return AlertsHis 返回对象
     */
    AlertRepPanelMoniterItem selectByPrimaryKey(@Param(value = "id") String id);
    
     
    

    
    
    /**
     * 根据主键更新数据
     *
     * @param alerts 警告PO对象
     * @return int 数据条数
     */
    int update(AlertRepPanelItem panel);
    
    
    int deleteByPrimaryKey(String id);
    
    int deleteByItemId(@Param(value = "itemId") String panelId);
    
    int deleteByPanelId(@Param(value = "panelId") String panelId);
    
    List<AlertRepPanelMoniterItem> getItemsByItemId(@Param(value = "id") String itemId);
}
