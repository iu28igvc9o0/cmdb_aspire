package com.aspire.mirror.indication.realnational.day;

import com.aspire.mirror.annotation.RtzDayIndication;
import com.aspire.mirror.indication.AbstractFactory;
import com.aspire.mirror.util.IndicationConst;
import com.aspire.mirror.util.IndicationUtils;
import net.sf.json.JSONObject;

/**
 * 积累用户量处理类
 */
@RtzDayIndication
public class RtzTotalUserHandler extends AbstractDayFactory<RtzTotalUserHandler> {

    @Override
    public void executePrimaryItem() {
        Double countryUser = 0.0;
        for (String provinceCode : super.PROVINCE_LIST) {
            Long totalUser = 0L;
            if (super.SDK_RESULT_MAP != null && super.SDK_RESULT_MAP.containsKey(provinceCode)) {
                JSONObject jsonValue = JSONObject.fromObject(super.SDK_RESULT_MAP.get(provinceCode));
                totalUser = IndicationUtils.getLong(jsonValue.get("totaluser"));
            }
            countryUser = IndicationUtils.formatSum(countryUser, totalUser);
            super.addItemResult(super.endTime, provinceCode, itemEntity.getMirrorColumn(), totalUser);
        }
        super.addItemResult(super.endTime, IndicationConst.COUNTRY_PROVINCE_CODE, itemEntity.getMirrorColumn(), countryUser);
    }

    @Override
    public void executeOtherItem() {

    }
}
