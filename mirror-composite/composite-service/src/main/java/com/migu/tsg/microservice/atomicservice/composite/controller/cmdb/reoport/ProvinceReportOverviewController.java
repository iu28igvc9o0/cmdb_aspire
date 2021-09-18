package com.migu.tsg.microservice.atomicservice.composite.controller.cmdb.reoport;

import com.alibaba.fastjson.JSONObject;
import com.aspire.mirror.composite.service.cmdb.report.IProvinceReportOverviewAPI;
import com.aspire.ums.cmdb.report.playload.Cmdb31ProvinceTable;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb.report.IProvinceReportOverviewClient;
import com.migu.tsg.microservice.atomicservice.composite.controller.util.ExportExcelUtil;
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
import java.util.List;
import java.util.Map;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: ProvinceReportOverviewController
 * Author:   hangfang
 * Date:     2020/5/8
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
    IProvinceReportOverviewClient overviewClient;

    @Override
    public Map<String, Object> getReportOverview(@RequestParam("tableId") String tableId,
                                                 @RequestParam("month") String month,
                                                 @RequestParam("type") String type) {
        return overviewClient.getReportOverview(tableId, month, type);
    }

    @Override
    public List<Cmdb31ProvinceTable> listByOwnerAndPage(@RequestParam(value = "resourceOwner", required = false) String resourceOwner,
                                                        @RequestParam("type") String type) {
        return overviewClient.listByOwnerAndPage(resourceOwner, type);
    }

    @Override
    public void exportReportOverview(@RequestParam("tableId") String tableId,
                                     @RequestParam("month") String month,
                                     @RequestParam("type") String type,
                                     HttpServletResponse response) {
        try {
            Map<String, Object>  resultData = overviewClient.getReportOverview(tableId,month,type);
            if (resultData == null) {
                throw new RuntimeException("根据表id["+ tableId +"]未获取到任何相关数据");
            }
            String title = resultData.get("tableName").toString();
            String fileName = title+".xlsx";
            response.setHeader("Content-Disposition", "attachment;filename=".concat(String.valueOf(URLEncoder.encode
                    (fileName, "UTF-8"))));
            List<Map<String, Object>> dataList = (List<Map<String, Object>>)resultData.get("tableData");
            List<String> headers = (List<String>)resultData.get("headers");
            String[] headerList = headers.toArray(new String[headers.size()]);
            response.setHeader("Connection", "close");
            response.setHeader("Content-Type", "application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            OutputStream os = response.getOutputStream();// 取得输出流
            ExportExcelUtil eeu = new ExportExcelUtil();
            Workbook book = new SXSSFWorkbook(128);
            eeu.exportExcel(book, 0, fileName, headerList, dataList, headerList);
            book.write(os);
            os.flush();
            os.close();
        } catch (Exception e) {
            log.error("导出Excel数据失败!", e);
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }

    @Override
    public JSONObject exportAllReport(@RequestParam("month") String month, HttpServletResponse response) {
        JSONObject rs = new JSONObject();
        try {
            String fileName = "allReport.xlsx";
            response.setHeader("Content-Disposition", "attachment;filename=".concat(String.valueOf(URLEncoder.encode
                    (fileName, "UTF-8"))));
            response.setHeader("Connection", "close");
            response.setHeader("Content-Type", "application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            ExportExcelUtil eeu = new ExportExcelUtil();
            Workbook book = new SXSSFWorkbook(128);
            int sheetIndex = 0,haveDataSheet = 0;
            // 获取需要导出的报表
            List<Cmdb31ProvinceTable> excelList = overviewClient.listByOwnerAndPage(null,"export");
            for(Cmdb31ProvinceTable item : excelList) {
                Map<String, Object>  resultData = overviewClient.getReportOverview(item.getId(),month,"export");
                String title = resultData.get("tableName").toString();
                List<Map<String, Object>> dataList = (List<Map<String, Object>>)resultData.get("tableData");
                if(dataList.isEmpty()) {
                    haveDataSheet++;
                    continue;
                }
                List<String> headers = (List<String>)resultData.get("headers");
                String[] headerList = headers.toArray(new String[headers.size()]);
                eeu.exportExcel(book, sheetIndex++, title, headerList, dataList, headerList);
            }
            if(haveDataSheet == excelList.size()){
                throw new RuntimeException(month + "月份没有报表数据");
            } else {
                // 取得输出流
                OutputStream os = response.getOutputStream();
                book.write(os);
                os.flush();
                os.close();
            }
        } catch (Exception e) {
            log.error("导出Excel数据失败!", e);
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            rs.put("msg",month + "月份没有报表数据");
        }
        return rs;
    }
}
