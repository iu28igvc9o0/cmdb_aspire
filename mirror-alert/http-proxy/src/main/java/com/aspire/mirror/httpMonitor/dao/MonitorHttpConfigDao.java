package com.aspire.mirror.httpMonitor.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.aspire.mirror.httpMonitor.dao.po.MonitorHttpConfig;
import com.aspire.mirror.httpMonitor.dao.po.MonitorHttpHis;
import com.aspire.mirror.httpMonitor.dao.po.MonitorHttpReq;


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
    
    List<MonitorHttpConfig> selectAll();
    
    
    void insertHis(MonitorHttpHis his);
    
    MonitorHttpHis getLatestHis(@Param(value = "id") String id);
}
