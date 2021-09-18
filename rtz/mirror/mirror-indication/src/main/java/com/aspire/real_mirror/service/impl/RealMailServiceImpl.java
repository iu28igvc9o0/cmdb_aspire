package com.aspire.real_mirror.service.impl;

import com.aspire.common.EnvConfigProperties;
import com.aspire.mirror.dao.IIndicationDAO;
import com.aspire.mirror.entity.IndicationAllItemEntity;
import com.aspire.mirror.entity.IndicationEntity;
import com.aspire.mirror.service.IIndicationDataService;
import com.aspire.mirror.util.EmailUtil;
import com.aspire.mirror.util.IndicationConst;
import com.aspire.real_mirror.service.IRealIndicationDataService;
import com.aspire.real_mirror.service.IRealMailService;
import com.aspire.real_mirror.util.MailConst;
import com.cmcc.family.alertagent.AlertAgent;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.*;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: RealMailServiceImpl
 * Author:   zhu.juwang
 * Date:     2019/11/7 14:48
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Service
@Slf4j
public class RealMailServiceImpl implements IRealMailService {
    @Autowired
    private EnvConfigProperties envConfigProperties;
    @Autowired
    private IIndicationDAO indicationDAO;
    @Autowired
    private IIndicationDataService indicationDataService;
    @Autowired
    private IRealIndicationDataService realIndicationDataService;

    @Override
    public void sendDayIndicationEmail(String indicationOwner, String indicationCatalog, String indicationPosition,
                                    String indicationFrequency, String calcDate) {
        calcDate = calcDate.replaceAll("[^0-9]","").trim();
        JSONObject exceptionData = realIndicationDataService.getDayIndicationMailData(indicationOwner, indicationCatalog, indicationPosition,
                indicationFrequency, calcDate);
        Iterator iterator = exceptionData.keySet().iterator();
        StringBuilder emailBuilder = new StringBuilder();
        int prefix = 1;
        while (iterator.hasNext()) {
            String key = iterator.next().toString();
            JSONObject countryData = exceptionData.getJSONObject(key).getJSONObject(IndicationConst.INDICATION_GROUP_COUNTRY);
            JSONObject provinceData = exceptionData.getJSONObject(key).getJSONObject(IndicationConst.INDICATION_GROUP_PROVINCE);
            //拼装发送邮件的格式
            emailBuilder.append(getEmailContent(prefix, key, countryData, provinceData));
            prefix ++;
        }
        //处理发送邮件
        String title = MailConst.REAL_MAIL_DAY_COUNTRY_TITLE_TEMPLATE;
        String model = MailConst.REAL_MAIL_COUNTRY_CONTENT_TEMPLATE;
        String tempTimeString = "";
        String pattern = "yyyy年MM月dd日";
        try {
            Date dateTime = DateUtils.parseDate(calcDate, new String[]{IndicationConst.QUERY_MONGODB_DATE_PATTERN});
            tempTimeString = DateFormatUtils.format(dateTime, pattern);
        } catch (ParseException e) {
            String errorMsg = String.format("Parse date error. -> date:%s -> pattern:%s", calcDate, IndicationConst.QUERY_MONGODB_DATE_PATTERN);
            log.error(errorMsg, e);
            throw new RuntimeException(errorMsg, e);
        }
        title = String.format(title,tempTimeString, indicationCatalog);
        model = String.format(model, tempTimeString, indicationCatalog, emailBuilder.toString());
        log.info("指标分类-> {} 时间-> {} 指标出现异常. 正在发送邮件...", indicationCatalog, tempTimeString);
        log.info("发送邮件标题-> {}", title);
        log.info("发送邮件内容-> {}", model);
        String topic = envConfigProperties.getRealNationalWeb().getAutoSendTopic();
        JSONObject returnJson = new JSONObject();
        returnJson.accumulate("title", title);
        returnJson.accumulate("model", model);
        returnJson.accumulate("alertAgent", envConfigProperties.getAlertAgent().getServer());
        returnJson.accumulate("topic", topic);
        returnJson.accumulate("needSendMail", StringUtils.isNotEmpty(emailBuilder.toString()));
        sendEmail(returnJson);
    }

    @Override
    public void sendHourIndicationEmail(List<IndicationEntity> indicationList, String calcDate) {
        calcDate = calcDate.replaceAll("[^0-9]","").trim();
        Map<String, List<JSONObject>> exceptionMap = indicationDataService.getRealHourAndActualExceptionDataForMail(indicationList, calcDate);
        StringBuilder emailBuilder = new StringBuilder();
        int prefix = 1;
        for (Map.Entry<String, List<JSONObject>> entry : exceptionMap.entrySet()) {
            String groupName = entry.getKey();
            //设置标题及阈值
            emailBuilder.append("<b>").append(prefix).append(". ").append(groupName).append("</b><br/>");
            List<JSONObject> dataList = entry.getValue();
            int loopIndex = 1;
            for (JSONObject data : dataList) {
                //设置子标题
                StringBuilder titleBuilder = new StringBuilder();
                String indicationName = data.getString("indicationName");
                titleBuilder.append("<b>").append(prefix).append(".").append(loopIndex).append(" ").append(indicationName)
                        .append(" ").append("（").append(data.getString("indicationUpdateFrequency")).append("）").append("</b><br/>");
                emailBuilder.append(titleBuilder);
                //设置阈值
                JSONObject limitObject = data.getJSONObject("limitEntity");
                emailBuilder.append(getLimitBuilder(data, limitObject));
                //设置表格内容
                JSONArray jsonArray = data.getJSONArray("data");
                emailBuilder.append(getTableBuilder(jsonArray));
                loopIndex ++;
            }
            prefix ++;
        }
        //处理发送邮件
        String tempTimeString = "";
        try {
            Date dateTime = DateUtils.parseDate(calcDate, new String[]{IndicationConst.QUERY_MONGODB_DATE_PATTERN_0});
            tempTimeString = DateFormatUtils.format(dateTime, "yyyy年MM月dd日HH时");
        } catch (ParseException e) {
            String errorMsg = String.format("Parse date error. -> date:%s -> pattern:%s", calcDate, IndicationConst.QUERY_MONGODB_DATE_PATTERN_0);
            log.error(errorMsg, e);
            throw new RuntimeException(errorMsg, e);
        }
        String title = String.format(MailConst.REAL_MAIL_COUNTRY_TITLE_TEMPLATE,tempTimeString, "机顶盒");
        String model = String.format(MailConst.REAL_MAIL_COUNTRY_CONTENT_TEMPLATE, tempTimeString, "机顶盒", emailBuilder.toString());
        log.info("指标分类-> {} 时间-> {} 指标出现异常. 正在发送邮件...", "机顶盒", tempTimeString);
        log.info("发送邮件标题-> {}", title);
        log.info("发送邮件内容-> {}", model);
        if (StringUtils.isNotBlank(emailBuilder.toString())) {
            final AlertAgent alert = new AlertAgent(envConfigProperties.getAlertAgent().getServer());
            alert.sendAlert(envConfigProperties.getRealNationalWeb().getAutoSendTopic(), title, model, false);
        }
    }

    /**
     * 发送邮件
     * @param emailObject
     */
    private void sendEmail(JSONObject emailObject) {
        String title = emailObject.getString("title");
        String model = emailObject.getString("model");
        String topic = emailObject.getString("topic");
        boolean needSend = emailObject.getBoolean("needSendMail");
        log.info("Email title -> {}", title);
        log.info("Email model -> {}", model);
        log.info("Email topic -> {}", topic);
        log.info("Email needSend -> {}", needSend);
        if (needSend && StringUtils.isNotBlank(model)) {
            final AlertAgent alert = new AlertAgent(envConfigProperties.getAlertAgent().getServer());
            alert.sendAlert(topic, title, model, false);
        } else {
            log.info("NeedSendMail is false, don't send alarm e-mail.");
        }
    }

    @Override
    public void sendMinuteIndicationMail(String indicationOwner, String catalogBox, String indicationPosition,
                                         String indicationFrequency, String date) {
        List<IndicationEntity> entityList = indicationDAO.getIndicationListByOwnerAndCatalogAndFrequencyAndGroup(indicationOwner, catalogBox,
                indicationFrequency, null, indicationPosition);
        for (IndicationEntity entity : entityList) {
            List<Map<String, String>> dataList = indicationDataService.getMinuteIndicationDataForMail(entity.getIndicationId(), date, date);
            String dateString = null;
            try {
                Date dateCN = DateUtils.parseDate(date, new String[] {IndicationConst.QUERY_MONGODB_DATE_PATTERN_2});
                dateString = DateFormatUtils.format(dateCN, IndicationConst.DATE_PATTERN_CN);
            } catch (ParseException e) {
                log.error("Parse date string {} error.", date, e);
            }
            for (Map<String, String> data : dataList) {
                JSONObject emailObject = new JSONObject();
                String title = "", model = "";
                //没有异常 判断是否发送恢复邮件
                if (StringUtils.isEmpty(data.get(IndicationConst.INDICATION_ITEM_NAME_EXCEPTION_REASON))) {
                    if(EmailUtil.isRestoreAlarm(entity.getIndicationId().toString(), date)) {
                        title = String.format(MailConst.KADUN_MINUTE_COUNTRY_RESTORE_TITLE_TEMPLATE, dateString);
                        model = String.format(MailConst.KADUN_MINUTE_COUNTRY_RESTORE_CONTENT_TEMPLATE, dateString);
                    }
                } else {
                    if (EmailUtil.isContinueAlarm(entity.getIndicationId().toString(), date)) {
                        JSONArray list = new JSONArray();
                        list.add(data);
                        title = String.format(MailConst.KADUN_MINUTE_COUNTRY_TITLE_TEMPLATE, dateString);
                        model = String.format(MailConst.KADUN_MINUTE_COUNTRY_CONTENT_TEMPLATE, dateString, getTableBuilder(list).toString());
                    }
                }
                emailObject.accumulate("title", title);
                emailObject.accumulate("model", model);
                emailObject.accumulate("alertAgent", envConfigProperties.getAlertAgent().getServer());
                emailObject.accumulate("topic", envConfigProperties.getRealNationalWeb().getAutoSendTopic());
                emailObject.accumulate("needSendMail", StringUtils.isNotEmpty(model));
                sendEmail(emailObject);
            }
        }
    }

    private StringBuilder getEmailContent(int prefix, String indicationTopic, JSONObject countryData, JSONObject provinceData) {
        StringBuilder contentBuilder = new StringBuilder();
        if (countryData.size() == 0 && provinceData.size() == 0) {
            return contentBuilder;
        }
        int index = 1;
        JSONArray dataArray;
        if (countryData.size() > 0) {
            JSONObject indication = countryData.getJSONObject("indication");
            dataArray = countryData.getJSONArray("data");
            StringBuilder titleBuilder = new StringBuilder();
            if (StringUtils.isNotEmpty(indicationTopic)) {
                titleBuilder.append("<b>").append(prefix).append(". ").append(indicationTopic).append("</b><br/>");
            }
            //指标标题
            titleBuilder.append("<b>").append(prefix).append(".").append(index).append(" ").append(indication.getString("indicationName")).append("</b><br/>");
            contentBuilder.append(titleBuilder);
            //设置阈值
            if (indication.get("limitEntity") != null) {
                contentBuilder.append(getLimitBuilder(indication, indication.getJSONObject("limitEntity")));
            }
            //设置邮件内容
            JSONArray newArray = null;
            if (dataArray.size() > 0) {
                //由于返回的是全国14天的数据, 所以只能取当天的数据.
                JSONObject firstCountry = dataArray.getJSONObject(0);
                newArray = new JSONArray();
                if (firstCountry.containsKey(IndicationConst.INDICATION_ITEM_NAME_EXCEPTION_REASON) &&
                        StringUtils.isNotEmpty(firstCountry.getString(IndicationConst.INDICATION_ITEM_NAME_EXCEPTION_REASON))) {
                    newArray.add(firstCountry);
                }
            }
            contentBuilder.append(getTableBuilder(newArray));
            index ++;
        }
        if (provinceData != null && provinceData.size() >0) {
            JSONObject indication = provinceData.getJSONObject("indication");
            dataArray = provinceData.getJSONArray("data");
            StringBuilder titleBuilder = new StringBuilder();
            if (countryData.size() == 0) {
                titleBuilder.append("<b>").append(prefix).append(". ").append(indicationTopic).append("</b><br/>");
            }
            //指标标题
            titleBuilder.append("<b>").append(prefix).append(".").append(index).append(" ").append(indication.getString("indicationName")).append("</b><br/>");
            contentBuilder.append(titleBuilder);
            //设置阈值
            if (indication.get("limitEntity") != null) {
                contentBuilder.append(getLimitBuilder(indication, indication.getJSONObject("limitEntity")));
            }
            //设置邮件内容
            contentBuilder.append(getTableBuilder(dataArray));
        }
        return contentBuilder;
    }

    private StringBuilder getLimitBuilder(JSONObject indicationData , JSONObject limitObject) {
        StringBuilder limitBuilder = new StringBuilder();
        if (limitObject == null || limitObject.size() == 0) {
            return limitBuilder;
        }
        String max = formatLimit(limitObject.getString("maxValue"), indicationData.getString("indicationUnit"));
        String min = formatLimit(limitObject.getString("minValue"), indicationData.getString("indicationUnit"));
        String changeValueMax = formatLimit(limitObject.getString("changeValueMax"), indicationData.getString("indicationUnit"));
        String changeValueMin = formatLimit(limitObject.getString("changeValueMin"), indicationData.getString("indicationUnit"));
        String changeRateMax = formatLimit(limitObject.getString("changeRateMax"), "%");
        String changeRateMin = formatLimit(limitObject.getString("changeRateMin"), "%");
        String limitName = indicationData.getString("indicationName");
        if (limitName.startsWith(IndicationConst.INDICATION_GROUP_COUNTRY) ||
                limitName.startsWith(IndicationConst.INDICATION_GROUP_PROVINCE)) {
            limitName = limitName.substring(2);
        }
        limitBuilder.append("<span style='font-size:11px;'>(").append(limitName).append("--")
                .append("上限：").append(max).append(",")
                .append("下限：").append(min).append(",")
                .append("变动值上限：").append(changeValueMax).append(",")
                .append("变动值下限：").append(changeValueMin).append(",")
                .append("变动率上限：").append(changeRateMax).append(",")
                .append("变动率下限：").append(changeRateMin).append(")</span>").append("<br/>");
        return limitBuilder;
    }

    private StringBuilder getTableBuilder(JSONArray dataArray) {
        StringBuilder tableBuilder = new StringBuilder();
        if (dataArray == null || dataArray.size() == 0) {
            tableBuilder.append("无异常数据<br/><br/>");
            return tableBuilder;
        }
        String thTemplate = "<th style='border: 1px solid #000; text-align:center; font-size:13px'>%s</th>";
        String tdTemplate = "<td style='border: 1px solid #000; text-align:center; font-size:13px'>%s</td>";
        tableBuilder.append("<table style='border-collapse:collapse; width:100%;'>");
        //设置表头
        StringBuilder thBuilder = new StringBuilder();
        Set keySet = dataArray.getJSONObject(0).keySet();
        for (Object thKey : keySet) {
            thBuilder.append(String.format(thTemplate, thKey));
        }
        tableBuilder.append("<tr>").append(thBuilder).append("</tr>");
        //设置正文
        for (Object data : dataArray) {
            JSONObject trData = JSONObject.fromObject(data);
            StringBuilder trBuilder = new StringBuilder();
            for (Object trKey : keySet) {
                Object value = trData.get(trKey);
                //处理日期显示
                if (trKey.equals(IndicationConst.INDICATION_ITEM_NAME_CALC_DATE)) {
                    String temp = value.toString();
                    if (temp.length() == 8) {
                        value = temp.substring(0,4) + "-" + temp.substring(4,6) + "-" + temp.substring(6,8);
                    }
                    if (temp.length() == 10) {
                        value = temp.substring(0,4) + "-" + temp.substring(4,6) + "-" + temp.substring(6,8) + " "
                                + temp.substring(8,10) + ":00:00";
                    }
                    if (temp.length() == 12) {
                        value = temp.substring(0,4) + "-" + temp.substring(4,6) + "-" + temp.substring(6,8) + " "
                                + temp.substring(8,10) + ":" + temp.substring(10,12) + ":00";
                    }
                    if (temp.length() == 14) {
                        value = temp.substring(0,4) + "-" + temp.substring(4,6) + "-" + temp.substring(6,8) + " "
                                + temp.substring(8,10) + ":" + temp.substring(10,12) + ":" + temp.substring(12,14);
                    }
                }
                trBuilder.append(String.format(tdTemplate, value));
            }
            tableBuilder.append("<tr>").append(trBuilder).append("</tr>");
        }
        tableBuilder.append("</table>").append("<br/>");
        return tableBuilder;
    }

    private String formatLimit(String limitValue, String indicationUnit) {
        if (StringUtils.isEmpty(limitValue)) {
            return "NaN" + indicationUnit;
        }
        if (limitValue.equalsIgnoreCase("nan")) {
            return "NaN" + indicationUnit;
        }
        return limitValue + indicationUnit;
    }
}
