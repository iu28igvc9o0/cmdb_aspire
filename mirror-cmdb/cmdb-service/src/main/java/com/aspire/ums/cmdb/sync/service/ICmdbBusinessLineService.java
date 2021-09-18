package com.aspire.ums.cmdb.sync.service;

import java.util.List;

import com.aspire.ums.cmdb.sync.entity.CmdbBusinessLine;

/**
 * 描述：
 * 
 * @author
 * @date 2020-05-20 09:16:01
 */
public interface ICmdbBusinessLineService {

    /**
     * 获取所有实例
     * 
     * @return 返回所有实例数据
     */
    List<CmdbBusinessLine> list();

    /**
     * 根据主键ID 获取数据信息
     * 
     * @param entity
     *            实例信息
     * @return 返回实例信息的数据信息
     */
    CmdbBusinessLine get(CmdbBusinessLine entity);

    /**
     * 新增实例
     * 
     * @param entity
     *            实例数据
     * @return
     */
    void insert(CmdbBusinessLine entity);

    /**
     * 修改实例
     * 
     * @param entity
     *            实例数据
     * @return
     */
    void update(CmdbBusinessLine entity);

    /**
     * 删除实例
     * 
     * @param entity
     *            实例数据
     * @return
     */
    void delete(CmdbBusinessLine entity);
    
    void deleteByBusinessCode(String businessCode);
}
