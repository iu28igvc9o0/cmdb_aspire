package com.aspire.real_mirror.indication.actual;

import com.aspire.common.WsdlUtils;
import com.aspire.mirror.entity.IndicationAllItemEntity;
import com.aspire.mirror.entity.IndicationProvinceEntity;
import com.aspire.mirror.util.IndicationConst;
import com.aspire.mirror.util.IndicationUtils;
import net.sf.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 无卡顿花屏占比
 */
public class RtzScreenBlurredHandler extends AbstractRealActualFactory<RtzScreenBlurredHandler> {
    @Override
    public String getEndpointAddress() {
        return WsdlUtils.PROGRAM_INFO_SERVER;
    }

    @Override
    public String initWsdlMethod() {
        return "getProgramKartunTimeRate";
    }

    @Override
    public void initSDK() {
        for(IndicationAllItemEntity itemEntity : indicationEntity.getIndicationItemList()) {
            if (itemEntity.getIndicationItemName().equals("卡顿花屏时长占比")) {
                List<String> dateList = new ArrayList<String>();
                dateList.add(endTime);
                Map<String, String> dataMap = new HashMap<String, String>();
                try {
                    dataMap = WsdlUtils.getWsdlServiceReturnMap(this.wsdl, this.wsdlMethod,
                            dateList, null, null, null, null, IndicationConst.QUERY_MONGODB_TYPE_PROVINCE);
                } catch (Exception e) {
                    LOGGER.error("获取指标[{}]-[{}]原始数据错误. WSDL -> {} method -> {}.",
                            endTime, indicationEntity.getIndicationName(), this.wsdl, this.wsdlMethod, e);
                }
                double totalStutterTime = 0.0, totalScreenBlurredTime = 0.0, totalPlayTime = 0.0;
                for (IndicationProvinceEntity entity : PROVINCE_LIST) {
                    if (entity.getProvinceCode().equals(IndicationConst.COUNTRY_PROVINCE_CODE)) {
                        continue;
                    }
                    double stutterTime = 0.0, playTime = 0.0, screenBlurredTime = 0.0;
                    if (dataMap.containsKey(entity.getProvinceCode())) {
                        JSONObject jsonValue = JSONObject.fromObject(dataMap.get(entity.getProvinceCode()));
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
                    submitData(itemEntity, entity.getProvinceCode(), endTime, rate, rate, false);
                }
                double rate = IndicationUtils.formatSubtract(100,
                        IndicationUtils.formatRate(totalStutterTime + totalScreenBlurredTime, totalPlayTime, 100, 4));
                submitData(itemEntity, IndicationConst.COUNTRY_PROVINCE_CODE, endTime, rate, rate, false);
            }
        }
    }

    @Override
    public void calcSpecialItem(IndicationAllItemEntity itemEntity) {

    }
}
