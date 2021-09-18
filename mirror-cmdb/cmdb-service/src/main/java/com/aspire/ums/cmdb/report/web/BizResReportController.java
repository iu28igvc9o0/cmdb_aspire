package com.aspire.ums.cmdb.report.web;

import com.aspire.ums.cmdb.allocate.payload.Result;
import com.aspire.ums.cmdb.report.IBizResReportAPI;
import com.aspire.ums.cmdb.report.playload.BizResReport;
import com.aspire.ums.cmdb.report.service.IBizResReportService;
import com.aspire.ums.cmdb.util.StringUtils;
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
 * 创建时间: 2019/6/18 10:47
 * 版本: v1.0
 */
@RefreshScope
@RestController
@Slf4j
public class BizResReportController implements IBizResReportAPI {
    @Autowired
    private IBizResReportService reportService;
    
    @Override
    public Result<BizResReport> getAllReportBizResData(@RequestParam(value = "pageNum",required = false) int pageNum,
                                                       @RequestParam(value = "pageSize",required = false) int pageSize,
                                                       @RequestParam(value = "bizSystem",required = false) String bizSystem,
                                                       @RequestParam(value = "idcType",required = false) String idcType,
                                                       @RequestParam(value = "department1",required = false) String department1,
                                                       @RequestParam(value = "department2",required = false) String department2,
                                                       @RequestParam(value = "deviceType", required = false) String deviceType,
                                                       @RequestParam(value = "createTime1", required = false) String createTime1,
                                                       @RequestParam(value = "createTime2", required = false) String createTime2) {
        return reportService.getAllReportBizResData(pageNum, pageSize, bizSystem, idcType, department1, department2, deviceType, createTime1, createTime2);
    }
    
    @Override
    public List<Map<String, Object>> exportBizRes(@RequestParam(value = "bizSystem",required = false) String bizSystem,
                                                  @RequestParam(value = "idcType",required = false) String idcType,
                                                  @RequestParam(value = "department1",required = false) String department1,
                                                  @RequestParam(value = "department2",required = false) String department2,
                                                  @RequestParam(value = "deviceType", required = false) String deviceType,
                                                  @RequestParam(value = "createTime1", required = false) String createTime1,
                                                  @RequestParam(value = "createTime2", required = false) String createTime2) {
        return reportService.getBizResExportData(bizSystem,idcType,department1,department2,deviceType,createTime1,createTime2);
    }
}
