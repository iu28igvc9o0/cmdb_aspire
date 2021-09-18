package com.aspire.ums.cmdb.report.mapper;

import com.aspire.ums.cmdb.report.playload.BizResReport;
import com.aspire.ums.cmdb.report.playload.BizResReportReq;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 项目名称:
 * 包: com.aspire.ums.cmdb.report.mapper
 * 类名称:
 * 类描述:
 * 创建人: PJX
 * 创建时间: 2019/6/18 10:14
 * 版本: v1.0
 */
public interface BizResourceMapper {
    
    List<BizResReport> getAllReportData_total(BizResReportReq request);
    
    Integer getAllReportCount_total(BizResReportReq request);
    
    List<Map<String,Object>> getExportData_total(@Param("bizSystem") String bizSystem,
                                                 @Param("idcType") String idcType,
                                                 @Param("department1") String department1,
                                                 @Param("department2") String department2,
                                                 @Param("deviceType") String deviceType,
                                                 @Param("createTime1") String createTime1,
                                                 @Param("createTime2") String createTime2);
    
    List<BizResReport> getAllReportData(BizResReportReq request);
    
    Integer getAllReportCount(BizResReportReq request);
    
    List<Map<String,Object>> getExportData(@Param("bizSystem") String bizSystem,
                                           @Param("idcType") String idcType,
                                           @Param("department1") String department1,
                                           @Param("department2") String department2,
                                           @Param("deviceType") String deviceType,
                                           @Param("createTime1") String createTime1,
                                           @Param("createTime2") String createTime2);
    
    void insert(BizResReport bizResReport);
}
