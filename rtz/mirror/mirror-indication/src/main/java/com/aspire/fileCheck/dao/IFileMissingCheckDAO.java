package com.aspire.fileCheck.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface IFileMissingCheckDAO {
    List<Map<String,String>> getFileMissingCheckData(Map<String,Object> param);

    List<Map<String, String>> getMissingList(@Param("uploadDate") String uploadDate, @Param("type") String type);

    Integer getFileMissingCheckDataCount(Map<String, Object> param);

    List<Map<String,String>> getFileCheckResultForMail(Map<String,Object> param);
}
