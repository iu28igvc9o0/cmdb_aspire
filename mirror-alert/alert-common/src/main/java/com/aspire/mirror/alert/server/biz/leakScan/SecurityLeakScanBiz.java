package com.aspire.mirror.alert.server.biz.leakScan;

import com.aspire.mirror.alert.server.dao.leakScan.po.SecurityLeakScanReportFile;
import com.aspire.mirror.alert.server.vo.leakScan.LeakScanSummaryVo;
import com.aspire.mirror.alert.server.vo.leakScan.SecurityLeakScanRecordVo;
import com.aspire.mirror.alert.server.vo.leakScan.SecurityLeakScanReportVo;
import com.aspire.mirror.common.entity.PageRequest;
import com.aspire.mirror.common.entity.PageResponse;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

public interface SecurityLeakScanBiz {

    String persistScanRecords(File localFile, String dateStr, String ftpFilePath, String fileName) throws IOException, ParseException;
    void fillScanRecordBpmId(String scanId, String bpmId);
    void fillScanRecordBpmFileId(String scanId, String bpmFileId);
    void fillScanReportHtmlPath(String scanId, String dateStr, String bizLine);
    void setBpmReapirStat(String bpmId, int stat);
    List<SecurityLeakScanReportFile> getFileByFtpPath(String ftpFilePath);
    SecurityLeakScanRecordVo getSecurityLeakScanRecordById(String id);
    List<SecurityLeakScanRecordVo> getSecurityLeakScanRecordByDateAndFileName(String dateStr, String fileName) throws ParseException;
    PageResponse<LeakScanSummaryVo> summaryList(PageRequest pageRequest);
    List<LeakScanSummaryVo> exportList(PageRequest pageRequest);
    PageResponse<SecurityLeakScanReportVo> reportList(PageRequest pageRequest);
    List<SecurityLeakScanReportVo> getReportListByScanId(String scanId);
    void clearPastScanRecords(String bizLine, String dateStr) throws ParseException;
    
	PageResponse<LeakScanSummaryVo> getLeakScanDetailByDate(PageRequest pageRequest);
	Map<String, Object> leaksRankDistribute(String beginDate, String endDate);
	PageResponse<Map<String, Object>> leakStatByBiz(String beginDate, String endDate, Integer begin,Integer pageSize);
	PageResponse<Map<String, Object>> leakStatListByBiz(String beginDate, String endDate, String rankType, Integer begin,Integer pageSize);
	PageResponse<Map<String, Object>> leakTrend(String beginDate, String endDate,Integer begin,Integer pageSize);
	Map<String, Object> leakSummary(String beginDate, String endDate);
    List<Map<String, Object>> getLeakByBizSystem();
    List<Map<String, Object>> getLeakByIp();
    List<Map<String,Object>> getLeakByIdcType();
}
