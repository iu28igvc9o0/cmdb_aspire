package com.aspire.mirror.indication.realnational.day;

import com.aspire.mirror.util.IndicationConst;
import com.aspire.mirror.util.IndicationUtils;
import com.aspire.mirror.util.TimeUtil;
import com.aspire.mirror.util.WSDLUtil;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.apache.commons.collections.MapUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
public class RtzCoverRateHandler extends AbstractDayFactory<RtzCoverRateHandler> {

    @Override
    public void callWsdl() {

    }

    @Override
    public void executePrimaryItem() {
        List<String> provinceList = new ArrayList<String>(IndicationUtils.PROVINCE_MAP.keySet());
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        WSDLUtil wsdlUtil = new WSDLUtil();
        Map<String, String> totalMap = wsdlUtil.getWsdlServiceReturnMap( classLoader,wsdl,"getTotalUserOsgi",super.beginTime, super.endTime, null, null, "provinceCode" );

        List<String> nextdatelist = new ArrayList<String>();
        String nextdatetime = TimeUtil.getSepcialDay(endTime, "yyyyMMdd", 1, "yyyyMMdd");
        nextdatelist.add(nextdatetime);
        Map<String, String> amap = wsdlUtil.getWsdlServiceReturnMap(classLoader, wsdl,"getOsgiActivation", nextdatelist, null, null, provinceList, null);
        double totalNumber = 0, totalUser = 0, coverRate = 0;
        String json = amap.get(nextdatetime);
        JSONObject jsonObject = JSONObject.fromObject(json);
        for (String province : provinceList) {
            double number = 0, user = 0;
            if (!amap.isEmpty()) {
                JSONObject map = jsonObject.getJSONObject(province);
                if (MapUtils.isNotEmpty(jsonObject)) {
                    if (MapUtils.isNotEmpty(map)) {
                        number = map.containsKey("iHguActivationNumbers") ? map.getDouble( "iHguActivationNumbers" ):0;
                    }
                }
                if (totalMap != null && totalMap.containsKey(province)) {
                    JSONObject userJson = JSONObject.fromObject(totalMap.get(province));
                    user = userJson.containsKey( "totaluser" ) ? userJson.getDouble( "totaluser" ):0;
                    totalUser = IndicationUtils.formatSum( totalUser, user );
                }
                if (number != 0) {
                    coverRate = IndicationUtils.formatDivide( user * 100 , number);
                }
            }
            super.addItemResult(super.endTime, province, itemEntity.getMirrorColumn(), coverRate);
            totalNumber = IndicationUtils.formatSum( totalNumber, number );
        }
        double totalCoverRate = 0;
        if (totalNumber != 0) {
            totalCoverRate = IndicationUtils.formatDivide( totalUser * 100 , totalNumber );
        }
        super.addItemResult(super.endTime, IndicationConst.COUNTRY_PROVINCE_CODE, itemEntity.getMirrorColumn(), totalCoverRate);
    }

    @Override
    public void executeOtherItem() {

    }
}
