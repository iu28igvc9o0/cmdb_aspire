package com.aspire.cmdb.agent.sync.service.impl;

import com.aspire.cmdb.agent.collect.CollectConst;
import com.aspire.cmdb.agent.sync.service.ICloudSyncService;
import com.aspire.cmdb.agent.util.UUIDUtil;
import com.aspire.ums.cmdb.client.ICmdbESClient;
import com.aspire.ums.cmdb.code.payload.CmdbSimpleCode;
import com.aspire.ums.cmdb.collectApproval.payload.CmdbCollectApproval;
import com.aspire.ums.cmdb.collectApproval.payload.CmdbCollectResource;
import com.aspire.ums.cmdb.collectUnknown.payload.CmdbCollectUnknown;
import com.aspire.ums.cmdb.common.Result;
import com.aspire.ums.cmdb.util.DateUtils;
import com.aspire.ums.cmdb.v2.code.service.ICmdbCodeService;
import com.aspire.ums.cmdb.v2.collect.service.CmdbCollectApprovalService;
import com.aspire.ums.cmdb.v2.collect.service.CmdbCollectResourceService;
import com.aspire.ums.cmdb.v2.collectUnknown.service.CmdbCollectUnknownService;
import com.aspire.ums.cmdb.v2.instance.service.ICmdbInstanceService;
import com.aspire.ums.cmdb.v2.module.service.ModuleService;
import com.aspire.ums.cmdb.v3.config.payload.CmdbConfig;
import com.aspire.ums.cmdb.v3.config.service.ICmdbConfigService;
import com.aspire.ums.cmdb.v3.dictMapper.payload.CmdbDictMapperEntity;
import com.aspire.ums.cmdb.v3.dictMapper.service.ICmdbDictMapperService;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: CloudSyncServiceImpl
 * Author:   zhu.juwang
 * Date:     2019/11/28 19:43
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Service
@Slf4j
@Primary
public class CloudSyncServiceImpl implements ICloudSyncService {
    @Autowired
    private CmdbCollectResourceService resourceService;
    @Autowired
    private CmdbCollectApprovalService approvalService;
    @Autowired
    private ICmdbInstanceService instanceService;
    @Autowired
    private ICmdbCodeService codeService;
    @Autowired
    private ModuleService moduleService;
    @Autowired
    private ICmdbDictMapperService dictMapperService;
    @Autowired
    private CmdbCollectUnknownService unknownService;
    @Autowired
    private ICmdbConfigService configService;
    //码表信息
    private Map<String, CmdbSimpleCode> CODE_MAP = new HashMap<>();
    //映射字段信息
    private List<CmdbDictMapperEntity> FILED_MAPPER_LIST = new ArrayList<>();
    //POD池映射信息
    private List<CmdbDictMapperEntity> POD_MAPPER_LIST = new ArrayList<>();
    //运行状态映射信息
    private List<CmdbDictMapperEntity> DEVICE_STATUS_MAPPER_LIST = new ArrayList<>();
    //制造厂家映射信息
    private List<CmdbDictMapperEntity> DEVICE_MFRS_MAPPER_LIST = new ArrayList<>();
    //资源池映射信息
    private List<CmdbDictMapperEntity> DEVICE_IDC_TYPE_MAPPER_LIST = new ArrayList<>();
    //业务系统映射信息
    private List<CmdbDictMapperEntity> DEVICE_BIZ_SYSTEM_MAPPER_LIST = new ArrayList<>();
    // 不介入资源池
    private List<String> EXCLUDE_IDC_LIST = new ArrayList<>();
    @Value("${cmdb.module.id.physicalMachine:353f0967f68d40d18ecaecafae6b3f15}")
    private String pyModuleId;
    @Value("${cmdb.module.id.vmwareMachine:ce5362623c5a47869173a43ff6e982f8}")
    private String vmModuleId;
    @Autowired
    private ICmdbESClient esClient;

    /**
     * 初始化映射信息
     */
    private void initMapper() {
        List<CmdbSimpleCode> codes = codeService.simpleCodeListByEntity(null);
        codes.forEach(code -> this.CODE_MAP.put(code.getFiledCode(), code));
        this.FILED_MAPPER_LIST = dictMapperService.listByEntity(new CmdbDictMapperEntity("filed_mapper", "苏研"));
        this.POD_MAPPER_LIST = dictMapperService.listByEntity(new CmdbDictMapperEntity("POD", "苏研"));
        this.DEVICE_STATUS_MAPPER_LIST = dictMapperService.listByEntity(new CmdbDictMapperEntity("device_status", "苏研"));
        this.DEVICE_MFRS_MAPPER_LIST = dictMapperService.listByEntity(new CmdbDictMapperEntity("device_mfrs", "苏研"));
        this.DEVICE_IDC_TYPE_MAPPER_LIST = dictMapperService.listByEntity(new CmdbDictMapperEntity("idc_type", "苏研"));
        this.DEVICE_BIZ_SYSTEM_MAPPER_LIST = dictMapperService.listByEntity(new CmdbDictMapperEntity("bizSystem", "苏研"));
        CmdbConfig config = configService.getConfigByCode("sync_suyan_exclude_idc_ids");
        if (config != null && StringUtils.isNotEmpty(config.getConfigValue())) {
            EXCLUDE_IDC_LIST = Arrays.asList(config.getConfigValue().split(","));
        }
    }

    /*
     *  将数据库数据 和 得到的数据 进行对比，如果不同，记录存储（物理机）
     * */
    @Override
    public void syncData(Object data) {
        CollectConst.threadPool.execute(new Runnable() {
            @Override
            public void run() {
                int currentPage = 0, totalPage = 0;
                String deviceType = "";
                List<Map<String, Object>> dataList = new ArrayList<>();
                try {
                    initMapper();
                    JSONObject dataJson = JSONObject.fromObject(data.toString());
                    deviceType = dataJson.getString("device_type");
                    JSONArray array = dataJson.getJSONArray("data");
                    currentPage = dataJson.getInt("currentpage");
                    totalPage = dataJson.getInt("page");
                    log.info("正在处理类型 -> {} 第{}页数据, 共{}页.", deviceType, currentPage, totalPage);
                    if (!array.isEmpty()) {
                        List<CmdbDictMapperEntity> mapperList = new ArrayList<>();
                        if (deviceType.equals("X86服务器")) {
                            FILED_MAPPER_LIST.forEach((entity) -> {
                                if (entity.getMapperDeviceType().equalsIgnoreCase("X86服务器")) {
                                    mapperList.add(entity);
                                }
                            });
                        }
                        if (deviceType.equals("云主机")) {
                            FILED_MAPPER_LIST.forEach((entity) -> {
                                if (entity.getMapperDeviceType().equalsIgnoreCase("云主机")) {
                                    mapperList.add(entity);
                                }
                            });
                        }
                        for (int i = 0; i < array.size(); i++) {
                            Map<String, Object> data = new LinkedHashMap<>();
                            data.put("collect_time", DateUtils.format(new Date(), "yyyy-MM-dd hh:mm:ss"));
                            data.put("ip", "");
                            data.put("idcType", "");
                            data.put("suyan_uuid", "");
                            data.put("data", array.getJSONObject(i));
                            String ip = "", mapperIdcType = "", suyanUuid = "";
                            try {
                                if (deviceType.equals("X86服务器")) {
                                    ip = array.getJSONObject(i).getString("manageIp");
                                }
                                if (deviceType.equals("云主机")) {
                                    ip = array.getJSONObject(i).getString("privateIp");
                                }
                                suyanUuid = array.getJSONObject(i).getString("resourceId");
                                mapperIdcType = array.getJSONObject(i).getString("rpName");
                                String deviceName = array.getJSONObject(i).getString("name");
                                //获取UMS的资源池名称
                                String umsIdcName = mapperIdcType;
                                final String mapperIdcName = mapperIdcType;
                                CmdbDictMapperEntity mapperEntity = DEVICE_IDC_TYPE_MAPPER_LIST.stream().
                                        filter((mapper) -> mapper.getMapperDictCode().equals(mapperIdcName)).findFirst().orElse(null);
                                if (mapperEntity != null) {
                                    umsIdcName = mapperEntity.getUmsDictCode();
                                }
                                // 判断是否属于不介入资源池
                                if (EXCLUDE_IDC_LIST.contains(umsIdcName)) {
                                    throw new RuntimeException("Don't sync idcType -> " + umsIdcName);
                                }
                                String moduleId = "";
                                if (deviceType.equals("X86服务器")) {
                                    moduleId = pyModuleId;
                                } else if (deviceType.equals("云主机")) {
                                    moduleId = vmModuleId;
                                } else {
                                    throw new RuntimeException("Don't support device_type -> " + deviceType);
                                }
                                // 先使用suyan_uuid查询, 如果匹配不上 再使用ip+资源池查询
                                Map<String, Object> queryParams = new HashMap<>();
                                queryParams.put("suyan_uuid", suyanUuid);
                                queryParams.put("module_id", moduleId);
                                Map<String, Object> instanceData = null;

                                log.info("Query instance params -> {}", queryParams);
                                Result<Map<String, Object>> dataResult = instanceService.getInstanceList(queryParams, null);
                                // 再使用ip+资源池查询一次
                                if (dataResult == null || dataResult.getTotalSize() <= 0) {
                                    if (StringUtils.isEmpty(ip) || StringUtils.isEmpty(umsIdcName)) {
                                        throw new RuntimeException("Ip or idcType is empty. Skip it.Ip -> " + ip + " idcType -> " + umsIdcName);
                                    }
                                    queryParams.clear();
                                    queryParams.put("ip", ip);
                                    queryParams.put("idcType", umsIdcName);
                                    queryParams.put("module_id", moduleId);
                                    dataResult = instanceService.getInstanceList(queryParams, null);
                                }
                                if (dataResult != null) {
                                    if (dataResult.getTotalSize() == 1) {
                                        Map<String, Object> baseInstance = dataResult.getData().get(0);
                                        if (!MapUtils.isEmpty(baseInstance)) {
                                            instanceData = instanceService.getInstanceDetail(baseInstance.get("module_id").toString(), baseInstance.get("id").toString());
                                        }
                                    }
                                    if (dataResult.getTotalSize() > 1) {
                                        throw new RuntimeException("Too many result find. Except One, But " + dataResult.getTotalSize());
                                    }
                                }
                                if (MapUtils.isEmpty(instanceData) && deviceType.equals("云主机")) {
                                    // 判断如果是云主机, 则自动入库
                                    instanceData = new HashMap<>();
                                    instanceData.put("module_id", vmModuleId);
                                    instanceData.put("ip", ip);
                                    instanceData.put("idcType", umsIdcName);
                                    instanceData.put("suyan_uuid", suyanUuid);
                                    instanceData.put("device_class", "服务器");
                                    instanceData.put("device_type", "云主机");
                                    instanceData.put("insert_time", DateFormatUtils.format(new Date(), "yyyy-MM-dd hh:mm:ss"));
                                    instanceData.put("insert_person", "系统管理员");
                                    instanceData.put("update_time", DateFormatUtils.format(new Date(), "yyyy-MM-dd hh:mm:ss"));
                                    instanceData.put("update_person", "系统管理员");
                                    log.info("{} ->  自动入库. 入库参数 -> {}", deviceType, instanceData);
                                    instanceService.addInstanceNoApprove("系统管理员", instanceData, "苏研数据");
                                    log.info("{} -> 自动入库成功. 入库参数 -> {}", deviceType, instanceData);
                                } else if (MapUtils.isEmpty(instanceData)) {
                                    // 未知设备的处理
                                    log.error("资源池 -> {} IP -> {}设备未找到.", umsIdcName, ip);
                                    CmdbCollectUnknown collectUnknown = new CmdbCollectUnknown();
                                    collectUnknown.setIp(ip);
                                    collectUnknown.setIdcType(mapperEntity.getUmsDictName());
                                    collectUnknown.setDeviceType(deviceType);
                                    collectUnknown.setDeviceName(deviceName);
                                    collectUnknown.setCommitUser("系统管理员");
                                    collectUnknown.setDataFrom("苏研数据");
                                    collectUnknown.setRemark("来源于苏研数据对接");
                                    unknownService.insert(collectUnknown);
                                    continue;
                                }
                                for (CmdbDictMapperEntity entity : mapperList) {
                                    contrastAndSave(array.getJSONObject(i), entity.getMapperDictCode(), instanceData, entity.getUmsDictCode(), deviceType);
                                }
                            } catch (Exception e) {
                                data.put("result", e.getMessage());
                                log.error("处理类型 -> {} IP -> {} 报错. 异常:{}", ip, mapperIdcType, e.getMessage(), e);
                            } finally {
                                data.put("ip", ip);
                                data.put("idcType", mapperIdcType);
                                data.put("suyan_uuid", suyanUuid);
                                dataList.add(data);
                            }
                        }
                    }
                } catch (Exception e) {
                    log.error("处理苏研数据报错:", e);
                } finally {
                    log.info("发送到ES数据库 -> {}", dataList);
                    esClient.insert(dataList, "cmdb_agent_third_collect_log", "suyan");
                }
                log.info("类型 -> {} 第{}页数据, 共{}页 处理结束.", deviceType, currentPage, totalPage);
            }
        });
    }

    /*
     *   对比数据，并且存储(来到的数据 和 本地数据)
     *   @param jsonObject 新来的数据
     *   @param key  新来数据中的字段key值
     *   @param cmdbInstance 旧数据
     *   @param collectMapperKey cmdb_code表中的字段
     *   @param deviceType 设备类型
     * */
    public void contrastAndSave(JSONObject jsonObject, String key, Map<String, Object> cmdbInstance, String collectMapperKey, String deviceType) throws Exception {
        if(jsonObject.containsKey(key)) {
            try {
                // 筛选字符串为空的
                if (jsonObject.get(key) == null || StringUtils.isEmpty(jsonObject.getString(key).trim())) {
                    return;
                }
                // 新传入的值
                String updateValue = jsonObject.getString(key).trim();
                // 如果值为空, 则不录入审核
                if (StringUtils.isEmpty(updateValue)) {
                    return;
                }
                final String mapperValue = jsonObject.getString(key).trim();
                // 获取codeId
                if(CODE_MAP.containsKey(collectMapperKey)) {
                    CmdbSimpleCode updateCode = CODE_MAP.get(collectMapperKey);
                    if("podName".equals(key)) {
                        CmdbDictMapperEntity podEntity = this.POD_MAPPER_LIST.stream()
                                .filter((entity -> entity.getMapperDictCode().equals(mapperValue)))
                                .findFirst()
                                .orElse(null);
                        if (podEntity != null) {
                            updateValue = podEntity.getUmsDictCode();
                        }
                    }
                    if("status".equals(key)) {
                        CmdbDictMapperEntity statusEntity = this.DEVICE_STATUS_MAPPER_LIST.stream()
                                .filter((entity -> entity.getMapperDeviceType().equals(deviceType) && entity.getMapperDictCode().equals(mapperValue)))
                                .findFirst()
                                .orElse(null);
                        if (statusEntity != null) {
                            updateValue = statusEntity.getUmsDictCode();
                        }
                    }
                    if("cpuType".equals(key)) {
                        updateValue = updateValue + jsonObject.getString("cpuFrequency").trim();
                    }
                    if ("manufacturer".equals(key)) {
                        CmdbDictMapperEntity mfrsEntity = this.DEVICE_MFRS_MAPPER_LIST.stream()
                                .filter((entity -> entity.getMapperDictCode().equals(mapperValue)))
                                .findFirst()
                                .orElse(null);
                        if (mfrsEntity != null) {
                            updateValue = mfrsEntity.getUmsDictCode();
                        }
                    }
                    if (("businessName").equals(key)) {
                        CmdbDictMapperEntity bizSystem = this.DEVICE_BIZ_SYSTEM_MAPPER_LIST.stream()
                                .filter((entity -> entity.getMapperDictCode().equals(mapperValue)))
                                .findFirst()
                                .orElse(null);
                        if (bizSystem != null) {
                            updateValue = bizSystem.getUmsDictCode();
                        }
                    }
                    // 保存源数据
                    saveCollectResource(cmdbInstance,updateCode,mapperValue);
                    if (StringUtils.isNotEmpty(updateValue) && !updateValue.equals(cmdbInstance.get(updateCode.getFiledCode()))) {
                        Map<String,Object> mp = new HashMap<>();
                        mp.put(updateCode.getFiledCode(),updateValue);
                        mp.put("module_id",cmdbInstance.get("module_id").toString());
                        instanceService.updateInstance(cmdbInstance.get("id").toString(),"苏研数据同步",mp, "苏研数据");
                    }
                } else {
                    log.error("类型 -> {} 码表不包含字段 -> {}", deviceType, collectMapperKey);
                }
            } catch (Exception e) {
                throw new Exception(e);
            }
        } else {
            log.error("对接数据不包含字段 -> {}", key);
        }
    }

    /*
     *   记录 源数据
     * */
    private void saveCollectResource(Map<String, Object> cmdbInstance, CmdbSimpleCode updateCode,String updateValue) {
        CmdbCollectResource resource = new CmdbCollectResource();
        resource.setId(UUIDUtil.getUUID());
        resource.setInstanceId(cmdbInstance.get("id").toString());
        resource.setCreateTime(new Date());
        resource.setCodeId(updateCode.getCodeId());
        resource.setValue(updateValue);
        resourceService.insert(resource);
    }

    /*
     *  记录 变更审批
     * */
    private void saveCollectApproval(Map<String, Object> cmdbInstance,CmdbSimpleCode updateCode,String updateValue) {
        List<CmdbCollectApproval> list = new ArrayList<>();
        CmdbCollectApproval approval = new CmdbCollectApproval();
        approval.setId(UUIDUtil.getUUID());
        approval.setModuleId(cmdbInstance.get("module_id") == null ? null : cmdbInstance.get("module_id").toString());
        approval.setInstanceId(cmdbInstance.get("id").toString());
        approval.setCodeId(updateCode.getCodeId());
        approval.setCurrValue(updateValue);
        approval.setOldValue(cmdbInstance.get(updateCode.getFiledCode()) == null || "".equals(cmdbInstance.get(updateCode.getFiledCode())) ? "" : cmdbInstance.get(updateCode.getFiledCode()).toString());
        approval.setOperator("系统");
        approval.setOperaterType("苏研数据");
        approval.setOperatorTime(new Date());
        list.add(approval);
        approvalService.insertByBatch(list);
    }
}
