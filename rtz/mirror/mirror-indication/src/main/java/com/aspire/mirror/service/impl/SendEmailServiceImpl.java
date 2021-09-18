package com.aspire.mirror.service.impl;

import com.aspire.common.EnvConfigProperties;
import com.aspire.mirror.bean.IndicationLimitEntity;
import com.aspire.mirror.entity.IndicationEntity;
import com.aspire.mirror.entity.IndicationProvinceEntity;
import com.aspire.mirror.service.IIndicationDataService;
import com.aspire.mirror.service.IIndicationService;
import com.aspire.mirror.service.SendEmailService;
import com.aspire.mirror.util.IndicationConst;
import com.aspire.mirror.util.IndicationUtils;
import com.cmcc.family.alertagent.AlertAgent;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@Slf4j
public class SendEmailServiceImpl implements SendEmailService {

    @Autowired
    private IIndicationDataService dataService;
    @Autowired
    private IIndicationService indicationService;
    @Autowired
    private EnvConfigProperties envConfigProperties;

    @Override
    public String autoSendEmail(String indicationOwner, String indicationCatalog, String indicationCycle,
                                String indicationFrequency, String dateTime) {
        //获取异常数据
        JSONObject exceptionData = dataService.getExceptionData(indicationOwner, indicationCatalog,
                indicationCycle, indicationFrequency, dateTime, null, null, null);
        String result = sendEmail(indicationOwner, exceptionData,dateTime,indicationCatalog);
        return String.format("[%s]平台-[%s]-邮件发送结果:%s", indicationOwner,indicationCatalog, result);
    }

    /**
     * 发送双送平台实时指标邮件提醒
     * @param catalogBox 指标分类
     * @param indicationFrequency 指标更新频率
     * @param calcDate 计算时间
     */
    @Override
    public void sendRealActualEmail(String catalogBox, String indicationFrequency, String calcDate) {
        sendRealActualCountry(catalogBox, indicationFrequency, calcDate);
        sendRealActualProvince(catalogBox, indicationFrequency, calcDate);
    }

    /**
     * 发送给研究院的邮件
     * @return
     */
    private void sendRealActualCountry(String catalogBox, String indicationFrequency, String calcDate) {
        List<IndicationEntity> indicationList = indicationService.getIndicationListByOwnerAndCatalogAndFrequencyAndGroup(
                IndicationConst.INDICATION_OWNER_REAL, IndicationConst.CATALOG_BOX,
                IndicationConst.INDICATION_FREQUENCY_ACTUAL, null, null);
        List<Integer> alreadyHandler = new ArrayList<Integer>();
        StringBuilder emailBuilder = new StringBuilder();
        int index = 1;
        for (IndicationEntity entity : indicationList) {
            if (alreadyHandler.contains(entity.getIndicationId())) {
                continue;
            }
            int countryIndicationId = 0, provinceIndicationId = 0;
            IndicationEntity countryEntity = null, provinceEntity = null;
            //判断当前指标是否是全国指标, 如果全国指标则同时加载他的省份指标
            if (entity.getIndicationGroup().equals(IndicationConst.INDICATION_GROUP_COUNTRY)) {
                countryIndicationId = entity.getIndicationId();
                countryEntity = entity;
                if (StringUtils.isNotEmpty(entity.getIndicationAlias())) {
                    provinceIndicationId = Integer.parseInt(entity.getIndicationAlias());
                    provinceEntity = indicationService.getIndicationByIndicationId(provinceIndicationId);
                }
            }
            if (entity.getIndicationGroup().equals(IndicationConst.INDICATION_GROUP_PROVINCE)) {
                provinceIndicationId = entity.getIndicationId();
                provinceEntity = entity;
                if (StringUtils.isNotEmpty(entity.getIndicationAlias())) {
                    countryIndicationId = Integer.parseInt(entity.getIndicationAlias());
                    countryEntity = indicationService.getIndicationByIndicationId(countryIndicationId);
                }
            }
            alreadyHandler.add(countryIndicationId);
            alreadyHandler.add(provinceIndicationId);
            int innerIndex = 1;
            StringBuilder titleBuilder = new StringBuilder();
            //指标标题
            String title = entity.getIndicationName();
            if (countryIndicationId > 0 && provinceIndicationId > 0) {
                if (title.startsWith(IndicationConst.INDICATION_GROUP_COUNTRY) || title.startsWith(IndicationConst.INDICATION_GROUP_PROVINCE)) {
                    title = title.substring(2);
                }
                if (title.contains("（")) {
                    title = title.substring(0, title.indexOf("（"));
                }
                titleBuilder.append("<b>").append(index).append(". ").append(title).append("</b><br/>");
            }
            if (countryEntity != null) {
                titleBuilder.append("<b>").append(index).append(".").append(innerIndex).append(" ").append(countryEntity.getIndicationName()).append("</b><br/>");
                //添加阈值栏
                titleBuilder.append(getEmailLimitBuilder(countryEntity));
                //获取全国的计算数据
                List<Map<String, String>> countryData = dataService.getExceptionDataByIndicationIdAndProvinceCode(calcDate,
                        null, countryIndicationId, countryEntity.getIndicationId().toString());
                //表格标题及内容
                titleBuilder.append(getEmailTableBuilder(countryEntity, countryData));
                innerIndex ++ ;
            }
            if (provinceEntity != null) {
                titleBuilder.append("<b>").append(index).append(".").append(innerIndex).append(" ").append(provinceEntity.getIndicationName()).append("</b><br/>");
                //添加阈值栏
                titleBuilder.append(getEmailLimitBuilder(provinceEntity));
                //获取省份的计算数据
                List<Map<String, String>> provinceData = dataService.getExceptionDataByIndicationIdAndProvinceCode(calcDate,
                        null, provinceIndicationId, provinceEntity.getIndicationId().toString());
                //表格标题及内容
                titleBuilder.append(getEmailTableBuilder(provinceEntity, provinceData));
            }
            emailBuilder.append(titleBuilder);
            index++;
        }
        String date = calcDate.substring(0,8);
        String hour = calcDate.substring(8,10);
        hour = hour.startsWith("0") ? hour.substring(1,2) : hour;
        calcDate = date + "日" + hour + "时";
        String title = IndicationConst.REAL_HOUR_MAIL_COUNTRY_TITLE_TEMPLATE;
        String model = IndicationConst.REAL_HOUR_MAIL_COUNTRY_CONTENT_TEMPLATE;
        title = String.format(title, calcDate, catalogBox);
        model = String.format(model, calcDate, catalogBox, emailBuilder.toString());
        log.info("公司->全国 时间->{} 指标出现异常. 正在发送邮件...", calcDate);
        log.info("发送邮件标题-> {}", title);
        log.info("发送邮件内容-> {}", model);
        final AlertAgent alert = new AlertAgent(envConfigProperties.getAlertAgent().getServer());
        alert.sendAlert(envConfigProperties.getRealNationalWeb().getAutoSendTopic(), title, model, false);
    }

    /**
     * 发送给研究院的邮件
     * @return
     */
    private void sendRealActualProvince(String catalogBox, String indicationFrequency, String calcDate) {
        List<IndicationEntity> indicationList = indicationService.getIndicationListByOwnerAndCatalogAndFrequencyAndGroup(
                IndicationConst.INDICATION_OWNER_REAL, IndicationConst.CATALOG_BOX,
                IndicationConst.INDICATION_FREQUENCY_ACTUAL, IndicationConst.INDICATION_GROUP_PROVINCE, null);
        String date = calcDate.substring(0,8);
        String hour = calcDate.substring(8,10);
        hour = hour.startsWith("0") ? hour.substring(1,2) : hour;
        String tempTimeString = date + "日" + hour + "时";
        for (IndicationProvinceEntity provinceEntity : IndicationUtils.getProvinceList()) {
            StringBuilder emailBuilder = new StringBuilder();
            int index = 1;
            for (IndicationEntity entity : indicationList) {
                if (provinceEntity.getProvinceCode().equalsIgnoreCase(IndicationConst.COUNTRY_PROVINCE_CODE)) {
                    continue;
                }
                //所有省份的计算数据
                List<Map<String, String>> provinceData = dataService.getExceptionDataByIndicationIdAndProvinceCode(calcDate,
                        provinceEntity.getProvinceCode(), entity.getIndicationId(), entity.getIndicationId().toString());
                boolean provinceHasException = false;
                List<Map<String, String>> newDataList = new ArrayList<Map<String, String>>();
                //判断该省份是否有异常数据
                for (Map<String, String> dataMap : provinceData) {
                    if (StringUtils.isNotEmpty(dataMap.get(IndicationConst.INDICATION_ITEM_NAME_EXCEPTION_REASON))) {
                        provinceHasException = true;
                        newDataList.add(dataMap);
                        break;
                    }
                }
                if (!provinceHasException) {
                    continue;
                }
                StringBuilder titleBuilder = new StringBuilder();
                //指标标题
                titleBuilder.append("<b>").append(index).append(" ").append(entity.getIndicationName()).append("</b><br/>");
                //添加阈值栏
                titleBuilder.append(getEmailLimitBuilder(entity));
                //表格标题及内容
                titleBuilder.append(getEmailTableBuilder(entity, newDataList));
                emailBuilder.append(titleBuilder);
                index++;
            }
            //如果邮件内容不为空, 则发送邮件
            if (StringUtils.isNotEmpty(emailBuilder.toString())) {
                String title = IndicationConst.REAL_HOUR_MAIL_PROVINCE_TITLE_TEMPLATE;
                String model = IndicationConst.REAL_HOUR_MAIL_PROVINCE_CONTENT_TEMPLATE;
                title = String.format(title, provinceEntity.getProvinceName(),tempTimeString, catalogBox);
                model = String.format(model, provinceEntity.getProvinceName(),tempTimeString, catalogBox, emailBuilder.toString());
                log.info("公司->{} 时间->{} 指标出现异常. 正在发送邮件...", provinceEntity.getProvinceName(), tempTimeString);
                log.info("发送邮件标题-> {}", title);
                log.info("发送邮件内容-> {}", model);
                final AlertAgent alert = new AlertAgent(envConfigProperties.getAlertAgent().getServer());
                alert.sendAlert(envConfigProperties.getRealNationalWeb().getAutoSendTopic(), title, model, false);
            }
        }
    }

    private StringBuilder getEmailLimitBuilder(IndicationEntity entity) {
        StringBuilder limitBuilder = new StringBuilder();
        //全国指标表头部分
        IndicationLimitEntity limitEntity = entity.getLimitEntity();
        String max = formatLimit(limitEntity.getMaxValue(), entity.getIndicationUnit());
        String min = formatLimit(limitEntity.getMinValue(), entity.getIndicationUnit());
        String changeValueMax = formatLimit(limitEntity.getChangeValueMax(), entity.getIndicationUnit());
        String changeValueMin = formatLimit(limitEntity.getChangeValueMin(), entity.getIndicationUnit());
        String changeRateMax = formatLimit(limitEntity.getChangeRateMax(), "%");
        String changeRateMin = formatLimit(limitEntity.getChangeRateMin(), "%");
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

    private StringBuilder getEmailTableBuilder(IndicationEntity entity, List<Map<String, String>> dataList) {
        StringBuilder tableBuilder = new StringBuilder();
        boolean hasException = false;
        if (dataList.size() == 0) {
            tableBuilder.append("无异常数据");
            return tableBuilder;
        }
        String thTemplate = "<th style='border: 1px solid #000; text-align:center; font-size:13px'>%s</th>";
        String tdTemplate = "<td style='border: 1px solid #000; text-align:center; font-size:13px'>%s</td>";
        tableBuilder.append("<table style='border-collapse:collapse; width:100%;'>");
        //设置表头
        StringBuilder thBuilder = new StringBuilder();
        Set<String> keySet = dataList.get(0).keySet();
        for (String thKey : keySet) {
            thBuilder.append(String.format(thTemplate, thKey));
        }
        tableBuilder.append("<tr>").append(thBuilder).append("</tr>");
        //设置正文
        for (Map<String,String> trData : dataList) {
            if (StringUtils.isNotEmpty(trData.get(IndicationConst.INDICATION_ITEM_NAME_EXCEPTION_REASON))) {
                StringBuilder trBuilder = new StringBuilder();
                for (String trKey : keySet) {
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
                hasException = true;
            }
        }
        tableBuilder.append("</table>").append("<br/>");
        if (!hasException) {
            tableBuilder = new StringBuilder();
            tableBuilder.append("无异常数据").append("<br/><br/>");
        }
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

    /**
     * 发送邮件
     * @param exceptionData
     * @param dateTime
     * @param indicationCatalog
     */
    private String sendEmail(String indicationOwner, JSONObject exceptionData, String dateTime, String indicationCatalog) {
        log.info("开始发送{}-{}-{}邮件...", indicationOwner, indicationCatalog, dateTime);
        StringBuffer emailData = getEmailIndicationBuffer(exceptionData);
        log.info("需要发送的邮件数据:{}", emailData.toString());
        if (StringUtils.isNotBlank(emailData.toString())) {
            //拼装省份邮件
            String title = IndicationConst.MAIL_COUNTRY_TITLE_TEMPLATE;
            String model = IndicationConst.MAIL_COUNTRY_CONTENT_TEMPLATE;
            if (IndicationConst.INDICATION_OWNER_REAL.equals(indicationOwner)) {
                String date = dateTime.substring(0,8);
                String hour = dateTime.substring(8,10);
                hour = hour.startsWith("0") ? hour.substring(1,2) : hour;
                dateTime = date + "日" + hour + "时";
                title = IndicationConst.REAL_MAIL_COUNTRY_TITLE_TEMPLATE;
                model = IndicationConst.REAL_MAIL_COUNTRY_CONTENT_TEMPLATE;
            }
            title = String.format(title, dateTime, indicationCatalog);
            model = String.format(model, dateTime, indicationCatalog, emailData.toString());
            log.info("由kafka将信息通过Email发送  请求 ---> 开始");
            log.info("发送邮件内容:"+model);
            final AlertAgent alert = new AlertAgent(envConfigProperties.getAlertAgent().getServer());
            if (IndicationConst.INDICATION_OWNER_REAL.equals(indicationOwner)) {
                alert.sendAlert(envConfigProperties.getRealNationalWeb().getAutoSendTopic(), title, model, false);
            }
            if (IndicationConst.INDICATION_OWNER_ALL.equals(indicationOwner)) {
                alert.sendAlert(envConfigProperties.getNationalWeb().getAutoSendTopic(), title, model, false);
            }
            log.info("由kafka将信息通过Email发送  请求 ---> 结束");
            return "SUCCESS";
        } else {
            log.info("{}-{}无异常数据,无需发送邮件.", dateTime, indicationCatalog);
            return "无异常数据，无需发送邮件";
        }
    }

    /**
     *
     * @param obj
     * @return
     */
    @SuppressWarnings("unchecked")
    private StringBuffer getEmailIndicationBuffer(JSONObject obj) {
        JSONArray provinceIndicationData = obj.getJSONArray("分省异常");
        JSONArray allCountryIndicationData = obj.getJSONArray("全国14天");

        int provenceCount = 0;
        int countryCount = 0;

        StringBuffer emailIndicationBuffer = new StringBuffer();
        int num = 1;
        for (int i = 0; i < allCountryIndicationData.size(); i++) {
            //组装每个全国指标的邮件内容
            StringBuffer indicationBuffer = new StringBuffer();
            Map<String,Object> indicationData = Maps.newHashMap();
            indicationData = (Map<String,Object>)allCountryIndicationData.get(i);
            String indicationId = String.valueOf(indicationData.get("indicationId"));
            Map<String,String> limit = (Map<String,String>)indicationData.get("limit");//阙值
            String indicationUnit = String.valueOf(indicationData.get("indicationUnit"));
            List<Map<String,Object>> data = (List<Map<String,Object>>)indicationData.get("data");
            if(null == data || data.size() ==0) {
            	continue;
            }
            Map<String,Object> firstData = data.get(0);

            //全国指标表头部分
            setIndicationTitle(limit,indicationUnit,num,indicationData,indicationBuffer,Boolean.FALSE);
            String cause = (String)firstData.get("异常原因");
            if (StringUtils.isNotBlank(cause)) {
                List<Map<String,Object>> list = Lists.newArrayList();
                list.add(firstData);
                indicationBuffer.append(this.getTable(list));
            } else {
                indicationBuffer.append("无异常数据.<br/>");
                countryCount+=1;
            }
            //获取全国指标对应各省指标的异常数据
            for (int j = 0; j < provinceIndicationData.size();j++) {
                Map<String,Object> province = Maps.newHashMap();
                province = (Map<String,Object>)provinceIndicationData.get(j);
                String indicationAlias = (String)province.get("indicationAlias");
                if (indicationId.equals(indicationAlias)) {
                    List<Map<String,Object>> provinceData = (List<Map<String,Object>>)province.get("data");
                    Map<String,String> provinceLimit = (Map<String,String>)province.get("limit");//阙值
                    String provinceIndicationUnit = (String)province.get("indicationUnit");
                    setIndicationTitle(provinceLimit,provinceIndicationUnit,num,province,indicationBuffer,Boolean.TRUE);
                    if (provinceData.isEmpty()) {
                        indicationBuffer.append("无异常数据.<br/>");
                        provenceCount+=1;
                    } else {
                        indicationBuffer.append(this.getTable(provinceData));
                    }
                }
            }
            emailIndicationBuffer.append(indicationBuffer);
            num+=1;
        }
        return  countryCount != 0 && provenceCount != 0 && countryCount == allCountryIndicationData.size() && provenceCount == provinceIndicationData.size() ?
                new StringBuffer() : emailIndicationBuffer;
    }

    /**
     * 设置指标表头部分
     * @param limit
     * @param indicationUnit
     * @param num
     * @param indicationData
     * @param indicationBuffer
     * @param isNum
     */
    private void setIndicationTitle(Map<String,String> limit,String indicationUnit,int num,Map<String,Object> indicationData,StringBuffer indicationBuffer,Boolean isNum) {
        //全国指标表头部分
        String max = StringUtils.isNotEmpty(limit.get("maxValue")) ? "上限:"+limit.get("maxValue")+indicationUnit : "NaN"+indicationUnit;
        String min = StringUtils.isNotEmpty(limit.get("minValue")) ? "下限:"+limit.get("minValue")+indicationUnit : "NaN"+indicationUnit;
        String changeValueMax = StringUtils.isNotEmpty(limit.get("changeValueMax")) ? "变动值上限:"+limit.get("changeValueMax")+indicationUnit : "NaN"+indicationUnit;
        String changeValueMin = StringUtils.isNotEmpty(limit.get("changeValueMin")) ? "变动值下限:"+limit.get("changeValueMin")+indicationUnit : "NaN"+indicationUnit;
        String changeRateMax = StringUtils.isNotEmpty(limit.get("changeRateMax")) ? "变动率上限:"+limit.get("changeRateMax")+"%" : "NaN %";
        String changeRateMin = StringUtils.isNotEmpty(limit.get("changeRateMin")) ? "变动率下限:"+limit.get("changeRateMin")+"%" : "NaN %";
        indicationBuffer.append("<b>");
        String newNum = isNum ? num+".1" : num+"";
        indicationBuffer.append(newNum);
        indicationBuffer.append(".").append(indicationData.get("indicationName")).append("</b><br/>");
        String indicationName = (String) indicationData.get("indicationName");
        indicationBuffer.append("<span style='font-size:11px;'>(").append(indicationName.substring(2)).append("--")
                .append(max).append(",").append(min).append(",").append(changeValueMax).append(",").append(changeValueMin)
                .append(",").append(changeRateMax).append(",").append(changeRateMin).append(")</span>").append("<br/>");
    }

    /**
     * 设置邮件表格
     * @param list
     * @return
     */
    private StringBuffer getTable(List<Map<String,Object>> list) {
        //表格部分
        StringBuffer tableBuffer = new StringBuffer();
        tableBuffer.append("<table style=\"border-collapse:collapse; width:100%\">");

        //设置表头
        StringBuffer thBuffer = new StringBuffer();
        for (String thKey : list.get(0).keySet()) {
            if (!"indicationId".equals(thKey)) {
                thBuffer.append("<th style=\"border: 1px solid #000; text-align:center; font-size:13px\">")
                        .append(thKey).append("</th>");
            }
        }
        tableBuffer.append("<tr>").append(thBuffer).append("</tr>");
        //设置正文
        for (Map<String,Object> trData : list) {
            StringBuffer trBuffer = new StringBuffer();
            for (String trKey : trData.keySet()) {
                if (!"indicationId".equals(trKey)) {
                    trBuffer.append("<td style=\"border: 1px solid #000; text-align:center; font-size:13px\">")
                            .append(trData.get(trKey)).append("</td>");
                }
            }
            tableBuffer.append("<tr>").append(trBuffer).append("</tr>");
        }
        tableBuffer.append("</table>").append("<br/>");
        return tableBuffer;
    }
}
