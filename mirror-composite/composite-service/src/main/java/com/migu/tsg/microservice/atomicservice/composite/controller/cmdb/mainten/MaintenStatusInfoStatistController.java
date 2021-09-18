package com.migu.tsg.microservice.atomicservice.composite.controller.cmdb.mainten;

import com.alibaba.fastjson.JSONObject;
import com.aspire.mirror.composite.service.cmdb.mainten.IMaintenStatusInfoStatistAPI;
import com.aspire.ums.cmdb.common.Result;
import com.aspire.ums.cmdb.maintenance.payload.MaintenStatusInfoStatistRequest;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb.mainten.MaintenStatusInfoStatistClient;
import com.migu.tsg.microservice.atomicservice.composite.controller.util.ExportExcelUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import scala.util.parsing.combinator.testing.Str;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
public class MaintenStatusInfoStatistController implements IMaintenStatusInfoStatistAPI {

    @Autowired
    private MaintenStatusInfoStatistClient maintenStatusInfoStatistClient;

    @Override
    public List<Map<String, Object>> statistMaintenStatusInfo(@RequestBody MaintenStatusInfoStatistRequest request) {
        return maintenStatusInfoStatistClient.statistMaintenStatusInfo(request);
    }

    @Override
    public Result<Map<String,Object>> getMaintenProjectList(@RequestBody MaintenStatusInfoStatistRequest request) {
        return maintenStatusInfoStatistClient.getMaintenProjectList(request);
    }

    @Override
    public JSONObject exportProject(@RequestBody MaintenStatusInfoStatistRequest query, HttpServletResponse response) {
        JSONObject jsonObject = new JSONObject();
        try {
            List<Map<String, Object>> list = maintenStatusInfoStatistClient.statistMaintenStatusInfo(query);
            String fileName = "维保状态信息统计.xlsx";
            response.setHeader("Content-Disposition", "attachment;filename=".concat(String.valueOf(URLEncoder.encode
                    (fileName, "UTF-8"))));
            response.setHeader("Connection", "close");
            response.setHeader("Content-Type", "application/vnd.ms-excel");
            String[] header = new String[] {"维保状态","维保项目数量","维保设备数量"};
            String[] keys = new String[] {"maintenStatus","maintenNum","deviceNum"};
            OutputStream os = response.getOutputStream();// 取得输出流
            ExportExcelUtil eeu = new ExportExcelUtil();
            Workbook book = new SXSSFWorkbook(128);
            eeu.exportExcel(book, 0, "维保状态信息统计", header, list, keys);
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
    public JSONObject exportMaintenList(@RequestBody MaintenStatusInfoStatistRequest query, HttpServletResponse response) {
        JSONObject jsonObject = new JSONObject();
        try {
            Result<Map<String,Object>> result = maintenStatusInfoStatistClient.getMaintenProjectList(query);
            List<Map<String,Object>> list = result.getData();
            for(Map<String,Object> item : list) {
                // 时间戳转日期
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                long st = new Long(item.get("service_start_time") == null ? null: item.get("service_start_time").toString());
                long et = new Long(item.get("service_end_time") == null ? null : item.get("service_end_time").toString());
                item.put("service_start_time",simpleDateFormat.format(new Date(st)));
                item.put("service_end_time",simpleDateFormat.format(new Date(et)));
                StringBuffer sb = new StringBuffer("");
                if(item.get("serviceType") != null) {
                    String[] types = item.get("serviceType").toString().split(",");
                    String[] nums = item.get("serviceNum").toString().split(",");
                    sb.append(types[0] + "(" + nums[0] + ")");
                    for(int i=1;i<types.length;i++) {
                        sb.append(";" + types[i] + "(" + nums[i] + ")");
                    }
                }
                item.put("service_num",sb.toString());
            }
            String fileName = "维保项目列表.xlsx";
            response.setHeader("Content-Disposition", "attachment;filename=".concat(String.valueOf(URLEncoder.encode
                    (fileName, "UTF-8"))));
            response.setHeader("Connection", "close");
            response.setHeader("Content-Type", "application/vnd.ms-excel");
            String[] header = new String[] {"项目名称","合同编号","合同供应商","合同联系人","合同联系人电话","合同联系人邮箱","维保类型",
                    "服务形式","服务数量","开始时间","结束时间","服务供应商","服务联系人","服务联系人电话","服务联系人邮箱","设备区域","维保对象类型",
                    "采购类型","金额(万)","设备类型","税前金额(万)","税率","税后金额(万)","单价(万)","总价(万)","结算方式","折扣后金额(万)","折扣率","关联设备数量"};
            String[] keys = new String[] {"project_name","project_no","contractProduce","contractNames","contractPhones",
                    "contractEmails","maintenance_type","service_type","service_num","service_start_time",
                    "service_end_time","serviceProduce","serviceNames","servicePhones","serviceEmails",
                    "device_area","maintenance_project_type","procure_type","money","device_type","pre_tax","tax_rate","after_tax",
                    "unit_price","total_price","pay_method","discount_amount","discount_rate","deviceNum"};
            OutputStream os = response.getOutputStream();// 取得输出流
            ExportExcelUtil eeu = new ExportExcelUtil();
            Workbook book = new SXSSFWorkbook(128);
            eeu.exportExcel(book, 0, "维保项目列表", header, list, keys);
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
}
