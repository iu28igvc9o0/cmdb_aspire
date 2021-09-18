package com.aspire.mirror.alert.server.vo.taskChange;

import lombok.Data;


@Data
public class TaskDetailDTO {
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
    private String taskStartTime;
    /**
     * 任务结束时间
     */
    private String taskEndTime;
    /**
     * 工单id
     */
    private String orderId;
    /**
     * 任务状态 1-待执行 2-执行中 3-已执行
     */
    private String taskStatus;
    /**
     * 执行结果 1-执行成功 2-执行失败
     */
    private String taskResult;
    /**
     * 失败原因
     */
    private String content;
    /**
     * 创建者
     */
    private String creator;
    /**
     * 创建时间
     */
    private String createTime;
    /**
     * 更新者
     */
    private String updater;
    /**
     * 更新时间
     */
    private String updateTime;
    /**
     * 更新时间
     */
    private int messageCount;

}
