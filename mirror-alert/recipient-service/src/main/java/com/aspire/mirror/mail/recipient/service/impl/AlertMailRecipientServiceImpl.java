package com.aspire.mirror.mail.recipient.service.impl;

import com.aspire.mirror.mail.recipient.client.AlertMailRecipientReceiver;
import com.aspire.mirror.mail.recipient.dao.AlertMailRecipientDao;
import com.aspire.mirror.mail.recipient.po.AlertMailRecipient;
import com.aspire.mirror.mail.recipient.service.AlertMailRecipientService;
import com.aspire.mirror.mail.recipient.util.CronUtil;
import com.aspire.mirror.mail.recipient.vo.AlertMailRecipientTaskRule;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.SchedulingException;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.config.TriggerTask;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

@Service
public class AlertMailRecipientServiceImpl implements AlertMailRecipientService, SchedulingConfigurer {

    @Autowired
    private AlertMailRecipientReceiver recipientReceiver;
    @Autowired
    private AlertMailRecipientDao recipientDao;
    private static ScheduledTaskRegistrar taskRegistrar;
    private Map<Integer, ScheduledFuture<?>> taskFutures = new ConcurrentHashMap<>();
    /**
     * 项目启动时加载所有定时任务
     *
     * @param taskRegistrar the registrar to be configured.
     */
    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        List<AlertMailRecipient> recipientList = recipientDao.selectActiveRecipients();
        ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
        threadPoolTaskScheduler.setPoolSize(10);
        threadPoolTaskScheduler.initialize();
        taskRegistrar.setTaskScheduler(threadPoolTaskScheduler);
        TaskScheduler scheduler = taskRegistrar.getScheduler();
        for (AlertMailRecipient recipient : recipientList) {
            String cron = CronUtil.createCron(recipient.getPeriod(), recipient.getUnit());
            TriggerTask triggerTask = new TriggerTask(() -> {
                recipientReceiver.produce(recipient);
            }, new CronTrigger(cron));
            taskFutures.put(recipient.getId(), scheduler.schedule(triggerTask.getRunnable(), triggerTask.getTrigger()));
        }
        this.taskRegistrar = taskRegistrar;
    }

    @Override
    public void addTriggerTask(AlertMailRecipientTaskRule taskRule) {
        Integer taskId = taskRule.getId();
        if (taskFutures.containsKey(taskId)) {
            throw new SchedulingException("the taskId[" + taskRule.getId() + "] was added.");
        }
        TaskScheduler scheduler = taskRegistrar.getScheduler();
        String cron = CronUtil.createCron(taskRule.getPeriod(), taskRule.getUnit() == null ? 0 : taskRule.getUnit());
        TriggerTask triggerTask = new TriggerTask(() -> {
            AlertMailRecipient recipient = new AlertMailRecipient();
            BeanUtils.copyProperties(taskRule, recipient);
            recipientReceiver.produce(recipient);
        }, new CronTrigger(cron));
        ScheduledFuture<?> future = scheduler.schedule(triggerTask.getRunnable(), triggerTask.getTrigger());
        taskFutures.put(taskId, future);
    }

    @Override
    public void cancelTriggerTask(Integer taskId) {
        ScheduledFuture<?> future = taskFutures.get(taskId);
        if (future != null) {
            future.cancel(true);
        }
        taskFutures.remove(taskId);
    }

    @Override
    public void resetTriggerTask(AlertMailRecipientTaskRule taskRule) {
        cancelTriggerTask(taskRule.getId());
        addTriggerTask(taskRule);
    }
}
