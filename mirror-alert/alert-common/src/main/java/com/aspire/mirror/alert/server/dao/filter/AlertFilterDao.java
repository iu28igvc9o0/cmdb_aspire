package com.aspire.mirror.alert.server.dao.filter;

import com.aspire.mirror.alert.server.dao.filter.po.AlertFilter;
import com.aspire.mirror.common.entity.Page;
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
public interface AlertFilterDao {
    /**
     * 新增
     *
     * @param alerts 告警对象
     */
    int insert(AlertFilter filter);


    /**
     * 分页查询
     *
     * @param page 分页对象
     * @return List<AlertsHis> 告警历史
     */
    List<AlertFilter> pageList(Page page);

    /**
     * 查询count
     * @param page 分页对象
     * @return int 数据条数
     */
    int pageListCount(Page page);

    /**
     * 根据主键查询
     *
     * @param alertId 主键
     * @return AlertsHis 返回对象
     */
    AlertFilter selectByPrimaryKey(@Param(value = "id") int id);
    
     
    

    
    
    /**
     * 根据主键更新数据
     *
     * @param alerts 警告PO对象
     * @return int 数据条数
     */
    int update(AlertFilter filter);
    
    
    int deleteByPrimaryKey(Integer id);
    
    AlertFilter getByName(@Param(value = "name") String name);
    
    /**
     * 查询所有告警过滤数据
     * @return
     */
    List<AlertFilter> selectAll();
    
    List<AlertFilter> selectFilterAll(@Param(value = "operateUser") String operateUser);
    
}
