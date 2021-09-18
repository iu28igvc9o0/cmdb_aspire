package com.aspire.ums.cmdb.v3.module.service;
import java.util.List;
import com.aspire.ums.cmdb.v3.module.payload.CmdbV3ModuleCodeSetting;

/**
* 描述：
* @author
* @date 2020-01-09 14:33:20
*/
public interface ICmdbV3ModuleCodeSettingService {
     /**
     * 获取所有实例
     * @return 返回所有实例数据
     */
    List<CmdbV3ModuleCodeSetting> list();

    /**
     * 获取所有实例
     * @return 返回所有实例数据
     */
    List<CmdbV3ModuleCodeSetting> listByEntity(CmdbV3ModuleCodeSetting codeSetting);

    /**
     * 根据主键ID 获取数据信息
     * @param entity 实例信息
     * @return 返回实例信息的数据信息
     */
    CmdbV3ModuleCodeSetting get(CmdbV3ModuleCodeSetting entity);

    /**
     * 新增实例
     * @param entity 实例数据
     * @return
     */
    void insert(CmdbV3ModuleCodeSetting entity);

    /**
     * 批量新增实例
     * @param entities 实例数据
     * @return
     */
    void insertByBatch(List<CmdbV3ModuleCodeSetting> entities);

    /**
     * 修改实例
     * @param entity 实例数据
     * @return
     */
    void update(CmdbV3ModuleCodeSetting entity);

    /**
     * 删除实例
     * @param entity 实例数据
     * @return
     */
    void delete(CmdbV3ModuleCodeSetting entity);

    /**
     * 根据模型id删除实例
     * @param moduleId 实例数据
     * @return
     */
    void deleteByModuleId(String moduleId);
}