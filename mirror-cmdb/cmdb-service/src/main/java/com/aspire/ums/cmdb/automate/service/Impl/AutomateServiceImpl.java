package com.aspire.ums.cmdb.automate.service.Impl;

import com.aspire.ums.cmdb.automate.constants.AutomateConfigConstant;
import com.aspire.ums.cmdb.automate.enums.AutomateInstanceModuleEnum;
import com.aspire.ums.cmdb.automate.mapper.AutomateHostMapper;
import com.aspire.ums.cmdb.automate.payload.*;
import com.aspire.ums.cmdb.automate.payload.easyops.*;
import com.aspire.ums.cmdb.automate.service.AutomateInstanceLogService;
import com.aspire.ums.cmdb.automate.service.AutomateService;
import com.aspire.ums.cmdb.automate.utils.AutomateInstanceQueryHelper;
import com.aspire.ums.cmdb.code.payload.CmdbSimpleCode;
import com.aspire.ums.cmdb.common.Constants;
import com.aspire.ums.cmdb.ipCollect.payload.vmware.*;
import com.aspire.ums.cmdb.module.payload.Module;
import com.aspire.ums.cmdb.util.*;
import com.aspire.ums.cmdb.v2.code.service.ICmdbCodeService;
import com.aspire.ums.cmdb.v2.module.service.ModuleService;
import com.aspire.ums.cmdb.v3.config.payload.CmdbConfig;
import com.aspire.ums.cmdb.v3.config.service.ICmdbConfigService;
import com.aspire.ums.cmdb.v3.redis.service.IRedisService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author fanwenhui
 * @date 2020-08-24 9:38
 * @description 自动化模型业务逻辑具体实现
 */
@Slf4j
@Service
public class AutomateServiceImpl implements AutomateService {

    // 操作人
    private final static String OP_NAME = "自动化配置";
    private static final String DATE_FMT_PARTNAME = "yyyy_MM_dd";
    private static final String TABLE_NAME_AUTOMATE_CONF = "cmdb_instance_automate_conf";
    private final ExecutorService executorService = Executors.newCachedThreadPool();
    @Autowired
    private AutomateInstanceLogService instanceLogService;
    @Resource
    private AutomateHostMapper mapper;
    @Autowired
    private ModuleService moduleService;
    @Autowired
    private IRedisService redisService;
    @Autowired
    private ICmdbCodeService codeService;
    @Autowired
    private ICmdbConfigService cmdbConfigService;

    @Override
    public void instanceCreate(AutomateHostDataDTO request){
        String objectId = request.getObjectId();
        AutomateInstanceModuleEnum moduleEnum = AutomateInstanceModuleEnum.fromKey(objectId);
        if (null == moduleEnum) {
            return;
        }
        try {
            // 主机资源
            if (moduleEnum == AutomateInstanceModuleEnum.HOST) {
                createHostInstance(request);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void instanceCreateByJson(String param, String synLogId) {
        Map<String,Object> synRetMap = buildKafkaRetMap(synLogId);
        boolean synStatus = true;
        try {
            InstanceCreateRequest request = new JsonMapper().readValue(param, InstanceCreateRequest.class);
            saveLog(request,synLogId);

            String instanceId = request.getData().getExtInfo().getInstanceId();
            AutomateHostDataDTO hostDto = getAutomateHostDto(instanceId);
            if (null != hostDto) {
                instanceCreate(hostDto);
            }
        } catch (Exception e) {
            synStatus = false;
            synRetMap.put("synResult",e.toString());
//            log.error("新增失败",e);
        } finally {
            saveAndUpdateKafkaLog(synStatus,synLogId,param,AutomateConfigConstant.AUTOMATE_CREATE,synRetMap);
        }
    }

    @Override
    public void instanceUpdateByJson(String paramJson, String synLogId) {
        InstanceUpdateRequest request = null;
        Map<String,Object> synRetMap = buildKafkaRetMap(synLogId);
        boolean synStatus = true;
        try {
            request = new JsonMapper().readValue(paramJson, InstanceUpdateRequest.class);
            saveLog(request,synLogId);
            String objectId = request.getData().getExtInfo().getObjectId();
            String instanceId = request.getData().getExtInfo().getInstanceId();
            AutomateInstanceModuleEnum moduleEnum = AutomateInstanceModuleEnum.fromKey(objectId);
            if (null == moduleEnum) {
                return;
            }
            Map<String, Object> updateMap = extractUpdateField(request);
            Map<String,String> param = new HashMap<>();
            param.put("instanceId",instanceId);
            Map<String, String> automateHostInfo = mapper.getAutomateHostInfo(param);
            // 如果不存在则进行新增操作
            if (null == automateHostInfo) {
                AutomateHostDataDTO hostDto = getAutomateHostDto(instanceId);
                if (null != hostDto) {
                    instanceCreate(hostDto);
                }
            } else {
                Object cpu = updateMap.get("cpu");
                String uuid = automateHostInfo.get("id");
                if (null != cpu) {
                    List<AutomateHostCpuEntity> cpuList = Lists.newArrayList();
                    List<AutomateHostDataCpuDTO> cpuDataList = new JsonMapper().readValue(cpu.toString(), new TypeReference<List<AutomateHostDataCpuDTO>>() {});
                    if (null != cpuDataList && !cpuDataList.isEmpty()) {
                        for (AutomateHostDataCpuDTO cpuData : cpuDataList) {
                            AutomateHostCpuEntity temp = buildAutoHostCpu(cpuData,uuid);
                            cpuList.add(temp);
                        }
                        if (StringUtils.isNotEmpty(uuid)) {
                            updateMap.put("cpuJson",JsonUtil.toJacksonJson(cpu));
                            mapper.delAutomateCpu(uuid);
                            mapper.batchSaveCpuList(cpuList);
                        }
                    }
                }
                // 针对结构体数组型的做特殊处理，磁盘，网卡，服务信息这3种
                AutomateHostDataDTO hostDto = getAutomateHostDto(instanceId);
                if (null != hostDto) {
                    List<AutomateHostDiskEntity> diskList = Lists.newArrayList();
                    List<AutomateHostDataDiskDTO> diskDataList = hostDto.getDisk();
                    if (null != diskDataList && !diskDataList.isEmpty() ) {
                        for (AutomateHostDataDiskDTO data : diskDataList) {
                            AutomateHostDiskEntity temp = buildAutoHostDisk(data,uuid);
                            diskList.add(temp);
                        }
                        if (StringUtils.isNotEmpty(uuid)) {
                            updateMap.put("diskJson",JsonUtil.toJacksonJson(diskDataList));
                            mapper.delAutomateDisk(uuid);
                            mapper.batchSaveDiskList(diskList);
                        }
                    }

                    List<AutomateHostEthEntity> ethList = Lists.newArrayList();
                    List<AutomateHostDataEthDTO> ethDataList = hostDto.getEth();
                    if (null != ethDataList && !ethDataList.isEmpty()) {
                        for (AutomateHostDataEthDTO data : ethDataList) {
                            AutomateHostEthEntity temp = buildAutoHostEth(data,uuid);
                            ethList.add(temp);
                        }
                        if (StringUtils.isNotEmpty(uuid)) {
                            updateMap.put("ethJson",JsonUtil.toJacksonJson(ethDataList));
                            mapper.delAutomateEth(uuid);
                            mapper.batchSaveEthList(ethList);
                        }
                    }

                    List<AutomateHostServiceEntity> serviceList = Lists.newArrayList();
                    List<AutomateHostDataServiceDTO> serviceDataList = hostDto.getService();
                    if (null != serviceDataList && !serviceDataList.isEmpty()) {
                        for (AutomateHostDataServiceDTO data : serviceDataList) {
                            AutomateHostServiceEntity temp = buildAutoHostService(data,uuid);
                            serviceList.add(temp);
                        }
                        if (StringUtils.isNotEmpty(uuid)) {
                            updateMap.put("serviceJson",JsonUtil.toJacksonJson(serviceDataList));
                            mapper.delAutomateService(uuid);
                            mapper.batchSaveServiceList(serviceList);
                        }
                    }
                }
                // 更新主机配置信息主表字段
                mapper.updateHostByInstanceId(updateMap);
            }
        } catch (Exception e) {
            synStatus = false;
            synRetMap.put("synResult",e.toString());
//            log.error("<<<< 自动化模型[修改-实例]更新失败! <<<<<",e);
        } finally {
            saveAndUpdateKafkaLog(synStatus,synLogId,paramJson,AutomateConfigConstant.AUTOMATE_UPDATE,synRetMap);
        }
    }

    @Override
    public void instanceDeleteByJson(String paramJson, String synLogId) {
        InstanceDeleteRequest request = null;
        Map<String,Object> synRetMap = buildKafkaRetMap(synLogId);
        boolean synStatus = true;
        try {
            request = new JsonMapper().readValue(paramJson, InstanceDeleteRequest.class);
            saveLog(request,synLogId);

            String objectId = request.getData().getExtInfo().getObjectId();
            String instanceId = request.getData().getExtInfo().getInstanceId();
            AutomateInstanceModuleEnum moduleEnum = AutomateInstanceModuleEnum.fromKey(objectId);
            if (null == moduleEnum) {
                return;
            }
            if(StringUtils.isNotEmpty(instanceId)) {
                mapper.updateDelAutomate(instanceId);
            }
        } catch (IOException e) {
            synStatus = false;
            synRetMap.put("synResult",e.toString());
            log.error("主机配置模型【删除】失败",e);
        } finally {
            saveAndUpdateKafkaLog(synStatus,synLogId,paramJson,AutomateConfigConstant.AUTOMATE_DELETE,synRetMap);
        }
    }

    private void createHostInstance(AutomateHostDataDTO data){
        if (null == data) {
            return;
        }
        AutomateHostEntity entity = new AutomateHostEntity();
        BeanUtils.copyProperties(data,entity);
        String ip = data.getIp();
        if (StringUtils.isNotEmpty(ip)) {
            Map<String, Object> detail = mapper.getAutomateHostDetail(data.getIp());
            if (null != detail) {
                log.info("自动化主机配置信息已存在,实例Id:{},主机IP为{}",data.getInstanceId(),data.getIp());
                return;
            }
        }

        // 配置信息主表ID
        String uuid = UUIDUtil.getUUID();
        entity.setId(uuid);
        entity.setIsDelete("0");
        entity.setInsertPerson(OP_NAME);
        entity.setUpdatePerson(OP_NAME);

        AutomateHostDataCpuDTO cpuData = data.getCpu();
        List<AutomateHostDataDiskDTO> diskDataList = data.getDisk();
        List<AutomateHostDataEthDTO> ethDataList = data.getEth();
        List<AutomateHostDataServiceDTO> serviceDataList = data.getService();

        List<AutomateHostCpuEntity> cpuList = Lists.newArrayList();
        List<AutomateHostDiskEntity> diskList = Lists.newArrayList();
        List<AutomateHostEthEntity> ethList = Lists.newArrayList();
        List<AutomateHostServiceEntity> serviceList = Lists.newArrayList();

        if (null != cpuData) {
            AutomateHostCpuEntity temp = buildAutoHostCpu(cpuData,uuid);
            cpuList.add(temp);
            entity.setCpuJson(JsonUtil.toJacksonJson(cpuData));
        }

        if(null != diskDataList && !diskDataList.isEmpty()) {
            for (AutomateHostDataDiskDTO map : diskDataList) {
                AutomateHostDiskEntity temp = buildAutoHostDisk(map,uuid);
                diskList.add(temp);
            }
            entity.setDiskJson(JsonUtil.toJacksonJson(diskDataList));
        }

        if (null != ethDataList && !ethDataList.isEmpty()) {
            for (AutomateHostDataEthDTO map : ethDataList) {
                AutomateHostEthEntity temp = buildAutoHostEth(map,uuid);
                ethList.add(temp);
            }
            entity.setEthJson(JsonUtil.toJacksonJson(ethDataList));
        }

        if (null != serviceDataList && !serviceDataList.isEmpty()) {
            for (AutomateHostDataServiceDTO map : serviceDataList) {
                AutomateHostServiceEntity temp = buildAutoHostService(map,uuid);
                serviceList.add(temp);
            }
            entity.setServiceJson(JsonUtil.toJacksonJson(serviceDataList));
        }
        mapper.saveHost(entity);
        if (!cpuList.isEmpty()) {
            mapper.batchSaveCpuList(cpuList);
        }
        if (!diskList.isEmpty()) {
            mapper.batchSaveDiskList(diskList);
        }
        if (!ethList.isEmpty()) {
            mapper.batchSaveEthList(ethList);
        }
        if (!serviceList.isEmpty()) {
            mapper.batchSaveServiceList(serviceList);
        }
    }

    @Override
    public Map<String,Object> getAutomateHostDetail(String ip) {
        Map<String, Object> detail = mapper.getAutomateHostDetail(ip);
        if (null == detail) {
            return null;
        }
        Object idObj = detail.get("id");
        if (StringUtils.isEmpty(idObj.toString())) {
            return null;
        }
        // 获取主表ID关联查询其他的数组信息
        List<Map<String, String>> cpuList = mapper.getCpuList(idObj.toString());
        List<Map<String, String>> diskList = mapper.getDiskList(idObj.toString());
        List<Map<String, String>> ethList = mapper.getEthList(idObj.toString());
        List<Map<String, String>> serviceList = mapper.getServiceList(idObj.toString());
        detail.put("automate_cpu",cpuList);
        detail.put("automate_disk",diskList);
        detail.put("automate_eth",ethList);
        detail.put("automate_service",serviceList);
        return detail;
    }

    @Override
    public List<Map<String, Object>> getAutomateColumns() {
        List<Map<String, Object>> retList = new LinkedList<>();
        List<String> codes = Lists.newArrayList("automate", "automate_cpu", "automate_disk","automate_eth","automate_service");
        // 默认不需要展示的字段
        String notNeedColumn = "id,module_id,insert_person,insert_time,update_person,update_time,auto2_instanceid,";
        // 配置其他不需要展示的字段
        CmdbConfig cmdbConfig = cmdbConfigService.getConfigByCode("automate_not_need_field");
        String configValue = cmdbConfig.getConfigValue();
        if (StringUtils.isNotEmpty(configValue)) {
            notNeedColumn += configValue;
        }
        List<Map<String, String>> moduleMap = mapper.getAutomateModuleIds(codes);
        for (Map<String, String> map : moduleMap) {
            Map<String, Object> retMap = new LinkedHashMap<>();
            String code = map.get("code");
            retMap.put("code",code);
            retMap.put("name",map.get("name"));
            retMap.put("type","text");
            // 如果包含有下划线则为数组格式
            long start = System.currentTimeMillis();
            Map<String, Map<String, String>> moduleColumns = moduleService.getModuleColumns(map.get("id"));
            long end = System.currentTimeMillis();
            log.info("自动化二期配置字段查询耗时：{} 毫秒",(end-start));
            if (code.contains("automate_")) {
                retMap.put("type","list");
            }
            List<Map<String, String>> columns = new ArrayList<>();
            for (Map<String, String> value : moduleColumns.values()) {
                if (!notNeedColumn.contains(value.get("filed_code"))) {
                    columns.add(value);
                }
            }
            retMap.put("column",columns);
            if("automate".equals(code)) {
                retList.add(0,retMap);
            } else {
                retList.add(retMap);
            }
        }
        return retList;
    }

    @Override
    public boolean syncModule4Redis() {
        try {
            List<String> codes = Lists.newArrayList("automate", "automate_cpu", "automate_disk","automate_eth","automate_service");
            List<Map<String, String>> moduleMap = mapper.getAutomateModuleIds(codes);
            for (Map<String, String> map : moduleMap) {
                redisService.asyncRefresh(Constants.REDIS_TYPE_MODULE,map.get("id"));
            }
        } catch (Exception e) {
            log.error("自动化二期配置模型录入redis缓存失败",e);
            return false;
        }
        return true;
    }

    public Map<String,List<String>> buildExportHeaderList() {
        Map<String,List<String>> retMap = new HashMap<>();
        List<CmdbSimpleCode> headerList = new ArrayList<>();
        List<Map<String, String>> mainModuleList = mapper.getAutomateModuleIds(Lists.newArrayList("automate"));
//        List<String> subCodes = Lists.newArrayList("automate_cpu", "automate_disk","automate_eth","automate_service");
//        List<Map<String, String>> subModuleList = mapper.getAutomateModuleIds(subCodes);
        List<String> header = new LinkedList<>();
        List<String> keys = new LinkedList<>();

        Map<String, String> mainModule = mainModuleList.get(0);
        String mainModuleId = mainModule.get("id");
        if (StringUtils.isNotEmpty(mainModuleId)) {
            Module module = moduleService.getModuleDetail(mainModuleId);
            if (null != module) {
                headerList = codeService.getSimpleCodeListByModuleId(module.getId());
            }
        }
        if (!headerList.isEmpty()) {
            // 不需要导出的字段
            List<String> filterCodeList = Arrays.asList("id", "module_id", "insert_person", "insert_time", "update_person", "update_time");
            headerList.forEach(code -> {
                if (!filterCodeList.contains(code.getFiledCode())) {
                    header.add(code.getFiledName());
                    keys.add(code.getFiledCode());
                }
            });
//            subModuleList.forEach(map -> {
//                header.add(map.get("name"));
//                keys.add(map.get("code"));
//            });
            retMap.put("header",header);
            retMap.put("keys",keys);
        }
        return retMap;
    }

    @Override
    public Map<String, Object> buildSynParam(String param, String type) {
        Map<String,Object> synParam = new HashMap<>();
        String synLogId = UUIDUtil.getUUID();
        synParam.put("synLogId",synLogId);
        synParam.put("requestContent",param);
        synParam.put("type",type);
        synParam.put("synType", AutomateConfigConstant.AUTOMATE_SUCCESS);
        synParam.put("synResult", AutomateConfigConstant.AUTOMATE_SUCCESS_DESC);
        return synParam;
    }

    @Override
    public void synAutomateConfFile() {
        // 读取主机配置文件路径下今天日期的管理IP文件有哪些
        Map<String,String> partMap = new HashMap<>();

        try {
            Date currentDate = new Date();
            Date nextDate = DateUtils.addDate(currentDate, 1);
            String next_time_par = DateUtils.datetimeToString(DATE_FMT_PARTNAME, nextDate);
            String next_time = DateUtils.datetimeToString(DateUtils.DEFAULT_DATE_FMT, nextDate);
            partMap.put("next_time_par",next_time_par);
            partMap.put("next_time",next_time);
            String partName = mapper.getAutomateHostPartName(TABLE_NAME_AUTOMATE_CONF, next_time_par);
            if (StringUtils.isEmpty(partName)) {
                mapper.alterAutomateHostPart(partMap);
            } else {
                log.info("主机配置文件数据已录入");
                return;
            }

            String nowDate = DateUtils.getCurrentDate("yyyyMMdd"); // 获取当天的日期
            // 构建今天要读取的文件目录
            String nowDatePath = AutomateConfigConstant.AUTOMATE_CONF_ROOT + File.separator + nowDate;
            // 构建全量读取路径
            CmdbConfig cmdbConfig = cmdbConfigService.getConfigByCode("automate_conf_all");
            if (null != cmdbConfig) {
                nowDatePath = AutomateConfigConstant.AUTOMATE_CONF_ROOT;
            }
            // 构建需要读取的所有目录
            File dir = new File(nowDatePath);

            List<Map<String,Object>> list = new ArrayList<>();
            long start = System.currentTimeMillis();
            getBackJson(dir,list);
            long end = System.currentTimeMillis();
            log.info("主机配置文件解析json耗时{}",end - start);
            if (!list.isEmpty()) {
                // 分批次插入数据库
                long start1 = System.currentTimeMillis();
                List<List<Map<String, Object>>> partitionList = Lists.partition(list, 1000);
                for (List<Map<String, Object>> partition : partitionList) {
                    mapper.batchSaveHostConfList(partition);
                }
                long end1 = System.currentTimeMillis();
                log.info("主机配置文件入库耗时{}",end1 - start1);
            }
        } catch (IOException e) {
            log.error("主机配置解析失败",e);
        }
    }

    @Override
    public List<Map<String, String>> findAutomateConfList(Map<String, Object> param) {
        return mapper.findAutomateConfList(param);
    }

    private Map<String, Object> extractUpdateField(InstanceUpdateRequest request) {
        InstanceRequestData<InstanceUpdateRequestExtInfo> data = request.getData();
        InstanceUpdateRequestExtInfo extInfo = data.getExtInfo();
        String instanceId = extInfo.getInstanceId();
        Map<String, Object> params = Maps.newHashMap();
        params.put("operator", data.getOperator());
        params.put("instanceId", instanceId);
        params.put("version", extInfo.getVersion());
        params.put("preTimestamp", extInfo.getPreTimestamp());
        params.put("timestamp", extInfo.getTimestamp());
        LinkedHashMap<String, Object> fieldValueHashMap = extInfo.getChangeData();
        for (Map.Entry<String, Object> entry : fieldValueHashMap.entrySet()) {
            Object value = entry.getValue();
            String version = entry.getKey();
            if (!Arrays.asList("eth", "cpu", "disk","service").contains(version)) {
                List<String> updateValueList = AutomateInstanceQueryHelper.array2List(value, String.class);
                String joinStr = "";
                if (org.apache.commons.collections4.CollectionUtils.isNotEmpty(updateValueList)) {
                    joinStr = Joiner.on(",").join(updateValueList);
                }
                params.put(version, joinStr);
            } else {
                List<String> updateValueList = AutomateInstanceQueryHelper.array2List(value, String.class);
                params.put(version, updateValueList);
            }
        }
        return params;
    }

    private AutomateHostCpuEntity buildAutoHostCpu(AutomateHostDataCpuDTO cpuData,String uuid) {
        AutomateHostCpuEntity temp = new AutomateHostCpuEntity();
        BeanUtils.copyProperties(cpuData,temp);
        temp.setId(UUIDUtil.getUUID());
        temp.setAssetInstanceId(uuid);
        temp.setIsDelete("0");
        temp.setInsertPerson(OP_NAME);
        temp.setUpdatePerson(OP_NAME);
        return temp;
    }

    private AutomateHostDiskEntity buildAutoHostDisk(AutomateHostDataDiskDTO data,String uuid) {
        AutomateHostDiskEntity temp = new AutomateHostDiskEntity();
        BeanUtils.copyProperties(data,temp);
        temp.setId(UUIDUtil.getUUID());
        temp.setAssetInstanceId(uuid);
        temp.setIsDelete("0");
        temp.setInsertPerson(OP_NAME);
        temp.setUpdatePerson(OP_NAME);
        return temp;
    }

    private AutomateHostEthEntity buildAutoHostEth(AutomateHostDataEthDTO data,String uuid) {
        AutomateHostEthEntity temp = new AutomateHostEthEntity();
        BeanUtils.copyProperties(data,temp);
        temp.setId(UUIDUtil.getUUID());
        temp.setAssetInstanceId(uuid);
        temp.setIsDelete("0");
        temp.setInsertPerson(OP_NAME);
        temp.setUpdatePerson(OP_NAME);
        return temp;
    }

    private AutomateHostServiceEntity buildAutoHostService(AutomateHostDataServiceDTO data,String uuid) {
        AutomateHostServiceEntity temp = new AutomateHostServiceEntity();
        BeanUtils.copyProperties(data,temp);
        temp.setId(UUIDUtil.getUUID());
        temp.setAssetInstanceId(uuid);
        temp.setIsDelete("0");
        temp.setInsertPerson(OP_NAME);
        temp.setUpdatePerson(OP_NAME);
        return temp;
    }

    /**
     * 根据自动化的实例ID获取主机配置详情
     * @param instanceId 自动化实例ID
     */
    private AutomateHostDataDTO getAutomateHostDto(String instanceId) throws Exception {
        String host = AutomateInstanceQueryHelper.queryInstanceByType(Collections.singletonList(instanceId), "HOST");
        if (StringUtils.isNotEmpty(host)) {
            AutomateHostDTO automateHostDTO = new JsonMapper().readValue(host, AutomateHostDTO.class);
            List<AutomateHostDataDTO> list = automateHostDTO.getData().getList();
            if (!list.isEmpty()) {
                return list.get(0);
            }
        }
        return null;
    }

    /**
     * 构建kafka同步结果
     * @param synLogId kafka日志ID
     */
    private Map<String,Object> buildKafkaRetMap(String synLogId) {
        Map<String,Object> synRetMap = new HashMap<>();
        synRetMap.put("synLogId",synLogId);
        synRetMap.put("synStatus",AutomateConfigConstant.AUTOMATE_SUCCESS);
        synRetMap.put("synResult",AutomateConfigConstant.AUTOMATE_SUCCESS_DESC);
        return synRetMap;
    }

    /**
     * 保存和更新kafka同步日志
     * @param synStatus 同步状态
     * @param synLogId kafka日志ID
     * @param param 同步入参
     * @param type 同步类型
     * @param synRetMap 同步结果描述
     */
    private void saveAndUpdateKafkaLog(boolean synStatus,String synLogId,String param,String type,Map<String,Object> synRetMap) {
        if (!synStatus) {
            instanceLogService.updateHostLogStatus(synLogId);
            String kafkaHostLog = instanceLogService.getKafkaHostLog(synLogId);
            if (StringUtils.isEmpty(kafkaHostLog)) {
                Map<String, Object> synParam = buildSynParam(param, type);
                synParam.put("synType", AutomateConfigConstant.AUTOMATE_FAIL);
                synParam.put("synResult", synRetMap.get("synResult"));
                instanceLogService.saveKafkaHostLog(synParam);
            } else {
                synRetMap.put("synType", AutomateConfigConstant.AUTOMATE_FAIL);
                instanceLogService.updateKafkaHostLogStatus(synRetMap);
            }
        }
    }

    /**
     * 保存请求日志
     * @param request 自动化模型请求
     */
    private void saveLog(BaseInstanceRequest request,String synLogId) {
        executorService.execute(() -> instanceLogService.saveInstanceLog(request,synLogId));
    }

    private void getBackJson(File dir,List<Map<String,Object>> list) throws IOException {
        if (!dir.exists()) {
            log.error("目录：{},不存在",dir);
            return;
        }
        if(!dir.isDirectory()){
            log.error("{},不是目录",dir);
            return;
        }
        File[] files = dir.listFiles();
        if (null != files && files.length > 0) {
            for (File file : files) {
                if (file.isDirectory()) {
                    getBackJson(file,list);
                } else {
                    String name = file.getName();
                    if (AutomateConfigConstant.AUTOMATE_CONF_JSON.equals(name)) {
                        // 解析json文件
                        // 获取IP
                        File parentFile = file.getParentFile();
                        String parentFileName = parentFile.getName();
                        // 获取日期
                        File parentFile1 = parentFile.getParentFile();
                        String name1 = parentFile1.getName();

                        String jsonStr = parseBackJson(file);
                        Map<String,Object> map = new HashMap<>();
                        map.put("hostIp",parentFileName);
                        map.put("remark","配置文件解析成功");
                        if (StringUtils.isEmpty(jsonStr)) {
                            map.put("remark","json文件解析失败");
                            log.info("【{}】json文件解析失败",parentFileName);
                        } else {
                            // 将json字符串转换为Map对象
                            Map<String, Object> JsonMap = new JsonMapper().readValue(jsonStr, Map.class);
                            if (null != JsonMap) {
                                Object time = JsonMap.get("time");
                                for (Map.Entry<String, Object> entry : JsonMap.entrySet()) {
                                    String key = entry.getKey();
                                    if (!AutomateConfigConstant.AUTOMATE_CONF_JSON_TIME_KEY.equals(key)) {
                                        Map<String,Object> confMap = new HashMap<>(map);
                                        confMap.put("id",UUIDUtil.getUUID());
                                        confMap.put("time",time);
                                        confMap.put("fileName",key);
                                        confMap.put("fileType","hostConf");
                                        confMap.put("fileStatus",entry.getValue());
                                        confMap.put("filePath", name1);
                                        list.add(confMap);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } else {
            // 记录日志，IP文件夹下没有其他文件夹
        }
    }

    /**
     * 解析json文件
     * @param jsonFile 被解析的文件
     */
    private String parseBackJson(File jsonFile){
        try (Reader reader = new InputStreamReader(new FileInputStream(jsonFile), StandardCharsets.UTF_8)) {
            int ch;
            StringBuilder sb = new StringBuilder();
            while ((ch = reader.read()) != -1) {
                sb.append((char) ch);
            }
            return sb.toString();
        } catch (Exception e) {
            log.error("json文件解析失败", e);
        }
        return null;
    }
}
