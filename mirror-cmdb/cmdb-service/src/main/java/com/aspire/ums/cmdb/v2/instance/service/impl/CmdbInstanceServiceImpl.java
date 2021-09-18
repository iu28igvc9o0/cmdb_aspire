package com.aspire.ums.cmdb.v2.instance.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.aspire.ums.cmdb.cmic.service.ICmdbModuleEventService;
import com.aspire.ums.cmdb.code.payload.CmdbCode;
import com.aspire.ums.cmdb.code.payload.CmdbControlType;
import com.aspire.ums.cmdb.code.payload.CmdbModuleCodeGroup;
import com.aspire.ums.cmdb.code.payload.CmdbSimpleCode;
import com.aspire.ums.cmdb.collectApproval.payload.CmdbCollectApproval;
import com.aspire.ums.cmdb.common.Constants;
import com.aspire.ums.cmdb.common.Result;
import com.aspire.ums.cmdb.dict.payload.ConfigDict;
import com.aspire.ums.cmdb.dict.service.ConfigDictService;
import com.aspire.ums.cmdb.helper.JDBCHelper;
import com.aspire.ums.cmdb.instance.payload.*;
import com.aspire.ums.cmdb.module.payload.Module;
import com.aspire.ums.cmdb.module.payload.SimpleModule;
import com.aspire.ums.cmdb.schema.service.SchemaService;
import com.aspire.ums.cmdb.sqlManage.CmdbSqlManage;
import com.aspire.ums.cmdb.sync.payload.CmdbOptType;
import com.aspire.ums.cmdb.sync.service.producer.CmdbModuleProducerServiceImpl;
import com.aspire.ums.cmdb.util.StringUtils;
import com.aspire.ums.cmdb.util.UUIDUtil;
import com.aspire.ums.cmdb.v2.code.service.ICmdbCodeService;
import com.aspire.ums.cmdb.v2.code.service.ICmdbControlTypeService;
import com.aspire.ums.cmdb.v2.collect.service.CmdbCollectApprovalService;
import com.aspire.ums.cmdb.v2.idc.entity.CmdbIdcManager;
import com.aspire.ums.cmdb.v2.idc.service.ICmdbIdcManagerService;
import com.aspire.ums.cmdb.v2.instance.handler.AbstractInstanceInsertFactory;
import com.aspire.ums.cmdb.v2.instance.mapper.CmdbInstanceMapper;
import com.aspire.ums.cmdb.v2.instance.service.ICmdbInstanceIpManagerService;
import com.aspire.ums.cmdb.v2.instance.service.ICmdbInstanceService;
import com.aspire.ums.cmdb.v2.module.CmdbConst;
import com.aspire.ums.cmdb.v2.module.service.ModuleService;
import com.aspire.ums.cmdb.v2.room.entity.CmdbRoomManager;
import com.aspire.ums.cmdb.v2.room.service.ICmdbRoomManagerService;
import com.aspire.ums.cmdb.v3.code.payload.CmdbV3CodeApprove;
import com.aspire.ums.cmdb.v3.code.payload.CmdbV3CodeBindSource;
import com.aspire.ums.cmdb.v3.code.service.ICmdbV3CodeApproveService;
import com.aspire.ums.cmdb.v3.condication.service.ICmdbV3CondicationSettingService;
import com.aspire.ums.cmdb.v3.config.payload.CmdbConfig;
import com.aspire.ums.cmdb.v3.config.service.ICmdbConfigService;
import com.aspire.ums.cmdb.v3.es.service.ICmdbESService;
import com.aspire.ums.cmdb.v3.module.event.EventConst;
import com.aspire.ums.cmdb.v3.module.payload.CmdbV3ModuleCatalog;
import com.aspire.ums.cmdb.v3.module.service.ICmdbV3ModuleCatalogService;
import com.aspire.ums.cmdb.v3.module.service.ICmdbV3ModuleCodeSettingService;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.map.LinkedMap;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 描述：
 * 
 * @author
 * @date 2019-05-20 20:56:07
 */
@Service
@Slf4j
public class CmdbInstanceServiceImpl implements ICmdbInstanceService {

    @Autowired
    private CmdbInstanceMapper mapper;

    @Autowired
    private ICmdbInstanceIpManagerService ipManagerService;

    @Autowired
    private ModuleService moduleService;

    @Autowired
    private ICmdbIdcManagerService idcManagerService;

    @Autowired
    private CmdbCollectApprovalService approvalService;

    @Autowired
    private ICmdbRoomManagerService roomManagerService;

    @Autowired
    private ConfigDictService configDictService;

    @Autowired
    private ICmdbV3CondicationSettingService condctService;

    @Autowired
    private ICmdbESService cmdbESService;

    @Autowired
    private ICmdbCodeService codeService;

    @Autowired
    private ICmdbV3CodeApproveService codeApproveService;

    @Autowired
    private ICmdbControlTypeService controlTypeService;

    @Autowired
    private ICmdbV3ModuleCodeSettingService codeSettingService;

    @Autowired
    private ICmdbV3ModuleCatalogService catalogService;

    @Autowired
    private SchemaService schemaService;

    @Autowired
    private ICmdbModuleEventService moduleEventService;

    @Autowired
    private JDBCHelper jdbcHelper;

    @Autowired
    private ICmdbConfigService cmdbConfigService;

    @Value("${cmdb.enable.modifyApproval: true}")
    private String modifyApproval;

    @Value("${cmdb.access.inner}")
    private String innerUserId;

    @Value("${cmdb.query.db:mysql}")
    private String queryDbType;

    @Autowired
    private CmdbModuleProducerServiceImpl cmdbModuleProducerService;

    @Autowired
    private PlatformTransactionManager transactionManager;

    /**
     * 获取所有实例
     *
     * @return 返回所有实例数据
     */
    @Override
    public List<CmdbInstance> list() {
        return mapper.list();
    }

    @Override
    public List<CmdbInstance> listByEntity(CmdbInstance query) {
        return mapper.listByEntity(query);
    }

    /**
     * 根据主键ID 获取数据信息
     * 
     * @param id
     *            实例信息
     * @return 返回指定ID的数据信息
     */
    @Override
    public List<Map<String, Object>> getById(String id) {
        return mapper.getByInsId(id);
    }

    /**
     * 根据主键ID 获取数据信息
     * 
     * @param entity
     *            实例信息
     * @return 返回指定ID的数据信息
     */
    @Override
    public CmdbInstance get(CmdbInstance entity) {
        return mapper.get(entity);
    }

    /**
     * 根据参数获取数据
     *
     * @param params
     *            实例信息
     * @return 返回指定ID的数据信息
     */
    @Override
    public Map<String, Object> getByParams(String moduleId, Map<String, Object> params) {
        String sql = moduleService.getModuleQuerySQL(moduleId);
        // String querySQL = "select * from (" + sql + ") res where res.suyan_uuid=#{instanceId}";
        StringBuilder whereSql = new StringBuilder("where 1=1");
        for (String filed : params.keySet()) {
            whereSql.append(" and ").append(filed).append(" = #{").append(filed).append("}");
        }
        CmdbSqlManage sqlManage = new CmdbSqlManage(sql + whereSql.toString(), moduleId, Constants.INSTANCE_AUTH_MODULE,
                Constants.NEED_AUTH);
        List<Map<String, Object>> resultList = jdbcHelper.getQueryList(sqlManage, null, null, null, params);
        if (resultList.size() > 0) {
            return resultList.get(0);
        } else {
            return new HashMap<>();
        }

    }

    @Override
    public Map<String, Object> getInstanceDetail(String moduleId, String instanceId) {
        Module module = moduleService.getModuleDetail(moduleId);
        if (module == null) {
            throw new RuntimeException("Can't find module record. module id -> " + moduleId);
        }
        if (queryDbType != null && queryDbType.toLowerCase(Locale.ENGLISH).equals("es")) {
            return cmdbESService.getById(instanceId, module.getModuleCatalog().getCatalogCode(), module.getCode());
        }
        return getInstanceById(moduleId, instanceId);
    }

    private Map<String, Object> getInstanceById(String moduleId, String instanceId) {
        String querySQL = moduleService.getModuleQuerySQL(moduleId);
        querySQL = "select * from (" + querySQL + ") res where res.id=#{instanceId}";
        Map<String, Object> params = new HashMap<>();
        params.put("instanceId", instanceId);
        CmdbSqlManage cmdbSqlManage = new CmdbSqlManage(querySQL, moduleId, Constants.INSTANCE_AUTH_MODULE, Constants.NEED_AUTH);
        List<Map<String, Object>> returnList = jdbcHelper.getQueryList(cmdbSqlManage, null, null, null, params);
        if (returnList != null && returnList.size() > 0) {
            return returnList.get(0);
        }
        return new HashMap<>();
    }

    /**
     * 新增实例信息入审核库
     * 
     * @param approvalList
     *            实例数据
     * @return
     */
    private void addToApproval(List<CmdbCollectApproval> approvalList) {
        approvalService.insertByBatch(approvalList);
    }

    /**
     * 新增设备唯一校验
     * 
     * @param moduleId
     *            模型信息
     * @param instanceData
     *            实例信息
     */
    private void checkUnique(String moduleId, Map<String, Object> instanceData) {
        // 查数据库是否存在
        Map<String, Object> dbData = moduleService.getModuleDataByPrimarys(moduleId, instanceData);
        if (dbData != null) {
            throw new RuntimeException("Input data is already exists, Please check it.");
        }
    }

    /**
     * 新增实例信息入审核库
     * 
     * @param instanceData
     *            实例数据
     * @return
     */
    @Override
    public CmdbCollectApproval handleAddApproval(String username, Map<String, Object> instanceData, String operatorType) {
        long startTime = System.currentTimeMillis();
        toCheckUnique(instanceData.get("module_id").toString(), instanceData);
        log.info(">>>>>> 2.1 校验唯一性 耗时:{}", System.currentTimeMillis() - startTime);
        startTime = System.currentTimeMillis();
        Module module = moduleService.getModuleDetail(instanceData.get("module_id").toString());
        Map<String, List<Map<String, String>>> moduleCodeMap = new HashMap<>();
        StringBuilder currValue = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        for (String key : instanceData.keySet()) {
            params.put(key, instanceData.get(key));
        }
        params.put("customerType", "approve");
        String instanceId = UUIDUtil.getUUID();
        params.put("id", instanceId);
        String querySQL = moduleService.getModuleQuerySQL(module.getId());
        querySQL = "select * from (" + querySQL + ") res where res.id=#{id}";
        CmdbSqlManage cmdbSqlManage = new CmdbSqlManage(querySQL, Constants.INSTANCE_AUTH_MODULE, Constants.UN_NEED_AUTH);
        List<Map<String, Object>> returnList = jdbcHelper.getQueryList(cmdbSqlManage, null, null, null, params);
        if (returnList == null || returnList.size() == 0) {
            throw new RuntimeException("查询新增设备对应中文信息失败");
        }
        log.info(">>>>>> 2.2 查询新增设备对应中文信息 耗时:{}", System.currentTimeMillis() - startTime);
        startTime = System.currentTimeMillis();
        // 新增数据内容
        Map<String, Object> instanceDetail = returnList.get(0);
        Map<String, Map<String, String>> columnInfo = moduleService.getModuleColumns(module.getId());
        log.info(">>>>>> 2.3 查询模型列信息 耗时:{}", System.currentTimeMillis() - startTime);
        startTime = System.currentTimeMillis();
        log.info(">>>>>> 2.4 查询模型下码表信息 耗时:{}", System.currentTimeMillis() - startTime);
        startTime = System.currentTimeMillis();

        for (CmdbModuleCodeGroup group : module.getGroupList()) {
            for (CmdbCode code : group.getCodeList()) {
                String key = code.getCodeSetting().getOwnerModuleId();
                if (!moduleCodeMap.containsKey(key)) {
                    moduleCodeMap.put(key, new ArrayList<>());
                }
                if (StringUtils.isNotEmpty(instanceData.get(code.getFiledCode()))) {
                    String codeValue = "";
                    if (columnInfo.containsKey(code.getFiledCode())
                            && "ref".equals(columnInfo.get(code.getFiledCode()).get("type"))) {
                        log.debug("code.getFiledCode()=={}", code.getFiledCode());
                        log.debug("columnInfo.get(code.getFiledCode()).get(\"ref_name\")=={}",
                                columnInfo.get(code.getFiledCode()).get("ref_name"));
                        Object codeObj = instanceDetail.get(columnInfo.get(code.getFiledCode()).get("ref_name"));
                        if (codeObj != null) {
                            codeValue = codeObj.toString();
                        }

                    } else {
                        codeValue = instanceDetail.get(code.getFiledCode()).toString();
                    }
                    validCode(code, codeValue);
                    currValue.append(code.getFiledName());
                    currValue.append("->");
                    currValue.append(codeValue != null ? codeValue.trim() : codeValue);
                    currValue.append(",");
                } else {
                    continue;
                }
            }
        }
        log.info(">>>>>> 2.5 字段处理 耗时:{}", System.currentTimeMillis() - startTime);
        // 拼接源数据
        CmdbCollectApproval approval = new CmdbCollectApproval();
        approval.setInstanceId(instanceId);
        approval.setId(UUIDUtil.getUUID());
        approval.setOperatorTime(new Date());
        approval.setOperator(username);
        approval.setApprovalType("add");
        approval.setOperaterType(StringUtils.isNotEmpty(operatorType) ? operatorType : "手动新增");
        approval.setApprovalStatus(0);
        approval.setModuleId(module.getId());
        approval.setResourceData(JSONObject.toJSONString(instanceData));
        approval.setCurrValue(currValue.deleteCharAt(currValue.length() - 1).toString());
        return approval;
    }


    private void toCheckUnique(String module_id, Map<String, Object> instanceData) {
        CmdbConfig config = cmdbConfigService.getConfigByCode("exclude_check_primary_module");
        boolean needCheckUnique = true;
        if (config != null) {
            List<String> excludeCheckModule = Arrays.asList(config.getConfigValue().split(","));
            if (excludeCheckModule.contains(module_id)) {
                needCheckUnique = false;
            }
        }
        if (needCheckUnique) {
            checkUnique(module_id, instanceData);
        }
    }

    @Override
    public void validCode(CmdbCode code, String codeValue) {
        long startTime = System.currentTimeMillis();
        // 校验数据长度
        Integer codeLength = StringUtils.isNotEmpty(code.getCodeLength()) ? code.getCodeLength() : Constants.CODE_DEFAULT_LENGTH;
        if (codeValue.length() > codeLength) {
            throw new RuntimeException(
                    "the " + code.getFiledName() + " length is to small. You must change code length >= " + codeValue.length());
        }
        startTime = System.currentTimeMillis();
        // 验证数据是否合格
        Map<String, Object> codeParam = new HashMap<>();
        codeParam.put(code.getCodeId(), codeValue);
        Map<String, Map<String, String>> validResults = codeService.validCodeValue(codeParam);
        if (validResults.keySet().contains(code.getCodeId())) {
            Map<String, String> validResult = validResults.get(code.getCodeId());
            if ("error".equals(validResult.get("flag"))) {
                throw new RuntimeException("码表[" + code.getFiledName() + "]数据验证不通过 " + validResult.get("msg"));
            }
        }
        log.info(">>>>>> ---- !!! 验证数据是否合格 耗时：{}", System.currentTimeMillis() - startTime);
    }

    @Override
    public Map<String, Object> queryInstanceDetail(Map<String, Object> params, String moduleType) {
        if (!params.containsKey("token")) {
            params.put("token", innerUserId);
        }
        if (!params.containsKey("condicationCode")) {
            params.put("condicationCode", "instance_detail");
        }
        if (!params.containsKey("module_id") && !params.containsKey("device_type")) {
            Module module = moduleService.getDefaultModule(moduleType);
            params.put("module_id", module.getId());
        }
        Map<String, Object> queryParams = condctService.parseQuery(params);
        if (queryDbType != null && queryDbType.toLowerCase(Locale.ENGLISH).equals("es")) {
            return cmdbESService.queryDetail(queryParams);
        }
        Object[] formatQuerys = this.formatStatementParams(queryParams);
        String moduleId = queryParams.get("query_module_id").toString();
        String querySQL = moduleService.getModuleQuerySQL(moduleId);
        CmdbSqlManage sqlManage = new CmdbSqlManage(querySQL, moduleId, Constants.INSTANCE_AUTH_MODULE, Constants.NEED_AUTH);
        return jdbcHelper.getQueryMap(sqlManage, (String) formatQuerys[0], (Map<String, Object>) formatQuerys[1]);
    }

    /**
     * 获取CI详细信息
     * 
     * @param params
     *            查询设置 包含: { "params": [{ // 查询参数设置 "operator": "操作符", "filed": "字段名称", "value": "字段值", "filed_type": "字段控件类型" }],
     *            "query_module_id": "查询的模型ID", "result": [] //返回结果 "index": "", //索引名称 "type": "" //类型 }
     * @return 具体实例数据
     */
    @Override
    public Map<String, Object> getInstanceByPrimaryKey(Map<String, Object> params) {
        if (queryDbType != null && queryDbType.toLowerCase(Locale.ENGLISH).equals("es")) {
            com.aspire.mirror.elasticsearch.api.dto.cmdb.Result<Map<String, Object>> returnResult = cmdbESService.list(params);
            if (returnResult == null) {
                return new HashMap<>();
            } else if (returnResult.getCount() > 1) {
                throw new RuntimeException("Too many result to be find. Except one. But find " + returnResult.getCount());
            } else if (returnResult.getCount() == 0) {
                return new HashMap<>();
            }
            return returnResult.getData().get(0);
        } else {
            Object[] formatQuerys = this.formatStatementParams(params);
            String moduleId = params.get("query_module_id").toString();
            String querySQL = moduleService.getModuleQuerySQL(moduleId);
            CmdbSqlManage sqlManage = new CmdbSqlManage(querySQL, moduleId, Constants.INSTANCE_AUTH_MODULE, Constants.NEED_AUTH);
            return jdbcHelper.getQueryMap(sqlManage, (String) formatQuerys[0], (Map<String, Object>) formatQuerys[1]);
        }
    }

    @Override
    public Map<String, Object> queryDeviceByIdcTypeAndIP(Map<String, Object> params) {
        if (!params.containsKey("token")) {
            params.put("token", innerUserId);
        }
        if (!params.containsKey("condicationCode")) {
            params.put("condicationCode", "instance_by_idc_ip");
        }
        if (!params.containsKey("ip")) {
            throw new RuntimeException("未传ip");
        }
        if (params.containsKey("idcType") && params.containsKey("is_cn") && (boolean) params.get("is_cn")) {
            Map<String, String> idc = configDictService.getIdcTypeByName(params.get("idcType").toString());
            if (idc == null) {
                throw new RuntimeException("未知资源池：" + params.get("idcType").toString());
            }
            params.put("idcType", idc.get("id"));
        }
        Map<String, Object> returnMap = new HashMap<>();
        if (queryDbType != null && queryDbType.toLowerCase(Locale.ENGLISH).equals("es")) {
            List<Map<String, Object>> result = cmdbESService.queryDeviceByIpAndIdcType(condctService.parseQuery(params));
            if (result != null && result.size() > 0) {
                returnMap = result.get(0);
            }
        } else {
            Map<String, Object> queryParams = condctService.parseQuery(params);
            // String ip = params.get("ip").toString();
            // String idcType = StringUtils.isNotEmpty(params.get("idcType")) ? params.get("idcType").toString() : "";
            Result<Map<String, Object>> mapResult = getInstanceList(queryParams);
            if (mapResult.getTotalSize() != 1) {
                log.error("Get result error. Find {} record. Query params -> {}", mapResult.getTotalSize(), params);
                return null;
            }
            return mapResult.getData().get(0);
        }
        return returnMap;
    }

    /**
     * 获取模型的码表信息
     * 
     * @param moduleId
     *            模型ID
     * @param moduleType
     *            模型类型
     * @return
     */
    @Override
    public List<CmdbSimpleCode> getInstanceHeader(String moduleId, String moduleType) {
        Module module;
        if (StringUtils.isNotEmpty(moduleId)) {
            module = moduleService.getModuleDetail(moduleId);
        } else {
            module = moduleService.getDefaultModule(moduleType);

        }
        if (module == null) {
            throw new RuntimeException("Can't find module record. moduleId -> " + moduleId + " moduleType -> " + moduleType);
        }
        return codeService.getSimpleCodeListByModuleId(module.getId());
    }

    @Override
    public List<Map<String, Object>> exportInstanceList(Map<String, Object> params, String moduleType) {
        if (!params.containsKey("token")) {
            params.put("token", innerUserId);
        }
        if (!params.containsKey("condicationCode")) {
            params.put("condicationCode", "instance_list");
        }
        Module module = new Module();
        if (!params.containsKey("module_id") && !params.containsKey("device_type")) {
            module = moduleService.getDefaultModule(moduleType);
            params.put("module_id", module.getId());
        } else {
            module = moduleService.getModuleDetail(params.get("module_id").toString());
        }
        List<Map<String, Object>> returnList = new LinkedList<>();
        Integer currentPage = 0, pageSize = 100;
        Map<String, Object> queryParams = condctService.parseQuery(params);
        queryParams.put("pageSize", null);
        queryParams.put("currentPage", null);
        Result<Map<String, Object>> pageResult = this.getInstanceList(params, moduleType);

        // 请求es数据
        // log.info("Request es params -> {}", queryParams);
        // com.aspire.mirror.elasticsearch.api.dto.cmdb.Result<Map<String, Object>> temp = cmdbESService.list(queryParams);
        // Integer totalCount = temp.getCount();
        // Integer totalPageSize = totalCount % pageSize == 0 ? (totalCount / pageSize) : (totalCount / pageSize + 1);
        // while (totalCount > 0 && currentPage <= totalPageSize) {
        // currentPage++;
        // queryParams.put("currentPage", currentPage);
        // queryParams.put("pageSize", pageSize);
        // log.info("Request es params -> {}", queryParams);
        // com.aspire.mirror.elasticsearch.api.dto.cmdb.Result<Map<String, Object>> temp1 = cmdbESService.list(queryParams);
        // if (temp1.getData() != null) {
        // returnList.addAll(temp1.getData());
        // }
        // }
        return returnList;
    }

    @Override
    public String addInstance(String userName, Map<String, Object> instanceData, String operateType) {
        long startTime = System.currentTimeMillis();
        String msg = "";
        SimpleModule module = validModule(instanceData);
        // 针对特殊模型的新增处理, 走指定的特殊处理类
        CmdbConfig specialInsert = cmdbConfigService.getConfigByCode("module_insert_handler:" + module.getId());
        if (specialInsert != null) {
            Map<String, Object> returnMap = specialHandler(specialInsert.getConfigValue(), userName, instanceData, operateType);
            return returnMap.get("msg").toString();
        }
        log.info(">>>>>> 1.针对特殊模型的新增处理 耗时：{}", System.currentTimeMillis() - startTime);
        startTime = System.currentTimeMillis();
        log.info(">>>>>> 2.开始处理数据处理成审核格式");
        CmdbCollectApproval approval = handleAddApproval(userName, instanceData, operateType);
        log.info(">>>>>> 2.数据处理成审核格式结束 总耗时：{}", System.currentTimeMillis() - startTime);
        startTime = System.currentTimeMillis();
        // 从界面调用此接口是需要进行判断是否审核的
        if (module.getEnableApprove() != null && module.getEnableApprove() == 0) {
            // 不用审核
            approval.setApprovalUser(userName);
            log.info(">>>>>> 3.开始审核数据");
            msg = this.insert(userName, approval);
            log.info(">>>>>> 3.审核数据结束 总耗时：{}", System.currentTimeMillis() - startTime);
        } else {
            // 添加到配置审核库
            List<CmdbCollectApproval> approvals = new ArrayList<>();
            approvals.add(approval);
            approvalService.insertByBatch(approvals);
            msg = "已成功录入配置审核，请联系管理里进行审核";
            log.info(">>>>>> 4.入审核库 耗时：{}", System.currentTimeMillis() - startTime);
        }
        return msg;
    }

    /**
     * 针对特殊的新增处理逻辑
     * 
     * @param handlerClass
     *            处理类 必须实现
     * @param userName
     *            新增用户
     * @param instanceData
     *            实例数据
     * @param operateType
     *            操作方式
     * @return 返回状态信息
     */
    private Map<String, Object> specialHandler(String handlerClass, String userName, Map<String, Object> instanceData,
            String operateType) {
        Map<String, Object> returnMap = new HashMap<>();
        try {
            Class clz = Class.forName(handlerClass);
            Constructor constructor = clz.getConstructor();
            AbstractInstanceInsertFactory factory = (AbstractInstanceInsertFactory) constructor.newInstance();
            return factory.execute(userName, instanceData, operateType);
        } catch (ClassNotFoundException e) {
            log.error("无法找到处理类 -> {}", handlerClass);
            returnMap.put("flag", "error");
            returnMap.put("msg", "无法找到处理类");
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | InstantiationException e) {
            log.error("新增网段异常.", e);
            returnMap.put("flag", "error");
            returnMap.put("msg", "无法找到处理类");
        }
        return returnMap;
    }

    @Override
    public String addInstanceNoApprove(String userName, Map<String, Object> instanceData, String operateType) {
        String msg = "";
        validModule(instanceData);
        CmdbCollectApproval approval = handleAddApproval(userName, instanceData, operateType);
        approval.setApprovalUser(userName);
        msg = this.insert(userName, approval);
        return msg;
    }

    private SimpleModule validModule(Map<String, Object> instanceData) {
        if (!instanceData.containsKey("module_id")) {
            throw new RuntimeException("Params[module_id] is require.");
        }
        String moduleId = instanceData.get("module_id").toString();
        SimpleModule module = moduleService.getSimpleModuleDetail(moduleId);
        if (module == null) {
            throw new RuntimeException("can't find module record. module_id: " + moduleId);
        }
        return module;
    }

    @Override
    public String updateInstance(String id, String userName, Map<String, Object> instanceData, String operateType) {
        String msg = "";
        SimpleModule module = validModule(instanceData);
        // 将ID放到实例数据中
        instanceData.put("id", id);
        List<CmdbCollectApproval> approvalList = handleUpdateApproval(userName, instanceData, operateType);
        if (approvalList.size() == 0) {
            msg = "未检测到配置项发生变更.";
        }
        // 从界面调用此接口是需要进行判断是否审核的
        else if (module.getEnableApprove() != null && module.getEnableApprove() == 0) {
            // 不用审核
            msg = this.update(userName, approvalList);
        } else {
            msg = this.updateToApproval(approvalList);
        }
        return msg;
    }

    private List<CmdbCollectApproval> handleUpdateApproval(String userName, Map<String, Object> instanceData, String operateType) {
        List<CmdbCollectApproval> approvals = new ArrayList<>();
        String moduleId = instanceData.get("module_id").toString();
        String instanceId = instanceData.get("id").toString();
        Map<String, Map<String, String>> columnInfo = moduleService.getModuleColumns(moduleId);
        Map<String, Object> instanceDataMap = Maps.newHashMap();
        for (Map.Entry<String, Object> entry : instanceData.entrySet()) {
            if (ImmutableList.of("module_id", "id").contains(entry.getKey())) {
                continue;
            }
            instanceDataMap.put(entry.getKey(), entry.getValue());
        }
        Map<String, Object> oldInstanceData = getInstanceDetail(moduleId, instanceId);
        oldInstanceData.put("module_id", moduleId);
        Map<String, Object> params = new HashMap<>();
        params.put("customerType", "approve");
        params.put("id", instanceId);
        for (String key : instanceDataMap.keySet()) {
            params.put(key, instanceDataMap.get(key));
        }
        String querySQL = moduleService.getModuleQuerySQL(moduleId);
        querySQL = "select * from (" + querySQL + ") res where res.id=#{id}";
        CmdbSqlManage cmdbSqlManage = new CmdbSqlManage(querySQL, Constants.INSTANCE_AUTH_MODULE, Constants.UN_NEED_AUTH);
        List<Map<String, Object>> returnList = jdbcHelper.getQueryList(cmdbSqlManage, null, null, null, params);
        if (returnList == null || returnList.size() == 0) {
            throw new RuntimeException("查询更新设备对应中文信息失败");
        }
        Map<String, Object> newInstanceData = returnList.get(0);
        Module module = moduleService.getModuleDetail(moduleId);
        for (CmdbModuleCodeGroup group : module.getGroupList()) {
            for (CmdbCode code : group.getCodeList()) {
                // 如果更新数据中不包含当前字段，例批量更新等只传部分数据过来
                if (!instanceDataMap.containsKey(code.getFiledCode())) {
                    continue;
                }
                // 新增判断，字段不可编辑则不做处理
//                if (null != code.getUpdateReadOnly() && 1 == code.getUpdateReadOnly()) {
//                    continue;
//                }
                String oldValue = "";
                String curValue = "";
                if (columnInfo.containsKey(code.getFiledCode()) && "ref".equals(columnInfo.get(code.getFiledCode()).get("type"))) {
                    String field = columnInfo.get(code.getFiledCode()).get("ref_name");
                    oldValue = oldInstanceData.get(field) == null ? "" : oldInstanceData.get(field).toString();
                    curValue = newInstanceData.get(field) == null ? "" : newInstanceData.get(field).toString();
                } else {
                    oldValue = StringUtils.isNotEmpty(oldInstanceData.get(code.getFiledCode()))
                            ? oldInstanceData.get(code.getFiledCode()).toString()
                            : "";
                    curValue = StringUtils.isNotEmpty(instanceDataMap.get(code.getFiledCode()))
                            ? instanceDataMap.get(code.getFiledCode()).toString()
                            : "";
                }
                CmdbCollectApproval approval = new CmdbCollectApproval();
                if (instanceDataMap.containsKey(code.getFiledCode()) && !oldValue.equals(curValue)) {
                    approval.setModuleId(oldInstanceData.get("module_id").toString());
                    approval.setOwnerModuleId(code.getCodeSetting().getOwnerModuleId());
                    approval.setCodeId(code.getCodeId());
                    approval.setOperaterType(operateType);
                    approval.setApprovalType("update");
                    approval.setInstanceId(instanceId);
                    approval.setOldValue(oldValue.trim());
                    approval.setCurrValue(curValue.trim());
                    approval.setOperator(userName);
                    approval.setOperatorTime(new Date());
                    Map<String, Object> resourceMap = new HashMap<>();
                    resourceMap.put(code.getFiledCode(), newInstanceData.get(code.getFiledCode()));
                    approval.setResourceData(JSONObject.toJSONString(resourceMap));
                    approvals.add(approval);
                }
            }
        }
        return approvals;
    }

    private CmdbCollectApproval handleDeleteApproval(String userName, Map<String, Object> instanceData, String operateType) {
        List<CmdbCollectApproval> approvals = new ArrayList<>();
        String moduleId = instanceData.get("module_id").toString();
        String instanceId = instanceData.get("id").toString();
        Map<String, Object> instanceDetail = getInstanceDetail(moduleId, instanceId);
        Module module = moduleService.getModuleDetail(moduleId);
        Map<String, CmdbV3CodeBindSource> refCodes = new HashMap<>();
        Map<String, Object> oldValue = new HashMap<>();
        Map<String, Map<String, String>> columnInfo = moduleService.getModuleColumns(moduleId);
        for (CmdbModuleCodeGroup group : module.getGroupList()) {
            for (CmdbCode code : group.getCodeList()) {
                if (columnInfo.containsKey(code.getFiledCode()) && "ref".equals(columnInfo.get(code.getFiledCode()).get("type"))) {
                    oldValue.put(code.getFiledCode(), instanceDetail.get(columnInfo.get(code.getFiledCode()).get("ref_name")));
                } else {
                    oldValue.put(code.getFiledCode(), instanceDetail.get(code.getFiledCode()));
                }
            }
        }
        CmdbCollectApproval approval = new CmdbCollectApproval();
        approval.setModuleId(moduleId);
        approval.setOwnerModuleId(moduleId);
        approval.setOperaterType(operateType);
        approval.setInstanceId(instanceId);
        approval.setApprovalType("delete");
        approval.setOldValue(oldValue.toString());
        approval.setOperator(userName);
        approval.setApprovalUser(userName);
        approval.setOperatorTime(new Date());
        approvals.add(approval);
        return approval;
    }

    /**
     * 新增实例
     *
     * @param approval
     *            实例数据
     * @return
     */
    @Override
    @Transactional(rollbackFor = { RuntimeException.class, Exception.class, SQLException.class })
    public String insert(String userName, CmdbCollectApproval approval) {
        Map<String, Object> instanceData = (Map<String, Object>) JSONObject.parse(approval.getResourceData());
        if (!instanceData.containsKey("module_id")) {
            throw new RuntimeException("Params[module_id] is require.");
        }
        String moduleId = instanceData.get("module_id").toString();
        // 生成UUID并覆盖id数据
        String instanceId = approval.getInstanceId();
        // 网管同步的id用网管的资产id
        Optional<Object> instance_id = Optional.ofNullable(instanceData.get("instance_id"));
        if (instance_id.isPresent()) {
            instanceId = instance_id.get().toString();
        }
        // 覆盖录入人和录入时间
        instanceData.put("insert_person", approval.getOperator());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        instanceData.put("insert_time", dateFormat.format(new Date()));
        // 覆盖最后更新人和最后更新时间
        instanceData.put("update_person", approval.getOperator());
        instanceData.put("update_time", dateFormat.format(new Date()));
        instanceData.put("id", instanceId);
        // 新增逻辑
        long startTime = System.currentTimeMillis();
        this.insertCiByNoValid(moduleId, instanceData);
        log.info(">>>>>> 3.1完整新增处理 耗时:{}", System.currentTimeMillis() - startTime);
        startTime = System.currentTimeMillis();
        // 增加新增回调事件
        Map<String, Object> handleData = new HashMap<>();
        List<CmdbCollectApproval> approvals = new ArrayList<>();
        approval.setApprovalStatus(1);
        approvals.add(approval);
        handleData.put("username", userName);
        handleData.put("approvals", approvals);
        handleData.put("operateType", EventConst.EVENT_TYPE_DATA_INSERT);
        moduleEventService.handlerModuleDataEvent(moduleId, instanceId, null, handleData, EventConst.EVENT_TYPE_DATA_INSERT);
        log.info(">>>>>> 3.2处理新增回调事件 耗时:{}", System.currentTimeMillis() - startTime);
        return "新增实例成功";
    }

    /**
     * 新增实例, 无IP及资源池信息校验. 注意情况下请调用上面的insert方法. 此方法目前提供给苏研云主机对应使用
     *
     * @param
     *
     */
    private void insertCiByNoValid(String moduleId, Map<String, Object> instanceData) {
        // 获取当前模型的主键，如果有相同则返回设备已存在
        long startTime = System.currentTimeMillis();
        toCheckUnique(moduleId, instanceData);
        log.info(">>>>>> 校验唯一性 耗时:{}", System.currentTimeMillis() - startTime);
        String instanceId = instanceData.get("id").toString();
        // 网管同步的id用网管的资产id
        Optional<Object> instance_id = Optional.ofNullable(instanceData.get("instance_id"));
        if (instance_id.isPresent()) {
            instanceId = instance_id.get().toString();
        }
        startTime = System.currentTimeMillis();
        Map<String, Object> handelResult = handleModuleData(instanceId, moduleId, instanceData);
        log.info(">>>>>> 处理设备数据 耗时:{}", System.currentTimeMillis() - startTime);
        Map<String, Map<String, Object>> moduleData = (Map<String, Map<String, Object>>) handelResult.get("moduleData");
        if (!moduleData.containsKey(moduleId)) {
            moduleData.put(moduleId, new HashMap<>());
        }
        List<CmdbInstanceIpManager> ipManagerList = (List<CmdbInstanceIpManager>) handelResult.get("ipManagerList");
        // 创建IP地址信息
        if (ipManagerList.size() > 0) {
            ipManagerService.insertByBatch(instanceId, ipManagerList);
        }
        // 创建其他模型
        startTime = System.currentTimeMillis();

        // 这里要用编程式事务
        DefaultTransactionDefinition transDefinition = new DefaultTransactionDefinition();
        // 设置事务的传播机制
        transDefinition.setPropagationBehavior(DefaultTransactionDefinition.PROPAGATION_REQUIRES_NEW);
        // 得到事务。根据事务的传播机制判断是新建事务还是用当前已有的事务
        TransactionStatus transStatus = transactionManager.getTransaction(transDefinition);
        Map<String, CmdbV3ModuleCatalog> catalogMap = Maps.newHashMap();
        try {
            // 创建其他模型
            for (String refModuleId : moduleData.keySet()) {
                // 每个模型都要存ci的id
                moduleData.get(refModuleId).put("id", instanceId);
                CmdbV3ModuleCatalog catalog = catalogService.getByModuleId(refModuleId);
                if (catalog == null) {
                    throw new RuntimeException("未查到引用模型Id[" + refModuleId + "]分组信息");
                }
                catalogMap.put(refModuleId, catalog);
                // 数据库插入数据
                if (moduleData.get(refModuleId).keySet().size() > 0) {
                    schemaService.insertCi(catalog.getCatalogCode(), moduleData.get(refModuleId));
                }
            }
            transactionManager.commit(transStatus);
        } catch (RuntimeException e) {
            transactionManager.rollback(transStatus);
            throw new RuntimeException(e);
        }

        // 同步到ES
        if (queryDbType != null && queryDbType.toLowerCase(Locale.ENGLISH).equals("es")) {
            for (Map.Entry<String, CmdbV3ModuleCatalog> entry : catalogMap.entrySet()) {
                String refModuleId = entry.getKey();
                CmdbV3ModuleCatalog catalog = entry.getValue();
                List<Map<String, Object>> esData = new ArrayList<>();
                esData.add(moduleData.get(refModuleId));
                SimpleModule refModule = moduleService.getSimpleModuleDetail(refModuleId);
                if (refModule == null) {
                    throw new RuntimeException("未查到引用模型Id[" + refModuleId + "]信息");
                }
                cmdbESService.insert(esData, catalog.getCatalogCode(), refModule.getCode());
            }
        }
        log.info(">>>>>> 新增设备数据 耗时:{}", System.currentTimeMillis() - startTime);

        // 网管同步过来的时候，调用次方法，不能再同步到网管.
        Object noSyncFlag = instanceData.get("noSyncFlag");
        if (noSyncFlag == null || BooleanUtils.toBoolean(noSyncFlag.toString()) == false) {
            // 审核通过，插入模型实例后，发送同步数据到kafka
            cmdbModuleProducerService.saveEventLogAndSendMsg(CmdbOptType.OPT_ADD, moduleId, instanceId);
        }
    }

    @Override
    public List<CmdbInstanceIpManager> handleIpManagerList(String curValue, String codeId, String instanceId) {
        List<CmdbInstanceIpManager> ipManagerList = new ArrayList<>();
        String ipValue = curValue.replace("，", ",");
        // 先删除关系
        CmdbInstanceIpManager deleteEntity = new CmdbInstanceIpManager();
        deleteEntity.setInstanceId(instanceId);
        deleteEntity.setCodeId(codeId);
        ipManagerService.delete(deleteEntity);
        if (StringUtils.isNotEmpty(ipValue)) {
            String[] ips = ipValue.split(",");
            for (String ip : ips) {
                if (StringUtils.isEmpty(ip.trim())) {
                    continue;
                }
                CmdbInstanceIpManager ipManager = new CmdbInstanceIpManager(instanceId, codeId, ip.trim(), null);
                ipManagerList.add(ipManager);
            }

        }
        return ipManagerList;
    }

    /**
     * 修改实例处理
     */
    @Override
    public String update(String userName, List<CmdbCollectApproval> approvals) {
        if (approvals == null || approvals.size() == 0) {
            return "无需要审核数据";
        }
        // 同一CI不同模型
        Map<String, Map<String, Object>> moduleData = new HashMap<>();
        Map<String, String> tableMap = new HashMap<>();
        // 如果是批量更新的话需要控制为同一CI
        // 当前要更新的CI
        long startTime = System.currentTimeMillis();
        String instanceId = approvals.get(0).getInstanceId();
        String moduleId = approvals.get(0).getModuleId();
        boolean isAloneModule = moduleService.isAloneModule(moduleId);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Module topModule = null;
        // 非独立模型, 需要获取
        if (!isAloneModule) {
            // 获取当前模型的所有依赖模型
            List<Module> moduleList = moduleService.getCurRefModule(moduleId);
            for (Module module : moduleList) {
                tableMap.put(module.getId(), module.getModuleCatalog().getCatalogCode());
                moduleData.put(module.getModuleCatalog().getCatalogCode(), new HashMap<>());
                // 如果是顶级节点
                if ("0".equals(module.getModuleCatalog().getParentCatalogId())) {
                    topModule = module;
                }
            }
            if (topModule == null) {
                throw new RuntimeException("获取顶级模型失败.");
            }
        } else {
            topModule = moduleService.getModuleDetail(moduleId);
            if (topModule == null) {
                throw new RuntimeException("获取模型失败moduleId[" + moduleId + "].");
            }
            tableMap.put(topModule.getId(), topModule.getModuleCatalog().getCatalogCode());
            moduleData.put(topModule.getModuleCatalog().getCatalogCode(), new HashMap<>());
        }
        // 获取到update_person/update_time 字段对应的模型
        moduleData.get(topModule.getModuleCatalog().getCatalogCode()).put("update_person", approvals.get(0).getOperator());
        moduleData.get(topModule.getModuleCatalog().getCatalogCode()).put("update_time", dateFormat.format(new Date()));

        log.info(">>>>>> 更新CI 处理各模型数据信息 耗时：{}", System.currentTimeMillis() - startTime);
        startTime = System.currentTimeMillis();
        List<CmdbInstanceIpManager> ipManagerList = new ArrayList<>();
        // 字段归属模型
        for (CmdbCollectApproval approval : approvals) {
            if (!approval.getInstanceId().equals(instanceId)) {
                throw new RuntimeException("批量更新操作仅限于同一条CI");
            }
            approval.setApprovalUser(userName);
            approval.setApprovalTime(new Date());
            approval.setApprovalStatus(1);
            // 获取id存储数据值（一条更新只有一个字段）
            Map<String, Object> instanceData = (Map<String, Object>) JSONObject.parse(approval.getResourceData());
            // moduleData.put(, new HashMap<>());
            // CmdbV3ModuleCatalog ownerCatalog = catalogService.getByModuleId(approval.getOwnerModuleId());
            //
            // String tableName = ownerCatalog.getCatalogCode();
            // if (!moduleData.containsKey(tableName)) {
            // moduleData.put(tableName, new HashMap<>());
            // }
            CmdbSimpleCode code = codeService.getSimpleCodeById(approval.getCodeId());
            CmdbControlType type = controlTypeService.getById(code.getControlTypeId());
            String currentValue = StringUtils.isNotEmpty(instanceData.get(code.getFiledCode()))
                    ? instanceData.get(code.getFiledCode()).toString()
                    : "";
            moduleData.get(tableMap.get(approval.getOwnerModuleId())).put(code.getFiledCode(), currentValue);
            // 如果类型是ip入ip管理表
            if (Constants.CODE_CONTROL_TYPE_IP.equals(type.getControlCode())) {
                ipManagerList.addAll(handleIpManagerList(currentValue, code.getCodeId(), instanceId));
            }
            //
        }
        log.info(">>>>>> 更新CI 字段归属模型处理 耗时：{}", System.currentTimeMillis() - startTime);
        startTime = System.currentTimeMillis();
        // 根据不同表去更新CI
        for (String tableName : moduleData.keySet()) {
            // mapper.update(tableName, instanceId, moduleData.get(tableName));
            schemaService.updateCi(tableName, instanceId, moduleData.get(tableName));
        }
        log.info(">>>>>> 更新CI 表处理 耗时：{}", System.currentTimeMillis() - startTime);
        startTime = System.currentTimeMillis();
        // ip类型入库
        if (ipManagerList.size() > 0) {
            ipManagerService.insertByBatch(instanceId, ipManagerList);
        }
        if (queryDbType != null && queryDbType.toLowerCase(Locale.ENGLISH).equals("es")) {
            // 刷新CI所属模型ES数据
            cmdbESService.asyncRefresh(moduleId, instanceId);
        }
        // 增加修改回调事件
        Map<String, Object> handleData = new HashMap<>();
        handleData.put("username", userName);
        handleData.put("approvals", approvals);
        handleData.put("operateType", EventConst.EVENT_TYPE_DATA_UPDATE);
        moduleEventService.handlerModuleDataEvent(moduleId, instanceId, null, handleData, EventConst.EVENT_TYPE_DATA_UPDATE);
        log.info(">>>>>> 更新CI 处理事件 耗时：{}", System.currentTimeMillis() - startTime);
        startTime = System.currentTimeMillis();
        // 审核通过，插入模型实例后，发送同步数据到kafka
        cmdbModuleProducerService.saveEventLogAndSendMsg(CmdbOptType.OPT_MODIFY, moduleId, instanceId);
        log.info(">>>>>> 更新CI 同步数据到kafka 耗时：{}", System.currentTimeMillis() - startTime);
        return "更新实例成功";
    }

    @Override
    public Map<String, Object> handleModuleData(String instanceId, String moduleId, Map<String, Object> instanceData) {

        Map<String, Object> handleResult = new HashMap<>();
        Map<String, Map<String, Object>> moduleData = new HashMap<>();
        Module module = moduleService.getModuleDetail(moduleId);
        for (Module refModule : module.getRefModules()) {
            moduleData.put(refModule.getId(), new HashMap<>());
        }
        List<CmdbInstanceIpManager> ipManagerList = new ArrayList<>();
        for (CmdbModuleCodeGroup group : module.getGroupList()) {
            for (CmdbCode code : group.getCodeList()) {
                if (StringUtils.isNotEmpty(instanceData.get(code.getFiledCode()))) {
                    String ownerModuleId = code.getCodeSetting().getOwnerModuleId();
                    String filedCode = code.getFiledCode();
                    String curValue = instanceData.get(code.getFiledCode()).toString();
                    validCode(code, curValue);
                    // 处理码表信息
                    if (code.getControlType().getControlCode().equals(Constants.CODE_CONTROL_TYPE_IP)) {
                        ipManagerList.addAll(handleIpManagerList(curValue, code.getCodeId(), instanceId));
                    }
                    if (!moduleData.containsKey(ownerModuleId)) {
                        moduleData.put(ownerModuleId, new HashMap<>());
                    }
                    moduleData.get(ownerModuleId).put(filedCode, curValue);
                }
            }
        }

        handleResult.put("moduleData", moduleData);
        handleResult.put("ipManagerList", ipManagerList);
        return handleResult;
    }

    @Override
    public void updateInstance(Map<String, Object> instanceData) {
        log.debug("instanceData=={}", instanceData);
        Object instanceIdObj = instanceData.get("instance_id");
        Object moduleIdObj = instanceData.get("module_id");
        Preconditions.checkArgument(instanceIdObj != null, "instanceId 不能为空!");
        Preconditions.checkArgument(moduleIdObj != null, "moduleId 不能为空!");
        String instanceId = instanceIdObj.toString();
        String moduleId = moduleIdObj.toString();
        if (org.apache.commons.lang3.StringUtils.isBlank(instanceId)) {
            return;
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        instanceData.put("update_person", "系统");
        instanceData.put("update_time", dateFormat.format(new Date()));
        // 同一CI不同模型
        Map<String, List<Map<String, String>>> moduleData = new HashMap<>();
        Module module = moduleService.getModuleDetail(moduleId);
        String ownerTable = module.getModuleCatalog().getCatalogCode();

        List<CmdbCode> ownerCodeList = codeService.getSelfCodeListByModuleId(module.getId());
        // 如果码表没数据则不更新表
        if (!moduleData.containsKey(ownerTable) && CollectionUtils.isNotEmpty(ownerCodeList)) {
            moduleData.put(ownerTable, new ArrayList<>());
        }
        for (CmdbCode cmdbCode : ownerCodeList) {
            Object fieldValue = instanceData.get(cmdbCode.getFiledCode());
            Map<String, String> filedInfo = new HashMap<>();
            // 如果为空，则修改为null
            // if (fieldValue != null) {
            filedInfo.put("filedCode", cmdbCode.getFiledCode());
            if (fieldValue != null && org.apache.commons.lang3.StringUtils.isNotBlank(fieldValue.toString())) {
                filedInfo.put("value", fieldValue.toString());
            } else {
                filedInfo.put("value", null);
            }
            // log.debug("filedInfo=={}", filedInfo);
            moduleData.get(ownerTable).add(filedInfo);
            // }
        }

        List<Module> refModules = module.getRefModules();
        for (Module module1 : refModules) {
            String refTable = module1.getModuleCatalog().getCatalogCode();
            if (!moduleData.containsKey(refTable)) {
                moduleData.put(refTable, new ArrayList<>());
            }
            List<CmdbCode> codes = codeService.getSelfCodeListByModuleId(module1.getId());

            for (CmdbCode cmdbCode : codes) {
                Map<String, String> filedInfo1 = new HashMap<>();
                Object fieldValue = instanceData.get(cmdbCode.getFiledCode());
                // 如果为空，则修改为null
                // if (fieldValue != null) {
                filedInfo1.put("filedCode", cmdbCode.getFiledCode());
                if (fieldValue != null && org.apache.commons.lang3.StringUtils.isNotBlank(fieldValue.toString())) {
                    filedInfo1.put("value", fieldValue.toString());
                } else {
                    filedInfo1.put("value", null);
                }
                moduleData.get(refTable).add(filedInfo1);
                // if (fieldValue != null) {
                // filedInfo1.put("filedCode", cmdbCode.getFiledCode());
                // filedInfo1.put("value", fieldValue.toString());
                // moduleData.get(refTable).add(filedInfo1);
                // break;
                // }
            }

        }
        log.debug("待更新的moduleData=={}", moduleData);
        // 根据不同表去更新CI
        for (String tableName : moduleData.keySet()) {
            mapper.update(tableName, instanceId, moduleData.get(tableName));
        }
        // TODO:其他IP是否入库到这里？？？ipmanager???
        // // ip类型入库
        // if (ipManagerList.size() > 0) {
        // ipManagerService.insertByBatch(instanceId, ipManagerList);
        // }

        if (queryDbType != null && queryDbType.toLowerCase(Locale.ENGLISH).equals("es")) {
            // 刷新CI所属模型ES数据
            cmdbESService.asyncRefresh(moduleId, instanceId);
        }
    }

    /**
     * 修改实例入审核库
     *
     * @param approvalList
     *            实例数据
     * @return
     */
    @Transactional(rollbackFor = { RuntimeException.class, Exception.class, SQLException.class })
    public String updateToApproval(List<CmdbCollectApproval> approvalList) {
        List<CmdbCollectApproval> insertApprovals = new ArrayList<>();
        List<CmdbCollectApproval> autoPass = new ArrayList<>();
        List<CmdbCollectApproval> autoRefuse = new ArrayList<>();
        String instanceId = approvalList.get(0).getInstanceId();
        List<CmdbInstanceIpManager> ipManagerList = new ArrayList<>();
        for (CmdbCollectApproval approval : approvalList) {
            String codeId = approval.getCodeId();
            CmdbSimpleCode code = codeService.getSimpleCodeById(codeId);
            CmdbControlType controlType = controlTypeService.getById(code.getControlTypeId());
            CmdbV3CodeApprove approve = codeApproveService.getByCodeId(codeId);
            // 判断是否需要自动审核
            if (approve != null) {
                if ("自动通过".equals(approve.getApproveType())) {
                    autoPass.add(approval);
                    // 处理ip类型数据
                    if (controlType.getControlCode().equals(Constants.CODE_CONTROL_TYPE_IP)) {
                        ipManagerList.addAll(handleIpManagerList(approval.getCurrValue(), codeId, approval.getInstanceId()));
                    }
                } else if ("自动驳回".equals(approve.getApproveType())) {
                    autoRefuse.add(approval);
                }
            } else {
                insertApprovals.add(approval);
            }
        }
        if (ipManagerList.size() > 0) {
            ipManagerService.insertByBatch(instanceId, ipManagerList);
        }
        if (insertApprovals.size() > 0) {
            approvalService.insertByBatch(insertApprovals);
        }
        if (autoRefuse.size() > 0) {
        }
        if (autoPass.size() > 0) {
            this.update(autoPass.get(0).getOperator(), autoPass);
        }
        return "成功录入配置审核" + insertApprovals.size() + "条, 自动通过" + autoPass.size() + "自动驳回" + autoRefuse.size() + "条";
    }

    @Override
    public Integer batchUpdateCount(String moduleId, Map<String, Object> batchUpdate) {
        Module module = moduleService.getModuleDetail(moduleId);
        if (module == null) {
            throw new RuntimeException("未查询到相应模型！");
        }
        String sql = startToQuery(module, batchUpdate, "count");
        // 这里设置成不需要判断权限 因为能进入到审核步骤的, 默认已经具备了数据权限
        CmdbSqlManage cmdbSqlManage = new CmdbSqlManage(sql, Constants.INSTANCE_AUTH_MODULE, Constants.UN_NEED_AUTH);
        return jdbcHelper.getInt(cmdbSqlManage, null, null);
    }

    /**
     * 根据条件获取需要批量更新CI的审核数据
     */
    @Override
    public List<String> getBatchUpdateApprovals(String username, String moduleId, Map<String, Object> batchUpdate) {
        Module module = moduleService.getModuleDetail(moduleId);
        if (module == null) {
            throw new RuntimeException("未查询到相应模型！");
        }
        String sql = "select id from (" + startToQuery(module, batchUpdate, "list") + ") upRes";
        // 这里设置成不需要判断权限 因为能进入到审核步骤的, 默认已经具备了数据权限
        CmdbSqlManage cmdbSqlManage = new CmdbSqlManage(sql, Constants.INSTANCE_AUTH_MODULE, Constants.UN_NEED_AUTH);
        List<String> queryRes = jdbcHelper.getQueryFiled(cmdbSqlManage, null, null, null, null);
        return queryRes;
    }

    private String startToQuery(Module module, Map<String, Object> batchUpdate, String type) {
        List<Map<String, String>> updateFileds = (List<Map<String, String>>) batchUpdate.get("update");
        List<Map<String, String>> querys = (List<Map<String, String>>) batchUpdate.get("querys");
        validUpdateRequest(querys, updateFileds);
        String condition = CmdbConst.getConditionSql(querys);
        String notEqualsCurrValue = getIfNull(updateFileds);
        // 获取被查询的全量sql
        String moduleSql = moduleService.getModuleQuerySQL(module.getId());
        if (StringUtils.isEmpty(moduleSql)) {
            throw new RuntimeException("未获取相应模型sql");
        }
        return "select * from (" + moduleSql + ")res where 1=1 " + condition + notEqualsCurrValue;
    }

    private String getIfNull(List<Map<String, String>> updateFileds) {
        StringBuilder ifNull = new StringBuilder("and (");
        for (Map<String, String> filed : updateFileds) {
            ifNull.append(" IFNULL(").append(filed.get("field")).append(", '') != '").append(filed.get("value")).append("' OR ");
        }
        ifNull.deleteCharAt(ifNull.length() - 3);
        ifNull = new StringBuilder(ifNull.substring(0, ifNull.length() - 3));
        ifNull.append(")");
        return ifNull.toString();
    }

    private void validUpdateRequest(List<Map<String, String>> querys, List<Map<String, String>> update) {
        if (querys == null || querys.size() == 0) {
            throw new RuntimeException("查询条件不能为空");
        }
        for (Map<String, String> query : querys) {
            if (StringUtils.isEmpty(query.get("field"))) {
                throw new RuntimeException("筛选条件码表名称不可为空");
            }
            if (StringUtils.isEmpty(query.get("operator"))) {
                throw new RuntimeException("查询条件不可为空");
            }
        }
        if (update == null || update.size() == 0) {
            throw new RuntimeException("修正字段不能为空");
        }
        for (Map<String, String> up : update) {
            if (StringUtils.isEmpty(up.get("codeId"))) {
                throw new RuntimeException("修正字段id不能为空");
            }
        }
    }

    /**
     * 删除实例
     * 
     * @param entity
     *            实例数据
     * @return
     */
    @Override
    public void delete(CmdbInstance entity) {
        mapper.delete(entity);
    }

    @Override
    public Result<Map<String, Object>> getInstanceList(Map<String, Object> params, String moduleType) {
        if (!params.containsKey("token")) {
            params.put("token", innerUserId);
        }
        if (!params.containsKey("condicationCode")) {
            params.put("condicationCode", "instance_list");
        }
        if (StringUtils.isNotEmpty(moduleType)) {
            params.put("moduleType", moduleType);
        }
        Map<String, Object> queryParams = condctService.parseQuery(params);
        queryParams.put("condicationCode4Tab", params.get("condicationCode"));
        queryParams.put("exportTabType", params.get("exportTabType"));
        if (null == queryParams.get("query_module_id")) {
            return new Result<>(0, new ArrayList<>());
        }
        if (queryDbType != null && queryDbType.toLowerCase(Locale.ENGLISH).equals("es")) {
            // 请求es数据
            log.info("Request es params -> {}", net.sf.json.JSONObject.fromObject(queryParams).toString());
            com.aspire.mirror.elasticsearch.api.dto.cmdb.Result result = cmdbESService.list(queryParams);
            return new Result<>(result.getCount(), result.getData());
        }
        long start = new Date().getTime();
        Result<Map<String, Object>> result = getInstanceList(queryParams);
        log.info("-------耗时: {}", (new Date().getTime() - start));
        return result;
    }

    @Override
    public List<Map<String, Object>> getInstanceListData(Map<String, Object> params, String moduleType) {
        if (!params.containsKey("token")) {
            params.put("token", innerUserId);
        }
        if (!params.containsKey("condicationCode")) {
            params.put("condicationCode", "instance_list");
        }
        if (StringUtils.isNotEmpty(moduleType)) {
            params.put("moduleType", moduleType);
        }
        Map<String, Object> queryParams = condctService.parseQuery(params);
        queryParams.put("condicationCode4Tab", params.get("condicationCode"));
        queryParams.put("exportTabType", params.get("exportTabType"));
        if (null == queryParams.get("query_module_id")) {
            return new ArrayList<>();
        }
        if (queryDbType != null && queryDbType.toLowerCase(Locale.ENGLISH).equals("es")) {
            // 请求es数据
            log.info("Request es params -> {}", net.sf.json.JSONObject.fromObject(queryParams).toString());
            com.aspire.mirror.elasticsearch.api.dto.cmdb.Result result = cmdbESService.list(queryParams);
            return new ArrayList<>();
        }
        Object[] formatQuerys = this.formatStatementParams(queryParams);
        StringBuilder queryBuilder = new StringBuilder((String) formatQuerys[0]);
        Map<String, Object> statementParams = (Map<String, Object>) formatQuerys[1];
        StringBuilder sortBuilderSQL = new StringBuilder((String) formatQuerys[2]);
        Integer currentPage = null, pageSize = null;
        if (queryParams.containsKey("currentPage") && queryParams.get("currentPage") != null) {
            currentPage = (Integer) queryParams.get("currentPage");
        }
        if (queryParams.containsKey("pageSize") && queryParams.get("pageSize") != null) {
            pageSize = (Integer) queryParams.get("pageSize");
        }
        String moduleId = queryParams.get("query_module_id").toString();
        String querySQL = "";
        List<String> tabParamList = new ArrayList<>(statementParams.keySet());
        CmdbConfig cmdbConfig = cmdbConfigService.getConfigByCode("instance_tab_list");
        if (null != cmdbConfig && "instance_list".equals(queryParams.get("condicationCode4Tab").toString())) {
            String exportTabType = Constants.MODULE_TAB_LIST;
            if (null != queryParams.get("exportTabType")) {
                exportTabType = queryParams.get("exportTabType").toString();
            }
            querySQL = moduleService.getModuleQuerySQL4Tab(moduleId, tabParamList, exportTabType);
        } else {
            querySQL = moduleService.getModuleQuerySQL(moduleId);
        }
        // 增加排序
        String orderBy = null;
        if (!("").equals(sortBuilderSQL.toString())) {
            orderBy = " order by " + sortBuilderSQL.toString();
        }
        String limitString = null;
        if (currentPage != null && pageSize != null) {
            limitString = " limit " + ((currentPage - 1) * pageSize) + ", " + pageSize;
        }
        // 如果是IP管理模块，则跳过资源验证
        CmdbSqlManage sqlManage = new CmdbSqlManage(querySQL, moduleId, Constants.INSTANCE_AUTH_MODULE, Constants.NEED_AUTH);
        CmdbV3ModuleCatalog catalog = catalogService.getByModuleId(moduleId);
        CmdbV3ModuleCatalog topCatalog = catalogService.getTopCatalog(catalog.getId());
        if (topCatalog !=null && Constants.MODULE_IP_REPOSITORY.equals(topCatalog.getCatalogCode())) {
            sqlManage = new CmdbSqlManage(querySQL, moduleId, Constants.INSTANCE_AUTH_MODULE, Constants.UN_NEED_AUTH);
        }
        return jdbcHelper.getQueryList(sqlManage, queryBuilder.toString(), orderBy, limitString, statementParams);
    }

    /**
     * 获取文件下载类型
     * 
     * @return
     */
    @Override
    public String getDownloadType() {
        List<ConfigDict> configDictList = configDictService.selectDictsByType("export_type", null, null, null);
        // 默认ftp方式
        String ftpType = "ftp";
        if (configDictList != null && configDictList.size() > 0) {
            ftpType = configDictList.get(0).getValue();
        }
        return ftpType;
    }

    private void assessKey(Map<String, Object> map, String key, String msg) {
        if (!map.containsKey(key)) {
            throw new RuntimeException(msg);
        }
    }

    /**
     * 转化查询条件
     * 
     * @param queryParams
     * @return
     */
    private Object[] formatStatementParams(Map<String, Object> queryParams) {
        List<Map<String, Object>> paramsList = (List<Map<String, Object>>) queryParams.get("params");
        StringBuilder queryBuilder = new StringBuilder();
        Map<String, Object> statementParams = new LinkedMap();
        for (Map<String, Object> params : paramsList) {
            this.assessKey(params, "filed",
                    "Format params error. querySetting.params every member must has filed、operator、value properties.");
            this.assessKey(params, "operator",
                    "Format params error. querySetting.params every member must has filed、operator、value properties.");
            this.assessKey(params, "value",
                    "Format params error. querySetting.params every member must has filed、operator、value properties.");
            String operator = params.get("operator").toString().trim();
            String filed = params.get("filed").toString().trim();
            Object value = params.get("value");
            // 处理特殊的全量IP地址查询字段, 由于全量IP查询条件是在module sql中内置的, 因此这里不用拼接where条件
            if (("cmdb_instance_ip_manager_ip").equals(filed.toLowerCase(Locale.ENGLISH))) {
                statementParams.put(filed, value);
                continue;
            }
            if (StringUtils.isNotEmpty(value)) {
                switch (operator.toLowerCase(Locale.ENGLISH)) {
                    case "like":
                        queryBuilder.append(jdbcHelper.likeSql(filed, true));
                        statementParams.put(filed, value);
                        break;
                    case "<":
                    case "<=":
                        queryBuilder.append(jdbcHelper.lteSql(filed, true));
                        statementParams.put(filed, value);
                        break;
                    case ">":
                    case ">=":
                        queryBuilder.append(jdbcHelper.gteSql(filed, true));
                        statementParams.put(filed, value);
                        break;
                    case "in":
                        queryBuilder.append(jdbcHelper.inSql(filed, true));
                        statementParams.put(filed, value);
                        break;
                    case "=":
                        queryBuilder.append(jdbcHelper.eqSql(filed, true));
                        statementParams.put(filed, value);
                        break;
                    case "not in":
                        queryBuilder.append(jdbcHelper.notInSql(filed, true));
                        statementParams.put(filed, value);
                        break;
                    case "between":
                        List<String> valueList = (List<String>) value;
                        queryBuilder.append(jdbcHelper.betweenSql(filed, true));
                        statementParams.put(filed, value);
                        statementParams.put(filed + "_start", valueList.get(0));
                        statementParams.put(filed + "_end", valueList.get(1));
                        break;
                    case "contain":
                        Object containFiled = params.get("containFiled");
                        if (null != containFiled) {
                            String s = containFiled.toString();
                            String[] split = s.split(",");
                            queryBuilder.append(jdbcHelper.containSql(filed,split, true));
                            statementParams.put(filed, value);
                        } else {
                            queryBuilder.append(jdbcHelper.likeSql(filed, true));
                            statementParams.put(filed, value);
                        }
                        break;
                    default:
                        throw new RuntimeException("Don't support operator type [" + operator + "]");
                }
            }
        }
        // 处理排序
        StringBuilder sortBuilderSQL = new StringBuilder();
        if (queryParams.containsKey("sort")) {
            List<Map<String, Object>> sortList = (List<Map<String, Object>>) queryParams.get("sort");
            for (Map<String, Object> sortMap : sortList) {
                if (sortMap.get("filed") != null) {
                    String sortFiled = sortMap.get("filed").toString();
                    if (sortMap.get("type") != null) {
                        if (!("").equals(sortBuilderSQL.toString())) {
                            sortBuilderSQL.append(",");
                        }
                        String sortType = sortMap.get("type").toString();
                        // 修改需要特殊排序的字段 modify by fanwenhui 20200730
                        boolean index = checkInstanceListSortField(queryParams.get("index").toString(), sortFiled);
                        sortBuilderSQL.append(" IFNULL(").append(sortFiled).append(",'') = '' , ");
                        if (index) {
                            sortBuilderSQL.append(" INET_ATON(").append(sortFiled).append(") ");
                        } else {
                            sortBuilderSQL.append(sortFiled).append(" ");
                        }
                        sortBuilderSQL.append(sortType).append(" ");
                    }
                }
            }
        }
        return new Object[] { queryBuilder.toString(), statementParams, sortBuilderSQL.toString() };
    }

    private Result<Map<String, Object>> getInstanceList(Map<String, Object> queryParams) {
        Object[] formatQuerys = this.formatStatementParams(queryParams);
        StringBuilder queryBuilder = new StringBuilder((String) formatQuerys[0]);
        Map<String, Object> statementParams = (Map<String, Object>) formatQuerys[1];
        StringBuilder sortBuilderSQL = new StringBuilder((String) formatQuerys[2]);
        Integer currentPage = null, pageSize = null;
        if (queryParams.containsKey("currentPage") && queryParams.get("currentPage") != null) {
            currentPage = (Integer) queryParams.get("currentPage");
        }
        if (queryParams.containsKey("pageSize") && queryParams.get("pageSize") != null) {
            pageSize = (Integer) queryParams.get("pageSize");
        }
        //当前查询的moduleId
        String moduleId = queryParams.get("query_module_id").toString();
        String querySQL = "";
        List<String> tabParamList = new ArrayList<>(statementParams.keySet());
        CmdbConfig cmdbConfig = cmdbConfigService.getConfigByCode("instance_tab_list");
        if (null != cmdbConfig && "instance_list".equals(queryParams.get("condicationCode4Tab").toString())) {
            String exportTabType = Constants.MODULE_TAB_LIST;
            if (null != queryParams.get("exportTabType")) {
                exportTabType = queryParams.get("exportTabType").toString();
            }
            querySQL = moduleService.getModuleQuerySQL4Tab(moduleId, tabParamList, exportTabType);
        } else {
            querySQL = moduleService.getModuleQuerySQL(moduleId);
        }
        // 增加排序
        String orderBy = null;
        if (!("").equals(sortBuilderSQL.toString())) {
            orderBy = " order by " + sortBuilderSQL.toString();
        }
        String limitString = null;
        if (currentPage != null && pageSize != null) {
            limitString = " limit " + ((currentPage - 1) * pageSize) + ", " + pageSize;
        }
        // 如果是IP管理模块，则跳过资源验证
        CmdbSqlManage sqlManage = new CmdbSqlManage(querySQL, moduleId, Constants.INSTANCE_AUTH_MODULE, Constants.NEED_AUTH);
        CmdbV3ModuleCatalog catalog = catalogService.getByModuleId(moduleId);
        CmdbV3ModuleCatalog topCatalog = catalogService.getTopCatalog(catalog.getId());
        if (topCatalog !=null && Constants.MODULE_IP_REPOSITORY.equals(topCatalog.getCatalogCode())) {
            sqlManage = new CmdbSqlManage(querySQL, moduleId, Constants.INSTANCE_AUTH_MODULE, Constants.UN_NEED_AUTH);
        }
        Result<Map<String, Object>> resultPage = new Result<>();
        List<Map<String, Object>> list = jdbcHelper.getQueryList(sqlManage, queryBuilder.toString(), orderBy, limitString,
                statementParams);
        String queryCountSql = "";
        if (null != cmdbConfig && "instance_list".equals(queryParams.get("condicationCode4Tab").toString())) {
            queryCountSql = moduleService.getModuleQueryCountSQL4Tab(moduleId, tabParamList);
        } else {
            queryCountSql = moduleService.getModuleQueryCountSQL(moduleId);
        }
        CmdbSqlManage countManage = new CmdbSqlManage(queryCountSql, moduleId, Constants.INSTANCE_AUTH_MODULE, Constants.NEED_AUTH);
        Integer count = jdbcHelper.getInt(countManage, queryBuilder.toString(), statementParams);
        resultPage.setData(list);
        resultPage.setTotalSize(count);
        resultPage.setColumns(moduleService.getModuleColumns(moduleId));
        return resultPage;
    }

    @Override
    public List<CmdbInstance> getInstanceByIp(Map<String, Object> param) {
        return mapper.getInstanceByIp(param);
    }

    @Override
    @Transactional(rollbackFor = { RuntimeException.class, Exception.class, SQLException.class })
    public String deleteInstance(String userName, List<Map<String, Object>> instanceList, String operateType) {
        String msg = "";
        Integer deleteCount = 0;
        Integer approvalCount = 0;
        for (Map<String, Object> instanceData : instanceList) {
            SimpleModule module = validModule(instanceData);
            CmdbCollectApproval approval = handleDeleteApproval(userName, instanceData, operateType);
            // 从界面调用此接口是需要进行判断是否审核的
            if (module.getEnableApprove() != null && module.getEnableApprove() == 0) {
                // 不用审核
                this.delete(userName, approval);
                deleteCount++;
            } else {
                List<CmdbCollectApproval> approvals = new ArrayList<>();
                approvals.add(approval);
                approvalService.insertByBatch(approvals);
                approvalCount++;
            }
        }
        if (deleteCount > 0 && approvalCount > 0) {
            return "已成功删除实例" + deleteCount + ",录入配置审核" + approvalCount + "条";
        } else if (deleteCount > 0) {
            return "已成功删除实例" + deleteCount + "条";
        } else {
            return "录入配置审核" + approvalCount + "条";
        }
    }

    /**
     * 删除CI数据逻辑,支持不同模型下ci删除
     * 
     * @param userName
     *            操作用户
     * @param approval
     *            删除数据信息
     */
    @Override
    public void delete(String userName, CmdbCollectApproval approval) {
        String instanceId = approval.getInstanceId();
        String moduleId = approval.getModuleId();
        Module module = moduleService.getModuleDetail(moduleId);
        CmdbV3ModuleCatalog catalog = catalogService.getByModuleId(moduleId);
        List<String> deleteTableName = new ArrayList<>();
        deleteTableName.add(catalog.getCatalogCode());
        // 是否独立模型
        boolean isAloneModule = moduleService.isAloneModule(moduleId);
        CmdbV3ModuleCatalog topCatalog = null;
        if (!isAloneModule) {
            topCatalog = catalogService.getTopCatalog(catalog.getId());
            List<Module> refModules = module.getRefModules();
            for (Module m : refModules) {
                CmdbV3ModuleCatalog refCatalog = catalogService.getByModuleId(m.getId());
                deleteTableName.add(refCatalog.getCatalogCode());
            }
        }
        Map<String, Object> instanceDetail = getInstanceDetail(moduleId, instanceId);
        // 开始删除数据
        for (String tableName : deleteTableName) {
            Map<String, Object> instanceData = new HashMap<>();
            if (isAloneModule || tableName.equals(topCatalog.getCatalogCode())) {
                instanceData.put("update_person", userName);
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                instanceData.put("update_time", dateFormat.format(new Date()));
            }
            instanceData.put("is_delete", 1);
            schemaService.deleteCi(tableName, approval.getInstanceId(), instanceData);
        }
        // 同步删除网管的资产
        cmdbModuleProducerService.saveEventLogAndSendMsg(CmdbOptType.OPT_DEL, moduleId, approval.getInstanceId());
        // 增加新增回调事件
        Map<String, Object> handleData = new HashMap<>();
        List<CmdbCollectApproval> approvals = new ArrayList<>();
        approval.setApprovalStatus(1);
        approvals.add(approval);
        handleData.put("username", userName);
        handleData.put("approvals", approvals);
        handleData.put("instanceDetail", instanceDetail);
        handleData.put("operateType", EventConst.EVENT_TYPE_DATA_DELETE);
        moduleEventService.handlerModuleDataEvent(moduleId, approval.getInstanceId(), null, handleData,
                EventConst.EVENT_TYPE_DATA_DELETE);
    }

    @Override
    public void deletePhysic(CmdbCollectApproval approval) {
        String moduleId = approval.getModuleId();
        Module module = moduleService.getModuleDetail(moduleId);
        CmdbV3ModuleCatalog catalog = catalogService.getByModuleId(moduleId);
        // CmdbV3ModuleCatalog topCatalog = catalogService.getTopCatalog(catalog.getId());
        List<String> deleteTableName = new ArrayList<>();
        // 存当前模型表
        deleteTableName.add(catalog.getCatalogCode());
        List<Module> refModules = module.getRefModules();
        for (Module m : refModules) {
            CmdbV3ModuleCatalog refCatalog = catalogService.getByModuleId(m.getId());
            deleteTableName.add(refCatalog.getCatalogCode());
        }
        // 开始删除数据
        for (String tableName : deleteTableName) {
            schemaService.delete(tableName, approval.getInstanceId());
        }
    }

    @Override
    public List<Map> getIdcTree() {
        // 资源池 -> 机房位置
        List<CmdbIdcManager> idcManagerList = idcManagerService.list();
        final List<Map> list = new LinkedList<>();
        idcManagerList.stream().forEach((idc) -> {
            Map<String, Object> idcMap = new HashMap<>();
            idcMap.put("uuid", idc.getId());
            idcMap.put("name", idc.getIdcName());
            idcMap.put("type", "idcType");
            // 加载ROOM
            CmdbRoomManager queryRoom = new CmdbRoomManager();
            queryRoom.setIdcId(idc.getId());
            List<CmdbRoomManager> roomList = roomManagerService.listByEntity(queryRoom);
            List<Map> roomMapList = new LinkedList<>();
            if (roomList != null && roomList.size() > 0) {
                roomList.stream().forEach((room) -> {
                    Map<String, Object> roomMap = new HashMap<>();
                    roomMap.put("uuid", room.getId());
                    roomMap.put("name", room.getRoomName());
                    roomMap.put("type", "room");
                    roomMap.put("subList", new ArrayList<>());
                    roomMapList.add(roomMap);
                });
            }
            idcMap.put("subList", roomMapList);
            list.add(idcMap);
        });
        return list;
    }

    @Override
    public List<Map> getDeviceClassTree() {
        final List<Map> list = new LinkedList<>();
        List<ConfigDict> deviceClassList = configDictService.selectDictsByType("device_class", null, null, null);
        if (deviceClassList != null && deviceClassList.size() > 0) {
            deviceClassList.stream().forEach((deviceClass) -> {
                Map<String, Object> deviceMap = new HashMap<>();
                deviceMap.put("uuid", deviceClass.getId());
                deviceMap.put("name", deviceClass.getName());
                deviceMap.put("type", "device_class");
                List<ConfigDict> deviceTypeList = configDictService.selectDictsByType("device_type",
                        String.valueOf(deviceClass.getId()), deviceClass.getValue(), "device_class");
                List<Map> typeMapList = new LinkedList<>();
                if (deviceTypeList != null && deviceTypeList.size() > 0) {
                    deviceTypeList.stream().forEach((type) -> {
                        Map<String, Object> typeMap = new HashMap<>();
                        typeMap.put("uuid", type.getId());
                        typeMap.put("name", type.getName());
                        typeMap.put("type", "device_type");
                        typeMap.put("subList", new ArrayList<>());
                        typeMapList.add(typeMap);
                    });
                }
                deviceMap.put("subList", typeMapList);
                list.add(deviceMap);
            });
        }
        return list;
    }

    @Override
    public List<String> getDepartmentsByIDC(String idcType) {
        return mapper.getDepartmentsByIDC(idcType);
    }

    @Override
    public List<Map<String, String>> getIdcByIds(String ids) {
        String[] idcIds = ids.split(",");
        List<Map<String, String>> idcList = new LinkedList<>();
        for (String id : idcIds) {
            Map<String, String> idcMap = mapper.getIdcById(id);
            if (idcMap != null && idcMap.size() > 0) {
                idcList.add(idcMap);
            }
        }
        return idcList;
    }

    @Override
    public List<Map<String, String>> getPodByIds(String ids) {
        String[] podIds = ids.split(",");
        List<Map<String, String>> podList = new LinkedList<>();
        for (String id : podIds) {
            Map<String, String> podMap = mapper.getPodById(id);
            if (podMap != null && podMap.size() > 0) {
                podList.add(podMap);
            }
        }
        return podList;
    }

    @Override
    public List<Map<String, String>> getRoomByIds(String ids) {
        String[] roomIds = ids.split(",");
        List<Map<String, String>> roomList = new LinkedList<>();
        for (String id : roomIds) {
            Map<String, String> roomMap = mapper.getRoomById(id);
            if (roomMap != null && roomMap.size() > 0) {
                roomList.add(roomMap);
            }
        }
        return roomList;
    }

    @Override
    public List<CmdbDeviceTypeCount> queryServiceCount(String bizSystem) {
        List<String> bizSystemList = Lists.newArrayList();
        if (!StringUtils.isEmpty(bizSystem)) {
            String[] bizSystemArray = bizSystem.split(",");
            bizSystemList = Arrays.asList(bizSystemArray);
        }
        return mapper.queryServiceCount(bizSystemList);
    }

    @Override
    public List<CmdbDeviceTypeCount> queryServiceCountForKG() {
        return mapper.queryServiceCountForKG();
    }

    @Override
    public List<Map<String, Object>> getNetworkAndSafetyDeivce(CmdbQueryInstance cmdbQueryInstance) {
        return mapper.getNetworkAndSafetyDeivce(cmdbQueryInstance);
    }

    @Override
    public List<Map<String, String>> getProjectNameByIdcType(String idcType) {
        return mapper.getProjectNameByIdcType(idcType);
    }

    @Override
    public List<CmdbDeviceTypeByConditonCount> queryDeviceCountByIdctype(String idcType, String deviceType, String startTime,
            String endTime) {
        return mapper.queryDeviceCountByIdctype(idcType, deviceType, startTime, endTime);
    }

    @Override
    public List<CmdbDeviceTypeByConditonCount> queryDeviceCountByBizsystem(String bizSystem, String idcType, String deviceType,
            String startTime, String endTime, String sourceType) {
        List<String> bizSystemList = Lists.newArrayList();
        if (!StringUtils.isEmpty(bizSystem)) {
            String[] bizSystemArray = bizSystem.split(",");
            bizSystemList = Arrays.asList(bizSystemArray);
        }
        return mapper.queryDeviceCountByBizsystem(bizSystemList, idcType, deviceType, startTime, endTime, sourceType);
    }

    @Override
    public Map<String, Map<String, Map<String, Integer>>> filterEmptyCiItem(String ciItem) {
        // 先获取资源池列表
        Map<String, Map<String, Map<String, Integer>>> returnMap = new LinkedHashMap<>();
        List<ConfigDict> idcList = configDictService.selectDictsByType("idcType", null, null, null);
        for (ConfigDict configDict : idcList) {
            if (!returnMap.containsKey(configDict.getName())) {
                returnMap.put(configDict.getName(), new HashMap<>());
            }
            Map<String, Map<String, Integer>> deviceMap = returnMap.get(configDict.getName());
            // 循环模型列表
            List<Module> moduleList = moduleService.getModuleTree(null, null); // 父模型
            for (Module parentModule : moduleList) {
                if (parentModule.getChildModules() != null && parentModule.getChildModules().size() > 0) {
                    // 资源池 -> 设备类型 -> 空值设备数量
                    // 解析子模型
                    for (Module childModule : parentModule.getChildModules()) {
                        if (!deviceMap.containsKey(childModule.getName())) {
                            deviceMap.put(childModule.getName(), new HashMap<>());
                        }
                        Map<String, Integer> filedMap = deviceMap.get(childModule.getName());
                        // 获取模型下有的码表字段
                        List<CmdbCode> codeList = codeService.getCodeListByModuleId(childModule.getId());
                        for (CmdbCode cmdbCode : codeList) {
                            for (String ciCode : ciItem.split(",")) {
                                if (ciCode.equals(cmdbCode.getFiledName())) {
                                    // 去查询为空的设备数量
                                    int count = mapper.queryEmptyCiItemCount("cmdb_instance_" + childModule.getCode(),
                                            cmdbCode.getFiledCode(), configDict.getName(), childModule.getName());
                                    if (!filedMap.containsKey(ciCode)) {
                                        filedMap.put(ciCode, count);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return returnMap;
    }

    @Override
    public Map<String, Object> getInstanceByIPMI(String ipmiIp, String idcType) {
        Map<String, Object> params = new HashMap<>();
        params.put("ipmi_ip", ipmiIp);
        Map<String, Object> detail = queryInstanceDetail(params, null);
        return detail;
    }

    @Override
    public void updateAllPool(List<String> monitorPools, String flag) {
        mapper.updateAllPool(monitorPools, flag);
    }

    @Override
    public void updateZbxMonitorStatus(String instanceId, String flag) {
        mapper.updateZbxMonitorStatus(instanceId, flag);
    }

    @Override
    public void updateProMonitorStatus(String instanceId, String flag) {
        mapper.updateProMonitorStatus(instanceId, flag);
    }

    @Override
    public List<Map<String, Object>> getInstanceBaseInfo(Map<String, Object> param) {
        return mapper.getInstanceBaseInfo(param);
    }

    @Override
    public Result<Map<String, Object>> getAllIPInstance(Map<String, Object> params) {
        Module module = moduleService.getDefaultModule("host");
        String querySql = moduleService.getModuleQuerySQL(module.getId());
        querySql = querySql.replace(Constants.INNER_LIMIT_STRING, "");
        // 组织SQL
        String finalSql = "select m.ip cmdb_instance_manager_ip, c.* from cmdb_instance_ip_manager m ";
        finalSql += "inner join (" + querySql + ") c on m.instance_id = c.id ";
        finalSql += "where IFNULL(m.ip, '') != '' " + Constants.INNER_LIMIT_STRING;
        String whereString = "";
        Map<String, Object> queryParams = new HashMap<>();
        if (params.containsKey("idcType") && StringUtils.isNotEmpty(params.get("idcType"))) {
            whereString += jdbcHelper.eqSql("idcType", true);
            queryParams.put("idcType", params.get("idcType"));
        }
        if (params.containsKey("update_time") && StringUtils.isNotEmpty(params.get("update_time"))) {
            whereString += jdbcHelper.gteSql("update_time", true);
            queryParams.put("update_time", params.get("update_time"));
        }
        Integer pageSize = 10000, currentPage = 1;
        try {
            pageSize = Integer.parseInt(params.get("pageSize").toString());
            currentPage = Integer.parseInt(params.get("currentPage").toString());
        } catch (Exception e) {
            log.error("Get paged error, use default size instanceof.");
        }
        String limitString = " limit " + ((currentPage - 1) * pageSize) + ", " + pageSize;
        long start1 = new Date().getTime();
        CmdbSqlManage sqlManage = new CmdbSqlManage(finalSql, Constants.INSTANCE_AUTH_MODULE, Constants.UN_NEED_AUTH);
        List<Map<String, Object>> dataList = jdbcHelper.getQueryList(sqlManage, whereString, null, limitString, queryParams);
        log.info("步驟1===>耗时 {} ms", (new Date().getTime() - start1));
        // 组织SQL
        String finalCountSql = "select 1 from cmdb_instance_ip_manager m ";
        finalCountSql += "inner join cmdb_instance c on m.instance_id = c.id ";
        finalCountSql += "where IFNULL(m.ip, '') != '' and c.is_delete='0'";
        start1 = new Date().getTime();
        CmdbSqlManage countSqlManager = new CmdbSqlManage(finalCountSql, Constants.INSTANCE_AUTH_MODULE, Constants.UN_NEED_AUTH);
        int count = jdbcHelper.getInt(countSqlManager, null, queryParams);
        log.info("步驟2===>耗时 {} ms", (new Date().getTime() - start1));

        Result<Map<String, Object>> pagedResult = new Result();
        pagedResult.setData(dataList);
        pagedResult.setTotalSize(count);
        pagedResult.setColumns(moduleService.getModuleColumns(module.getId()));
        return pagedResult;
    }

    /**
     * 根据查询条件 返回主机实例数据 提供给所有外部API接口使用
     * 
     * @param params
     *            查询参数
     * @return
     */
    @Override
    public Result<Map<String, Object>> getInstanceListForCommon(Map<String, Object> params) {
        Map<String, Object> queryParams = condctService.parseQuery(params);
        if (queryDbType != null && queryDbType.toLowerCase(Locale.ENGLISH).equals("es")) {
            // 请求es数据
            log.info("Request es params -> {}", net.sf.json.JSONObject.fromObject(queryParams).toString());
            com.aspire.mirror.elasticsearch.api.dto.cmdb.Result result = cmdbESService.list(queryParams);
            return new Result<>(result.getCount(), result.getData());
        }
        return getInstanceList(queryParams);
    }

    @Override
    public List<Map<String, Object>> deviceCountByDeviceClass(String deviceClass) {
        return mapper.deviceCountByDeviceClass(deviceClass);
    }

    @Override
    public Map<String, Object> deviceCountByDeviceType(String deviceClass, String deviceType) {
        return mapper.deviceCountByDeviceType(deviceClass, deviceType);
    }

    @Override
    public Object getBlockSize() {
        Map<String, Object> blockSize = mapper.getBlockSize();
        return null != blockSize && null != blockSize.get("block_size") ? blockSize.get("block_size") : 0;
    }

    @Override
    public void countSegmentIp(String segmentTableName, String segmentAddress, String ipTableName, String ipSegment) {
        mapper.countSegmentIp(segmentTableName, segmentAddress, ipTableName, ipSegment);
    }

    @Override
    public void countSegmentIp4Segment(String segmentTableName, String segmentAddress, String ipTableName, String ipSegment,
            String segmentAddressValue) {
        mapper.countSegmentIp4Segment(segmentTableName, segmentAddress, ipTableName, ipSegment, segmentAddressValue);
    }

    @Override
    public void syncIpBussiness(String ipCode, String segmentTableName, String segmentAddress, String ipTableName,
            String ipSegment) {
        mapper.syncIpBussiness(ipCode, segmentTableName, segmentAddress, ipTableName, ipSegment);
    }

    @Override
    public Integer listV3Count(Map<String, Object> params, String moduleType) {
        if (!params.containsKey("token")) {
            params.put("token", innerUserId);
        }
        if (!params.containsKey("condicationCode")) {
            params.put("condicationCode", "instance_list");
        }
        String moduleId = "";
        if (!params.containsKey("module_id") && !params.containsKey("device_type")) {
            Module module = moduleService.getDefaultModule(moduleType);
            params.put("module_id", module.getId());
            moduleId = module.getId();
        }
        Map<String, Object> queryParams = condctService.parseQuery(params);
        if (StringUtils.isEmpty(moduleId)) {
            moduleId = queryParams.get("query_module_id").toString();
        }
        queryParams.put("module_id", moduleId);
        if (queryDbType != null && queryDbType.toLowerCase(Locale.ENGLISH).equals("es")) {
            // 请求es数据
            log.info("Request es params -> {}", net.sf.json.JSONObject.fromObject(queryParams).toString());
            com.aspire.mirror.elasticsearch.api.dto.cmdb.Result result = cmdbESService.list(queryParams);
            return 0;
        }
        return getInstanceCount(queryParams);
    }

    private Integer getInstanceCount(Map<String, Object> queryParams) {
        Object[] formatQuerys = this.formatStatementParams(queryParams);
        StringBuilder queryBuilder = new StringBuilder((String) formatQuerys[0]);
        Map<String, Object> statementParams = (Map<String, Object>) formatQuerys[1];
        String moduleId = queryParams.get("query_module_id").toString();
        List<String> tabParamList = new ArrayList<>(statementParams.keySet());
        CmdbConfig cmdbConfig = cmdbConfigService.getConfigByCode("instance_tab_list");
        String queryCountSql = "";
        if (null != cmdbConfig && queryParams.containsKey("condicationCode4Tab")
                && "instance_list".equals(queryParams.get("condicationCode4Tab").toString())) {
            queryCountSql = moduleService.getModuleQueryCountSQL4Tab(moduleId, tabParamList);
        } else {
            queryCountSql = moduleService.getModuleQueryCountSQL(moduleId);
        }
        CmdbSqlManage countManage = new CmdbSqlManage(queryCountSql, moduleId, Constants.INSTANCE_AUTH_MODULE, Constants.NEED_AUTH);
        // 如果是IP管理模块，则跳过资源验证
        CmdbV3ModuleCatalog catalog = catalogService.getByModuleId(moduleId);
        CmdbV3ModuleCatalog topCatalog = catalogService.getTopCatalog(catalog.getId());
        if (Constants.MODULE_IP_REPOSITORY.equals(topCatalog.getCatalogCode())) {
            countManage = new CmdbSqlManage(queryCountSql, moduleId, Constants.INSTANCE_AUTH_MODULE, Constants.UN_NEED_AUTH);
        }
        Integer count = jdbcHelper.getInt(countManage, queryBuilder.toString(), statementParams);
        return count;
    }

    private Integer getInstanceList4Count(Map<String, Object> queryParams) {
        List<Map<String, Object>> paramsList = (List<Map<String, Object>>) queryParams.get("params");
        StringBuilder queryBuilder = new StringBuilder();
        Map<String, Object> statementParams = new LinkedMap();
        for (Map<String, Object> params : paramsList) {
            this.assessKey(params, "filed",
                    "Format params error. querySetting.params every member must has filed、operator、value properties.");
            this.assessKey(params, "operator",
                    "Format params error. querySetting.params every member must has filed、operator、value properties.");
            this.assessKey(params, "value",
                    "Format params error. querySetting.params every member must has filed、operator、value properties.");
            String operator = params.get("operator").toString().trim();
            String filed = params.get("filed").toString().trim();
            Object value = params.get("value");
            if (StringUtils.isNotEmpty(value)) {
                switch (operator.toLowerCase(Locale.ENGLISH)) {
                    case "like":
                        queryBuilder.append(jdbcHelper.likeSql(filed, true));
                        statementParams.put(filed, value);
                        break;
                    case "<":
                    case "<=":
                        queryBuilder.append(jdbcHelper.lteSql(filed, true));
                        statementParams.put(filed, value);
                        break;
                    case ">":
                    case ">=":
                        queryBuilder.append(jdbcHelper.gteSql(filed, true));
                        statementParams.put(filed, value);
                        break;
                    case "in":
                        queryBuilder.append(jdbcHelper.inSql(filed, true));
                        statementParams.put(filed, value);
                        break;
                    case "=":
                        queryBuilder.append(jdbcHelper.eqSql(filed, true));
                        statementParams.put(filed, value);
                        break;
                    case "not in":
                        queryBuilder.append(jdbcHelper.notInSql(filed, true));
                        statementParams.put(filed, value);
                        break;
                    case "between":
                        List<String> valueList = (List<String>) value;
                        queryBuilder.append(jdbcHelper.betweenSql(filed, true));
                        statementParams.put(filed, value);
                        statementParams.put(filed + "_start", valueList.get(0));
                        statementParams.put(filed + "_end", valueList.get(1));
                        break;
                    default:
                        throw new RuntimeException("Don't support operator type [" + operator + "]");
                }
            }
        }
        // 处理排序
        StringBuilder sortBuilderSQL = new StringBuilder();
        if (queryParams.containsKey("sort")) {
            List<Map<String, Object>> sortList = (List<Map<String, Object>>) queryParams.get("sort");
            for (Map<String, Object> sortMap : sortList) {
                if (sortMap.get("filed") != null) {
                    String sortFiled = sortMap.get("filed").toString();
                    if (sortMap.get("type") != null) {
                        if (!("").equals(sortBuilderSQL.toString())) {
                            sortBuilderSQL.append(",");
                        }
                        String sortType = sortMap.get("type").toString();
                        // 修改需要特殊排序的字段 modify by fanwenhui 20200730
                        boolean index = checkInstanceListSortField(queryParams.get("index").toString(), sortFiled);
                        sortBuilderSQL.append(" IFNULL(").append(sortFiled).append(",'') = '' , ");
                        if (index) {
                            sortBuilderSQL.append(" INET_ATON(").append(sortFiled).append(") ");
                        } else {
                            sortBuilderSQL.append(sortFiled).append(" ");
                        }
                        sortBuilderSQL.append(sortType).append(" ");
                    }
                }
            }
        }
        String moduleId = queryParams.get("module_id").toString();
        String querySQL = moduleService.getModuleQuerySQL(moduleId);
        // 增加查询条件
        if (!("").equals(queryBuilder.toString())) {
            querySQL += queryBuilder.toString();
        }
        // 增加排序
        if (!("").equals(sortBuilderSQL.toString())) {
            querySQL += " order by " + sortBuilderSQL.toString();
        }
        CmdbSqlManage sqlManage = new CmdbSqlManage(querySQL, moduleId, Constants.INSTANCE_AUTH_MODULE, Constants.NEED_AUTH);
        return jdbcHelper.getInt(sqlManage, null, statementParams);
    }

    /**
     * 检查哪些字段需要对排序做特殊处理，如IP排序使用 INET_ATON()
     * 
     * @param tableName
     *            主表名称
     * @param sortField
     *            排序字段
     * @return true - 需要特殊排序处理
     */
    private boolean checkInstanceListSortField(String tableName, String sortField) {
        // 获取特殊排序配置字段信息,instance_search_sort, create by fanwenhui 20200730
        CmdbConfig cmdbConfig = cmdbConfigService.getConfigByCode("instance_search_sort");
        if (null == cmdbConfig) {
            return false;
        }
        Map<String, String> configFiledMap = (Map<String, String>) JSONObject.parse(cmdbConfig.getConfigValue());
        if (null == configFiledMap) { // 没有该字段配置
            return false;
        }
        String configField = configFiledMap.get(tableName); // 需要特殊排序的字段
        if (StringUtils.isEmpty(configField)) { // 没有该模型表的特殊排序字段
            return false;
        }
        if (!configField.contains(sortField)) {
            return false;
        }
        return true;
    }

    @Data
    class QueryStatement {

        private String partSql;

        private Map<String, Object> params;
    }

    @Override
    @Transactional(rollbackFor = { RuntimeException.class, Exception.class, SQLException.class })
    public String deleteInstanceNoApprove(String userName, List<Map<String, Object>> instanceList, String operateType) {
        String msg = "";
        Integer deleteCount = 0;
        for (Map<String, Object> instanceData : instanceList) {
            SimpleModule module = validModule(instanceData);
            CmdbCollectApproval approval = handleDeleteApproval(userName, instanceData, operateType);
            // 不用审核
            this.delete(userName, approval);
            deleteCount++;
        }
        if (deleteCount > 0) {
            msg = "成功删除实例:" + deleteCount + "条";
        }
        return msg;
    }

    @Override
    @Transactional(rollbackFor = { RuntimeException.class, Exception.class, SQLException.class })
    public String deleteInstancePysicalNoApprove(String userName, List<Map<String, Object>> instanceList, String operateType) {
        String msg = "";
        Integer deleteCount = 0;
        for (Map<String, Object> instanceData : instanceList) {
            // SimpleModule module = validModule(instanceData);
            CmdbCollectApproval approval = handleDeleteApproval(userName, instanceData, operateType);
            // 不用审核
            this.deletePhysic(approval);
            deleteCount++;
        }
        if (deleteCount > 0) {
            msg = "成功删除实例:" + deleteCount + "条";
        }
        return msg;
    }
}
