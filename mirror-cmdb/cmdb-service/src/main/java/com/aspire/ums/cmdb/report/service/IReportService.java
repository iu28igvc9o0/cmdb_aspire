package com.aspire.ums.cmdb.report.service;

import com.aspire.ums.cmdb.allocate.payload.Result;
import com.aspire.ums.cmdb.report.playload.BizResReport;
import com.aspire.ums.cmdb.report.playload.Report;

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
public interface IReportService {

    List<Report> listReportByBizSystem(String bizSystem,String idcType,String department1,String department2);
    
    List<Map<String,Object>> exportReportByBizSystem(String bizSystem, String idcType,String department1,String department2);

    List<Report> listReportByDepartment(String idcType,String department1,String department2);

    List<Map<String,Object>> exportReportByDepartment(String idcType,String department1,String department2);
}
