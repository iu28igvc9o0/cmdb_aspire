package com.aspire.mirror.alert.server.dao.monitorHttp;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.aspire.mirror.alert.server.dao.monitorHttp.po.MonitorHttpConfig;
import com.aspire.mirror.alert.server.dao.monitorHttp.po.MonitorHttpReq;


/**
 * 告警DAO
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.alert.server.dao
 * 类名称:    AlertsConfigDao.java
 * 类描述:    告警DAO
 * 创建人:    JinSu
 * 创建时间:  2018/9/18 16:16
 * 版本:      v1.0
 */
@Mapper
public interface MonitorHttpConfigDao {
    /**
     * 新增
     *
     * @param alerts 告警对象
     */
    void insert(MonitorHttpConfig Config);


    /**
     * 分页查询
     *
     * @param page 分页对象
     * @return List<AlertsConfig> 告警历史
     */
    List<MonitorHttpConfig> pageList(MonitorHttpReq pageRequset);

    /**
     * 查询count
     * @param page 分页对象
     * @return int 数据条数
     */
    int pageListCount(MonitorHttpReq pageRequset);

    /**
     * 根据主键查询
     *
     * @param alertId 主键
     * @return AlertsConfig 返回对象
     */
    MonitorHttpConfig selectByPrimaryKey(@Param(value = "id") String id);
    
     
    

    
    
    /**
     * 根据主键更新数据
     *
     * @param alerts 警告PO对象
     * @return int 数据条数
     */
    int update(MonitorHttpConfig Config);
    
    int updateStatus(MonitorHttpConfig config);
    
    
    int deleteByPrimaryKey(Integer id);
    
    MonitorHttpConfig getByName(@Param(value = "name") String name);
    
    
    
}
