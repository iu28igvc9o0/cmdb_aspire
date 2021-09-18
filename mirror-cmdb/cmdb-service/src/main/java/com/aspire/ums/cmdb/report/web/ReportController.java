package com.aspire.ums.cmdb.report.web;

import com.aspire.ums.cmdb.report.IReportAPI;
import com.aspire.ums.cmdb.report.playload.Report;
import com.aspire.ums.cmdb.report.service.IReportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 项目名称:
 * 包: com.aspire.ums.cmdb.report.web
 * 类名称:
 * 类描述:
 * 创建人: PJX
 * 创建时间: 2019/6/14 15:40
 * 版本: v1.0
 */
@RefreshScope
@RestController
@Slf4j
public class ReportController implements IReportAPI {
    
    @Autowired
    private IReportService reportService;
    
    @Override
    public List<Report> listReportByBizSystem(@RequestParam(value = "bizSystem",required = false) String bizSystem,
                                          @RequestParam(value = "idcType",required = false) String idcType,
                                          @RequestParam(value = "department1",required = false) String department1,
                                          @RequestParam(value = "department2",required = false) String department2) {
        return reportService.listReportByBizSystem(bizSystem,idcType,department1,department2);
    }
    
    @Override
    public List<Map<String, Object>> exportReportByBizSystem(@RequestParam(value = "bizSystem",required = false) String bizSystem,
                                            @RequestParam(value = "idcType",required = false) String idcType,
                                            @RequestParam(value = "department1",required = false) String department1,
                                            @RequestParam(value = "department2",required = false) String department2) {
        return reportService.exportReportByBizSystem(bizSystem,idcType,department1,department2);
    }

    @Override
    public List<Report> listReportByDepartment(@RequestParam(value = "idcType",required = false) String idcType,
                                               @RequestParam(value = "department1",required = false) String department1,
                                               @RequestParam(value = "department2",required = false) String department2) {
        return reportService.listReportByDepartment(idcType, department1, department2);
    }

    @Override
    public List<Map<String, Object>> exportReportByDepartment(@RequestParam(value = "idcType",required = false) String idcType,
                                                              @RequestParam(value = "department1",required = false) String department1,
                                                              @RequestParam(value = "department2",required = false) String department2) {
        return reportService.exportReportByDepartment(idcType, department1, department2);
    }
}
