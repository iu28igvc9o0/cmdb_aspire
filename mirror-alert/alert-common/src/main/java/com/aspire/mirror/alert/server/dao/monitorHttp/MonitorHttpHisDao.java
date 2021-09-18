package com.aspire.mirror.alert.server.dao.monitorHttp;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.aspire.mirror.alert.server.dao.monitorHttp.po.MonitorHttpHis;
import com.aspire.mirror.alert.server.dao.monitorHttp.po.MonitorHttpReq;


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
public interface MonitorHttpHisDao {
    /**
     * 新增
     *
     * @param alerts 告警对象
     */
    void insert(MonitorHttpHis his);
    
    void batchInsert(List<MonitorHttpHis> hisList);


    /**
     * 分页查询
     *
     * @param page 分页对象
     * @return List<AlertsHis> 告警历史
     */
    List<MonitorHttpHis> pageList(MonitorHttpReq pageRequset);

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
     * @return AlertsHis 返回对象
     */
    MonitorHttpHis selectByPrimaryKey(@Param(value = "id") int id);
    
    
    List<MonitorHttpHis> getByConfigId(@Param(value = "configId") int configId);
    
    //根据监控id查询最新的历史记录
    MonitorHttpHis getLatestHis(@Param(value = "id") int id);
}
