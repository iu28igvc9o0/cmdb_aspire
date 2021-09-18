package com.aspire.ums.cmdb.v3.code.mapper;

import com.aspire.ums.cmdb.v3.code.payload.CmdbV3CodeCollect;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* 描述：
* @author
* @date 2020-01-09 14:33:19
*/
@Mapper
public interface CmdbV3CodeCollectMapper {
     /**
     * 获取所有实例
     * @return 返回所有实例数据
     */
    List<CmdbV3CodeCollect> list();

    /**
     * 获取所有实例
     * @param entity 实例信息
     * @return 返回所有实例数据
     */
    List<CmdbV3CodeCollect> listByEntity(CmdbV3CodeCollect entity);

    /**
     * 根据主键ID 获取数据信息
     * @param entity 实例信息
     * @return 返回实例信息的数据信息
     */
    CmdbV3CodeCollect get(CmdbV3CodeCollect entity);

    /**
     * 根据主键ID 获取数据信息
     * @param codeId 码表ID
     * @return 返回实例信息的数据信息
     */
    CmdbV3CodeCollect getByCodeId(@Param("codeId") String codeId);

    /**
     * 新增实例
     * @param entity 实例数据
     * @return
     */
    void insert(CmdbV3CodeCollect entity);

    /**
     * 新增实例
     * @param collects 实例数据
     * @return
     */
    void insertByBatch(List<CmdbV3CodeCollect> collects);
    /**
     * 修改实例
     * @param entity 实例数据
     * @return
     */
    void update(CmdbV3CodeCollect entity);

    /**
     * 删除实例
     * @param entity 实例数据
     * @return
     */
    void delete(CmdbV3CodeCollect entity);

    /**
     * 根据码表ID 删除实例
     * @param codeId
     */
    void deleteByCodeId(String codeId);
}