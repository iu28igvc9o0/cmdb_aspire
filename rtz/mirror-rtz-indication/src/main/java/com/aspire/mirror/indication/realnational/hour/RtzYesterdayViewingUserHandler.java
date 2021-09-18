package com.aspire.mirror.indication.realnational.hour;

import com.aspire.mirror.util.IndicationConst;
import com.aspire.mirror.util.IndicationUtils;
import com.aspire.mirror.util.WSDLUtil;
import net.sf.json.JSONObject;

import java.util.List;

/**
 * 收视用户数（昨日）（个）
 */
public class RtzYesterdayViewingUserHandler extends AbstractHourFactory<RtzYesterdayViewingUserHandler> {

    @Override
    public void callWsdl() {
        //获取昨天的日期
        String prevDay  = IndicationUtils.getPrevDay(endTime.substring(0, 8), IndicationConst.QUERY_MONGODB_DATE_PATTERN);
        List<String> dateList = IndicationUtils.getDateListALL(prevDay, "hour");
        WSDLUtil wsdlUtil = new WSDLUtil();
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        super.SDK_RESULT_MAP = wsdlUtil.getWsdlServiceReturnMap(classLoader, wsdl, itemEntity.getMethod(),
                dateList, null, null, null, null, "datetime");
    }

    @Override
    public void executePrimaryItem() {
        String prevDay  = IndicationUtils.getPrevDay(endTime.substring(0, 8), IndicationConst.QUERY_MONGODB_DATE_PATTERN);
        String hour = endTime.substring(8, 10);
        long programUser = 0L;
        if (super.SDK_RESULT_MAP != null && super.SDK_RESULT_MAP.containsKey(prevDay + hour)) {
            JSONObject jsonValue = JSONObject.fromObject(super.SDK_RESULT_MAP.get(prevDay + hour));
            programUser = IndicationUtils.getLong(jsonValue.get("programuser"));
        }
        super.addItemResult(super.endTime, IndicationConst.COUNTRY_PROVINCE_CODE, itemEntity.getMirrorColumn(), programUser);
    }

    @Override
    public void executeOtherItem() {

    }
}
