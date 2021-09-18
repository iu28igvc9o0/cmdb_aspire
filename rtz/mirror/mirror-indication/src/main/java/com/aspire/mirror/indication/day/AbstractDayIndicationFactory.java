package com.aspire.mirror.indication.day;

import com.aspire.mirror.entity.*;
import com.aspire.AbstractIndicationFactory;
import com.aspire.mirror.indication.AbstractMirrorIndicationFactory;
import com.aspire.mirror.util.IndicationConst;
import com.aspire.mirror.util.IndicationUtils;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
public abstract class AbstractDayIndicationFactory extends AbstractMirrorIndicationFactory<AbstractDayIndicationFactory> {
    /**
     * 计算时间
     */
    @Setter @Getter
    public String calcDate;

    public void init() {
        super.setCalcDate(calcDate);
        super.init();
    }

    /**
     * 初始化最近7天的日期
     */
    public void initSevenDays() {
        this.PREV_SEVEN_DAYS_LIST = IndicationUtils.getPrevDays(calcDate,IndicationConst.QUERY_MONGODB_DATE_PATTERN,-7);
    }
}
