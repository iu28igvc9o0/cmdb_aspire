package com.aspire.ums.cmdb.v2.collect.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aspire.ums.cmdb.code.payload.CmdbCode;
import com.aspire.ums.cmdb.code.payload.CmdbControlType;
import com.aspire.ums.cmdb.code.payload.CmdbSimpleCode;
import com.aspire.ums.cmdb.collectApproval.payload.CmdbCollectApproval;
import com.aspire.ums.cmdb.collectApproval.payload.CmdbCollectApprovalQuery;
import com.aspire.ums.cmdb.common.Constants;
import com.aspire.ums.cmdb.helper.JDBCHelper;
import com.aspire.ums.cmdb.sqlManage.CmdbSqlManage;
import com.aspire.ums.cmdb.util.StringUtils;
import com.aspire.ums.cmdb.util.UUIDUtil;
import com.aspire.ums.cmdb.v2.code.service.ICmdbCodeService;
import com.aspire.ums.cmdb.v2.code.service.ICmdbControlTypeService;
import com.aspire.ums.cmdb.v2.collect.CollectConst;
import com.aspire.ums.cmdb.v2.collect.mapper.CmdbCollectApprovalMapper;
import com.aspire.ums.cmdb.v2.collect.service.CmdbCollectApprovalService;
import com.aspire.ums.cmdb.v2.instance.service.ICmdbInstanceService;
import com.aspire.ums.cmdb.v2.module.service.ModuleService;
import com.aspire.ums.cmdb.v3.config.payload.CmdbConfig;
import com.aspire.ums.cmdb.v3.config.service.ICmdbConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
* 描述：
* @author
* @date 2019-06-18 20:55:56
*/
@Service
@Slf4j
public class CmdbCollectApprovalServiceImpl implements CmdbCollectApprovalService {

    @Autowired
    private CmdbCollectApprovalMapper mapper;

    @Autowired
    private ICmdbInstanceService instanceService;
    @Autowired
    private ICmdbCodeService codeService;
    @Autowired
    private ICmdbControlTypeService controlTypeService;
    @Autowired
    private ModuleService moduleService;
    @Autowired
    private JDBCHelper jdbcHelper;
    @Autowired
    private ICmdbConfigService configService;
    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    public List<Map<String,Object>> list(CmdbCollectApprovalQuery approvalQuery) {
        if (StringUtils.isEmpty(approvalQuery.getModuleId())) {
            throw new RuntimeException("请先选择模型");
//            return mapper.listByQuery(approvalQuery);
        }
        StringBuilder approvalSql = toHandleApprovalSql(approvalQuery);
        StringBuilder querySql = toHandleQuerySql(approvalQuery, approvalSql.toString());
        String whereSql = toHandleWhereSql(approvalQuery, approvalSql.toString());
        if (approvalQuery.getPageSize() != null && approvalQuery.getPageNum() != null) {
            querySql.append(" limit ").append((approvalQuery.getPageNum() - 1) * approvalQuery.getPageSize());
            querySql.append(",").append(approvalQuery.getPageSize());
        }
        CmdbSqlManage sqlManage = new CmdbSqlManage(querySql.toString(), approvalQuery.getModuleId(),null, Constants.NEED_AUTH);
        List<Map<String,Object>> resultList = jdbcHelper.getQueryList(sqlManage, whereSql, null, null, null);
        return resultList;
    }

    private String toHandleWhereSql(CmdbCollectApprovalQuery approvalQuery, String approvalSql) {
        // 模型数据过滤条件
        StringBuilder mWhereSql = new StringBuilder();
        mWhereSql.append(" and id in (select instanceId from (").append(approvalSql).append(") q)");
        return mWhereSql.toString();
    }

    private String toHandlePrimarWhereSql(CmdbCollectApprovalQuery approvalQuery ) {
        String moduleId = approvalQuery.getModuleId();
        Map<String, String> primaryQuery = approvalQuery.getPrimaryQuery();
        StringBuilder primaryWhereSql = new StringBuilder("");
        if (primaryQuery.size() > 0) {
            for (String key : primaryQuery.keySet()) {
                if (StringUtils.isEmpty(primaryQuery.get(key))) {
                    continue;
                }
                CmdbSimpleCode code = codeService.getSimpleCodeByCodeAndModuleId(moduleId, key);
                CmdbControlType type = controlTypeService.getById(code.getControlTypeId());
                primaryWhereSql.append("and (").append(key);
                if ("否".equals(type.getIsBindSource())) {
                    primaryWhereSql.append(" like '%").append(primaryQuery.get(key)).append("%' ");
                } else {
                    primaryWhereSql.append(" = '").append(primaryQuery.get(key)).append("'");
                }
                primaryWhereSql.append(" or JSON_UNQUOTE(JSON_EXTRACT(resourceData, '$.").append(key).append("'))").append(" like '%").append(primaryQuery.get(key)).append("%') ");
            }
        }
        return primaryWhereSql.toString();
    }

    private StringBuilder toHandleQuerySql(CmdbCollectApprovalQuery approvalQuery, String approvalSql) {
        String moduleId = approvalQuery.getModuleId();
        CmdbConfig config = configService.getConfigByCode("primaryKey:" + moduleId);
        if (config == null) {
            throw new RuntimeException("模型[" + moduleId + "]未配置主键信息");
        }
        String primaryKeys = toHandlePKeySql(config, moduleId);
//        String perSql = toHandlePerSql(moduleId);
        String baseSql = moduleService.getModuleQuerySQL(moduleId);
        // 最终SQL
        StringBuilder querySql = new StringBuilder();
        StringBuilder moduleSql = new StringBuilder(baseSql);
        // 最后sql拼接
        querySql.append("select app.*").append(primaryKeys).append(" from (").append(approvalSql).append(") app ");
//        querySql.append("left join (").append(moduleSql).append(") md on app.instanceId = md.id where (approvalType != 'add' AND md.id IS NOT NULL) OR " +
//                "approvalType = 'add' ");
        querySql.append("left join (").append(moduleSql).append(") md on app.instanceId = md.id where 1=1 ");
        querySql.append(toHandlePrimarWhereSql(approvalQuery));
        querySql.append(" and  (approvalType ='add' or (approvalType != 'add' and md.id is not null) ) ");
        querySql.append(" ORDER BY operatorTime DESC");
        return querySql;
    }

    private String toHandlePerSql(String moduleId) {
        StringBuilder perSql = new StringBuilder();
        Map<String, String> permissionKeys = jdbcHelper.getModuleCatalogRightMapper(moduleId);
        if (permissionKeys == null) {
            return perSql.toString();
        }
        for (String pKey : permissionKeys.values()) {
            if ("id".equals(pKey)) {
                continue;
            }
            perSql.append( ",JSON_UNQUOTE(JSON_EXTRACT(resource_data,'$.").append(pKey).append("')) ").append(pKey).append(" ");
        }
        return perSql.toString();
    }

    private String toHandlePKeySql(CmdbConfig config, String moduleId) {
        String primaryKeySql = "";
        String primaryValue = config.getConfigValue();
        // 列数据用来判断取字段对应数据中文
        Map<String, Map<String, String>> moduleColumns = moduleService.getModuleColumns(moduleId);
        List<String> primaryKeyList = Arrays.asList(primaryValue.split(","));
        for (String key : primaryKeyList) {
            primaryKeySql += ",";
            if (StringUtils.isNotEmpty(moduleColumns.get(key).get("ref_name"))) {
                primaryKeySql += moduleColumns.get(key).get("ref_name");
                primaryKeySql += " " + key;
            } else {
                primaryKeySql += key;
            }
        }

        return primaryKeySql;
    }

    private StringBuilder toHandleApprovalSql(CmdbCollectApprovalQuery approvalQuery) {
        StringBuilder approvalSql = new StringBuilder();
        StringBuilder appWhereSql = new StringBuilder();
        String camelFiled = "id, module_id moduleId, instance_id instanceId,code_id codeId,filed_name filedName," +
                "old_value oldValue,curr_value currValue, approval_status approvalStatus," +
                "approval_type approvalType,approval_user approvalUser, approval_time approvalTime," +
                "approval_describe approvalDescribe,operater_type operaterType, operator,operator_time operatorTime," +
                "resource_data resourceData,owner_module_id ownerModuleId ";
        String perSql = toHandlePerSql(approvalQuery.getModuleId());
        approvalSql.append("select ").append(camelFiled).append(" from (select a.*").append(perSql).append(",b.filed_name from cmdb_collect_approval a" +
                " left join cmdb_code b on a.code_id = b.code_id and b.is_delete=0 ) cca where 1=1");
        if (StringUtils.isNotEmpty(approvalQuery.getModuleId())) {
            appWhereSql.append(" and module_id = '").append(approvalQuery.getModuleId()).append("'");
        }
        if (StringUtils.isNotEmpty(approvalQuery.getStartTime())) {
            appWhereSql.append(" and operator_time >= '").append(approvalQuery.getStartTime()).append("'");
        }
        if (StringUtils.isNotEmpty(approvalQuery.getEndTime())) {
            appWhereSql.append(" and operator_time <= '").append(approvalQuery.getEndTime()).append("'");
        }
        if (StringUtils.isNotEmpty(approvalQuery.getCodeFiledName())) {
            appWhereSql.append(" and code_id = '").append(approvalQuery.getCodeFiledName()).append("'");
        }
        if (StringUtils.isNotEmpty(approvalQuery.getOperaterType())) {
            appWhereSql.append(" and operater_type = '").append(approvalQuery.getOperaterType()).append("'");
        }
        if (StringUtils.isNotEmpty(approvalQuery.getApprovalType())) {
            appWhereSql.append(" and `approval_type` = '").append(approvalQuery.getApprovalType()).append("'");
        }
        if (StringUtils.isNotEmpty(approvalQuery.getOperator())) {
            appWhereSql.append(" and `operator` like '%").append(approvalQuery.getOperator()).append("%'");
        }
        appWhereSql.append("and (approval_type = 'add' [<auth>] or approval_type !='add')");

        return approvalSql.append(appWhereSql);
    }


    @Override
    public Integer listCount(CmdbCollectApprovalQuery approvalQuery) {
        if (StringUtils.isEmpty(approvalQuery.getModuleId())) {
            return  mapper.listCount(approvalQuery);
        }
        StringBuilder approvalSql = toHandleApprovalSql(approvalQuery);
        StringBuilder querySql = toHandleQuerySql(approvalQuery, approvalSql.toString());
        String whereSql = toHandleWhereSql(approvalQuery, approvalSql.toString());
        CmdbSqlManage sqlManage = new CmdbSqlManage(querySql.toString(), approvalQuery.getModuleId(),null,Constants.NEED_AUTH);
        return jdbcHelper.getInt(sqlManage, whereSql, null);
    }

    @Override
    public List<CmdbCollectApproval> listSimpleByQuery(CmdbCollectApprovalQuery approvalQuery) {
        if (approvalQuery.getPrimaryQuery() == null ) {

        }
        List<Map<String, Object>> apps = list(approvalQuery);
//        return (List<CmdbCollectApproval>)apps;
        return JSONObject.parseArray(JSON.toJSONString(apps), CmdbCollectApproval.class);
//        return mapper.listSimpleByQuery(approvalQuery);
    }

    @Override
    public List<CmdbCollectApproval> listByIds(List<String> ids) {
        return mapper.listByIds(ids);
    }


    @Override
    public List<CmdbSimpleCode> getApprovalHeaderCode(String moduleId) {
//      定制返回审核相关主键
        CmdbConfig primaryConfig = configService.getConfigByCode("primaryKey:" + moduleId);
        if (primaryConfig == null) {
            throw new RuntimeException("模型" + moduleId + "未配置主键信息.");
        }
        String primaryKeysStr = primaryConfig.getConfigValue();
        List<String> primaryKeyList= Arrays.asList(primaryKeysStr.split(","));
        List<CmdbSimpleCode> codeList = new LinkedList<>();
        // 主键分割
        for (String primaryKey : primaryKeyList) {
            CmdbSimpleCode code = codeService.getSimpleCodeByCodeAndModuleId(moduleId, primaryKey);
            codeList.add(code);
        }
        return codeList;
    }

    @Override
    public CmdbCollectApproval getDetail(String instanceId, String codeId) {
        CmdbCollectApproval approval = new CmdbCollectApproval();
        approval.setInstanceId(instanceId);
        approval.setCodeId(codeId);
        return mapper.get(approval);
    }

    /**
     * 根据主键ID 获取数据信息
     * @param entity 实例信息
     * @return 返回指定ID的数据信息
     */
    public CmdbCollectApproval get(CmdbCollectApproval entity) {
        return mapper.get(entity);
    }


    /**
     * 新增实例
     * @param entity 实例数据
     * @return
     */
    @Override
    public void insert(CmdbCollectApproval entity) {
        validApproval(entity);
        List<CmdbCollectApproval> approvals = new ArrayList<>();
        approvals.add(entity);
        this.insertByBatch(approvals);
    }

    @Override
    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    public void insertByBatch(List<CmdbCollectApproval> entities) {
        List<CmdbCollectApproval> updateApprovals = new ArrayList<>();
        List<CmdbCollectApproval> insertApprovals = new ArrayList<>();
        for (CmdbCollectApproval approval : entities) {
            //简单检验
            validApproval(approval);
            //如果没有操作时间
            if (approval.getOperatorTime() == null) {
                approval.setOperatorTime(new Date());
            }
            if (approval.getApprovalStatus() == null) {
                approval.setApprovalStatus(0);
            }
            //如果有CI的ID则需要判断之前有无同字段配置审核，有则更新
            if (StringUtils.isNotEmpty(approval.getInstanceId())) {
                //查数据库中是否还有待处理数据
                CmdbCollectApproval query = new CmdbCollectApproval();
                query.setCodeId(approval.getCodeId());
                if (StringUtils.isEmpty(approval.getCodeId())) {
                    query.setCodeId("null");
                }
                query.setInstanceId(approval.getInstanceId());
                query = mapper.get(query);
                //如果有待处理记录则更新该条记录 // todo 这里需要处理下.
                if (query != null) {
                    approval.setId(query.getId());
                    updateApprovals.add(approval);
                } else {
                    //否则新增记录
                    approval.setId(UUIDUtil.getUUID());
                    insertApprovals.add(approval);
                }
            } else {
                //否则新增CI
                approval.setId(UUIDUtil.getUUID());
                insertApprovals.add(approval);
            }
        }
        //新增审核数据
        if (insertApprovals.size() > 0) {
            mapper.insertByBatch(insertApprovals);
        }
        //更新CI已存在字段数据
        if (updateApprovals.size() > 0) {
            for (CmdbCollectApproval approval : updateApprovals) {
                mapper.update(approval);
            }
        }
    }

    private void approveCode(CmdbCode code, Object codeValue) {
        // 验证数据
        Map<String, Object> codeParam = new HashMap<>();
        codeParam.put(code.getCodeId(), codeValue);
        Map<String, Map<String, Object>> validResults = codeService.approveCodeValue(codeParam);
        Map<String, Object> validResult = validResults.get(code.getCodeId());
        if ("error".equals(validResult.get("flag"))) {
            throw new RuntimeException("自动审核数据出错: " + validResult.get("msg"));
        }
    }

    //检验传参是否合法
    private void validApproval(CmdbCollectApproval approval) {
        if (StringUtils.isEmpty(approval.getOperator())) {
            throw new RuntimeException("数据异常，未知操作用户");
        }
        //判断模型id是否有给
        if (StringUtils.isEmpty(approval.getModuleId())) {
            throw new RuntimeException("数据异常，未传参模型ID!");
        }
        //判断如果是新增的情况下是否有给数据源
        if (StringUtils.isEmpty(approval.getInstanceId()) && StringUtils.isEmpty(approval.getResourceData())) {
            throw new RuntimeException("数据异常，新增CI数据源未传");
        }
        //判断如果是更新的情况下是否有传字段信息--删除时些状态不适用故注释
//        if (StringUtils.isNotEmpty(approval.getInstanceId()) && StringUtils.isEmpty(approval.getCodeId())) {
//            throw new RuntimeException("数据异常，更新CI字段未传");
//        }
    }

    /**
     * 删除实例
     * @param approval 实例数据
     * @return
     */
    @Override
    @Transactional(rollbackFor = { RuntimeException.class, Exception.class })
    public void delete(CmdbCollectApproval approval) {
        long startTime = new Date().getTime();
        try {
            mapper.delete(approval);
            log.info("--------- 从审核库删除数据 消耗时间: {} ms", ((new Date()).getTime() - startTime));
            startTime = new Date().getTime();
        } catch (Exception e) {
            throw new RuntimeException(" 从审核库删除数据失败,error:" + e.getMessage());
        }
        try {
            List<CmdbCollectApproval> approvals = new ArrayList<>();
            approvals.add(approval);
            kafkaTemplate.send("cmdb_approval_info", JSON.toJSONString(approvals));
            log.info("--------- 发送数据至kafka 消耗时间: {} ms", ((new Date()).getTime() - startTime));
        } catch (Exception e) {
            throw new RuntimeException("数据发送到kafka失败,error:" + e.getMessage());
        }

    }
    /**
     * 批量删除实例
     * @param approvals 实例数据
     * @return
     */
    @Override
    @Transactional(rollbackFor = { RuntimeException.class, Exception.class })
    public void deleteByBatch(List<CmdbCollectApproval> approvals) {
        long startTime = new Date().getTime();
        try {
            mapper.deleteByBatch(approvals);
            log.info("--------- 从审核库删除{}条数据 消耗时间: {} ms",approvals.size(), ((new Date()).getTime() - startTime));
        } catch (Exception e) {
            throw new RuntimeException(" 从审核库删除数据失败,error:" + e.getMessage());
        }
        startTime = new Date().getTime();
        try {
            kafkaTemplate.send("cmdb_approval_info", JSON.toJSONString(approvals));
            log.info("--------- 发送数据至kafka 消耗时间: {} ms", ((new Date()).getTime() - startTime));
        } catch (Exception e) {
            throw new RuntimeException("数据发送至kafka失败,error:" + e.getMessage());
        }
   }


    @Override
    public List<Map<String, Object>> approve(String userName, Integer approvalStatus, String approvalDescribe, List<CmdbCollectApproval> approvalList) {
        List<Map<String, Object>> returnList = new ArrayList<>();
        if (approvalList == null || approvalList.size() == 0) {
            return returnList;
        }
        long startTime = System.currentTimeMillis();
//        // 一批数据的状态应该一样的(要么是驳回要么是通过)
//        // 如果是驳回则直接发往kafka
        if (approvalStatus == CollectConst.APPROVE_STATUS_REFUSE.intValue()) {
            // 完善审核信息
            List<CmdbCollectApproval> needHandleApprovals = new ArrayList<>();
            for (CmdbCollectApproval approval : approvalList) {
                try {
                    approval = fillData(userName, approvalStatus, approvalDescribe, approval);
                    needHandleApprovals.add(approval);
                } catch (Exception e) {
                    this.setReturnList(returnList, approval, "error", "CI审核失败:" + e.getMessage());
                    continue;
                }
            }
            try {
                if (needHandleApprovals.size() > 0) {
                    deleteByBatch(needHandleApprovals);
                }
            } catch (Exception e) {
                for (CmdbCollectApproval approval : needHandleApprovals) {
                    this.setReturnList(returnList, approval, "error", e.getMessage());
                }
            }
            return returnList;
        } else if (approvalStatus == CollectConst.APPROVE_STATUS_PASS.intValue()) {
            // 如果是通过则进行CI新增或更新
            List<CmdbCollectApproval> updateApprovals = new ArrayList<>();
            List<CmdbCollectApproval> deleteApprovals = new ArrayList<>();
            List<CmdbCollectApproval> passList = new ArrayList<>();
            for (CmdbCollectApproval approval : approvalList) {
                try {
                    //完善数据
                    approval = fillData(userName, approvalStatus, approvalDescribe,approval);
                    log.info(">>>>>> 完善数据 耗时：{}", System.currentTimeMillis() - startTime);
                    startTime = System.currentTimeMillis();
                } catch (Exception e) {
                    this.setReturnList(returnList, approval, "error", "CI完善数据失败:" + e.getMessage());
                    continue;
                }
                //如果是新增CI数据(一条条去处理)
                if ("add".equals(approval.getApprovalType())) {
                    startTime = new Date().getTime();
                    try {
                        //完善数据
                        instanceService.insert(userName, approval);
                        startTime = System.currentTimeMillis();
                        passList.add(approval);
                    } catch (Exception e) {
                        this.setReturnList(returnList, approval, "error", "CI新增入库失败:" + e.getMessage());
                        log.info("--------- CI新增入库失败 error:{} 消耗时间: {} ms", e.toString(), ((new Date()).getTime() - startTime));
                    }
                } else if ("delete".equals(approval.getApprovalType())) {
                    deleteApprovals.add(approval);
                } else {
                    updateApprovals.add(approval);
                }
            }
            // 批量更新CI数据
            if (updateApprovals.size() > 0) {
                try {
                    instanceService.update(userName, updateApprovals);
                    passList.addAll(updateApprovals);
                } catch (Exception e) {
                    log.error("数据更新入库失败.", e);
                    for (CmdbCollectApproval approval : updateApprovals) {
                        this.setReturnList(returnList, approval, "error", "数据更新入库失败:" + e.toString());
                    }
                }
            }
            if (deleteApprovals.size() > 0) {
                List<CmdbCollectApproval> delApprovals = new ArrayList<>();
                for (CmdbCollectApproval approval : deleteApprovals) {
                    try {
                        instanceService.delete(userName, approval);
                        delApprovals.add(approval);

                    } catch (Exception e) {
                        log.error("删除数据失败, error:{} " + e.toString());
                        this.setReturnList(returnList, approval, "error", "删除数据失败:" + e.toString());
                    }
                }
                passList.addAll(delApprovals);
            }
            // 移除审核表待审核数据
            try {
                if (passList.size() > 0) {
                    deleteByBatch(passList);
                }
            } catch (Exception e) {
                for (CmdbCollectApproval approval : passList) {
                    this.setReturnList(returnList, approval, "error", e.getMessage());
                }
            }
        }
        return returnList;
    }

    private CmdbCollectApproval fillData(String userName, Integer approvalStatus, String approvalDescribe,CmdbCollectApproval approval) {
        //判断当前审核数据是否还是待审核状态
        if (StringUtils.isNotEmpty(approval.getId())) {
            validApproveData(approval);
        }
        if (approval.getApprovalType().equals("update")){
            CmdbSimpleCode code = codeService.getSimpleCodeById(approval.getCodeId());
            approval.setFiledName(code.getFiledName());
         }
        approval.setApprovalUser(userName);
        approval.setApprovalStatus(approvalStatus);
        approval.setApprovalDescribe(approvalDescribe);
        approval.setApprovalTime(new Date());
        return approval;
    }

    private void validApproveData(CmdbCollectApproval approval) {
        // 判断数据库里否存在
        long startTime = new Date().getTime();
        CmdbCollectApproval approvalQuery = new CmdbCollectApproval();
        approvalQuery.setId(approval.getId());
        approvalQuery = mapper.get(approvalQuery);
        if (approvalQuery == null) {
            throw new RuntimeException("该配置已被其它进程审核");
        }
       log.info("---------判断当前配置项是否还在等待审批 消耗时间: {} ms", ((new Date()).getTime() - startTime));
    }



    @Override
    public List<Map<String, String>> getFiledNameList() {
        return mapper.getFiledNameList();
    }

    @Override
    public List<Map<String, String>> getOperatorTypeList() {
        return mapper.getOperatorTypeList();
    }

    private void setReturnList(List<Map<String, Object>> returnList, CmdbCollectApproval approval, String flag, String msg) {
        Map<String, Object> returnMap = new LinkedHashMap<>();
        returnMap.put("data", approval);
        returnMap.put("flag", flag);
        returnMap.put("msg", msg);
        returnList.add(returnMap);
    }


}
