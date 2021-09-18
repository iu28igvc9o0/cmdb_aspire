package com.aspire.ums.cmdb.collect.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.aspire.ums.cmdb.collect.CollectConst;
import com.aspire.ums.cmdb.collect.entity.CollectChangeLogEntity;
import com.aspire.ums.cmdb.collect.entity.Page;
import com.aspire.ums.cmdb.collect.mapper.CollectChangeLogMapper;
import com.aspire.ums.cmdb.collect.service.CollectChangeLogService;
import com.aspire.ums.cmdb.helper.MailSendHelper;
import com.aspire.ums.cmdb.maintain.service.InstanceService;
import com.aspire.ums.cmdb.module.entity.FormBean;
import com.aspire.ums.cmdb.module.entity.Module;
import com.aspire.ums.cmdb.module.service.FormService;
import com.aspire.ums.cmdb.util.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: CollectService
 * Author:   zhu.juwang
 * Date:     2019/3/12 14:23
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Service
@Slf4j
public class CollectChangeLogServiceImpl implements CollectChangeLogService {

    @Autowired
    private CollectChangeLogMapper changeLogMapper;
    @Autowired
    private InstanceService instanceService;
    @Autowired
    private FormService formService;
    @Autowired
    private MailSendHelper mailSendHelper;

    @Override
    public Page getChangeLogsByModuleId(String moduleId, Map<String, Object> requestInfo) {
        List<Map<String, String>> queryFields = new LinkedList<>();
        if (requestInfo.containsKey("queryFields")) {
            queryFields = (List<Map<String, String>>) requestInfo.get("queryFields");
        }
        //高级筛选查询条件
        List<Map<String, Object>> searchFields = new LinkedList<>();
        if (requestInfo.containsKey("searchFields")) {
            searchFields = (List<Map<String, Object>>) requestInfo.get("searchFields");
        }
        //拼装查询条件
        StringBuilder queryBuilder = new StringBuilder();
        if (searchFields != null && searchFields.size() > 0) {
            for (Object query : searchFields) {
                Map<String, Object> queryMap = (Map<String, Object>) query;
                //判断类型 如果是日期类型, 需要特殊处理
                String type = queryMap.get("type").toString();
                String id = queryMap.get("id").toString();
                String code = queryMap.get("code").toString();
                Object value = null;
                if (queryMap.containsKey("value")) {
                    value = queryMap.get("value");
                }
                if (value != null) {
                    if (type.equalsIgnoreCase("text") || type.equalsIgnoreCase("singleRowText")) {
                        queryBuilder.append(" and ").append(code).append(" like ").append("'%").append(value).append("%' ");
                    } else if (type.equalsIgnoreCase("listSel")) {
                        queryBuilder.append(" and ").append(code).append(" = ").append("'").append(value).append("'");
                    }
                }
            }
        }
        //普通查询条件
        StringBuilder innerQueryWhere = new StringBuilder();
        if (requestInfo.containsKey("startTime")) {
            innerQueryWhere.append(" and ").append("operatorTime").append(" >= '").append(requestInfo.get("startTime")).append("'");
        }
        if (requestInfo.containsKey("endTime")) {
            innerQueryWhere.append(" and ").append("operatorTime").append(" <= '").append(requestInfo.get("endTime")).append("'");
        }
        if (requestInfo.containsKey("operaterType")) {
            innerQueryWhere.append(" and ").append("operaterType").append(" = '").append(requestInfo.get("operaterType")).append("'");
        }
        if (requestInfo.containsKey("changeContent")) {
            innerQueryWhere.append(" and ").append("changeContent").append(" like '%").append(requestInfo.get("changeContent")).append("%'");
        }
        log.info("getChangeLogsByModuleId: queryFields -> {}. innerWhere-> {}. where -> {}",
                queryFields.toString(), innerQueryWhere.toString(), queryBuilder.toString());
        Integer pageSize = null, currPage = null;
        if (requestInfo.containsKey("pageSize")) {
            pageSize = Integer.parseInt(requestInfo.get("pageSize").toString());
        }
        if (requestInfo.containsKey("pageNumber")) {
            currPage = Integer.parseInt(requestInfo.get("pageNumber").toString());
        }
        Page page = new Page(currPage, pageSize);
        page.setDataList(changeLogMapper.getChangeLogsByModuleId(moduleId, queryFields, innerQueryWhere.toString(), queryBuilder.toString()));
        return page;
    }

    @Override
    public List<CollectChangeLogEntity> getChangeLogsByBatchId(String batchId) {
        return changeLogMapper.getChangeLogsByBatchId(batchId);
    }

    @Override
    public Map<String, Object> getChangeLogDetailByBatchId(String batchId) {
        Map<String, Object> returnMap = new HashMap<>();
        List<CollectChangeLogEntity> entityList = getChangeLogsByBatchId(batchId);
        if (entityList != null && entityList.size() > 0) {
            //获取实例IP
            CollectChangeLogEntity firstEntity = entityList.get(0);
            Map<String, String> queryInstanceMap = new HashMap<>();
            queryInstanceMap.put("instanceId", firstEntity.getInstanceId());
            List<Map> instanceMap = instanceService.getInstanceInfoById(queryInstanceMap);
            String deviceIp = "", moduleName = "", moduleId = "";
            if (instanceMap != null && instanceMap.size() > 0) {
                deviceIp = instanceMap.get(0).get("name").toString();
                moduleName = instanceMap.get(0).get("moduleName").toString();
                moduleId = instanceMap.get(0).get("moduleId").toString();
            }
            returnMap.put("batchId", batchId);
            returnMap.put("deviceIp", deviceIp);
            returnMap.put("operaterType", firstEntity.getOperaterType());
            returnMap.put("operaterObject", moduleName);
            returnMap.put("operatorTime", DateUtils.format(firstEntity.getOperatorTime(), "yyyy-MM-dd HH:mm:ss"));
            returnMap.put("description", firstEntity.getOperaterType() + moduleName);
            //处理属性
            List<Map<String, String>> forms = new LinkedList<>();
            Module module = new Module();
            module.setId(moduleId);
            try {
                List<FormBean> formBeanList = formService.getForms(module);
                if (formBeanList != null && formBeanList.size() > 0) {
                    for (FormBean formBean : formBeanList) {
                        Map<String, String> formMap = new HashMap<>();
                        formMap.put("name", formBean.getName());
                        formMap.put("changeBefore", "");
                        formMap.put("changeAfter", "");
                        for (CollectChangeLogEntity logEntity : entityList) {
                            if (logEntity.getFormId().equals(formBean.getId())) {
                                formMap.put("changeBefore", logEntity.getOldValue());
                                formMap.put("changeAfter", logEntity.getCurrValue());
                            }
                        }
                        forms.add(formMap);
                    }
                }
            } catch (Exception e) {
                log.error("Get module forms error.", e.getMessage() ,e);
                return null;
            }
            returnMap.put("forms", forms);
        }
        return returnMap;
    }

    @Override
    public Map<String, String> sendNotice(JSONObject sendRequest) {
        Map<String, String> returnMap = new HashMap<>();
        if (!sendRequest.containsKey("batchList")) {
            returnMap.put("code", "error");
            returnMap.put("message", "未选择需要发送的告警设备.");
            return returnMap;
        }
        if (!sendRequest.containsKey("employList")) {
            returnMap.put("code", "error");
            returnMap.put("message", "未选择通知对象.");
            return returnMap;
        }
        List<String> batchList = (List<String>) sendRequest.get("batchList");
        List<String> employeeList = (List<String>) sendRequest.get("employList");
        String alarmLevel = sendRequest.getString("alarmLevel");
        for (Object batch : batchList) {
            String instanceId = ((LinkedHashMap) batch).get("instanceId").toString();
            String batchId = ((LinkedHashMap) batch).get("batchId").toString();
            Map<String, String> params = new HashMap<>();
            params.put("instanceId", instanceId);
            List<Map> instanceList = instanceService.getInstanceInfoById(params);
            if (instanceList != null && instanceList.size() > 0) {
                Module module = new Module();
                module.setId(instanceList.get(0).get("moduleId").toString());
                List<FormBean> formBeans = null;
                try {
                    formBeans = formService.getForms(module);
                } catch (Exception e) {
                    log.error("Get form beans error -> {}", e.getMessage() ,e);
                    throw new RuntimeException(e);
                }
                Map<String, String> changeLogDetail = getChangeLogByBatchId(instanceId, batchId, formBeans);
                String deviceIp = instanceList.get(0).get("name").toString();
                String changeContent = changeLogDetail.get("changeContent");
                String emailContent = getEmailContent(deviceIp, changeContent, alarmLevel).toString();
                for (String employeeName : employeeList) {
                    String employeeAddress = CollectConst.EMPLOYEE.get(employeeName);
                    try {
                        mailSendHelper.sendMail("配置项异常变更告警", emailContent, true, employeeAddress);
                    } catch (Exception e) {
                        log.error("发送配置项异常变更告警邮件失败 -> {}", e.getMessage(), e);
                    }
                }
            }
        }
        returnMap.put("code", "success");
        return returnMap;
    }

    @Override
    public Map<String, String> getChangeLogByBatchId(String instanceId, String batchId, List<FormBean> formBeans) {
        return changeLogMapper.getChangeLogByBatchId(instanceId, batchId, formBeans);
    }

    @Override
    public void insertVO(CollectChangeLogEntity changeLogEntity) {
        changeLogMapper.insertVO(changeLogEntity);
    }

    @Override
    public StringBuilder getEmailContent(String deviceIp, String changeContent, String alarmLevel) {
        //重新解析一下发送内容, 去掉多余的";"
        char[] chars = changeContent.toCharArray();
        String newContent = "";
        for (int i=0; i<chars.length; i++) {
            if ((chars[i] == ';' && i == 0) || (chars[i] == ';' && i > 0 && i + 1 < chars.length && chars[i + 1] == ';')) {
                continue;
            }
            newContent += String.valueOf(chars[i]);
        }
        if (newContent.substring(0,1).equalsIgnoreCase(";")) {
            newContent = newContent.substring(1);
        }
        String parseTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        String thTemplate = "<th style='border: 1px solid #000; text-align:center; font-size:13px; width:150px;'>%s</th>";
        String tdTemplate = "<td style='border: 1px solid #000; text-align:center; font-size:13px; width:250px;'>%s</td>";
        StringBuilder tableBuilder = new StringBuilder();
        tableBuilder.append("<table style='border-collapse:collapse; width:400px;'>");
        tableBuilder.append("<tr>").append(String.format(thTemplate, "告警字段")).append(String.format(thTemplate, "告警值")).append("</tr>");
        tableBuilder.append("<tr>").append(String.format(tdTemplate, "所属设备IP")).append(String.format(tdTemplate, deviceIp)).append("</tr>");
        tableBuilder.append("<tr>").append(String.format(tdTemplate, "监控对象")).append(String.format(tdTemplate, "Cmdb DataChange")).append("</tr>");
        tableBuilder.append("<tr>").append(String.format(tdTemplate, "监控值")).append(String.format(tdTemplate, "DataChange")).append("</tr>");
        tableBuilder.append("<tr>").append(String.format(tdTemplate, "告警内容")).append(String.format(tdTemplate, newContent)).append("</tr>");
        tableBuilder.append("<tr>").append(String.format(tdTemplate, "告警时间")).append(String.format(tdTemplate, parseTime)).append("</tr>");
        tableBuilder.append("<tr>").append(String.format(tdTemplate, "应用类型")).append(String.format(tdTemplate, "系统告警")).append("</tr>");
        tableBuilder.append("<tr>").append(String.format(tdTemplate, "告警等级")).append(String.format(tdTemplate, CollectConst.ALARMLAVEL.get(alarmLevel))).append("</tr>");
        tableBuilder.append("</table>");
        return tableBuilder;
    }
}
