package com.aspire.cmdb.agent.collect.schedule;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aspire.cmdb.agent.collect.CollectConst;
import com.aspire.cmdb.agent.collect.entity.EsAgentDataEntity;
import com.aspire.cmdb.agent.collect.mapper.EsAgentDataMapper;
import com.aspire.cmdb.agent.util.StringUtils;
import com.aspire.ums.cmdb.client.ICmdbESClient;
import com.aspire.ums.cmdb.collectUnknown.payload.CmdbCollectUnknown;
import com.aspire.ums.cmdb.dict.mapper.ConfigDictMapper;
import com.aspire.ums.cmdb.dict.payload.ConfigDict;
import com.aspire.ums.cmdb.instance.payload.CmdbInstance;
import com.aspire.ums.cmdb.util.DateUtils;
import com.aspire.ums.cmdb.v2.collectUnknown.service.CmdbCollectUnknownService;
import com.aspire.ums.cmdb.v2.instance.service.ICmdbInstanceService;
import com.aspire.ums.cmdb.v2.module.service.ModuleService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.regex.Pattern;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: MonitorFromKafka
 * Author:   hangfang 资源池各主机业务监控、带外监控状态上报任务类
 * Date:     2019/8/21
 * Description: DESCRIPTION
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Data
@Component
@Slf4j
public class MonitorFromKafka {

    @Autowired
    private ICmdbInstanceService instanceService;
    @Autowired
    private ConfigDictMapper dictMapper;
    @Autowired
    private ModuleService moduleService;
    @Autowired
    private CmdbCollectUnknownService unknownService;
    @Autowired
    private EsAgentDataMapper esAgentDataMapper;
    //默认未发现设备处理状态
    private static final Integer DEFAULT_HANDLE = 0;
    private Integer updateFailed = 0;
    private Integer updateSuccess = 0;
    private List<String> monitorPools;
    public final static List<String> poolTranslate= Arrays.asList(new String[]{"HHhot", "Xxg","Hrb"});
    private List<String> deviceTypeList = new ArrayList<>();

    @Autowired
    private ICmdbESClient esClient;

    @KafkaListener(id = "monitor-0", topics = {"${kafka.topic.cmdb_instance_monitor:monitor_host}"})
    public void monitor(ConsumerRecord<?, String> cr) {
        log.info("monitor-0 Listener, Thread ID:{}, record: {}", Thread.currentThread().getId(), cr);
        resovleData(cr.value());
    }

    @Scheduled(cron = "0 0 0 1/1 * ?")
    public void resetMonitor() {
        //每天0点把资源池内所有监控置为否
        monitorPools = new ArrayList<>();
        updateFailed = 0;
        updateSuccess = 0;
        // 获取资源池
       setMonitorPools(monitorPools);
        // 重置资源池
        log.info("start to reset these pool: {} is_ansible to 否", monitorPools);
        instanceService.updateAllPool(monitorPools, "11463");
    }

    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    public void resovleData(String value) {
        String ip = "", poolId = "";
        List<Map<String, Object>> dataList = new ArrayList<>();
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("agent_date", DateUtils.format(new Date(), "yyyy-MM-dd"));
        data.put("ip", "");
        data.put("pool", "");
        data.put("result", "");
        data.put("instance_id", "");
        data.put("other_ip", "");
        data.put("exsi_ip", "");
        data.put("vm_name", "");
        data.put("source", "");
        data.put("type", "");
        data.put("collector_server", "");
        data.put("message", "");
        try {
            if (null == monitorPools || monitorPools.size() == 0) {
                monitorPools = new ArrayList<>();
                setMonitorPools(monitorPools);
            }
            if (StringUtils.isNotEmpty(value)) {
                JSONObject jsonData;
                try {
                    jsonData = JSONObject.parseObject(value);
                } catch (Exception e) {
                    log.error("kafka message {} cant be json parse", value);
                    throw new RuntimeException("kafka message  cant be json parse");
                }
                if (!jsonData.containsKey("ip") || !StringUtils.isNotEmpty(jsonData.get("ip")) || !jsonData.containsKey("pool") || !StringUtils.isNotEmpty(jsonData.get("pool"))) {
                    log.error("kafka message don't have key ip or pool");
                    throw new RuntimeException("缺少 ip 或 pool");
                }
                if (!jsonData.containsKey(CollectConst.COLLECT_SOURCE) || !StringUtils.isNotEmpty(jsonData.get(CollectConst.COLLECT_SOURCE))) {
                    log.error("kafka message don't have key source");
                    throw new RuntimeException("缺少 source");
                }
                Map<String, String> poolResult = dictMapper.getIdcTypeByName(jsonData.get("pool").toString());
                poolId = poolResult.get("id");
                ip = jsonData.get("ip").toString();
                if (!toValidIp(ip)) {
                    throw new RuntimeException("ip地址：" + ip + "不符合规则");
                }
                String deviceType = jsonData.containsKey("device_type") && StringUtils.isNotEmpty(jsonData.get("device_type")) ? jsonData.get("device_type").toString() : "";
                String source = jsonData.get(CollectConst.COLLECT_SOURCE).toString();
                // 处理信息港云主机数据
                Map<String, String> vmData = new HashMap<>();
                if (jsonData.containsKey("hostname") && StringUtils.isNotEmpty(jsonData.get("hostname"))) {
                    // 承载虚拟网名称
                    vmData.put("vmName", jsonData.get("hostname").toString() );
                    data.put("vmName", jsonData.get("hostname").toString());
                }
                if (jsonData.containsKey("parentHost") && StringUtils.isNotEmpty(jsonData.get("parentHost"))) {
                    vmData.put("existIp", jsonData.get("parentHost").toString() );
                    data.put("existIp", jsonData.get("parentHost").toString() );
                }
                if (jsonData.containsKey("appip") && StringUtils.isNotEmpty(jsonData.get("appip"))) {
                    List<String> ipList = JSONArray.parseArray(JSON.toJSONString(jsonData.get("appip")), String.class);
                    String otherIpString = "";
                    for (String oIp : ipList) {
                        if (!toValidIp(ip)) {
                            continue;
                        }
                        otherIpString = otherIpString + oIp;
                        otherIpString = otherIpString + ",";
                    }
                    if (otherIpString.length() > 0) {
                        otherIpString = otherIpString.substring(0, otherIpString.length() - 1);
                        vmData.put("otherIp", otherIpString);
                        data.put("otherIp", source);
                    }
                }
                if (jsonData.containsKey("type") && StringUtils.isNotEmpty(jsonData.get("type"))) {
                    data.put("type", jsonData.get("type"));
                }
                String agentStatus = "11462";
                if (jsonData.containsKey("message") && StringUtils.isNotEmpty(jsonData.get("message"))) {
                    String message = jsonData.get("message").toString();
                    if (message.equals("采集失败")) {
                        agentStatus = "11466";
                    }
                    data.put("message", message);
                }
                if (jsonData.containsKey("collector_server") && StringUtils.isNotEmpty(jsonData.get("collector_server"))) {
                    data.put("collector_server", jsonData.get("collector_server"));
                }
                data.put("ip", ip);
                data.put("pool", poolId);
                data.put("source", source);
                if (StringUtils.isEmpty(source)) {
                    log.error("缺少数据来源");
                    throw new RuntimeException("缺少数据来源");
                }
                Map<String, Object> queryParam = new HashMap<>();
                queryParam.put("ip", ip);
                queryParam.put("idcType", poolId);
                List<CmdbInstance> queryInstanceList = instanceService.getInstanceByIp(queryParam);
                if (queryInstanceList == null || queryInstanceList.size() == 0) {
                    log.error("ip:{},pool:{}.未查到相应设备",ip , poolId);
                    updateFailed++;
                    saveUnknownInstance(ip, poolId, deviceType, vmData);
                    return;
                }
                CmdbInstance instance = queryInstanceList.get(0);
                data.put("instance_id", instance.getId());
                if ("zbx".equals(source)) {
                    updateSuccess++;
                    instanceService.updateZbxMonitorStatus(instance.getId(), agentStatus);
                }
                if ("pro".equals(source)) {
                    updateSuccess++;
                    instanceService.updateProMonitorStatus(instance.getId(), agentStatus);
                }
                data.put("result", "成功");
                log.info("更新监控结束, 成功更新{}条数据,更新失败未找到相应数据{}条", updateSuccess, updateFailed);
            }
            data.put("result", "成功");
        } catch (Exception e) {
            data.put("result", e.getMessage());
            throw new RuntimeException(e);
        } finally {
            dataList.add(data);
            // 记录到mysql库
            esAgentDataMapper.insert(new EsAgentDataEntity().parseMapToEntity(dataList.get(0)));
            // 记录到ES库
            esClient.insert(dataList, "cmdb_agent_monitor_log", "monitor_status");
        }
    }

    private boolean toValidIp(String ip) {
        String ipReg = "^((\\d)|([1-9]\\d)|(1\\d{2})|((2[0-4]\\d)|(25[0-5])))(\\.((\\d)|([1-9]\\d)|(1\\d{2})|((2[0-4]\\d)|(25[0-5])))){3}$";
        Pattern ipPattern = Pattern.compile(ipReg);
        return ipPattern.matcher(ip).matches();
    }

    public void setMonitorPools(List<String> monitorPools) {
        List<ConfigDict> dicts = dictMapper.selectDictsByType("monitor_idc_type", null, null, null);
        for (ConfigDict dict : dicts) {
            this.monitorPools.add(dict.getValue());
        }
        log.info("获取到资源池数据 {}", monitorPools);
    }

    private void initDeviceType() {
        List<ConfigDict> dicts = dictMapper.selectDictsByType("device_type", null, null, null);
        dicts.forEach(dict -> {
            this.deviceTypeList.add(dict.getValue());
        });
    }
    public void saveUnknownInstance (String ip, String pool, String deviceType, Map<String, String> otherData) {
        if (deviceTypeList.size() == 0) {
            initDeviceType();
        }
//        if (!deviceTypeList.contains(deviceType)) {
//            initDeviceType();
//            if (!deviceTypeList.contains(deviceType)) {
//                throw new RuntimeException("不支持采集设备类型：" + deviceType);
//            }
//        }
        CmdbCollectUnknown collectUnknown = new CmdbCollectUnknown();
        if (!StringUtils.isNotEmpty(deviceType)) {
            log.error("采集返回数据中未包含设备类型信息");
        }
        collectUnknown.setDeviceType(deviceType);
        collectUnknown.setIp(ip);
        collectUnknown.setIdcType(pool);
        try {
            BeanUtils.populate(collectUnknown, otherData);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        collectUnknown.setCommitUser("系统管理员");
        collectUnknown.setDataFrom("监控上报");
        unknownService.insert(collectUnknown);
    }
}
