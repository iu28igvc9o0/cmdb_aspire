package com.aspire.mirror.schedule.realnational;

import com.aspire.common.EnvConfigProperties;
import com.aspire.common.HttpUtil;
import com.aspire.mirror.entity.IndicationEntity;
import com.aspire.mirror.entity.ItemEntity;
import com.aspire.mirror.entity.ThemeEntity;
import com.aspire.mirror.indication.AbstractFactory;
import com.aspire.mirror.service.ISendMailService;
import com.aspire.mirror.util.IndicationConst;
import com.aspire.mirror.util.SpringUtil;
import com.aspire.mirror.util.XMLUtil;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;

@EnableScheduling
@Component
@Slf4j
public class RtzIndicationSchedule {

    @Autowired
    ISendMailService sendMailService;

    @Scheduled(cron = "0 5 9 * * ?")
    private void executeDayFactory() {
        String themeType = IndicationConst.INDICATION_FREQUENCY_DAY;
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);
        String[] dates = getSdkDate(calendar, themeType);
        if (dates[0] == null || dates[1] == null) {
            log.error("Get sdk calc date error. Calc skip.");
            return;
        }
        List<ThemeEntity> themeList = XMLUtil.getThemeList(themeType);
        calc(dates, themeList);
        //发送邮件
        sendMailService.sendIndicationEmail(IndicationConst.INDICATION_OWNER_REAL, IndicationConst.CATALOG_BOX, null,
                themeType, dates[1]);
    }

    @Scheduled(cron = "0 0/5 * * * ?")
    private void executeHourFactory() {
        //计算小时指标
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR_OF_DAY, -1);
        String[] dates = getSdkDate(calendar, IndicationConst.INDICATION_FREQUENCY_HOUR);
        List<ThemeEntity> themeList = XMLUtil.getThemeList(IndicationConst.INDICATION_FREQUENCY_HOUR);
        calc(dates, themeList);

        //计算实时指标
        Calendar actualCalender = Calendar.getInstance();
        actualCalender.add(Calendar.HOUR_OF_DAY, -1);
        dates = getSdkDate(actualCalender, IndicationConst.INDICATION_FREQUENCY_ACTUAL);
        themeList = XMLUtil.getThemeList(IndicationConst.INDICATION_FREQUENCY_ACTUAL);
        calc(dates, themeList);
        List<IndicationEntity> actualList = XMLUtil.getIndicationList(IndicationConst.INDICATION_OWNER_REAL, IndicationConst.CATALOG_BOX, null,
                IndicationConst.INDICATION_FREQUENCY_ACTUAL);
        List<IndicationEntity> hourList = XMLUtil.getIndicationList(IndicationConst.INDICATION_OWNER_REAL, IndicationConst.CATALOG_BOX, null,
                IndicationConst.INDICATION_FREQUENCY_HOUR);
        actualList.addAll(hourList);
        sendMailService.sendHourIndicationEmail(actualList, dates[1]);
    }

    @Scheduled(cron = "0 0/1 * * * ?")
    private void executeMinuteFactory() {
        String themeType = IndicationConst.INDICATION_FREQUENCY_MINUTE;
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, -3);
        String[] dates = getSdkDate(calendar, themeType);
        if (dates[0] == null || dates[1] == null) {
            log.error("Get sdk calc date error. Calc skip.");
            return;
        }
        List<ThemeEntity> themeList = XMLUtil.getThemeList(themeType);
        calc(dates, themeList);
        sendMailService.sendMinuteIndicationMail(IndicationConst.INDICATION_OWNER_REAL, IndicationConst.CATALOG_BOX, null,
                IndicationConst.INDICATION_FREQUENCY_MINUTE, dates[1]);
    }

    public void calc(String[] dates, List<ThemeEntity> themeList) {
        EnvConfigProperties envConfigProperties = SpringUtil.getBean(EnvConfigProperties.class);
        for (ThemeEntity themeEntity : themeList) {
            log.info("Begin handler theme -themeCode={} -sysCode={} . need handler item size={}",
                    themeEntity.getThemeCode(), themeEntity.getSysCode(), themeEntity.getItemList().size());
            JSONObject calcParams = new JSONObject();
            calcParams.put("group_time", getExecuteDate(dates[1]));
            Calendar startCalender = Calendar.getInstance();
            LinkedHashMap itemResult = new LinkedHashMap<String, LinkedHashMap<String, Object>>();
            for (ItemEntity itemEntity : themeEntity.getItemList()) {
                try {
                    log.info("Begin handler -beginTime={} -endTime={} theme item -name={} -type={} -handler={} -wsdl={} -method={} -mirrorColumn={} ",
                            dates[0], dates[1], itemEntity.getName(), itemEntity.getType(), itemEntity.getHandler(), itemEntity.getWsdl(),
                            itemEntity.getMethod(), itemEntity.getMirrorColumn());
                    Calendar startCalender2 = Calendar.getInstance();
                    Class factoryClass = Class.forName(itemEntity.getHandler());
                    AbstractFactory abstractFactory = (AbstractFactory) factoryClass.newInstance();
                    abstractFactory.setBeginTime(dates[0]);
                    abstractFactory.setEndTime(dates[1]);
                    abstractFactory.setThemeEntity(themeEntity);
                    abstractFactory.setCommonEntity(themeEntity.getCommonEntity());
                    abstractFactory.setItemEntity(itemEntity);
                    abstractFactory.init();
                    LinkedHashMap<String, Object> calcResult= abstractFactory.getItemResult();
                    for (Map.Entry<String, Object> entry : calcResult.entrySet()) {
                        LinkedHashMap<String, Object> values = (LinkedHashMap<String, Object>) itemResult.get(entry.getKey());
                        if (itemResult.containsKey(entry.getKey())) {
                            LinkedHashMap<String, Object> valuesEntry = (LinkedHashMap<String, Object>) entry.getValue();
                            values.putAll(valuesEntry);
                        } else {
                            values = (LinkedHashMap<String, Object>) entry.getValue();
                        }
                        itemResult.put(entry.getKey(),values);
                    }
                    Calendar endCalender2 = Calendar.getInstance();
                    log.info("End handler theme item -name={}. use time={}s", itemEntity.getName(),
                            (endCalender2.getTimeInMillis() - startCalender2.getTimeInMillis()) / 1000);
                } catch ( ClassNotFoundException e) {
                    log.error("Can't find handler class {} error.", itemEntity.getHandler(), e);
                } catch (IllegalAccessException e) {
                    log.error("Get handler class {} error.", itemEntity.getHandler(), e);
                } catch (InstantiationException e) {
                    log.error("Get handler class {} error.", itemEntity.getHandler(), e);
                }
            }
            log.info("Begin transfer -sysCode={} -themeCode={} data to mirror ...", themeEntity.getSysCode(), themeEntity.getThemeCode());
            JSONArray jsonArray = new JSONArray();
            List<ItemEntity> itemList = new ArrayList<ItemEntity>();
            itemList.addAll(themeEntity.getCommonEntity().getItemList());
            itemList.addAll(themeEntity.getItemList());
            for (Object data : itemResult.values()) {
                LinkedHashMap<String, Object> dataMap = (LinkedHashMap<String, Object>) data;
                StringBuilder stringBuilder = new StringBuilder();
                for (ItemEntity itemEntity : itemList) {
                    if (!stringBuilder.toString().equals("")) {
                        stringBuilder.append("|");
                    }
                    if (dataMap.containsKey(itemEntity.getMirrorColumn())) {
                        stringBuilder.append(dataMap.get(itemEntity.getMirrorColumn()));
                    } else {
                        stringBuilder.append("0");
                    }
                }
                if (!stringBuilder.toString().equals("")) {
                    jsonArray.add(stringBuilder.toString());
                }
            }
            JSONObject header = new JSONObject();
            JSONObject params = new JSONObject();
            params.put("SysCode", themeEntity.getSysCode());
            params.put("Time", getExecuteDate(dates[1]));
            params.put("ThemeCode", themeEntity.getThemeCode());
            params.put("Datas", jsonArray.toString());
            HttpUtil.postMethod(envConfigProperties.getInterFace().getPostThemeData(), header, params);
            log.info("Transfer -sysCode={} -themeCode={} data {}", themeEntity.getSysCode(), themeEntity.getThemeCode(), jsonArray.toString());
            log.info("Transfer -sysCode={} -themeCode={} data to mirror success.", themeEntity.getSysCode(), themeEntity.getThemeCode());
            Calendar endCalender = Calendar.getInstance();
            log.info("End handler theme -themeCode={} -sysCode={} . use time={}s", themeEntity.getThemeCode(),
                    themeEntity.getSysCode(), (endCalender.getTimeInMillis() - startCalender.getTimeInMillis()) / 1000);
            //线程挂起1分钟, 等待产品化主题数据处理
            try {
                log.info("Thread sleep 10s.");
                Thread.sleep(10*1000);
            } catch (InterruptedException e) {
                log.error("Thread sleep error.", e);
            }
            //开始触发产品化计算指标接口
            log.info("Begin trigger calc theme code {} indication ...", themeEntity.getThemeCode());
            List<IndicationEntity> indicationList = XMLUtil.getIndicationList(themeEntity.getThemeCode());
            log.info("Theme code {} indication list -> {}.", themeEntity.getThemeCode(), JSONArray.fromObject(indicationList));
            for (IndicationEntity indicationEntity : indicationList) {
                String mirrorMsg = String.format("indication id -> [%s] name -> [%s].", indicationEntity.getIndicationId(), indicationEntity.getIndicationName());
                log.info("Mirror begin calc {}", mirrorMsg);
                String calcUrl = envConfigProperties.getInterFace().getThemeCalcItem() + indicationEntity.getIndicationId();
                Object result = HttpUtil.getMethod(calcUrl, calcParams);
                if (result != null) {
                    JSONObject jsonObject = JSONObject.fromObject(result);
                    if (jsonObject.containsKey("success") && jsonObject.getString("success").equals("1")) {
                        log.info("Mirror calc {} success.", mirrorMsg);
                    } else {
                        if (jsonObject.containsKey("reason")) {
                            log.error("Mirror calc {} error. Cause reason : {}", mirrorMsg, jsonObject.getString("reason"));
                        }
                    }
                } else {
                    log.error("Mirror calc indication id -> [{}] name -> [{}] error. Response result is null.",
                            indicationEntity.getIndicationId(), indicationEntity.getIndicationName());
                }
            }
            log.info("End trigger mirror calc indication.");
            //线程挂起1分钟, 等待产品化主题数据处理
            try {
                log.info("Thread sleep 10s.");
                Thread.sleep(10*1000);
            } catch (InterruptedException e) {
                log.error("Thread sleep error.", e);
            }
        }
    }

    /**
     * 获取传递给产品化的时间
     * @return
     */
    private String getExecuteDate(String calcTime) {
        //时间格式为 yyyyMMddHHmmss
        String executeDate = calcTime;
        int appendLength = 14-executeDate.length();
        for (int i = 0; i < appendLength; i ++) {
            executeDate += "0";
        }
        return executeDate;
    }

    /**
     * 获取传递给sdk的数据
     * @param calendar
     * @param themeType
     * @return
     */
    public String[] getSdkDate(Calendar calendar, String themeType) {
        String[] dates = new String[] {null, null};
        //日指标
        if (themeType.equals(IndicationConst.INDICATION_THEME_TYPE_DAY)) {
            dates[0] = DateFormatUtils.format(calendar.getTime(), IndicationConst.QUERY_MONGODB_DATE_PATTERN);
            dates[1] = DateFormatUtils.format(calendar.getTime(), IndicationConst.QUERY_MONGODB_DATE_PATTERN);
//            dates[0] = "20181203";
//            dates[1] = "20181203";
        }
        //小时指标--统计当前时间的上一个小时的数据
        if (themeType.equals(IndicationConst.INDICATION_THEME_TYPE_HOUR)) {
            dates[0] = DateFormatUtils.format(calendar.getTime(), IndicationConst.QUERY_MONGODB_DATE_PATTERN_0);
            dates[1] = DateFormatUtils.format(calendar.getTime(), IndicationConst.QUERY_MONGODB_DATE_PATTERN_0);
            //dates[0] = "2018120412";
            //dates[1] = "2018120412";
        }
        //实时指标--统计当日截止到上一个小时的数据
        if (themeType.equals(IndicationConst.INDICATION_THEME_TYPE_ACTUAL)) {
            dates[0] = DateFormatUtils.format(calendar.getTime(), IndicationConst.QUERY_MONGODB_DATE_PATTERN) + "00";
            dates[1] = DateFormatUtils.format(calendar.getTime(), IndicationConst.QUERY_MONGODB_DATE_PATTERN_0);
            //dates[0] = "2018120400";
            //dates[1] = "2018120412";
        }
        //分钟指标--获取当前时间前3分钟的时间点
        if (themeType.equals(IndicationConst.INDICATION_FREQUENCY_MINUTE)) {
            dates[0] = DateFormatUtils.format(calendar.getTime(), IndicationConst.DATE_PATTERN_00);
            dates[1] = DateFormatUtils.format(calendar.getTime(), IndicationConst.DATE_PATTERN_00);
            //dates[0] = "2018120400";
            //dates[1] = "2018120412";
        }
        return dates;
    }
}
