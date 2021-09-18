package com.aspire.mirror.composite.payload.taskChange;

import lombok.Data;

@Data
public class CompTaskQueryRequest {

    /**
     * 开始时间
     */
    private String startDateTime;
    /**
     * 结束时间
     */
    private String endDateTime;
    /**
     * 视图类型 1-月视图 2-周试图 3-列表视图
     */
    private String viewType;
    /**
     * 任务状态 1-待执行 2-执行中 3-已执行
     */
    private String taskStatus;
    /**
     * 任务结果 1-执行成功 2-执行失败
     */
    private String taskResult;
    /**
     * 反馈信息 1-有反馈 2-无反馈
     */
    private String taskMassage;

    // 分页
    private Integer pageNum;
    private Integer pageSize;


}
