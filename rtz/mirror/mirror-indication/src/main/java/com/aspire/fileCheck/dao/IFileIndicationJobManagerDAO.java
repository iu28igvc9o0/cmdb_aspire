package com.aspire.fileCheck.dao;

import com.aspire.fileCheck.entity.FileIndicationJobManagerEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface IFileIndicationJobManagerDAO {
    /**
     * 根据JOB名称获取JOB信息
     * @param jobName
     * @return
     */
    List<FileIndicationJobManagerEntity> getJobManagerByJobName(@Param("jobName") String jobName);

    /**
     * 获取指定JOB的最后一次信息
     * @param jobName
     * @return
     */
    FileIndicationJobManagerEntity getLatestJobManagerByJobName(@Param("jobName") String jobName);

    /**
     * 新增JOB运行信息
     * @param entity
     */
    void insertEntity(@Param("entity") FileIndicationJobManagerEntity entity);

    /**
     * 修改JOB运行信息
     * @param entity
     */
    void updateEntity(@Param("entity") FileIndicationJobManagerEntity entity);
}
