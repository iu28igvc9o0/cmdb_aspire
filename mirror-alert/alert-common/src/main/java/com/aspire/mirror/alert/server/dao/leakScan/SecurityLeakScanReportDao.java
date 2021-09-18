package com.aspire.mirror.alert.server.dao.leakScan;

import com.aspire.mirror.alert.server.dao.leakScan.po.SecurityLeakScanReport;
import com.aspire.mirror.common.entity.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

@Mapper
public interface SecurityLeakScanReportDao {
    void insert(SecurityLeakScanReport securityLeakScanReport);
    void batchInsert(@Param("list") List<SecurityLeakScanReport> securityLeakScanReportList);
    void batchUpateReportHtmlPath(@Param("scanId") String scanId, @Param("prefix") String prefix);
    void delete(@Param("bizLine") String bizLine, @Param("scanDate") Date scanDate);
    List<SecurityLeakScanReport> reportList(Page page);
    List<SecurityLeakScanReport> getReportListByScanId(@Param(value = "scanId") String scanId);
    int reportListCount(Page page);
}
