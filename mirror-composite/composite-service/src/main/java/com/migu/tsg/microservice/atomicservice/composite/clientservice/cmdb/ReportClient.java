package com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb;

import java.util.List;
import java.util.Map;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.aspire.ums.cmdb.report.playload.Report;

/**
 * 项目名称:
 * 包: com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb
 * 类名称:
 * 类描述:
 * 创建人: PJX
 * 创建时间: 2019/6/14 16:46
 * 版本: v1.0
 */
@FeignClient(value = "CMDB")
public interface ReportClient {
    
    @GetMapping("/cmdb/report/listReportByBizSystem")
    List<Report> listReportByBizSystem(@RequestParam(value = "bizSystem",required = false) String bizSystem,
                                    @RequestParam(value = "idcType",required = false) String idcType,
                                    @RequestParam(value = "department1",required = false) String department1,
                                    @RequestParam(value = "department2",required = false) String department2);
    
    @GetMapping("/cmdb/report/exportReportByBizSystem")
    List<Map<String, Object>> exportReportByBizSystem(@RequestParam(value = "bizSystem",required = false) String bizSystem,
                                     @RequestParam(value = "idcType",required = false) String idcType,
                                     @RequestParam(value = "department1",required = false) String department1,
                                     @RequestParam(value = "department2",required = false) String department2);
    @GetMapping("/cmdb/report/listReportByDepartment")
    List<Report> listReportByDepartment(@RequestParam(value = "idcType",required = false) String idcType,
                                  @RequestParam(value = "department1",required = false) String department1,
                                  @RequestParam(value = "department2",required = false) String department2);

    @GetMapping("/cmdb/report/exportReportByDepartment")
    List<Map<String, Object>> exportReportByDepartment(@RequestParam(value = "idcType",required = false) String idcType,
                                                      @RequestParam(value = "department1",required = false) String department1,
                                                      @RequestParam(value = "department2",required = false) String department2);
}
