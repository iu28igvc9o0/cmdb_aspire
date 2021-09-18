package com.aspire.fileCheck.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileIndicationJobManagerEntity {
    /**
     * JOB ID
     */
    private Integer jobId;
    /**
     * JOB名称
     */
    private String jobName;
    /**
     * JOB运行类
     */
    private String jobClass;
    /**
     * JOB运行状态  运行/完成
     */
    private String jobStatus;
    /**
     * JOB运行的时间段所属的日期
     */
    private String runningDate;
    /**
     * JOB运行的时间段
     */
    private String runningPeriod;
    /**
     * 是否是最后一次运行 0: 否  1:是
     */
    private Integer latestRunning;
    /**
     * 记录生成时间
     */
    private Date createTime;
    /**
     * 记录修改时间
     */
    private Date modifyTime;
}
