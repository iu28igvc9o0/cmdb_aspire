package com.aspire.mirror.alert.server.dao.leakScan;

import com.aspire.mirror.alert.server.dao.leakScan.po.SecurityLeakScanRecord;
import com.aspire.mirror.common.entity.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Mapper
public interface SecurityLeakScanRecordDao {
    void insert(SecurityLeakScanRecord record);
    List<SecurityLeakScanRecord> summaryList(Page page);
    List<SecurityLeakScanRecord> exportList(Page page);
    int summaryListCount(Page page);
    SecurityLeakScanRecord selectById(@Param(value = "id") String id);
    List<SecurityLeakScanRecord> selectByDateAndFileName(@Param(value = "date") Date date, @Param(value = "fileName") String fileName);
    void updateBpmFileId(@Param(value = "scanId") String scanId, @Param(value = "fileId") String bpmFileId);
    void updateBpmId(@Param(value = "scanId") String scanId, @Param(value = "bpmId") String bpmId);
    void updateBpmRepairStat(@Param(value = "bpmId") String bpmFileId, @Param(value = "bpmStat") int bpmStat);
    void delete(@Param("bizLine") String bizLine, @Param("scanDate") Date scanDate);
    
    Map<String,Object> getScanCount(@Param("beginDate") String beginDate, @Param("endDate") String endDate);
    Map<String,Object> getBizLineCount(@Param("beginDate") String beginDate, @Param("endDate") String endDate);
    Map<String,Object> getLeaksAllAndRepairedStatCount(@Param("beginDate") String beginDate, @Param("endDate") String endDate);
    
    
    List<Map<String,Object>> getLeaksStatCountByBizLine(@Param("beginDate") String beginDate, @Param("endDate") String endDate
    		, @Param("begin") Integer begin, @Param("pageSize") Integer pageSize);
    int getLeaksStatCountTotalByBizLine(@Param("beginDate") String beginDate, @Param("endDate") String endDate);
    
    
    Map<String,Object> getleaksStatCount(@Param("beginDate") String beginDate, @Param("endDate") String endDate);
    
    
    List<Map<String,Object>> getCountByleaks(@Param("beginDate") String beginDate, @Param("endDate") String endDate,
    		@Param("leak")String leak, @Param("begin") Integer begin, @Param("pageSize") Integer pageSize);
    int getCountTotalByleaks(@Param("beginDate") String beginDate, @Param("endDate") String endDate);
    
    List<Map<String,Object>> getScanResultByDay(@Param("beginDate") String beginDate, @Param("endDate") String endDate
    		, @Param("begin") Integer begin, @Param("pageSize") Integer pageSize);
    int getScanResultCountByDay(@Param("beginDate") String beginDate, @Param("endDate") String endDate);
    
    List<Map<String,Object>> getLeakByBizSystem();
    List<Map<String,Object>> getLeakByIp();
    List<Map<String,Object>> getLeakByIdcType();
    
    
}
