package com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb;

import java.util.List;
import java.util.Map;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.aspire.mirror.composite.service.inspection.payload.Result;
import com.aspire.ums.cmdb.report.playload.BizResReport;

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
public interface BizResReportClient {
    
    @PostMapping("/cmdb/bizResReport/list")
    Result<BizResReport> getAllBizResReportData(@RequestParam(value = "pageNum", required = false) int pageNum,
                                                @RequestParam(value = "pageSize", required = false) int pageSize,
                                                @RequestParam(value = "bizSystem", required = false) String bizSystem,
                                                @RequestParam(value = "idcType", required = false) String idcType,
                                                @RequestParam(value = "department1", required = false) String department1,
                                                @RequestParam(value = "department2", required = false) String department2,
                                                @RequestParam(value = "deviceType", required = false) String deviceType,
                                                @RequestParam(value = "createTime1", required = false) String createTime1,
                                                @RequestParam(value = "createTime2", required = false) String createTime2);
    
    @PostMapping("/cmdb/bizResReport/export")
    List<Map<String, Object>> exportBizRes(@RequestParam(value = "bizSystem", required = false) String bizSystem,
                                           @RequestParam(value = "idcType", required = false) String idcType,
                                           @RequestParam(value = "department1", required = false) String department1,
                                           @RequestParam(value = "department2", required = false) String department2,
                                           @RequestParam(value = "deviceType", required = false) String deviceType,
                                           @RequestParam(value = "createTime1", required = false) String createTime1,
                                           @RequestParam(value = "createTime2", required = false) String createTime2);
}
