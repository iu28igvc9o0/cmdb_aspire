package com.aspire.ums.cmdb.cmic.mapper;

import com.aspire.ums.cmdb.cmic.payload.CmdbModuleEvent;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* 描述：
* @author
* @date 2020-05-18 18:27:54
*/
@Mapper
public interface CmdbModuleEventMapper {
     /**
     * 获取所有实例
     * @return 返回所有实例数据
     */
    List<CmdbModuleEvent> list();

    /**
     * 获取所有实例
     * @param entity 实例信息
     * @return 返回所有实例数据
     */
    List<CmdbModuleEvent> listByEntity(CmdbModuleEvent entity);

    /**
     * 根据模型id获取配置项事件
     * @param moduleId 模型id
     * @return 返回所有实例数据
     */
    List<CmdbModuleEvent> listCodeEventByModuleId(String moduleId);
    /**
     * 根据主键ID 获取数据信息
     * @param entity 实例信息
     * @return 返回实例信息的数据信息
     */
    CmdbModuleEvent get(CmdbModuleEvent entity);

    /**
     * 新增实例
     * @param entity 实例数据
     * @return
     */
    void insert(CmdbModuleEvent entity);

    /**
     * 批量新增实例
     * @param entities 实例数据
     * @return
     */
    void insertByBatch(List<CmdbModuleEvent> entities);

    /**
     * 修改实例
     * @param entity 实例数据
     * @return
     */
    void update(CmdbModuleEvent entity);

    /**
     * 删除实例
     * @param entity 实例数据
     * @return
     */
    void delete(CmdbModuleEvent entity);

    /**
     * 根据模型和当前存在id判断并删除数据
     * @param currEventIdList 实例数据
     * @return
     */
    void deleteByNotExistId(@Param("moduleId") String moduleId, @Param("currEventIdList") List<String> currEventIdList);

}