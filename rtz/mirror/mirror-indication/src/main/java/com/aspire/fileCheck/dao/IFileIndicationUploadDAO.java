package com.aspire.fileCheck.dao;

import com.aspire.fileCheck.entity.FileIndicationUploadEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface IFileIndicationUploadDAO {

    List<Map<String,Object>> getFileUploadDelayDetectionData();

    Map<String,String> checkFileExist(@Param("checkFileExistData") Map<String,Object> checkFileExistData);

    void insetFileIndicationUpload(@Param("insertData") Map<String,Object> insertData);

    Map<String,String> checkFileIndicationPeriodState(@Param("checkData") Map<String,Object> checkData);

    void updateFileIndicationPeriodState(@Param("updateData") Map<String,Object> updateData);

    void insertFileIndicationPeriodState(@Param("insertData") Map<String,Object> insertData);

    List<Map<String,String>> getFileUploadDelayDetectionData1(Map<String,Object> param);

    /**
     * 批量创建
     * @param entityList
     */
    void batchInsert(@Param("entityList") List<FileIndicationUploadEntity> entityList);

    /**
     *
     * @param configId
     * @return
     */
    List<FileIndicationUploadEntity> getUploadFilesByPeriodConfigId(@Param("configId") Integer configId);

    /**
     * 删除上传记录
     * @param entity
     */
    void deleteEntity(@Param("entity") FileIndicationUploadEntity entity);
}
