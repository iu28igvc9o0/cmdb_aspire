package com.aspire.mirror.schedule;

import com.aspire.mirror.entity.IndicationEntity;
import com.aspire.mirror.service.IIndicationDataService;
import com.aspire.mirror.service.IIndicationService;
import com.aspire.mirror.util.IndicationConst;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@EnableScheduling
@Component
@Slf4j
public class AutoCalcIndicationSchedule {
    
    @Autowired
    private IIndicationDataService dataService;
    @Autowired
    private IIndicationService indicationService;

    /**
     * 全量 -> 小时指标自动计算 每整点过5分钟执行 计算日活跃数
     */
    @Scheduled(cron = "0 5 * * * ?")
    private void autoCalcHourIndication(){
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.HOUR, -3);
            String dateStr = DateFormatUtils.format(calendar.getTime(), IndicationConst.QUERY_MONGODB_DATE_PATTERN_0);
            long start = new Date().getTime();
            log.info("Box hour indication : start at {}.", start);
            List<IndicationEntity> hourList = indicationService.getIndicationListByOwnerAndCatalogAndFrequencyAndGroup(
                    IndicationConst.INDICATION_OWNER_ALL, null, IndicationConst.INDICATION_FREQUENCY_HOUR, null, null);
            dataService.runHourCalc(dateStr, dateStr, hourList, null);
            long end = new Date().getTime();
            log.info("Hour Indication : end at {}. use time {} s.", end, (end - start) / 1000);
        } catch (ParseException e) {
            log.error("Date parse error.", e);
        }
    }
}
