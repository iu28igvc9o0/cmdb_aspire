package com.aspire.ums.cmdb.v3.condication.mapper;

import com.aspire.ums.cmdb.v3.condication.payload.CmdbV3AccessUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* 描述：
* @author
* @date 2020-03-11 15:11:42
*/
@Mapper
public interface CmdbV3AccessUserMapper {
     /**
     * 获取所有实例
     * @return 返回所有实例数据
     */
    List<CmdbV3AccessUser> list();

    /**
     * 获取所有实例
     * @param entity 实例信息
     * @return 返回所有实例数据
     */
    List<CmdbV3AccessUser> listByEntity(CmdbV3AccessUser entity);

    /**
     * 根据主键ID 获取数据信息
     * @param entity 实例信息
     * @return 返回实例信息的数据信息
     */
    CmdbV3AccessUser get(CmdbV3AccessUser entity);

    /**
     * 获取接入用户信息
     * @param accessUserId 接入用户ID
     * @return
     */
    CmdbV3AccessUser getById(@Param("id") String accessUserId);

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