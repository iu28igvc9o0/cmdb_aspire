package com.aspire.ums.cmdb.v2.module.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.aspire.ums.cmdb.code.payload.CmdbCode;
import com.aspire.ums.cmdb.code.payload.CmdbModuleCodeGroup;
import com.aspire.ums.cmdb.code.payload.CmdbModuleCodeValidate;
import com.aspire.ums.cmdb.code.payload.CmdbSimpleCode;
import com.aspire.ums.cmdb.collectApproval.payload.CmdbCollectApproval;
import com.aspire.ums.cmdb.common.Constants;
import com.aspire.ums.cmdb.common.throwable.CmdbRuntimeException;
import com.aspire.ums.cmdb.dict.payload.ConfigDict;
import com.aspire.ums.cmdb.dict.service.ConfigDictService;
import com.aspire.ums.cmdb.helper.JDBCHelper;
import com.aspire.ums.cmdb.module.payload.Module;
import com.aspire.ums.cmdb.module.payload.ModuleTag;
import com.aspire.ums.cmdb.module.payload.SimpleModule;
import com.aspire.ums.cmdb.schema.mapper.SchemaMapper;
import com.aspire.ums.cmdb.schema.service.SchemaService;
import com.aspire.ums.cmdb.sqlManage.CmdbSqlManage;
import com.aspire.ums.cmdb.util.StringUtils;
import com.aspire.ums.cmdb.util.UUIDUtil;
import com.aspire.ums.cmdb.v2.code.mapper.CmdbModuleCodeGroupMapper;
import com.aspire.ums.cmdb.v2.code.service.ICmdbCodeService;
import com.aspire.ums.cmdb.v2.collect.service.CmdbCollectApprovalService;
import com.aspire.ums.cmdb.v2.module.CmdbConst;
import com.aspire.ums.cmdb.v2.module.mapper.ModuleMapper;
import com.aspire.ums.cmdb.v2.module.mapper.ModuleTagMapper;
import com.aspire.ums.cmdb.v2.module.service.ModuleService;
import com.aspire.ums.cmdb.v2.module.service.ModuleTabelService;
import com.aspire.ums.cmdb.v3.authorization.payload.CmdbV3Authorization;
import com.aspire.ums.cmdb.v3.code.payload.CmdbV3CodeApprove;
import com.aspire.ums.cmdb.v3.code.payload.CmdbV3CodeBindSource;
import com.aspire.ums.cmdb.v3.code.payload.CmdbV3CodeCollect;
import com.aspire.ums.cmdb.v3.code.payload.CmdbV3CodeValidate;
import com.aspire.ums.cmdb.v3.config.payload.CmdbConfig;
import com.aspire.ums.cmdb.v3.config.service.ICmdbConfigService;
import com.aspire.ums.cmdb.v3.es.service.ICmdbESService;
import com.aspire.ums.cmdb.v3.module.mapper.CmdbV3ModuleAuthorizationMapper;
import com.aspire.ums.cmdb.v3.module.mapper.CmdbV3ModuleCodeSettingMapper;
import com.aspire.ums.cmdb.v3.module.mapper.CmdbV3ModuleRefRelationMapper;
import com.aspire.ums.cmdb.v3.module.payload.CmdbV3ModuleAuthorization;
import com.aspire.ums.cmdb.v3.module.payload.CmdbV3ModuleCatalog;
import com.aspire.ums.cmdb.v3.module.payload.CmdbV3ModuleCodeSetting;
import com.aspire.ums.cmdb.v3.module.payload.CmdbV3ModuleRefRelation;
import com.aspire.ums.cmdb.v3.module.service.ICmdbV3ModuleCatalogService;
import com.aspire.ums.cmdb.v3.module.service.ICmdbV3ModuleCodeSettingService;
import com.aspire.ums.cmdb.v3.redis.service.IRedisService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 *
 * <p>Project: ums-cmdb-service</p>
 *
 * @Description:
 *
 * @author: mingjianyong
 *
 * @Date: 2017-6-30
 */
@Service
@Slf4j
public class ModuleServiceImpl implements ModuleService {

    public static final String FILED_REF_MODULE_ID = "ref_module_id";
    public static final String FILED_OWNER_MODULE_ID = "owner_module_id";
    public static final String FILED_REFALIAS = "refAlias";
    public static final String LOG_ERROR_TEMPLATE = "模型[{}]缺少顶级模型分组信息.";

    @Autowired
    private ModuleMapper moduleMapper;
    @Autowired
    private ICmdbV3ModuleCatalogService catalogService;
    @Autowired
    private CmdbV3ModuleCodeSettingMapper codeSettingMapper;
    @Autowired
    private CmdbModuleCodeGroupMapper moduleCodeGroupMapper;
    @Autowired
    private ModuleTagMapper tagMapper;
    @Autowired
    private CmdbV3ModuleAuthorizationMapper moduleAuthMapper;
    @Autowired
    private CmdbV3ModuleRefRelationMapper refRelationMapper;
    @Autowired
    private ModuleTabelService tabelService;
    @Autowired
    private IRedisService redisService;
    @Autowired
    private ICmdbCodeService codeService;
    @Autowired
    private SchemaService schemaService;
    @Autowired
    private ConfigDictService dictService;
    @Autowired
    private ICmdbESService esService;
    @Autowired
    private ICmdbV3ModuleCodeSettingService codeSettingService;
    @Autowired
    private CmdbCollectApprovalService approvalService;
    @Autowired
    private ICmdbConfigService configService;
    @Autowired
    private SchemaMapper schemaMapper;
    @Autowired
    private JDBCHelper jdbcHelper;
    @Value("${cmdb.schema.name}")
    private String schemaName;
    @Value("${cmdb.query.db:mysql}")
    private String queryDbType;

    @Override
    public Module getDefaultModule(String moduleType) {
        // todo 后面改成配置管理的形式
        String colName = CmdbConst.CMDB_DEFAULT_CI_MODULE_ID;
        if (CmdbConst.CMDB_MODULE_TYPE_HOST.equalsIgnoreCase(moduleType)) {
            colName = CmdbConst.CMDB_DEFAULT_CI_MODULE_ID;
        } else if (CmdbConst.CMDB_MODULE_TYPE_BUSINESS.equalsIgnoreCase(moduleType)) {
            colName = CmdbConst.CMDB_DEFAULT_BUSINESS_MODULE_ID;
        } else if (CmdbConst.CMDB_MODULE_TYPE_DICT.equalsIgnoreCase(moduleType)) {
            colName = CmdbConst.CMDB_DEFAULT_DICT_MODULE_ID;
        } else if (CmdbConst.CMDB_DEFAULT_PRODUCE_MODULE_ID.equals(moduleType)) {
            colName = CmdbConst.CMDB_DEFAULT_PRODUCE_MODULE_ID;
        } else if (CmdbConst.CMDB_DEFAULT_BUSINESS_SYSTEM_ID.equalsIgnoreCase(moduleType)) {
            colName = CmdbConst.CMDB_DEFAULT_BUSINESS_SYSTEM_ID;
        } else {
            if (StringUtils.isNotEmpty(moduleType)) {
                throw new CmdbRuntimeException("Not support query module by type -> " + moduleType);
            }
        }
        List<ConfigDict> dicts = dictService.selectDictsByType(colName, null, null, null);
        if (dicts.size() != 1) {
            throw new CmdbRuntimeException("Can't find default module_id config -> " + colName);
        }
        String moduleId = dicts.get(0).getValue();
        Module module = getModuleDetail(moduleId);
        if (module == null) {
            throw new CmdbRuntimeException("Can't find module record. module_id -> " + moduleId);
        }
        return module;
    }

    @Override
    public List<Module> getModuleTree(String catalogId, String moduleType) {
        Object object = redisService.get(String.format(Constants.REDIS_MODULE_CATALOG_TREE, catalogId));
        if (object == null) {
            return moduleMapper.getModuleTree(catalogId);
        }
        return (new ObjectMapper().convertValue(object, new TypeReference<List<Module>>(){}));
    }

    @Override
    public List<Module> getModuleList() {
        return moduleMapper.getModuleList();
    }

    /**
     * 根据id获取module数据
     * @param moduleId 模型数据
     * @return
     */
    @Override
    public Module getModuleDetail(String moduleId) {
        Object object = redisService.get(String.format(Constants.REDIS_MODULE_DETAIL, moduleId));
        if (object == null) {
            return  moduleMapper.getModuleDetail(moduleId);
        }
        return (new ObjectMapper().convertValue(object, new TypeReference<Module>(){}));
    }

    @Override
    public SimpleModule getSimpleModuleDetail(String moduleId) {
        return moduleMapper.getSimpleModuleDetail(moduleId);
    }

    @Override
    public List<Module> getModuleSelective(Module module) {
        return moduleMapper.getModuleSelective(module);
    }

    /**
     * 新增模型
     * @param module 模型数据
     * @return
     */
    @Override
    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    public void addModule(String topCatalogId, Module module) {
        validData(module);
        module = filterCode(module);
        String moduleId = UUIDUtil.getUUID();
        module.setId(moduleId);
        module.setIsDelete(0);
        module.setPartitionId(1);
        int maxIndex = moduleMapper.selectMaxIndex();
        module.setSortIndex(maxIndex + 1);
        module.setModuleCatalog(catalogService.getById(module.getCatalogId()));
        // 建表（如未成功则删除刚存储的标签）
        try {
            createTable(module);
            //添加标签到模型标签表
            saveTags(moduleId, module.getTags());
            // 建完表添加字段关系
            addModuleCode("insert",module);
            // 存储模型权限信息
            saveModuleAuths(moduleId, module.getAuths());
            // 存储引用模型关系
            saveRefModule(moduleId, module.getRefModules());
            //插入数据库
            moduleMapper.insert(module);
        } catch (Exception e) {
            CmdbV3ModuleCatalog catalog = catalogService.getById(module.getCatalogId());
            tabelService.deleteEmptyTable(catalog.getCatalogCode());
            throw new CmdbRuntimeException("创建模型实例表失败" + e.getMessage());
        }
        // 刷新redis缓存
        redisService.syncRefresh(Constants.REDIS_TYPE_MODULE, moduleId);
    }

    //过滤重复CODE
    private Module filterCode(Module module) {
        CmdbCode errorCode = null;
        try {
            List<String> existCodeIds = new ArrayList<>();
            for (CmdbModuleCodeGroup group : module.getGroupList()) {
                List<CmdbCode> copyCodeList = group.getCodeList();
                for (CmdbCode code : group.getCodeList()) {
                    if (existCodeIds.contains(code.getCodeId())) {
                        errorCode = code;
                        copyCodeList.remove(code);
                    } else {
                        existCodeIds.add(code.getCodeId());
                    }
                }
                group.setCodeList(copyCodeList);
            }
        } catch (Exception e) {
            if (errorCode != null) {
                log.error(errorCode.getFiledCode() + "----" + errorCode.getFiledName() + "--- 重复");
            }
        }
        return module;
    }

    private void validData(Module module) {
        Module m = new Module();
        if (StringUtils.isEmpty(module.getName())) {
            throw new CmdbRuntimeException("模型名称不能为空");
        }
        if (StringUtils.isEmpty(module.getCode())) {
            throw new CmdbRuntimeException("模型编码不能为空");
        }
        m.setCode(module.getCode());
        List<Module> modules = moduleMapper.getModuleSelective(m);
        if (StringUtils.isEmpty(module.getId())) {
            if (modules != null && modules.size() > 0) {
                throw new CmdbRuntimeException("模型编码已存在");
            }
        } else {
            if (modules != null && modules.size() > 1) {
                throw new CmdbRuntimeException("模型编码已存在");
            }
        }
        // 检验模型分组
        if (StringUtils.isEmpty(module.getCatalogId())) {
            throw new CmdbRuntimeException("模型分组不能为空");
        }
        CmdbV3ModuleCatalog catalog = catalogService.getById(module.getCatalogId());
        if (catalog == null) {
            throw new CmdbRuntimeException("当前分组不存在，请查看是否被他人删除！");
        }
        m = new Module();
        m.setCatalogId(module.getCatalogId());
        modules = moduleMapper.getModuleSelective(m);
        if (StringUtils.isEmpty(module.getId())) {
            if (modules != null && modules.size() > 0) {
                throw new CmdbRuntimeException("当前分组模型已存在");
            }
        } else {
            if (modules != null && modules.size() > 1) {
                throw new CmdbRuntimeException("当前分组模型已存在");
            }
        }
        CmdbV3ModuleCatalog topModule = catalogService.getTopCatalog(module.getCatalogId());
        if (!StringUtils.isEmpty(module.getId())) {
            m = moduleMapper.getModuleDetail(module.getId());
            if (m == null) {
                throw new CmdbRuntimeException("当前模型已不存在，请检查是否被他人删除！");
            }
            List<Module> refModules = module.getRefModules();
            if (refModules != null && refModules.size() > 0) {
                for (Module refM : refModules) {
                    CmdbV3ModuleRefRelation refRelation = new CmdbV3ModuleRefRelation();
                    refRelation.setModuleId(refM.getId());
                    refRelation.setRefModuleId(module.getId());
                    List<CmdbV3ModuleRefRelation> relations = refRelationMapper.listByEntity(refRelation);
                    if (relations != null && relations.size() > 0) {
                        Module refModule = new ObjectMapper().convertValue(redisService.get(String.format(Constants.REDIS_MODULE_DETAIL, refM.getId())), new TypeReference<Module>(){});
                        throw new CmdbRuntimeException("当前模型已与模型"+ refModule.getName() +"存在引用关系");
                    }
                    CmdbV3ModuleCatalog refCatalog = catalogService.getByModuleId(refM.getId());
                    CmdbV3ModuleCatalog refTopModule = catalogService.getTopCatalog(refCatalog.getId());
                    if (!refTopModule.getCatalogCode().equals(topModule.getCatalogCode())) {
                        throw new CmdbRuntimeException("引用模型 " +  refM.getName() + " 与当前模型不在同一分组下，只能引用当前模型所属分组下模型！");
                    }
                }
            }
        }
        // 必须包含的列 验证
        validMustHasColumns(module);
    }

    private void validMustHasColumns(Module module) {
//        if (module.getIsVice().intValue() == Constants.IS_TRUE) {
//            return;
//        }
        CmdbConfig cmdbConfig = new CmdbConfig();
        String moduleMustIncludeProperties;
        if (module.getIsVice() == Constants.IS_TRUE) {
            cmdbConfig = configService.getConfigByCode("vice_module_must_include_properties");
            moduleMustIncludeProperties = "id";
            if (cmdbConfig != null) {
                moduleMustIncludeProperties = cmdbConfig.getConfigValue();
            }
        } else {
            cmdbConfig = configService.getConfigByCode("module_must_include_properties");
            moduleMustIncludeProperties = "id,module_id,insert_person,insert_time,update_person,update_time";
            if (cmdbConfig != null) {
                moduleMustIncludeProperties = cmdbConfig.getConfigValue();
            }
        }

//        CmdbConfig cmdbConfig = configService.getConfigByCode("module_must_include_properties");
//        CmdbConfig viceCmdbConfig = configService.getConfigByCode("vice_module_must_include_properties");
//        String moduleMustIncludeProperties = "id,module_id,insert_person,insert_time,update_person,update_time";
//        String viceModuleMustIncludeProperties = "id";
//        if (cmdbConfig != null && module.getIsVice() == Constants.IS_FALSE) {
//            moduleMustIncludeProperties = cmdbConfig.getConfigValue();
//        }
//        if (viceCmdbConfig != null && module.getIsVice() == Constants.IS_TRUE) {
//            viceModuleMustIncludeProperties = cmdbConfig.getConfigValue();
//        }
        List<String> mustProperties = Arrays.asList(moduleMustIncludeProperties.split(","));
        List<String> hasProperties = new ArrayList<>();
        for (CmdbModuleCodeGroup group : module.getGroupList()) {
            if (group.getCodeList() != null && group.getCodeList().size() > 0) {
                // 如果必须包含的列中存在, 则移除
                group.getCodeList().forEach((code) -> {
                    if (mustProperties.contains(code.getFiledCode())) {
                        if (!hasProperties.contains(code.getFiledCode())) {
                            hasProperties.add(code.getFiledCode());
                        }
                    }
                });
            }
        }
        if (mustProperties.size() != hasProperties.size()) {
            throw new CmdbRuntimeException("模型中必须包含配置项" + (module.getIsVice() == Constants.IS_TRUE ? "[ID]" : "[ID、模型ID、录入人、录入时间、最后更新人、最后更新时间]."));
        }
    }

    private void saveRefModule(String moduleId, List<Module> refModules) {
        if (null != refModules && refModules.size() > 0) {
            int i = 0;
            for (Module ref : refModules) {
                i++;
                CmdbV3ModuleRefRelation moduleRefRelation = new CmdbV3ModuleRefRelation();
                moduleRefRelation.setId(UUIDUtil.getUUID());
                moduleRefRelation.setModuleId(moduleId);
                moduleRefRelation.setIsDelete(0);
                moduleRefRelation.setRefModuleId(ref.getId());
                moduleRefRelation.setSortIndex(i);
                refRelationMapper.insert(moduleRefRelation);
            }
        }
    }

    private void saveModuleAuths(String moduleId, List<CmdbV3Authorization> auths) {
        if (null != auths && auths.size() > 0) {
            for (CmdbV3Authorization ma : auths) {
                CmdbV3ModuleAuthorization moduleAuth = new CmdbV3ModuleAuthorization();
                moduleAuth.setId(UUIDUtil.getUUID());
                moduleAuth.setAuthId(ma.getId());
                moduleAuth.setIsDelete(0);
                moduleAuth.setModuleId(moduleId);
                moduleAuthMapper.insert(moduleAuth);
            }
        }

    }

    private void saveTags(String moduleId, List<ModuleTag> tags) {
        if (null != tags && tags.size() > 0) {
            for (ModuleTag tag : tags) {
                tag.setId(UUIDUtil.getUUID());
                tag.setModuleId(moduleId);
                tagMapper.insertSelective(tag);
            }
        }
    }

    @Override
    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    public void updateModule(String topCatalogId, Module module) {
        long startTime = System.currentTimeMillis();
        validData(module);
        log.info("------1.检验模型 消耗时间 {}", System.currentTimeMillis() - startTime);

        startTime = System.currentTimeMillis();
        //过滤重复code
        module = filterCode(module);
        log.info("------2.过滤模型重复字段 消耗时间 {}", System.currentTimeMillis() - startTime);

        startTime = System.currentTimeMillis();
        // 更新module数据
        moduleMapper.update(module);
        log.info("------3.更新模型基本信息 消耗时间 {}", System.currentTimeMillis() - startTime);

        startTime = System.currentTimeMillis();
        ModuleTag tag = new ModuleTag();
        tag.setModuleId(module.getId());
        // 删除已存在的相关tag数据
        tagMapper.deleteTagSelective(tag);
        saveTags(module.getId(), module.getTags());
        log.info("------4.保存模型新标签信息 消耗时间 {}", System.currentTimeMillis() - startTime);

        startTime = System.currentTimeMillis();
        // 删除已存在的相关权限数据
        CmdbV3ModuleAuthorization moduleAuth = new CmdbV3ModuleAuthorization();
        moduleAuth.setModuleId(module.getId());
        moduleAuthMapper.delete(moduleAuth);
        saveModuleAuths(module.getId(), module.getAuths());
        log.info("------5.保存模型新权限信息 消耗时间 {}", System.currentTimeMillis() - startTime);

        startTime = System.currentTimeMillis();
        // 删除已存在的相关引用数据
        CmdbV3ModuleRefRelation refRelation = new CmdbV3ModuleRefRelation();
        refRelation.setModuleId(module.getId());
        refRelationMapper.delete(refRelation);
        saveRefModule(module.getId(), module.getRefModules());
        log.info("------5.保存模型新引用信息 消耗时间 {}", System.currentTimeMillis() - startTime);
        addModuleCode("update", module);
        startTime = System.currentTimeMillis();
        this.redisService.syncRefresh(Constants.REDIS_TYPE_MODULE, module.getId());
        log.info("------ 更新REDIS模型信息 消耗时间 {}", System.currentTimeMillis() - startTime);
        //
    }

    @Override
    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    public void updateModuleSort(String sortType, String moduleId) {
        CmdbV3ModuleCatalog catalog = moduleMapper.getModuleDetail(moduleId).getModuleCatalog();
        Module limitSort = moduleMapper.selectLimitSort(sortType,moduleId,catalog.getParentCatalogId());
        if (null != limitSort) {
            Module updateModule = moduleMapper.getModuleDetail(moduleId);
            int t1 = updateModule.getSortIndex();
            int t2 = limitSort.getSortIndex();
            limitSort.setSortIndex(t1);
            moduleMapper.update(limitSort);
            updateModule.setSortIndex(t2);
            moduleMapper.update(updateModule);
            // 刷新redis缓存
            this.redisService.syncRefresh(Constants.REDIS_TYPE_MODULE, moduleId);
        }
    }

    @Override
    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    public void deleteModule(String moduleId) {
        Module m = moduleMapper.getModuleDetail(moduleId);
        if (m == null) {
            throw new CmdbRuntimeException("当前模型不存在，请查看是否已被他人删除！");
        }
        String topCatalogId = catalogService.getTopCatalog(m.getCatalogId()).getId();
        if (m.getChildModules().size() > 0) {
            throw new CmdbRuntimeException("当前模型下有子级，请先删除子级！");
        };
        String tableName = m.getModuleCatalog().getCatalogCode();
        // 查询当前模型是否被引用
        CmdbV3ModuleRefRelation refRelation = new CmdbV3ModuleRefRelation();
        refRelation.setRefModuleId(m.getId());
        List<CmdbV3ModuleRefRelation> relations = refRelationMapper.listByEntity(refRelation);
        if(relations != null && relations.size() > 0) {
            throw new CmdbRuntimeException("当前模型已被" + relations.size() +"个模型引用，请先解除引用！");
        }
        // 查询当前模型下是否有Ci数据
        Integer i = tabelService.getTableDataCount(tableName);
        if (i > 0) {
            throw new CmdbRuntimeException("当前模型下尚有实例CI实例存在，请先删除CI实例！");
        }
        CmdbModuleCodeGroup group = new CmdbModuleCodeGroup();
        CmdbModuleCodeValidate validate = new CmdbModuleCodeValidate();
        CmdbV3ModuleAuthorization auth = new CmdbV3ModuleAuthorization();
        CmdbV3ModuleRefRelation relation = new CmdbV3ModuleRefRelation();
        CmdbCollectApproval approval = new CmdbCollectApproval();
        ModuleTag tag = new ModuleTag();
        group.setModuleId(moduleId);
        validate.setModuleId(moduleId);
        tag.setModuleId(moduleId);
        auth.setModuleId(moduleId);
        relation.setModuleId(moduleId);
        approval.setModuleId(moduleId);
        // 删除模型分组
        moduleCodeGroupMapper.deleteBySelective(group);
        log.info("------------删除模型模型成功----------");
        // 删除模型标签
        tagMapper.deleteTagSelective(tag);
        log.info("------------删除模型标签成功----------");
        // 删除模型码表字段关系
        codeSettingMapper.deleteByModuleId(moduleId);
        log.info("------------删除模型码表字段关系成功----------");
        // 删除模型权限
        moduleAuthMapper.delete(auth);
        log.info("------------删除模型权限成功----------");
        // 删除模型引用模型关系
        refRelationMapper.delete(relation);
        log.info("------------删除模型引用模型关系成功----------");
        // 删除对该模型的配置审核;
        approvalService.delete(approval);
        log.info("------------删除对该模型的配置审核成功----------");
        if (tabelService.hasTable(tableName)) {
            tabelService.deleteTableByName(m);
            log.info("------------删除对该模型的数据表成功----------");
        }
        m = new Module();
        m.setId(moduleId);
        m.setIsDelete(1);
        moduleMapper.update(m);
        this.redisService.syncRefresh(Constants.REDIS_TYPE_MODULE, moduleId);
        this.redisService.syncRefresh(Constants.REDIS_TYPE_CATALOG, topCatalogId);
    }

    /**
     * 新增模型字段分组关系
     * @return
     */
    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    public void addModuleCode(String type, Module module) {
        long startTime = System.currentTimeMillis();
        List<CmdbModuleCodeGroup> groupList = module.getGroupList();
        // 未更新前的模型表中字段
        List<CmdbCode> oldCodes = codeService.getSelfCodeListByModuleId(module.getId());
        int groupIndex = 1;
        List<CmdbModuleCodeGroup> addGroups = new ArrayList<>();
        List<CmdbV3ModuleCodeSetting> codeSettings = new ArrayList<>();
        List<CmdbV3CodeValidate> addValidates = new ArrayList<>();
        List<CmdbV3CodeApprove> addApproves = new ArrayList<>();
        List<CmdbV3CodeBindSource> addBinsSources = new ArrayList<>();
        List<CmdbV3CodeCollect> addCollects = new ArrayList<>();
        List<CmdbCode> allCodeList = new ArrayList<>();
        // 删除原先模型分组数据
        moduleCodeGroupMapper.deleteByModuleId(module.getId());
        // 删除原先模型-分组-码表关系数据
        codeSettingMapper.deleteByModuleId(module.getId());
        for (CmdbModuleCodeGroup group : groupList) {
            String groupId = UUIDUtil.getUUID();
            List<CmdbCode> codeList = group.getCodeList();
            if (codeList.size() == 0) {
                continue;
            }
            CmdbV3ModuleCodeSetting codeSet = codeList.get(0).getCodeSetting();
            // 当不是引用模型数据时（组名为空）
            if (StringUtils.isNotEmpty(group.getGroupName()) &&
                    (StringUtils.isEmpty(codeSet.getOwnerModuleId()) ||
                            codeSet.getOwnerModuleId().equals(codeSet.getModuleId()))) {
                group.setGroupName(group.getGroupName());
                allCodeList.addAll(group.getCodeList());
            } else {
                Module m = moduleMapper.getModuleDetail(codeSet.getOwnerModuleId());
                // 如果是引用模型, 则区域分组ID直接使用引用模型的ID
                groupId = m.getId();
                group.setGroupName(m.getName());
            }
            group.setModuleId(module.getId());
            group.setGroupId(groupId);
            group.setIsDelete(0);
            group.setSortIndex(groupIndex);
            addGroups.add(group);
            int codeIndex = 1;
            for (CmdbCode code : codeList) {
                // 存模型字段关系
                CmdbV3ModuleCodeSetting codeSetting = code.getCodeSetting();
                if (codeSetting != null) {
                    codeSetting.setModuleId(module.getId());
                    codeSetting.setIsDelete(0);
                    codeSetting.setId(UUIDUtil.getUUID());
                    codeSetting.setCodeId(code.getCodeId());
                    codeSetting.setGroupId(groupId);
                    codeSetting.setSortIndex(codeIndex);
                    if (StringUtils.isEmpty(codeSetting.getOwnerModuleId())) {
                        codeSetting.setOwnerModuleId(module.getId());
                    }
                    codeSettings.add(codeSetting);
                }
                List<CmdbV3CodeValidate> validates = code.getValidates();
                if (validates != null) {
                    for (CmdbV3CodeValidate validate : validates) {
                        validate.setId(UUIDUtil.getUUID());
                        validate.setCodeId(code.getCodeId());
                        addValidates.add(validate);
                    }
                }
                CmdbV3CodeApprove approve = code.getApprove();
                if (approve != null) {
                    approve.setId(UUIDUtil.getUUID());
                    approve.setCodeId(code.getCodeId());
                    addApproves.add(approve);
                }
                CmdbV3CodeBindSource bindSource = code.getCodeBindSource();
                if (bindSource != null) {
                    bindSource.setId(UUIDUtil.getUUID());
                    bindSource.setCodeId(code.getCodeId());
                    bindSource.setIsDelete(0);
                    addBinsSources.add(bindSource);
                }
                CmdbV3CodeCollect collect = code.getCodeCollect();
                if (collect != null) {
                    collect.setId(UUIDUtil.getUUID());
                    collect.setCodeId(code.getCodeId());
                    addCollects.add(collect);
                }
                codeIndex++;
            }
            groupIndex++;
        }
        if (addGroups.size() > 0) {
            moduleCodeGroupMapper.insertByBatch(addGroups);
        }
        if (codeSettings.size() > 0) {
            codeSettingMapper.insertByBatch(codeSettings);
        }
        log.info("------ 处理模型字段关系 消耗时间 {}", System.currentTimeMillis() - startTime);
        if ("update".equals(type)) {
            addTableColumn(module, oldCodes, allCodeList);
        }
    }




    @Override
    public List<ModuleTag> getModuleTag(String moduleId) {
        return tagMapper.selectByModuleId(moduleId);
    }

    @Override
    public List<Map<String, Object>> queryTableData(Map<String, Object> queryData) {
        List<Map<String, Object>> result = new ArrayList<>();
        String sql = (String) queryData.get("sql");
        if (StringUtils.isNotEmpty(sql)) {
            result = tabelService.getDataBySql(sql);
        }
        return result;
    }

    @Override
    public String moveModuleData() {
        StringBuilder builder = new StringBuilder();
        String sql = "insert into %s (id, is_delete)" +
                "select id, is_delete from cmdb.cmdb_instance where device_class='%s' and is_delete = '0'; \n";
//        // 主机资源
        builder.append(handlerModuleData(sql, "96603733-5793-11ea-ab96-fa163e982f89"));
        // 维保信息
        builder.append(handlerModuleData(sql, "966b1b9b-5793-11ea-ab96-fa163e982f89"));
//        // 处理服务器类
        builder.append(handlerModuleData(sql,"80e427ff5bf44eef85d93602b2131ee0"));
//        // 网络设备
        builder.append(handlerModuleData(sql,"e5077f28300e4ab98509d5fd845a8604"));
//        // 存储设备
        builder.append(handlerModuleData(sql,"3858ea28bb524170857a8e8a3001c9e5"));
//        // 安全设备
        builder.append(handlerModuleData(sql,"6faa6b6f83f74ddb8ce958acca488d08"));
        return builder.toString();
    }

    private StringBuilder handlerModuleData (String sql, String moduleId) {
        StringBuilder builder = new StringBuilder();
        // 迁移网络设备
        Module networkModule = moduleMapper.getModuleDetail(moduleId);
        builder.append("-- 处理" + networkModule.getName() + "---------").append("\n");
        builder.append(String.format(sql, networkModule.getModuleCatalog().getCatalogCode(), networkModule.getName())).append(" \n");
        CmdbV3ModuleCodeSetting codeSetting = new CmdbV3ModuleCodeSetting();
        codeSetting.setModuleId(moduleId);
        codeSetting.setOwnerModuleId(moduleId);
        // 拿到自己的码表信息
        List<CmdbV3ModuleCodeSetting> settingList = codeSettingService.listByEntity(codeSetting);
        if (settingList != null && settingList.size() > 0) {
            for (CmdbV3ModuleCodeSetting setting : settingList) {
                CmdbCode queryCode = new CmdbCode();
                queryCode.setCodeId(setting.getCodeId());
                CmdbCode cmdbCode = codeService.get(queryCode);
                if (cmdbCode == null) {
                    continue;
                }
                String codeSql = "select TABLE_NAME from information_schema.`COLUMNS` c where c.COLUMN_NAME = '" + cmdbCode.getFiledCode() +
                        "' and c.table_name in (select s.table_name from information_schema.`COLUMNS` s where s.COLUMN_NAME = " +
                        " 'instance_id' and s.TABLE_SCHEMA='cmdb') and c.TABLE_SCHEMA='cmdb'";
                // 主机资源
                if (moduleId.equals("96603733-5793-11ea-ab96-fa163e982f89")) {
                    codeSql = "select TABLE_NAME from information_schema.`COLUMNS` c where c.COLUMN_NAME = '" + cmdbCode.getFiledCode() +
                            "' and c.table_name in (select s.table_name from information_schema.`COLUMNS` s where s.TABLE_NAME = " +
                            " 'cmdb_instance' and s.TABLE_SCHEMA='cmdb') and c.TABLE_SCHEMA='cmdb'";
                }
                List<Map<String, Object>> dataList = schemaService.executeSql(codeSql);
                if (dataList == null || dataList.size() == 0) {
                    continue;
                }
                String querySql = "";
                for (Map<String, Object> data : dataList) {
                    String tableName = data.get("TABLE_NAME").toString();
                    if (tableName.endsWith("_his") || tableName.endsWith("_bak") || tableName.equals("cmdb_instance_ip_manager") ||
                            tableName.startsWith("vw_") || tableName.contains("_bak20")) {
                        continue;
                    }
                    if (tableName.equalsIgnoreCase("cmdb_instance_vulnerability_scan")) {
                        continue;
                    }
                    if (tableName.equalsIgnoreCase("cmdb_instance_maintenance")) {
                        continue;
                    }
                    if (!querySql.equals("")) {
                        querySql += " union ";
                    }
                    if (tableName.equals("cmdb_instance")) {
                        querySql += "select id instance_id, " + cmdbCode.getFiledCode() + " from cmdb." + tableName;
                    } else {
                        querySql += "select instance_id, " + cmdbCode.getFiledCode() + " from cmdb." + tableName;
                    }
                }
                if (!("").equals(querySql)) {
                    String updateSQL = "update " + networkModule.getModuleCatalog().getCatalogCode() + " c, (" + querySql + ") t set c." + cmdbCode.getFiledCode() + " = t." + cmdbCode.getFiledCode() +
                            " where c.id = t.instance_id; ";
                    builder.append(updateSQL).append(" \n");
                }
            }
        }
        // 挨个迁移每个指标项数据
        List<Module> networkChild = moduleMapper.getChildByCatalogId(networkModule.getCatalogId());
        // 迁移网络设备下的所有子模型
        for (Module netModule : networkChild) {
            String sql1 = "insert into %s (id, is_delete)\n" +
                    "select id, is_delete from cmdb.cmdb_instance where device_type='%s' and is_delete = '0';";
            try {
                builder.append("-- 处理" + netModule.getName() + "------").append("\n");
                builder.append(handlerModuleData(sql1, netModule.getId()));
            }  catch (Exception e) {
                log.error("模型{}处理失败.", netModule.getModuleCatalog().getCatalogCode(), sql1);
            }
        }
        return builder;
    }

    @Override
    public String getModuleQuerySQL(String moduleId) {
        Object object = redisService.get(String.format(Constants.REDIS_MODULE_TABLE, moduleId));
        String sql;
        if (object == null) {
            Module module = this.getModuleDetail(moduleId);
            CmdbV3ModuleCodeSetting codeSetting = new CmdbV3ModuleCodeSetting();
            codeSetting.setModuleId(moduleId);
            List<CmdbV3ModuleCodeSetting> codeSettingList = codeSettingMapper.listByEntity(codeSetting);
            String moduleTableName = module.getModuleCatalog().getCatalogCode();
            // 对code_setting进行分组
            Map<String, List<CmdbV3ModuleCodeSetting>> groupMap = new HashMap<>();
            if (codeSettingList == null || codeSettingList.size() == 0) {
                return "";
            }
            for (CmdbV3ModuleCodeSetting setting : codeSettingList) {
                // 如果是设置为不显示的字段, 则跳过更新
                if (setting.getDisplay() == 1) {
                    continue;
                }
                String ownerModuleId = setting.getOwnerModuleId();
                if (StringUtils.isEmpty(ownerModuleId)) {
                    ownerModuleId = setting.getModuleId();
                }
                List<CmdbV3ModuleCodeSetting> list = new LinkedList<>();
                if (groupMap.containsKey(ownerModuleId)) {
                    list = groupMap.get(ownerModuleId);
                }
                list.add(setting);
                groupMap.put(ownerModuleId, list);
            }
            // 开始组装SQL
            // 组装字段部分
            StringBuilder filedBuf = new StringBuilder();
            // 用来装审核处理字段数据
            StringBuilder approveFiledBuf = new StringBuilder();
            StringBuilder refFiledBuf = new StringBuilder();
            StringBuilder tableFiledBuf = new StringBuilder();
            String refModuleTableTemplate = "ref_tb_%s";
            String tableTableTemplate = "table_%s";
            String queryFiledTemplate = "`%s`.`%s`";
            String queryRefFiledTemplate = "`" + refModuleTableTemplate + "`.`%s` `%s_name`";
            String queryTableFiledTemplate = "`" + tableTableTemplate + "`.`value` `%s_value_name`";
            // 识别引用模型配置项
            Map<String, Map<String, Object>> refCodeMap = new HashMap<>();
            Map<String, Map<String, Object>> tableCodeMap = new HashMap<>();
            CmdbConfig cmdbConfig = configService.getConfigByCode("not_need_query_filed4cmdb_instance");
            boolean flag = false;
            for (Map.Entry<String, List<CmdbV3ModuleCodeSetting>> entry : groupMap.entrySet()) {
                String temModuleId = entry.getKey();
                Module temModule = this.getModuleDetail(temModuleId);
                if (temModule == null) {
                    continue;
                }
                List<CmdbV3ModuleCodeSetting> temSettingList = entry.getValue();
                for (CmdbV3ModuleCodeSetting setting : temSettingList) {
                    CmdbCode queryCode = new CmdbCode();
                    queryCode.setCodeId(setting.getCodeId());
                    CmdbCode code = codeService.get(queryCode);
                    if (code != null) {
                        if ("id".equals(code.getFiledCode().toLowerCase(Locale.ENGLISH))) {
                            if (flag) {
                                continue;
                            }
                            flag = true;
                        }
                        if (null != cmdbConfig) {
                            boolean notNeedFiled = queryInstanceListNotNeedFiled(cmdbConfig, temModule.getCode(), code.getFiledCode());
                            if (!notNeedFiled) {
                                if (StringUtils.isNotEmpty(filedBuf.toString())) {
                                    filedBuf.append(",");
                                    approveFiledBuf.append(",");
                                }
                                filedBuf.append(String.format(queryFiledTemplate, temModule.getCode(), code.getFiledCode()));
                                approveFiledBuf.append("#{");
                                approveFiledBuf.append(code.getFiledCode());
                                approveFiledBuf.append("} ");
                                approveFiledBuf.append("`").append(code.getFiledCode()).append("`");
                            }
                        } else {
                            if (StringUtils.isNotEmpty(filedBuf.toString())) {
                                filedBuf.append(",");
                                approveFiledBuf.append(",");
                            }
                            filedBuf.append(String.format(queryFiledTemplate, temModule.getCode(), code.getFiledCode()));
                            approveFiledBuf.append("#{");
                            approveFiledBuf.append(code.getFiledCode());
                            approveFiledBuf.append("} ");
                            approveFiledBuf.append("`").append(code.getFiledCode()).append("`");
                        }
                        // 引用模型处理
                        if (code.getCodeBindSource() != null) {
                            if ("引用模型".equals(code.getCodeBindSource().getBindSourceType())) {
                                Map<String, Object> refMap = new HashMap<>();
                                refMap.put(FILED_REF_MODULE_ID, code.getCodeBindSource().getRefModuleId());
                                refMap.put("code", code);
                                refMap.put(FILED_OWNER_MODULE_ID, setting.getOwnerModuleId());
                                refMap.put(FILED_REFALIAS, String.format(refModuleTableTemplate, code.getFiledCode()));
                                refCodeMap.put(code.getFiledCode(), refMap);
                                String refCodeId = code.getCodeBindSource().getShowModuleCodeId();
                                CmdbSimpleCode refCode = codeService.getSimpleCodeById(refCodeId);
                                if (StringUtils.isNotEmpty(refFiledBuf.toString()) && null != refCode) {
                                    refFiledBuf.append(",");
                                }
                                if(null != refCode) {
                                    refFiledBuf.append(String.format(queryRefFiledTemplate, code.getFiledCode(), refCode.getFiledCode(), code.getFiledCode() + "_" + refCode.getFiledCode()));
                                } else {
                                    log.info("refCodeId:{}",refCodeId);
                                }
                            }
                            // 处理数据表
                            if ("数据表".equals(code.getCodeBindSource().getBindSourceType())) {
                                Map<String, Object> tableMap = new HashMap<>();
                                tableMap.put("code", code);
                                tableMap.put(FILED_REFALIAS, String.format(tableTableTemplate, code.getFiledCode()));
                                tableMap.put("table_sql", code.getCodeBindSource().getTableSql());
                                tableCodeMap.put(code.getFiledCode(), tableMap);
                                if (StringUtils.isNotEmpty(tableFiledBuf.toString())) {
                                    tableFiledBuf.append(",");
                                }
                                tableFiledBuf.append(String.format(queryTableFiledTemplate, code.getFiledCode(), code.getFiledCode()));
                            }
                        }
                    }
                }
            }
            // 判断是否是独立模型
            boolean isAloneModule = this.isAloneModule(moduleId);
            CmdbV3ModuleCatalog topCatalog = null;
            Module topModule = null;
            if (!isAloneModule) {
                // 获取顶级节点
                topCatalog = catalogService.getTopCatalog(module.getCatalogId());
                if (topCatalog == null) {
                    log.error(LOG_ERROR_TEMPLATE, module.getId());
                    return "";
                }
                // 根据模型分组ID获取模型信息
                topModule = getModuleDetailByCatalogId(topCatalog.getId());
                if (topModule == null) {
                    log.error(LOG_ERROR_TEMPLATE, module.getId());
                    return "";
                }
            }
            // 组装Join部分
            StringBuilder joinBuf = new StringBuilder();
            if (!isAloneModule) {
                for (String temModuleId : groupMap.keySet()) {
                    // 自身模型或定义模型信息, 过滤
                    if (temModuleId.equals(module.getId()) || temModuleId.equals(topModule.getId())) {
                        continue;
                    }
                    Module temModule = this.getModuleDetail(temModuleId);
                    if (temModule == null) {
                        continue;
                    }
                    String temModuleTable = temModule.getModuleCatalog().getCatalogCode();
                    joinBuf.append(" left join ").append(temModuleTable).append(" `").append(temModule.getCode()).append("` on (")
                            .append("`").append(temModule.getCode()).append("`.`id`").append(" = ")
                            .append("`").append(module.getCode()).append("`.`id`").append(" and ")
                            .append("`").append(temModule.getCode()).append("`.`is_delete`").append(" = 0 )");
                }
            }
            // 组装引用模型部分
            StringBuilder refBuf = new StringBuilder();
            for (String filedCode : refCodeMap.keySet()) {
                String refModuleId = refCodeMap.get(filedCode).get(FILED_REF_MODULE_ID).toString();
                CmdbCode code = (CmdbCode) refCodeMap.get(filedCode).get("code");
                Module refModule = getModuleDetail(refModuleId);
                String refAlias = refCodeMap.get(filedCode).get(FILED_REFALIAS).toString();
                refBuf.append("left join ").append(refModule.getModuleCatalog().getCatalogCode())
                        .append(" ").append(refAlias).append(" ").append("on (`tt`.`")
                        .append(code.getFiledCode()).append("`=`").append(refAlias).append("`.`id`")
                        .append("and `").append(refAlias).append("`.`is_delete` = '0')");
            }
            // 组装数据表查询部分
            StringBuilder tableBuf = new StringBuilder();
            for (String filedCode : tableCodeMap.keySet()) {
                CmdbCode code = (CmdbCode) tableCodeMap.get(filedCode).get("code");
                String tableAlias = tableCodeMap.get(filedCode).get(FILED_REFALIAS).toString();
                String tableSql = tableCodeMap.get(filedCode).get("table_sql").toString();
                tableBuf.append("left join ").append("(").append(tableSql).append(")")
                        .append(" ").append(tableAlias).append(" ").append("on (`tt`.`")
                        .append(code.getFiledCode()).append("`=`").append(tableAlias).append("`.`id`)");
            }
            //组装完整的SQL
            StringBuilder sqlBuffer = new StringBuilder();
            StringBuilder customerSQL = new StringBuilder();
            sqlBuffer.append(" <if test=\"customerType == null || customerType == ''\" >")
                    .append("select ")
                    .append(filedBuf)
                    .append(" from ").append(moduleTableName).append(" `").append(module.getCode()).append("` ");
            if (!isAloneModule && !module.getId().equals(topModule.getId())) {
                sqlBuffer.append(" inner join ").append(topCatalog.getCatalogCode()).append(" `").append(topModule.getCode()).append("` on (")
                        .append("`").append(topModule.getCode()).append("`.`id`").append(" = ")
                        .append("`").append(module.getCode()).append("`.`id`").append(" and ")
                        .append("`").append(topModule.getCode()).append("`.`is_delete`").append(" = 0 ").append(" and ")
                        .append("`").append(topModule.getCode()).append("`.`module_id`").append("='").append(moduleId).append("') ");
            }
            sqlBuffer.append(joinBuf);
            sqlBuffer.append(" where ").append(module.getCode()).append(".`is_delete` = 0 ");
            // 主机资源, 增加全量IP地址查询功能, 需要关联cmdb_instance_ip_manager表进行查询
            sqlBuffer.append(" <if test=\"cmdb_instance_ip_manager_ip != null and cmdb_instance_ip_manager_ip != ''\" >")
                    .append("and `").append(module.getCode()).append("`.id in (select instance_id from `cmdb_instance_ip_manager`")
                    .append("where ip like CONCAT('%',#{cmdb_instance_ip_manager_ip}, '%'))")
                    .append(" </if>");
            customerSQL.append("select ").append(approveFiledBuf);
            StringBuilder authSql = new StringBuilder();
            authSql.append("select * from (")
                    .append(sqlBuffer)
                    .append("</if>")
                    .append(" <if test=\"customerType != null and customerType == 'approve'\" >")
                    .append(customerSQL)
                    .append("</if>")
                    .append(") t where 1=1 ")
                    // 增加内置权限条件占位符
                    .append(Constants.INNER_AUTH_STRING).append(" ")
                    // 增加内置WHERE条件占位符
                    .append(Constants.INNER_WHERE_STRING).append(" ")
                    // 增加内置SORT占位符
                    .append(Constants.INNER_ORDER_STRING).append(" ")
//                    // 增加内置LIMIT占位符
                    .append(Constants.INNER_LIMIT_STRING);
            sql = "select tt.* ";
            if (StringUtils.isNotEmpty(refFiledBuf.toString())) {
                sql +=  "," + refFiledBuf;
            }
            if (StringUtils.isNotEmpty(tableFiledBuf.toString())) {
                sql += "," + tableFiledBuf;
            }
            sql += " from (" + authSql + ") tt " + refBuf + " " + tableBuf;
        } else {
            Map<String, String> sqlMap = (new ObjectMapper().convertValue(object, new TypeReference<Map<String, String>>(){}));
            sql = sqlMap.get("sql");
        }
        return sql;
    }

    /**
     * 判断是否是独立模型, 独立模型不引用其他任何模型
     * 独立模型是指 不需要依赖父模型, 自己包含id/module_id/insert...等列
     * @param moduleId 模型ID
     * @return
     */
    public boolean isAloneModule(String moduleId) {
        return moduleMapper.getOwnerModuleCount(moduleId) < 1;
    }

    @Override
    public String getModuleQueryCountSQL(String moduleId) {
        Object object = redisService.get(String.format(Constants.REDIS_MODULE_COUNT_TABLE, moduleId));
        String sql;
        if (object == null) {
            Module module = this.getModuleDetail(moduleId);
            CmdbV3ModuleCodeSetting codeSetting = new CmdbV3ModuleCodeSetting();
            codeSetting.setModuleId(moduleId);
            List<CmdbV3ModuleCodeSetting> codeSettingList = codeSettingMapper.listByEntity(codeSetting);
            String moduleTableName = module.getModuleCatalog().getCatalogCode();
            // 对code_setting进行分组
            Map<String, List<CmdbV3ModuleCodeSetting>> groupMap = new HashMap<>();
            if (codeSettingList == null || codeSettingList.size() == 0) {
                return "";
            }
            for (CmdbV3ModuleCodeSetting setting : codeSettingList) {
                // 如果是设置为不显示的字段, 则跳过更新
                if (setting.getDisplay() == 1) {
                    continue;
                }
                String ownerModuleId = setting.getOwnerModuleId();
                if (StringUtils.isEmpty(ownerModuleId)) {
                    ownerModuleId = setting.getModuleId();
                }
                List<CmdbV3ModuleCodeSetting> list = new LinkedList<>();
                if (groupMap.containsKey(ownerModuleId)) {
                    list = groupMap.get(ownerModuleId);
                }
                list.add(setting);
                groupMap.put(ownerModuleId, list);
            }

            // 判断是否是独立模型
            boolean isAloneModule = this.isAloneModule(moduleId);
            CmdbV3ModuleCatalog topCatalog = null;
            Module topModule = null;
            if (!isAloneModule) {
                // 获取顶级节点
                topCatalog = catalogService.getTopCatalog(module.getCatalogId());
                if (topCatalog == null) {
                    log.error(LOG_ERROR_TEMPLATE, module.getId());
                    return "";
                }
                // 根据模型分组ID获取模型信息
                topModule = getModuleDetailByCatalogId(topCatalog.getId());
                if (topModule == null) {
                    log.error(LOG_ERROR_TEMPLATE, module.getId());
                    return "";
                }
            }

            // 组装Join部分
            StringBuilder joinBuf = new StringBuilder();
            if (!isAloneModule) {
                for (String temModuleId : groupMap.keySet()) {
                    // 自身模型或定义模型信息, 过滤
                    if (temModuleId.equals(module.getId()) || temModuleId.equals(topModule.getId())) {
                        continue;
                    }
                    Module temModule = this.getModuleDetail(temModuleId);
                    if (temModule == null) {
                        continue;
                    }
                    String temModuleTable = temModule.getModuleCatalog().getCatalogCode();
                    joinBuf.append(" left join ").append(temModuleTable).append(" `").append(temModule.getCode()).append("` on (")
                            .append("`").append(temModule.getCode()).append("`.`id`").append(" = ")
                            .append("`").append(module.getCode()).append("`.`id`").append(" and ")
                            .append("`").append(temModule.getCode()).append("`.`is_delete`").append(" = 0 )");
                }
            }
            //组装完整的SQL
            StringBuilder sqlBuffer = new StringBuilder();
            sqlBuffer.append("select 1 from ").append(moduleTableName).append(" `").append(module.getCode()).append("` ");
            if (!isAloneModule && !module.getId().equals(topModule.getId())) {
                sqlBuffer.append(" inner join ").append(topCatalog.getCatalogCode()).append(" `").append(topModule.getCode()).append("` on (")
                        .append("`").append(topModule.getCode()).append("`.`id`").append(" = ")
                        .append("`").append(module.getCode()).append("`.`id`").append(" and ")
                        .append("`").append(topModule.getCode()).append("`.`is_delete`").append(" = 0 ").append(" and ")
                        .append("`").append(topModule.getCode()).append("`.`module_id`").append("='").append(moduleId).append("') ");
            }
            sqlBuffer.append(joinBuf);
            sqlBuffer.append(" where ").append(module.getCode()).append(".`is_delete` = 0 ");
            // 主机资源, 增加全量IP地址查询功能, 需要关联cmdb_instance_ip_manager表进行查询
            sqlBuffer.append(" <if test=\"cmdb_instance_ip_manager_ip != null and cmdb_instance_ip_manager_ip != ''\" >")
                    .append("and `").append(module.getCode()).append("`.id in (select instance_id from `cmdb_instance_ip_manager`")
                    .append("where ip like CONCAT('%',#{cmdb_instance_ip_manager_ip}, '%'))")
                    .append(" </if>");
            sqlBuffer.append(Constants.INNER_AUTH_STRING).append(" ")
                    .append(Constants.INNER_WHERE_STRING).append(" ");
            sql = sqlBuffer.toString();
        } else {
            Map<String, String> sqlMap = (new ObjectMapper().convertValue(object, new TypeReference<Map<String, String>>(){}));
            sql = sqlMap.get("sql");
        }
        return sql;
    }

    @Override
    public String getTempModuleQuerySQL(String moduleId) {
        Object object = redisService.get(String.format(Constants.REDIS_TEMP_MODULE_TABLE, moduleId));
        String sql;
        if (object == null) {
            Module module = this.getModuleDetail(moduleId);
            CmdbV3ModuleCodeSetting codeSetting = new CmdbV3ModuleCodeSetting();
            codeSetting.setModuleId(moduleId);
            List<CmdbV3ModuleCodeSetting> codeSettingList = codeSettingMapper.listByEntity(codeSetting);
            String moduleTableName = module.getModuleCatalog().getCatalogCode();
            // 对code_setting进行分组
            Map<String, List<CmdbV3ModuleCodeSetting>> groupMap = new HashMap<>();
            if (codeSettingList == null || codeSettingList.size() == 0) {
                return "";
            }
            for (CmdbV3ModuleCodeSetting setting : codeSettingList) {
                // 如果是设置为不显示的字段, 则跳过更新
                if (setting.getDisplay() == 1) {
                    continue;
                }
                String ownerModuleId = setting.getOwnerModuleId();
                if (StringUtils.isEmpty(ownerModuleId)) {
                    ownerModuleId = setting.getModuleId();
                }
                List<CmdbV3ModuleCodeSetting> list = new LinkedList<>();
                if (groupMap.containsKey(ownerModuleId)) {
                    list = groupMap.get(ownerModuleId);
                }
                list.add(setting);
                groupMap.put(ownerModuleId, list);
            }
            // 开始组装SQL
            // 组装字段部分
            StringBuilder filedBuf = new StringBuilder();
            StringBuilder refFiledBuf = new StringBuilder();
            String refModuleTableTemplate = "ref_tb_%s";
            String refModuleFiledNameTemplate = "%s";
            String queryFiledTemplate = "`%s`.`%s`";
            String queryRefFiledTemplate = "`" + refModuleTableTemplate + "`.`%s` `" + refModuleFiledNameTemplate + "`";
            // 识别引用模型配置项
            Map<String, Map<String, Object>> refCodeMap = new HashMap<>();
            for (String temModuleId : groupMap.keySet()) {
                Module temModule = this.getModuleDetail(temModuleId);
                if (temModule == null) {
                    continue;
                }
                List<CmdbV3ModuleCodeSetting> temSettingList = groupMap.get(temModuleId);
                for (CmdbV3ModuleCodeSetting setting : temSettingList) {
                    CmdbCode queryCode = new CmdbCode();
                    queryCode.setCodeId(setting.getCodeId());
                    CmdbCode code = codeService.get(queryCode);
                    if (code != null) {
                        // 引用模型处理
                        if (code.getCodeBindSource() != null && "引用模型".equals(code.getCodeBindSource().getBindSourceType())) {
                            String refCodeId = code.getCodeBindSource().getShowModuleCodeId();
                            CmdbSimpleCode refCode = codeService.getSimpleCodeById(refCodeId);
                            Map<String, Object> refMap = new HashMap<>();
                            refMap.put(FILED_REF_MODULE_ID, code.getCodeBindSource().getRefModuleId());
                            refMap.put("code", code);
                            refMap.put(FILED_OWNER_MODULE_ID, setting.getOwnerModuleId());
                            refMap.put(FILED_REFALIAS, String.format(refModuleTableTemplate, code.getFiledCode()));
                            refCodeMap.put(code.getFiledCode(), refMap);
                            if (StringUtils.isNotEmpty(refFiledBuf.toString())) {
                                refFiledBuf.append(",");
                            }
                            refFiledBuf.append(String.format(queryRefFiledTemplate, code.getFiledCode(), refCode.getFiledCode(), code.getFiledCode()));
                        } else {
                            if (StringUtils.isNotEmpty(filedBuf.toString())) {
                                filedBuf.append(",");
                            }
                            filedBuf.append(String.format(queryFiledTemplate, temModule.getCode(), code.getFiledCode()));
                        }
                    }
                }
            }
            // 获取顶级节点
            CmdbV3ModuleCatalog topCatalog = catalogService.getTopCatalog(module.getCatalogId());
            if (topCatalog == null) {
                log.error(LOG_ERROR_TEMPLATE, module.getId());
                return "";
            }
            // 根据模型分组ID获取模型信息
            Module topModule = getModuleDetailByCatalogId(topCatalog.getId());
            if (topModule == null) {
                log.error(LOG_ERROR_TEMPLATE, module.getId());
                return "";
            }
            // 组装Join部分
            StringBuilder joinBuf = new StringBuilder();
            for (String temModuleId : groupMap.keySet()) {
                // 自身模型或定义模型信息, 过滤
                if (temModuleId.equals(module.getId()) || temModuleId.equals(topModule.getId())) {
                    continue;
                }
                Module temModule = this.getModuleDetail(temModuleId);
                if (temModule == null) {
                    continue;
                }
                String temModuleTable = temModule.getModuleCatalog().getCatalogCode();
                joinBuf.append(" left join ").append(temModuleTable).append(" `").append(temModule.getCode()).append("` on (")
                        .append("`").append(temModule.getCode()).append("`.`id`").append(" = ")
                        .append("`").append(module.getCode()).append("`.`id`").append(" and ")
                        .append("`").append(temModule.getCode()).append("`.`is_delete`").append(" = 0 )");
            }
            // 组装引用模型部分
            StringBuilder refBuf = new StringBuilder();
            for (String filedCode : refCodeMap.keySet()) {
                String refModuleId = refCodeMap.get(filedCode).get(FILED_REF_MODULE_ID).toString();
                String ownerModuleId = refCodeMap.get(filedCode).get(FILED_OWNER_MODULE_ID).toString();
                CmdbCode code = (CmdbCode) refCodeMap.get(filedCode).get("code");
                Module refModule = getModuleDetail(refModuleId);
                SimpleModule ownerModule = moduleMapper.getSimpleModuleDetail(ownerModuleId);
                String refAlias = refCodeMap.get(filedCode).get(FILED_REFALIAS).toString();
                refBuf.append("left join ").append(refModule.getModuleCatalog().getCatalogCode())
                        .append(" ").append(refAlias).append(" ").append("on (`").append(ownerModule.getCode()).append("`.`")
                        .append(code.getFiledCode()).append("`=`").append(refAlias).append("`.`id`")
                        .append("and `").append(refAlias).append("`.`is_delete` = '0')");
            }
            //组装完整的SQL
            StringBuilder sqlBuffer = new StringBuilder();
            sqlBuffer.append("select ")
                    .append(filedBuf)
                    .append(StringUtils.isNotEmpty(refFiledBuf.toString()) ? ("," + refFiledBuf.toString()) : "")
                    .append(" from ").append(moduleTableName).append(" `").append(module.getCode()).append("` ");
            if (!module.getId().equals(topModule.getId())) {
                sqlBuffer.append(" inner join ").append(topCatalog.getCatalogCode()).append(" `").append(topModule.getCode()).append("` on (")
                        .append("`").append(topModule.getCode()).append("`.`id`").append(" = ")
                        .append("`").append(module.getCode()).append("`.`id`").append(" and ")
                        .append("`").append(topModule.getCode()).append("`.`is_delete`").append(" = 0 ").append(" and ")
                        .append("`").append(topModule.getCode()).append("`.`module_id`").append("='").append(moduleId).append("') ");
            }
            sqlBuffer.append(joinBuf)
                    .append(refBuf)
                    .append(" where ").append(module.getCode()).append(".`is_delete` = 0 ");
            StringBuilder authSql = new StringBuilder();
            authSql.append("select * from (")
                    .append(sqlBuffer)
                    .append(") t where 1=1 ")
                    .append(Constants.INNER_AUTH_STRING);
            sql = authSql.toString();
        } else {
            Map<String, String> sqlMap = (new ObjectMapper().convertValue(object, new TypeReference<Map<String, String>>(){}));
            sql = sqlMap.get("sql");
        }
//        this.checkSQL(moduleId, sql);
        return sql;
    }

    /**
     * 校验SQL是否包含敏感字符
     * @param moduleId 模型ID
     * @param sql 查询SQL
     */
    public void checkSQL(String moduleId, String sql) {
        try {
            checkSQL(sql);
        } catch (RuntimeException e) {
            throw new CmdbRuntimeException("Query module[" + moduleId + "] SQL is bad. SQL -> " + sql);
        }
    }

    /**
     * 校验SQL是否包含敏感字符
     * @param sql 查询SQL
     */
    public void checkSQL(String sql) {
        // 做一次防注入过滤
        String temSql = sql.toLowerCase();
        String badKeyWords = "'|exec|insert|delete|update|*|%|chr|mid|master|truncate|char|declare|;|-|+";
        String[] badKeys = badKeyWords.split("\\|");
        String[] sqlWords = temSql.split(" ");
        for (String badKey : badKeys) {
            for (String sqlWord : sqlWords) {
                if (badKey.equals(sqlWord)) {
                    throw new CmdbRuntimeException("Query SQL is bad. SQL -> " + sql);
                }
            }
        }
    }

    @Override
    public String getTableNameByModuleId(String moduleId) {
        Module module = moduleMapper.getModuleDetail(moduleId);
        if (module == null) {
            throw new CmdbRuntimeException("根据模型ID:" + moduleId + "未查询到相应模型信息！");
        }
        if (module.getModuleCatalog() == null) {
            throw new CmdbRuntimeException("模型下未查询到分组信息，请检查数据！");
        }
        return module.getModuleCatalog().getCatalogCode();
    }

    @Override
    public List<Module> getChildByCatalogId(String catalogId) {
        return moduleMapper.getChildByCatalogId(catalogId);
    }

    /**
     * 根据模型分组找到对应的模型信息
     * @param catalogId 模型分组ID
     * @return
     */
    @Override
    public Module getModuleDetailByCatalogId(String catalogId) {
        return moduleMapper.getModuleDetailByCatalogId(catalogId);
    }

    @Override
    public SimpleModule getSimpleModuleByCatalogId(String catalogId) {
        return moduleMapper.getSimpleModuleByCatalogId(catalogId);
    }

    @Override
    public List<Module> getModuleByCodeId(String codeId) {
        return moduleMapper.getModuleByCodeId(codeId);
    }

    @Override
    public Module getModuleByInstanceId(String instanceId) {
        return moduleMapper.getModuleByInstanceId(instanceId);
    }

    @Override
    public List<SimpleModule> getRefModuleByModuleId(String moduleId) {
        return moduleMapper.getRefModuleByModuleId(moduleId);
    }

    @Override
    public List<SimpleModule> getModuleByRefModuleId(String moduleId) {
        return moduleMapper.getModuleByRefModuleId(moduleId);
    }

    @Override
    public List<Module> getCurRefModule(String moduleId) {
        return moduleMapper.getCurRefModule(moduleId);
    }

    @Override
    public Map<String, String> getModuleCodeAndCatalogCodeByDeviceType(String deviceType) {
        return moduleMapper.getModuleCodeAndCatalogCodeByDeviceType(deviceType);
    }

    @Override
    public List<Map<String, Object>> getModuleData(Map<String, Object> queryParams, String moduleId, String moduleType) {
        if (StringUtils.isEmpty(moduleId) && StringUtils.isEmpty(moduleType)) {
            throw new CmdbRuntimeException("Parameter moduleId or moduleType is require.");
        }
        Module module;
        if (StringUtils.isNotEmpty(moduleId)) {
            module = getModuleDetail(moduleId);
        } else {
            module = getDefaultModule(moduleType);
        }
        if (module != null) {
            // 解析参数格式
            Locale.setDefault(Locale.ENGLISH);
            if (queryDbType != null && queryDbType.toLowerCase().equals("es")) {
                List<Map<String, Object>> queryList = new LinkedList<>();
                if (queryParams != null) {
                    if (queryParams.containsKey("params")) {
                        Map<String, Object> params = (Map<String, Object>) queryParams.get("params");
                        for (String key : params.keySet()) {
                            Map<String, Object> param = new HashMap<>();
                            param.put("filed", key);
                            param.put("operator", "=");
                            param.put("filed_type", "text");
                            param.put("value", params.get(key));
                            queryList.add(param);
                        }
                    }
                }
                Map<String, Object> esParam = new HashMap<>();
                esParam.put("index", module.getModuleCatalog().getCatalogCode());
                esParam.put("type", module.getCode());
                esParam.put("params", queryList);
                Integer pageSize = 100;
                Integer currentPage = 0;
                List<Map<String, Object>> returnList = new LinkedList<>();
                esParam.put("currentPage", currentPage);
                esParam.put("pageSize", pageSize);
                log.info("Request es params -> {}", esParam);
                com.aspire.mirror.elasticsearch.api.dto.cmdb.Result<Map<String, Object>> temp = esService.list(esParam);
                Integer totalCount = temp.getCount();
                Integer totalPageSize = totalCount % pageSize == 0 ? (totalCount / pageSize) : (totalCount / pageSize + 1);
                while (totalCount > 0 && currentPage <= totalPageSize) {
                    currentPage ++;
                    esParam.put("currentPage", currentPage);
                    esParam.put("pageSize", pageSize);
                    log.info("Request es params -> {}", esParam);
                    com.aspire.mirror.elasticsearch.api.dto.cmdb.Result<Map<String, Object>> temp1 = esService.list(esParam);
                    if (temp1.getData() != null) {
                        returnList.addAll(temp1.getData());
                    }
                }
                return returnList;
            } else {
                StringBuilder queryBuilder = new StringBuilder();
                Map<String, Object> sqlParams = new HashMap<>();
                if (queryParams != null) {
                    if (queryParams.containsKey("params")) {
                        Map<String, Object> params = (Map<String, Object>) queryParams.get("params");
                        for (String key : params.keySet()) {
                            sqlParams.put(key, params.get(key));
                            queryBuilder.append(" and ").append(key).append(" = #{").append(key).append("}");
                        }
                    }
                }
                String querySql = getTempModuleQuerySQL(module.getId()) + queryBuilder.toString();
                CmdbSqlManage cmdbSqlManage = new CmdbSqlManage(querySql, module.getId(), null, Constants.NEED_AUTH);
                List<Map<String, Object>> queryList = jdbcHelper.getQueryList(cmdbSqlManage, null, null, null, sqlParams);
                return queryList;
            }
        }
        return null;
    }

    @Override
    public String getModuleFlagByIdOrModuleType(String moduleId, String moduleType) {
        Module module;
        if (StringUtils.isNotEmpty(moduleId)) {
            module = getModuleDetail(moduleId);
        } else {
            module = getDefaultModule(moduleType);
        }
        if (module == null) {
            throw new CmdbRuntimeException("Can't find module by module_id -> " + moduleId + " module_type -> " + moduleType);
        }
        ConfigDict dict = dictService.getDictByColNameAndDictCode("default_module_flag", module.getId());
        if (dict == null) {
            throw new CmdbRuntimeException("Can't find default_module_flag by module_id -> " + module.getId());
        }
        return dict.getValue();
    }

    @Override
    public List<String> getModuleKeysById(String moduleId) {
        String key = "primaryKey:" + moduleId;
        long startTime = System.currentTimeMillis();
        CmdbConfig cmdbConfig = configService.getConfigByCode(key);
        log.info(">>>>>> 查询主键配置 耗时:{}", System.currentTimeMillis() - startTime);
        startTime = System.currentTimeMillis();
        if (cmdbConfig == null) {
            throw new RuntimeException("模型[" + moduleId + "]未配置主键信息");
//            // 使用顶级模型分组ID再查一下
//            Module module = getModuleDetail(moduleId);
//            CmdbV3ModuleCatalog moduleCatalog = catalogService.getTopCatalog(module.getCatalogId());
//            Module topModule = getModuleDetailByCatalogId(moduleCatalog.getId());
//            cmdbConfig = configService.getConfigByCode("primaryKey:" + topModule.getId());
//            if (cmdbConfig  != null) {
//                String value = cmdbConfig.getConfigValue();
//                return Arrays.asList(value.split(","));
//            }
        }
        return Arrays.asList(cmdbConfig.getConfigValue().split(","));
    }

    @Override
    public Map<String, Object> getParentInfo(String moduleId) {
        return moduleMapper.getParentInfo(moduleId);
    }

    @Override
    public Map<String, Object> getModuleDataByPrimarys(String moduleId, Map<String, Object> primaryData) {
        long startTime = System.currentTimeMillis();
//        List<String> primaryKeys = getModuleKeysById(moduleId);
//        log.info(">>>>>> 查询模型主键 耗时:{}", System.currentTimeMillis() - startTime);
//        startTime = System.currentTimeMillis();
//        if (primaryKeys == null) {
//            throw new CmdbRuntimeException("Can't find module_id[" +  moduleId+ "] primary key config.");
//        }
        String moduleQuerySql = getModuleQuerySQL(moduleId);
        log.info(">>>>>> 查询模型sql 耗时:{}", System.currentTimeMillis() - startTime);
        StringBuilder whereBuilder = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        List<String> primaryKeys = getModulePrimaryKeys(moduleId, primaryData);
        // 校验参数是否存在
        for (String primary : primaryKeys) {
            if (!StringUtils.isNotEmpty(primaryData.get(primary))) {
                throw new CmdbRuntimeException("Primary key [" + primary + "] value can't be empty.");
            }
            params.put(primary, primaryData.get(primary));
            whereBuilder.append(" and `").append(primary).append("`").append(" = #{").append(primary).append("}");
        }
        log.info(">>>>>> 拼接处理唯一查询sql 耗时:{}", System.currentTimeMillis() - startTime);
        startTime = System.currentTimeMillis();
        // 校验SQL敏感字
        CmdbSqlManage cmdbSqlManage = new CmdbSqlManage(moduleQuerySql, null, Constants.UN_NEED_AUTH);
        List<Map<String, Object>> dataList = jdbcHelper.getQueryList(cmdbSqlManage, whereBuilder.toString(), null, null, params);
        log.info(">>>>>> 查询是否存在相同数据 耗时:{}", System.currentTimeMillis() - startTime);
        if (dataList != null && dataList.size() > 0) {
            return dataList.get(0);
        }
        return null;
    }

    @Override
    public List<String> getModulePrimaryKeys(String moduleId, Map<String, Object> instanceData) {
        long startTime = System.currentTimeMillis();
        List<String> primaryKeys = getModuleKeysById(moduleId);
        log.info(">>>>>> 查询模型主键 耗时:{}", System.currentTimeMillis() - startTime);
        if (primaryKeys == null) {
            throw new CmdbRuntimeException("Can't find module_id[" +  moduleId+ "] primary key config.");
        }
        // 校验参数是否存在
        for (String primary : primaryKeys) {
            if (!StringUtils.isNotEmpty(instanceData.get(primary))) {
                throw new CmdbRuntimeException("Primary key [" + primary + "] value can't be empty.");
            }
        }
        return primaryKeys;
    }

    @Override
    public List<Module> getTreeByCatalogIdOrModuleId(String catalogId, String moduleId) {
        List<Module> moduleList = new ArrayList<>();
        if (StringUtils.isNotEmpty(moduleId)) {
            moduleList.add(this.getModuleDetail(moduleId));
            return moduleList;
        }
        if (StringUtils.isNotEmpty(catalogId)) {
            Object object = redisService.get(String.format(Constants.REDIS_MODULE_CATALOG_TREE, catalogId));
            if (object == null) {
                return moduleMapper.getModuleTree(catalogId);
            }
            return (new ObjectMapper().convertValue(object, new TypeReference<List<Module>>(){}));
        }
        return moduleList;
    }

    @Override
    public Map<String, Map<String, String>> getModuleColumns(String moduleId) {
        Object object = redisService.get(String.format(Constants.REDIS_MODULE_COLUMNS, moduleId));
        if (object == null) {
            Module module = this.getModuleDetail(moduleId);
            Map<String, Map<String, String>> columnsMap = new LinkedHashMap<>();
            if (module != null) {
                List<CmdbModuleCodeGroup> groupList = module.getGroupList();
                groupList.forEach((group) -> {
                    List<CmdbCode> codeList = group.getCodeList();
                    codeList.forEach((code) -> {
                        Map<String, String> cacheMap = new HashMap<>();
                        cacheMap.put("code_id", code.getCodeId());
                        cacheMap.put("filed_code", code.getFiledCode());
                        String refName = "", type = "text";
                        if (StringUtils.isNotEmpty(code.getCodeBindSource()) && StringUtils.isNotEmpty(code.getCodeBindSource().getBindSourceType())) {
                            type = "ref";
                            if (("引用模型").equals(code.getCodeBindSource().getBindSourceType())) {
                                CmdbSimpleCode refCode = codeService.getSimpleCodeById(code.getCodeBindSource().getShowModuleCodeId());
                                refName = code.getFiledCode() + "_" + refCode.getFiledCode() + "_name";
                            }
                            if (("数据表").equals(code.getCodeBindSource().getBindSourceType())) {
                                refName = code.getFiledCode() + "_value_name";
                            }
                        }
                        cacheMap.put("type", type);
                        cacheMap.put("ref_name", refName);
                        cacheMap.put("filed_name", code.getFiledName());
                        columnsMap.put(code.getFiledCode(), cacheMap);
                    });
                });
            }
            return columnsMap;
        }
        return (new ObjectMapper().convertValue(object, new TypeReference<Map<String, Map<String, String>>>(){}));
    }

    @Override
    public <T> List<Map<String, T>> getRefModuleDict(String codeId) {
        return codeService.getRefCodeData(codeId);
    }

    /**
     * 获取模型中文字段名称
     * @param moduleType 模型类型
     * @param cnName 中文名称
     * @return
     */
    @Override
    public String getIDByCNName(String moduleType, String cnName) {
        String id = "";
        switch (moduleType) {
            case "idcType":
                id = moduleMapper.getIdcTypeIdByCNName(cnName);
                break;
            case "pod_name":
                id = moduleMapper.getPodIdByCNName(cnName);
                break;
            case "project_name":
                id = moduleMapper.getProjectIdByCNName(cnName);
                break;
            case "bizSystem":
                id = moduleMapper.getBizSystemIdByCNName(cnName);
                break;
            default:
                throw new CmdbRuntimeException("不支持的模型类型[" + moduleType + "]");
        }
        return id;
    }

    // 新增、更新模型相关字段时都会调用
    private void addTableColumn(Module m, List<CmdbCode> oldCodes, List<CmdbCode> codeList) {
        long startTime = System.currentTimeMillis();
        if (m.getModuleCatalog() == null) {
            m.setModuleCatalog(catalogService.getById(m.getCatalogId()));
        }
        String tableName = m.getModuleCatalog().getCatalogCode();
        List<String> columns = tabelService.getColumnListByTableName(tableName);
        if (!tabelService.hasTable(tableName)) {
//            createTable(catalogService.getTopCatalog(m.getCatalogId()).getId(), m);
        }
        // 先从关系表中查出模型下当前所有字段
        List<CmdbCode> addCodes = new ArrayList<>();
        List<CmdbCode> removeCodes = new ArrayList<>();
        for (CmdbCode code : codeList) {
            boolean flag = true;
            for (CmdbCode baseCode : oldCodes) {
                if (baseCode == null) {
                    continue;
                }
                if (code.getFiledCode().equals(baseCode.getFiledCode())) {
                    flag = false;
                    break;
                }
            }
            if (flag && !columns.contains(code.getFiledCode())) {
                addCodes.add(code);
            }
        }
        for (CmdbCode code : oldCodes) {
            if (code == null) {
                continue;
            }
            boolean flag = true;
            for (CmdbCode code1 : codeList) {
                if (code.getFiledCode().equals(code1.getFiledCode())) {
                    flag = false;
                }
            }
            if (flag) {
                removeCodes.add(code);
            }
        }
        // 改变所有引用该模型的主机列表
        List<SimpleModule> refModuleList = getRefModuleByModuleId(m.getId());
        toChangeRefModules(m,refModuleList, addCodes, removeCodes);
        if (addCodes.size() > 0) {
            tabelService.addTableColumn(tableName, addCodes);
        }
        if (removeCodes.size() > 0) {
            tabelService.deleteTableColumn(m, removeCodes);
        }
        toRefreshRedis(refModuleList);
        log.info("------ 从{}表中删除{}个字段，新增{}个字段 消耗时间 {}", tableName, removeCodes.size(), addCodes.size(), System.currentTimeMillis() - startTime);

    }

    private void toChangeRefModules(Module m,List<SimpleModule> refModuleList, List<CmdbCode> addCodes, List<CmdbCode> removeCodes) {
        if (StringUtils.isNotEmpty(m.getId())) { //新增模型
            if (refModuleList != null) {
                long startTime = System.currentTimeMillis();
                for (SimpleModule refModule : refModuleList) {
                    // 处理新增字段
                    List<CmdbV3ModuleCodeSetting> codeSettings = new ArrayList<>();
                    for (CmdbCode addCode : addCodes) {
                        CmdbV3ModuleCodeSetting codeSetting = new CmdbV3ModuleCodeSetting();
                        codeSetting.setId(UUIDUtil.getUUID());
                        codeSetting.setModuleId(refModule.getId());
                        codeSetting.setOwnerModuleId(m.getId());
                        codeSetting.setCodeId(addCode.getCodeId());
                        codeSetting.setGroupId(m.getId());
                        codeSetting.setIsDelete(0);
                        codeSetting.setDisplay(addCode.getCodeSetting().getDisplay()); // 引用模型的显示方式需要继承主类型方式
                        codeSettings.add(codeSetting);
                    }
                    if (codeSettings.size() > 0) {
                        codeSettingMapper.insertByBatch(codeSettings);
                    }
                    // 处理删除字段
                    for (CmdbCode deleteCode : removeCodes) {
                        codeSettingMapper.deleteByModuleAndCodeId(deleteCode.getCodeId(), refModule.getId());
                    }
                }
                log.info("------ 修改模型后处理相关引用模型数据 消耗时间 {}", removeCodes.size(), addCodes.size(), System.currentTimeMillis() - startTime);
            }
        }
    }

    private void toRefreshRedis(List<SimpleModule> refModuleList) {
        for (SimpleModule refModule : refModuleList) {
            redisService.asyncRefresh(Constants.REDIS_TYPE_MODULE, refModule.getId());
        }
    }


    private void createTable (Module module) {
        CmdbV3ModuleCatalog catalog = catalogService.getById(module.getCatalogId());
        if (catalog == null) {
            throw new CmdbRuntimeException("当前模型分组不存在，请查看是否已被删除");
        }
        String tableName = catalog.getCatalogCode();
        log.info("开始创建表 ====> {}", tableName);
        if (!tabelService.hasTable(tableName)) {
            try{
                List<CmdbCode> codeList = new ArrayList<>();
                for (CmdbModuleCodeGroup group : module.getGroupList()) {
                    List<CmdbCode> codes = group.getCodeList();
                    if (codes != null && codes.size() > 0) {
                        CmdbCode code = codes.get(0);
                        // 判断是否是自定义字段
                        if (StringUtils.isNotEmpty(group.getGroupName()) && StringUtils.isEmpty(code.getCodeSetting().getOwnerModuleId())) {
                            codeList.addAll(group.getCodeList());
                        }
                    }

                }
                StringBuilder columnBuffer = new StringBuilder();
                if (codeList.size() > 0) {
                    for (CmdbCode cmdbCode : codeList) {
                        // 把ID字段单独放到下面配置
                        if (cmdbCode.getFiledCode().equalsIgnoreCase("id") || cmdbCode.getFiledCode().equalsIgnoreCase("is_delete")) {
                            continue;
                        }
                        columnBuffer.append("`").append(cmdbCode.getFiledCode()).append("`")
                                .append(" VARCHAR(").append(cmdbCode.getCodeLength() == null ? 200 : cmdbCode.getCodeLength()).append(") ")
                                .append(" NULL COMMENT '").append(cmdbCode.getFiledName()).append("',\n");
                    }
                }
                StringBuilder tableBuffer = new StringBuilder();
                tableBuffer.append("CREATE TABLE `").append(tableName).append("` (").append("\n");
                tableBuffer.append("`id` varchar(40) NOT NULL COMMENT ").append("'资产ID'").append(",").append("\n");
                tableBuffer.append("`is_delete` int(11) NULL COMMENT ").append("'删除标识'").append(",").append("\n");
                tableBuffer.append(columnBuffer);
                tableBuffer.append("PRIMARY KEY (`id`), ").append("\n");
                tableBuffer.append("INDEX `IDX_").append(tableName).append("_id").append("` (`id`) USING BTREE").append("\n");
                tableBuffer.append(") ENGINE=InnoDB ").append("\n");
                tableBuffer.append(" DEFAULT CHARACTER SET = utf8 ").append("\n");
                tableBuffer.append(" COMMENT='").append(module.getName()).append("资产表' ").append("\n");
                log.info("Execute create table [{}].[{}] SQL -> {}", schemaName, tableName, tableBuffer.toString());
                schemaMapper.createTable(tableBuffer.toString().replace("\n", " "));
                log.info("成功创建表 ====> {}", tableName);
            } catch (Exception e) {
                throw new CmdbRuntimeException("模型表" + tableName + "创建失败" + e.getMessage());
            }

        } else {
            throw new CmdbRuntimeException("模型表" + tableName + "已存在,请重新输入模型编码");
        }

    }

    /**
     * 检查哪些字段不需要进行查询
     * @param tableName 需要查询表的编码
     * @param notNeedFiled 不需要查询的字段
     * @return true - 需要去掉的字段
     */
    private boolean queryInstanceListNotNeedFiled(CmdbConfig cmdbConfig,String tableName, String notNeedFiled) {
        // 获取业务资源详情不需要查询的字段,not_need_query_filed4cmdb_instance, create by fanwenhui 20200824
        Map<String, String> configFiledMap = (Map<String, String>) JSONObject.parse(cmdbConfig.getConfigValue());
        if (null == configFiledMap) { // 没有该字段配置
            return false;
        }
        String configField = configFiledMap.get(tableName); // 不需要查询的所有字段
        if (StringUtils.isEmpty(configField)) {
            return false;
        }
        if (!configField.contains(notNeedFiled)) {
            return false;
        }
        return true;
    }

    @Override
    public String getModuleQuerySQL4Tab(String moduleId,List<String> tabParamList,String type) {
        Object object = null;
        if (Constants.MODULE_TAB_LIST.equals(type)) {
            object = redisService.get(String.format(Constants.REDIS_MODULE_TAB_TABLE, moduleId));
        } else if (Constants.MODULE_TAB_EXPORT.equals(type)) {
            object = redisService.get(String.format(Constants.REDIS_MODULE_TAB_EXPORT_TABLE, moduleId));
        }
        String sql;
        if (object == null) {
            Module module = this.getModuleDetail(moduleId);
            // 对code_setting进行分组
            Map<String, List<CmdbV3ModuleCodeSetting>> groupMap = buildModuleCodeSetting(moduleId);
            if (null == groupMap) {
                return "";
            }
            // 获取顶级节点
            CmdbV3ModuleCatalog topCatalog = catalogService.getTopCatalog(module.getCatalogId());
            if (topCatalog == null) {
                log.error(LOG_ERROR_TEMPLATE, module.getId());
                return "";
            }
            // 根据模型分组ID获取模型信息
            Module topModule = getModuleDetailByCatalogId(topCatalog.getId());
            if (topModule == null) {
                log.error(LOG_ERROR_TEMPLATE, module.getId());
                return "";
            }

            // 开始组装SQL
            // 1.1 查询用字段code
            StringBuilder filedBuf = new StringBuilder();
            // 1.2 审核用字段code
            StringBuilder approveFiledBuf = new StringBuilder();
            // 1.3 引用模型字段code
            StringBuilder refFiledBuf = new StringBuilder();
            // 1.4 数据表字段code,eg: 字典表之类的下拉框选值
            StringBuilder tableFiledBuf = new StringBuilder();

            // 2.模型配置项拼装
            // 2.1 识别引用模型配置项
            Map<String, Map<String, Object>> refCodeMap = new HashMap<>();
            // 2.2 识别数据表模型配置项
            Map<String, Map<String, Object>> tableCodeMap = new HashMap<>();

            // 4.拼接列表查询字段code的字符串
            buildFiledListSql(groupMap,filedBuf,approveFiledBuf,refFiledBuf,tableFiledBuf,refCodeMap,tableCodeMap);

            // 5.数据库表拼装
            StringBuilder joinBuf = new StringBuilder();
            // 5.1 拼装模型的基本sql,并记录已经进行join处理的模型ID
            List<String> exitModuleIdList = buildJoinSql(joinBuf,groupMap,module,topModule);
            // 5.2 获取查询条件配置的附属模型的moduleId和码表字段code的对应关系, eg- 自动化主机配置模型（附属模型）
            List<Map<String, String>> noExitModuleList = getSQL4Tab(tabParamList,filedBuf,moduleId,type);
            // 5.3 构建主机模型和主机配置附属模型的JoinSql
            if (!noExitModuleList.isEmpty()) {
                buildJoinSql2(noExitModuleList,module,joinBuf,filedBuf,exitModuleIdList,type);
            }
            // 5.4 组装引用模型JoinSql部分
            StringBuilder refBuf = buildJoinSql3(refCodeMap);
            // 5.5 组织数据表查询JoinSql部分
            StringBuilder tableBuf = buildJoinSql4(tableCodeMap);

            // 6.组装完整的SQL
            StringBuilder sqlBuffer = buildAllSql(module,topModule,filedBuf,joinBuf,topCatalog);
            StringBuilder authSql = buildAllSql2(approveFiledBuf,sqlBuffer);
            sql = "select tt.* ";
            if (StringUtils.isNotEmpty(refFiledBuf.toString())) {
                sql +=  "," + refFiledBuf;
            }
            if (StringUtils.isNotEmpty(tableFiledBuf.toString())) {
                sql += "," + tableFiledBuf;
            }
            sql += " from (" + authSql + ") tt " + refBuf + " " + tableBuf;
        } else {
            Map<String, String> sqlMap = (new ObjectMapper().convertValue(object, new TypeReference<Map<String, String>>(){}));
            sql = sqlMap.get("sql");
        }
        return sql;
    }

    @Override
    public String getModuleQueryCountSQL4Tab(String moduleId,List<String> tabParamList) {
        Object object = redisService.get(String.format(Constants.REDIS_MODULE_COUNT_TAB_TABLE, moduleId));
        String sql;
        if (object == null) {
            Module module = this.getModuleDetail(moduleId);
            String moduleTableName = module.getModuleCatalog().getCatalogCode();
            // 对code_setting进行分组
            Map<String, List<CmdbV3ModuleCodeSetting>> groupMap = buildModuleCodeSetting(moduleId);
            if (null == groupMap) {
                return "";
            }
            // 获取顶级节点
            CmdbV3ModuleCatalog topCatalog = catalogService.getTopCatalog(module.getCatalogId());
            if (topCatalog == null) {
                log.error(LOG_ERROR_TEMPLATE, module.getId());
                return "";
            }
            // 根据模型分组ID获取模型信息
            Module topModule = getModuleDetailByCatalogId(topCatalog.getId());
            if (topModule == null) {
                log.error(LOG_ERROR_TEMPLATE, module.getId());
                return "";
            }
            // 开始组装SQL
            // 1.数据库表拼装
            StringBuilder joinBuf = new StringBuilder();
            // 1.1 拼装模型的基本sql,并记录已经进行join处理的模型ID
            List<String> exitModuleIdList = buildJoinSql(joinBuf,groupMap,module,topModule);
            // 1.2 获取查询条件配置的附属模型的moduleId和码表字段code的对应关系, eg- 自动化主机配置模型（附属模型）
            List<Map<String, String>> noExitModuleList = getSQL4Tab(tabParamList,null,moduleId,Constants.MODULE_TAB_LIST);
            // 1.3 构建主机模型和主机配置附属模型的JoinSql
            if (!noExitModuleList.isEmpty()) {
                // 获取tab查询的主表字段,构建tab查询主表关联sql
                Map<String, String> mainTab = noExitModuleList.get(0);
                Module mainTabModule = this.getModuleDetail(mainTab.get("ownModuleId"));
                if (null != mainTabModule) {
                    buildJoinSql2(noExitModuleList,module,joinBuf,null,exitModuleIdList,null);
                }
            }

            // 2.组装完整的SQL
            StringBuilder sqlBuffer = new StringBuilder();
            sqlBuffer.append("select 1 from ").append(moduleTableName).append(" `").append(module.getCode()).append("` ");
            if (!module.getId().equals(topModule.getId())) {
                sqlBuffer.append(" inner join ").append(topCatalog.getCatalogCode()).append(" `").append(topModule.getCode()).append("` on (")
                        .append("`").append(topModule.getCode()).append("`.`id`").append(" = ")
                        .append("`").append(module.getCode()).append("`.`id`").append(" and ")
                        .append("`").append(topModule.getCode()).append("`.`is_delete`").append(" = 0 ").append(" and ")
                        .append("`").append(topModule.getCode()).append("`.`module_id`").append("='").append(moduleId).append("') ");
            }
            sqlBuffer.append(joinBuf);
            sqlBuffer.append(" where ").append(module.getCode()).append(".`is_delete` = 0 ");
            sqlBuffer.append(Constants.INNER_AUTH_STRING).append(" ")
                    .append(Constants.INNER_WHERE_STRING).append(" ");
            sql = sqlBuffer.toString();
        } else {
            Map<String, String> sqlMap = (new ObjectMapper().convertValue(object, new TypeReference<Map<String, String>>(){}));
            sql = sqlMap.get("sql");
        }
        return sql;
    }

    /**
     * 根据设备类型获取模型信息
     *
     * @param deviceType 设备类型
     * @return
     */
    @Override
    public Map<String, Object> getModuleIdByDeviceType(String deviceType) {
        return moduleMapper.getModuleIdByDeviceType(deviceType);
    }

    /**
     * 生成权限字符串
     *
     * @param moduleId
     * @param parentModuleId
     * @param grantMapper
     * @return
     */
    @Override
    public Map<String, Map<String, String>> grantRightJson(String moduleId, String parentModuleId, Map<String, String> grantMapper) {
        Map<String, Map<String, String>> returnMap = new HashMap<>();
        if (StringUtils.isNotEmpty(moduleId)) {
            returnMap.put(moduleId, grantMapper);
            return returnMap;
        }
        if (StringUtils.isNotEmpty(parentModuleId)) {
            this.forLoopModule(returnMap, grantMapper, moduleId);
        }
        return returnMap;
    }

    private void forLoopModule(Map<String, Map<String, String>> returnMap, Map<String, String> grantMapper, String parentModuleId) {
        List<Map<String, Object>> childList = moduleMapper.getChildModuleListByParentModuleId(parentModuleId);
        if (childList != null && childList.size() > 0) {
            for (Map<String, Object> child : childList) {
                String moduleId = child.get("id").toString();
                returnMap.put(moduleId, grantMapper);
                forLoopModule(returnMap, grantMapper, parentModuleId);
            }
        }
    }

    /**
     * 拼接查询用的字段code字符串
     * @param groupMap 模型分组
     * @param filedBuf 字段code
     * @param approveFiledBuf 审批字段code
     * @param refFiledBuf 引用模型字段code
     * @param tableFiledBuf 数据表模型字段code
     * @param refCodeMap 引用模型配置项
     * @param tableCodeMap 数据表模型配置项
     */
    private void buildFiledListSql(Map<String, List<CmdbV3ModuleCodeSetting>> groupMap,StringBuilder filedBuf,StringBuilder approveFiledBuf,StringBuilder refFiledBuf,StringBuilder tableFiledBuf,Map<String, Map<String, Object>> refCodeMap,Map<String, Map<String, Object>> tableCodeMap) {
        // 查询配置表，将不需要的字段过滤掉
        CmdbConfig cmdbConfig = configService.getConfigByCode("not_need_query_filed4cmdb_instance");
        boolean flag = false;
        for (Map.Entry<String, List<CmdbV3ModuleCodeSetting>> entry : groupMap.entrySet()) {
            String temModuleId = entry.getKey();
            Module temModule = this.getModuleDetail(temModuleId);
            if (temModule == null) {
                continue;
            }
            List<CmdbV3ModuleCodeSetting> temSettingList = entry.getValue();
            for (CmdbV3ModuleCodeSetting setting : temSettingList) {
                CmdbCode queryCode = new CmdbCode();
                queryCode.setCodeId(setting.getCodeId());
                CmdbCode code = codeService.get(queryCode);
                if (code != null) {
                    if ("id".equals(code.getFiledCode().toLowerCase(Locale.ENGLISH))) {
                        if (flag) {
                            continue;
                        }
                        flag = true;
                    }
                    if (null != cmdbConfig) {
                        boolean notNeedFiled = queryInstanceListNotNeedFiled(cmdbConfig, temModule.getCode(), code.getFiledCode());
                        if (!notNeedFiled) {
                            buildFiledSql(filedBuf,approveFiledBuf,temModule,code);
                        }
                    } else {
                        buildFiledSql(filedBuf,approveFiledBuf,temModule,code);
                    }
                    // 其他字段绑定的引用数据处理
                    if (code.getCodeBindSource() != null) {
                        // 处理引用模型的字段code
                        if ("引用模型".equals(code.getCodeBindSource().getBindSourceType())) {
                            buildRefCodeMap(code,setting.getOwnerModuleId(),refCodeMap,refFiledBuf);
                        }
                        // 处理数据表的字段code
                        if ("数据表".equals(code.getCodeBindSource().getBindSourceType())) {
                            buildTableCodeMap(code,tableCodeMap,tableFiledBuf);
                        }
                    }
                }
            }
        }
    }

    /**
     * 构建主机配置信息相关模型(附属模型)和码表的对应关系，module-code
     * @param tabParamList 查询条件参数
     * @param filedBuf 已经拼接的字段code
     * @param moduleId 列表查询主要模型ID，eg-主机资源模型ID
     */
    private List<Map<String, String>> getSQL4Tab(List<String> tabParamList,StringBuilder filedBuf,String moduleId,String type) {
        // 判断查询条件里面的参数在查询字段中是否存在
        Map<String,Object> noExitMap = new HashMap<>();
        List<String> tabTempList = new ArrayList<>();
        noExitMap.put("moduleId",moduleId);
        if (Constants.MODULE_TAB_LIST.equals(type)) {
            if (tabParamList.isEmpty()) {
                return new ArrayList<>();
            }
            if (!tabParamList.contains("auto2_ip")) {
                tabParamList.add("auto2_ip");
            }
            tabTempList = tabParamList;
        } else if (Constants.MODULE_TAB_EXPORT.equals(type)) {
            // 获取主机配置的所有字段
            List<String> codes = Lists.newArrayList("automate");
            noExitMap.put("codeList", codes);
            List<Map<String, String>> tempList = codeService.getCodeIdByNameAndModuleId(noExitMap);
            String filedCode = tempList.get(0).get("filedCode");
            if (!tabParamList.isEmpty()) {
                for (String s : tabParamList) {
                    if (!filedCode.contains(s)) {
                        tabTempList.add(s);
                    }
                }
            }
            tabTempList.addAll(Arrays.asList(filedCode.split(",")));
        }

        List<String> noExitList = new ArrayList<>(tabTempList.size());
        if (null != filedBuf) {
            String noExitStr = filedBuf.toString();
            for (String tabParam : tabTempList) {
                if (!noExitStr.contains(tabParam)) {
                    noExitList.add(tabParam);
                }
            }
        } else {
            noExitList = tabTempList;
        }
        noExitMap.put("list",noExitList);

        // 通过不存在的字段名称和顶层模型ID（如主机资源）查询对应的码表 cmdb_code 的id
        // 然后再通过 codeid 查询 cmdb_v3_module_code_setting 获取配置了这个字段的具体模型的模型ID
        List<Map<String, String>> tempList = codeService.getCodeIdByNameAndModuleId(noExitMap);
        List<Map<String, String>> tabList = new LinkedList<>();
        for (Map<String, String> tab : tempList) {
            String filedCode = tab.get("filedCode");
            if (StringUtils.isNotEmpty(filedCode) && filedCode.contains("auto2_ip")) {
                tabList.add(0,tab);
            } else {
                tabList.add(tab);
            }
        }
        return tabList;
    }

    /**
     * 构建主机资源自动化配置主表关联sql
     * @param mainTabModule 自动化配置主模型
     * @param module 主机资源模型
     * @param mainTabFiledCodes 自动化配置主表字段
     * @param joinBuf join关联sql
     * @param filedBuf join查询的字段
     */
    private void buildSQL4MainTab(Module mainTabModule,Module module,String mainTabFiledCodes,StringBuilder joinBuf,StringBuilder filedBuf,String type) {
        String notNeedColumn = "id,module_id,insert_person,insert_time,update_person,update_time,auto2_instanceid";
        String mainTabTable = mainTabModule.getModuleCatalog().getCatalogCode();
        if ("export".equals(type)) {
            joinBuf.append(" inner join ");
        } else {
            joinBuf.append(" left join ");
        }
        joinBuf.append(mainTabTable).append(" `").append(mainTabModule.getCode()).append("` on (")
                .append("`").append(mainTabModule.getCode()).append("`.`auto2_ip`").append(" = ")
                .append("`").append(module.getCode()).append("`.`ip`").append(" and ")
                .append("`").append(mainTabModule.getCode()).append("`.`is_delete`").append(" = 0 )");

        if (null != filedBuf) {
            String[] mainTabSplit = mainTabFiledCodes.split(",");
            for (String filedCode : mainTabSplit) {
                if (!notNeedColumn.contains(filedCode)) {
                    if (StringUtils.isNotEmpty(filedBuf.toString())) {
                        filedBuf.append(",");
                    }
                    filedBuf.append(String.format("`%s`.`%s`", mainTabModule.getCode(),filedCode));
                }
            }
        }

    }

    /**
     * 构建主机资源自动化配置子表关联sql
     * @param temModule 自动化配置子模型
     * @param mainTabModule 自动化配置主模型
     * @param filedCodes 自动化配置子表查询字段
     * @param joinBuf join关联sql
     * @param filedBuf join查询的字段
     */
    private void buildSQL4SubTab(Module temModule,Module mainTabModule,String filedCodes,StringBuilder joinBuf,StringBuilder filedBuf) {
        String notNeedColumn = "id,module_id,insert_person,insert_time,update_person,update_time,auto2_instanceid";
        String temModuleTable = temModule.getModuleCatalog().getCatalogCode();
        joinBuf.append(" left join ").append(temModuleTable).append(" `").append(temModule.getCode()).append("` on (")
                .append("`").append(temModule.getCode()).append("`.`auto2_instanceid`").append(" = ")
                .append("`").append(mainTabModule.getCode()).append("`.`id`").append(" and ")
                .append("`").append(temModule.getCode()).append("`.`is_delete`").append(" = 0 )");

        if (null != filedBuf) {
            String[] split = filedCodes.split(",");
            for (String filedCode : split) {
                if (!notNeedColumn.contains(filedCode)) {
                    if (StringUtils.isNotEmpty(filedBuf.toString())) {
                        filedBuf.append(",");
                    }
                    filedBuf.append(String.format("`%s`.`%s`", temModule.getCode(),filedCode));
                }

            }
        }
    }

    /**
     * 构建模型和码表字段的分组数据
     * @param moduleId 查询的模型列表ID
     */
    private Map<String, List<CmdbV3ModuleCodeSetting>> buildModuleCodeSetting(String moduleId) {
        CmdbV3ModuleCodeSetting codeSetting = new CmdbV3ModuleCodeSetting();
        codeSetting.setModuleId(moduleId);
        List<CmdbV3ModuleCodeSetting> codeSettingList = codeSettingMapper.listByEntity(codeSetting);
        if (codeSettingList == null || codeSettingList.isEmpty()) {
            return null;
        }

        Map<String, List<CmdbV3ModuleCodeSetting>> groupMap = new HashMap<>();
        for (CmdbV3ModuleCodeSetting setting : codeSettingList) {
            // 如果是设置为不显示的字段, 则跳过更新
            if (setting.getDisplay() == 1) {
                continue;
            }
            String ownerModuleId = setting.getOwnerModuleId();
            if (StringUtils.isEmpty(ownerModuleId)) {
                ownerModuleId = setting.getModuleId();
            }
            List<CmdbV3ModuleCodeSetting> list = new LinkedList<>();
            if (groupMap.containsKey(ownerModuleId)) {
                list = groupMap.get(ownerModuleId);
            }
            list.add(setting);
            groupMap.put(ownerModuleId, list);
        }
        return groupMap;
    }

    /**
     * 拼装列表查询字段code
     * @param filedBuf 字段code
     * @param approveFiledBuf 审批字段code
     * @param temModule 模型实体
     * @param code 模型字段实体
     */
    private void buildFiledSql(StringBuilder filedBuf,StringBuilder approveFiledBuf,Module temModule,CmdbCode code) {
        String queryFiledTemplate = "`%s`.`%s`";
        if (StringUtils.isNotEmpty(filedBuf.toString())) {
            filedBuf.append(",");
            approveFiledBuf.append(",");
        }
        filedBuf.append(String.format(queryFiledTemplate, temModule.getCode(), code.getFiledCode()));
        approveFiledBuf.append("#{");
        approveFiledBuf.append(code.getFiledCode());
        approveFiledBuf.append("} ");
        approveFiledBuf.append("`").append(code.getFiledCode()).append("`");
    }

    /**
     * 拼装引用模型字段code
     * @param code 模型字段实体
     * @param ownerModuleId 配置项归属模型ID
     * @param refCodeMap 引用模型配置项
     * @param refFiledBuf 引用模型字段code
     */
    private void buildRefCodeMap(CmdbCode code,String ownerModuleId,Map<String, Map<String, Object>> refCodeMap,StringBuilder refFiledBuf) {
        String refModuleTableTemplate = "ref_tb_%s";
        String queryRefFiledTemplate = "`" + refModuleTableTemplate + "`.`%s` `%s_name`";
        Map<String, Object> refMap = new HashMap<>();
        refMap.put(FILED_REF_MODULE_ID, code.getCodeBindSource().getRefModuleId());
        refMap.put("code", code);
        refMap.put(FILED_OWNER_MODULE_ID, ownerModuleId);
        refMap.put(FILED_REFALIAS, String.format(refModuleTableTemplate, code.getFiledCode()));
        refCodeMap.put(code.getFiledCode(), refMap);
        String refCodeId = code.getCodeBindSource().getShowModuleCodeId();
        CmdbSimpleCode refCode = codeService.getSimpleCodeById(refCodeId);
        if (StringUtils.isNotEmpty(refFiledBuf.toString()) && null != refCode) {
            refFiledBuf.append(",");
        }
        if(null != refCode) {
            refFiledBuf.append(String.format(queryRefFiledTemplate, code.getFiledCode(), refCode.getFiledCode(), code.getFiledCode() + "_" + refCode.getFiledCode()));
        } else {
            log.info("refCodeId:{}",refCodeId);
        }
    }

    /**
     * 拼装数据表模型字段code
     * @param code 模型字段实体
     * @param tableCodeMap 数据表模型配置项
     * @param tableFiledBuf 数据表模型字段code
     */
    private void buildTableCodeMap(CmdbCode code,Map<String, Map<String, Object>> tableCodeMap,StringBuilder tableFiledBuf) {
        String tableTableTemplate = "table_%s";
        String queryTableFiledTemplate = "`" + tableTableTemplate + "`.`value` `%s_value_name`";
        Map<String, Object> tableMap = new HashMap<>();
        tableMap.put("code", code);
        tableMap.put(FILED_REFALIAS, String.format(tableTableTemplate, code.getFiledCode()));
        tableMap.put("table_sql", code.getCodeBindSource().getTableSql());
        tableCodeMap.put(code.getFiledCode(), tableMap);
        if (StringUtils.isNotEmpty(tableFiledBuf.toString())) {
            tableFiledBuf.append(",");
        }
        tableFiledBuf.append(String.format(queryTableFiledTemplate, code.getFiledCode(), code.getFiledCode()));
    }

    /**
     * 构建需要和主模型数据表表进行join关联的表
     * @param joinBuf joinSql
     * @param groupMap 模型分组code
     * @param module 列表查询的主模型
     * @param topModule 顶级模型
     */
    private List<String> buildJoinSql(StringBuilder joinBuf,Map<String, List<CmdbV3ModuleCodeSetting>> groupMap, Module module, Module topModule) {
        // 保存已经进行join处理的模型ID
        List<String> exitModuleIdList = new ArrayList<>();
        exitModuleIdList.add(module.getId());
        exitModuleIdList.add(topModule.getId());
        for (String temModuleId : groupMap.keySet()) {
            // 自身模型或定义模型信息, 过滤
            if (temModuleId.equals(module.getId()) || temModuleId.equals(topModule.getId())) {
                continue;
            }
            Module temModule = this.getModuleDetail(temModuleId);
            if (temModule == null) {
                continue;
            }
            exitModuleIdList.add(temModuleId);
            String temModuleTable = temModule.getModuleCatalog().getCatalogCode();
            joinBuf.append(" left join ").append(temModuleTable).append(" `").append(temModule.getCode()).append("` on (")
                    .append("`").append(temModule.getCode()).append("`.`id`").append(" = ")
                    .append("`").append(module.getCode()).append("`.`id`").append(" and ")
                    .append("`").append(temModule.getCode()).append("`.`is_delete`").append(" = 0 )");
        }
        return exitModuleIdList;
    }

    /**
     * 拼装Tab管理的附属模型和列表查询主模型的JoinSql
     * @param noExitModuleList 未进行join关联的附属模型
     * @param module 模型实体
     * @param joinBuf JoinSql
     * @param filedBuf 字段codeSql
     * @param exitModuleIdList 已经进行join关联的模型ID
     */
    private void buildJoinSql2(List<Map<String, String>> noExitModuleList,Module module,StringBuilder joinBuf,StringBuilder filedBuf,List<String> exitModuleIdList,String type) {
        // 获取tab查询的主表字段,构建tab查询主表关联sql
        Map<String, String> mainTab = noExitModuleList.get(0);
        Module mainTabModule = this.getModuleDetail(mainTab.get("ownModuleId"));
        if (null != mainTabModule) {
            buildSQL4MainTab(mainTabModule,module,mainTab.get("filedCode"),joinBuf,filedBuf,type);
            // 获取tab查询的子表字段,构建tab查询子表关联sql
            noExitModuleList.remove(0);
            for (Map<String,String> noExitModuleMap : noExitModuleList) {
                String noExitModuleId = noExitModuleMap.get("ownModuleId");
                // 如果该模型已经进行过join处理，则过滤掉，避免重复join
                if (exitModuleIdList.contains(noExitModuleId)) {
                    continue;
                }
                Module temModule = this.getModuleDetail(noExitModuleId);
                if (temModule == null) {
                    continue;
                }
                buildSQL4SubTab(temModule,mainTabModule,noExitModuleMap.get("filedCode"),joinBuf,filedBuf);
            }
        }
    }

    /**
     * 拼装引用模型JoinSql
     * @param refCodeMap 引用模型配置项
     */
    private StringBuilder buildJoinSql3(Map<String, Map<String, Object>> refCodeMap) {
        StringBuilder refBuf = new StringBuilder();
        for (String filedCode : refCodeMap.keySet()) {
            String refModuleId = refCodeMap.get(filedCode).get(FILED_REF_MODULE_ID).toString();
            CmdbCode code = (CmdbCode) refCodeMap.get(filedCode).get("code");
            Module refModule = getModuleDetail(refModuleId);
            String refAlias = refCodeMap.get(filedCode).get(FILED_REFALIAS).toString();
            refBuf.append("left join ").append(refModule.getModuleCatalog().getCatalogCode())
                    .append(" ").append(refAlias).append(" ").append("on (`tt`.`")
                    .append(code.getFiledCode()).append("`=`").append(refAlias).append("`.`id`")
                    .append("and `").append(refAlias).append("`.`is_delete` = '0')");
        }
        return refBuf;
    }

    /**
     * 拼装数据表模型JoinSql
     * @param tableCodeMap 数据表模型配置项
     */
    private StringBuilder buildJoinSql4(Map<String, Map<String, Object>> tableCodeMap) {
        StringBuilder tableBuf = new StringBuilder();
        for (String filedCode : tableCodeMap.keySet()) {
            CmdbCode code = (CmdbCode) tableCodeMap.get(filedCode).get("code");
            String tableAlias = tableCodeMap.get(filedCode).get(FILED_REFALIAS).toString();
            String tableSql = tableCodeMap.get(filedCode).get("table_sql").toString();
            tableBuf.append("left join ").append("(").append(tableSql).append(")")
                    .append(" ").append(tableAlias).append(" ").append("on (`tt`.`")
                    .append(code.getFiledCode()).append("`=`").append(tableAlias).append("`.`id`)");
        }
        return tableBuf;
    }

    /**
     * 构建完整的查询Sql1
     * @param module 查询sql的主模型实体
     * @param topModule 顶级分组模型实体
     * @param filedBuf 字段codeSql
     * @param joinBuf JoinSql
     * @param topCatalog 顶级节点
     */
    private StringBuilder buildAllSql(Module module,Module topModule,StringBuilder filedBuf,StringBuilder joinBuf,CmdbV3ModuleCatalog topCatalog) {
        StringBuilder sqlBuffer = new StringBuilder();
        String moduleId = module.getId();
        String moduleTableName = module.getModuleCatalog().getCatalogCode();
        sqlBuffer.append(" <if test=\"customerType == null || customerType == ''\" >")
                .append("select ")
                .append(filedBuf)
                .append(" from ").append(moduleTableName).append(" `").append(module.getCode()).append("` ");
        if (!module.getId().equals(topModule.getId())) {
            sqlBuffer.append(" inner join ").append(topCatalog.getCatalogCode()).append(" `").append(topModule.getCode()).append("` on (")
                    .append("`").append(topModule.getCode()).append("`.`id`").append(" = ")
                    .append("`").append(module.getCode()).append("`.`id`").append(" and ")
                    .append("`").append(topModule.getCode()).append("`.`is_delete`").append(" = 0 ").append(" and ")
                    .append("`").append(topModule.getCode()).append("`.`module_id`").append("='").append(moduleId).append("') ");
        }
        sqlBuffer.append(joinBuf);
        sqlBuffer.append(" where ").append(module.getCode()).append(".`is_delete` = 0 ");
        return sqlBuffer;
    }

    /**
     * 构建完整的查询Sql2
     * @param approveFiledBuf 审批字段codeSql
     * @param sqlBuffer 完整查询Sql1
     */
    private StringBuilder buildAllSql2(StringBuilder approveFiledBuf,StringBuilder sqlBuffer) {
        StringBuilder authSql = new StringBuilder();
        String customerSQL = "select " + approveFiledBuf;
        authSql.append("select * from (")
                .append(sqlBuffer)
                .append("</if>")
                .append(" <if test=\"customerType != null and customerType == 'approve'\" >")
                .append(customerSQL)
                .append("</if>")
                .append(") t where 1=1 ")
                // 增加内置权限条件占位符
                .append(Constants.INNER_AUTH_STRING).append(" ")
                // 增加内置WHERE条件占位符
                .append(Constants.INNER_WHERE_STRING).append(" ")
                // 增加内置SORT占位符
                .append(Constants.INNER_ORDER_STRING).append(" ")
                // 增加内置LIMIT占位符
                .append(Constants.INNER_LIMIT_STRING);
        return authSql;
    }

}