package com.aspire.ums.cmdb.v3.code.mapper;

import com.aspire.ums.cmdb.code.payload.CmdbV3CodeCascade;
import com.aspire.ums.cmdb.code.payload.CmdbV3CodeTable;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* 描述：
* @author
* @date 2020-02-20 17:49:53
*/
@Mapper
public interface CmdbV3CodeTableMapper {
     /**
     * 获取所有实例
     * @return 返回所有实例数据
     */
    List<CmdbV3CodeTable> list();

    /**
     * 获取所有实例
     * @param entity 实例信息
     * @return 返回所有实例数据
     */
    List<CmdbV3CodeTable> listByEntity(CmdbV3CodeTable entity);

    /**
     * 获取所有实例
     * @param codeId 码表ID
     * @return 返回所有实例数据
     */
    List<CmdbV3CodeCascade> getByCodeId(@Param("codeId") String codeId);

    /**
     * 根据主键ID 获取数据信息
     * @param entity 实例信息
     * @return 返回实例信息的数据信息
     */
    CmdbV3CodeTable get(CmdbV3CodeTable entity);

    /**
     * 新增实例
     * @param entity 实例数据
     * @return
     */
    void insert(CmdbV3CodeTable entity);

    /**
     * 修改实例
     * @param entity 实例数据
     * @return
     */
    void update(CmdbV3CodeTable entity);

    /**
     * 删除实例
     * @param codeId 实例数据
     * @return
     */
    void deleteByCodeId(@Param("codeId") String codeId);
}