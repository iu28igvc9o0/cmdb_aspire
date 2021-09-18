package com.aspire.ums.cmdb.cmic.service;

import com.aspire.ums.cmdb.cmic.payload.CmdbModuleEvent;

import java.util.List;
import java.util.Map;

/**
* 描述：
* @author
* @date 2020-05-18 18:27:54
*/
public interface ICmdbModuleEventService {
     /**
     * 获取所有实例
     * @return 返回所有实例数据
     */
    List<CmdbModuleEvent> list();
    /**
     * 获取所有实例
     * @return 返回所有实例数据
     */
    List<CmdbModuleEvent> getModuleEventList(String moduleId, String eventClass);

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
     * @param eventList 实例数据
     * @return
     */
    void saveModuleEvents(String userName, String moduleId, List<CmdbModuleEvent> eventList);

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
     * 处理模型数据事件类
     * @param moduleId 模型ID
     * @param instanceId 实例ID
     * @param codeId 码表ID
     * @param handleData 事件处理所需要的参数
     * @param eventType 事件类型
     * @return
     */
    Object handlerModuleDataEvent(String moduleId, String instanceId, String codeId, Map<String, Object> handleData, String eventType);
    /**
     * 处理配置项数据事件类
     * @param moduleId 模型ID
     * @param codeId 码表ID
     * @param eventType 事件类型
     * @return
     */
    Map<String, Object> handlerCodeDataEvent(String moduleId, String codeId, Map<String, Object> selectItem, String eventType);
    /**
     * 处理模型数据事件类
     * @param moduleId 模型ID
     * @return
     */
    Map<String, List<Map<String, Object>>> getHaveEventCodeList(String moduleId);


}