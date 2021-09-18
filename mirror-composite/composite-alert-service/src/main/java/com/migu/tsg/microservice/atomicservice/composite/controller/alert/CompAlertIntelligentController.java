package com.migu.tsg.microservice.atomicservice.composite.controller.alert;

import com.alibaba.fastjson.JSONObject;
import com.aspire.mirror.alert.api.dto.alert.AlertsDetailResponse;
import com.aspire.mirror.alert.api.dto.alert.AlertsOverViewResponse;
import com.aspire.mirror.alert.api.dto.alert.AlertsQueryRequest;
import com.aspire.mirror.common.entity.PageResponse;
import com.aspire.mirror.composite.payload.alert.CompAlertsDetailResp;
import com.aspire.mirror.composite.payload.alert.CompAlertsOverViewResponse;
import com.aspire.mirror.composite.payload.alert.CompAlertsQueryRequest;
import com.aspire.mirror.composite.service.alert.ICompAlertIntelligentService;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.alert.AlertIntelligentServiceClient;
import com.migu.tsg.microservice.atomicservice.composite.controller.CommonResourceV2Controller;
import com.migu.tsg.microservice.atomicservice.composite.controller.authcontext.RequestAuthContext;
import com.migu.tsg.microservice.atomicservice.composite.controller.util.ExportExcelUtil;
import com.migu.tsg.microservice.atomicservice.composite.helper.CmdbV2Helper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.migu.tsg.microservice.atomicservice.composite.controller.util.PayloadParseUtil.jacksonBaseParse;

@RestController
@Slf4j
public class CompAlertIntelligentController extends CommonResourceV2Controller implements ICompAlertIntelligentService {

    private static final String CMDB_HELPER_KEY_IDCTYPE = "idcType";

    @Autowired
    private AlertIntelligentServiceClient alertIntelligentServiceClient;
    @Autowired
    private CmdbV2Helper cmdbHelper;

    @Override
    public PageResponse<CompAlertsDetailResp> queryAlertIntelligent(@RequestBody CompAlertsQueryRequest queryRequest,
                                                                    @RequestParam(value = "alertId", required = false) String alertId) throws ParseException {
        AlertsQueryRequest alertsQueryRequest = jacksonBaseParse(AlertsQueryRequest.class, queryRequest);
        RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
        String username = authCtx.getUser().getUsername();
        alertsQueryRequest.setUserName(username);
        log.info("#=====> alert query entity: {}", JSONObject.toJSONString(alertsQueryRequest));
        PageResponse<AlertsDetailResponse> pageResponse = alertIntelligentServiceClient.queryAlertIntelligent(alertsQueryRequest,alertId);
        PageResponse<CompAlertsDetailResp> compResponse = new PageResponse<>();
        compResponse.setCount(pageResponse.getCount());
        List<CompAlertsDetailResp> alertsRespList = Lists.newArrayList();
        List<AlertsDetailResponse> alertsList = pageResponse.getResult();
        if (!CollectionUtils.isEmpty(alertsList)) {
            alertsRespList = jacksonBaseParse(CompAlertsDetailResp.class, alertsList);
//            for (AlertsDetailResponse detailResponse : alertsList) {
//                CompAlertsDetailResp resp = jacksonBaseParse(CompAlertsDetailResp.class, detailResponse);
//
//                String bizSys = cmdbHelper.getBizSysName(detailResponse.getBizSys());
//                resp.setBizSys( StringUtils.isEmpty(bizSys) ? detailResponse.getBizSys() : bizSys);
//
//                String sourceRoom = cmdbHelper.getRoomName(detailResponse.getSourceRoom());
//                resp.setSourceRoom(StringUtils.isEmpty(sourceRoom) ? detailResponse.getSourceRoom() : sourceRoom);
//
//                String idcType = cmdbHelper.getCodeName(CMDB_HELPER_KEY_IDCTYPE, detailResponse.getIdcType());
//                resp.setIdcType(StringUtils.isEmpty(idcType) ? detailResponse.getIdcType() : idcType);
//
//                String deviceType= cmdbHelper.getCodeName("device_class", detailResponse.getDeviceType());
//                resp.setDeviceType(StringUtils.isEmpty(deviceType) ? detailResponse.getDeviceType() : deviceType);
//                alertsRespList.add(resp);
//            }
        }
        compResponse.setResult(alertsRespList);
        return compResponse;
    }

    @Override
    public CompAlertsOverViewResponse alertIntelligentOverview(@RequestBody CompAlertsQueryRequest queryRequest) throws ParseException {
        AlertsQueryRequest alertsQueryRequest = jacksonBaseParse(AlertsQueryRequest.class, queryRequest);
        RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
        String username = authCtx.getUser().getUsername();
        alertsQueryRequest.setUserName(username);
        AlertsOverViewResponse overview = alertIntelligentServiceClient.alertIntelligentOverview(alertsQueryRequest);
        return jacksonBaseParse(CompAlertsOverViewResponse.class, overview);
    }

//    private static final SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");

    @Override
    public void exportAlertIntelligentData(@RequestBody CompAlertsQueryRequest queryRequest,HttpServletResponse response) throws Exception {
        AlertsQueryRequest alertsQueryRequest = jacksonBaseParse(AlertsQueryRequest.class, queryRequest);
        RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
        String username = authCtx.getUser().getUsername();
        alertsQueryRequest.setUserName(username);
        String[] headerList = {"级别","设备IP","监控对象","监控值","告警内容","开始时间","当前时间","资源池","所属位置","机房位置","业务线","告警类型","状态","通知方式","通知状态","通知时间","转派人","转派状态","转派时间","待确认人","确认人","确认时间","确认内容","派单状态","派单时间","派单类型","告警次数","设备厂商","设备型号"};
        String[] keyList = {"alertLevel","deviceIp","moniObject","curMoniValue","moniIndex","alertStartTime","curMoniTime","idcType","source","sourceRoom","idcType","objectType","orderStatus","reportType","reportStatus","reportTime","transUser","transStatus","transTime","toConfirmUser","confirmedUser","confirmedTime","confirmedContent","deliverStatus","deliverTime","orderType","alertCount","deviceMfrs","deviceModel"};
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        String title = "告警导出列表_" + sDateFormat.format(new Date());
        String fileName = title+".xlsx";
        List<Map<String, Object>> dataList = alertIntelligentServiceClient.exportAlertIntelligentData(alertsQueryRequest);
        for (Map<String, Object> alert : dataList) {
//            if (alert.get("bizSys") != null) {
//                String bizSys = cmdbHelper.getBizSysName(String.valueOf(alert.get("bizSys")));
//                alert.put("bizSys", StringUtils.isEmpty(bizSys) ? alert.get("bizSys") : bizSys);
//            }
//            if (alert.get("sourceRoom") != null) {
//                String sourceRoom = cmdbHelper.getRoomName(String.valueOf(alert.get("sourceRoom")));
//                alert.put("sourceRoom", StringUtils.isEmpty(sourceRoom) ? alert.get("sourceRoom") : sourceRoom);
//            }
            if (alert.get("idcType") != null) {
                String idcType = cmdbHelper.getCodeName(CMDB_HELPER_KEY_IDCTYPE, String.valueOf(alert.get("idcType")));
                alert.put("idcType", StringUtils.isEmpty(idcType) ? alert.get("idcType") : idcType);
            }
            if (alert.get("deviceType") != null) {
                String deviceType = cmdbHelper.getCodeName("device_class", String.valueOf(alert.get("deviceType")));
                alert.put("deviceType", StringUtils.isEmpty(deviceType) ? alert.get("deviceType") : deviceType);
            }
        }
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
