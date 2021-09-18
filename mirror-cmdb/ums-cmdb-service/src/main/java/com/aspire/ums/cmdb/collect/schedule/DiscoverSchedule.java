package com.aspire.ums.cmdb.collect.schedule;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aspire.ums.cmdb.collect.CollectConst;
import com.aspire.ums.cmdb.collect.entity.AutoDiscoveryLogEntity;
import com.aspire.ums.cmdb.collect.entity.AutoDiscoveryRuleEntity;
import com.aspire.ums.cmdb.collect.service.AutoDiscoveryLogService;
import com.aspire.ums.cmdb.collect.service.AutoDiscoveryLogShieldService;
import com.aspire.ums.cmdb.collect.service.AutoDiscoveryRuleService;
import com.aspire.ums.cmdb.util.CronUtil;
import com.aspire.ums.cmdb.util.UUIDUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.SchedulingException;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.config.TriggerTask;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;


@Component
@Slf4j
@EnableScheduling
public class DiscoverSchedule implements SchedulingConfigurer {

    @Autowired
    AutoDiscoveryLogService logService;

    @Autowired
    AutoDiscoveryRuleService ruleService;

    @Autowired
    AutoDiscoveryLogShieldService logShieldService;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    private ScheduledTaskRegistrar taskRegistrar;

    private Map<String, ScheduledFuture<?>> taskFutures = new ConcurrentHashMap<String, ScheduledFuture<?>>();

    @Override
    public void configureTasks(ScheduledTaskRegistrar scheduledTaskRegistrar) {
        initSchedule(scheduledTaskRegistrar);
    }

    /**
    *初始化定时任务
     */
    private void initSchedule(ScheduledTaskRegistrar scheduledTaskRegistrar) {
        List<AutoDiscoveryRuleEntity> rules = ruleService.listRules();
        ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
        threadPoolTaskScheduler.setPoolSize(10);
        threadPoolTaskScheduler.initialize();
        scheduledTaskRegistrar.setTaskScheduler(threadPoolTaskScheduler);
        TaskScheduler scheduler = scheduledTaskRegistrar.getScheduler();
        for (AutoDiscoveryRuleEntity rule : rules) {
            String cron = CronUtil.createCron(rule.getCollectCycle(), rule.getCycleUnit());
            TriggerTask triggerTask = new TriggerTask(new DiscoveryFromKafka(kafkaTemplate, rule), new CronTrigger(cron));
            taskFutures.put(rule.getId(), scheduler.schedule(triggerTask.getRunnable(), triggerTask.getTrigger()));
            this.taskRegistrar = scheduledTaskRegistrar;
        }
    }


    /**
     * 添加定时任务
     */
    public void addTriggerTask(AutoDiscoveryRuleEntity rule)
    {
        if (taskFutures.containsKey(rule.getId()))
        {
            throw new SchedulingException("the taskId[" + rule.getId() + "] was added.");
        }
        TaskScheduler scheduler = taskRegistrar.getScheduler();
        String cron = CronUtil.createCron(rule.getCollectCycle(), rule.getCycleUnit());
        TriggerTask triggerTask = new TriggerTask(new DiscoveryFromKafka(kafkaTemplate, rule), new CronTrigger(cron));
        ScheduledFuture<?> future = scheduler.schedule(triggerTask.getRunnable(), triggerTask.getTrigger());
        taskFutures.put(rule.getId(), future);
    }

    /**
     * 取消任务
     *
     * @param taskId
     */
    public void cancelTriggerTask(String taskId)
    {
        ScheduledFuture<?> future = taskFutures.get(taskId);
        if (future != null)
        {
            future.cancel(true);
        }
        taskFutures.remove(taskId);
    }

    /**
     * 重置任务
     *
     * @param rule
     */
    public void resetTriggerTask(AutoDiscoveryRuleEntity rule)
    {
        cancelTriggerTask(rule.getId());
        addTriggerTask(rule);
    }

    /**
     * 监控kafka消费消息
     */
    @KafkaListener(topics = {"cmdb_discovery_info_response"})
    private void discoveryKafkaMessage(String response) {
        if (log.isInfoEnabled()) {
            log.info("topic -> cmdb_discovery_info_response get kafka messages -> {}", response);
        }
        if (response != null) {
            JSONObject jsonData = JSON.parseObject(response);
            String ruleId = jsonData.getString("rule_id");
            List<String> shieldIps = logShieldService.list(ruleId);
            List<String> existIps = logService.getInstanceNameByRuleId(ruleId);
            if (jsonData.containsKey("data_list")) {
                JSONArray dataList = jsonData.getJSONArray("data_list");
                for (Object data: dataList) {
                    JSONObject json = JSONObject.parseObject(data.toString());
                    if (json.containsKey("ip")) {
                        String ip = json.getString("ip");
                        AutoDiscoveryLogEntity logEntity = new AutoDiscoveryLogEntity();
                        if (!shieldIps.contains(ip) && !existIps.contains(ip)) {
                            logEntity.setInstanceName(ip);
                            logEntity.setId(UUIDUtil.getUUID());
                            logEntity.setCreateTime(new Date());
                            logEntity.setStatus(CollectConst.PENDING);
                            logEntity.setRuleId(ruleId);
                            logService.insertVO(logEntity);
                            log.info("topic -> cmdb_discovery_info_response discovery new ip-> {]", ip);
                        } else {
                            logEntity.setUpdateTime(new Date());
                            logService.updateVO(logEntity);
                            log.info("topic -> cmdb_discovery_info_response discovery exist ip-> {}", ip);
                        }
                    }
                }
            }
        }
    }


}
