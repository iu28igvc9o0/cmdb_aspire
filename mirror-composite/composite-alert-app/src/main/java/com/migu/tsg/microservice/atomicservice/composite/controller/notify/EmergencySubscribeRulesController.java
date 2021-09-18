package com.migu.tsg.microservice.atomicservice.composite.controller.notify;

import com.aspire.mirror.alert.api.dto.notify.*;
import com.aspire.mirror.common.entity.PageResponse;
import com.aspire.mirror.composite.payload.notify.*;
import com.aspire.mirror.composite.service.notify.EmergencySubscribeRulesService;
import com.google.common.collect.Lists;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.notify.EmergencySubscribeRulesClient;
import com.migu.tsg.microservice.atomicservice.composite.controller.util.ExportExcelUtil;
import com.migu.tsg.microservice.atomicservice.composite.controller.util.PayloadParseUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;


import static com.migu.tsg.microservice.atomicservice.composite.controller.util.PayloadParseUtil.jacksonBaseParse;


@Slf4j
@RestController
public class EmergencySubscribeRulesController implements EmergencySubscribeRulesService {
    @Autowired
    private EmergencySubscribeRulesClient emergencySubscribeRulesClient;

    /**
     * 订阅告警管理的查询
     *
     * @return
     */
    @Override
    public PageResponse<AlertSubscribeRulesResponse> query(@RequestParam(value = "subscribeRules", required = false) String subscribeRules,
                                                           @RequestParam(value = "deviceIp", required = false) String deviceIp,
                                                           @RequestParam(value = "alertLevel", required = false) String alertLevel,
                                                           @RequestParam(value = "idcType", required = false) String idcType,
                                                           @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
                                                           @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        PageResponse<AlertSubscribeRulesDto> queryResponse = emergencySubscribeRulesClient.query(subscribeRules,deviceIp,alertLevel,idcType,pageNo,pageSize);
        PageResponse<AlertSubscribeRulesResponse> pageResponseAlertSubscribeRulesResponse = new PageResponse<AlertSubscribeRulesResponse>();
        pageResponseAlertSubscribeRulesResponse.setCount(queryResponse.getCount());
        List<AlertSubscribeRulesResponse> hisAlertsRespListResponse = Lists.newArrayList();
        List<AlertSubscribeRulesDto> result1 = queryResponse.getResult();
        if (!CollectionUtils.isEmpty(result1)) {
            hisAlertsRespListResponse = jacksonBaseParse(AlertSubscribeRulesResponse.class, result1);

        }
        pageResponseAlertSubscribeRulesResponse.setResult(hisAlertsRespListResponse);
        return pageResponseAlertSubscribeRulesResponse;
    }

    /**
     * 通知订阅规则管理的查询
     *
     * @return
     */
    @Override
    public PageResponse<AlertSubscribeRulesResponse> queryRules(@RequestParam(value = "subscribeRules", required = false) String subscribeRules,
                                                                @RequestParam(value = "isOpen", required = false) String isOpen,
                                                                @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
                                                                @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        PageResponse<AlertSubscribeRulesDto> pageResponse = emergencySubscribeRulesClient.queryRules(subscribeRules,isOpen,pageNo,pageSize);
        PageResponse<AlertSubscribeRulesResponse> pageResponseAlertSubscribeRulesResponse = new PageResponse<AlertSubscribeRulesResponse>();
        pageResponseAlertSubscribeRulesResponse.setCount(pageResponse.getCount());
        List<AlertSubscribeRulesResponse> hisAlertsRespList = Lists.newArrayList();
        List<AlertSubscribeRulesDto> result = pageResponse.getResult();
        if (!CollectionUtils.isEmpty(result)) {
            hisAlertsRespList = jacksonBaseParse(AlertSubscribeRulesResponse.class, result);
        }
        pageResponseAlertSubscribeRulesResponse.setResult(hisAlertsRespList);
        return pageResponseAlertSubscribeRulesResponse;
    }


    /**
     * 批量删除告警订阅管理
     *
     * @return
     */
    @Override
    public ResponseEntity<String> deteleRules(@RequestParam("ids") String ids) {

        return emergencySubscribeRulesClient.deteleRules(ids);

    }
    @Override
    public ResponseEntity<String> deleteAlertSubscribeRulesManagement(@RequestParam("ids") String ids) {

        return emergencySubscribeRulesClient.deleteAlertSubscribeRulesManagement(ids);

    }


    /**
     * 批量更新告警订阅的状态
     *
     * @return
     */
    @Override
    public ResponseEntity<String> updateRules(@RequestParam("ids") String ids, @RequestParam("isOpen") String isOpen) {
        return emergencySubscribeRulesClient.updateRules(ids, isOpen);

    }


    /**
     * 导出告警管理的数据
     */
    @Override
    public void export(@RequestParam("ids") String ids, HttpServletResponse response) throws Exception {
        List<Map<String, Object>>dataList = emergencySubscribeRulesClient.export(ids);
        log.info("订阅告警中的数据" + dataList);
        String[] headerList = {"规则名称","关联设备IP","告警等级","归属资源池","监控项key","告警内容"};
        String[] keyList = {"subscribeRules","deviceIp","alertLevel","idcType","itemKey","moniIndex"};
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        String title = "告警订阅导出列表_" + sDateFormat.format(new Date());
        String fileName = title + ".xlsx";
        OutputStream os = response.getOutputStream();// 取得输出流
        response.setHeader("Content-Disposition", "attachment;filename=".concat(String.valueOf(URLEncoder.encode(fileName, "UTF-8"))));
        response.setHeader("Connection", "close");
        response.setHeader("Content-Type", "application/vnd.ms-excel");
        log.info("响应的名称头"+response);
        //excel constuct
        ExportExcelUtil eeu = new ExportExcelUtil();
        Workbook book = new SXSSFWorkbook(128);
        eeu.exportExcel(book, 0, title, headerList, dataList, keyList);
        book.write(os);
    }

    /**
     * 规则名称，告警等级，归属资源池
     */
    @Override
    public List<AlertSubscribeRulesManagementRespone> querySubscribeRules() {

        List<AlertSubscribeRulesManagementDto> alertSubscribeRulesPageResponse = emergencySubscribeRulesClient.querySubscribeRules();
        List<AlertSubscribeRulesManagementRespone> alertList = Lists.newArrayList();
        if (null == alertSubscribeRulesPageResponse) {
            return alertList;
        }
        alertList = jacksonBaseParse(AlertSubscribeRulesManagementRespone.class, alertSubscribeRulesPageResponse);
        return alertList;
    }

    /**
     * 依据前端传过来的模板来对邮件内容进行数据拼装
     */
    @Override
    public List<EmergencySubscribeRulesEmailRespone> emailNotify(@RequestBody EmergencySubscribeRules emergencySubscribeRules) {
        EmergencySubscribeRulesEmailRequestDto emergencySubscribeRulesRequest = PayloadParseUtil.jacksonBaseParse(EmergencySubscribeRulesEmailRequestDto.class, emergencySubscribeRules);
        List<EmergencySubscribeRulesEmailRespone> alertList = Lists.newArrayList();
        List<EmergencySubscribeRulesEmailRequestDto> emergencySubscribeRulesEmailRequests = emergencySubscribeRulesClient.alertSubscribeRulesEmailNotify(emergencySubscribeRulesRequest);
        if (null == emergencySubscribeRulesEmailRequests) {
            return alertList;
        }
        alertList = jacksonBaseParse(EmergencySubscribeRulesEmailRespone.class, emergencySubscribeRulesEmailRequests);
        return alertList;
    }

    /**
     * 添加告警订阅
     */
    @Override
    public ResponseEntity<String> CreateSubscribeRules(@RequestBody CreateAlertSubscribeRulesReq createAlertSubscribeRulesReq) {
        return emergencySubscribeRulesClient.CreateSubscribeRules(PayloadParseUtil.jacksonBaseParse(CreateAlertSubscribeRulesDto.class, createAlertSubscribeRulesReq));

    }


    /**
     * 更新告警订阅
     */
    @Override
    public ResponseEntity<String> UpdateSubscribeRules(@RequestBody UpdateAlertSubscribeRulesRequest updateAlertSubscribeRulesRequest) {
        return emergencySubscribeRulesClient.UpdateSubscribeRules(PayloadParseUtil.jacksonBaseParse(UpdateAlertSubscribeRulesDto.class, updateAlertSubscribeRulesRequest));

    }

    @Override
    public AlertSubscribeRulesDetailShowDtoDetailResponse GetSubscribeRulesById(@RequestParam("id") String id) {
        AlertSubscribeRulesDetailShowDtoDetailResponse alertSubscribeRulesDetailShowDtoDetailResponse = new AlertSubscribeRulesDetailShowDtoDetailResponse();
        AlertSubscribeRulesDetailShowApiDto alertSubscribeRulesDetailShowApiDto = emergencySubscribeRulesClient.GetSubscribeRulesById(id);
        AlertSubscribeRulesDetailShowDtoDetail alertSubscribeRulesDetailShowDtoDetail = new AlertSubscribeRulesDetailShowDtoDetail();
        AlertSubscribeRulesDetailDto alertSubscribeRulesDetail = alertSubscribeRulesDetailShowApiDto.getAlertSubscribeRulesDetail();
        AlertSubscribeRulesDetailShowDtoDetail alertSubscribeRulesDetailShowDtoDetail1 = PayloadParseUtil.jacksonBaseParse(AlertSubscribeRulesDetailShowDtoDetail.class, alertSubscribeRulesDetail);
        alertSubscribeRulesDetailShowDtoDetailResponse.setAlertSubscribeRulesDetailShowDtoDetail(alertSubscribeRulesDetailShowDtoDetail1);
        List<ReciverDto> reciverList = alertSubscribeRulesDetailShowApiDto.getReciverList();
        List<AlertSubscribeRulesManagementDto> alertSubscribeRulesManagementList = alertSubscribeRulesDetailShowApiDto.getAlertSubscribeRulesManagementList();
        List<AlertSubscribeRulesManagementRespone>alertSubscribeRulesManagementResponeList=Lists.newArrayList();
        List<Reciver> reciverList1 = Lists.newArrayList();
        if (!CollectionUtils.isEmpty(reciverList)) {
            reciverList1 = jacksonBaseParse(Reciver.class, reciverList);
        }
        alertSubscribeRulesDetailShowDtoDetailResponse.setReciverList(reciverList1);
        if (!CollectionUtils.isEmpty(alertSubscribeRulesManagementList)) {
            alertSubscribeRulesManagementResponeList = jacksonBaseParse(AlertSubscribeRulesManagementRespone.class, alertSubscribeRulesManagementList);
        }
        alertSubscribeRulesDetailShowDtoDetailResponse.setAlertSubscribeRulesManagementResponeList(alertSubscribeRulesManagementResponeList);
        return alertSubscribeRulesDetailShowDtoDetailResponse;
    }

    @Override
    public List<AlertSubscribeRulesResponse>queryAlertSubscribeRules(){
        List<AlertSubscribeRulesDto> alertSubscribeRulesDtos = emergencySubscribeRulesClient.queryAlertSubscribeRules();
        List<AlertSubscribeRulesResponse> alertList = Lists.newArrayList();
        if (null == alertSubscribeRulesDtos) {
            return alertList;
        }
        alertList = jacksonBaseParse(AlertSubscribeRulesResponse.class, alertSubscribeRulesDtos);
        return alertList;
    }
}

