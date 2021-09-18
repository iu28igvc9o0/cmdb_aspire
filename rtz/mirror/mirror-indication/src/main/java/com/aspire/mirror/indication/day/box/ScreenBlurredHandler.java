package com.aspire.mirror.indication.day.box;

import com.aspire.common.WsdlUtils;
import com.aspire.mirror.entity.IndicationAllItemEntity;
import com.aspire.mirror.entity.IndicationProvinceEntity;
import com.aspire.mirror.indication.day.AbstractDayIndicationFactory;
import com.aspire.mirror.util.IndicationConst;
import com.aspire.mirror.util.IndicationUtils;
import net.sf.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: jw.zhu
 * Date: 2018/11/10
 * 软探针异常指标监控系统 - 无卡顿花屏收视用户占比指标
 * FirstLoadAvgHandler
 */
public class ScreenBlurredHandler extends AbstractDayIndicationFactory {
    @Override
    public String initWsdlMethod() {
        return "getProgramKartunTimeRate";
    }

    @Override
    public void initSDK() {
        for(IndicationAllItemEntity itemEntity : indicationEntity.getIndicationItemList()) {
            if (itemEntity.getIndicationItemName().equals("卡顿花屏时长占比")) {
                List<String> prevDays = IndicationUtils.getPrevDays(calcDate, IndicationConst.QUERY_MONGODB_DATE_PATTERN, 0);
                String dateStr = prevDays.get(0);
                Map<String, String> screenMap = new HashMap<String, String>();
                try {
                    screenMap = WsdlUtils.getWsdlServiceReturnMap(this.wsdl,this.wsdlMethod, prevDays, null,
                            null, null, null, IndicationConst.QUERY_MONGODB_TYPE_PROVINCE);
                } catch (Exception e) {
                    LOGGER.error("获取指标[{}]-[{}]原始数据错误. WSDL -> {} method -> {}.",
                            dateStr, indicationEntity.getIndicationName(), this.wsdl, this.wsdlMethod, e);
                }
                double totalStutterTime = 0.0;
                double totalScreenBlurredTime = 0.0;
                double totalPlayTime = 0.0;
                for (IndicationProvinceEntity entity : PROVINCE_LIST) {
                    if (entity.getProvinceCode().equals(IndicationConst.COUNTRY_PROVINCE_CODE)) {
                        continue;
                    }
                    String temp = "0";
                    if (screenMap.containsKey(entity.getProvinceCode())) {
                        JSONObject json = JSONObject.fromObject(screenMap.get(entity.getProvinceCode()));
                        //无卡顿花屏时长占比
                        double stutterTime = json.getDouble("stuttertime");
                        double playTime = json.getDouble("playtime");
                        double screenBlurredTime = json.getDouble("screenblurredtime");
                        totalPlayTime = IndicationUtils.formatSum(totalPlayTime, playTime);
                        totalScreenBlurredTime = IndicationUtils.formatSum(totalScreenBlurredTime, screenBlurredTime);
                        totalStutterTime = IndicationUtils.formatSum(totalStutterTime, stutterTime);
                        temp = IndicationUtils.formatRate(stutterTime + screenBlurredTime, playTime, 100, 4);
                    }
                    double rate = IndicationUtils.formatSubtract(100, temp);
                    submitData(itemEntity, entity.getProvinceCode(), dateStr, rate, rate, false);
                }
                String stutterBlurred = IndicationUtils.formatRate(totalStutterTime + totalScreenBlurredTime,
                        totalPlayTime, 100, null);
                double resetRate = IndicationUtils.formatSubtract(100, stutterBlurred);
                //全国的数据
                submitData(itemEntity, IndicationConst.COUNTRY_PROVINCE_CODE, prevDays.get(0), resetRate, resetRate, false);
            }
        }
    }

    @Override
    public void calcSpecialItem(IndicationAllItemEntity itemEntity) {

    }

    @Override
    public String getEndpointAddress() {
        return WsdlUtils.PROGRAM_INFO_SERVER;
    }
}
