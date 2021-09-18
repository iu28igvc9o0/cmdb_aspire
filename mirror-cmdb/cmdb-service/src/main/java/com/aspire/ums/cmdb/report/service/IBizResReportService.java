package com.aspire.ums.cmdb.report.service;

import com.aspire.ums.cmdb.allocate.payload.Result;
import com.aspire.ums.cmdb.report.playload.BizResReport;

import java.util.List;
import java.util.Map;

/**
 * 项目名称:
 * 包: com.aspire.ums.cmdb.report.service
 * 类名称:
 * 类描述:
 * 创建人: PJX
 * 创建时间: 2019/6/14 15:48
 * 版本: v1.0
 */
public interface IBizResReportService {
    
    Result<BizResReport> getAllReportBizResData(int pageNum, int pageSize, String bizSystem, String idcType, String department1, String department2,String deviceType,String createTime1,String createTime2);
    
    List<Map<String,Object>> getBizResExportData(String bizSystem, String idcType, String department1, String department2,String deviceType,String createTime1,String createTime2);
    
    void insert(BizResReport bizResReport);
}
