package com.aspire.ums.cmdb.v2.code.mapper;

import com.aspire.ums.cmdb.code.payload.CmdbCodeValidate;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* 描述：
* @author
* @date 2019-05-13 18:39:39
*/
@Mapper
public interface CmdbCodeValidateMapper {
     /**
     * 获取所有实例
     * @return 返回所有实例数据
     */
    List<CmdbCodeValidate> list();

    /**
     * 获取所有实例
     * @param entity 实例信息
     * @return 返回所有实例数据
     */
    List<CmdbCodeValidate> listByEntity(CmdbCodeValidate entity);

    /**
     * 根据主键ID 获取数据信息
     * @param entity 实例信息
     * @return 返回实例信息的数据信息
     */
    CmdbCodeValidate get(CmdbCodeValidate entity);

    /**
     * 新增实例
     * @param entity 实例数据
     * @return
     */
    void insert(CmdbCodeValidate entity);

    /**
     * 修改实例
     * @param entity 实例数据
     * @return
     */
    void update(CmdbCodeValidate entity);

    /**
     * 删除实例
     * @param entity 实例数据
     * @return
     */
    void delete(CmdbCodeValidate entity);

    /**
     * 根据码表ID 查询码表绑定的验证方法列表
     * @param codeId 码表ID
     * @return
     */
    List<CmdbCodeValidate> getValidateListByCodeId(@Param("codeId") String codeId);
}