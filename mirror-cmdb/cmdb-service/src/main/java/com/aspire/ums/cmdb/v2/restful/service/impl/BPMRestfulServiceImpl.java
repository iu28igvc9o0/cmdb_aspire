package com.aspire.ums.cmdb.v2.restful.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.aspire.ums.cmdb.client.IRbacDepartmentClient;
import com.aspire.ums.cmdb.client.IRbacUserClient;
import com.aspire.ums.cmdb.cmic.util.EventThreadUtils;
import com.aspire.ums.cmdb.common.Constants;
import com.aspire.ums.cmdb.common.Result;
import com.aspire.ums.cmdb.helper.JDBCHelper;
import com.aspire.ums.cmdb.report.service.ICmdb31ProvinceReportService;
import com.aspire.ums.cmdb.sqlManage.CmdbSqlManage;
import com.aspire.ums.cmdb.util.StringUtils;
import com.aspire.ums.cmdb.util.UUIDUtil;
import com.aspire.ums.cmdb.v2.instance.service.ICmdbInstanceService;
import com.aspire.ums.cmdb.v2.module.service.ModuleService;
import com.aspire.ums.cmdb.v2.restful.mapper.BPMRestfulMapper;
import com.aspire.ums.cmdb.v2.restful.service.IBPMRestfulService;
import com.aspire.ums.cmdb.v3.config.payload.CmdbConfig;
import com.aspire.ums.cmdb.v3.config.service.ICmdbConfigService;
import com.aspire.ums.cmdb.v3.dictMapper.payload.CmdbDictMapperEntity;
import com.aspire.ums.cmdb.v3.dictMapper.payload.CmdbSyncFiledMapperEntity;
import com.aspire.ums.cmdb.v3.dictMapper.service.ICmdbDictMapperService;
import com.aspire.ums.cmdb.v3.dictMapper.service.ICmdbSyncFiledMapperService;
import com.migu.tsg.microservice.atomicservice.rbac.dto.model.DepartmentDTO;
import com.migu.tsg.microservice.atomicservice.rbac.dto.vo.UserVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;

@Service
@Slf4j
public class BPMRestfulServiceImpl implements IBPMRestfulService {

    @Autowired
    private ICmdbInstanceService instanceService;
    @Autowired
    private ICmdbConfigService configService;
    @Autowired
    private BPMRestfulMapper bpmRestfulMapper;
    @Autowired
    private ICmdbDictMapperService dictMapperService;
    @Autowired
    private ModuleService moduleService;
    @Autowired
    private JDBCHelper jdbcHelper;
    @Autowired
    private ICmdbSyncFiledMapperService filedMapperService;
    @Autowired
    private IRbacUserClient rbacUserClient;
    @Autowired
    private IRbacDepartmentClient departmentClient;
    @Autowired
    private ICmdb31ProvinceReportService provinceReportService;
    /**
     * 对接BPM资源申请流程
     * @param resourceInfo 申请资源信息
     *  {
     *    "bizSystem": "业务系统",
     *    "idcType": "资源池名称",
     *    "request_type": "request/release",
     *    "pod": "POD名称",
     *    "data": [{
     *          "type": "资源类型",
     *          "num": "申请数量",
     *          "cpu": "云主机CPU数量",
     *          "memory": "云主机内存数量"
     *       }]
     *  }
     */
    @Override
    public Map<String, Object> resourceRequestProcess(Map<String, Object> resourceInfo) {
        // 基本校验
        Map<String, Object> returnMap = new HashMap<>();
        if (resourceInfo.isEmpty()) {
            returnMap.put("flag", "error");
            returnMap.put("msg", "请求信息不能为空.");
            return returnMap;
        }
        if (!resourceInfo.containsKey("bizSystem") || !StringUtils.isNotEmpty(resourceInfo.get("bizSystem"))) {
            returnMap.put("flag", "error");
            returnMap.put("msg", "请求信息中缺少业务系统信息.");
            return returnMap;
        }
        if (!resourceInfo.containsKey("idcType") || !StringUtils.isNotEmpty(resourceInfo.get("idcType"))) {
            returnMap.put("flag", "error");
            returnMap.put("msg", "请求信息中缺少资源池信息.");
            return returnMap;
        }
        if (!resourceInfo.containsKey("data")) {
            returnMap.put("flag", "error");
            returnMap.put("msg", "请求信息中缺少申请配额信息.");
            return returnMap;
        }
        if (!resourceInfo.containsKey("request_type")) {
            returnMap.put("flag", "error");
            returnMap.put("msg", "请求信息中缺少申请类型.");
            return returnMap;
        }
        String bizSystem = resourceInfo.get("bizSystem").toString();
        String idcType = resourceInfo.get("idcType").toString();
        String podName = null;
        if (resourceInfo.containsKey("pod") && !StringUtils.isNotEmpty(resourceInfo.get("pod"))) {
            podName = resourceInfo.get("pod").toString();
        }
        String requestType = resourceInfo.get("request_type").toString().toLowerCase(Locale.ENGLISH);
        Map<String, Object> params = new HashMap<>();
        params.put("idcType", idcType);
        params.put("bizSystem", bizSystem);
        if (podName != null) {
            params.put("podName", podName);
        }
        List<Map<String, Object>> quoteList = bpmRestfulMapper.queryBizSystemQuote(params);
        Map<String, Object> quoteMap = null;
        if (quoteList != null && quoteList.size() > 0) {
            quoteMap = quoteList.get(0);
        }
        CmdbDictMapperEntity queryEntity = new CmdbDictMapperEntity();
        queryEntity.setMapperSource("BPM");
        queryEntity.setMapperDictType("BPM资源申请工单");
        List<CmdbDictMapperEntity> entityList = dictMapperService.listByEntity(queryEntity);
        if (entityList == null || entityList.size() == 0) {
            returnMap.put("flag", "error");
            returnMap.put("msg", "请先配置cmdb_dict_mapper的工单资源类型关系.");
            return returnMap;
        }
        Map<String, Object> requestData = new HashMap<>();
        // 处理数据
        List<Map<String, Object>> dataList = (List<Map<String, Object>>) resourceInfo.get("data");
        for (Map<String, Object> data : dataList) {
            String resourceType = data.get("type").toString();
            String number = data.get("num").toString();
            String cpu = null, memory = null;
            if (data.containsKey("cpu") && StringUtils.isNotEmpty(data.get("cpu"))) {
                cpu = data.get("cpu").toString();
            }
            if (data.containsKey("memory") && StringUtils.isNotEmpty(data.get("memory"))) {
                memory = data.get("memory").toString();
            }
            // 匹配类型数据
            for (CmdbDictMapperEntity dictEntity : entityList) {
                if (dictEntity.getMapperDictCode().equals(resourceType)) {
                    if (StringUtils.isEmpty(dictEntity.getUmsDictCode())) {
                        log.error("cmdb_dict_mapper未配置ums_dict_code, 请先配置.");
                        continue;
                    }
                    requestData.put(dictEntity.getUmsDictCode(), number);
                    if (resourceType.equals("云主机")) {
                        if (cpu != null) {
                            requestData.put("yzj_vcpu_allocation_amount", cpu);
                        }
                        if (memory != null) {
                            requestData.put("yzj_memory_allocation_amount", memory);
                        }
                    }
                }
            }
        }
        // 新增
        if (quoteMap == null) {
            if (requestType.equals("request")) {
                // 获取业务系统、资源池、POD的ID
                String idcId = moduleService.getIDByCNName("idcType", idcType);
                if (StringUtils.isEmpty(idcId)) {
                    returnMap.put("flag", "error");
                    returnMap.put("msg", "未找到资源池名称" + idcType + ", 请检测资源池是否在CMDB中管理.");
                    return returnMap;
                }
                String bizSystemId = moduleService.getIDByCNName("bizSystem", bizSystem);
                if (StringUtils.isEmpty(bizSystemId)) {
                    returnMap.put("flag", "error");
                    returnMap.put("msg", "未找到业务系统名称" + bizSystem + ", 请检测业务系统是否在CMDB中管理.");
                    return returnMap;
                }
                if (!StringUtils.isEmpty(podName)) {
                    String podId = moduleService.getIDByCNName("pod_name", podName);
                    if (StringUtils.isEmpty(podId)) {
                        returnMap.put("flag", "error");
                        returnMap.put("msg", "未找到POD名称" + podName + ", 请检测POD是否在CMDB中管理.");
                        return returnMap;
                    }
                    requestData.put("pod", podId);
                }
                // 获取资产配额
                CmdbConfig cmdbConfig = configService.getConfigByCode("business_quote_module_id", "38de00ee103b4bafb82489b3a0dc3311");
                String moduleId = cmdbConfig.getConfigValue();
                requestData.put("id", UUIDUtil.getUUID());
                requestData.put("module_id", moduleId);
                requestData.put("idcType", idcId);
                requestData.put("owner_biz_system", bizSystemId);
                instanceService.addInstance("BPM工单", requestData, "工单对接");
            }
        } else {
            for (String key : requestData.keySet()) {
                Object oldValue = quoteMap.get(key);
                if (requestType.equals("request")) {
                    quoteMap.put(key, plusValue(oldValue, requestData.get(key)));
                } else if (requestType.equals("release")) {
                    quoteMap.put(key, subtractValue(oldValue, requestData.get(key)));
                } else {
                    returnMap.put("flag", "error");
                    returnMap.put("msg", "不支持的处理类型" + requestType + ", 请检查配置.");
                    return returnMap;
                }
            }
            instanceService.updateInstance(quoteMap.get("id").toString(),"BPM工单", quoteMap, "工单对接");
        }
        returnMap.put("flag", "success");
        return returnMap;
    }

    @Override
    public Map<String, Object> syncOrgSystem(Map<String, Object> orgManagerData) {
        log.info("start to sync org system.");
        Map<String, Object> resultMap = new HashMap<>();
        String msg = "同步成功";
        boolean success = true;
        try {
            // 提取新增更新数据（删除以更新is_delete字段处理）
            List<Map> insertOrg = JSONArray.parseArray(JSON.toJSONString(orgManagerData.get("insertOrg")), Map.class);
            List<Map> updateOrg = JSONArray.parseArray(JSON.toJSONString(orgManagerData.get("updateOrg")), Map.class);
            List<Map> deleteOrg = new ArrayList<>();
            // 从更新数据中提取删除数据
            updateOrg.forEach(item -> {
                boolean isDelete = Boolean.parseBoolean(item.get("deleted").toString());
                if(isDelete) {
                    deleteOrg.add(item);
                }
            });
            // 从更新数据中移除删除数据
            updateOrg.removeAll(deleteOrg);
            // 合并新增更新数据，新增更0新以本地查询为准自己判断
            insertOrg.addAll(updateOrg);
            // 获取部门组织模型id配置
            CmdbConfig config = configService.getConfigByCode("org_manager_module_id");
            if (null == config) {
                success = false;
                throw new RuntimeException("同步失败，未配置部门租户模型信息[org_manager_module_id]");
            }
            String orgModuleId = config.getConfigValue();
            // 获取BPM组织同步相关字段映射
            CmdbSyncFiledMapperEntity queryEntity = new CmdbSyncFiledMapperEntity();
            queryEntity.setMapperType("部门组织");
            queryEntity.setSource("BPM");
            Map<String, String> filedMap = new HashMap<>();
            List<CmdbSyncFiledMapperEntity> filedMapList = filedMapperService.listByEntity(queryEntity);
            filedMapList.forEach(item -> {
                filedMap.put(item.getOtherFiledCode(), item.getUmsFiledCode());
            });
            // 获取模型基础查询sql
            String baseSql = moduleService.getModuleQuerySQL(orgModuleId);
            // 处理新增更新组织数据
            for (Map org : insertOrg) {
                EventThreadUtils.NORMAL_POOL.execute(()->{
                    try{
                        handleAddOrUpdate(orgModuleId, org, baseSql, filedMap);
                    } catch (Exception e) {
                        log.error("新增/更新同步失败，data: {}，error:{}", org, e.getMessage());
                    }
                });
            }
            // 处理删除组织数据
            List<Map<String,Object>> deleteList = new ArrayList<>();
            for (Map org : deleteOrg) {
                if (!org.containsKey("uuid") || !StringUtils.isNotEmpty(org.get("uuid"))) {
                    log.error("同步失败，BPM数据未提供UUID数据：{}",org.toString());
                    continue;
                }
                Map<String,Object> instanceData = new HashMap<>();
                String uuid = org.get("uuid").toString();
                Map<String, Object> params = new HashMap<>();
                params.put(filedMap.get("uuid"), uuid);
                String whereString = " and source_id = #{source_id} ";
                List<Map<String, Object>> exsitOrg= jdbcHelper.getQueryList(new CmdbSqlManage(baseSql, null ,Constants.UN_NEED_AUTH),whereString, null, null,params );
                if (exsitOrg != null && exsitOrg.size() > 0) {
                    instanceData.put("module_id", orgModuleId);
                    instanceData.put("id", exsitOrg.get(0).get("id"));
                    deleteList.add(instanceData);
                }
            }
            instanceService.deleteInstance("系统管理员", deleteList, "同步BPM组织");
        } catch (Exception e) {
            msg = e.getMessage();
        }

        resultMap.put("success", success);
        resultMap.put("msg", msg);
        return resultMap;
    }

    /**
     * 根据系统账号获取业务系统列表
     * 1. 如果账号有绑定业务系统, 则显示绑定的业务系统列表
     * 2. 如果账号没有绑定业务系统, 则显示账号归属的部门及该部门下所有子部门的业务系统列表
     *
     * @param account 系统账号
     * @return
     */
    @Override
    public Result<Map<String, Object>> getBizSystemListByAccount(String account, String bizSystem, int currentPage,int pageSize) {
        // 获取业务系统列表
        int bizCount = bpmRestfulMapper.getBizSystemListByAccountCount(account, bizSystem);
        List<Map<String, Object>> bizList = new LinkedList<>();
        if (bizCount > 0) {
            bizList = bpmRestfulMapper.getBizSystemListByAccount(account, bizSystem, currentPage, pageSize);
            return new Result<>(bizCount, bizList);
        }
        // 账号未绑定业务系统, 则查询账号归属的部门, 使用部门
        UserVO user = rbacUserClient.findByLdapId(account);
        List<DepartmentDTO> deptList = user.getDeptList();
        // 没有部门信息
        if (deptList == null || deptList.size() == 0) {
            return new Result<>(0, new LinkedList<>());
        }
        // 查询部门下的所有部门数据
        List<String> deptIdSet = new LinkedList<>();
        getDeptIdSet(deptIdSet, deptList);
        // 获取部门对应的
        bizCount = bpmRestfulMapper.getBizSystemListByOrgDepartmentIdsCount(deptIdSet, bizSystem);
        if (bizCount > 0) {
            bizList = bpmRestfulMapper.getBizSystemListByOrgDepartmentIds(deptIdSet, bizSystem, currentPage, pageSize);
        }
        return new Result<>(bizCount, bizList);
    }

    @Override
    public List<Map<String, Object>> listOrderReportData(String submitMonth) {
        return provinceReportService.listOrderReportData(submitMonth);
    }


    private void getDeptIdSet(List<String> deptIdSet, List<DepartmentDTO> departments) {
        for (DepartmentDTO department : departments) {
            deptIdSet.add(department.getUuid());
            List<DepartmentDTO> subDepartemnt = departmentClient.queryByDeptId(department.getUuid());
            if (!CollectionUtils.isEmpty(subDepartemnt)) {
                getDeptIdSet(deptIdSet, subDepartemnt);
            }
        }
    }

    void handleAddOrUpdate(String orgModuleId, Map org, String baseSql, Map<String, String> filedMap) {
            Map<String,Object> instanceData = new HashMap<>();
        instanceData.put("module_id", orgModuleId);
        if (!org.containsKey("uuid") || !StringUtils.isNotEmpty(org.get("uuid"))) {
            throw new RuntimeException("同步失败，BPM数据未提供UUID,数据：" + org.toString());
        }
        // 根据映射字段抽取对应数据
        for (String key : filedMap.keySet()) {
            if ("parent_id".equals(filedMap.get(key))) {
                if (!StringUtils.isNotEmpty(org.get(key))) {
                    instanceData.put(filedMap.get(key),"0");
                } else {
                    // 根据parent_id源值查询id
                    String parentId = org.get("parent_id").toString();
                    Map<String, Object> params = new HashMap<>();
                    params.put("source_id", parentId);
                    String whereString = " and source_id = #{source_id} ";
                    List<Map<String, Object>> exsitOrg= jdbcHelper.getQueryList(new CmdbSqlManage(baseSql, null ,Constants.UN_NEED_AUTH),whereString, null, null,params );
                    if (exsitOrg != null && exsitOrg.size() > 0) {
                        instanceData.put(filedMap.get(key), exsitOrg.get(0).get("id"));
                    } else {
                        instanceData.put(filedMap.get(key), "");
                    }

                }

            } else {
                instanceData.put(filedMap.get(key),org.get(key));
            }
        }
        String uuid = org.get("uuid").toString();
        String whereString = " and " + filedMap.get("uuid") + " = '" + uuid + "' ";
        List<Map<String, Object>> exsitOrg= jdbcHelper.getQueryList(new CmdbSqlManage(baseSql, null ,Constants.UN_NEED_AUTH),whereString, null, null,null );
        // 判断更新还是
        if (exsitOrg.size() > 0) {
            Map<String, Object> oldOrg = exsitOrg.get(0);
            instanceService.updateInstance(oldOrg.get("id").toString(), "系统管理员", instanceData, "同步BPM组织");
        } else {
            instanceService.addInstance("系统管理员",instanceData, "同步BPM组织");
        }
    }


    private Object plusValue(Object oldValue, Object plusValue) {
        try {
            if (oldValue == null) {
                return oldValue;
            }
            Long v = Long.parseLong(oldValue.toString());
            Long pV = Long.parseLong(plusValue.toString());
            return v + pV;
        } catch (Exception e) {
            Double v = Double.parseDouble(oldValue.toString());
            Double pV = Double.parseDouble(plusValue.toString());
            return v + pV;
        }
    }

    private Object subtractValue(Object oldValue, Object plusValue) {
        try {
            if (oldValue == null) {
                return 0;
            }
            Long v = Long.parseLong(oldValue.toString());
            Long pV = Long.parseLong(plusValue.toString());
            if (v - pV >= 0) {
                return v - pV;
            }
            return 0;
        } catch (Exception e) {
            Double v = Double.parseDouble(oldValue.toString());
            Double pV = Double.parseDouble(plusValue.toString());
            if (v - pV >= 0) {
                return v - pV;
            }
            return 0;
        }
    }
}
