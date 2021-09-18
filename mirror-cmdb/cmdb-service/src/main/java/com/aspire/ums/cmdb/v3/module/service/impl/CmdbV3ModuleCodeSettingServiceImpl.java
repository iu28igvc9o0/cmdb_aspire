package com.aspire.ums.cmdb.v3.module.service.impl;

import com.aspire.ums.cmdb.util.UUIDUtil;
import com.aspire.ums.cmdb.v2.module.mapper.ModuleMapper;
import com.aspire.ums.cmdb.v3.module.mapper.CmdbV3ModuleCodeSettingMapper;
import com.aspire.ums.cmdb.v3.module.payload.CmdbV3ModuleCodeSetting;
import com.aspire.ums.cmdb.v3.module.service.ICmdbV3ModuleCodeSettingService;
import com.aspire.ums.cmdb.v3.redis.service.IRedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
* 描述：
* @author
* @date 2020-01-09 14:33:20
*/
@Service
public class CmdbV3ModuleCodeSettingServiceImpl implements ICmdbV3ModuleCodeSettingService {

    @Autowired
    private CmdbV3ModuleCodeSettingMapper mapper;

    @Autowired
    private ModuleMapper moduleMapper;

    @Autowired
    private IRedisService redisService;

    /**
     * 获取所有实例
     * @return 返回所有实例数据
     */
    public List<CmdbV3ModuleCodeSetting> list() {
        return mapper.list();
    }

    /**
     * 获取所有实例
     * @return 返回所有实例数据
     */
    public List<CmdbV3ModuleCodeSetting> listByEntity(CmdbV3ModuleCodeSetting codeSetting) {
        return mapper.listByEntity(codeSetting);
    }

    /**
     * 根据主键ID 获取数据信息
     * @param entity 实例信息
     * @return 返回指定ID的数据信息
     */
    public CmdbV3ModuleCodeSetting get(CmdbV3ModuleCodeSetting entity) {
        return mapper.get(entity);
    }

    /**
     * 新增实例
     * @param entity 实例数据
     * @return
     */
    public void insert(CmdbV3ModuleCodeSetting entity) {
        mapper.insert(entity);
    }

    /**
     * 批量新增实例
     * @param entities 实例数据
     * @return
     */
    @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
    public void insertByBatch(List<CmdbV3ModuleCodeSetting> entities) {
        String moduleId = entities.get(0).getModuleId();
        // 先删除原本的显示设置
        mapper.deleteByModuleId(moduleId);
        for (CmdbV3ModuleCodeSetting set : entities) {
            set.setId(UUIDUtil.getUUID());
        }
        mapper.insertByBatch(entities);
    }


    /**
     * 修改实例
     * @param entity 实例数据
     * @return
     */
    public void update(CmdbV3ModuleCodeSetting entity) {
        mapper.update(entity);
    }

    /**
     * 删除实例
     * @param entity 实例数据
     * @return
     */
    public void delete(CmdbV3ModuleCodeSetting entity) {
        mapper.delete(entity);
    }

    @Override
    public void deleteByModuleId(String moduleId) {
        mapper.deleteByModuleId(moduleId);
    }
}