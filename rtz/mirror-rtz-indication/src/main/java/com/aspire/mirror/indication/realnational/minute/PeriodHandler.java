package com.aspire.mirror.indication.realnational.minute;

import com.aspire.mirror.util.IndicationConst;
import com.aspire.mirror.util.TimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.time.DateUtils;

import java.text.ParseException;
import java.util.Date;

/**
 * Created with IntelliJ IDEA. User: jw.zhu Date: 2018/11/10 软探针异常指标监控系统 -
 * 无卡顿花屏收视用户占比指标 FirstLoadAvgHandler
 */
@Slf4j
public class PeriodHandler extends AbstractMinuteFactory {

    @Override
    public void callWsdl() {

    }

    @Override
    public void executePrimaryItem() {
        Date date = null;
        try {
			date =  DateUtils.parseDate(this.endTime, new String[] {IndicationConst.DATE_PATTERN});
		} catch (ParseException e) {
			log.error("时间格式错误{}", this.endTime, e);
		}
        String curPeriod = TimeUtil.formatDateToPeriod(date);
        for (String provinceCode : super.PROVINCE_LIST) {
            super.addItemResult(super.endTime, provinceCode, itemEntity.getMirrorColumn(), curPeriod);
        }
        super.addItemResult(this.endTime, IndicationConst.COUNTRY_PROVINCE_CODE, itemEntity.getMirrorColumn(), curPeriod);
    }

    @Override
    public void executeOtherItem() {

    }
}
