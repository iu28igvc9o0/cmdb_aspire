package com.aspire.real_mirror.schedule;

import com.aspire.common.FactoryUtils;
import com.aspire.mirror.entity.IndicationEntity;
import com.aspire.mirror.entity.ProcessEntity;
import com.aspire.mirror.service.IIndicationService;
import com.aspire.mirror.service.IProcessService;
import com.aspire.mirror.util.IndicationConst;
import com.aspire.real_mirror.indication.AbstractRealMirrorIndicationFactory;
import com.aspire.real_mirror.indication.actual.AbstractRealActualFactory;
import com.aspire.real_mirror.indication.hour.AbstractRealHourFactory;
import com.aspire.real_mirror.service.IRealMailService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: AutoCalcRealIndicationSchedule
 * Author:   zhu.juwang
 * Date:     2019/9/19 14:05
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@EnableScheduling
@Component
@Slf4j
public class AutoCalcRealIndicationSchedule {

    @Autowired
    private IIndicationService indicationService;
    @Autowired
    private IProcessService processService;
    @Autowired
    private IRealMailService mailService;

    /**
     * 双送-日指标计算 每日9点05分执行
     */
    @Scheduled(cron = "0 5 9 * * ?")
    private void executeDayFactory() {
        long start = new Date().getTime();
        try {
            log.info("Day Indication : start at {}.", start);
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DATE, -1);
            List<IndicationEntity> indicationList = indicationService.getIndicationListByOwnerAndCatalogAndFrequencyAndGroup(
                    IndicationConst.INDICATION_OWNER_REAL, IndicationConst.CATALOG_BOX,
                    IndicationConst.INDICATION_FREQUENCY_DAY, null, null);
            log.info("Find need calc indication count : {}", indicationList.size());
            String[] date = getSdkDate(calendar, IndicationConst.INDICATION_FREQUENCY_DAY);
            calc(date, indicationList);
            // 发送邮件
            mailService.sendDayIndicationEmail(IndicationConst.INDICATION_OWNER_REAL, IndicationConst.CATALOG_BOX, null,
                    IndicationConst.INDICATION_FREQUENCY_DAY, date[1]);
        } catch (ParseException e) {
            log.error("Date parse error.", e);
        }
        long end = new Date().getTime();
        log.info("Day Indication : end at {}. use time {} s.", end, (end - start) / 1000);
    }

    /**
     * 计算小时指标和实时指标 每小时过5分执行一次
     */
    @Scheduled(cron = "0 5 * * * ?")
    private void executeHourFactory() {
        long start = new Date().getTime();
        try {
            log.info("Hour Indication : start at {}.", start);
            //计算小时指标
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.HOUR_OF_DAY, -1);
            //小时指标
            List<IndicationEntity> hourIndicationList = indicationService.getIndicationListByOwnerAndCatalogAndFrequencyAndGroup(
                    IndicationConst.INDICATION_OWNER_REAL, IndicationConst.CATALOG_BOX,
                    IndicationConst.INDICATION_FREQUENCY_HOUR, null, null);
            log.info("Find need calc hour indication count : {}", hourIndicationList.size());
            String[] hourDate = getSdkDate(calendar, IndicationConst.INDICATION_FREQUENCY_HOUR);
            calc(hourDate, hourIndicationList);
            //实时指标
            List<IndicationEntity> actualIndicationList = indicationService.getIndicationListByOwnerAndCatalogAndFrequencyAndGroup(
                    IndicationConst.INDICATION_OWNER_REAL, IndicationConst.CATALOG_BOX,
                    IndicationConst.INDICATION_FREQUENCY_ACTUAL, null, null);
            log.info("Find need calc actual indication count : {}", actualIndicationList.size());
            String[] actualDate = getSdkDate(calendar, IndicationConst.INDICATION_FREQUENCY_HOUR);
            calc(actualDate, actualIndicationList);
            actualIndicationList.addAll(hourIndicationList);
            mailService.sendHourIndicationEmail(actualIndicationList, actualDate[1]);
        } catch (ParseException e) {
            log.error("Date parse error.", e);
        }
        long end = new Date().getTime();
        log.info("Hour Indication : end at {}. use time {} s.", end, (end - start) / 1000);
    }

    /**
     * 双送-分钟指标 每分钟执行一次
     */
    @Scheduled(cron = "0 0/1 * * * ?")
    private void executeMinuteFactory() {
        long start = new Date().getTime();
        try {
            log.info("Minute Indication : start at {}.", start);
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.MINUTE, -3);
            String[] dates = new String[2];
            dates[0] = DateFormatUtils.format(calendar.getTime(), IndicationConst.DATE_PATTERN_00);
            dates[1] = DateFormatUtils.format(calendar.getTime(), IndicationConst.DATE_PATTERN_00);
            List<IndicationEntity> indicationList = indicationService.getIndicationListByOwnerAndCatalogAndFrequencyAndGroup(
                    IndicationConst.INDICATION_OWNER_REAL, IndicationConst.CATALOG_BOX,
                    IndicationConst.INDICATION_FREQUENCY_MINUTE, null, null);
            calc(dates, indicationList);
            mailService.sendMinuteIndicationMail(IndicationConst.INDICATION_OWNER_REAL, IndicationConst.CATALOG_BOX, null,
                    IndicationConst.INDICATION_FREQUENCY_MINUTE, dates[1]);
        } catch (Exception e) {
            log.error("Calc indication error.", e);
        }
        long end = new Date().getTime();
        log.info("Minute Indication : end at {}. use time {} s.", end, (end - start) / 1000);
    }

    /**
     * 获取传递给sdk的数据
     * @param calendar
     * @param indicationFrequency
     * @return
     */
    public String[] getSdkDate(Calendar calendar, String indicationFrequency) throws ParseException {
        String[] dates = new String[] {null, null};
        //日指标
        if (indicationFrequency.equals(IndicationConst.INDICATION_FREQUENCY_DAY)) {
            dates[0] = DateFormatUtils.format(calendar.getTime(), IndicationConst.QUERY_MONGODB_DATE_PATTERN);
            dates[1] = DateFormatUtils.format(calendar.getTime(), IndicationConst.QUERY_MONGODB_DATE_PATTERN);
//            dates[0] = "20190902";
//            dates[1] = "20190902";
        }
        //小时指标--统计当前时间的上一个小时的数据
        if (indicationFrequency.equals(IndicationConst.INDICATION_FREQUENCY_HOUR)) {
            dates[0] = DateFormatUtils.format(calendar.getTime(), IndicationConst.QUERY_MONGODB_DATE_PATTERN_0);
            dates[1] = DateFormatUtils.format(calendar.getTime(), IndicationConst.QUERY_MONGODB_DATE_PATTERN_0);
//            dates[0] = "2019090212";
//            dates[1] = "2019090212";
        }
        //实时指标--统计当日截止到上一个小时的数据
        if (indicationFrequency.equals(IndicationConst.INDICATION_FREQUENCY_ACTUAL)) {
            dates[0] = DateFormatUtils.format(calendar.getTime(), IndicationConst.QUERY_MONGODB_DATE_PATTERN) + "00";
            dates[1] = DateFormatUtils.format(calendar.getTime(), IndicationConst.QUERY_MONGODB_DATE_PATTERN_0);
//            dates[0] = "2019090200";
//            dates[1] = "2019090212";
        }
        //分钟指标--获取当前时间前3分钟的时间点
        if (indicationFrequency.equals(IndicationConst.INDICATION_FREQUENCY_MINUTE)) {
            dates[0] = DateFormatUtils.format(calendar.getTime(), IndicationConst.DATE_PATTERN_00);
            dates[1] = DateFormatUtils.format(calendar.getTime(), IndicationConst.DATE_PATTERN_00);
//            dates[0] = "2019090200";
//            dates[1] = "2019090212";
        }
        return new String[] {dates[0], dates[1]};
    }

    private void calc (String[] date, List<IndicationEntity> indicationList) {
        for (IndicationEntity indicationEntity : indicationList) {
            try {
                log.info("Begin handler indication {}. data {} started at {}", indicationEntity.getIndicationName(), date[0], new Date().getTime());
                //根据指标ID, 查找对应的处理类 进行计算
                ProcessEntity processEntity = processService.getProcessByIndicationId(indicationEntity.getIndicationId());
                if (processEntity == null) {
                    log.error("Can't find indication_id " + indicationEntity.getIndicationId() + " process class. Please check it.");
                    continue;
                }
                log.info("Handler day indication {} process class [{}]", indicationEntity.getIndicationName(), processEntity.getProcessClass());
                AbstractRealMirrorIndicationFactory factory = FactoryUtils.invokeRealFactory(processEntity.getProcessClass());
                if (factory instanceof AbstractRealActualFactory || factory instanceof AbstractRealHourFactory) {
                    factory.setBeginTime(date[0]);
                    factory.setEndTime(date[1]);
                } else {
                    factory.setCalcDate(date[1]);
                }
                factory.setIndicationEntity(indicationEntity);
                factory.setProcessEntity(processEntity);
                factory.init();
            } catch (Exception e) {
                log.error("Handler day indication {} error:{}", indicationEntity.getIndicationName(), e.getMessage(), e);
            }
        }
    }
}
