package com.aspire.fileCheck.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.aspire.fileCheck.entity.FileIndicationPeriodStateOtherFileEntity;
import com.aspire.fileCheck.entity.FileStateOtherReportEntity;

@Mapper
public interface IFileIndicationPeriodStateOtherDAO {

    int batchInsertOtherFile(List<FileIndicationPeriodStateOtherFileEntity> list);
    
    List<FileStateOtherReportEntity> getOtherFileReport(@Param("catolog") String catolog, @Param("provinceCode") String provinceCode,
                                                        @Param("period") String period, @Param("thisDate") String thisDate, @Param("lastDate") String lastDate,
                                                        @Param("fileIndication") String fileIndication);

    List<FileIndicationPeriodStateOtherFileEntity> getDataByDate(String uploadDate);

    List<Map<String, String>> getOtherFileData(@Param("uploadDate") String uploadDate, @Param("type") String type);
}
