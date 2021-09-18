package com.aspire.ums.cmdb.v3.condication.service.impl;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.aspire.ums.cmdb.code.payload.CmdbCode;
import com.aspire.ums.cmdb.common.Constants;
import com.aspire.ums.cmdb.common.Result;
import com.aspire.ums.cmdb.dict.service.ConfigDictService;
import com.aspire.ums.cmdb.module.payload.Module;
import com.aspire.ums.cmdb.util.StringUtils;
import com.aspire.ums.cmdb.util.UUIDUtil;
import com.aspire.ums.cmdb.v2.code.service.ICmdbCodeService;
import com.aspire.ums.cmdb.v2.module.service.ModuleService;
import com.aspire.ums.cmdb.v3.condication.mapper.CmdbV3CondicationReturnRelationMapper;
import com.aspire.ums.cmdb.v3.condication.mapper.CmdbV3CondicationSettingMapper;
import com.aspire.ums.cmdb.v3.condication.mapper.CmdbV3CondicationSettingRelationMapper;
import com.aspire.ums.cmdb.v3.condication.mapper.CmdbV3CondicationSortRelationMapper;
import com.aspire.ums.cmdb.v3.condication.payload.CmdbV3AccessUser;
import com.aspire.ums.cmdb.v3.condication.payload.CmdbV3CondicationReturnRelation;
import com.aspire.ums.cmdb.v3.condication.payload.CmdbV3CondicationSetting;
import com.aspire.ums.cmdb.v3.condication.payload.CmdbV3CondicationSettingQuery;
import com.aspire.ums.cmdb.v3.condication.payload.CmdbV3CondicationSettingRelation;
import com.aspire.ums.cmdb.v3.condication.payload.CmdbV3CondicationSortRelation;
import com.aspire.ums.cmdb.v3.condication.service.ICmdbV3AccessUserService;
import com.aspire.ums.cmdb.v3.condication.service.ICmdbV3CondicationSettingService;
import com.aspire.ums.cmdb.v3.redis.service.IRedisService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

/**
* 描述：
* @author
* @date 2020-01-09 14:33:20
*/
@Service
@Slf4j
public class CmdbV3CondicationSettingServiceImpl implements ICmdbV3CondicationSettingService {

    @Autowired
    private CmdbV3CondicationSettingMapper mapper;
    @Autowired
    private CmdbV3CondicationSettingRelationMapper relationMapper;
    @Autowired
    private CmdbV3CondicationReturnRelationMapper returnRelationMapper;
    @Autowired
    private CmdbV3CondicationSortRelationMapper sortRelationMapper;
    @Autowired
    private ICmdbV3AccessUserService userService;
    @Autowired
    private ModuleService moduleService;
    @Autowired
    private ConfigDictService dictService;
    @Autowired
    private IRedisService redisService;
    @Autowired
    private ICmdbCodeService codeService;
    @Value("${cmdb.access.inner}")
    private String innerUserId;

    /**
     * 获取所有实例
     * @return 返回所有实例数据
     */
    @Override
    public List<CmdbV3CondicationSetting> list() {
        return mapper.list();
    }

    /**
     * 根据主键ID 获取数据信息
     * @param entity 实例信息
     * @return 返回指定ID的数据信息
     */
    @Override
    public CmdbV3CondicationSetting get(CmdbV3CondicationSetting entity) {
         return mapper.get(entity);
    }

    /**
     * 根据配置编码及接入用户获取条件配置信息
     * @param indicationCode 配置编码
     * @param accessUserId 接入用户ID
     * @return
     */
    @Override
    public Map<String, Object> getSettingByCodeAndAccessUserId(String indicationCode, String accessUserId) {
        String key = indicationCode + "_" + accessUserId;
        Object object = redisService.get(String.format(Constants.REDIS_CONDITION_SETTING_DETAIL, key));
        if (object == null) {
            CmdbV3CondicationSetting querySetting = new CmdbV3CondicationSetting();
            querySetting.setAccessUserId(accessUserId);
            querySetting.setCondicationCode(indicationCode);
            CmdbV3CondicationSetting setting = get(querySetting);
            if (setting == null) {
                throw new RuntimeException("Can't find condication record. indicationCode -> " + indicationCode +
                        " accessUserId -> " + accessUserId);
            }
            // 解析SETTING
            Map<String, Object> returnMap = new HashMap<>();
            returnMap.put("moduleId", setting.getModuleId());
            // 判断查询条件的必填性
            List<CmdbV3CondicationSettingRelation> relationList = setting.getSettingRelationList();
            List<Map<String, Object>> paramsList = new LinkedList<>();
//            String defaultDeviceType = ""; // 默认适用的设备类型
            if (relationList != null && relationList.size() > 0) {
                for (CmdbV3CondicationSettingRelation relation : relationList) {
                    if (relation.getCmdbCode() == null) {
                        log.warn("Code id -> {} has been deleted.", relation.getCodeId());
                        continue;
                    }
                    Map<String, Object> paramMap = new HashMap<>();
                    paramMap.put("filed", relation.getCmdbCode().getFiledCode());
                    paramMap.put("filed_type", relation.getCmdbCode().getControlType().getControlCode());
                    paramMap.put("operator", relation.getOperateType());
                    Object value = null;
                    if (StringUtils.isNotEmpty(relation.getDefaultValue())) { // 存在默认值
                        if (relation.getOperateType().equalsIgnoreCase("in")
                                || relation.getOperateType().equalsIgnoreCase("not in")
                                || relation.getOperateType().equalsIgnoreCase("between")) {
                            String[] temArray = String.valueOf(relation.getDefaultValue()).split("\\,");
                            value = Arrays.asList(temArray);
                        } else {
                            value = relation.getDefaultValue();
                        }
//                        // 处理设备类型
//                        if (relation.getCmdbCode().getFiledCode().equalsIgnoreCase("device_type")) {
//                            defaultDeviceType = relation.getDefaultValue();
//                        }
                    }
                    paramMap.put("value", value);
                    if ("0".equals(relation.getIsRequire())) {
                        paramMap.put("require", true);
                    } else {
                        paramMap.put("require", false);
                    }
                    if (relation.getOperateType().equalsIgnoreCase("contain")) {
                        String codeId = relation.getCodeId(); // 默认的codeId
                        String containCodeId = relation.getContainCodeId();
                        // 判断多选的查询条件里面是否存在默认的查询字段，没有就添加进去
                        if (!containCodeId.contains(codeId)) {
                            containCodeId += "," + codeId;
                        }
                        paramMap.put("containFiledId", containCodeId);
                        String[] split = containCodeId.split(",");
                        List<String> containFiledIdList = Arrays.asList(split);
                        String cmdbCodes = relationMapper.findCmdbCodeByIdList(containFiledIdList);
                        paramMap.put("containFiled", cmdbCodes);
                    }
                    paramsList.add(paramMap);
                }
            }
            returnMap.put("query", paramsList);
            String index , type;
            // 根据类型查询模型分组编码及模型编码
            Module module = moduleService.getModuleDetail(setting.getModuleId());
            index = module.getModuleCatalog().getCatalogCode();
            type = module.getCode();
            // 设置索引信息
            returnMap.put("index", index);
            returnMap.put("type", type);
            // 设置返回值
            List<CmdbV3CondicationReturnRelation> returnRelationList = setting.getReturnRelationList();
            if (returnRelationList != null && returnRelationList.size() > 0) {
                Map<String, String> resultMap = new HashMap<>();
                resultMap.put("id", "text");
                for (CmdbV3CondicationReturnRelation returnRelation : returnRelationList) {
                    if (returnRelation.getCmdbCode() != null) {
                        resultMap.put(returnRelation.getCmdbCode().getFiledCode(), returnRelation.getCmdbCode().getControlType().getControlCode());
                    }
                }
                returnMap.put("result", resultMap);
            }
            // 设置所有的模型字段
            List<CmdbCode> moduleCodeList = codeService.getCodeListByModuleId(module.getId());
            if (moduleCodeList != null && moduleCodeList.size() > 0) {
                Map<String, String> codeMap = new HashMap();
                for (CmdbCode cmdbCode : moduleCodeList) {
                    codeMap.put(cmdbCode.getFiledCode(), cmdbCode.getControlType().getControlCode());
                }
                returnMap.put("filed_list", codeMap);
            }
            // 设置排序信息
            List<CmdbV3CondicationSortRelation> sortRelationList = setting.getSortRelationList();
            if (sortRelationList !=null && sortRelationList.size() > 0) {
                List<Map<String, String>> returnList = new LinkedList<>();
                for (CmdbV3CondicationSortRelation sortRelation : sortRelationList) {
                    if (sortRelation.getCmdbCode() != null) {
                        Map<String, String> sortMap = new HashMap<>();
                        sortMap.put("filed", sortRelation.getCmdbCode().getFiledCode());
                        sortMap.put("type", sortRelation.getSortType());
                        returnList.add(sortMap);
                    }
                }
                returnMap.put("sort", returnList);
            }
            return returnMap;
        }
        return (new ObjectMapper()).convertValue(object, new TypeReference<Map>(){});
    }

    /**
     * 新增实例
     * @param entity 实例数据
     * @return
     */
    @Override
    public Map<String, String> insert(CmdbV3CondicationSetting entity) {
        Map<String, String> returnMap = new HashMap<>();
        // 验证编码是否重复
        CmdbV3CondicationSetting queryEntity = new CmdbV3CondicationSetting();
        queryEntity.setCondicationName(entity.getCondicationCode());
        CmdbV3CondicationSetting dbEntity = mapper.get(queryEntity);
        if (dbEntity != null) {
            returnMap.put("flag", "error");
            returnMap.put("msg", "查询条件编码已经存在, 新增失败.");
            return returnMap;
        }
        try {
            entity.setId(UUIDUtil.getUUID());
            this.batchInsertSetting(entity);
            this.batchInsertReturnSetting(entity);
            this.batchInsertSortSetting(entity);
            mapper.insert(entity);
            redisService.asyncRefresh(Constants.REDIS_TYPE_CONDITION_SETTING, entity);
        } catch (Exception e) {
            returnMap.put("flag", "error");
            returnMap.put("msg", "新增查询条件失败." + e.getMessage());
            return returnMap;
        }
        returnMap.put("flag", "success");
        return returnMap;
    }

    /**
     * 批量新增查询条件配置
     * @param entity
     */
    private void batchInsertSetting(CmdbV3CondicationSetting entity) {
        // 先删除之前存的码表关系
        relationMapper.deleteByCondicationSettingId(entity.getId());
        // 重新绑定关系
        List<CmdbV3CondicationSettingRelation> relationList = entity.getSettingRelationList();
        if (relationList != null && relationList.size() > 0) {
            relationList.forEach((relation) -> {
                relation.setId(UUIDUtil.getUUID());
                relation.setCondicationSettingId(entity.getId());
                relationMapper.insert(relation);
            });
        }
    }

    /**
     * 批量新增查询返回值配置
     * @param entity
     */
    private void batchInsertReturnSetting(CmdbV3CondicationSetting entity) {
        // 删除之前的返回值信息
        returnRelationMapper.deleteByCondicationSettingId(entity.getId());
        List<CmdbV3CondicationReturnRelation> returnRelationList = entity.getReturnRelationList();
        if (returnRelationList != null && returnRelationList.size() > 0) {
            returnRelationList.forEach((relation) -> {
                relation.setId(UUIDUtil.getUUID());
                relation.setCondicationSettingId(entity.getId());
                returnRelationMapper.insert(relation);
            });
        }
    }

    /**
     * 批量新增查询排序配置
     * @param entity
     */
    private void batchInsertSortSetting(CmdbV3CondicationSetting entity) {
        sortRelationMapper.deleteByCondicationSettingId(entity.getId());
        List<CmdbV3CondicationSortRelation> sortRelationList = entity.getSortRelationList();
        if (sortRelationList != null && sortRelationList.size() > 0) {
            sortRelationList.forEach((relation) -> {
                relation.setId(UUIDUtil.getUUID());
                relation.setCondicationSettingId(entity.getId());
                sortRelationMapper.insert(relation);
            });
        }
    }
    /**
     * 修改实例
     * @param entity 实例数据
     * @return
     */
    @Override
    public Map<String, String> update(CmdbV3CondicationSetting entity) {
        Map<String, String> returnMap = new HashMap<>();
        // 验证编码是否重复
        CmdbV3CondicationSetting queryEntity = new CmdbV3CondicationSetting();
        queryEntity.setCondicationName(entity.getCondicationCode());
        CmdbV3CondicationSetting dbEntity = mapper.get(queryEntity);
        if (dbEntity != null && !dbEntity.getId().equals(entity.getId())) {
            returnMap.put("flag", "error");
            returnMap.put("msg", "查询条件名称已经存在, 修改失败.");
            return returnMap;
        }
        try {
            this.batchInsertSetting(entity);
            this.batchInsertReturnSetting(entity);
            this.batchInsertSortSetting(entity);
            mapper.update(entity);
            redisService.asyncRefresh(Constants.REDIS_TYPE_CONDITION_SETTING, entity);
            // 新增处理tab管理关联主机模型的查询逻辑
            if("instance_list".equals(entity.getCondicationCode())) {
                redisService.asyncRefresh(Constants.REDIS_TYPE_MODULE_TAB, entity);
            }
        } catch (Exception e) {
            returnMap.put("flag", "error");
            returnMap.put("msg", "修改查询条件失败." + e.getMessage());
            return returnMap;
        }
        returnMap.put("flag", "success");
        return returnMap;
    }

    /**
     * 删除实例
     * @param entity 实例数据
     * @return
     */
    @Override
    public Map<String, String> delete(CmdbV3CondicationSetting entity) {
        Map<String, String> returnMap = new HashMap<>();
        try {
            // 先删除之前存的码表关系
            relationMapper.deleteByCondicationSettingId(entity.getId());
            returnRelationMapper.deleteByCondicationSettingId(entity.getId());
            sortRelationMapper.deleteByCondicationSettingId(entity.getId());
            mapper.delete(entity);
            redisService.asyncRefresh(Constants.REDIS_TYPE_CONDITION_SETTING, entity);
            returnMap.put("flag", "success");
        } catch (Exception e) {
            returnMap.put("flag", "error");
            returnMap.put("msg", "删除查询条件失败:" + e.getMessage());
        }
        return returnMap;
    }

    @Override
    public Result<CmdbV3CondicationSetting> getCondicationSettingList(CmdbV3CondicationSettingQuery settingQuery) {
        Integer count = mapper.getCondicationSettingListCount(settingQuery);
        List<CmdbV3CondicationSetting> list = mapper.getCondicationSettingList(settingQuery);
        return new Result<>(count, list);
    }




//    @Override
//    public List<Map<String, Object>> validCondication(CmdbV3CondicationSetting cdt, List<Map<String,Object>> params) {
//        cdt = getCdtInfo(cdt);
//        List<Map<String, Object>> filterParams = new ArrayList<>();
//        List<CmdbV3CondicationSettingRelation> relations = cdt.getSettingRelationList();
//        if (params == null) {
//            if (relations != null && relations.size() > 0) {
//                throw new RuntimeException("未传查询参数");
//            }
//            return filterParams;
//        }
//        // 存储字段值关系{filed:value}
//        Map<String, Object> filedMap = new HashMap<>();
//        // 存储字段符号关系{filedcode:operator}
//        Map<String, Object> filedOperatorMap = new HashMap<>();
//        for (Map<String, Object> p : params) {
//            filedMap.put(p.get("filed").toString(), p.get("value"));
//            if (StringUtils.isNotEmpty(p.get("operator"))) {
//                filedOperatorMap.put(p.get("filed").toString(), p.get("operator"));
//            }
//        }
//        for (CmdbV3CondicationSettingRelation relation : relations) {
//            Map<String, Object> tempMap = new HashMap<>();
//            String cdtKey = relation.getCmdbCode().getFiledCode();
//            CmdbSimpleCode code = relation.getCmdbCode();
//            // 如果必填
//            if (relation.getIsRequire().equals("0")) {
//                // 没有传参且没有默认值
//                if(!filedMap.containsKey(cdtKey) && StringUtils.isEmpty(relation.getDefaultValue())) {
//                    throw new RuntimeException("未传参数：" + code.getFiledName() + "!");
//                }
//                // 没有传参且有默认值
//                if (!filedMap.containsKey(cdtKey) && StringUtils.isNotEmpty(relation.getDefaultValue())){
//                    tempMap = new HashMap<>();
//                    tempMap.put("filed", cdtKey);
//                    tempMap.put("value", relation.getDefaultValue());
//                    tempMap.put("operator", "=");
//                    filterParams.add(tempMap);
//                }
//            } else if (filedMap.containsKey(cdtKey)){
//                // 如果非必填，并且有传参
//                tempMap = new HashMap<>();
//                tempMap.put("filed", cdtKey);
//                tempMap.put("value", filedMap.get(cdtKey));
//                tempMap.put("operator", StringUtils.isNotEmpty(filedOperatorMap.get(cdtKey)) ? filedOperatorMap.get(cdtKey) :"=");
//                filterParams.add(tempMap);
//            }
//        }
//        return filterParams;
//    }

    @Override
    public Map<String, Object> parseQuery(Map<String, Object> params) {
        if (!params.containsKey("token") || params.get("token") == null || StringUtils.isEmpty(params.get("token").toString())) {
            log.error("Can't find authentication token.");
            throw new RuntimeException("Authentication failed.");
        }
        long startTime = new Date().getTime();
        String token = params.get("token").toString();
        String accessUserId;
        // 跳过token认证
        if (!innerUserId.equals(token)) {
            if (!params.containsKey("condicationCode") || params.get("condicationCode") == null
                    || StringUtils.isEmpty(params.get("condicationCode").toString())) {
                log.error("Can't find query parameter[condicationCode].");
                throw new RuntimeException("Query parameter[condicationCode] is required.");
            }
            CmdbV3AccessUser accessUser = userService.getUserByToken(token);
            if (accessUser == null) {
                log.error("Invalid authentication token -> {}", token);
                throw new RuntimeException("Authentication failed.");
            }
            accessUserId = accessUser.getId();
        } else {
            accessUserId = innerUserId;
        }
        String condicationCode = params.get("condicationCode").toString();
        Map<String, Object> settingMap;
        try {
            settingMap = getSettingByCodeAndAccessUserId(condicationCode, accessUserId);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException("Check token grant error. Please check it.");
        }
        Map<String, Object> returnMap = new HashMap<>();
        if (settingMap.containsKey("query")) {
            List<Map<String, Object>> settingParams = (List<Map<String, Object>>) settingMap.get("query");
            for (Map<String, Object> settingParam : settingParams) {
                String filed = settingParam.get("filed").toString();
                // 如果是内部接口, 则不显示必填性
                if (!accessUserId.equals(innerUserId)) {
                    // 必填
                    if ((Boolean) settingParam.get("require")) {
                        if (!params.containsKey(filed)) {
                            throw new RuntimeException("Query parameter[" + filed + "] is required.");
                        }
                    }
                }
                Object paramValue = params.get(filed);
                if (StringUtils.isNotEmpty(paramValue)) {
                    String operator = settingParam.get("operator").toString();
                    if (operator.equalsIgnoreCase("in")
                            || operator.equalsIgnoreCase("not in")
                            || operator.equalsIgnoreCase("between")) {
                        if (paramValue instanceof List) {
                            settingParam.put("value", paramValue);
                        } else {
                            String[] temArray = String.valueOf(paramValue).split("\\,");
                            settingParam.put("value", Arrays.asList(temArray));
                        }
                    } else if (operator.equalsIgnoreCase("contain")) {
                        settingParam.put("value", paramValue);
                        settingParam.put("containFiled", settingParam.get("containFiled"));
                    } else {
                        settingParam.put("value", paramValue);
                    }
                }
            }
            returnMap.put("params", settingParams);
        }
        if (!settingMap.containsKey("moduleId")) {
            throw new RuntimeException("查询条件未配置模型信息. 请检查条件[" + condicationCode + "]");
        }
        String moduleId = settingMap.get("moduleId").toString();
        // 根据类型查询模型分组编码及模型编码
        if (params.containsKey("device_type") && StringUtils.isNotEmpty(params.get("device_type")) && params.get("device_type").toString().indexOf(",") == -1) {
            String deviceType = params.get("device_type").toString();
            Map<String, String> codeInfo = moduleService.getModuleCodeAndCatalogCodeByDeviceType(deviceType);
            if (codeInfo != null) {
                moduleId = codeInfo.get("id");
            }
        }
        // 如果有传递module_id, 则使用传递的module_id作为查询条件.
        if (params.containsKey("module_id") && StringUtils.isNotEmpty(params.get("module_id"))) {
            moduleId = params.get("module_id").toString();
        }
        Module module = moduleService.getModuleDetail(moduleId);
        if (module == null) {
            throw new RuntimeException("Invalid module id [" + moduleId + "]");
        } else {
            returnMap.put("index", module.getModuleCatalog().getCatalogCode());
            returnMap.put("type", module.getCode());
            returnMap.put("query_module_id", moduleId);
        }
        if (settingMap.containsKey("result")) {
            returnMap.put("result", settingMap.get("result"));
        }
        if (params.containsKey("sort")) {
            try {
                List<Map<String, Object>> sortList = (List<Map<String, Object>>) params.get("sort");
                for (Map<String, Object> sort : sortList) {
                    if (!sort.containsKey("filed") && !StringUtils.isNotEmpty(sort.get("filed"))) {
                       continue;
                    }
                    if (!sort.containsKey("type") && !StringUtils.isNotEmpty(sort.get("type"))) {
                       continue;
                    }
                }
                returnMap.put("sort", params.get("sort"));
            } catch (Exception e) {
                log.error("Parse params.sort error. -> {}", e.getMessage());
            }
        } else if (settingMap.containsKey("sort")) {
            returnMap.put("sort", settingMap.get("sort"));
        }
        if (settingMap.containsKey("filed_list")) {
            returnMap.put("filed_list", settingMap.get("filed_list"));
        }
        if (params.containsKey("pageSize")) {
            try {
                returnMap.put("pageSize", params.get("pageSize"));
            } catch (Exception e) {
                throw new RuntimeException("Query parameter[pageSize] is invalid.");
            }
        } else {
            returnMap.put("pageSize", 50);
        }
        if (params.containsKey("currentPage")) {
            try {
                returnMap.put("currentPage", params.get("currentPage"));
            } catch (Exception e) {
                throw new RuntimeException("Query parameter[currentPage] is invalid.");
            }
        } else {
            returnMap.put("currentPage", 1);
        }
        log.info("=== #1.组装查询条件耗时 {}s=====================", (new Date().getTime() - startTime) / 1000);
        return returnMap;
    }

    @Override
    public List<CmdbV3CondicationSetting> getConditionListByCodeId(String codeId) {
        return mapper.getConditionListByCodeId(codeId);
    }

    private CmdbV3CondicationSetting getCdtInfo(CmdbV3CondicationSetting cdt) {
        if (null == cdt) {
            throw new RuntimeException("未提供条件信息！");
        }
        // 如果通过配置条件和配置类型有一个没有则报错，或者通过id查
        if (StringUtils.isNotEmpty(cdt.getId())) {
            CmdbV3CondicationSetting c = new CmdbV3CondicationSetting();
            c.setId(cdt.getId());
            cdt = mapper.get(c);
        } else {
            if (StringUtils.isEmpty(cdt.getCondicationName()) || StringUtils.isEmpty(cdt.getCondicationType())) {
                throw new RuntimeException("未提供条件信息！请提供条件id或者条件名称和条件类型！");
            }
            cdt = mapper.get(cdt);
        }
        if (cdt == null || StringUtils.isEmpty(cdt.getId())) {
            throw new RuntimeException("未查到相应条件信息!");
        }
        return cdt;
    }

    @Override
    public JSONObject validConditionUnique(String code, String name) {
        Integer count = mapper.validConditionUnique(code,name);
        JSONObject result = new JSONObject();
        if(count > 0) {
            result.put("flag","false");
            result.put("msg","数据已有重复,请更改");
        } else {
            result.put("flag","true");
            result.put("msg","数据可用");
        }
        return result;
    }
}
