package com.aspire.real_mirror.indication.hour;

import com.aspire.mirror.util.IndicationUtils;
import com.aspire.real_mirror.indication.AbstractRealMirrorIndicationFactory;
import lombok.Getter;
import lombok.Setter;

public abstract class AbstractRealHourFactory<T> extends AbstractRealMirrorIndicationFactory<T> {

    /**
     * 开始时间
     */
    @Setter @Getter
    public String beginTime;

    /**
     * 结束时间
     */
    @Setter @Getter
    public String endTime;

    @Override
    public void init() {
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
