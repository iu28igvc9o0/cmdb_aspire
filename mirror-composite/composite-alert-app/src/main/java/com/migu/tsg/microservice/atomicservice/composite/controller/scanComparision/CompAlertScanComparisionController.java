package com.migu.tsg.microservice.atomicservice.composite.controller.scanComparision;

import com.aspire.mirror.alert.api.dto.scanComparision.AlertScanComparisionDetail;
import com.aspire.mirror.alert.api.dto.scanComparision.AlertScanComparisionReq;
import com.aspire.mirror.common.entity.PageResponse;
import com.aspire.mirror.composite.payload.scanComparision.CompAlertScanComparisionDetail;
import com.aspire.mirror.composite.payload.scanComparision.CompAlertScanComparisionReq;
import com.aspire.mirror.composite.service.scanComparision.ICompAlertScanComparisionService;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.scanComparision.AlertScanComparisionServiceClient;
import com.migu.tsg.microservice.atomicservice.composite.controller.util.ExportExcelUtil;
import com.migu.tsg.microservice.atomicservice.composite.controller.util.PayloadParseUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

import static com.migu.tsg.microservice.atomicservice.composite.controller.util.PayloadParseUtil.jacksonBaseParse;

@RestController
@Slf4j
public class CompAlertScanComparisionController implements ICompAlertScanComparisionService {

    private static final String SUCCESS = "success";
    private static final String ERROR = "error";

    @Autowired
    private AlertScanComparisionServiceClient alertScanComparisionServiceClient;

    @Override
    public PageResponse<CompAlertScanComparisionDetail> getScanComparisionList(
            @RequestBody CompAlertScanComparisionReq request) {
        if (null == request) {
            log.error("[composite >>>]Get Scan Comparision Request is null");
            throw new RuntimeException("[composite >>>]Get Scan Comparision Request is null");
        }
        log.info("[composite >>>]Get Scan Comparision Request is {}", request);
        PageResponse<CompAlertScanComparisionDetail> response = new PageResponse<CompAlertScanComparisionDetail>();
        try {
            PageResponse<AlertScanComparisionDetail> scanComparisionList =
                    alertScanComparisionServiceClient.getScanComparisionList(PayloadParseUtil.jacksonBaseParse(AlertScanComparisionReq.class, request));
            response.setCount(scanComparisionList.getCount());
            response.setResult(jacksonBaseParse(CompAlertScanComparisionDetail.class,scanComparisionList.getResult()));
        } catch (Exception e) {
            throw new RuntimeException("[composite >>>]Get Scan Comparision Error is {}",e);
        }
        return response;
    }

    @Override
    public String deleteScanComparisionById(@RequestBody List<String> request) {
        if (CollectionUtils.isEmpty(request)) {
            log.error("[composite >>>]Delete Scan Comparision Request is null");
            throw new RuntimeException("[composite >>>]Delete Scan Comparision Request is null");
        }
        log.info("[composite >>>]Delete Scan Comparision Request is {}", request);
        try {
            alertScanComparisionServiceClient.deleteScanComparisionById(request);
        } catch (Exception e) {
            log.info("[composite >>>]Delete Scan Comparision Error is {}",e);
            return ERROR;
        }
        return SUCCESS;
    }

    @Override
    public String synScanComparision(@RequestBody List<Map<String,String>> request) {
        if (CollectionUtils.isEmpty(request)) {
            log.error("[composite >>>]Syn Scan Comparision Request is null");
            throw new RuntimeException("[AlertService >>>]Syn Scan Comparision Request is null");
        }
        log.info("[composite >>>]Syn Scan Comparision Request is {}", request);
        try {
            alertScanComparisionServiceClient.synScanComparision(request);
        } catch (Exception e) {
            log.info("[composite >>>]Syn Scan Comparision Error is {}",e);
            return ERROR;
        }
        return SUCCESS;
    }

    @Override
    public void exportScanComparision(@RequestBody CompAlertScanComparisionReq request,
                                      HttpServletResponse response) throws Exception {
        if (null == request) {
            log.error("[composite >>>]Export Scan Comparision Request is null");
            throw new RuntimeException("[composite >>>]Export Scan Comparision Request is null");
        }
        log.info("[composite >>>]Export Scan Comparision Request is {}", request);
        List<Map<String, Object>> dataList =
                alertScanComparisionServiceClient.exportScanComparision(PayloadParseUtil.jacksonBaseParse(AlertScanComparisionReq.class, request));
        String[] headerList = {"设备IP","资源池","首次扫描时间","最新扫描时间","同步状态","最新同步时间"};
        String[] keyList = {"deviceIp","idcType","startScanTime","curScanTime","synStatus","curSynTime"};
        String title = "告警扫描对账导出列表";
        String fileName = title+".xlsx";
        OutputStream os = response.getOutputStream();// 取得输出流
        response.setHeader("Content-Disposition", "attachment;filename=".concat(String.valueOf( URLEncoder.encode(fileName, "UTF-8"))));
        response.setHeader("Connection", "close");
        response.setHeader("Content-Type", "application/vnd.ms-excel");
        //excel constuct
        ExportExcelUtil eeu = new ExportExcelUtil();
        Workbook book = new SXSSFWorkbook(128);
        eeu.exportExcel(book, 0, title, headerList, dataList, keyList);
        book.write(os);
    }
}
