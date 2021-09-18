package com.aspire.mirror.indication.realnational.actual;

import com.aspire.mirror.util.IndicationConst;
import com.aspire.mirror.util.IndicationUtils;
import com.aspire.mirror.util.WSDLUtil;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * EPG响应成功率
 */
@Slf4j
public class RtzEpgSuccessHandler extends AbstractActualFactory<RtzEpgSuccessHandler> {

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
        long epgReqCntSum = 0L, epgReqReachCntSum = 0;
        log.info("EPG Map size : {}", super.SDK_RESULT_MAP == null ? 0 : super.SDK_RESULT_MAP.size());
        if (super.SDK_RESULT_MAP != null && super.SDK_RESULT_MAP.size() > 0) {
            log.info("EPG Map data: {}", JSONObject.fromObject(super.SDK_RESULT_MAP).toString());
            try {
                JSONObject jsonValue = JSONObject.fromObject(super.SDK_RESULT_MAP.get(calcDay));
                epgReqCntSum = IndicationUtils.getLong(jsonValue.get("epgReqCntSum"));
                epgReqReachCntSum = IndicationUtils.getLong(jsonValue.get("epgReqReachCntSum"));
            } catch (Exception e) {
                log.error("Parse json data error.", e);
            }
        }
        Double successRate = IndicationUtils.formatDivide(epgReqReachCntSum * 100, epgReqCntSum);
        log.info("EPG epgReqCntSum {} epgReqReachCntSum {} successRate {} ", epgReqCntSum, epgReqReachCntSum, successRate);
        super.addItemResult(super.endTime, IndicationConst.COUNTRY_PROVINCE_CODE, itemEntity.getMirrorColumn(), successRate);
    }

    @Override
    public void executeOtherItem() {

    }
}
