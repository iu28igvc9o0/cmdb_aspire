package com.aspire.ums.cmdb.v3.module.service.impl;

import com.aspire.mirror.redis.api.payload.KeyValueRedisEntity;
import com.aspire.ums.cmdb.code.payload.CmdbCode;
import com.aspire.ums.cmdb.common.Constants;
import com.aspire.ums.cmdb.util.StringUtils;
import com.aspire.ums.cmdb.v2.code.service.ICmdbCodeService;
import com.aspire.ums.cmdb.v3.code.payload.CmdbV3CodeValidate;
import com.aspire.ums.cmdb.v3.module.mapper.CmdbV3ModuleGroupMapper;
import com.aspire.ums.cmdb.v3.module.payload.CmdbV3ModuleCodeSetting;
import com.aspire.ums.cmdb.v3.module.payload.CmdbV3ModuleGroup;
import com.aspire.ums.cmdb.v3.module.payload.CmdbV3ModuleGroupRelation;
import com.aspire.ums.cmdb.v3.module.service.ICmdbV3ModuleCodeSettingService;
import com.aspire.ums.cmdb.v3.module.service.ICmdbV3ModuleGroupService;
import com.aspire.ums.cmdb.v3.redis.client.IRedisClient;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
* 描述：
* @author
* @date 2020-01-09 14:33:21
*/
@Service
public class CmdbV3ModuleGroupServiceImpl implements ICmdbV3ModuleGroupService {

    @Autowired
    private CmdbV3ModuleGroupMapper mapper;
    @Autowired
    private ICmdbCodeService codeService;
    @Autowired
    private ICmdbV3ModuleCodeSettingService codeSettingService;
    @Autowired
    private IRedisClient redisClient;

    public List<Map<String, Object>> getGroupHeader(String moduleId, String tableHeaderCode) {
        if (StringUtils.isEmpty(tableHeaderCode)) {
            tableHeaderCode = "";
        }
        Object redisValue = redisClient.get(String.format(Constants.REDIS_MODULE_GROUP_SETTING, moduleId, tableHeaderCode));
        List<Map<String, Object>> titleMapList = new ArrayList<>();
        if (redisValue == null) {
            List<CmdbV3ModuleGroup> rootGroupList = getChildModuleGroupList(moduleId, tableHeaderCode,"0");
            if (rootGroupList != null && rootGroupList.size() > 0) {
                for (CmdbV3ModuleGroup moduleGroup : rootGroupList) {
                    Map<String, Object> titleProperty = new HashMap<>();
                    titleProperty.put("key", moduleGroup.getId());
                    titleProperty.put("name", moduleGroup.getGroupName());
                    titleProperty.put("display", moduleGroup.getDisplay());
                    if (StringUtils.isNotEmpty(moduleGroup.getFixed())) {
                        titleProperty.put("fixed", moduleGroup.getFixed());
                    }
                    this.loopModuleGroup(moduleId, tableHeaderCode, moduleGroup, titleProperty);
                    titleMapList.add(titleProperty);
                }
            } else {
                CmdbV3ModuleCodeSetting codeSetting = new CmdbV3ModuleCodeSetting();
                codeSetting.setModuleId(moduleId);
                List<CmdbV3ModuleCodeSetting> codeSettingList = codeSettingService.listByEntity(codeSetting);
                for (CmdbV3ModuleCodeSetting setting : codeSettingList) {
                    // 过滤不显示的字段
                    titleMapList.add(formatCodeProperty(setting.getCodeId(), setting.getDisplay()));
                }
            }
            KeyValueRedisEntity keyValue = new KeyValueRedisEntity();
            keyValue.setKey(String.format(Constants.REDIS_MODULE_GROUP_SETTING, moduleId, tableHeaderCode));
            keyValue.setValue(titleMapList);
            redisClient.set(keyValue);
        } else {
            titleMapList = new ObjectMapper().convertValue(redisValue, new TypeReference<List<Map<String, Object>>>(){});
        }
        return titleMapList;
    }

    private Map<String, Object> formatCodeProperty(String codeId, Integer display) {
        Map<String, Object> titleProperty = new HashMap<>();
        CmdbCode cmdbCode = new CmdbCode();
        cmdbCode.setCodeId(codeId);
        cmdbCode = codeService.get(cmdbCode);
        if (cmdbCode != null) {
            titleProperty.put("codeId", cmdbCode.getCodeId());
            titleProperty.put("key", cmdbCode.getFiledCode());
            titleProperty.put("name", cmdbCode.getFiledName());
            titleProperty.put("controlType", cmdbCode.getControlType().getControlCode());
            titleProperty.put("display", display);
            List<Map<String, Object>> ruleList = new LinkedList<>();
            // 增加校验规则
            List<CmdbV3CodeValidate> validates = cmdbCode.getValidates();
            if (validates != null && validates.size() > 0) {
                for (CmdbV3CodeValidate codeValidate : validates) {
                    if (codeValidate.getValidType().equals("非空")) {
                        Map<String, Object> emptyRule = new LinkedHashMap<>();
                        emptyRule.put("validType", "fromConfig");
                        emptyRule.put("required", true);
                        emptyRule.put("message", cmdbCode.getFiledName() + "不能为空!");
                        ruleList.add(emptyRule);
                    } else {
                        Map<String, Object> rule = new LinkedHashMap<>();
                        rule.put("validType", "fromFun");
                        ruleList.add(rule);
                        break;
                    }
                }
            }
            titleProperty.put("rules", ruleList);
        }
        return titleProperty;
    }

    private void loopModuleGroup(String moduleId, String tableHeaderCode, CmdbV3ModuleGroup moduleGroup, Map<String, Object> titleMap) {
        // 0 非根分组 1 根分组
        if (moduleGroup.getIsEndNode() == 0) {
            List<CmdbV3ModuleGroup> childList = getChildModuleGroupList(moduleId, tableHeaderCode, moduleGroup.getParentGroupId());
            List<Map<String, Object>> childTitle = new LinkedList<>();
            for (CmdbV3ModuleGroup child : childList) {
                // 分组
                Map<String, Object> titleProperty = new HashMap<>();
                titleProperty.put("key", child.getId());
                titleProperty.put("name", child.getGroupName());
                titleProperty.put("display", child.getDisplay());
                if (StringUtils.isNotEmpty(moduleGroup.getFixed())) {
                    titleProperty.put("fixed", moduleGroup.getFixed());
                }
                this.loopModuleGroup(moduleId, tableHeaderCode, child, titleProperty);
                childTitle.add(titleProperty);
            }
            titleMap.put("children", childTitle);
        } else {
            List<CmdbV3ModuleGroupRelation> relationList = this.getAllGroupCodeList(moduleGroup.getId());
            List<Map<String, Object>> childList = new LinkedList<>();
            for (CmdbV3ModuleGroupRelation relation : relationList) {
                Map<String, Object> titleProperty = formatCodeProperty(relation.getCodeId(), relation.getDisplay());
                childList.add(titleProperty);
            }
            titleMap.put("children", childList);
        }
    }

    /**
     * 获取模型分组列表
     */
    @Override
    public List<CmdbV3ModuleGroup> getModuleGroupList(String moduleId) {
        return mapper.getModuleGroupList(moduleId);
    }

    /**
     * 获取父分组下所有的分组信息
     *
     * @param parentGroupId 父分组ID
     * @return 子模型分组列表
     */
    @Override
    public List<CmdbV3ModuleGroup> getChildModuleGroupList(String moduleId, String tableHeaderCode, String parentGroupId) {
        return mapper.getChildModuleGroupList(moduleId, tableHeaderCode, parentGroupId);
    }

    /**
     * 获取分组下所有的码表配置信息
     *
     * @param groupId 分组ID
     * @return 所有的分组码表关系
     */
    @Override
    public List<CmdbV3ModuleGroupRelation> getAllGroupCodeList(String groupId) {
        return mapper.getAllGroupCodeList(groupId);
    }

//
//    /**
//     * 获取所有实例
//     * @return 返回所有实例数据
//     */
//    public List<CmdbV3ModuleGroup> list() {
//        return mapper.list();
//    }
//
//    /**
//     * 根据主键ID 获取数据信息
//     * @param entity 实例信息
//     * @return 返回指定ID的数据信息
//     */
//    public CmdbV3ModuleGroup get(CmdbV3ModuleGroup entity) {
//        return mapper.get(entity);
//    }
//
//    /**
//     * 新增实例
//     * @param entity 实例数据
//     * @return
//     */
//    public void insert(CmdbV3ModuleGroup entity) {
//        mapper.insert(entity);
//    }
//
//    /**
//     * 修改实例
//     * @param entity 实例数据
//     * @return
//     */
//    public void update(CmdbV3ModuleGroup entity) {
//        mapper.update(entity);
//    }
//
//    /**
//     * 删除实例
//     * @param entity 实例数据
//     * @return
//     */
//    public void delete(CmdbV3ModuleGroup entity) {
//        mapper.delete(entity);
//    }
}