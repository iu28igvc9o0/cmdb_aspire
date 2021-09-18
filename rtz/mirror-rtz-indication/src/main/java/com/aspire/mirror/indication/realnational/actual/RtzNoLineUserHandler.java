package com.aspire.mirror.indication.realnational.actual;

import com.aspire.mirror.util.IndicationConst;
import com.aspire.mirror.util.IndicationUtils;
import com.aspire.mirror.util.WSDLUtil;
import net.sf.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 无线用户占比
 */
public class RtzNoLineUserHandler extends AbstractActualFactory<RtzNoLineUserHandler> {
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
        long lineUser = 0L, noLineUser = 0;
        Double allUser = 0.0;
        if (super.SDK_RESULT_MAP != null && super.SDK_RESULT_MAP.size() > 0) {
            JSONObject jsonValue = JSONObject.fromObject(super.SDK_RESULT_MAP.get(calcDay));
            noLineUser = IndicationUtils.getLong(jsonValue.get("programuser_0"));
            lineUser = IndicationUtils.getLong(jsonValue.get("programuser_1"));
            allUser = IndicationUtils.formatSum(noLineUser, lineUser);
        }
        Double rate = IndicationUtils.formatDivide(noLineUser * 100, allUser);
        super.addItemResult(super.endTime, IndicationConst.COUNTRY_PROVINCE_CODE, itemEntity.getMirrorColumn(), rate);
    }

    @Override
    public void executeOtherItem() {

    }
}
