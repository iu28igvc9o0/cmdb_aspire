package com.aspire.ums.cmdb.v2.process.instance;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;

import com.aspire.ums.cmdb.code.payload.CmdbCode;
import com.aspire.ums.cmdb.code.payload.CmdbV3CodeCascade;
import com.aspire.ums.cmdb.common.Constants;
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
import com.aspire.ums.cmdb.v3.module.service.ICmdbV3ModuleCatalogService;
import com.aspire.ums.cmdb.v3.module.service.ICmdbV3ModuleCodeSettingService;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Copyright (C), 2015-2020, 卓望数码有限公司 FileName: IPRepositoryFactory Author: zhu.juwang Date: 2020/6/10 16:36 Description: 线路结算配置导入
 * History: <author> <time> <version> <desc> 作者姓名 修改时间 版本号 描述
 */
@Data
@Slf4j
@NoArgsConstructor
public class ServerProjectRepositoryFactory extends ImportFactory {

    private ICmdbInstanceService instanceService;

    private ICmdbCodeService codeService;

    private ICmdbCodeValidateService validateService;

    private ConfigDictService configDictService;

    private ModuleService moduleService;

    private ICmdbV3ModuleCatalogService catalogService;

    private ICmdbV3ModuleCodeSettingService codeSettingService;

    private ICmdbV3CodeCascadeService codeCascadeService;

    private ICmdbConfigService cmdbConfigService;

    // 缓存码表下拉框可选值列表
    private final Map<String, List<Map<String, String>>> cacheSelectValues = new HashMap<>();

    // 缓存表格title与码表字段的对应关系
    private final Map<String, List<CmdbCode>> cacheModuleCodeMap = new HashMap<>();

    private Map<String, List<Map<String, Object>>> CODE_ID_DATA_MAP = new LinkedHashMap<>();

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
    public void execute(Map<String, String> dataMap) {
        String moduleType = getProcessParams().get("moduleType") == null ? "" : getProcessParams().get("moduleType");
        String moduleId = getProcessParams().get("moduleId") == null ? "" : getProcessParams().get("moduleId");
        Module selfModule;
        if (StringUtils.isNotEmpty(moduleId)) {
            selfModule = getModuleService().getModuleDetail(moduleId);
        } else {
            selfModule = getModuleService().getDefaultModule(moduleType);
        }
        if (selfModule == null) {
            throw new RuntimeException("未匹配到模型.");
        }
        // 验证主键列表
        List<String> moduleKeys = getModuleService().getModuleKeysById(selfModule.getId());
        // 验证主键值是否存在
        List<CmdbCode> codeList = getModuleCodeList(selfModule);
        Map<String, Object> realKeyMap = new HashMap<>();
        // 验证是否有多余的列, 存在多余列是不允许更新
        Map<String, Object> instanceData = new HashMap<>(); // 所有列的值
        // 主键列的值
        Map<String, Object> primaryKeysValue = new HashMap<>();
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
            // 如果未匹配到码表字段 且 改列的值不为空 则提示导入失败错误
            if (cmdbCode == null) {
                if (StringUtils.isNotEmpty(dataMap.get(filedName))) {
                    throw new RuntimeException("模型[" + selfModule.getName() + "]中未找到配置项[" + super.getRealMapKey(filedName) + "]");
                }
                continue;
            }
            if (StringUtils.isNotEmpty(dataMap.get(filedName))) {
                instanceData.put(cmdbCode.getFiledCode(), dataMap.get(filedName));
            }
            // 增加字段验证
            this.validCodeValue(cmdbCode, dataMap, filedName);
            // 增加实例数据值
            if (StringUtils.isNotEmpty(dataMap.get(filedName))) {
                // 转化存储值为ID
                String convertValue = this.convertValueToID(cmdbCode, instanceData, dataMap, filedName);
                instanceData.put(cmdbCode.getFiledCode(), convertValue);
            }
            // 处理主键
            for (String primaryKey : moduleKeys) {
                if (cmdbCode.getFiledCode().equals(primaryKey)) {
                    // 主键值为空
                    if (!StringUtils.isNotEmpty(dataMap.get(filedName))) {
                        throw new RuntimeException("主键[" + cmdbCode.getFiledCode() + "]字段值不能为空.");
                    }
                    Object fileValue = instanceData.get(cmdbCode.getFiledCode());
                    if (null != fileValue) {
                        primaryKeysValue.put(primaryKey, fileValue.toString());
                    } else {
                        primaryKeysValue.put(primaryKey, dataMap.get(filedName).trim());
                    }
                }
            }
            realKeyMap.put(super.getRealMapKey(filedName), dataMap.get(filedName));
        }
        // 补充模型ID字段
        if (!instanceData.containsKey("module_id") || instanceData.get("module_id") == null
                || ("").equals(instanceData.get("module_id").toString())) {
            instanceData.put("module_id", selfModule.getId());
        }
        Map<String, Object> existsInstanceData = getModuleService().getModuleDataByPrimarys(moduleId, primaryKeysValue);
        String operatorUser = StringUtils.isEmpty(getProcessParams().get("operatorUser")) ? "系统管理员"
                : getProcessParams().get("operatorUser");
        if (existsInstanceData != null) {
            if (!existsInstanceData.containsKey("id")) {
                throw new RuntimeException("Get result data missing require property [id].");
            }
            getInstanceService().updateInstance(existsInstanceData.get("id").toString(), operatorUser, instanceData, "批量导入");
        } else {
            CmdbConfig canNotImportInsertModuleConfig = getCmdbConfigService()
                    .getConfigByCode(CmdbConfigConst.CAN_NOT_IMPORT_INSERT_MODULE);
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
            getInstanceService().addInstance(operatorUser, instanceData, "批量导入");
        }
    }

    private String convertValueToID(CmdbCode cmdbCode, Map<String, Object> instanceData, Map<String, String> dataMap,
            String filedName) {
        // 先判断配置项类型, 如果未绑定数据源, 则无效转化
        if (("否").equals(cmdbCode.getIsBindSource()) || cmdbCode.getCodeBindSource() == null
                || cmdbCode.getCodeBindSource().getBindSourceType() == null) {
            return dataMap.get(filedName);
        }
        List<Map<String, Object>> sourceDataList;
        if (!CODE_ID_DATA_MAP.containsKey("DATA_" + cmdbCode.getCodeId() + "_" + dataMap.get(filedName))) {
            // 判断当前码表有没有被级联
            List<CmdbV3CodeCascade> cascadeList = codeCascadeService.getByChildCodeId(cmdbCode.getCodeId());
            if (cascadeList != null && cascadeList.size() > 0) {
                cascadeList.forEach((cascade) -> {
                    // 判断位置, 确保级联选项顺序
                    CmdbCode queryCode = new CmdbCode(cascade.getCodeId());
                    CmdbCode cascadeCode = codeService.get(queryCode);
                    if (cascadeCode == null) {
                        throw new RuntimeException(
                                "配置项[" + cascade.getCodeId() + "]无效, 请检测配置项[" + cmdbCode.getFiledName() + "]的级联配置.");
                    }
                    if (!instanceData.containsKey(cascadeCode.getFiledCode())) {
                        throw new RuntimeException(
                                "请将Excel中配置项[" + cascadeCode.getFiledName() + "]放到配置项[" + cmdbCode.getFiledName() + "]前面.");
                    }
                });
            }
            try {
                sourceDataList = codeService.getRefCodeData(cmdbCode.getCodeId());
                CODE_ID_DATA_MAP.put("DATA_" + cmdbCode.getCodeId() + "_" + dataMap.get(filedName), sourceDataList);
            } catch (Exception e) {
                log.error("获取配置项失败.", e);
                throw new RuntimeException("获取配置项[" + cmdbCode.getFiledName() + "]数据源失败.");
            }
        } else {
            sourceDataList = CODE_ID_DATA_MAP.get("DATA_" + cmdbCode.getCodeId() + "_" + dataMap.get(filedName));
        }
        Map<String, Object> idMap = sourceDataList.stream().filter((dict) -> dataMap.get(filedName).equals(dict.get("value")))
                .findFirst().orElse(null);
        String idValue = null;
        if (idMap != null) {
            idValue = idMap.get("id").toString();
        }
        if (idMap == null || StringUtils.isEmpty(idValue)) {
            List<String> keys = new ArrayList<>();
            sourceDataList.forEach(map -> keys.add(map.get("value").toString()));
            throw new RuntimeException("列[" + cmdbCode.getFiledName() + "]的值[" + dataMap.get(filedName) + "]不在可选值范围["
                    + StringUtils.join(keys, ",") + "]");
        }
        return idValue;
    }

    /**
     * 验证数据
     *
     * @param cmdbCode
     *            码表信息
     * @param dataMap
     *            行实例数据
     * @param filedName
     *            字段信息
     */
    private void validCodeValue(CmdbCode cmdbCode, Map<String, String> dataMap, String filedName) {
        String value = dataMap.get(filedName);
        // 验证码表长度
        this.validCodeLength(cmdbCode, value);
        // 验证数据格式
        this.validCodeValidator(cmdbCode, value);
        /*
         * todo 级联涉及到ID与VALUE值的切换, 需要后面优化设计方案处理. 以下内容注释, 但不可删除 // 判断字段是否被其他码表字段级联 List<CmdbV3CodeCascade> parentCascadeList =
         * codeCascadeService.getByChildCodeId(cmdbCode.getCodeId()); if (parentCascadeList != null && parentCascadeList.size() > 0)
         * { // 处理验证级联数据 validCascader(parentCascadeList, cmdbCode, dataMap, value); }
         */
        // 验证可选值范围
        switch (cmdbCode.getControlType().getControlCode()) {
            case Constants.CODE_CONTROL_TYPE_LISTSEL: // 下拉菜单
            case Constants.CODE_CONTROL_TYPE_SINGLESEL: // 单选
            case Constants.CODE_CONTROL_TYPE_MULTISEL: // 多选
                // this.validSelectControl(cmdbCode, value);
                break;
            case Constants.CODE_CONTROL_TYPE_DATETIME:// 时间
                validDateTime(cmdbCode, value);
                break;
            case Constants.CODE_CONTROL_TYPE_CASCADER: // 级联
                // 已经在上面对每个码表进行过级联配置过滤
                // this.validSelectControl(cmdbCode, value); // 增加下拉值验证
                break;
            default:
                break;
        }
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
            throw new RuntimeException(
                    "列[" + cmdbCode.getFiledName() + "]日期格式不正确, 支持['yyyy-mm-dd','yyyy-mm-dd HH:mm:ss','yyyy/mm/dd']格式");
        }
    }

    /**
     * 验证下拉框值
     * 
     * @param cmdbCode
     *            码表对象
     * @param columnValue
     *            值
     */
    private void validSelectControl(CmdbCode cmdbCode, String columnValue) {
        if (StringUtils.isNotEmpty(columnValue)) {
            // 先填充码表的值列表
            this.fillSelectValues(cmdbCode);
            List<Map<String, String>> dataValues = cacheSelectValues.get(cmdbCode.getCodeId());
            List<String> keys = new ArrayList<>();
            dataValues.forEach((data) -> {
                keys.add(data.get("value"));
            });
            if (!keys.contains(columnValue)) {
                throw new RuntimeException("列[" + cmdbCode.getFiledName() + "]不在可选值范围. 可选值范围[" + StringUtils.join(keys, ',') + "]");
            }
        }
    }

    /**
     * 填充下拉框的值
     * 
     * @param cmdbCode
     *            码表信息
     * @return
     */
    private void fillSelectValues(CmdbCode cmdbCode) {
        final List<Map<String, String>> options = cacheSelectValues.getOrDefault(cmdbCode.getCodeId(), new ArrayList<>());
        if (options.size() == 0) {
            // 如果有绑定数据源
            if (Constants.YES.equals(cmdbCode.getIsBindSource())) {
                String bindSource = cmdbCode.getCodeBindSource().getBindSourceType();
                // 码表绑定字典表
                if (Constants.CODE_SOURCE_TYPE_DICT.equals(bindSource)) {
                    options.addAll(super.getDictList(cmdbCode.getCodeBindSource().getDictSource(), null, null, null));
                }
                // 码表绑定数据表
                else if (Constants.CODE_SOURCE_TYPE_TABLE.equals(bindSource)) {
                    try {
                        List<Map<String, Object>> list = super.getSchemaService()
                                .executeSql(cmdbCode.getCodeBindSource().getTableSql());
                        list.forEach(map -> {
                            Map<String, String> data = new HashMap<>();
                            data.put("id", String.valueOf(map.get("id")));
                            data.put("key", String.valueOf(map.get("key")));
                            data.put("value", String.valueOf(map.get("value")));
                            options.add(data);
                        });
                    } catch (Exception e) {
                        throw new RuntimeException("列[" + cmdbCode.getFiledName() + "]绑定数据源无效, 配置查询SQL["
                                + cmdbCode.getCodeBindSource().getTableSql() + "]不正确");
                    }
                } else if (Constants.CODE_SOURCE_TYPE_REF_MODULE.equals(bindSource)) {
                    throw new RuntimeException("列[" + cmdbCode.getFiledName() + "]绑定数据源无效, 目前不支持引用模型方式");
                } else {
                    throw new RuntimeException("列[" + cmdbCode.getFiledName() + "]绑定数据源无效, 目前不支持[" + bindSource + "]方式");
                }
                if (options.size() == 0) {
                    throw new RuntimeException("列[" + cmdbCode.getFiledName() + "]绑定数据源无效, 未发现字典类型["
                            + cmdbCode.getCodeBindSource().getDictSource() + "]");
                }
                cacheSelectValues.put(cmdbCode.getCodeId(), options);
            } else {
                throw new RuntimeException("列[" + cmdbCode.getFiledName() + "]未绑定数据源信息.");
            }
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
}
