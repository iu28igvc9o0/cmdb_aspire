package com.aspire.mirror.schedule;

import com.aspire.mirror.service.IIndicationDataService;
import com.aspire.mirror.util.IndicationConst;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;

@EnableScheduling
@Component
@Slf4j
public class AutoClearIndicationDataSchedule {
    
    @Autowired
    private IIndicationDataService iIndicationDataService;

    /**
     * 自动清除一个月之前的数据
     */
    //@Scheduled(cron = "0 0 0 * * ?")
    private void synchronizationIndicationData() {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.MONTH, -1);
        String calcDate = DateFormatUtils.format(c.getTime(), IndicationConst.QUERY_MONGODB_DATE_PATTERN);
        log.info("清理指标数据开始：*****************************************");
        iIndicationDataService.clearData(calcDate);
        log.info("清理指标数据结束：*****************************************");
    }

}
