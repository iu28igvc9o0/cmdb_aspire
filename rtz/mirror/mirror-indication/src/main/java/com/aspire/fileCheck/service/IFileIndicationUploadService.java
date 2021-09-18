package com.aspire.fileCheck.service;

import com.aspire.fileCheck.entity.FileIndicationUploadEntity;

import java.util.List;
import java.util.Map;

public interface IFileIndicationUploadService {

    /**
     * 文件上传延迟检查
     */
    void check();

    /**
     * 获取文件上传完整度列表
     * @param param
     * @return
     */
    List<Map<String, String>> getFileUploadDelayDetectionData(Map<String,Object> param);

    /**
     * 批量创建
     * @param entityList
     */
    void batchInsert(List<FileIndicationUploadEntity> entityList);

    /**
     * 获取指定配置ID对应的延迟文件列表
     * @param configId
     * @return
     */
    List<FileIndicationUploadEntity> getUploadFilesByPeriodConfigId(Integer configId);

    /**
     * 删除上传记录
     * @param entity
     */
    void deleteEntity(FileIndicationUploadEntity entity);
}
