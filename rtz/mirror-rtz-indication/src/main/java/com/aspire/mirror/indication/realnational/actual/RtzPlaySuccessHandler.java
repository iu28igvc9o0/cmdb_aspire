package com.aspire.mirror.indication.realnational.actual;

import com.aspire.mirror.util.IndicationConst;
import com.aspire.mirror.util.IndicationUtils;
import com.aspire.mirror.util.WSDLUtil;
import net.sf.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 播放成功率
 */
public class RtzPlaySuccessHandler extends AbstractActualFactory<RtzPlaySuccessHandler> {
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
        long playCntSum = 0L, playSuccessCntSum = 0;
        if (super.SDK_RESULT_MAP != null && super.SDK_RESULT_MAP.size() > 0) {
            JSONObject jsonValue = JSONObject.fromObject(super.SDK_RESULT_MAP.get(calcDay));
            playCntSum = IndicationUtils.getLong(jsonValue.get("playCntSum"));
            playSuccessCntSum = IndicationUtils.getLong(jsonValue.get("playSuccessCntSum"));
        }
        Double playRate = IndicationUtils.formatDivide(playSuccessCntSum * 100, playCntSum);
        super.addItemResult(super.endTime, IndicationConst.COUNTRY_PROVINCE_CODE, itemEntity.getMirrorColumn(), playRate);
    }

    @Override
    public void executeOtherItem() {

    }
}
