package com.aspire.mirror.log.server.dao;

import com.aspire.mirror.log.server.dao.po.ConfigFileUploadLogDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ConfigFileDao {

    void insertConfigFileUploadLog(List<ConfigFileUploadLogDTO> request);
}
