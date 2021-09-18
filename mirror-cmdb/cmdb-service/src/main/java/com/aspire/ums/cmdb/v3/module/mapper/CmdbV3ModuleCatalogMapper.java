package com.aspire.ums.cmdb.v3.module.mapper;

import com.aspire.ums.cmdb.v3.module.payload.CmdbV3ModuleCatalog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* 描述：
* @author
* @date 2020-01-09 14:33:20
*/
@Mapper
public interface CmdbV3ModuleCatalogMapper {
     /**
     * 获取所有实例
     * @return 返回所有实例数据
     */
    List<CmdbV3ModuleCatalog> list();

    /**
     * 获取所有实例
     * @param entity 实例信息
     * @return 返回所有实例数据
     */
    List<CmdbV3ModuleCatalog> listByEntity(CmdbV3ModuleCatalog entity);

    /**
     * 根据主键ID 获取数据信息
     * @param entity 实例信息
     * @return 返回实例信息的数据信息
     */
    CmdbV3ModuleCatalog get(CmdbV3ModuleCatalog entity);

    /**
     * 根据主键ID 获取数据信息
     * @param catalogId ID
     * @return 返回实例信息的数据信息
     */
    CmdbV3ModuleCatalog getById(@Param("id") String catalogId);

    /**
     * 新增实例
     * @param entity 实例数据
     * @return
     */
    void insert(CmdbV3ModuleCatalog entity);

    /**
     * 修改实例
     * @param entity 实例数据
     * @return
     */
    void update(CmdbV3ModuleCatalog entity);

    /**
     * 删除实例
     * @param entity 实例数据
     * @return
     */
    void delete(CmdbV3ModuleCatalog entity);

    /**
     * 查询最大index
     * @param
     * @return
     */
    Integer selectMaxIndex();
    /**
     * 获取升序最近的entity
     * @param catalogId
     * @return
     */
    CmdbV3ModuleCatalog getSortUpEntity(@Param("catalogId") String catalogId);

    /**
     * 获取降序最近的entity
     * @param catalogId
     * @return
     */
    CmdbV3ModuleCatalog getSortDownEntity(@Param("catalogId") String catalogId);

    /**
     * 根据模型id获取分组信息
     * @param moduleId
     * @return
     */
    CmdbV3ModuleCatalog getByModuleId(@Param("moduleId") String moduleId);
}