package com.aspire.mirror.indication.realnational.actual;

import com.aspire.mirror.util.IndicationConst;
import com.aspire.mirror.util.IndicationUtils;
import com.aspire.mirror.util.WSDLUtil;
import net.sf.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 无卡顿花屏占比
 */
public class RtzScreenBlurredHandler extends AbstractActualFactory<RtzScreenBlurredHandler> {
    @Override
    public void callWsdl() {
        List<String> dateList = new ArrayList<String>();
        dateList.add(endTime);
        WSDLUtil wsdlUtil = new WSDLUtil();
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        super.SDK_RESULT_MAP = wsdlUtil.getWsdlServiceReturnMap(classLoader, wsdl, itemEntity.getMethod(),
                dateList, null, null, null, null, IndicationConst.QUERY_MONGODB_TYPE_PROVINCE);
    }

    @Override
    public void executePrimaryItem() {
        double totalStutterTime = 0.0, totalScreenBlurredTime = 0.0, totalPlayTime = 0.0;
        for (String provinceCode : super.PROVINCE_LIST) {
            double stutterTime = 0.0, playTime = 0.0, screenBlurredTime = 0.0;
            if (super.SDK_RESULT_MAP != null && super.SDK_RESULT_MAP.containsKey(provinceCode)) {
                JSONObject jsonValue = JSONObject.fromObject(super.SDK_RESULT_MAP.get(provinceCode));
                //无卡顿花屏时长占比
                stutterTime = jsonValue.getDouble("stuttertime");
                playTime = jsonValue.getDouble("playtime");
                screenBlurredTime = jsonValue.getDouble("screenblurredtime");
                totalPlayTime = IndicationUtils.formatSum(totalPlayTime, playTime);
                totalScreenBlurredTime = IndicationUtils.formatSum(totalScreenBlurredTime, screenBlurredTime);
                totalStutterTime = IndicationUtils.formatSum(totalStutterTime, stutterTime);
            }
            double rate = IndicationUtils.formatSubtract(100,
                    IndicationUtils.formatRate(stutterTime + screenBlurredTime, playTime, 100, 4));
            super.addItemResult(super.endTime, provinceCode, itemEntity.getMirrorColumn(), rate);
        }
        double rate = IndicationUtils.formatSubtract(100,
                IndicationUtils.formatRate(totalStutterTime + totalScreenBlurredTime, totalPlayTime, 100, 4));
        super.addItemResult(super.endTime, IndicationConst.COUNTRY_PROVINCE_CODE, itemEntity.getMirrorColumn(), rate);
    }

    @Override
    public void executeOtherItem() {

    }
}
