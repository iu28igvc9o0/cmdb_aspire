package com.aspire.real_mirror.indication.day;

import com.aspire.common.WsdlUtils;
import com.aspire.mirror.entity.IndicationAllItemEntity;
import com.aspire.mirror.entity.IndicationProvinceEntity;
import com.aspire.mirror.util.IndicationConst;
import com.aspire.mirror.util.IndicationUtils;
import net.sf.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 活跃用户率处理类
 */
public class RtzActiveRateHandler extends AbstractRealDayFactory<RtzActiveRateHandler> {
    @Override
    public String initWsdlMethod() {
        return "getTotalUserStb";
    }

    @Override
    public void initSDK() {
        for(IndicationAllItemEntity itemEntity : indicationEntity.getIndicationItemList()) {
            if (itemEntity.getIndicationItemName().equals("软探针活跃率")) {
                List<String> prevDays = IndicationUtils.getPrevDays(calcDate, IndicationConst.QUERY_MONGODB_DATE_PATTERN, 0);
                String dateStr = prevDays.get(0);
                Map<String, String> activeRateMap = new HashMap<String, String>();
                try {
                    activeRateMap = WsdlUtils.getWsdlServiceReturnMap(this.wsdl, this.wsdlMethod,
                            calcDate, calcDate, null, null, null, null, IndicationConst.QUERY_MONGODB_TYPE_PROVINCE);
                } catch (Exception e) {
                    LOGGER.error("获取指标[{}]-[{}]原始数据错误. WSDL -> {} method -> {}.",
                            dateStr, indicationEntity.getIndicationName(), this.wsdl, this.wsdlMethod, e);
                }
                Double countryUser = 0.0, countryActiveUser = 0.0;
                for (IndicationProvinceEntity entity : PROVINCE_LIST) {
                    if (entity.getProvinceCode().equals(IndicationConst.COUNTRY_PROVINCE_CODE)) {
                        continue;
                    }
                    Long totalUser = 0L, activeUser = 0L;
                    Double activeRate = 0.0;
                    if (activeRateMap.containsKey(entity.getProvinceCode())) {
                        JSONObject jsonValue = JSONObject.fromObject(activeRateMap.get(entity.getProvinceCode()));
                        totalUser = IndicationUtils.getLong(jsonValue.get("totaluser"));
                        activeUser = IndicationUtils.getLong(jsonValue.get("activeuser"));
                        activeRate = IndicationUtils.formatDivide(activeUser * 100, totalUser);
                    }
                    countryUser = IndicationUtils.formatSum(countryUser, totalUser);
                    countryActiveUser = IndicationUtils.formatSum(countryActiveUser, activeUser);
                    String formatUnit = IndicationUtils.formatUnit(activeRate, indicationEntity.getIndicationUnit());
                    submitData(itemEntity, entity.getProvinceCode(), dateStr, formatUnit, activeRate, false);
                }
                Double totalRate = IndicationUtils.formatDivide(countryActiveUser * 100, countryUser);
                String formatUnit = IndicationUtils.formatUnit(totalRate, indicationEntity.getIndicationUnit());
                submitData(itemEntity, IndicationConst.COUNTRY_PROVINCE_CODE, prevDays.get(0), formatUnit, totalRate, false);
            }
        }
    }

    @Override
    public void calcSpecialItem(IndicationAllItemEntity itemEntity) {
    }

    @Override
    public String getEndpointAddress() {
        return WsdlUtils.USER_INFO_SERVER;
    }
}
