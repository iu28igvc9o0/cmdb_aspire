package com.aspire.mirror.indication.realnational.day;

import com.aspire.mirror.annotation.RtzDayIndication;
import com.aspire.mirror.indication.AbstractFactory;
import com.aspire.mirror.util.IndicationConst;
import com.aspire.mirror.util.IndicationUtils;
import net.sf.json.JSONObject;

/**
 * 活跃用户率处理类
 */
@RtzDayIndication
public class RtzActiveRateHandler extends AbstractDayFactory<RtzActiveRateHandler> {

    @Override
    public void executePrimaryItem() {
        Double countryUser = 0.0, countryActiveUser = 0.0;
        for (String provinceCode : super.PROVINCE_LIST) {
            Long totalUser = 0L, activeUser = 0L;
            Double activeRate = 0.0;
            if (super.SDK_RESULT_MAP != null && super.SDK_RESULT_MAP.containsKey(provinceCode)) {
                JSONObject jsonValue = JSONObject.fromObject(super.SDK_RESULT_MAP.get(provinceCode));
                totalUser = IndicationUtils.getLong(jsonValue.get("totaluser"));
                activeUser = IndicationUtils.getLong(jsonValue.get("activeuser"));
                activeRate = IndicationUtils.formatDivide(activeUser * 100, totalUser);
            }
            countryUser = IndicationUtils.formatSum(countryUser, totalUser);
            countryActiveUser = IndicationUtils.formatSum(countryActiveUser, activeUser);
            super.addItemResult(super.endTime, provinceCode, itemEntity.getMirrorColumn(), activeRate);
        }
        Double totalRate = IndicationUtils.formatDivide(countryActiveUser * 100, countryUser);
        super.addItemResult(super.endTime, IndicationConst.COUNTRY_PROVINCE_CODE, itemEntity.getMirrorColumn(), totalRate);
    }

    @Override
    public void executeOtherItem() {

    }
}
