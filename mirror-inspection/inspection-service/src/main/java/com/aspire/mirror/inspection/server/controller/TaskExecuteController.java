package com.aspire.mirror.inspection.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.aspire.mirror.inspection.api.service.TaskExecuteApiService;
import com.aspire.mirror.inspection.server.biz.TaskExecuteBiz;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 巡检任务执行    <br/>
 * Project Name:inspection-service
 * File Name:TaskExecuteController.java
 * Package Name:com.aspire.mirror.inspection.server.controller
 * ClassName: TaskExecuteController <br/>
 * date: 2018年8月30日 下午3:56:34 <br/>
 *
 * @author pengguihua
 * @since JDK 1.6
 */
@Slf4j
@RestController
public class TaskExecuteController implements TaskExecuteApiService {
    @Autowired
    private TaskExecuteBiz taskExecuteBiz;

    @Override
    public void executeInspectionTask(@PathVariable("taskId") String taskId) {
        log.info("Begin to execute the inpsection task with id {}", taskId);
        ExecutorService executor = Executors.newFixedThreadPool(1);
        CompletableFuture.runAsync(() -> {
            taskExecuteBiz.executeInspectionTask(taskId);
        }, executor);
    }
}
