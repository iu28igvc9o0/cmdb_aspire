package com.migu.tsg.microservice.atomicservice.composite.controller.cmdb;

import com.aspire.mirror.composite.service.cmdb.IReportService;
import com.aspire.ums.cmdb.report.playload.Report;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb.ReportClient;
import com.migu.tsg.microservice.atomicservice.composite.controller.util.ExportExcelUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

/**
 * 项目名称:
 * 包: com.migu.tsg.microservice.atomicservice.composite.controller.cmdb
 * 类名称:
 * 类描述:
 * 创建人: PJX
 * 创建时间: 2019/6/14 16:48
 * 版本: v1.0
 */
@RestController
@Slf4j
public class ReportController implements IReportService {
    
    @Autowired
    private ReportClient reportClient;
    
    @Override
    public List<Report> listReportByBizSystem(@RequestParam(value = "bizSystem",required = false) String bizSystem,
                                           @RequestParam(value = "idcType",required = false) String idcType,
                                           @RequestParam(value = "department1",required = false) String department1,
                                           @RequestParam(value = "department2",required = false) String department2) {
        return reportClient.listReportByBizSystem(bizSystem,idcType,department1,department2);
    }
    
    @Override
    public void exportReportByBizSystem(@RequestParam(value = "bizSystem",required = false) String bizSystem,
                       @RequestParam(value = "idcType",required = false) String idcType,
                       @RequestParam(value = "department1",required = false) String department1,
                       @RequestParam(value = "department2",required = false) String department2) {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletResponse response = servletRequestAttributes.getResponse();
        try {
            List<Map<String, Object>> reportData = reportClient.exportReportByBizSystem(bizSystem,idcType,department1,department2);
            log.info("[cdmb Report Data] >>> {}", reportData );
            String[] headerList = {"业务系统名称","联系人","所属租户（一级）","归属部门（二级）","资源池名称","POD名称","设备数量","服务器数量","虚拟机数量","存储数量","网络数量"};
            String[] keyList = {"bizSystem","bizSystemConcat","department1","department2","idcType","pod_name","totalNum","serverNum","vmNum","storageNum","networkNum"};
            String title = "业务系统分析报表";
            String fileName = title+".xlsx";
            OutputStream os = response.getOutputStream();// 取得输出流
            response.setHeader("Content-Disposition", "attachment;filename=".concat(String.valueOf( URLEncoder.encode(fileName, "UTF-8"))));
            response.setHeader("Connection", "close");
            response.setHeader("Content-Type", "application/vnd.ms-excel");
            //excel constuct
            ExportExcelUtil eeu = new ExportExcelUtil();
            Workbook book = new SXSSFWorkbook(128);
            eeu.exportExcel(book, 0, title, headerList, reportData, keyList);
            book.write(os);
        } catch (Exception e) {
            log.error("[export reportData is error] >>> " + e);
        }
    }

    @Override
    public List<Report> listReportByDepartment(@RequestParam(value = "idcType",required = false) String idcType,
                                               @RequestParam(value = "department1",required = false) String department1,
                                               @RequestParam(value = "department2",required = false) String department2) {
        return reportClient.listReportByDepartment(idcType, department1, department2);
    }

    @Override
    public void exportReportByDepartment(@RequestParam(value = "idcType",required = false) String idcType,
                                                              @RequestParam(value = "department1",required = false) String department1,
                                                              @RequestParam(value = "department2",required = false) String department2) {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletResponse response = servletRequestAttributes.getResponse();
        try {
            List<Map<String, Object>> reportData = reportClient.exportReportByDepartment(idcType,department1,department2);
            log.info("[cdmb Report Data] >>> {}", reportData );
            String[] headerList = {"所属租户（一级）","归属部门（二级）","资源池名称","POD名称","设备数量","服务器数量","虚拟机数量","存储数量","网络数量"};
            String[] keyList = {"department1","department2","idcType","pod_name","totalNum","serverNum","vmNum","storageNum","networkNum"};
            String title = "归属部门分析报表";
            String fileName = title+".xlsx";
            OutputStream os = response.getOutputStream();// 取得输出流
            response.setHeader("Content-Disposition", "attachment;filename=".concat(String.valueOf( URLEncoder.encode(fileName, "UTF-8"))));
            response.setHeader("Connection", "close");
            response.setHeader("Content-Type", "application/vnd.ms-excel");
            //excel constuct
            ExportExcelUtil eeu = new ExportExcelUtil();
            Workbook book = new SXSSFWorkbook(128);
            eeu.exportExcel(book, 0, title, headerList, reportData, keyList);
            book.write(os);
        } catch (Exception e) {
            log.error("[export reportData is error] >>> " + e);
        }
    }
}
