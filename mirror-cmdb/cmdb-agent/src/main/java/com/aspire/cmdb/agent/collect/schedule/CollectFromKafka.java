package com.aspire.cmdb.agent.collect.schedule;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aspire.cmdb.agent.collect.CollectConst;
import com.aspire.cmdb.agent.util.UUIDUtil;
import com.aspire.ums.cmdb.code.payload.CmdbCode;
import com.aspire.ums.cmdb.collectApproval.payload.CmdbCollectResource;
import com.aspire.ums.cmdb.collectUnknown.payload.CmdbCollectUnknown;
import com.aspire.ums.cmdb.dict.mapper.ConfigDictMapper;
import com.aspire.ums.cmdb.dict.payload.ConfigDict;
import com.aspire.ums.cmdb.instance.payload.CmdbInstance;
import com.aspire.ums.cmdb.module.payload.Module;
import com.aspire.ums.cmdb.v2.code.mapper.CmdbCodeMapper;
import com.aspire.ums.cmdb.v2.collect.service.CmdbCollectApprovalService;
import com.aspire.ums.cmdb.v2.collect.service.CmdbCollectResourceService;
import com.aspire.ums.cmdb.v2.collectUnknown.service.CmdbCollectUnknownService;
import com.aspire.ums.cmdb.v2.instance.service.ICmdbInstanceService;
import com.aspire.ums.cmdb.v2.module.mapper.ModuleMapper;
import com.aspire.ums.cmdb.v2.module.mapper.ModuleTableMapper;
import com.aspire.ums.cmdb.v3.code.payload.CmdbV3CodeCollect;
import com.aspire.ums.cmdb.v3.module.service.ICmdbV3ModuleCatalogService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.regex.Pattern;

/**
 * 指标自动采集任务类
 */
@Data
@Component
@Slf4j
public class CollectFromKafka {

    //默认未发现设备处理状态
    private static final Integer DEFAULT_HANDLE = 0;

    private Module module;

    private List<String> deviceTypeList;

    //1000条 提交一次数据库
    private static final Integer LIMIT_SUBMIT_DATA_LENGTH = 1000;

    private static Map<String, Map<String, CmdbCode>> moduleCodes = new HashMap<>();

    //CMDB数据库的实例名称
    @Value("${cmdb.schema.name}")
    private String schemaName;

    @Autowired
    private CmdbCollectUnknownService unknownService;

    @Autowired
    private CmdbCodeMapper codeMapper;

    @Autowired
    private ICmdbInstanceService instanceService;

    @Autowired
    private ModuleMapper moduleMapper;
    @Autowired
    private ICmdbV3ModuleCatalogService catalogService;

    @Autowired
    private CmdbCollectResourceService resourceService;

    @Autowired
    private CmdbCollectApprovalService approvalService;

    @Autowired
    private ModuleTableMapper moduleTableMapper;

    @Autowired
    private ConfigDictMapper dictMapper;

    @KafkaListener(id = "collect-zbx-0", topicPartitions = {@TopicPartition(topic = "${kafka.topic.cmdb_instance_topic_zbx:Ci_Zbx}", partitions = {"0"})})
    public void listenZbxPartition0(ConsumerRecord<?, String> cr) {
        log.info("collect-zbx-0 Listener, Thread ID:{}, record: {}", Thread.currentThread().getId(), cr);
        resovleData(cr.value());
    }
    @KafkaListener(id = "collect-zbx-1", topicPartitions = {@TopicPartition(topic = "${kafka.topic.cmdb_instance_topic_zbx:Ci_Zbx}", partitions = {"1"})})
    public void listenZbxPartition1(ConsumerRecord<?, String> cr) {
        log.info("collect-zbx-1 Listener, Thread ID:{}, record: {}", Thread.currentThread().getId(), cr);
        resovleData(cr.value());
    }
    @KafkaListener(id = "collect-zbx-2", topicPartitions = {@TopicPartition(topic = "${kafka.topic.cmdb_instance_topic_zbx:Ci_Zbx}", partitions = {"2"})})
    public void listenZbxPartition2(ConsumerRecord<?, String> cr) {
        log.info("collect-zbx-2 Listener, Thread ID:{}, record: {}", Thread.currentThread().getId(), cr);
        resovleData(cr.value());
    }
    @KafkaListener(id = "collect-zbx-3", topicPartitions = {@TopicPartition(topic = "${kafka.topic.cmdb_instance_topic_zbx:Ci_Zbx}", partitions = {"3"})})
    public void listenZbxPartition3(ConsumerRecord<?, String> cr) {
        log.info("collect-zbx-3 Listener, Thread ID:{}, record: {}", Thread.currentThread().getId(), cr);
        resovleData(cr.value());
    }
    @KafkaListener(id = "collect-zbx-4", topicPartitions = {@TopicPartition(topic = "${kafka.topic.cmdb_instance_topic_zbx:Ci_Zbx}", partitions = {"4"})})
    public void listenZbxPartition4(ConsumerRecord<?, String> cr) {
        log.info("collect-zbx-4 Listener, Thread ID:{}, record: {}", Thread.currentThread().getId(), cr);
        resovleData(cr.value());
    }
    @KafkaListener(id = "collect-zbx-5", topicPartitions = {@TopicPartition(topic = "${kafka.topic.cmdb_instance_topic_zbx:Ci_Zbx}", partitions = {"5"})})
    public void listenZbxPartition5(ConsumerRecord<?, String> cr) {
        log.info("collect-zbx-5 Listener, Thread ID:{}, record: {}", Thread.currentThread().getId(), cr);
        resovleData(cr.value());
    }
    @KafkaListener(id = "collect-zbx-6", topicPartitions = {@TopicPartition(topic = "${kafka.topic.cmdb_instance_topic_pro:Ci_Zbx}", partitions = {"6"})})
    public void listenZbxPartition6(ConsumerRecord<?, String> cr) {
        log.info("collect-zbx-6 Listener, Thread ID:{}, record: {}", Thread.currentThread().getId(), cr);
        resovleData(cr.value());
    }
    @KafkaListener(id = "collect-zbx-7", topicPartitions = {@TopicPartition(topic = "${kafka.topic.cmdb_instance_topic_zbx:Ci_Zbx}", partitions = {"7"})})
    public void listenZbxPartition7(ConsumerRecord<?, String> cr) {
        log.info("collect-zbx-7 Listener, Thread ID:{}, record: {}", Thread.currentThread().getId(), cr);
        resovleData(cr.value());
    }
    @KafkaListener(id = "collect-zbx-8", topicPartitions = {@TopicPartition(topic = "${kafka.topic.cmdb_instance_topic_zbx:Ci_Zbx}", partitions = {"8"})})
    public void listenZbxPartition8(ConsumerRecord<?, String> cr) {
        log.info("collect-zbx-8 Listener, Thread ID:{}, record: {}", Thread.currentThread().getId(), cr);
        resovleData(cr.value());
    }
    @KafkaListener(id = "collect-zbx-9", topicPartitions = {@TopicPartition(topic = "${kafka.topic.cmdb_instance_topic_zbx:Ci_Zbx}", partitions = {"9"})})
    public void listenZbxPartition9(ConsumerRecord<?, String> cr) {
        log.info("collect-zbx-9 Listener, Thread ID:{}, record: {}", Thread.currentThread().getId(), cr);
        resovleData(cr.value());
    }

    // promethus监听
    @KafkaListener(id = "collect-pro-0", topicPartitions = {@TopicPartition(topic = "${kafka.topic.cmdb_instance_topic_pro:Ci_Pro01}", partitions = {"0"})})
    public void listenProPartition0(ConsumerRecord<?, String> cr) {
        log.info("collect-pro-0 Listener, Thread ID:{}, record: {}", Thread.currentThread().getId(), cr);
        resovleData(cr.value());
    }
    @KafkaListener(id = "collect-pro-1", topicPartitions = {@TopicPartition(topic = "${kafka.topic.cmdb_instance_topic_pro:Ci_Pro}", partitions = {"1"})})
    public void listenProPartition1(ConsumerRecord<?, String> cr) {
        log.info("collect-pro-1 Listener, Thread ID:{}, record: {}", Thread.currentThread().getId(), cr);
        resovleData(cr.value());
    }
    @KafkaListener(id = "collect-pro-2", topicPartitions = {@TopicPartition(topic = "${kafka.topic.cmdb_instance_topic_pro:Ci_Pro}", partitions = {"2"})})
    public void listenProPartition2(ConsumerRecord<?, String> cr) {
        log.info("collect-pro-2 Listener, Thread ID:{}, record: {}", Thread.currentThread().getId(), cr);
        resovleData(cr.value());
    }
    @KafkaListener(id = "collect-pro-3", topicPartitions = {@TopicPartition(topic = "${kafka.topic.cmdb_instance_topic_pro:Ci_Pro}", partitions = {"3"})})
    public void listenProPartition3(ConsumerRecord<?, String> cr) {
        log.info("collect-zbx-3 Listener, Thread ID:{}, record: {}", Thread.currentThread().getId(), cr);
        resovleData(cr.value());
    }
    @KafkaListener(id = "collect-pro-4", topicPartitions = {@TopicPartition(topic = "${kafka.topic.cmdb_instance_topic_pro:Ci_Pro}", partitions = {"4"})})
    public void listenProPartition4(ConsumerRecord<?, String> cr) {
        log.info("collect-pro-4 Listener, Thread ID:{}, record: {}", Thread.currentThread().getId(), cr);
        resovleData(cr.value());
    }
    @KafkaListener(id = "collect-pro-5", topicPartitions = {@TopicPartition(topic = "${kafka.topic.cmdb_instance_topic_pro:Ci_Pro}", partitions = {"5"})})
    public void listenProPartition5(ConsumerRecord<?, String> cr) {
        log.info("collect-pro-5 Listener, Thread ID:{}, record: {}", Thread.currentThread().getId(), cr);
        resovleData(cr.value());
    }
    @KafkaListener(id = "collect-pro-6", topicPartitions = {@TopicPartition(topic = "${kafka.topic.cmdb_instance_topic_pro:Ci_Pro}", partitions = {"6"})})
    public void listenProPartition6(ConsumerRecord<?, String> cr) {
        log.info("collect-pro-6 Listener, Thread ID:{}, record: {}", Thread.currentThread().getId(), cr);
        resovleData(cr.value());
    }
    @KafkaListener(id = "collect-pro-7", topicPartitions = {@TopicPartition(topic = "${kafka.topic.cmdb_instance_topic_pro:Ci_Pro}", partitions = {"7"})})
    public void listenProPartition7(ConsumerRecord<?, String> cr) {
        log.info("collect-pro-7 Listener, Thread ID:{}, record: {}", Thread.currentThread().getId(), cr);
        resovleData(cr.value());
    }
    @KafkaListener(id = "collect-pro-8", topicPartitions = {@TopicPartition(topic = "${kafka.topic.cmdb_instance_topic_pro:Ci_Pro}", partitions = {"8"})})
    public void listenProPartition8(ConsumerRecord<?, String> cr) {
        log.info("collect-pro-8 Listener, Thread ID:{}, record: {}", Thread.currentThread().getId(), cr);
        resovleData(cr.value());
    }
    @KafkaListener(id = "collect-pro-9", topicPartitions = {@TopicPartition(topic = "${kafka.topic.cmdb_instance_topic_pro:Ci_Pro}", partitions = {"9"})})
    public void listenProPartition9(ConsumerRecord<?, String> cr) {
        log.info("collect-pro-9 Listener, Thread ID:{}, record: {}", Thread.currentThread().getId(), cr);
        resovleData(cr.value());
    }


    private void resovleData(String value) {
        if (StringUtils.isNotEmpty(value)) {
            JSONObject jsonData = JSON.parseObject(value);
            String ip = "";
            String pool = "";
            String deviceType = "";
            String key = "";
            String updateValue = "";
            CmdbCode updateCode = null;
            String source = jsonData.getString(CollectConst.COLLECT_SOURCE);
            if (jsonData.containsKey(CollectConst.PRO_METRIC)) {
                //promethus数据（主机是云主机数据）
                JSONObject metric = jsonData.getJSONObject(CollectConst.PRO_METRIC);
                key = metric.getString(CollectConst.PRO_KEY);
                // 获取ip和pool
                ip = metric.getString("ip");
                deviceType = jsonData.getString("device_type");
                updateValue = jsonData.getJSONArray("value").get(1).toString().trim();
                if (StringUtils.isEmpty(updateValue)) {
                    log.info("采集最新值为空，进行过滤");
                    return;
                }
            } else if ("zbx".equalsIgnoreCase(source) || "ops".equalsIgnoreCase(source)) {
                //zabbix数据（几乎所有数据采集从这过来）
                key = jsonData.getString(CollectConst.ZBX_KEY);
                ip = jsonData.getString("ip");
                deviceType = jsonData.getString("device_type");
                updateValue = jsonData.getString("lastvalue").trim();
                if (StringUtils.isEmpty(updateValue)) {
                    log.info("采集最新值为空，进行过滤");
                    return;
                }
            }
            Map<String, String> poolResult = dictMapper.getIdcTypeByName(jsonData.getString("pool"));
            if (poolResult == null || poolResult.size() ==0) {
                log.error("采集返回数据中资源池在cmdb中不存在");
                throw new RuntimeException("采集返回数据中资源池在cmdb中不存在");
            }
            pool = poolResult.get("id");
            if (StringUtils.isEmpty(ip) || StringUtils.isEmpty(pool)) {
                log.error("采集返回数据中未包含ip或资源池信息");
                throw new RuntimeException("采集返回数据中未包含ip或资源池信息");
            }
            String ipReg = "^((\\d)|([1-9]\\d)|(1\\d{2})|((2[0-4]\\d)|(25[0-5])))(\\.((\\d)|([1-9]\\d)|(1\\d{2})|((2[0-4]\\d)|(25[0-5])))){3}$";
            Pattern ipPattern = Pattern.compile(ipReg);
            boolean flag = ipPattern.matcher(ip).matches();
            if (!flag) {
                throw new RuntimeException("ip地址：" + ip + "不符合规则");
            }
//            Map<String, Object> instance = null;
            // 根据数据源来获取instance
//            if ("pro".equals(source)) {
//                // ip为ipmi地址
//                instance = instanceService.getInstanceByIPMI(ip, pool);
//            } else if ("zbx".equals(source) || "ops".equalsIgnoreCase(source)) {
                CmdbInstance queryInstance = new CmdbInstance();
                queryInstance.setIp(ip);
                queryInstance.setIdcType(pool);
                Map<String, Object> query = new HashMap<>();
                query.put("ip", ip);
                query.put("idcType", pool);
//                instance = instanceService.queryDeviceByIdcTypeAndIP(query);
//            }
            List<CmdbInstance> instanceList = instanceService.getInstanceByIp(query);
            if (instanceList == null || instanceList.size() == 0) {
                log.error("source: {}, ip:{}, pool:{}.未查到相应设备",source, ip , pool);
                if (!"ops".equals(source)) {
                    saveUnknownInstance(ip, pool, deviceType);
                }
                throw new RuntimeException("source: " + source + ", ip：" + ip + ", pool:" + pool +".未查到相应设备");
            }
            CmdbInstance instance = instanceList.get(0);
            // 根据moduleId获取module
            Module module = moduleMapper.getModuleDetail(instance.getModuleId());
            if (null == module) {
                log.error("source: {}, ip:{}, pool:{}.未查到相应模型", source, ip , pool);
                throw new RuntimeException("ip：" + ip + ",pool:" + pool +".未查到相应模型");
            }
            // 获取当前模型下的所有采集码表字段
            Map<String, CmdbCode> codeMap = moduleCodes.get(module.getName());
            if (null != codeMap && codeMap.containsKey(key)) {
                updateCode = codeMap.get(key);
            }
            if (null == updateCode) {
                initCode(module);
                codeMap = moduleCodes.get(module.getName());
                if (null != codeMap && codeMap.containsKey(key)) {
                    updateCode = codeMap.get(key);
                } else {
                    log.error("采集指标：{} 不存在", key);
                    throw new RuntimeException("采集指标：" + key + "不存在");
                }
            }

            // 源数据存储
            CmdbCollectResource resource = new CmdbCollectResource();
            resource.setId(UUIDUtil.getUUID());
            resource.setInstanceId(instance.getId());
            resource.setCreateTime(new Date());
            resource.setCodeId(updateCode.getCodeId());
            resource.setValue(updateValue);
            resourceService.insert(resource);
            // 存入变更审批表
            Map<String, Object> upateData = new HashMap<>();
            upateData.put("id", instance.getId());
            upateData.put("module_id", module.getId());
            upateData.put(updateCode.getFiledCode(), updateValue);
            instanceService.updateInstance(instance.getId(), "系统管理员", upateData,"自动采集");
        }
    }

    private void initDeviceType() {
        this.deviceTypeList = new ArrayList<>();
        List<ConfigDict> dicts = dictMapper.selectDictsByType("device_type", null, null, null);
        dicts.forEach(dict -> {
            this.deviceTypeList.add(dict.getValue());
        });
    }

    private void initCode(Module module) {
        List<CmdbCode> codes = codeMapper.getCodeListByModuleId(module.getId());
        Map<String, CmdbCode> codeMap = new HashMap<>();
        for (CmdbCode code : codes) {
            if ("否".equals(code.getIsCollect())) {
                continue;
            }
            if (code.getCodeCollect() == null) {
                log.error("码表字段[{}]未配置采集信息", code.getFiledName());
                continue;
            }
            CmdbV3CodeCollect codeCollect = code.getCodeCollect();
            if (!"zabbix".equalsIgnoreCase(codeCollect.getCollectType())) {
                continue;
            }
            if (StringUtils.isEmpty(codeCollect.getCollectMapperKey())) {
                log.error("码表字段[{}]未配置映射", code.getFiledName());
                continue;
            }
            codeMap.put(codeCollect.getCollectMapperKey(), code);
        }
        moduleCodes.put(module.getName(), codeMap);
    }

    public void saveUnknownInstance (String ip, String pool, String deviceType) {
        if (deviceTypeList == null || deviceTypeList.size() == 0) {
            initDeviceType();
        }
        if (!deviceTypeList.contains(deviceType)) {
            initDeviceType();
            if (!deviceTypeList.contains(deviceType)) {
                throw new RuntimeException("不支持采集设备类型：" + deviceType);
            }
        }
        if (StringUtils.isEmpty(deviceType)) {
            log.error("采集返回数据中未包含设备类型信息");
            throw new RuntimeException("采集返回数据中未包含设备类型信息");
        }
        CmdbCollectUnknown collectUnknown = new CmdbCollectUnknown();
        collectUnknown.setIp(ip);
        collectUnknown.setIdcType(pool);
        collectUnknown.setDeviceType(deviceType);
        collectUnknown.setCommitUser("系统管理员");
        collectUnknown.setDataFrom("自动采集");
        unknownService.insert(collectUnknown);
    }

}
