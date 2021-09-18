package com.aspire.real_mirror.indication.day;

import com.aspire.mirror.util.IndicationConst;
import com.aspire.mirror.util.IndicationUtils;
import com.aspire.real_mirror.indication.AbstractRealMirrorIndicationFactory;

public abstract class AbstractRealDayFactory<T> extends AbstractRealMirrorIndicationFactory<T> {
    /**
     * 初始化最近7天的日期
     */
    public void initSevenDays() {
        this.PREV_SEVEN_DAYS_LIST = IndicationUtils.getPrevDays(calcDate, IndicationConst.QUERY_MONGODB_DATE_PATTERN,-7);
    }
}
