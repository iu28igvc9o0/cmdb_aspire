package com.aspire.mirror.indication.realnational.actual;

import com.aspire.mirror.util.IndicationConst;
import com.aspire.mirror.util.IndicationUtils;
import com.aspire.mirror.util.WSDLUtil;
import net.sf.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 点播用户占比
 */
public class RtzVodProgramUserHandler extends AbstractActualFactory<RtzVodProgramUserHandler> {
    @Override
    public void callWsdl() {
        List<String> dateList = new ArrayList<String>();
        dateList.add(endTime.substring(0,8));
        WSDLUtil wsdlUtil = new WSDLUtil();
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        super.SDK_RESULT_MAP = wsdlUtil.getWsdlServiceReturnMap(classLoader, wsdl, itemEntity.getMethod(),
                dateList, null, null, null, null, IndicationConst.QUERY_MONGODB_TYPE_ALL);
    }
    @Override
    public void executePrimaryItem() {
        String calcDay = endTime.substring(0,8);
        long liveUser = 0L, vodUser = 0;
        Double allUser = 0.0;
        if (super.SDK_RESULT_MAP != null && super.SDK_RESULT_MAP.size() > 0) {
            JSONObject jsonValue = JSONObject.fromObject(super.SDK_RESULT_MAP.get(calcDay));
            liveUser = IndicationUtils.getLong(jsonValue.get("programuser_live"));
            vodUser = IndicationUtils.getLong(jsonValue.get("programuser_vod"));
            allUser = IndicationUtils.formatSum(liveUser, vodUser);
        }
        Double rate = IndicationUtils.formatDivide(vodUser * 100, allUser);
        super.addItemResult(super.endTime, IndicationConst.COUNTRY_PROVINCE_CODE, itemEntity.getMirrorColumn(), rate);
    }

    @Override
    public void executeOtherItem() {

    }
}
