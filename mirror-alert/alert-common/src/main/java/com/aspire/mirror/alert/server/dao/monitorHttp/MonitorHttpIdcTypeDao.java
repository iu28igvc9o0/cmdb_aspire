package com.aspire.mirror.alert.server.dao.monitorHttp;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.aspire.mirror.alert.server.dao.monitorHttp.po.MonitorHttpIdcType;


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
public interface MonitorHttpIdcTypeDao {
 


    /**
     * 分页查询
     *
     * @param page 分页对象
     * @return List<AlertsHis> 告警历史
     */
    List<MonitorHttpIdcType> getAll();

 
}
