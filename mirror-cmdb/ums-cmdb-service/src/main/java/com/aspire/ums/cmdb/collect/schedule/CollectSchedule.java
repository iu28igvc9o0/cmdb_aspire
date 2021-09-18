package com.aspire.ums.cmdb.collect.schedule;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.aspire.ums.cmdb.collect.CollectConst;
import com.aspire.ums.cmdb.collect.entity.CollectEntity;
import com.aspire.ums.cmdb.collect.entity.CollectOriginalRecordEntity;
import com.aspire.ums.cmdb.collect.pool.ThreadPool;
import com.aspire.ums.cmdb.collect.service.CollectChangeLogService;
import com.aspire.ums.cmdb.collect.service.CollectOriginalRecordService;
import com.aspire.ums.cmdb.collect.service.CollectService;
import com.aspire.ums.cmdb.helper.MailSendHelper;
import com.aspire.ums.cmdb.maintain.entity.InstanceBaseColumn;
import com.aspire.ums.cmdb.maintain.service.FormValueService;
import com.aspire.ums.cmdb.maintain.service.InstanceService;
import com.aspire.ums.cmdb.util.UUIDUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: CollectSchedule
 * Author:   zhu.juwang
 * Date:     2019/3/14 13:54
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@EnableScheduling
@Component
@Slf4j
public class CollectSchedule {
    @Autowired
    private CollectService collectService;
    @Autowired
    private InstanceService instanceService;
    @Autowired
    private CollectOriginalRecordService recordService;
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;
    @Autowired
    private MailSendHelper mailSendHelper;
    @Autowired
    private FormValueService formValueService;
    @Autowired
    private CollectChangeLogService changeLogService;

    //高频率 采集信息
    @Scheduled(cron = "0 0 0 */3 * ?")
    private void collectHighFrequency() {
        List<CollectEntity> entityList = collectService.getCollectByFrequency(CollectConst.COLLECT_FREQUENCY_HIGH);
        Map<String, Map<String, Object>> taskMap = getCollectListByFrequency(entityList);
        generateCollectTask(entityList, taskMap);
    }

    //中频率 采集信息
    @Scheduled(cron = "0 0 0 */5 * ?")
    private void collectMidFrequency() {
        List<CollectEntity> entityList = collectService.getCollectByFrequency(CollectConst.COLLECT_FREQUENCY_MID);
        Map<String, Map<String, Object>> taskMap = getCollectListByFrequency(entityList);
        generateCollectTask(entityList, taskMap);
    }

    //低频率 采集信息
    @Scheduled(cron = "0 0 0 */7 * ?")
    private void collectLowFrequency() {
        List<CollectEntity> entityList = collectService.getCollectByFrequency(CollectConst.COLLECT_FREQUENCY_LOW);
        Map<String, Map<String, Object>> taskMap = getCollectListByFrequency(entityList);
        generateCollectTask(entityList, taskMap);
    }

    /**
     * 监控kafka消费消息
     */
    @KafkaListener(topics = {"cmdb_collect_info_response"})
    private void collectKafkaMessage(String response) {
        //String response = "{\"collect_object_type\": \"1\", \"status\": \"true\", \"room\": null, \"task_id\": \"c2e1ec9631914da8adc975bc1357f4ec\", \"area\": null, \"batchId\": \"B201904111122\", \"device_list\": [{\"cpu_number\": 8, \"status\": \"true\", \"host_hostname\": \"wanglei-4\", \"host_architecture\": \"OpenStack Nova\", \"device_ip\": \"10.12.70.40\", \"device_id\": \"fe787f484b404123ab2af51f8517b163\", \"mount_list\": [{\"mount_point\": \"/\", \"mount_device\": \"/dev/mapper/vg-lvm--root\", \"mount_size_available\": 3000639488, \"mount_size_total\": 33153662976, \"mount_fstype\": \"ext4\", \"mount_options\": \"rw,relatime,data=ordered\"}, {\"mount_point\": \"/boot\", \"mount_device\": \"/dev/vda1\", \"mount_size_available\": 358477824, \"mount_size_total\": 511647744, \"mount_fstype\": \"ext4\", \"mount_options\": \"rw,relatime,data=ordered\"}, {\"mount_point\": \"/opt\", \"mount_device\": \"/dev/mapper/vg-lvm--opt\", \"mount_size_available\": 170161983488, \"mount_size_total\": 220755968000, \"mount_fstype\": \"ext4\", \"mount_options\": \"rw,relatime,data=ordered\"}, {\"mount_point\": \"/var/lib/docker/devicemapper\", \"mount_device\": \"/dev/mapper/vg-lvm--root\", \"mount_size_available\": 3000631296, \"mount_size_total\": 33153662976, \"mount_fstype\": \"ext4\", \"mount_options\": \"rw,relatime,data=ordered\"}], \"network_list\": [{\"network_macaddress\": \"02:42:9c:22:51:2d\", \"network_active\": true, \"network_network\": \"172.17.0.0\", \"network_ipaddress\": \"172.17.0.1\", \"network_netmask\": \"255.255.0.0\", \"network_type\": \"bridge\"}, {\"network_macaddress\": \"36:ef:83:ec:de:cb\", \"network_active\": true, \"network_network\": \"\", \"network_ipaddress\": \"\", \"network_netmask\": \"\", \"network_type\": \"ether\"}, {\"network_macaddress\": \"22:1e:57:92:5f:9d\", \"network_active\": true, \"network_network\": \"\", \"network_ipaddress\": \"\", \"network_netmask\": \"\", \"network_type\": \"ether\"}, {\"network_macaddress\": \"ce:cb:89:85:1e:2b\", \"network_active\": true, \"network_network\": \"\", \"network_ipaddress\": \"\", \"network_netmask\": \"\", \"network_type\": \"ether\"}, {\"network_macaddress\": \"\", \"network_active\": true, \"network_network\": \"127.0.0.0\", \"network_ipaddress\": \"127.0.0.1\", \"network_netmask\": \"255.0.0.0\", \"network_type\": \"loopback\"}, {\"network_macaddress\": \"fa:16:3e:98:2f:89\", \"network_active\": true, \"network_network\": \"10.12.70.0\", \"network_ipaddress\": \"10.12.70.40\", \"network_netmask\": \"255.255.255.0\", \"network_type\": \"ether\"}], \"memory_size\": 15885, \"cpu_type\": \"x86_64\", \"swap_size\": 8191, \"os_type\": \"Linux\", \"message\": \"\", \"os_detail_version\": \"7.4.1708\", \"disk_list\": [{\"disk_sectorsize\": \"512\", \"disk_sectors\": \"524288000\", \"disk_size\": \"250.00 GB\", \"disk_model\": null, \"disk_key\": \"vda\"}, {\"disk_sectorsize\": \"2048\", \"disk_sectors\": \"844\", \"disk_size\": \"1.65 MB\", \"disk_model\": \"QEMU DVD-ROM\", \"disk_key\": \"sr0\"}]}], \"message\": \"\", \"idc\": null}";
        if (log.isInfoEnabled()) {
            log.info("topic -> cmdb_collect_info_response get kafka messages -> {}", response);
        }
        if (response != null) {
            //先记录这次响应的状态
            JSONObject jsonData = JSON.parseObject(response);
            //先更新任务状态
            String taskId = jsonData.getString("task_id");
            boolean status = jsonData.getBoolean("status");
            //采集失败 邮件告警
            if (!status) {
                String message = jsonData.getString("message");
                Map<String, Object> recordEntity = new HashMap<>();
                recordEntity.put("taskId", taskId);
                recordEntity.put("collectStatus", CollectConst.COLLECT_STATUS_FAIL);
                recordEntity.put("collectResult", message);
                recordService.updateVOByTaskId(recordEntity);
                //todo 需要告警
                //sendAlert(instanceId, deviceIp, String.format("采集配置失败(%s)", message), null, collectEntity.getAlermLevel());
                return;
            }
            //使用线程池处理返回的每个主机IP数据
            ExecutorService executorService = ThreadPool.getExecutorService();
            final String batchId = jsonData.getString("batchId");
            //处理每个设备的数据
            final Map<String, Map<String, List<JSONObject>>> noticeMap = new HashMap<>();
            final JSONArray deviceArray = jsonData.getJSONArray("device_list");
            //根据taskId 获取数据库中所有的任务
            List<Map<String, String>> taskCollectList = recordService.getCollectInfoByTaskId(taskId);
            if (taskCollectList != null && taskCollectList.size() > 0) {
                List<String> failInstances = new ArrayList<>();
                for (Map<String, String> taskCollect : taskCollectList) {
                    executorService.execute(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                long activeCount = ((ThreadPoolExecutor) executorService).getActiveCount();
                                long queueCount = ((ThreadPoolExecutor) executorService).getQueue().size();
                                log.info("在处理任务->{} 任务等待队列数量->{}",String.valueOf(activeCount), String.valueOf(queueCount));
                                Date startDate = new Date();
                                String instanceId = taskCollect.get("instanceId");
                                JSONObject instanceData = collectInstance(instanceId, deviceArray);
                                if (instanceData == null) {
                                    return;
                                }
                                boolean deviceStatus = instanceData.getBoolean("status");
                                //获取实例数据失败
                                if (!deviceStatus && !failInstances.contains(instanceId)) {
                                    String deviceMessage = instanceData.getString("message");
                                    Map<String, Object> instanceRecordEntity = new HashMap<>();
                                    instanceRecordEntity.put("taskId", taskId);
                                    instanceRecordEntity.put("instanceId", instanceId);
                                    instanceRecordEntity.put("collectStatus", CollectConst.COLLECT_STATUS_FAIL);
                                    instanceRecordEntity.put("collectResult", deviceMessage);
                                    recordService.updateVOByTaskId(instanceRecordEntity);
                                    failInstances.add(instanceId);
                                    return;
                                }
                                String formCode = taskCollect.get("formCode");
                                String formData = collectFormData(formCode, instanceId, deviceArray);
                                //更新获取到的数据信息
                                Map<String, Object> instanceRecordEntity = new HashMap<>();
                                instanceRecordEntity.put("taskId", taskId);
                                instanceRecordEntity.put("instanceId", instanceId);
                                instanceRecordEntity.put("collectId", taskCollect.get("collectId"));
                                instanceRecordEntity.put("collectStatus", CollectConst.COLLECT_STATUS_SUCCESS);
                                instanceRecordEntity.put("collectResult", formData);
                                recordService.updateVOByTaskId(instanceRecordEntity);
                                //解析数据
                                recordService.resolveData(batchId, noticeMap, taskCollect, formData);
                                Date endDate = new Date();
                                log.info("Complated at {} s.", (endDate.getTime() - startDate.getTime()) / 1000);
                            } catch (Exception e) {
                                log.error("Resolve collect data error. {}", e.getMessage(), e);
                            }
                        }
                    });
                }
            }
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                log.error("Add time sleep error.", e);
            }
            long activeCount = ((ThreadPoolExecutor) executorService).getActiveCount();
            long queueCount = ((ThreadPoolExecutor) executorService).getQueue().size();
            while (activeCount != 0 || queueCount != 0) {
                try {
                    activeCount = ((ThreadPoolExecutor) executorService).getActiveCount();
                    queueCount = ((ThreadPoolExecutor) executorService).getQueue().size();
                    log.info("Waiting thread run finish ...");
                    TimeUnit.SECONDS.sleep(5);
                } catch (InterruptedException e) {
                    log.error("Add time sleep error.", e);
                }
            }
            log.info("Start send notice ...");
            log.info("need send notice size -> {}", noticeMap.size());
            //发送通知
            if (noticeMap.size() > 0) {
                Iterator iterator = noticeMap.keySet().iterator();
                while (iterator.hasNext()) {
                    String key = iterator.next().toString();
                    String[] keyInfo = key.split("_");
                    //组装消息
                    List<JSONObject> dataList = noticeMap.get(key).get("data");
                    StringBuilder changeBuilder = new StringBuilder(keyInfo[0] + "设备CMDB配置数据值变更");
                    for (JSONObject object : dataList) {
                        changeBuilder.append(",");
                        changeBuilder.append(object.get("formName")).append("字段:").append(object.get("oldValue"))
                                .append("->").append(object.get("currValue"));
                    }
                    String emailContent = changeLogService.getEmailContent(keyInfo[0], changeBuilder.toString(), keyInfo[2]).toString();
                    try {
                        mailSendHelper.sendMail("配置项异常变更告警", emailContent, true, CollectConst.EMPLOYEE.get(keyInfo[1]));
                    } catch (Exception e) {
                        log.error("发送配置项异常变更告警邮件失败 -> {}", e.getMessage(), e);
                    }
                }
            }
            log.info("End send notice .");
        }
    }

    /**
     * 获取采集过来实例
     * @param instanceId 实例ID
     * @param deviceArray 实例集合
     * @return
     */
    private JSONObject collectInstance(String instanceId, JSONArray deviceArray) {
        for (Object device : deviceArray) {
            JSONObject json = JSONObject.parseObject(device.toString());
            if (json.containsKey("device_id") && instanceId.equals(json.get("device_id"))) {
                return json;
            }
        }
        return null;
    }

    /**
     * 获取采集过来的字段值
     * @param formCode
     * @param instanceId
     * @param deviceArray
     * @return
     */
    private String collectFormData(String formCode, String instanceId, JSONArray deviceArray) {
        JSONObject deviceJson = collectInstance(instanceId, deviceArray);
        if (deviceJson != null && deviceJson.containsKey(formCode) && deviceJson.get(formCode) != null) {
            return deviceJson.get(formCode).toString();
        }
        return null;
    }

    /**
     * 生成采集任务
     * @param entityList 采集配置列表
     * @param taskMap 采集发送任务集合
     */
    private void generateCollectTask (List<CollectEntity> entityList, Map<String, Map<String, Object>> taskMap) {
        for (Map.Entry<String, Map<String, Object>> taskInfo : taskMap.entrySet()) {
            try {
                //发送采集任务
                Map<String, Object> requestInfo = taskInfo.getValue();
                String moduleId = requestInfo.get("module_id").toString();
                String taskId = requestInfo.get("task_id").toString();
                requestInfo.remove("module_id");
                for (CollectEntity collectEntity : entityList) {
                    if (collectEntity.getModuleId().equals(moduleId)) {
                        //获取module下所有的是instance
                        List<Map<String, String>> deviceList = (List<Map<String, String>>) taskInfo.getValue().get("device_list");
                        if (deviceList != null && deviceList.size() > 0) {
                            for (Map<String, String> deviceMap : deviceList) {
                                //新增采集任务
                                CollectOriginalRecordEntity recordEntity = new CollectOriginalRecordEntity();
                                recordEntity.setId(UUIDUtil.getUUID());
                                recordEntity.setCollectId(collectEntity.getId());
                                recordEntity.setInstanceId(deviceMap.get("device_id"));
                                recordEntity.setOperateType(CollectConst.COLLECT_TYPE_AUTO);
                                recordEntity.setCollectStatus(CollectConst.COLLECT_STATUS_EXECUTE);
                                recordEntity.setOperator("系统管理员");
                                recordEntity.setCollectTime(new Date());
                                recordEntity.setTaskId(taskId);
                                recordService.insertVO(recordEntity);
                            }
                        }
                    }
                }
                String requestMessage = JSON.toJSONString(requestInfo, SerializerFeature.WriteMapNullValue);
                if (log.isInfoEnabled()) {
                    log.info("Send kafka content -> {}", requestMessage);
                }
                String area = requestInfo.get("area") == null ? "" : requestInfo.get("area").toString();
                //发送kafka信息
                kafkaTemplate.send("cmdb_collect_info_request_" + area, requestMessage);
                if (log.isInfoEnabled()) {
                    log.info("Send kafka ok.");
                }
            } catch (Exception e) {
                log.error("Make collect task error. -> {}", e.getMessage() ,e);
                //todo 如果kafka消息发送异常, 则删除刚才创建的采集任务
            }
        }
    }

    /**
     * 根据采集频率 获取采集配置
     * @param entityList 采集配置集合
     * @return 采集配置列表
     */
    private Map<String, Map<String, Object>> getCollectListByFrequency(List<CollectEntity> entityList) {
        if (entityList == null || entityList.size() == 0) {
            return new HashMap<>();
        }
        Map<String, Map<String, Object>> requestModule = new HashMap<>();
        String batchId = "B" + new SimpleDateFormat("yyyyMMddHHmm").format(new Date());
        Map<String, List<InstanceBaseColumn>> cacheInstances = new HashMap<>();
        for (CollectEntity entity : entityList) {
            if (entity.getDisabled().equalsIgnoreCase(CollectConst.WHETHER_NO)) {
                continue;
            }
            //缓存起来, 提升效率
            List<InstanceBaseColumn> instanceList;
            if (cacheInstances.containsKey(entity.getModuleId())) {
                instanceList = cacheInstances.get(entity.getModuleId());
            } else {
                //获取所有的实例
                Map<String, Object> queryMap = new HashMap<>();
                queryMap.put("moduleId", entity.getModuleId());
                instanceList = instanceService.getInstanceBaseInfoList(queryMap);
                cacheInstances.put(entity.getModuleId(), instanceList);
            }
            if (instanceList == null || instanceList.size() == 0) {
                continue;
            }
            for (InstanceBaseColumn instance : instanceList) {
                //获取信息
                String idcType = instance.getIdcType();
                String roomId = instance.getRoomId();
                String area = instance.getDeviceRegion();
                //Map key使用 moduleId+idcType+roomId+area的形式. 区别发送不同的任务
                String key = entity.getModuleId() + "_" + idcType + "_" + roomId + "_" + area;
                Map<String, Object> instanceMap;
                if (requestModule.containsKey(key)) {
                    instanceMap = requestModule.get(key);
                } else {
                    instanceMap = new HashMap<>();
                    instanceMap.put("task_id", UUIDUtil.getUUID());
                    instanceMap.put("device_list", new ArrayList<>());
                }
                List<Map<String, Object>> deviceList = (List<Map<String, Object>>) instanceMap.get("device_list");
                boolean needInsert = true;
                for (Map<String, Object> deviceMap : deviceList) {
                    if (deviceMap.get("device_id").equals(instance.getInstanceId())) {
                        needInsert = false;
                        break;
                    }
                }
                if (needInsert) {
                    Map<String, Object> deviceMap = new HashMap<>();
                    deviceMap.put("device_id", instance.getInstanceId());
                    deviceMap.put("device_ip", instance.getIp());
                    JSONObject visitRequest = JSONObject.parseObject(entity.getVisitRequest());
                    if (entity.getVisitType().equalsIgnoreCase("ssh")) {
                        String userFormId = visitRequest.getJSONObject("ssh").getString("username");
                        visitRequest.getJSONObject("ssh").put("username", getFormValue(instance.getInstanceId(), userFormId));
                        String passFormId = visitRequest.getJSONObject("ssh").getString("password");
                        visitRequest.getJSONObject("ssh").put("password", getFormValue(instance.getInstanceId(), passFormId));
                    }
                    if (entity.getVisitType().equalsIgnoreCase("ipmi")) {
                        String userFormId = visitRequest.getJSONObject("ipmi").getString("username");
                        visitRequest.getJSONObject("ipmi").put("username", getFormValue(instance.getInstanceId(), userFormId));
                        String passFormId = visitRequest.getJSONObject("ipmi").getString("password");
                        visitRequest.getJSONObject("ipmi").put("password", getFormValue(instance.getInstanceId(), passFormId));
                    }
                    if (entity.getVisitType().equalsIgnoreCase("snmp")) {
                        String userFormId = visitRequest.getJSONObject("snmp").getString("public");
                        visitRequest.getJSONObject("snmp").put("public", getFormValue(instance.getInstanceId(), userFormId));
                    }
                    deviceMap.put("visit_type", entity.getVisitType().toLowerCase());
                    deviceMap.put("visit_request", visitRequest);
                    deviceList.add(deviceMap);
                }
                instanceMap.put("module_id", entity.getModuleId());
                instanceMap.put("idc", idcType);
                instanceMap.put("area", area);
                instanceMap.put("room", roomId);
                instanceMap.put("collect_object_type", "1");
                instanceMap.put("batchId", batchId);
                instanceMap.put("device_list", deviceList);
                requestModule.put(key, instanceMap);
            }
        }
        return requestModule;
    }

    private String getFormValue(String instanceId, String formId) {
        List<Map<String, String>> oldFormValues = formValueService.getFormValueMapByInstanceId(instanceId);
        for (Map<String, String> entry : oldFormValues) {
            if (entry.get("formId").equals(formId)) {
                return entry.get("formValue");
            }
        }
        return null;
    }
}
