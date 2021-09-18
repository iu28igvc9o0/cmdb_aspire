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
 * 软探针异常指标监控系统 - 播放成功率指标
 * PlaySuccessHandler
 */
public class PlaySuccessHandler extends AbstractDayIndicationFactory {
    @Override
    public String initWsdlMethod() {
        return "getProgramPlaySuccessRate";
    }

    @Override
    public void initSDK() {
        for(IndicationAllItemEntity itemEntity : indicationEntity.getIndicationItemList()) {
            if (itemEntity.getIndicationItemName().equals("播放成功率")) {
                List<String> prevDays = IndicationUtils.getPrevDays(calcDate, IndicationConst.QUERY_MONGODB_DATE_PATTERN, 0);
                String dateStr = prevDays.get(0);
                Map<String, String> playMap = new HashMap<String, String>();
                try {
                    playMap = WsdlUtils.getWsdlServiceReturnMap(this.wsdl, this.wsdlMethod, prevDays, null,
                            null, null, null, IndicationConst.QUERY_MONGODB_TYPE_PROVINCE);
                } catch (Exception e) {
                    LOGGER.error("获取指标[{}]-[{}]原始数据错误. WSDL -> {} method -> {}.",
                            dateStr, indicationEntity.getIndicationName(), this.wsdl, this.wsdlMethod, e);
                }
                Double totalPlaySuccessCntSum = 0.0;
                Double totalPlayCntSum = 0.0;
                for (IndicationProvinceEntity entity : PROVINCE_LIST) {
                    if (entity.getProvinceCode().equals(IndicationConst.COUNTRY_PROVINCE_CODE)) {
                        continue;
                    }
                    double playSuccessCntSum = 0.0;
                    double playCntSum = 0.0;
                    if (playMap.containsKey(entity.getProvinceCode())) {
                        JSONObject playJson = JSONObject.fromObject(playMap.get(entity.getProvinceCode()));
                        //播放成功率
                        playSuccessCntSum = playJson.getDouble("playSuccessCntSum");
                        playCntSum = playJson.getDouble("playCntSum");
                        totalPlaySuccessCntSum = IndicationUtils.formatSum(totalPlaySuccessCntSum, playSuccessCntSum);
                        totalPlayCntSum = IndicationUtils.formatSum(totalPlayCntSum, playCntSum);
                    }
                    String playSuccess = IndicationUtils.formatRate(playSuccessCntSum, playCntSum, 100, null);
                    String formatUnit = IndicationUtils.formatUnit(playSuccess, indicationEntity.getIndicationUnit());
                    submitData(itemEntity, entity.getProvinceCode(), dateStr, formatUnit, playSuccess, false);
                }
                String totalPlaySuccess = IndicationUtils.formatRate(totalPlaySuccessCntSum, totalPlayCntSum, 100, null);
                String formatUnit = IndicationUtils.formatUnit(totalPlaySuccess, indicationEntity.getIndicationUnit());
                //全国的数据
                submitData(itemEntity, IndicationConst.COUNTRY_PROVINCE_CODE, prevDays.get(0), formatUnit, totalPlaySuccess, false);
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
