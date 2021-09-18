package com.aspire.ums.cmdb.v2.process.instance;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;

import com.aspire.ums.cmdb.code.payload.CmdbCode;
import com.aspire.ums.cmdb.code.payload.CmdbV3CodeCascade;
import com.aspire.ums.cmdb.common.Constants;
import com.aspire.ums.cmdb.dict.payload.ConfigDict;
import com.aspire.ums.cmdb.dict.service.ConfigDictService;
import com.aspire.ums.cmdb.module.payload.Module;
import com.aspire.ums.cmdb.util.SpringUtils;
import com.aspire.ums.cmdb.v2.code.service.ICmdbCodeService;
import com.aspire.ums.cmdb.v2.code.service.ICmdbCodeValidateService;
import com.aspire.ums.cmdb.v2.instance.service.ICmdbInstanceService;
import com.aspire.ums.cmdb.v2.module.service.ModuleService;
import com.aspire.ums.cmdb.v2.process.ImportFactory;
import com.aspire.ums.cmdb.v3.code.service.ICmdbV3CodeCascadeService;
import com.aspire.ums.cmdb.v3.config.constant.CmdbConfigConst;
import com.aspire.ums.cmdb.v3.config.payload.CmdbConfig;
import com.aspire.ums.cmdb.v3.config.service.ICmdbConfigService;
import com.aspire.ums.cmdb.v3.module.payload.CmdbV3ModuleCatalog;
import com.aspire.ums.cmdb.v3.module.service.ICmdbV3ModuleCatalogService;
import com.aspire.ums.cmdb.v3.module.service.ICmdbV3ModuleCodeSettingService;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司 FileName: InstanceImportProcess Author: zhu.juwang Date: 2019/6/10 19:33 Description:
 * ${DESCRIPTION} History: <author> <time> <version> <desc> 作者姓名 修改时间 版本号 描述
 */
@Data
@Slf4j
@NoArgsConstructor
public class InstanceImportFactory extends ImportFactory {

    private ICmdbInstanceService instanceService;

    private ICmdbCodeService codeService;

    private ICmdbCodeValidateService validateService;

    private ConfigDictService configDictService;

    private ModuleService moduleService;

    private ICmdbV3ModuleCatalogService catalogService;

    private ICmdbV3ModuleCodeSettingService codeSettingService;

    private ICmdbV3CodeCascadeService codeCascadeService;

    private ICmdbConfigService cmdbConfigService;

    // 缓存表格title与码表字段的对应关系
    private final Map<String, List<CmdbCode>> cacheModuleCodeMap = new HashMap<>();

    // 缓存码表下拉框可选值列表
    private final Map<String, List<Map<String, String>>> cacheSelectValues = new HashMap<>();

    // 缓存码表级联框可选值列表
    private final Map<String, List<Map<String, String>>> cacheCascaderValuesMap = new HashMap<>();

    // 缓存获取的模型类型 {"module_name": {moduleInfo}}
    private final Map<String, Module> cacheModuleMap = new HashMap<>();

    // 缓存下拉类型转化ID的值
    private final Map<String,String> selectCacheMap = new HashMap<>();

    // 缓存获取的模型主键 {"moduleId": [key1, key2]}
    private final Map<String, List<String>> cachePrimaryKeyMap = new HashMap<>();

    private static boolean initValidPass = false;

    private static String initValidErrorMsg = "";

    private static String moduleFlag = "";

    private List<Module> catalogModuleList = null;

    private Module defaultModule; // 默认的类型

    private CmdbV3ModuleCatalog topCatalog = null; // 获取该模型分类 用来判断存放模型的字段

    @Override
    public void initSpringBean() {
        if (instanceService == null) {
            instanceService = SpringUtils.getBean(ICmdbInstanceService.class);
        }
        if (codeService == null) {
            codeService = SpringUtils.getBean(ICmdbCodeService.class);
        }
        if (validateService == null) {
            validateService = SpringUtils.getBean(ICmdbCodeValidateService.class);
        }
        if (configDictService == null) {
            configDictService = SpringUtils.getBean(ConfigDictService.class);
        }
        if (catalogService == null) {
            catalogService = SpringUtils.getBean(ICmdbV3ModuleCatalogService.class);
        }
        if (codeSettingService == null) {
            codeSettingService = SpringUtils.getBean(ICmdbV3ModuleCodeSettingService.class);
        }
        if (moduleService == null) {
            moduleService = SpringUtils.getBean(ModuleService.class);
        }
        if (codeCascadeService == null) {
            codeCascadeService = SpringUtils.getBean(ICmdbV3CodeCascadeService.class);
        }
        if (cmdbConfigService == null) {
            cmdbConfigService = SpringUtils.getBean(ICmdbConfigService.class);
        }
    }

    @Override
    public void valid() {
        Map<String, Object> importData = super.getProcessImportData(super.getProcessId());
        String moduleId = null, moduleType = null;
        if (super.getProcessParams().containsKey("moduleId")
                && StringUtils.isNotEmpty(super.getProcessParams().get("moduleId"))
                && !super.getProcessParams().get("moduleId").equals("undefined")
                && !super.getProcessParams().get("moduleId").equals("null")) {
            moduleId = super.getProcessParams().get("moduleId");
        } else if (importData.containsKey("moduleType")) {
            moduleType = importData.get("moduleType").toString();
        }
        /**
         * todo 修改逻辑:
         * 1. 获取module_id, 无module_id时, 不允许导入
         * 2. 获取module_id对应的cmdb_config配置, 判断当前模型有无特殊列标识用来使用新模型导入
         */
        long startDate = new Date().getTime();
        try {
            if (StringUtils.isNotEmpty(moduleId)) {
                defaultModule = getModuleService().getModuleDetail(moduleId);
                initValidPass = true;
                log.info("导入消耗: 1. {}", (new Date().getTime() - startDate));
                startDate = new Date().getTime();
            } else {
                // 如果不是按照模型导入, 则需要明确导入的数据中, 哪一列是用作获取模型信息的
                defaultModule = getModuleService().getDefaultModule(moduleType);
                log.info("导入消耗: 2. {}", (new Date().getTime() - startDate));
                startDate = new Date().getTime();
                if (defaultModule != null) {
                    try {
                        ConfigDict dict = getConfigDictService().getDictByColNameAndDictCode("default_module_flag", defaultModule.getId());
                        if (dict == null) {
                            throw new RuntimeException("Can't find default_module_flag by module_id -> " + defaultModule.getId());
                        }
                        moduleFlag = dict.getValue();
                    } catch (Exception e) {
                        log.error("获取模型[" + defaultModule.getId() + "]default_module_flag失败.");
                        initValidErrorMsg = e.getMessage();
                    }
                }
            }
            if (defaultModule == null || moduleFlag == null) {
                initValidPass = false;
                initValidErrorMsg = "获取模型失败. 未找到模型[" + (StringUtils.isEmpty(moduleId) ? moduleType : moduleId) + "]";
            } else {
                initValidPass = true;
            }
            if (defaultModule != null) {
                topCatalog = getCatalogService().getTopCatalog(defaultModule.getCatalogId());
                log.info("导入消耗: 3. {}", (new Date().getTime() - startDate));
                startDate = new Date().getTime();
                if (topCatalog == null) {
                    initValidPass = false;
                    initValidErrorMsg = "模型结构错误. 未找到模型[" + defaultModule.getId() + "]顶级分类.";
                } else {
                    catalogModuleList = getModuleService().getChildByCatalogId(topCatalog.getId());
                    log.info("导入消耗: 4. {}", (new Date().getTime() - startDate));
                    startDate = new Date().getTime();
                    if (catalogModuleList == null || catalogModuleList.size() == 0) {
                        initValidPass = false;
                        initValidErrorMsg = "定义模型分组[" + topCatalog.getCatalogName() + "]下无模型列表.";
                    }
                    this.fillModuleMap(catalogModuleList);
                    log.info("导入消耗: 5. {}", (new Date().getTime() - startDate));
                    startDate = new Date().getTime();
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            initValidPass = false;
            initValidErrorMsg = e.getMessage();
        }
    }

    /**
     * 填充模型缓存
     *
     * @param moduleList
     *            模型列表
     */
    private void fillModuleMap(List<Module> moduleList) {
        if (moduleList != null && moduleList.size() > 0) {
            for (Module module : moduleList) {
                if (!cacheModuleMap.containsKey(module.getId())) {
                    cacheModuleMap.put(module.getName(), module);
                }
                if (module.getChildModules() != null) {
                    fillModuleMap(module.getChildModules());
                }
            }
        }
    }

    @Override
    public void execute(Map<String, String> dataMap) {
        if (!initValidPass) {
            throw new RuntimeException(initValidErrorMsg);
        }
        /**
         * todo 修改逻辑:
         * 1. 根据valid方法结果, 判断是否有使用新列作为模型列,  如果有列但无列值, 则抛出异常
         * 2. 根据列值匹配redis模型, 获取模型信息, 匹配不到抛出异常
         */
        long startDate = new Date().getTime();
        long oneStartDate = new Date().getTime();
        Module selfModule;
        if (super.getProcessParams().containsKey("moduleId") && StringUtils.isNotEmpty(super.getProcessParams().get("moduleId"))
                && !super.getProcessParams().get("moduleId").equals("undefined")
                && !super.getProcessParams().get("moduleId").equals("null")) {
            selfModule = getModuleService().getModuleDetail(super.getProcessParams().get("moduleId"));
            log.info("导入消耗: 6. {}", (new Date().getTime() - startDate));
            startDate = new Date().getTime();
            if (selfModule == null) {
                throw new RuntimeException("未匹配到[" + super.getProcessParams().get("moduleId") + "]模型.");
            }
        } else {
            // 获取导入当前行的模型信息
            String moduleName = dataMap.get(getRealMapKey(moduleFlag));
            if (StringUtils.isEmpty(moduleName)) {
                throw new RuntimeException("列[" + getRealMapKey(moduleFlag) + "]不能为空.");
            }
            selfModule = getImportModule(moduleName);
            log.info("导入消耗: 7. {}", (new Date().getTime() - startDate));
            startDate = new Date().getTime();
            if (selfModule == null) {
                throw new RuntimeException("未匹配到[" + moduleName + "]模型.");
            }
        }
        // 验证主键列表
        List<String> moduleKeys = getImportModulePrimaryKeys(selfModule);
        log.info("Get module primary keys: {}", moduleKeys);
        // todo 3. getModuleCodeList 更换成redis读取
        // 验证主键值是否存在
        List<CmdbCode> codeList = getModuleCodeList(selfModule);
        log.info("导入消耗: 8. {}", (new Date().getTime() - startDate));
        startDate = new Date().getTime();
        Map<String, Object> realKeyMap = new HashMap<>();
        // 验证是否有多余的列, 存在多余列是不允许更新
        Map<String, Object> instanceData = new HashMap<>(); // 所有列的值
        // 主键列的值
        List<Map<String, Object>> primaryQueryList = new ArrayList<>();
        for (String filedName : dataMap.keySet()) {
            CmdbCode cmdbCode = null;
            for (CmdbCode code : codeList) {
                if (filedName != null) {
                    if (super.getRealMapKey(filedName).equals(code.getFiledName())) {
                        cmdbCode = code;
                        break;
                    }
                }
            }
            /*
             * zhujuwang modify 2019.10.16 中移信息张鹏提出需求, 导入excel中遇到空列直接跳过处理. 如若遇到需要将某一列的值修改为空. 则需要通过界面进行单个/批量修改操作. 由于需求特殊,
             * 若其他项目中用到改功能需要修改. 请先与我联系. 直接修改会引发现网问题
             */
            // 如果未匹配到码表字段 且 改列的值不为空 则提示导入失败错误
            if (cmdbCode == null) {
                if (StringUtils.isNotEmpty(dataMap.get(filedName))) {
                    throw new RuntimeException("模型[" + selfModule.getName() + "]中未找到配置项[" + super.getRealMapKey(filedName) + "]");
                }
                continue;
            }
            // 增加字段验证
            this.validCodeValue(cmdbCode, dataMap.get(filedName));
            // 增加实例数据值
            if (StringUtils.isNotEmpty(dataMap.get(filedName))) {
                // 转化存储值为ID
                startDate = new Date().getTime();
                String convertValue = this.convertValueToID(cmdbCode, instanceData, dataMap, filedName);
                log.info("导入消耗: 9. 转化ID {} {}", cmdbCode.getFiledCode(), (new Date().getTime() - startDate));
                startDate = new Date().getTime();
                instanceData.put(cmdbCode.getFiledCode(), convertValue);
            }
            // 处理主键
            for (String primaryKey : moduleKeys) {
                if (cmdbCode.getFiledCode().equals(primaryKey)) {
                    log.info("Get module primary key {} in value {}.", primaryKey, instanceData);
                    // 主键值为空
                    if (!StringUtils.isNotEmpty(dataMap.get(filedName))) {
                        throw new RuntimeException("主键[" + cmdbCode.getFiledCode() + "]字段值不能为空.");
                    }
//                    moduleKeysMap.put(cmdbCode.getFiledCode(), instanceData.get(cmdbCode.getFiledCode()));
                    Map<String, Object> moduleKeysMap = new HashMap<>();
                    moduleKeysMap.put("filed", cmdbCode.getFiledCode());
                    moduleKeysMap.put("require", true);
                    moduleKeysMap.put("value", instanceData.get(cmdbCode.getFiledCode()));
                    moduleKeysMap.put("operator", "=");
                    moduleKeysMap.put("filed_type", cmdbCode.getControlType().getControlCode());
                    primaryQueryList.add(moduleKeysMap);

                }
            }
            realKeyMap.put(super.getRealMapKey(filedName), dataMap.get(filedName));
        }
        // 基础校验结束 开始解析
        // 补充模型ID字段
        instanceData.put("module_id", selfModule.getId());
        // 拼装查询条件
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("query_module_id", selfModule.getId());
        queryParams.put("index", selfModule.getModuleCatalog().getCatalogCode());
        queryParams.put("type", selfModule.getCode());
        queryParams.put("params", primaryQueryList);
        startDate = new Date().getTime();
        Map<String, Object> esInstanceData = getInstanceService().getInstanceByPrimaryKey(queryParams);
        log.info("导入消耗: 10. {}", (new Date().getTime() - startDate));
        startDate = new Date().getTime();
        boolean isUpdate = MapUtils.isNotEmpty(esInstanceData);
        String operatorUser = StringUtils.isEmpty(getProcessParams().get("operatorUser")) ? "系统管理员" : getProcessParams().get(
                "operatorUser");
        if (isUpdate) {
            if (!esInstanceData.containsKey("id")) {
                throw new RuntimeException("Get result data missing require property [id].");
            }
            getInstanceService().updateInstance(esInstanceData.get("id").toString(), operatorUser, instanceData, "批量导入");
            log.info("导入消耗: 11. {}", (new Date().getTime() - startDate));
        } else {
            CmdbConfig canNotImportInsertModuleConfig = getCmdbConfigService().getConfigByCode(
                    CmdbConfigConst.CAN_NOT_IMPORT_INSERT_MODULE);
            if (canNotImportInsertModuleConfig != null && StringUtils.isNotEmpty(canNotImportInsertModuleConfig.getConfigValue())) {
                if (Arrays.asList(canNotImportInsertModuleConfig.getConfigValue().split(",")).contains(selfModule.getId())) {
                    throw new RuntimeException("该模型不允许导入新增.");
                }
            }
            // 如果是新增状态, 则需要处理默认值
            for (CmdbCode code : codeList) {
                if (realKeyMap.get(code.getFiledName()) == null || ("").equals(realKeyMap.get(code.getFiledName()))) {
                    if (StringUtils.isNotEmpty(code.getDefaultValue())) {
                        instanceData.put(code.getFiledCode(), code.getDefaultValue());
                    }
                }
            }
            log.info("导入消耗: 12. {}", (new Date().getTime() - startDate));
            startDate = new Date().getTime();
            getInstanceService().addInstance(operatorUser, instanceData, "批量导入");
            log.info("导入消耗: 13. {}", (new Date().getTime() - startDate));
        }
        log.info("导入消耗: 一条数据耗时: {}", (new Date().getTime() - oneStartDate));
    }

    /**
     * 针对于下拉框、级联框类 将中文值转成ID存储
     * @param cmdbCode 配置项
     * @param instanceData 实例值
     * @param dataMap 原始数据值
     * @param filedName 配置
     * @return
     */
    private String convertValueToID(CmdbCode cmdbCode, Map<String, Object> instanceData, Map<String, String> dataMap, String filedName) {
        // 先判断配置项类型, 如果未绑定数据源, 则无效转化
        String origValue = dataMap.get(filedName);
        if (!isRefCode(cmdbCode)) {
            return origValue;
        }
        String cacheKey = cmdbCode.getCodeId() + "_" + origValue;
        String idValue = null;
        if (selectCacheMap.containsKey(cacheKey)) {
            idValue = selectCacheMap.get(cacheKey);
        } else {
            List<Map<String, Object>> sourceDataList;
            try {
                this.validCascadeSort(cmdbCode, instanceData);
                sourceDataList = codeService.getRefCodeData(cmdbCode.getCodeId());
            } catch (Exception e) {
                log.error("获取配置项失败.", e);
                throw new RuntimeException("获取配置项[" + cmdbCode.getFiledName() + "]数据源失败.");
            }
            List<String> keys = new ArrayList<>();
            for (Map<String, Object> map : sourceDataList) {
                if (dataMap.get(filedName).equals(map.get("value"))) {
                    idValue = map.get("id").toString();
                    break;
                }
                keys.add(map.get("value").toString());
            }
            if (StringUtils.isEmpty(idValue)) {
                throw new RuntimeException("列[" + cmdbCode.getFiledName() + "]的值[" + dataMap.get(filedName) +
                        "]不在可选值范围[" + StringUtils.join(keys, ",") + "]");
            }
            selectCacheMap.put(cacheKey, idValue);
        }
        return idValue;
    }

    /**
     * 判断是否是引用字段
     * @param cmdbCode
     * @return
     */
    private boolean isRefCode(CmdbCode cmdbCode) {
        return ("是").equals(cmdbCode.getIsBindSource()) && cmdbCode.getCodeBindSource() != null
                && cmdbCode.getCodeBindSource().getBindSourceType() != null;
    }

    /**
     * 验证级联框的级联项顺序.
     * @param cmdbCode 配置项
     * @param instanceData 实例值
     */
    private void validCascadeSort(CmdbCode cmdbCode, Map<String, Object> instanceData){
        // 判断当前码表有没有被级联
        List<CmdbV3CodeCascade> cascadeList = codeCascadeService.getByChildCodeId(cmdbCode.getCodeId());
        if (cascadeList != null && cascadeList.size() > 0) {
            cascadeList.forEach((cascade) -> {
                // 判断位置, 确保级联选项顺序
                CmdbCode queryCode = new CmdbCode(cascade.getCodeId());
                CmdbCode cascadeCode = codeService.get(queryCode);
                if (cascadeCode == null) {
                    throw new RuntimeException("配置项[" + cascade.getCodeId() + "]无效, 请检测配置项[" + cmdbCode.getFiledName() + "]的级联配置.");
                }
                if (!instanceData.containsKey(cascadeCode.getFiledCode())) {
                    throw new RuntimeException("请将Excel中配置项[" + cascadeCode.getFiledName() + "]放到配置项[" + cmdbCode.getFiledName() + "]前面.");
                }
            });
        }
    }

    /**
     * 获取当前行的模型码表列表
     *
     * @param selfModule
     * @return
     */
    private List<CmdbCode> getModuleCodeList(Module selfModule) {
        List<CmdbCode> codeList;
        if (cacheModuleCodeMap.containsKey(selfModule.getId())) {
            codeList = cacheModuleCodeMap.get(selfModule.getId());
        } else {
            codeList = getCodeService().getCodeListByModuleId(selfModule.getId());
            cacheModuleCodeMap.put(selfModule.getId(), codeList);
        }
        if (codeList == null) {
            throw new RuntimeException("模型[" + selfModule.getName() + "]未找到配置字段列表.");
        }
        return codeList;
    }

    /**
     * 获取当前导入模型的模型识别主键
     *
     * @param module
     * @return
     */
    private List<String> getImportModulePrimaryKeys(Module module) {
        List<String> moduleKeys;
        if (cachePrimaryKeyMap.containsKey(module.getId())) {
            moduleKeys = cachePrimaryKeyMap.get(module.getId());
        } else {
            moduleKeys = getModuleService().getModuleKeysById(module.getId());
            if (moduleKeys == null || moduleKeys.size() == 0) {
                throw new RuntimeException("未匹配到模型名称[" + module.getName() + "]的主键标识.");
            }
            cachePrimaryKeyMap.put(module.getId(), moduleKeys);
        }
        return moduleKeys;
    }

    /**
     * 获取当前行的模型信息
     *
     * @param moduleName
     * @return
     */
    private Module getImportModule(String moduleName) {
        Module selfModule;
        if (cacheModuleMap.containsKey(moduleName)) {
            selfModule = cacheModuleMap.get(moduleName);
        } else {
            throw new RuntimeException("未匹配到模型名称[" + moduleName + "]");
        }
        return selfModule;
    }

    /**
     * 验证字段长度
     *
     * @param cmdbCode
     *            码表信息
     * @param value
     *            验证值
     */
    private void validCodeLength(CmdbCode cmdbCode, String value) {
        // 验证长度
        if (StringUtils.isNotEmpty(value) && cmdbCode.getCodeLength() != null) {
            if (cmdbCode.getCodeLength() < value.length()) {
                throw new RuntimeException(("列[" + cmdbCode.getFiledName() + "]" + "字段长度超过" + cmdbCode.getCodeLength()));
            }
        }
    }

//    private void validReadOnly(CmdbCode cmdbCode, String value) {
//        boolean readOnly = cmdbCode.getUpdateReadOnly() == 1;
//        if (readOnly) {
//            if (StringUtils.isNotEmpty(value)) {
//                throw new RuntimeException("列["+cmdbCode.getFiledName()+"]为只读列, 不允许导入维护. 请删除该列或将该列值置为空.");
//            }
//        }
//    }

    /**
     * 验证字段的验证器
     *
     * @param cmdbCode
     *            码表信息
     * @param value
     *            验证值
     */
    private void validCodeValidator(CmdbCode cmdbCode, String value) {
        Map<String, Object> codeMap = new HashMap<>();
        codeMap.put(cmdbCode.getCodeId(), value);
        Map<String, Map<String, String>> validReturn = this.codeService.validCodeValue(codeMap);
        if (validReturn.containsKey(cmdbCode.getCodeId())) {
            Map<String, String> returnMap = validReturn.get(cmdbCode.getCodeId());
            if (returnMap.containsKey("flag") && ("error").equals(returnMap.get("flag"))) {
                throw new RuntimeException("列[" + cmdbCode.getFiledName() + "]数据验证失败, " + returnMap.get("msg"));
            }
        }
    }

    /**
     * 验证数据
     *
     * @param cmdbCode
     *            码表信息
     * @param value 字段信息
     */
    private void validCodeValue(CmdbCode cmdbCode, String value) {
        long startDate = new Date().getTime();
        // 验证码表长度
        this.validCodeLength(cmdbCode, value);
//        // 只读属性验证
//        this.validReadOnly(cmdbCode, value);
        // 验证数据格式
        this.validCodeValidator(cmdbCode, value);
        log.info("导入消耗: {} 验证 1. {}", cmdbCode.getFiledCode(), (new Date().getTime() - startDate));
        startDate = new Date().getTime();
        // 验证可选值范围
        switch (cmdbCode.getControlType().getControlCode()) {
            case Constants.CODE_CONTROL_TYPE_LISTSEL: // 下拉菜单
            case Constants.CODE_CONTROL_TYPE_SINGLESEL: // 单选
            case Constants.CODE_CONTROL_TYPE_MULTISEL: // 多选
                // modify by zhu.jw 2020-7-27 目前模板中下拉或级联数据已经只能选择, 因此无需再做范围验证
                // this.validSelectControl(cmdbCode, value);
                break;
            case Constants.CODE_CONTROL_TYPE_DATETIME:// 时间
                validDateTime(cmdbCode, value);
                break;
            case Constants.CODE_CONTROL_TYPE_CASCADER: // 级联
                // 已经在上面对每个码表进行过级联配置过滤
                break;
            default:
                break;
        }
        log.info("导入消耗: {} 验证 2. {}", cmdbCode.getFiledCode(), (new Date().getTime() - startDate));
        startDate = new Date().getTime();
    }

    /**
     * 验证日期框值
     *
     * @param cmdbCode
     *            码表信息
     * @param columnValue
     *            值
     */
    private void validDateTime(CmdbCode cmdbCode, String columnValue) {
        try {
            if (StringUtils.isNotEmpty(columnValue)) {
                DateUtils.parseDate(columnValue, new String[] { Constants.DATE_PATTERN_YYYY_MM_DD,
                        Constants.DATE_PATTERN_YYYY_MM_DD_HH_MM_SS, Constants.DATE_PATTERN_YYYY_MM_DD_01 });
            }
        } catch (Exception e) {
            throw new RuntimeException("列[" + cmdbCode.getFiledName()
                    + "]日期格式不正确, 支持['yyyy-mm-dd','yyyy-mm-dd HH:mm:ss','yyyy/mm/dd']格式");
        }
    }
}
