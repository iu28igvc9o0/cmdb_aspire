package com.migu.tsg.microservice.atomicservice.composite.controller.bpm;

import com.aspire.mirror.alert.api.dto.bpm.AlertAutoOrderConfigDetail;
import com.aspire.mirror.alert.api.dto.bpm.AlertAutoOrderConfigReq;
import com.aspire.mirror.alert.api.dto.bpm.AlertAutoOrderLog;
import com.aspire.mirror.alert.api.dto.bpm.AlertAutoOrderLogReq;
import com.aspire.mirror.common.entity.PageResponse;
import com.aspire.mirror.composite.payload.bpm.CompAlertAutoOrderConfigDetail;
import com.aspire.mirror.composite.payload.bpm.CompAlertAutoOrderConfigReq;
import com.aspire.mirror.composite.payload.bpm.CompAlertAutoOrderLog;
import com.aspire.mirror.composite.payload.bpm.CompAlertAutoOrderLogReq;
import com.aspire.mirror.composite.service.bpm.ICompAlertAutoOrderConfigService;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.bpm.AlertAutoOrderConfigServiceClient;
import com.migu.tsg.microservice.atomicservice.composite.controller.authcontext.RequestAuthContext;
import com.migu.tsg.microservice.atomicservice.composite.controller.util.ExportExcelUtil;
import com.migu.tsg.microservice.atomicservice.composite.controller.util.PayloadParseUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
public class CompAlertsAutoOrderConfigController implements ICompAlertAutoOrderConfigService {

    @Autowired
    private AlertAutoOrderConfigServiceClient client;

    private static final String SUCCESS = "SUCCESS";
    private static final String ERROR = "ERROR";

    @Override
    public PageResponse<CompAlertAutoOrderConfigDetail> getAlertAutoOrderConfigList(@RequestParam(value = "configName", required = false) String configName,
                                                                                    @RequestParam(value = "isOpen", required = false) String isOpen,
                                                                                    @RequestParam(value = "startTime", required = false) String startTime,
                                                                                    @RequestParam(value = "endTime", required = false) String endTime,
                                                                                    @RequestParam(value = "orderType", required = false) String orderType,
                                                                                    @RequestParam(value = "orderTimeInterval", required = false) String orderTimeInterval,
                                                                                    @RequestParam(value = "pageNum", required = false) Integer pageNum,
                                                                                    @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        PageResponse<CompAlertAutoOrderConfigDetail> response = new PageResponse<CompAlertAutoOrderConfigDetail>();
        PageResponse<AlertAutoOrderConfigDetail> alertAutoOrderConfigList =
                client.getAlertAutoOrderConfigList(configName, isOpen, startTime, endTime, orderType, orderTimeInterval,pageNum, pageSize);
        response.setCount(alertAutoOrderConfigList.getCount());
        response.setResult(PayloadParseUtil.jacksonBaseParse(CompAlertAutoOrderConfigDetail.class,alertAutoOrderConfigList.getResult()));
        return response;
    }

    @Override
    public String createAlertAutoOrderConfig(@RequestBody CompAlertAutoOrderConfigReq request) {
        log.info("[AlertAutoOrderConfig] >>> Insert Alert Auto Order Config Request is {}", request);
        RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
        request.setCreator(authCtx.getUser().getUsername());
        try {
            client.createAlertAutoOrderConfig(PayloadParseUtil.jacksonBaseParse(AlertAutoOrderConfigReq.class, request));
        } catch (Exception e) {
            log.error("[AlertAutoOrderConfig] >>> Insert Alert Auto Order Config Error is {}", e);
            return ERROR;
        }
        return SUCCESS;

    }

    @Override
    public String checkName(@RequestParam(value = "configName") String configName) {
        Map<String, String> map = client.checkName(configName);
        return map.get("check");
    }

    @Override
    public String updateAlertAutoOrderConfig(@RequestBody CompAlertAutoOrderConfigReq request) {
        log.info("[AlertAutoOrderConfig] >>> Update Alert Auto Order Config Request is {}", request);
        RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
        request.setUpdater(authCtx.getUser().getUsername());
        try {
            client.updateAlertAutoOrderConfig(PayloadParseUtil.jacksonBaseParse(AlertAutoOrderConfigReq.class, request));
        } catch (Exception e) {
            log.error("[AlertAutoOrderConfig] >>> Update Alert Auto Order Config Error is {}", e);
            return ERROR;
        }
        return SUCCESS;
    }

    @Override
    public CompAlertAutoOrderConfigDetail getAlertAutoOrderConfigDetail(@RequestParam("uuid") String uuid) {
        return PayloadParseUtil.jacksonBaseParse(CompAlertAutoOrderConfigDetail.class,
                client.getAlertAutoOrderConfigDetail(uuid));
    }

    @Override
    public String deleteAlertAutoOrderConfig(@RequestBody List<String> uuidList) {
        log.info("[AlertAutoOrderConfig] >>> Delete Alert Auto Order Config Request is {}", uuidList);
        try {
            client.deleteAlertAutoOrderConfig(uuidList);
        } catch (Exception e) {
            log.error("[AlertAutoOrderConfig] >>> Delete Alert Auto Order Config Error is {}", e);
            return ERROR;
        }
        return SUCCESS;
    }

    @Override
    public String updateAlertAutoOrderConfigStatus(@RequestBody List<String> uuidList,
                                                 @RequestParam("configStatus") String configStatus) {
        log.info("[AlertAutoOrderConfig] >>> Update Alert Auto Order Config Status Request is {} - {}", uuidList, configStatus);
        try {
            client.updateAlertAutoOrderConfigStatus(uuidList, configStatus);
        } catch (Exception e) {
            log.error("[AlertAutoOrderConfig] >>> Update Alert Auto Order Config Status Error is {}", e);
            return ERROR;
        }
        return SUCCESS;
    }

    @Override
    public String copyAlertAutoOrderConfig(@RequestParam("uuid") String uuid) {
        RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
        try {
            client.copyAlertAutoOrderConfig(uuid,authCtx.getUser().getUsername());
        } catch (Exception e) {
            log.error("[AlertAutoOrderConfig] >>> Copy Alert Auto Order Config Error is {}", e);
            return ERROR;
        }
        return SUCCESS;
    }

    @Override
    public PageResponse<CompAlertAutoOrderLog> getAlertAutoOrderLogList(@RequestBody CompAlertAutoOrderLogReq request) {
        PageResponse<CompAlertAutoOrderLog> response = new PageResponse<CompAlertAutoOrderLog>();
        try {
            PageResponse<AlertAutoOrderLog> alertAutoOrderLogList =
                    client.getAlertAutoOrderLogList(PayloadParseUtil.jacksonBaseParse(AlertAutoOrderLogReq.class, request));
            response.setCount(alertAutoOrderLogList.getCount());
            response.setResult(PayloadParseUtil.jacksonBaseParse(CompAlertAutoOrderLog.class, alertAutoOrderLogList.getResult()));
        } catch (Exception e) {
            log.error("[AlertAutoOrderConfig] >>> Get Alert Auto Order Log Error is {}", e);
        }
        return response;
    }

    @Override
    public void exportAlertAutoOrderLogList(@RequestBody CompAlertAutoOrderLogReq request, HttpServletResponse response) {
        try {
            log.info("[AlertAutoOrderConfig] >>> Export Alert Auto Order Log Request is {}", request);
            List<Map<String, Object>> dataList =
                    client.exportAlertAutoOrderLogList(PayloadParseUtil.jacksonBaseParse(AlertAutoOrderLogReq.class, request));
            String[] headerList = {"规则名称","告警归属设备","告警等级","告警内容","当前监控时间","告警开始时间","告警归属资源池","告警来源",
                                   "业务系统","机房","告警设备名称","设备分类","设备类型","派单时间","派单类型","告警工单"};
            String[] keyList = {"configName","deviceIp","alertLevel","moniIndex","curMoniTime","alertStartTime","idcType","source",
                    "bizSys","sourceRoom","hostName","deviceClass","deviceType","orderTime","orderType","orderId"};
            String title = "告警自动派单日志";
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
