package com.aspire.ums.cmdb.v3.condication.service;

import com.aspire.ums.cmdb.v3.condication.payload.CmdbV3AccessUser;

import java.util.List;

/**
* 描述：
* @author
* @date 2020-03-11 15:11:42
*/
public interface ICmdbV3AccessUserService {
     /**
     * 获取所有实例
     * @return 返回所有实例数据
     */
    List<CmdbV3AccessUser> list();

    /**
     * 根据主键ID 获取数据信息
     * @param entity 实例信息
     * @return 返回实例信息的数据信息
     */
    CmdbV3AccessUser get(CmdbV3AccessUser entity);
    /**
     * 根据token 获取数据信息
     * @param token token信息
     * @return 返回实例信息的数据信息
     */
    CmdbV3AccessUser getUserByToken(String token);

    /**
     * 新增实例
     * @param entity 实例数据
     * @return
     */
    void insert(CmdbV3AccessUser entity);

    /**
     * 修改实例
     * @param entity 实例数据
     * @return
     */
    void update(CmdbV3AccessUser entity);

    /**
     * 删除实例
     * @param entity 实例数据
     * @return
     */
    void delete(CmdbV3AccessUser entity);
}