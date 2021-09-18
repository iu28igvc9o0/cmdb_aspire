package com.aspire.mirror.alert.server.controller.notify;

import com.aspire.mirror.alert.api.dto.notify.*;
import com.aspire.mirror.alert.api.service.notify.AlertSubscribeRulesService;
import com.aspire.mirror.alert.server.biz.notify.AlertSubscribeRulesBiz;
import com.aspire.mirror.alert.server.dao.notify.po.*;
import com.aspire.mirror.alert.server.util.PayloadParseUtil;
import com.aspire.mirror.alert.server.util.StringUtils;
import com.aspire.mirror.alert.server.biz.alert.AlertsBizV2;
import com.aspire.mirror.alert.server.vo.notify.UpdateAlertSubscribeRulesVo;
import com.aspire.mirror.common.entity.PageResponse;
import com.aspire.mirror.common.util.DateUtil;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Field;
import java.util.*;

import static com.aspire.mirror.alert.server.util.PayloadParseUtil.jacksonBaseParse;

@RestController
@Slf4j
public class AlertSubscribeRulesController implements AlertSubscribeRulesService {
    @Autowired
    private AlertSubscribeRulesBiz alertSubscribeRulesBiz;
    @Autowired
    private AlertsBizV2 alertsBizV2;

    /**
     * 通知订阅规则的查询
     *
     * @return
     */
    @Override
    public PageResponse<AlertSubscribeRulesDto> query(@RequestParam(value = "subscribeRules", required = false) String subscribeRules,
                                                      @RequestParam(value = "deviceIp", required = false) String deviceIp,
                                                      @RequestParam(value = "alertLevel", required = false) String alertLevel,
                                                      @RequestParam(value = "idcType", required = false) String idcType,
                                                      @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
                                                      @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        AlertSubscribeRules alertSubscribeRules1=new AlertSubscribeRules();
        Integer begin=(pageNo-1)*pageSize;
        alertSubscribeRules1.setPageNo(begin);
        alertSubscribeRules1.setSubscribeRules(subscribeRules);
        alertSubscribeRules1.setPageSize(pageSize);
        alertSubscribeRules1.setAlertLevel(alertLevel);
        alertSubscribeRules1.setIdcType(idcType);
        alertSubscribeRules1.setDeviceIp(deviceIp);
        PageResponse<AlertSubscribeRules> query = alertSubscribeRulesBiz.query(alertSubscribeRules1);
        PageResponse<AlertSubscribeRulesDto> pageResponseAlertSubscribeRulesResponse = new PageResponse<AlertSubscribeRulesDto>();
        pageResponseAlertSubscribeRulesResponse.setCount(query.getCount());
        List<AlertSubscribeRulesDto> hisAlertsRespList = Lists.newArrayList();
        List<AlertSubscribeRules> result = query.getResult();
        if (!CollectionUtils.isEmpty(result)) {
            hisAlertsRespList = jacksonBaseParse(AlertSubscribeRulesDto.class, result);
        }
        pageResponseAlertSubscribeRulesResponse.setResult(hisAlertsRespList);
        return pageResponseAlertSubscribeRulesResponse;
    }

    /**
     * 订阅管理的查询
     *
     * @return
     */
    @Override
    public PageResponse<AlertSubscribeRulesDto> queryRules(@RequestParam(value = "subscribeRules", required = false) String subscribeRules,
                                                           @RequestParam(value = "isOpen", required = false) String isOpen,
                                                           @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
                                                           @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        AlertSubscribeRules alertSubscribeRules1 = new AlertSubscribeRules();
        Integer begin = (pageNo - 1) * pageSize;
        alertSubscribeRules1.setPageNo(begin);
        alertSubscribeRules1.setPageSize(pageSize);
        alertSubscribeRules1.setSubscribeRules(subscribeRules);
        alertSubscribeRules1.setIsOpen(isOpen);
        PageResponse<AlertSubscribeRules> query = alertSubscribeRulesBiz.queryRules(alertSubscribeRules1);
        PageResponse<AlertSubscribeRulesDto> pageResponseAlertSubscribeRulesResponse = new PageResponse<AlertSubscribeRulesDto>();
        pageResponseAlertSubscribeRulesResponse.setCount(query.getCount());
        List<AlertSubscribeRulesDto> hisAlertsRespList = Lists.newArrayList();
        List<AlertSubscribeRules> result = query.getResult();
        if (!CollectionUtils.isEmpty(result)) {
            hisAlertsRespList = jacksonBaseParse(AlertSubscribeRulesDto.class, result);
        }
        pageResponseAlertSubscribeRulesResponse.setResult(hisAlertsRespList);
        return pageResponseAlertSubscribeRulesResponse;
    }
@Override
    public List<AlertSubscribeRulesDto>queryAlertSubscribeRules(){
        List<AlertSubscribeRules> alertSubscribeRulesList = alertSubscribeRulesBiz.queryAlertSubscribeRules();
        List<AlertSubscribeRulesDto> alertList = Lists.newArrayList();
        if (null == alertSubscribeRulesList) {
            return alertList;
        }
        alertList = jacksonBaseParse(AlertSubscribeRulesDto.class, alertSubscribeRulesList);
        return alertList;
    }


    /**
     * 批量删除告警订阅管理
     *
     * @return
     */
    @Override
    public  ResponseEntity<String> deteleRules(@RequestParam("ids") String ids) {
        if(null ==ids){
            throw new  RuntimeException("ids are null");
        }
        String[] alertIdArr = ids.split(",");
        alertSubscribeRulesBiz.deteleRules(Arrays.asList(alertIdArr));
        return new ResponseEntity<>("success", HttpStatus.OK);
    }
    /**
     * 批量
     *
     * @return
     */
    @Override
    public  ResponseEntity<String> deleteAlertSubscribeRulesManagement(@RequestParam("ids") String ids) {
        if(null ==ids){
            throw new  RuntimeException("ids are null");
        }
        String[] alertIdArr = ids.split(",");
        alertSubscribeRulesBiz.deleteSubscribeRulesManagement(Arrays.asList(alertIdArr));
        return new ResponseEntity<>("success", HttpStatus.OK);
    }
    /**
     * 批量更新告警订阅的状态
     *
     * @return
     */
    @Override
    public ResponseEntity<String> updateRules(@RequestParam("ids") String ids, String isOpen) {
        if(null ==ids){
            throw new RuntimeException("idlist are null");
        }
        if(null ==isOpen){
            throw new RuntimeException("isOpen are null");
        }
        String[] alertIdArr = ids.split(",");
        List<String> strings = Arrays.asList(alertIdArr);
        alertSubscribeRulesBiz.updateRules(strings, isOpen);
        return new ResponseEntity<>("success", HttpStatus.OK);
    }

    /**
     * 导出告警点阅数据
     *
     * @return
     */
    @Override
    public List<Map<String, Object>> export(@RequestParam("ids")String ids)throws Exception {
        List<Map<String, Object>> dataLists = Lists.newArrayList();
        if (ids == null) {
            log.error("ids  is null or query type is empty !");
            return dataLists;
        }
        String[] split = ids.split(",");
        List<String> strings = Arrays.asList(split);
        List<ExportAlertSubscribeRulesManagement> export = alertSubscribeRulesBiz.export(strings);
        for (ExportAlertSubscribeRulesManagement alertsDTO : export) {
            dataLists.add(objectToMap(alertsDTO));
        }
        return dataLists;

    }
    private Map<String, Object> objectToMap(Object obj) throws Exception {
        if(obj == null){
            return null;
        }
        Map<String, Object> map = new HashMap<String, Object>();
        Field[] declaredFields = obj.getClass().getDeclaredFields();
        for (Field field : declaredFields) {
            ReflectionUtils.makeAccessible(field);
            map.put(field.getName(), field.get(obj));
        }
        return map;
    }
    /**
     * 规则名称，告警等级，归属资源池
     */
    @Override
    public List<AlertSubscribeRulesManagementDto> querySubscribeRules() {
        List<AlertSubscribeRulesManagement> alertSubscribeRulesPageResponse = alertSubscribeRulesBiz.querySubscribeRules();
        List<AlertSubscribeRulesManagementDto> alertList = Lists.newArrayList();
        if (null == alertSubscribeRulesPageResponse) {
            return alertList;
        }
        alertList = jacksonBaseParse(AlertSubscribeRulesManagementDto.class, alertSubscribeRulesPageResponse);
        return alertList;

    }

    @Override
    public List<EmergencySubscribeRulesEmailRequestDto> alertSubscribeRulesEmailNotify(@RequestBody EmergencySubscribeRulesEmailRequestDto emergencySubscribeRulesRequest) {
        List<EmergencySubscribeRulesEmailRequestDto> EmailMap = Lists.newArrayList();
        if (StringUtils.isEmpty(emergencySubscribeRulesRequest.getAlertIds())) {
            throw new RuntimeException("alertIds are null");
        }
        if (StringUtils.isEmpty(emergencySubscribeRulesRequest.getEmails())) {
            throw new RuntimeException("emails are null");
        }
        String alertIds = emergencySubscribeRulesRequest.getAlertIds();
        String subject = emergencySubscribeRulesRequest.getSubject();
        String emails = emergencySubscribeRulesRequest.getEmails();
        String[] alertIdArr = alertIds.split(",");
        List<Map<String, Object>> maps = alertsBizV2.selectByIds(Arrays.asList(alertIdArr));
        String subjectMessage = getSubject(subject, maps);
        log.info("邮件主题&&*&%（（&%&" + subjectMessage);
        String mergeMessages = getEmailMessages(emails, maps);
        EmergencySubscribeRulesEmailRequestDto emergencySubscribeRulesEmailRequestDto = new EmergencySubscribeRulesEmailRequestDto();
        emergencySubscribeRulesEmailRequestDto.setEmails(mergeMessages.toString().replaceAll("@\\{[^\\}.]*\\}", ""));
        emergencySubscribeRulesEmailRequestDto.setSubject(subjectMessage.toString().replaceAll("@\\{[^\\}.]*\\}", ""));
        EmailMap.add(emergencySubscribeRulesEmailRequestDto);
        log.info("8&^&*%&^%*^&%*&%*^" + mergeMessages);
        return EmailMap;
    }

    @Override
    public ResponseEntity<String> CreateSubscribeRules(@RequestBody CreateAlertSubscribeRulesDto createAlertSubscribeRules) {
        if(createAlertSubscribeRules ==null){
            throw new RuntimeException("create param is null");
        }
        alertSubscribeRulesBiz.CreateSubscribeRules(PayloadParseUtil.jacksonBaseParse(CreateAlertSubscribeRules.class, createAlertSubscribeRules));
        return new ResponseEntity<>("success", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> UpdateSubscribeRules(@RequestBody UpdateAlertSubscribeRulesDto updateAlertSubscribeRules) {
        if (updateAlertSubscribeRules == null) {
            throw new RuntimeException("update param is null");
        }
        alertSubscribeRulesBiz.UpdateSubscribeRules(PayloadParseUtil.jacksonBaseParse(UpdateAlertSubscribeRulesVo.class, updateAlertSubscribeRules));
        return new ResponseEntity<>("success", HttpStatus.OK);
    }



    @Override
    public AlertSubscribeRulesDetailShowApiDto GetSubscribeRulesById(@RequestParam("id") String id) {
        AlertSubscribeRulesDetailShow alertSubscribeRulesDetailShow = alertSubscribeRulesBiz.GetSubscribeRulesById(id);
        AlertSubscribeRulesDetailShowApiDto alertDeriveResp = new AlertSubscribeRulesDetailShowApiDto();
        AlertSubscribeRulesDetail alertSubscribeRulesDetail = alertSubscribeRulesDetailShow.getAlertSubscribeRulesDetail();
        AlertSubscribeRulesDetailDto alertSubscribeRulesDetailDto=new AlertSubscribeRulesDetailDto();
        AlertSubscribeRulesDetailDto alertSubscribeRulesDetailDto1 = PayloadParseUtil.jacksonBaseParse(AlertSubscribeRulesDetailDto.class, alertSubscribeRulesDetail);
        List<Reciver> reciverList = alertSubscribeRulesDetailShow.getReciverList();
        List<AlertSubscribeRulesManagement> alertSubscribeRulesManagementList = alertSubscribeRulesDetailShow.getAlertSubscribeRulesManagementList();
        List<ReciverDto> reciverList1=Lists.newArrayList();
        List<AlertSubscribeRulesManagementDto> AlertSubscribeRulesManagementDtoList=Lists.newArrayList();
        if(!CollectionUtils.isEmpty(reciverList)) {
            reciverList1 = jacksonBaseParse(ReciverDto.class, reciverList);
        }
        alertDeriveResp.setReciverList(reciverList1);
        alertDeriveResp.setAlertSubscribeRulesDetail(alertSubscribeRulesDetailDto1);
        if(!CollectionUtils.isEmpty(alertSubscribeRulesManagementList)) {
            AlertSubscribeRulesManagementDtoList = jacksonBaseParse(AlertSubscribeRulesManagementDto.class, alertSubscribeRulesManagementList);
        }
        alertDeriveResp.setAlertSubscribeRulesManagementList(AlertSubscribeRulesManagementDtoList);
        return alertDeriveResp;
    }

    private String getEmailMessages(String mergeTemplate, List<Map<String, Object>> mergeList) {
        StringBuilder sb = new StringBuilder();
        if (org.springframework.util.StringUtils.isEmpty(mergeTemplate)) {
            return sb.toString();
        }
        if (!CollectionUtils.isEmpty(mergeList)) {
            for (Map<String, Object> merge : mergeList) {
                String tempTmeplate = mergeTemplate;
                for (Map.Entry<String, Object> entry : merge.entrySet()) {
                    String key = entry.getKey();
                    Object value = entry.getValue();
                    if (value == null) {
                        continue;
                    }
                    if (value instanceof String) {
                        tempTmeplate = tempTmeplate.replaceAll("@\\{" + key + "\\}", (String) value);
                    } else if (value instanceof Integer || value instanceof Double || value instanceof Float
                            || value instanceof Long || value instanceof Boolean) {
                        tempTmeplate = tempTmeplate.replaceAll("@\\{" + key + "\\}", value + "");
                    } else if (value instanceof Date) {
                        tempTmeplate = tempTmeplate.replaceAll("@\\{" + key + "\\}", DateUtil.format((Date) value, "yyyy-MM-dd HH:mm:ss"));
                    }

                }
                sb.append(tempTmeplate);
            }
        }
        return sb.toString();
    }

    private String getSubject(String mergeTemplate, List<Map<String, Object>> mergeList) {
        StringBuilder sb = new StringBuilder();
        if (org.springframework.util.StringUtils.isEmpty(mergeTemplate)) {
            return sb.toString();
        }
        if (!CollectionUtils.isEmpty(mergeList)) {
//            for (int i = 0; i < mergeList.size(); i++) {
                Map<String, Object> stringObjectMap = mergeList.get(0);
                String tempTmeplate = mergeTemplate;
                for (Map.Entry<String, Object> entry : stringObjectMap.entrySet()) {
                    String key = entry.getKey();
                    Object value = entry.getValue();
                    if (value == null) {
                        continue;
                    }
                    if (value instanceof String) {
                        tempTmeplate = tempTmeplate.replaceAll("@\\{" + key + "\\}", (String) value);
                    } else if (value instanceof Integer || value instanceof Double || value instanceof Float
                            || value instanceof Long || value instanceof Boolean) {
                        tempTmeplate = tempTmeplate.replaceAll("@\\{" + key + "\\}", value + "");
                    } else if (value instanceof Date) {
                        tempTmeplate = tempTmeplate.replaceAll("@\\{" + key + "\\}", DateUtil.format((Date) value, "yyyy-MM-dd HH:mm:ss"));
                    }

                }
                sb.append(tempTmeplate);
//            }
        }
        return sb.toString();
    }


}


