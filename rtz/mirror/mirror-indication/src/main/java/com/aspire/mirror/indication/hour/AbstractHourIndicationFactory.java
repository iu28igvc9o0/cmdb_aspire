package com.aspire.mirror.indication.hour;

import com.aspire.mirror.entity.IndicationAllItemEntity;
import com.aspire.AbstractIndicationFactory;
import com.aspire.mirror.indication.AbstractMirrorIndicationFactory;
import com.aspire.mirror.util.IndicationUtils;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
public abstract class AbstractHourIndicationFactory extends AbstractMirrorIndicationFactory<AbstractHourIndicationFactory> {

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
        this.PREV_SEVEN_DAYS_LIST = IndicationUtils.getPrevDaysHour(calcDate, -7);
    }

    /**
     * 初始化SDK数据 初始化最近20个自然日各省的数据, 方便均值等计算
     */
    public abstract void initSDK();

    /**
     * 需要子类重写 计算除以上公共的指标项及主指标项以外的项
     */
    public abstract void calcSpecialItem(IndicationAllItemEntity itemEntity);

    /**
     * 提交计算结果
     * @param commitNow 是否现在提交
     */
    public void submitData(boolean commitNow) {
        super.submitData(commitNow);
    }

    /**
     * 提交计算结果
     * @param itemEntity 指标项
     * @param provinceCode 省份编码
     * @param calcDate 计算日期
     * @param calcValue 计算值
     * @param commitNow 是否现在提交
     */
    public void submitData(IndicationAllItemEntity itemEntity, String provinceCode, String calcDate,
                           Object calcValue, Object origCalcValue, boolean commitNow) {
        super.submitData(itemEntity, provinceCode, calcDate, calcValue, origCalcValue, commitNow);
    }
}
