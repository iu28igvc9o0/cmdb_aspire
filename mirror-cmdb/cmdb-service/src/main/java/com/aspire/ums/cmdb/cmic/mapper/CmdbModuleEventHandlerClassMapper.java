package com.aspire.ums.cmdb.cmic.mapper;

import com.aspire.ums.cmdb.cmic.payload.CmdbModuleEventHandlerClass;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
* 描述：
* @author
* @date 2020-05-18 18:28:11
*/
@Mapper
public interface CmdbModuleEventHandlerClassMapper {
     /**
     * 获取所有实例
     * @return 返回所有实例数据
     */
    List<CmdbModuleEventHandlerClass> list();

    /**
     * 获取所有实例
     * @param entity 实例信息
     * @return 返回所有实例数据
     */
    List<CmdbModuleEventHandlerClass> listByEntity(CmdbModuleEventHandlerClass entity);

    /**
     * 根据主键ID 获取数据信息
     * @param entity 实例信息
     * @return 返回实例信息的数据信息
     */
    CmdbModuleEventHandlerClass get(CmdbModuleEventHandlerClass entity);

    /**
     * 新增实例
     * @param entity 实例数据
     * @return
     */
    void insert(CmdbModuleEventHandlerClass entity);

    /**
     * 修改实例
     * @param entity 实例数据
     * @return
     */
    void update(CmdbModuleEventHandlerClass entity);

    /**
     * 删除实例
     * @param entity 实例数据
     * @return
     */
    void delete(CmdbModuleEventHandlerClass entity);
}