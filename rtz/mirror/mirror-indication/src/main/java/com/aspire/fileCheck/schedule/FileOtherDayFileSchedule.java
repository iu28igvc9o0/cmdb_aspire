package com.aspire.fileCheck.schedule;

import com.aspire.fileCheck.service.IFileIndicationPeriodStateService;
import com.aspire.fileCheck.util.FileConst;
import com.aspire.fileCheck.util.TimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;

@Component
@Slf4j
public class FileOtherDayFileSchedule extends BaseSchedule<FileOtherDayFileSchedule> {

	 @Autowired
	 private IFileIndicationPeriodStateService iFileIndicationPeriodStateService;
	 
	@Scheduled(cron = "0 0/1 * * * ?")
    private void check() {
		Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.HOUR, - FileConst.PERIOD_LAZY_HOUR);
        calendar.add(Calendar.HOUR, - FileConst.CALC_LAZY_HOUR);
		log.info("非当天文件检测开始:"+DateFormatUtils.format(calendar, TimeUtil.DATE_FORMAT_YYYYMMDDHHMMSS));
		updateJobManager("CHECK_OTHER_DAY_FILE", FileConst.JOB_LATEST_RUNNING_1, FileConst.JOB_STATUS_RUNNING);
		iFileIndicationPeriodStateService.checkOtherFile();
		updateJobManager("CHECK_OTHER_DAY_FILE", FileConst.JOB_LATEST_RUNNING_1, FileConst.JOB_STATUS_FINISH);
		log.info("非当天文件检测结束:"+DateFormatUtils.format(calendar, TimeUtil.DATE_FORMAT_YYYYMMDDHHMMSS));
    }
	
}
