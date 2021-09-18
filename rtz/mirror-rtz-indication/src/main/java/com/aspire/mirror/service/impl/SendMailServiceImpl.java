package com.aspire.mirror.service.impl;

import com.aspire.common.EnvConfigProperties;
import com.aspire.common.HttpUtil;
import com.aspire.mirror.entity.IndicationEntity;
import com.aspire.mirror.service.IIndicationDataService;
import com.aspire.mirror.service.ISendMailService;
import com.aspire.mirror.util.EmailUtil;
import com.aspire.mirror.util.IndicationConst;
import com.aspire.mirror.util.TimeUtil;
import com.aspire.mirror.util.XMLUtil;
import com.cmcc.family.alertagent.AlertAgent;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.*;

@Slf4j
@Service
public class SendMailServiceImpl implements ISendMailService {

    @Autowired
    private EnvConfigProperties envConfigProperties;
    @Autowired
    private IIndicationDataService indicationDataService;

    @Override
    public void sendIndicationEmail(String indicationOwner, String indicationCatalog, String indicationPosition,
                                    String indicationFrequency, String calcDate) {
        JSONObject jsonObject = getSendEmailData(indicationOwner, indicationCatalog, indicationPosition, indicationFrequency,calcDate);
        sendEmail(indicationOwner, indicationCatalog, indicationPosition, indicationFrequency, calcDate, jsonObject);
    }

    @Override
    public void sendHourIndicationEmail(List<IndicationEntity> indicationList, String calcDate) {
        calcDate = calcDate.replaceAll("[^0-9]","").trim();
        int calcDateLength = calcDate.length();
        //把时间格式补成年月日时分秒
        for (int i=1; i <= 14 - calcDateLength; i++) {
            calcDate += "0";
        }
        Map<String, List<JSONObject>> exceptionMap = indicationDataService.getExceptionDataForMail(indicationList, calcDate, null);
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
                        .append(" ").append("（").append(data.getString("indicationFrequency")).append("）").append("</b><br/>");
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
            Date dateTime = DateUtils.parseDate(calcDate, new String[]{IndicationConst.DATE_PATTERN});
            tempTimeString = DateFormatUtils.format(dateTime, "yyyy年MM月dd日HH时");
        } catch (ParseException e) {
            String errorMsg = String.format("Parse date error. -> date:%s -> pattern:%s", calcDate, IndicationConst.DATE_PATTERN);
            log.error(errorMsg, e);
            throw new RuntimeException(errorMsg, e);
        }
        String title = String.format(IndicationConst.REAL_MAIL_COUNTRY_TITLE_TEMPLATE,tempTimeString, "机顶盒");
        String model = String.format(IndicationConst.REAL_MAIL_COUNTRY_CONTENT_TEMPLATE, tempTimeString, "机顶盒", emailBuilder.toString());
        log.info("指标分类-> {} 时间-> {} 指标出现异常. 正在发送邮件...", "机顶盒", tempTimeString);
        log.info("发送邮件标题-> {}", title);
        log.info("发送邮件内容-> {}", model);
        if (StringUtils.isNotBlank(emailBuilder.toString())) {
            final AlertAgent alert = new AlertAgent(envConfigProperties.getAlertAgent().getServer());
            alert.sendAlert(envConfigProperties.getRealNationalWeb().getAutoSendTopic(), title, model, true);
        }
    }

    /**
     * 发送邮件
     * @param emailObject
     */
    private void sendEmail(String indicationOwner, String indicationCatalog, String indicationPosition,
                            String indicationFrequency, String calcDate, JSONObject emailObject) {
        String title = emailObject.getString("title");
        String model = emailObject.getString("model");
        String topic = emailObject.getString("topic");
        boolean needSend = emailObject.getBoolean("needSendMail");
        log.info("Email info -> {}", emailObject.toString());
        if (needSend && StringUtils.isNotBlank(model)) {
            final AlertAgent alert = new AlertAgent(envConfigProperties.getAlertAgent().getServer());
            alert.sendAlert(topic, title, model, true);
        } else {
            log.info("NeedSendMail is false, don't send alarm e-mail.");
        }
    }

    @Override
    public JSONObject getSendEmailData(String indicationOwner, String indicationCatalog, String indicationPosition,
                                       String indicationFrequency, String calcDate) {
        calcDate = calcDate.replaceAll("[^0-9]","").trim();
        int calcDateLength = calcDate.length();
        //把时间格式补成年月日时分秒
        for (int i=1; i <= 14 - calcDateLength; i++) {
            calcDate += "0";
        }
        JSONObject exceptionData = indicationDataService.getIndicationData(indicationOwner, indicationCatalog,indicationPosition,
                indicationFrequency, calcDate, null);
        JSONArray provinceException = exceptionData.getJSONArray(IndicationConst.EXCEPTION_GROUP_PROVINCE_EXCEPTION);
        JSONArray countryException = exceptionData.getJSONArray(IndicationConst.EXCEPTION_GROUP_PROVINCE_COUNTRY);
        List<String> existsItems = new ArrayList<String>();
        int prefix = 1;
        StringBuilder emailBuilder = new StringBuilder();
        //处理全国指标
        JSONArray countryIndicationArray = new JSONArray();
        if (countryException != null && countryException.size() > 0) {
            countryIndicationArray = countryException.getJSONArray(0);
        }
        for (Object obj : countryIndicationArray) {
            JSONObject countryData = JSONObject.fromObject(obj);
            String indicationId = countryData.getString("indicationId");
            String indicationName = countryData.getString("indicationName");
            //String indicationGroup = countryData.getString("indicationGroup");
            IndicationEntity provinceIndicationEntity = XMLUtil.getEntityByName(IndicationConst.INDICATION_GROUP_PROVINCE,
                    IndicationConst.INDICATION_GROUP_PROVINCE + indicationName.substring(2));
            JSONObject provinceData = new JSONObject();
            if (provinceIndicationEntity != null) {
                for (Object provinceObj : provinceException) {
                    JSONArray indicationArray = JSONArray.fromObject(provinceObj);
                    for (Object indicationObj : indicationArray) {
                        JSONObject indicationDataJSON = JSONObject.fromObject(indicationObj);
                        if (indicationDataJSON.getString("indicationId").equals(provinceIndicationEntity.getIndicationId())) {
                            provinceData = indicationDataJSON;
                            break;
                        }
                    }
                }
            }
            //拼装发送邮件的格式
            emailBuilder.append(getEmailContent(prefix, countryData, provinceData));
            //存储已经处理过的指标ID
            existsItems.add(indicationId);
            if (provinceIndicationEntity != null) {
                existsItems.add(provinceIndicationEntity.getIndicationId());
            }
            prefix ++;
        }
        //处理各省指标
        for (Object obj : provinceException) {
            JSONArray objArray = JSONArray.fromObject(obj);
            for (Object indicationException : objArray) {
                JSONObject provinceData = JSONObject.fromObject(indicationException);
                String indicationId = provinceData.getString("indicationId");
                //已经作为全国指标的子指标处理, 这里跳过
                if (existsItems.contains(indicationId)) {
                    continue;
                }
                //拼装发送邮件的格式
                emailBuilder.append(getEmailContent(prefix, null, provinceData));
                //存储已经处理过的指标ID
                existsItems.add(indicationId);
                prefix ++;
            }
        }
        String title = null, model = null;
        //处理发送邮件
        if (IndicationConst.INDICATION_OWNER_REAL.equals(indicationOwner)) {
            if (IndicationConst.INDICATION_FREQUENCY_DAY.equals(indicationFrequency)) {
                title = IndicationConst.REAL_MAIL_DAY_COUNTRY_TITLE_TEMPLATE;
                model = IndicationConst.REAL_MAIL_COUNTRY_CONTENT_TEMPLATE;
            }
            if (IndicationConst.INDICATION_FREQUENCY_ACTUAL.equals(indicationFrequency)) {
                title = IndicationConst.REAL_MAIL_COUNTRY_TITLE_TEMPLATE;
                model = IndicationConst.REAL_MAIL_COUNTRY_CONTENT_TEMPLATE;
            }
            if (IndicationConst.INDICATION_FREQUENCY_HOUR.equals(indicationFrequency)) {
                title = IndicationConst.REAL_MAIL_COUNTRY_TITLE_TEMPLATE;
                model = IndicationConst.REAL_MAIL_COUNTRY_CONTENT_TEMPLATE;
            }
        }
        String tempTimeString = "";
        String pattern = "yyyy年MM月dd日";
        if (IndicationConst.INDICATION_FREQUENCY_ACTUAL.equals(indicationFrequency)
                || IndicationConst.INDICATION_FREQUENCY_HOUR.equals(indicationFrequency)) {
            pattern = "yyyy年MM月dd日HH时";
        }
        if (IndicationConst.INDICATION_FREQUENCY_DAY.equals(indicationFrequency)) {
            pattern = "yyyy年MM月dd日";
        }
        if (IndicationConst.INDICATION_FREQUENCY_MINUTE.equals(indicationFrequency)) {
            pattern = "yyyy年MM月dd日HH时mm分";
        }
        try {
            Date dateTime = DateUtils.parseDate(calcDate, new String[]{IndicationConst.DATE_PATTERN});
            tempTimeString = DateFormatUtils.format(dateTime, pattern);
        } catch (ParseException e) {
            String errorMsg = String.format("Parse date error. -> date:%s -> pattern:%s", calcDate, IndicationConst.DATE_PATTERN);
            log.error(errorMsg, e);
            throw new RuntimeException(errorMsg, e);
        }
        title = String.format(title,tempTimeString, indicationCatalog);
        model = String.format(model, tempTimeString, indicationCatalog, emailBuilder.toString());
        log.info("指标分类-> {} 时间-> {} 指标出现异常. 正在发送邮件...", indicationCatalog, tempTimeString);
        log.info("发送邮件标题-> {}", title);
        log.info("发送邮件内容-> {}", model);
        String topic = "";
        if (IndicationConst.INDICATION_OWNER_REAL.equals(indicationOwner)) {
            topic = envConfigProperties.getRealNationalWeb().getAutoSendTopic();
        }
        if (IndicationConst.INDICATION_OWNER_ALL.equals(indicationOwner)) {
            topic = envConfigProperties.getNationalWeb().getAutoSendTopic();
        }
        JSONObject returnJson = new JSONObject();
        returnJson.accumulate("title", title);
        returnJson.accumulate("model", model);
        returnJson.accumulate("alertAgent", envConfigProperties.getAlertAgent().getServer());
        returnJson.accumulate("topic", topic);
        returnJson.accumulate("needSendMail", StringUtils.isNotEmpty(emailBuilder.toString()));
        return returnJson;
    }

    @Override
    public void sendMinuteIndicationMail(String indicationOwner, String catalogBox, String indicationPosition,
                                         String indicationFrequency, String date) {
        List<IndicationEntity> entityList = XMLUtil.getIndicationList(indicationOwner, catalogBox, null, indicationFrequency);
        for (IndicationEntity entity : entityList) {
            String interfaceUrl = envConfigProperties.getInterFace().getIndicationResult() + entity.getIndicationId();
            JSONObject params = new JSONObject();
            params.accumulate("start_time", date);
            params.accumulate("end_time", date);
            Object result = HttpUtil.getMethod(interfaceUrl, params);
            if (result == null) {
                continue;
            }
            JSONArray resultArray = JSONArray.fromObject(result);
            String dateString = TimeUtil.getFormatDayCN(date);
            for (Object data : resultArray) {
                JSONObject jsonData = JSONObject.fromObject(data);
                JSONObject emailObject = new JSONObject();
                String title = "", model = "";
                //没有异常 判断是否发送恢复邮件
                if (StringUtils.isEmpty(jsonData.getString(IndicationConst.INDICATION_ITEM_NAME_EXCEPTION_REASON))) {
                    if(EmailUtil.isRestoreAlarm(entity.getIndicationId(), date)) {
                        title = String.format(IndicationConst.KADUN_MINUTE_COUNTRY_RESTORE_TITLE_TEMPLATE, dateString);
                        model = String.format(IndicationConst.KADUN_MINUTE_COUNTRY_RESTORE_CONTENT_TEMPLATE, dateString);
                    }
                } else {
                    if (!EmailUtil.isContinueAlarm(entity.getIndicationId(), date)) {
                        JSONArray list = new JSONArray();
                        list.add(jsonData);
                        title = String.format(IndicationConst.KADUN_MINUTE_COUNTRY_TITLE_TEMPLATE, dateString);
                        model = String.format(IndicationConst.KADUN_MINUTE_COUNTRY_CONTENT_TEMPLATE, dateString, getTableBuilder(list).toString());
                    }
                }
                emailObject.accumulate("title", title);
                emailObject.accumulate("model", model);
                emailObject.accumulate("alertAgent", envConfigProperties.getAlertAgent().getServer());
                emailObject.accumulate("topic", envConfigProperties.getRealNationalWeb().getAutoSendTopic());
                emailObject.accumulate("needSendMail", true);
                sendEmail(indicationOwner, catalogBox, indicationPosition, indicationFrequency, date, emailObject);
            }
        }
    }

    private StringBuilder getEmailContent(int prefix, JSONObject countryData, JSONObject provinceData) {
        StringBuilder contentBuilder = new StringBuilder();
        if ((countryData == null || countryData.size() ==0) && (provinceData == null || provinceData.size() ==0 )) {
            return contentBuilder;
        }
        int index = 1;
        String indicationTopic;
        String indicationName;
        JSONObject limitObject;
        JSONArray dataArray;
        if (countryData != null && countryData.size() > 0) {
            indicationName = countryData.getString("indicationName");
            indicationTopic = indicationName.substring(2);
            limitObject = countryData.getJSONObject("limit");
            dataArray = countryData.getJSONArray("data");
            StringBuilder titleBuilder = new StringBuilder();
            if (StringUtils.isNotEmpty(indicationTopic)) {
                titleBuilder.append("<b>").append(prefix).append(". ").append(indicationTopic).append("</b><br/>");
            }
            //指标标题
            titleBuilder.append("<b>").append(prefix).append(".").append(index).append(" ").append(indicationName).append("</b><br/>");
            contentBuilder.append(titleBuilder);
            //设置阈值
            if (limitObject != null) {
                contentBuilder.append(getLimitBuilder(countryData, limitObject));
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
            indicationName = provinceData.getString("indicationName");
            indicationTopic = indicationName.substring(2);
            limitObject = provinceData.getJSONObject("limit");
            dataArray = provinceData.getJSONArray("data");
            StringBuilder titleBuilder = new StringBuilder();
            if (countryData == null || countryData.size() == 0) {
                titleBuilder.append("<b>").append(prefix).append(". ").append(indicationTopic).append("</b><br/>");
            }
            //指标标题
            titleBuilder.append("<b>").append(prefix).append(".").append(index).append(" ").append(indicationName).append("</b><br/>");
            contentBuilder.append(titleBuilder);
            //设置阈值
            if (limitObject != null) {
                contentBuilder.append(getLimitBuilder(provinceData, limitObject));
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
        String indicationId = indicationData.getString("indicationId");
        IndicationEntity entity = XMLUtil.getEntityById(indicationId);
        String max = formatLimit(limitObject.getString("maxValue"), entity.getIndicationUnit());
        String min = formatLimit(limitObject.getString("minValue"), entity.getIndicationUnit());
        String changeValueMax = formatLimit(limitObject.getString("changeValueMax"), entity.getIndicationUnit());
        String changeValueMin = formatLimit(limitObject.getString("changeValueMin"), entity.getIndicationUnit());
        String changeRateMax = formatLimit(limitObject.getString("changeRateMax"), "%");
        String changeRateMin = formatLimit(limitObject.getString("changeRateMin"), "%");
        String limitName = entity.getIndicationName();
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
