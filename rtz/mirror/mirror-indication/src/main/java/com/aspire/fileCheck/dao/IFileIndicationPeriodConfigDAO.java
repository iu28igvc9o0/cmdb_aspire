package com.aspire.fileCheck.dao;

import java.util.List;

import com.aspire.fileCheck.entity.FileIndicationPeriodConfigEntity;
import com.aspire.fileCheck.entity.FileIndicationPeriodStateEntity;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface IFileIndicationPeriodConfigDAO {

	/**
     * 获取所有的指标项
     * @return
     */
    List<FileIndicationPeriodConfigEntity> getConfigsByIndicationId(@Param("indicationId") Integer indicationId);

    /**
     * 新增指标配置
     * @param configs
     */
    void insertIndicationPeriodConfig(@Param("configs") List<FileIndicationPeriodConfigEntity> configs);

    /**
     * 根据时间段, 获取需要检测的指标配置信息
     * @param calcHour
     * @return
     */
    List<FileIndicationPeriodConfigEntity> getConfigsByPeriod(@Param("period") String calcHour);
    
    List<FileIndicationPeriodConfigEntity> getConfigsByIndicationCatalog(@Param("catalog") String catalog);
    
    /**
     * 設置文件數
     */
    void batchUpdateFileCount(List<FileIndicationPeriodStateEntity> list);
}
