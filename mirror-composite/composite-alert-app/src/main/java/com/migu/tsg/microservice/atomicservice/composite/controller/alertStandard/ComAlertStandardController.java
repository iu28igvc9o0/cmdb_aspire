package com.migu.tsg.microservice.atomicservice.composite.controller.alertStandard;

import com.aspire.mirror.alert.api.dto.alertStandard.AlertStandardReq;
import com.aspire.mirror.alert.api.dto.alertStandard.AlertStandardResp;
import com.aspire.mirror.common.entity.PageResponse;
import com.aspire.mirror.composite.payload.alertStandard.ComAlertStandardReq;
import com.aspire.mirror.composite.payload.alertStandard.ComAlertStandardResp;
import com.aspire.mirror.composite.service.alertStandard.ICompAlertStandardService;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.alert.AlertStandardServiceClient;
import com.migu.tsg.microservice.atomicservice.composite.controller.util.ExportExcelUtil;
import com.migu.tsg.microservice.atomicservice.composite.controller.util.PayloadParseUtil;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

/**
 * @projectName: ComAlertStandardController
 * @description: 类
 * @author: luowenbo
 * @create: 2020-06-11 17:50
 **/
@RestController
public class ComAlertStandardController implements ICompAlertStandardService {
    @Autowired
    private AlertStandardServiceClient client;

    @Override
    public ResponseEntity<String> insert(@RequestBody ComAlertStandardReq as) {
        AlertStandardReq alertIsolateReq = PayloadParseUtil.jacksonBaseParse(AlertStandardReq.class, as);
        return client.insert(alertIsolateReq);
    }

    @Override
    public ResponseEntity<String> update(@RequestBody ComAlertStandardReq as) {
        AlertStandardReq alertIsolateReq = PayloadParseUtil.jacksonBaseParse(AlertStandardReq.class, as);
        return client.update(alertIsolateReq);
    }

    @Override
    public ResponseEntity<String> deleteByIds(@RequestBody Map<String,String> params) {
        return client.deleteByIds(params.get("operator"),params.get("ids").split(","));
    }

    @Override
    public PageResponse<ComAlertStandardResp> listWithPage(@RequestParam(value = "deviceClass",required = false) String deviceClass,
                                                           @RequestParam(value = "deviceType",required = false) String deviceType,
                                                           @RequestParam(value = "monitorKey",required = false) String monitorKey,
                                                           @RequestParam(value = "pageNo",defaultValue = "1") int pageNo,
                                                           @RequestParam(value = "pageSize",defaultValue = "50") int pageSize) {
        PageResponse<AlertStandardResp> list = client.listWithPage(deviceClass,deviceType,monitorKey,pageNo,pageSize);
        List<ComAlertStandardResp> compAlertIsolateRespList = PayloadParseUtil.jacksonBaseParse(ComAlertStandardResp.class, list.getResult());
        PageResponse<ComAlertStandardResp> page = new PageResponse<>();
        page.setCount(list.getCount());
        page.setResult(compAlertIsolateRespList);
        return page;
    }

    @Override
    public ResponseEntity<String> operatorStatus(@RequestBody Map<String,String> params) {
        return client.operatorStatus(params.get("operator"),params.get("ids").split(","));
    }

    @Override
    public ResponseEntity<String> updateHistory(@RequestBody Map<String,String> req) {
        return client.updateHistory(req);
    }

    @Override
    public ResponseEntity<String> updateHistoryOneRow(@PathVariable("id") String id,
                                                      @RequestBody Map<String,String> req) {
        return client.updateHistoryOneRow(id,req);
    }

    @Override
    public ResponseEntity<String> importAlertStandard(@RequestParam("file") MultipartFile file,
                                                      @RequestParam("operator") String operator) {
        return client.importAlertStandard(file,operator);
    }

    @Override
    public ResponseEntity<String> exportAlertStandard(@RequestParam(value = "deviceClass",required = false) String deviceClass,
                                                      @RequestParam(value = "deviceType",required = false) String deviceType,
                                                      @RequestParam(value = "monitorKey",required = false) String monitorKey,
                                                      HttpServletResponse response) {
        try {
            List<Map<String, Object>> list = client.exportAlertStandard(deviceClass, deviceType, monitorKey);
            for(Map<String, Object> item : list) {
                String status = item.get("status").toString().replace("0","禁用").replace("1","启用");
                String levels = item.get("alert_level").toString()
                        .replace("2","低")
                        .replace("3","中")
                        .replace("4","高")
                        .replace("5","重大");
                item.put("status",status);
                item.put("alert_level",levels);
            }
            String fileName = "告警标准化列表.xlsx";
            response.setHeader("Content-Disposition", "attachment;filename=".concat(String.valueOf(URLEncoder.encode
                    (fileName, "UTF-8"))));
            response.setHeader("Connection", "close");
            response.setHeader("Content-Type", "application/vnd.ms-excel");
            String[] header = new String[] {"设备分类","设备类型","监控指标Key","标准名称","告警描述","状态","告警等级"};
            String[] keys = new String[] {"device_class","device_type","monitor_key","standard_name","alert_desc","status","alert_level"};
            OutputStream os = response.getOutputStream();// 取得输出流
            ExportExcelUtil eeu = new ExportExcelUtil();
            Workbook book = new SXSSFWorkbook(128);
            eeu.exportExcel(book, 0, "告警标准化列表", header, list, keys);
            book.write(os);
            os.flush();
            os.close();
        } catch (Exception e) {
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        return new ResponseEntity<>("success", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> downloadTemp(@RequestParam("fileName") String fileName, HttpServletResponse response) {
        BufferedInputStream  inputStream = null;
        OutputStream outputStream = null;
        try {
            String name = fileName.substring(0,fileName.lastIndexOf("."));
            String path = "/download/" + fileName;
            response.setHeader("Content-Disposition", "attachment;filename=".concat(String.valueOf(URLEncoder.encode
                    (name, "UTF-8"))));
            response.setHeader("Connection", "close");
            response.setHeader("Content-Type", "application/vnd.ms-excel");
            // luowenbo 2020-07-24 修改： 代码审计——存储型XSS缺陷
            response.setHeader("Set-Cookie","cookiename=cookievalue; path=/; Domain=domainvaule; Max- age=seconds; HttpOnly");
            inputStream = new BufferedInputStream(this.getClass().getResourceAsStream(path));
            outputStream = response.getOutputStream();
            //循环从文件中读出数据后写出，完成下载
            byte[] b = new byte[1024];
            int len;
            while ((len = inputStream.read(b)) != -1) {
                outputStream.write(b, 0, len);
            }
            response.flushBuffer();
            outputStream.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
                // 召唤jvm的垃圾回收器
                System.gc();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return new ResponseEntity<>("success", HttpStatus.OK);
    }
}
