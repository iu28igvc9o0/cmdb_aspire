package com.aspire.ums.cmdb.v3.redis.service.impl;

import com.aspire.mirror.redis.api.payload.KeyValueRedisEntity;
import com.aspire.ums.cmdb.code.payload.CmdbCode;
import com.aspire.ums.cmdb.code.payload.CmdbCodeGroup;
import com.aspire.ums.cmdb.common.Constants;
import com.aspire.ums.cmdb.dict.service.ConfigDictService;
import com.aspire.ums.cmdb.module.payload.Module;
import com.aspire.ums.cmdb.module.payload.SimpleModule;
import com.aspire.ums.cmdb.util.StringUtils;
import com.aspire.ums.cmdb.v2.code.service.ICmdbCodeService;
import com.aspire.ums.cmdb.v2.module.service.ModuleService;
import com.aspire.ums.cmdb.v3.code.payload.CmdbV3CodeBindSource;
import com.aspire.ums.cmdb.v3.condication.payload.CmdbV3CondicationSetting;
import com.aspire.ums.cmdb.v3.condication.payload.CmdbV3CondicationSettingRelation;
import com.aspire.ums.cmdb.v3.condication.service.ICmdbV3CondicationSettingService;
import com.aspire.ums.cmdb.v3.module.payload.CmdbV3ModuleCatalog;
import com.aspire.ums.cmdb.v3.module.payload.CmdbV3ModuleCodeSetting;
import com.aspire.ums.cmdb.v3.module.service.ICmdbV3ModuleCatalogService;
import com.aspire.ums.cmdb.v3.module.service.ICmdbV3ModuleCodeSettingService;
import com.aspire.ums.cmdb.v3.redis.client.IRedisClient;
import com.aspire.ums.cmdb.v3.redis.service.IRedisService;
import com.aspire.ums.cmdb.view.payload.CmdbView;
import com.aspire.ums.cmdb.view.payload.CmdbViewData;
import com.aspire.ums.cmdb.view.service.ICmdbViewService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Copyright (C), 2015-2020, 卓望数码有限公司
 * FileName: RedisServiceImpl
 * Author:   zhu.juwang
 * Date:     2020/1/20 17:25
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Service
@Slf4j
public class RedisServiceImpl implements IRedisService {

    @Autowired
    private IRedisClient redisClient;
    @Autowired
    private ICmdbV3CondicationSettingService condicationSettingService;
    @Autowired
    private ICmdbCodeService codeService;
    @Autowired
    private ModuleService moduleService;
    @Autowired
    private ICmdbV3ModuleCodeSettingService codeSettingService;
    @Autowired
    private ICmdbV3ModuleCatalogService catalogService;
    @Autowired
    private ICmdbViewService viewService;
    @Autowired
    private ConfigDictService dictService;

    @Override
    public boolean set(String key, Object value) {
        return redisClient.set(new KeyValueRedisEntity(key, value, null, null));
    }

    @Override
    public boolean set(String key, Object value, Long expireTime, TimeUnit timeUnit) {
        return redisClient.set(new KeyValueRedisEntity(key, value, expireTime, timeUnit));
    }

    @Override
    public Object get(String key) {
        return redisClient.get(key);
    }

    @Override
    public boolean syncRemove(String key) {
        return redisClient.remove(key);
    }

    /**
     * 按照REDIS KEY规则删除缓存
     * @param key
     * @return
     */
    @Override
    public boolean syncRemovePattern(String key) {
        return redisClient.removePattern(key);
    }

    @Override
    public void asyncRemove(String key) {
        new Thread(() -> {
            this.syncRemove(key);
        }).start();
    }

    /**
     * 刷新REDIS缓存
     * @param redisType REDIS类型
     * @param object 更新信息
     * @return
     */
    public boolean syncRefresh(final String redisType, final Object object) {
        log.info("Ready to re-write redis -> {} {}", redisType, object);
        boolean flag;
        switch (redisType) {
            case Constants.REDIS_TYPE_MODULE:
                flag = refreshModule(object.toString());
                break;
            case Constants.REDIS_TYPE_CODE:
                flag = refreshCode(object.toString());
                break;
            case Constants.REDIS_TYPE_CODE_GROUP:
                flag = refreshCodeGroup(object == null ? null : object.toString());
                break;
            case Constants.REDIS_TYPE_DICT:
                flag = refreshDict(object.toString());
                break;
            case Constants.REDIS_TYPE_CATALOG:
                flag = refreshModuleCatalog(object.toString());
                break;
            case Constants.REDIS_TYPE_CONDITION_SETTING:
                flag = refreshConditionSetting((CmdbV3CondicationSetting) object);
                break;
            case Constants.REDIS_TYPE_MODULE_TAB:
                flag = refreshTabRelModuleSqlSetting((CmdbV3CondicationSetting) object);
                break;
            case Constants.REDIS_TYPE_VIEW:
                flag = refreshViewTree(object.toString());
                break;
            default:
                log.error("Don't support redis type.");
                flag = false;
                break;
        }
        log.info("Re-write redis -> {} {} end.", redisType, object);
        return flag;
    }


    /**
     * 刷新REDIS缓存
     * @param redisType REDIS类型
     * @param object 更新信息
     * @return
     */
    public void asyncRefresh(final String redisType, final Object object) {
        new Thread(() -> {
            syncRefresh(redisType, object);
        }).start();
    }

    /**
     * 刷新 数据字典
     * @param dictId
     * @return
     */
    private boolean refreshDict(String dictId) {
        return true;
    }

    private boolean refreshViewTree(String viewCode) {
        try {
            // 先清除所有的模型分组缓存
            this.syncRemovePattern(String.format(Constants.REDIS_VIEW_TREE, viewCode));
            CmdbViewData view = new CmdbViewData();
            view.setViewCode(viewCode);
            CmdbViewData viewData = viewService.getNextNodeData(view);
            if (viewData != null) {
                set(String.format(Constants.REDIS_VIEW_TREE, viewCode), viewData);
            }
            return true;
        } catch (Exception e) {
            log.error("缓存自定义树[{}]失败.", viewCode);
            return false;
        }

    }

    /**
     * 刷新模型分组
     * @param catalogId
     * @return
     */
    private boolean refreshModuleCatalog(String catalogId) {
        try {
            // 先清除所有的模型分组缓存
            this.syncRemovePattern(String.format(Constants.REDIS_MODULE_CATALOG_PREFIX, catalogId));
            CmdbV3ModuleCatalog catalog = catalogService.getById(catalogId);
            if (catalog != null) {
                set(String.format(Constants.REDIS_MODULE_CATALOG_TREE, catalogId), moduleService.getModuleTree(catalogId, null));
            }
            return true;
        } catch (Exception e) {
            log.error("缓存模型分组[{}]失败.", catalogId);
            return false;
        }
    }
    /**
     * 刷新码表分组信息
     * @param catalogId
     * @return
     */
    private boolean refreshCodeGroup(String catalogId) {
        redisClient.removePattern(String.format(Constants.REDIS_CODE_GROUP_PREFIX, catalogId));
        List<CmdbCodeGroup> groups = codeService.queryCodeListFormatGroup(catalogId);
        this.set(String.format(Constants.REDIS_CODE_GROUP_PREFIX, catalogId), groups);
        return true;
    }

    /**
     * 刷新码表引用类型的数据字典值
     * @param codeId 码表ID
     * @return
     */
    private boolean refreshRefCodeData(String codeId) {
        redisClient.removePattern(String.format(Constants.REDIS_CODE_DICT_DATA_VALUE, codeId));
        List<Map<String, Object>> refValue = codeService.getRefCodeData(codeId);
        this.set(String.format(Constants.REDIS_CODE_DICT_DATA_VALUE, codeId), refValue);
        return true;
    }

    /**
     * 刷新码表信息
     * @param codeId
     * @return
     */
    private boolean refreshCode(String codeId) {
        // 编码设计到的对象有 码表信息、条件配置、模型配置
        // 刷新码表信息
        redisClient.removePattern(String.format(Constants.REDIS_CODE_PREFIX, codeId));
        CmdbCode queryCode = new CmdbCode();
        queryCode.setCodeId(codeId);
        CmdbCode cmdbCode = codeService.get(queryCode);
        if (cmdbCode == null) {
            log.error("Can't find cmdb_code record, code_id = {}, refresh redis failed.", codeId);
            return false;
        }
        this.set(String.format(Constants.REDIS_CODE_DETAIL, codeId), cmdbCode);
        // 根据码表ID获取条件配置列表
        List<CmdbV3CondicationSetting> conditionList = condicationSettingService.getConditionListByCodeId(codeId);
        if (conditionList != null && conditionList.size() > 0) {
            for (CmdbV3CondicationSetting setting : conditionList) {
                refreshConditionSetting(setting);
            }
        }
        // 刷新码表分组
        this.refreshCodeGroup(null);
        this.refreshCodeGroup(cmdbCode.getModuleCatalogId());
        // 刷新模型配置
        CmdbV3ModuleCodeSetting querySetting = new CmdbV3ModuleCodeSetting();
        querySetting.setCodeId(codeId);
        List<CmdbV3ModuleCodeSetting> codeSettingList = codeSettingService.listByEntity(querySetting);
        if (codeSettingList != null && codeSettingList.size() > 0) {
            for (CmdbV3ModuleCodeSetting codeSetting : codeSettingList) {
                refreshModule(codeSetting.getModuleId());
            }
        }
        // 刷新引用模型数据字典值
        this.refreshRefCodeData(codeId);
        return false;
    }

    /**
     * 刷新模型信息
     * @param moduleId 模型ID
     * @return
     */
    private boolean refreshModule(String moduleId) {
        try {
            // 清理原先缓存
            redisClient.removePattern(String.format(Constants.REDIS_MODULE_MODULE_PREFIX, moduleId));
            // 重新缓存
            Object obj = redisClient.get(String.format(Constants.REDIS_MODULE_TABLE, moduleId));
            if ( obj != null) {
             return  true;
            }
            Module module = moduleService.getModuleDetail(moduleId);
            if (module != null) {
                set(String.format(Constants.REDIS_MODULE_DETAIL, moduleId), module);
                this.refreshModuleQuerySql(moduleId);
                this.refreshModuleColumns(moduleId);
                List<SimpleModule> modules = moduleService.getModuleByRefModuleId(moduleId);
                for (SimpleModule m : modules)  {
                    this.refreshModuleQuerySql(m.getId());
                    this.refreshModuleColumns(moduleId);
                }
                CmdbV3ModuleCatalog topCatalog = catalogService.getTopCatalog(module.getCatalogId());
                // 刷新模型分组
                if (topCatalog != null) {
                    refreshModuleCatalog(topCatalog.getId());
                }
            }
            return true;
        } catch (Exception e) {
            log.error("刷新模型[{}]信息失败.", moduleId, e);
            return false;
        }
    }

    /**
     * 刷新模型sql信息
     * @param moduleId 模型ID
     * @return
     */
    private boolean refreshModuleQuerySql(String moduleId) {
        try {
            // 清理原先缓存
            redisClient.removePattern(String.format(Constants.REDIS_MODULE_TABLE, moduleId));
            redisClient.removePattern(String.format(Constants.REDIS_TEMP_MODULE_TABLE, moduleId));
            redisClient.removePattern(String.format(Constants.REDIS_MODULE_COUNT_TABLE, moduleId));
            // 重新缓存
            Module module = moduleService.getModuleDetail(moduleId);
            if (module != null) {
                String sql = moduleService.getModuleQuerySQL(module.getId());
                if (StringUtils.isNotEmpty(sql)) {
                    Map<String, String> sqlMap = new HashMap<>();
                    sqlMap.put("moduleId", module.getId());
                    sqlMap.put("sql", sql);
                    set(String.format(Constants.REDIS_MODULE_TABLE, moduleId), sqlMap);
                } else {
                    log.error("模型[{}]未配置模型字段信息, 跳过缓存.", module.getCode());
                }
                String tempSql = moduleService.getTempModuleQuerySQL(module.getId());
                if (StringUtils.isNotEmpty(tempSql)) {
                    Map<String, String> sqlMap = new HashMap<>();
                    sqlMap.put("moduleId", module.getId());
                    sqlMap.put("sql", tempSql);
                    set(String.format(Constants.REDIS_TEMP_MODULE_TABLE, moduleId), sqlMap);
                } else {
                    log.error("模型[{}]未配置模型字段信息, 跳过缓存.", module.getCode());
                }
                String countSql = moduleService.getModuleQueryCountSQL(module.getId());
                if (StringUtils.isNotEmpty(countSql)) {
                    Map<String, String> sqlMap = new HashMap<>();
                    sqlMap.put("moduleId", module.getId());
                    sqlMap.put("sql", countSql);
                    set(String.format(Constants.REDIS_MODULE_COUNT_TABLE, moduleId), sqlMap);
                } else {
                    log.error("模型[{}]未配置模型字段信息, 跳过缓存.", module.getCode());
                }
            }
            return true;
        } catch (Exception e) {
            log.error("刷新模型sql[{}]信息失败.", moduleId, e);
            return false;
        }
    }

    /**
     * 刷新模型Columns信息
     * @param moduleId 模型ID
     * @return
     */
    private boolean refreshModuleColumns(String moduleId) {
        try {
            // 清理原先缓存
            redisClient.removePattern(String.format(Constants.REDIS_MODULE_COLUMNS, moduleId));
            // 重新缓存
            set(String.format(Constants.REDIS_MODULE_COLUMNS, moduleId), moduleService.getModuleColumns(moduleId));
            return true;
        } catch (Exception e) {
            log.error("刷新模型列[{}]信息失败.", moduleId, e);
            return false;
        }
    }

    /**
     * 刷新条件配置信息
     * @param setting 条件配置
     * @return
     */
    private boolean refreshConditionSetting(CmdbV3CondicationSetting setting) {
        try {
            String key = setting.getCondicationCode() + "_" + setting.getAccessUserId();
            // 清理原先缓存
            redisClient.removePattern(String.format(Constants.REDIS_CONDITION_SETTING_PREFIX, key));
            // 解析SETTING
            Map<String, Object> returnMap = condicationSettingService.getSettingByCodeAndAccessUserId(setting.getCondicationCode(),
                    setting.getAccessUserId());
            set(String.format(Constants.REDIS_CONDITION_SETTING_DETAIL, key), returnMap);
            return true;
        } catch (Exception e) {
            log.error("刷新配置条件[{}]信息失败.", setting.getId(), e);
            return false;
        }
    }

    /**
     * 刷新模型和Tab查询条件关联的列表sql
     * @param setting 条件配置
     */
    private boolean refreshTabRelModuleSqlSetting(CmdbV3CondicationSetting setting) {
        try {
            // 清理原先缓存
            String moduleId = setting.getModuleId();
            redisClient.removePattern(String.format(Constants.REDIS_MODULE_TAB_TABLE, moduleId));
            redisClient.removePattern(String.format(Constants.REDIS_MODULE_COUNT_TAB_TABLE, moduleId));
            redisClient.removePattern(String.format(Constants.REDIS_MODULE_TAB_EXPORT_TABLE, moduleId));
            Module module = moduleService.getModuleDetail(moduleId);
            if (null != module) {
                // 获取配置的tab管理查询条件，并筛选出没有绑定其他源（如数据字典这些）的字段
                List<CmdbV3CondicationSettingRelation> relationList = setting.getSettingRelationList();
                if (relationList != null && !relationList.isEmpty()) {
                    List<String> tabParamList = new ArrayList<>(relationList.size());
                    relationList.forEach((relation) -> {
                        CmdbCode cmdbCode = relation.getCmdbCode();
                        if(null == cmdbCode) {
                            CmdbCode param = new CmdbCode();
                            param.setCodeId(relation.getCodeId());
                            cmdbCode = codeService.get(param);
                        }
                        if (null != cmdbCode) {
                            String isBindSource = cmdbCode.getIsBindSource();
                            CmdbV3CodeBindSource codeBindSource = cmdbCode.getCodeBindSource();
                            if ("否".equals(isBindSource) && null == codeBindSource && !"id".equals(cmdbCode.getFiledCode())) {
                                tabParamList.add(cmdbCode.getFiledCode());
                            }
                        }
                    });
                    // 根据获得的字段筛选出需要join的模型sql
                    String sql4Tab = moduleService.getModuleQuerySQL4Tab(moduleId, tabParamList,Constants.MODULE_TAB_LIST);
                    // 将该sql重新录入redis中
                    if (StringUtils.isNotEmpty(sql4Tab)) {
                        Map<String, String> sqlMap = new HashMap<>();
                        sqlMap.put("moduleId", module.getId());
                        sqlMap.put("sql", sql4Tab);
                        set(String.format(Constants.REDIS_MODULE_TAB_TABLE, moduleId), sqlMap);
                    } else {
                        log.error("模型[{}]未配置模型和Tab字段的关联信息, 跳过缓存.", module.getCode());
                    }
                    String countSql4Tab = moduleService.getModuleQueryCountSQL4Tab(moduleId,tabParamList);
                    if (StringUtils.isNotEmpty(countSql4Tab)) {
                        Map<String, String> sqlMap = new HashMap<>();
                        sqlMap.put("moduleId", module.getId());
                        sqlMap.put("sql", countSql4Tab);
                        set(String.format(Constants.REDIS_MODULE_COUNT_TAB_TABLE, moduleId), sqlMap);
                    } else {
                        log.error("模型[{}]未配置模型和Tab字段的关联信息, 跳过缓存.", module.getCode());
                    }
                    String sql4TabExport = moduleService.getModuleQuerySQL4Tab(moduleId, tabParamList,Constants.MODULE_TAB_EXPORT);
                    if (StringUtils.isNotEmpty(sql4TabExport)) {
                        Map<String, String> sqlMap = new HashMap<>();
                        sqlMap.put("moduleId", module.getId());
                        sqlMap.put("sql", sql4TabExport);
                        set(String.format(Constants.REDIS_MODULE_TAB_EXPORT_TABLE, moduleId), sqlMap);
                    } else {
                        log.error("模型[{}]未配置模型和Tab字段的导出关联信息, 跳过缓存.", module.getCode());
                    }
                }
            }
            return true;
        } catch (Exception e) {
            log.error("刷新模型和Tab字段的关联[{}]信息失败.", setting.getId(), e);
            return false;
        }
    }
}
