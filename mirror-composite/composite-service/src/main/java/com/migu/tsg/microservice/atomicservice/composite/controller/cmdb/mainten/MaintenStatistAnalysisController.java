package com.migu.tsg.microservice.atomicservice.composite.controller.cmdb.mainten;

import com.alibaba.fastjson.JSONObject;
import com.aspire.mirror.composite.service.cmdb.mainten.IMaintenStatistAnalysisAPI;
import com.aspire.ums.cmdb.maintenance.payload.MaintenStatistAnalysisRequest;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb.mainten.MaintenStatistAnalysisClient;
import com.migu.tsg.microservice.atomicservice.composite.controller.util.ExportExcelUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
public class MaintenStatistAnalysisController implements IMaintenStatistAnalysisAPI {

    @Autowired
    private MaintenStatistAnalysisClient maintenStatistAnalysisClient;

    @Override
    public List<Map<String, Object>> firstLayer(@RequestBody(required = false) MaintenStatistAnalysisRequest request) {
        return maintenStatistAnalysisClient.firstLayer(request);
    }

    @Override
    public List<Map<String, Object>> secondLayer(@RequestBody(required = false) MaintenStatistAnalysisRequest request) {
        return maintenStatistAnalysisClient.secondLayer(request);
    }

    @Override
    public List<Map<String, Object>> thirdLayer(@RequestBody(required = false) MaintenStatistAnalysisRequest request) {
        return maintenStatistAnalysisClient.thirdLayer(request);
    }

    @Override
    public List<Map<String, Object>> fourthLayer(@RequestBody(required = false) MaintenStatistAnalysisRequest request) {
        return maintenStatistAnalysisClient.fourthLayer(request);
    }

    @Override
    public JSONObject firstExport(@RequestBody MaintenStatistAnalysisRequest request, HttpServletResponse response) {
        JSONObject jsonObject = new JSONObject();
        try {
            List<Map<String, Object>> list = maintenStatistAnalysisClient.firstLayer(request);
            for(Map<String, Object> item : list) {
                if(!item.get("serviceEndTime").toString().contains("-")) {
                    item.put("serviceEndTime",timestampToDate(item.get("serviceEndTime").toString()));
                }
            }
            String fileName = "维保统计分析.xlsx";
            response.setHeader("Content-Disposition", "attachment;filename=".concat(String.valueOf(URLEncoder.encode
                    (fileName, "UTF-8"))));
            response.setHeader("Connection", "close");
            response.setHeader("Content-Type", "application/vnd.ms-excel");
            String[] header = new String[] {"资源池","维保种类","维保项目数量","维保设备数量","总合同金额(万)","最近维保到期时间"};
            String[] keys = new String[] {"resourcePool","maintenanceProjectType","projectNum","deviceNum","totalMoney","serviceEndTime"};
            OutputStream os = response.getOutputStream();// 取得输出流
            ExportExcelUtil eeu = new ExportExcelUtil();
            Workbook book = new SXSSFWorkbook(128);
            eeu.exportExcel(book, 0, "维保统计分析", header, list, keys);
            book.write(os);
            os.flush();
            os.close();
            jsonObject.put("flag", "success");
        } catch (Exception e) {
            log.error("导出Excel数据失败!", e);
            jsonObject.put("flag", "error");
            jsonObject.put("msg", e.getMessage());
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        return jsonObject;
    }

    @Override
    public JSONObject secondExport(@RequestBody(required = false) MaintenStatistAnalysisRequest request, HttpServletResponse response) {
        JSONObject jsonObject = new JSONObject();
        try {
            List<Map<String, Object>> list = maintenStatistAnalysisClient.secondLayer(request);
            for(Map<String, Object> item : list) {
                if(!item.get("serviceEndTime").toString().contains("-")) {
                    item.put("serviceEndTime",timestampToDate(item.get("serviceEndTime").toString()));
                }
            }
            String fileName = "维保统计分析.xlsx";
            response.setHeader("Content-Disposition", "attachment;filename=".concat(String.valueOf(URLEncoder.encode
                    (fileName, "UTF-8"))));
            response.setHeader("Connection", "close");
            response.setHeader("Content-Type", "application/vnd.ms-excel");
            String[] header = new String[] {"资源池","维保种类","服务厂家数量","设备类型","维保项目数量","维保设备数量","总合同金额(万)","最近维保到期时间"};
            String[] keys = new String[] {"resourcePool","maintenanceProjectType","produceNum","deviceType","projectNum","deviceNum",
                    "totalMoney","serviceEndTime"};
            OutputStream os = response.getOutputStream();// 取得输出流
            ExportExcelUtil eeu = new ExportExcelUtil();
            Workbook book = new SXSSFWorkbook(128);
            eeu.exportExcel(book, 0, "维保统计分析", header, list, keys);
            book.write(os);
            os.flush();
            os.close();
            jsonObject.put("flag", "success");
        } catch (Exception e) {
            log.error("导出Excel数据失败!", e);
            jsonObject.put("flag", "error");
            jsonObject.put("msg", e.getMessage());
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        return jsonObject;
    }

    @Override
    public JSONObject thirdExport(@RequestBody(required = false) MaintenStatistAnalysisRequest request, HttpServletResponse response) {
        JSONObject jsonObject = new JSONObject();
        try {
            List<Map<String, Object>> list = maintenStatistAnalysisClient.thirdLayer(request);
            for(Map<String, Object> item : list) {
                if(!item.get("serviceEndTime").toString().contains("-")) {
                    item.put("serviceEndTime",timestampToDate(item.get("serviceEndTime").toString()));
                }
            }
            String fileName = "维保统计分析.xlsx";
            response.setHeader("Content-Disposition", "attachment;filename=".concat(String.valueOf(URLEncoder.encode
                    (fileName, "UTF-8"))));
            response.setHeader("Connection", "close");
            response.setHeader("Content-Type", "application/vnd.ms-excel");
            String[] header = new String[] {"资源池","维保种类","服务供应商","设备类型","维保项目数量","维保设备数量","总合同金额(万)","最近维保到期时间"};
            String[] keys = new String[] {"resourcePool","maintenanceProjectType","contractProduce","deviceType","projectNum","deviceNum",
                    "totalMoney","serviceEndTime"};
            OutputStream os = response.getOutputStream();// 取得输出流
            ExportExcelUtil eeu = new ExportExcelUtil();
            Workbook book = new SXSSFWorkbook(128);
            eeu.exportExcel(book, 0, "维保统计分析", header, list, keys);
            book.write(os);
            os.flush();
            os.close();
            jsonObject.put("flag", "success");
        } catch (Exception e) {
            log.error("导出Excel数据失败!", e);
            jsonObject.put("flag", "error");
            jsonObject.put("msg", e.getMessage());
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        return jsonObject;
    }

    @Override
    public JSONObject fourthExport(@RequestBody(required = false) MaintenStatistAnalysisRequest request, HttpServletResponse response) {
        JSONObject jsonObject = new JSONObject();
        try {
            List<Map<String, Object>> list = maintenStatistAnalysisClient.fourthLayer(request);
            // 处理维保数量数据、服务结束时间的格式
            for(Map<String, Object> item : list) {
                if(!item.get("serviceEndTime").toString().contains("-")) {
                    item.put("serviceEndTime",timestampToDate(item.get("serviceEndTime").toString()));
                }
                String serviceType = item.get("serviceType") == null ? null : item.get("serviceType").toString();
                String serviceNum = item.get("serviceNum") == null ? null : item.get("serviceNum").toString();
                if(serviceType != null && serviceNum != null) {
                    String[] types = serviceType.split(",");
                    String[] nums = serviceNum.split(",");
                    StringBuilder rsStr = new StringBuilder( types[0] + "(" + nums[0] + ")");
                    for(int i=1;i<types.length;i++) {
                        rsStr.append(";" + types[i] + "(" + nums[i] + ")");
                    }
                    item.put("service_num",rsStr.toString());
                } else {
                    item.put("service_num","");
                }
            }
            String fileName = "维保统计分析.xlsx";
            response.setHeader("Content-Disposition", "attachment;filename=".concat(String.valueOf(URLEncoder.encode
                    (fileName, "UTF-8"))));
            response.setHeader("Connection", "close");
            response.setHeader("Content-Type", "application/vnd.ms-excel");
            String[] header = new String[] {"维保项目","合同供应商","服务供应商","设备类型","资源池","采购方式","服务到期时间",
                    "服务数量","设备数量","合同金额(万)","折扣率(%)"};
            String[] keys = new String[] {"projectName","contractProduce","serviceProduce","deviceType","resourcePool",
                    "procureType","serviceEndTime","service_num","deviceNum","money","discountRate"};
            OutputStream os = response.getOutputStream();// 取得输出流
            ExportExcelUtil eeu = new ExportExcelUtil();
            Workbook book = new SXSSFWorkbook(128);
            eeu.exportExcel(book, 0, "维保统计分析", header, list, keys);
            book.write(os);
            os.flush();
            os.close();
            jsonObject.put("flag", "success");
        } catch (Exception e) {
            log.error("导出Excel数据失败!", e);
            jsonObject.put("flag", "error");
            jsonObject.put("msg", e.getMessage());
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        return jsonObject;
    }

    @Override
    public List<Map<String, Object>> maintenancePeriodAnalysis(@RequestBody MaintenStatistAnalysisRequest request) {
        return maintenStatistAnalysisClient.maintenancePeriodAnalysis(request);
    }

    @Override
    public JSONObject maintenPeriodExport(@RequestBody(required = false) MaintenStatistAnalysisRequest request, HttpServletResponse response) {
        JSONObject jsonObject = new JSONObject();
        try {
            List<Map<String, Object>> list = maintenStatistAnalysisClient.maintenancePeriodAnalysis(request);
            // 处理维保数量数据、服务结束时间的格式
            for(Map<String, Object> item : list) {
                if(!item.get("serviceEndTime").toString().contains("-")) {
                    item.put("serviceEndTime",timestampToDate(item.get("serviceEndTime").toString()));
                }
                Object type = item.get("serviceType");
                Object num = item.get("serviceNum");
                String serviceType = type == null|| "".equals(type.toString()) ? null : type.toString();
                String serviceNum = num == null || "".equals(num.toString()) ? null : num.toString();
                if(serviceType != null &&serviceNum != null) {
                    String[] types = serviceType.split(",");
                    String[] nums = serviceNum.split(",");
                    StringBuilder rsStr = new StringBuilder( types[0] + "(" + nums[0] + ")");
                    for(int i=1;i<types.length;i++) {
                        rsStr.append(";" + types[i] + "(" + nums[i] + ")");
                    }
                    item.put("service_num",rsStr.toString());
                } else {
                    item.put("service_num","");
                }
            }
            String fileName = "维保周期.xlsx";
            response.setHeader("Content-Disposition", "attachment;filename=".concat(String.valueOf(URLEncoder.encode
                    (fileName, "UTF-8"))));
            response.setHeader("Connection", "close");
            response.setHeader("Content-Type", "application/vnd.ms-excel");
            String[] header = new String[] {"维保项目","服务供应商","合同供应商","设备类型","资源池","周期",
                    "服务数量","设备数量","合同金额(万)","折扣率(%)","设备差额","合同金额差额","折扣率差额"};
            String[] keys = new String[] {"projectName","serviceProduce","contractProduce","deviceType","deviceArea",
                    "period","service_num","deviceNum","money","discountRate","deviceNumDiff","moneyDiff","rateDiff"};
            OutputStream os = response.getOutputStream();// 取得输出流
            ExportExcelUtil eeu = new ExportExcelUtil();
            Workbook book = new SXSSFWorkbook(128);
            eeu.exportExcelForPeriod(book, 0, "维保周期", header, list, keys);
            book.write(os);
            os.flush();
            os.close();
            jsonObject.put("flag", "success");
        } catch (Exception e) {
            log.error("导出Excel数据失败!", e);
            jsonObject.put("flag", "error");
            jsonObject.put("msg", e.getMessage());
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        return jsonObject;
    }

    private String timestampToDate(String timestamp) {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        long lt = new Long(timestamp);
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);
        return res;
    }
}
