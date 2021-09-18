package com.aspire.mirror.alert.server.dao.leakScan;

import com.aspire.mirror.alert.server.dao.leakScan.po.SecurityLeakScanReportFile;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SecurityLeakScanReportFileDao {
    void insert(SecurityLeakScanReportFile securityLeakScanReportFile);
    List<SecurityLeakScanReportFile> getFileByFtpPath(@Param(value = "ftpPath") String ftpFilePath);
}
