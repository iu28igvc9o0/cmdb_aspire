package com.aspire.mirror.alert.api.dto.taskChange;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class TaskRequest {
    /**
     * 配置id
     */
    private String uuid;
    /**
     * 任务名称
     */
    private String taskName;
    /**
     * 任务类型 1-割接 2-变更
     */
    private String taskType;
    /**
     * 任务描述
     */
    private String taskDescription;
    /**
     * 资源池
     */
    private String idcType;
    /**
     * 任务开始时间
     */
//    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
//    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String taskStartTime;
    /**
     * 任务结束时间
     */
//    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
//    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String taskEndTime;
    /**
     * 工单id
     */
    private String orderId;
    /**
     * 创建者
     */
    private String creator;
    /**
     * 更新者
     */
    private String updater;

}
