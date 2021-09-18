package com.aspire.fileCheck.service;

import com.aspire.fileCheck.entity.FileIndicationJobManagerEntity;

import java.util.List;

public interface IFileIndicationJobManagerService {

    /**
     * 根据JOB名称获取JOB信息
     * @param jobName
     * @return
     */
    List<FileIndicationJobManagerEntity> getJobManagerByJobName(String jobName);

    /**
     * 获取指定JOB的最后一次信息
     * @param jobName
     * @return
     */
    FileIndicationJobManagerEntity getLatestJobManagerByJobName(String jobName);

    /**
     * 新增JOB运行信息
     * @param entity
     */
    void insertEntity(FileIndicationJobManagerEntity entity);

    /**
     * 修改JOB运行信息
     * @param entity
     */
    void updateEntity(FileIndicationJobManagerEntity entity);
}
