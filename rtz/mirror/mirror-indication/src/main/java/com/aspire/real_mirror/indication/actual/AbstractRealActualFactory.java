package com.aspire.real_mirror.indication.actual;

import com.aspire.mirror.util.IndicationUtils;
import com.aspire.real_mirror.indication.AbstractRealMirrorIndicationFactory;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractRealActualFactory<T> extends AbstractRealMirrorIndicationFactory<T> {
    /**
     * 开始时间
     */
    @Setter
    @Getter
    public String beginTime;

    /**
     * 结束时间
     */
    @Setter @Getter
    public String endTime;

    @Override
    public void init() {
        beginTime = beginTime.substring(0,8) + "00";
        super.setCalcDate(endTime);
        super.init();
    }

    /**
     * 初始化最近7天的日期
     */
    public void initSevenDays() {
        this.PREV_SEVEN_DAYS_LIST = IndicationUtils.getPrevDaysHour(calcDate,-7);
    }
}
