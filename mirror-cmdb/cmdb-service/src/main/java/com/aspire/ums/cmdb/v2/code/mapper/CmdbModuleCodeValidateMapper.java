package com.aspire.ums.cmdb.v2.code.mapper;

import com.aspire.ums.cmdb.code.payload.CmdbModuleCodeValidate;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
* 描述：
* @author
* @date 2019-05-17 09:51:14
*/
@Mapper
public interface CmdbModuleCodeValidateMapper {
     /**
     * 获取所有实例
     * @return 返回所有实例数据
     */
    List<CmdbModuleCodeValidate> list();

    /**
     * 获取所有实例
     * @param entity 实例信息
     * @return 返回所有实例数据
     */
    List<CmdbModuleCodeValidate> listByEntity(CmdbModuleCodeValidate entity);

    /**
     * 根据主键ID 获取数据信息
     * @param entity 实例信息
     * @return 返回实例信息的数据信息
     */
   CmdbModuleCodeValidate get(CmdbModuleCodeValidate entity);

    /**
     * 新增实例
     * @param entity 实例数据
     * @return
     */
    void insert(CmdbModuleCodeValidate entity);

    /**
     * 批量新增实例
     * @param entities 实例数据
     * @return
     */
    void insertByBatch(List<CmdbModuleCodeValidate> entities);

    /**
     * 修改实例
     * @param entity 实例数据
     * @return
     */
    void update(CmdbModuleCodeValidate entity);

    /**
     * 删除实例
     * @param entity 实例数据
     * @return
     */
    void delete(CmdbModuleCodeValidate entity);

    /**
     * 删除实例
     * @param entity 实例数据
     * @return
     */
    void deleteBySelective(CmdbModuleCodeValidate entity);

}