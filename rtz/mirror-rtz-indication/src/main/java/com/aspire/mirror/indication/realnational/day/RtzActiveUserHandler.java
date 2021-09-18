package com.aspire.mirror.indication.realnational.day;

import com.aspire.mirror.annotation.RtzDayIndication;
import com.aspire.mirror.indication.AbstractFactory;
import com.aspire.mirror.util.IndicationConst;
import com.aspire.mirror.util.IndicationUtils;
import net.sf.json.JSONObject;

/**
 * 活跃用户量处理类
 */
@RtzDayIndication
public class RtzActiveUserHandler extends AbstractDayFactory<RtzActiveUserHandler> {
    @Override
    public void executePrimaryItem() {
        Double countryActiveUser = 0.0;
        for (String provinceCode : super.PROVINCE_LIST) {
            Long activeUser = 0L;
            if (super.SDK_RESULT_MAP != null && super.SDK_RESULT_MAP.containsKey(provinceCode)) {
                JSONObject jsonValue = JSONObject.fromObject(super.SDK_RESULT_MAP.get(provinceCode));
                activeUser = IndicationUtils.getLong(jsonValue.get("activeuser"));
            }
            super.addItemResult(super.endTime, provinceCode, itemEntity.getMirrorColumn(), activeUser);
            countryActiveUser = IndicationUtils.formatSum(countryActiveUser, activeUser);
        }
        super.addItemResult(super.endTime, IndicationConst.COUNTRY_PROVINCE_CODE, itemEntity.getMirrorColumn(), countryActiveUser);
    }

    @Override
    public void executeOtherItem() {

    }
}
