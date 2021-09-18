package com.aspire.mirror.indication.realnational.hour;

import com.aspire.mirror.util.IndicationConst;
import com.aspire.mirror.util.IndicationUtils;
import com.aspire.mirror.util.WSDLUtil;
import net.sf.json.JSONObject;

import java.util.List;

/**
 * 收视用户数（今日）（个）
 */
public class RtzTodayViewingUserHandler extends AbstractHourFactory<RtzTodayViewingUserHandler> {
    @Override
    public void callWsdl() {
        List<String> dateList = IndicationUtils.getDateListALL(endTime.substring(0, 8), "hour");
        WSDLUtil wsdlUtil = new WSDLUtil();
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        super.SDK_RESULT_MAP = wsdlUtil.getWsdlServiceReturnMap(classLoader, wsdl, itemEntity.getMethod(),
                dateList, null, null, null, null, "datetime");
    }

    @Override
    public void executePrimaryItem() {
        long programUser = 0L;
        if (super.SDK_RESULT_MAP != null && super.SDK_RESULT_MAP.containsKey(endTime)) {
            JSONObject jsonValue = JSONObject.fromObject(super.SDK_RESULT_MAP.get(endTime));
            programUser = IndicationUtils.getLong(jsonValue.get("programuser"));
        }
        super.addItemResult(super.endTime, IndicationConst.COUNTRY_PROVINCE_CODE, itemEntity.getMirrorColumn(), programUser);
    }

    @Override
    public void executeOtherItem() {

    }
}
