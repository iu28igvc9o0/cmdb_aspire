package com.aspire.ums.cmdb.report.web;

import com.aspire.ums.cmdb.allocate.payload.Result;
import com.aspire.ums.cmdb.collectUnknown.payload.CmdbCollectUnknown;
import com.aspire.ums.cmdb.report.IProvinceReportOverviewAPI;
import com.aspire.ums.cmdb.report.playload.Cmdb31ProvinceReport;
import com.aspire.ums.cmdb.report.service.ICmdb31ProvinceReportOverviewService;
import com.aspire.ums.cmdb.report.service.ICmdb31ProvinceReportService;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: ProvinceReportOverviewController
 * Author:   hangfang
 * Date:     2020/5/7
 * Description: DESCRIPTION
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@RefreshScope
@RestController
@Slf4j
public class ProvinceReportOverviewController implements IProvinceReportOverviewAPI {
    @Autowired
    private ICmdb31ProvinceReportOverviewService overviewService;
    @Override
    public Map<String, Object> getReportOverview(@RequestParam("tableId") String tableId,
                                                       @RequestParam("month") String month,
                                                       @RequestParam("type") String type) {
        return overviewService.getReportOverview(tableId,month,type);
    }
}
