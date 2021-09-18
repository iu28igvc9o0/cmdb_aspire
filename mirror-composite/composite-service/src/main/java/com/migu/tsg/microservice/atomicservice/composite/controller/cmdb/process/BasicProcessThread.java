package com.migu.tsg.microservice.atomicservice.composite.controller.cmdb.process;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: BasicProcessThread
 * Author:   zhu.juwang
 * Date:     2019/8/9 10:38
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Slf4j
@Data
public class BasicProcessThread extends ProcessThread {
    /**
     * 设置数据Map的值
     * @param keyValueMap
     * @param key
     * @param value
     */
    protected void setDataMap(Map<String, String> keyValueMap, String key, String value) {
        Pattern p = Pattern.compile("\t|\r|\n");
        Matcher m = p.matcher(value);
        value = m.replaceAll("");
        switch (importType.toLowerCase(Locale.ENGLISH)) {
            case "deviceinfo":
            case "repairevent":
            case "problemevent":
            case "equipmentproblem":
                keyValueMap.put(key, value.trim());
                keyValueMap.put("create_username", processParams.getCreateUsername());
                keyValueMap.put("create_user_phone", processParams.getCreateUserPhone());
                keyValueMap.put("quarter", processParams.getQuarter());
                keyValueMap.put("province", processParams.getProvince());
                break;
            default:
                keyValueMap.put(key, value);
                break;
        }
    }

    @Override
    protected void setSpecialData(Map<String, Object> importData) {
        importData.put("templateHeader", getTemplateHeader());
    }

    @Override
    protected void init() {

    }

    @Override
    protected void valid() {
        super.validHeader();
        boolean validUser = false;
        switch (importType.toLowerCase(Locale.ENGLISH)) {
            case "deviceinfo":
            case "repairevent":
            case "problemevent":
            case "equipmentproblem":
                validUser = true;
                break;
            default:
                break;
        }
        if (validUser) {
            if (StringUtils.isEmpty(processParams.getProvince())) {
                throw new RuntimeException("分支机构不能为空");
            }
            if (StringUtils.isEmpty(processParams.getCreateUsername())) {
                throw new RuntimeException("用户信息不能为空");
            }
            if (StringUtils.isEmpty(processParams.getQuarter())) {
                throw new RuntimeException("年份季度不能为空");
            }
        }
    }

    protected List<String> getTemplateHeader() {
        String templateHeader = null;
        switch (importType.toLowerCase(Locale.ENGLISH)) {
            case "repairevent":
                templateHeader = importTemplate.getMaintenance().getRepairevent();
                break;
            case "problemevent":
                templateHeader = importTemplate.getMaintenance().getProblemevent();
                break;
            case "equipmentproblem":
                templateHeader = importTemplate.getMaintenance().getEquipmentproblem();
                break;
            case "maintensoftware":
                templateHeader = importTemplate.getMaintenance().getMaintensoftware();
                break;
            case "maintensoftwarerecord":
                templateHeader = importTemplate.getMaintenance().getMaintensoftwarerecord();
                break;
            case "maintenance_project":
                templateHeader = importTemplate.getMaintenance().getProject();
                break;
            case "maintenance_project_bind_device":
                templateHeader = importTemplate.getMaintenance().getMaintenanceProjectBindDevice();
                break;
            case "portRelation":
                templateHeader = importTemplate.getMaintenance().getPortRelation();
                break;
            case "resourceassign":
                templateHeader = importTemplate.getMaintenance().getResourceassign();
                break;
            default:
                break;
        }
        List<String> headers = new ArrayList<>();
        if (StringUtils.isNotEmpty(templateHeader)) {
            headers = new ArrayList<>(Arrays.asList(templateHeader.split(",")));
        }
        return headers;
    }

    protected Integer getRealHeaderIndex() {
        Integer headerIndex = 0;
        switch (importType.toLowerCase(Locale.ENGLISH)) {
            case "repairevent":
            case "problemevent":
            case "equipmentproblem":
                headerIndex = 1;
                break;
            default:
                break;
        }
        return headerIndex;
    }
}
