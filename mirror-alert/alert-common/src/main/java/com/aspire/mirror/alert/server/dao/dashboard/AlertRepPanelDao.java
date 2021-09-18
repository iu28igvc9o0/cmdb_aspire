package com.aspire.mirror.alert.server.dao.dashboard;

import com.aspire.mirror.alert.server.dao.dashboard.po.AlertRepPanel;
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
public interface AlertRepPanelDao {
    /**
     * 新增
     *
     * @param alerts 告警对象
     */
    int insert(AlertRepPanel panel);




    /**
     * 根据主键查询
     *
     * @param alertId 主键
     * @return AlertsHis 返回对象
     */
    AlertRepPanel selectByPrimaryKey(@Param(value = "id") String id);
    
     
    

    
    
    /**
     * 根据主键更新数据
     *
     * @param alerts 警告PO对象
     * @return int 数据条数
     */
    int update(AlertRepPanel panel);
    
    
    int deleteByPrimaryKey(String id);
    
    List<AlertRepPanel> getByName(@Param(value = "panelName") String panelName);
    
    List<AlertRepPanel> selectAll();
    
    
}
