package com.aspire.ums.cmdb.serverProject.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.aspire.ums.cmdb.serverProject.payload.CmdbNetworkLineMgr;

/**
 * 描述：
 * 
 * @author
 * @date 2021-01-27 17:26:15
 */
@Mapper
public interface CmdbNetworkLineMgrMapper {

    /**
     * 获取所有实例
     * 
     * @return 返回所有实例数据
     */
    List<CmdbNetworkLineMgr> list();

    /**
     * 获取所有实例
     * 
     * @param entity
     *            实例信息
     * @return 返回所有实例数据
     */
    List<CmdbNetworkLineMgr> listByEntity(CmdbNetworkLineMgr entity);

    /**
     * 根据主键ID 获取数据信息
     * 
     * @param entity
     *            实例信息
     * @return 返回实例信息的数据信息
     */
    CmdbNetworkLineMgr get(CmdbNetworkLineMgr entity);

    /**
     * 新增实例
     * 
     * @param entity
     *            实例数据
     * @return
     */
    void insert(CmdbNetworkLineMgr entity);

    /**
     * 修改实例
     * 
     * @param entity
     *            实例数据
     * @return
     */
    void update(CmdbNetworkLineMgr entity);

    /**
     * 删除实例
     * 
     * @param entity
     *            实例数据
     * @return
     */
    void delete(CmdbNetworkLineMgr entity);
}
