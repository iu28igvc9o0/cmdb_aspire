package com.aspire.mirror.indication.realnational.actual;

import com.aspire.mirror.util.IndicationConst;
import com.aspire.mirror.util.IndicationUtils;
import net.sf.json.JSONObject;

/**
 * 收视用户数（今日）（个）
 */
public class RtzTodayViewingUserHandler extends AbstractActualFactory<RtzTodayViewingUserHandler> {
    @Override
    public void executePrimaryItem() {
        Double totalProgramUser = 0.0;
        for (String provinceCode : super.PROVINCE_LIST) {
            long programUser = 0L;
            if (super.SDK_RESULT_MAP != null && super.SDK_RESULT_MAP.containsKey(provinceCode)) {
                JSONObject jsonValue = JSONObject.fromObject(super.SDK_RESULT_MAP.get(provinceCode));
                programUser = IndicationUtils.getLong(jsonValue.get("programuser"));
            }
            super.addItemResult(super.endTime, provinceCode, itemEntity.getMirrorColumn(), programUser);
            totalProgramUser = IndicationUtils.formatSum(totalProgramUser, programUser);
        }
        super.addItemResult(super.endTime, IndicationConst.COUNTRY_PROVINCE_CODE, itemEntity.getMirrorColumn(), totalProgramUser);
    }

    @Override
    public void executeOtherItem() {

    }
}
