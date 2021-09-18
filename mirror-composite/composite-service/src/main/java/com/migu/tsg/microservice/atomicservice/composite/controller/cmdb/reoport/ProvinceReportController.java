package com.migu.tsg.microservice.atomicservice.composite.controller.cmdb.reoport;

import com.aspire.mirror.composite.service.cmdb.report.IProvinceReportAPI;
import com.aspire.ums.cmdb.report.playload.Cmdb31ProvinceReport;
import com.aspire.ums.cmdb.report.playload.Cmdb31ProvinceReportReq;
import com.aspire.ums.cmdb.report.playload.Cmdb31ProvinceReportSetting;
import com.aspire.ums.cmdb.report.playload.Cmdb31ProvinceTable;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb.report.IProvinceReportClient;
import com.migu.tsg.microservice.atomicservice.composite.controller.authcontext.RequestAuthContext;
import com.migu.tsg.microservice.atomicservice.composite.controller.util.ExportExcelUtil;
import com.migu.tsg.microservice.atomicservice.rbac.service.DepartmentService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.*;

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
public class ProvinceReportController implements IProvinceReportAPI {

    @Autowired
    private IProvinceReportClient reportClient;
    @Autowired
    private DepartmentService departmentService;

    @Override
    public Map<String, Object> getSetting(@RequestBody Map<String, String> queryParams) {
        return reportClient.getSetting(queryParams);
    }

    @Override
    public Map<String, String> save( @RequestBody Map<String,Object> insertDataBox) {
        RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
        String userName = authCtx.getUser().getUsername();
        return reportClient.save(userName, insertDataBox);
    }

    @Override
    public Map<String, List<Map<String, Object>>> getSettingData(@RequestBody Cmdb31ProvinceReportReq req) {
        return reportClient.getSettingData(req);
    }

    @Override
    public Map<String, String> exportReport(@RequestBody Map<String, String> queryParams) {

        //列头
//        departmentService.set
//        //数据
//        Map<String, List<Map<String, Object>>> resultReport = reportClient.getSettingData(queryParams);
////        String month = queryParams.get("submitMonth");
////        String provinceName = queryParams.get("provinceName");
//        String resourceOwner =  queryParams.get("resourceOwner");
//        if ("yunwei".equalsIgnoreCase(resourceOwner)) {
//            String fileName = "31省份上报数据.xlsx";
//        }
//        String fileName = moduleName + ".xlsx";
//        final List<String> header = new LinkedList<>();
//        final List<String> keys = new LinkedList<>();
//        if (headerList != null && headerList.size() > 0) {
//            headerList.stream().forEach((code) -> {
//                header.add(code.getFiledName());
//                keys.add(code.getFiledCode());
//            });
//            ExportExcelUtil eeu = new ExportExcelUtil();
//            Workbook book = new SXSSFWorkbook(128);
//            eeu.exportExcel(book, 0, moduleName, header.toArray(new String[header.size()]),
//                    dataList, keys.toArray(new String[keys.size()]));
//            ByteArrayOutputStream ops = null;
//            ByteArrayInputStream in = null;
//            try {
//                ops = new ByteArrayOutputStream();
//                book.write(ops);
//                byte[] b = ops.toByteArray();
//                in = new ByteArrayInputStream(b);
//                returnMap = ftpService.uploadtoFTP(fileName, in);
//                ops.flush();
//            } catch (Exception e) {
//                log.error("导出excel失败，失败原因：", e);
//                returnMap.put("code","error");
//                returnMap.put("message", e.getMessage());
//            }finally {
//                IOUtils.closeQuietly(book);
//                IOUtils.closeQuietly(ops);
//                IOUtils.closeQuietly(in);
//                return returnMap;
//            }
//        }
//    } catch (Exception e) {
//        log.error("导出Excel数据失败!", e);
//        returnMap.put("code", "error");
//        returnMap.put("message", e.getMessage());
//    }
        return null;
    }

    @Override
    public Map<String, String> exportReportTemplate(@RequestBody Map<String, String> queryParams, HttpServletResponse response) {
        Map<String, String> returnMap = new HashMap<>();
        if (!queryParams.containsKey("resourceOwner") || !StringUtils.isNotEmpty(queryParams.get("resourceOwner"))) {
            throw new RuntimeException("Query params.resourceOwner is require.");
        }
        if (!queryParams.containsKey("resourceClass") || !StringUtils.isNotEmpty(queryParams.get("resourceClass"))) {
            throw new RuntimeException("Query params.resourceClass is require.");
        }
        try {
            String resourceClass = queryParams.get("resourceClass");
            Cmdb31ProvinceTable table = reportClient.getTableByName(resourceClass, "update");
            List<Cmdb31ProvinceReportSetting> settings = table.getSettingList();
            List<String> headerSource = new ArrayList<>();
            for (Cmdb31ProvinceReportSetting setting : settings) {
                if ("省份名称".equals(setting.getResourceType())) {
                    continue;
                }
                headerSource.add(setting.getResourceType());
            }
            String fileName = table.getName();
            String[] headers = headerSource.toArray(new String[headerSource.size()]);
            response.setHeader("Content-Disposition", "attachment;filename=".concat(String.valueOf(URLEncoder.encode(fileName + ".xlsx", "UTF-8"))));
            response.setHeader("Connection", "close");
            response.setHeader("Content-Type", "application/vnd.ms-excel");
            OutputStream os = response.getOutputStream();// 取得输出流
            ExportExcelUtil report = new ExportExcelUtil();
            SXSSFWorkbook book = new SXSSFWorkbook(128);
            int sheetIndex = 0;
            report.exportExcel(book, sheetIndex,fileName, headers, new ArrayList<>(), headers);
            book.write(os);
            os.flush();
            os.close();
            returnMap.put("code", "success");
            response.setStatus(HttpStatus.NO_CONTENT.value());
        }catch (Exception e) {
            log.error("导出模型 {}", e.getMessage());
        }
        return returnMap;
    }

    @Override
    public Map<String, String> submitCheck(@RequestBody Map<String, String> queryParams) {
        return reportClient.submitCheck(queryParams);
    }

    @Override
    public Map<String, String> submitApprove(@RequestBody Map<String, String> queryParams) {
        return reportClient.submitApprove(queryParams);
    }

    @Override
    public List<Map<String, String>> getProvinceStatus(@RequestBody Map<String, String> queryParam) {
        return reportClient.getProvinceStatus(queryParam);
    }
}
