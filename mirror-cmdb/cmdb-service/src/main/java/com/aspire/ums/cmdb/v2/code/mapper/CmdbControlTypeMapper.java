package com.aspire.ums.cmdb.v2.code.mapper;

import com.aspire.ums.cmdb.code.payload.CmdbControlType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
* 描述：
* @author
* @date 2019-05-13 18:39:39
*/
@Mapper
public interface CmdbControlTypeMapper {
     /**
     * 获取所有实例
     * @return 返回所有实例数据
     */
    List<CmdbControlType> list(Map<String, Object> queryParams);

    /**
     * 获取所有实例
     * @param entity 实例信息
     * @return 返回所有实例数据
     */
    List<CmdbControlType> listByEntity(CmdbControlType entity);

    /**
     * 根据主键ID 获取数据信息
     * @param entity 实例信息
     * @return 返回实例信息的数据信息
     */
    CmdbControlType get(CmdbControlType entity);

    /**
     * 根据主键ID 获取数据信息
     * @param controlId 控件ID
     * @return 返回实例信息的数据信息
     */
    CmdbControlType getById(@Param("controlId") String controlId);

    /**
     * 新增实例
     * @param entity 实例数据
     * @return
     */
    void insert(CmdbControlType entity);

    /**
     * 修改实例
     * @param entity 实例数据
     * @return
     */
    void update(CmdbControlType entity);

    /**
     * 删除实例
     * @param entity 实例数据
     * @return
     */
    void delete(CmdbControlType entity);
}