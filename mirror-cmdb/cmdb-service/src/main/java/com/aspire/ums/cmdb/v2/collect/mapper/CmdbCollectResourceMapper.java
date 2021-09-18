package com.aspire.ums.cmdb.v2.collect.mapper;

import com.aspire.ums.cmdb.collectApproval.payload.CmdbCollectResource;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
* 描述：
* @author
* @date 2019-06-18 20:55:57
*/
@Mapper
public interface CmdbCollectResourceMapper {
     /**
     * 获取所有实例
     * @return 返回所有实例数据
     */
    List<CmdbCollectResource> list();

    /**
     * 获取所有实例
     * @param entity 实例信息
     * @return 返回所有实例数据
     */
    List<CmdbCollectResource> listByEntity(CmdbCollectResource entity);

    /**
     * 根据主键ID 获取数据信息
     * @param entity 实例信息
     * @return 返回实例信息的数据信息
     */
    CmdbCollectResource get(CmdbCollectResource entity);

    /**
     * 新增实例
     * @param entity 实例数据
     * @return
     */
    void insert(CmdbCollectResource entity);

    /**
     * 修改实例
     * @param entity 实例数据
     * @return
     */
    void update(CmdbCollectResource entity);

    /**
     * 删除实例
     * @param entity 实例数据
     * @return
     */
    void delete(CmdbCollectResource entity);
}