package com.aspire.fileCheck.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.aspire.fileCheck.entity.FileIndicationEntity;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface IFileIndicationDAO {

    /**
     * 查询文件指标列表
     * @param catalog 分类
     * @return
     */
    List<FileIndicationEntity> getFileIndication(@Param("catalog") String catalog);

    /**
     * 根据文件指标ID 获取指标信息
     * @param indicationId 文件指标ID
     * @return
     */
    FileIndicationEntity getFileIndicationByIndicationId(@Param("indicationId") Integer indicationId);
    
    /**
     * 根据文件指标ID 获取指标信息
     * @param indicationId 文件指标ID
     * @return
     */
    List<FileIndicationEntity> getFileIndicationByType(@Param("type") String type);
}
