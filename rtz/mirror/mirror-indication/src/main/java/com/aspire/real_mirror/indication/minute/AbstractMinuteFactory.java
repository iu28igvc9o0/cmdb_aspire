package com.aspire.real_mirror.indication.minute;

import com.aspire.real_mirror.indication.AbstractRealMirrorIndicationFactory;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedList;

public abstract class AbstractMinuteFactory<T> extends AbstractRealMirrorIndicationFactory<T> {
    @Override
    public void initSevenDays() {
        this.PREV_SEVEN_DAYS_LIST = new LinkedList<String>();
        this.PREV_SEVEN_DAYS_LIST.add(calcDate);
    }

    @Override
    public void initSevenDataList() {
        super.initSevenDataList();
    }
}
