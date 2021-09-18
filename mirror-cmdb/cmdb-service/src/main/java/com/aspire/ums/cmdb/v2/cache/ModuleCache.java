package com.aspire.ums.cmdb.v2.cache;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: ModuleCache
 * Author:   zhu.juwang
 * Date:     2019/5/20 21:43
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Component
@Slf4j
public class ModuleCache {

//    @Autowired
//    private ModuleService moduleService;
//    @Autowired
//    private ICmdbModuleCodeGroupService groupService;
//    @Autowired
//    private ICmdbModuleCodeRelationService relationService;
//    @Autowired
//    private ICmdbCodeService codeService;
//    @Autowired
//    private ICmdbCodeValidateService validateService;
//
//    /**
//     * 刷新Module List缓存
//     */
//    public void refreshCache() {
////        List<Module> moduleList =  moduleService.getModuleList();
////        log.info("开始计算模型缓存...");
////        if (moduleList != null && moduleList.size() > 0) {
////            moduleList.stream().forEach((module) -> {
////                refreshCache(module.getId());
////            });
////        }
////        log.info("计算模型缓存结束.");
//    }
//
//    /**
//     * 根据moduleId 刷新模型数据
//     * @param moduleId 模型ID
//     */
//    public void refreshCache(String moduleId) {
//        log.info("开始计算模型 -> {}", moduleId);
//        Module module = moduleService.getModuleDetail(moduleId);
//        CacheConst.CACHE_MODULE_MAP.put(module.getId(), getFullModule(module));
//        if (module.getChildModules() != null && module.getChildModules().size() > 0) {
//            module.getChildModules().stream().forEach((child) -> {
//                refreshCache(child.getId());
//            });
//        }
//    }
//
//    public FullModule getFullModule(Module module) {
//        List<FullModule.CmdbGroup> fullGroups = new LinkedList<>();
//        //加载模型分组
//        List<CmdbModuleCodeGroup> groupList = groupService.getGroupListByModuleId(module.getId());
//        if (groupList != null && groupList.size() > 0) {
//            for (CmdbModuleCodeGroup group : groupList) {
//                //加载分组下对应的码表列表
//                List<FullModule.CmdbCode> codeList = new LinkedList<>();
//                List<CmdbModuleCodeRelation> codeRelationList =
//                        relationService.getCodeListByModuleIdAndGroupId(module.getId(), group.getGroupId());
//                if (codeRelationList != null && codeRelationList.size() > 0) {
//                    for (CmdbModuleCodeRelation codeRelation : codeRelationList) {
//                        CmdbCode queryEntity = new CmdbCode();
//                        queryEntity.setCodeId(codeRelation.getCodeId());
//                        //加载码表详细信息
//                        CmdbCode cmdbCode = codeService.get(queryEntity);
//                        if (cmdbCode == null) {
//                            continue;
//                        }
//                        //加载码表验证信息
//                        List<CmdbCodeValidate> validateList = validateService.getValidateListByCodeId(codeRelation.getCodeId());
//                        FullModule.CmdbCode code = new FullModule.CmdbCode();
//                        FullCode.CmdbCode fullCode = new FullCode.CmdbCode();
//                        fullCode.setCmdbCode(cmdbCode);
//                        fullCode.setControlType(cmdbCode.getControlType());
//                        fullCode.setValidateList(validateList);
//                        code.setCmdbCode(fullCode);
//                        code.setDefaultValue(codeRelation.getDefaultValue());
//                        code.setColSpan(codeRelation.getColSpan());
//                        code.setSortIndex(codeRelation.getSortIndex());
//                        codeList.add(code);
//                    }
//                }
//                FullModule.CmdbGroup cmdbGroup = new FullModule.CmdbGroup();
//                cmdbGroup.setCodeGroup(group);
//                cmdbGroup.setCodeList(codeList);
//                fullGroups.add(cmdbGroup);
//            }
//        }
//        FullModule fullModule = new FullModule();
//        fullModule.setModule(module);
//        fullModule.setGroupList(fullGroups);
//        fullModule.setChildModules(new ArrayList<>());
//        return fullModule;
//    }
}
