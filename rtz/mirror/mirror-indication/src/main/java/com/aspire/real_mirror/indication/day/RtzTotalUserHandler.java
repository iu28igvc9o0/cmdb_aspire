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
 * 积累用户量处理类
 */
public class RtzTotalUserHandler extends AbstractRealDayFactory<RtzTotalUserHandler> {
    @Override
    public String getEndpointAddress() {
        return WsdlUtils.USER_INFO_SERVER;
    }

    @Override
    public String initWsdlMethod() {
        return "getTotalUserStb";
    }

    @Override
    public void initSDK() {
        for(IndicationAllItemEntity itemEntity : indicationEntity.getIndicationItemList()) {
            if (itemEntity.getIndicationItemName().equals("软探针累计量（日）")) {
                List<String> prevDays = IndicationUtils.getPrevDays(calcDate, IndicationConst.QUERY_MONGODB_DATE_PATTERN, 0);
                String dateStr = prevDays.get(0);
                Map<String, String> userMap = new HashMap<String, String>();
                try {
                    userMap = WsdlUtils.getWsdlServiceReturnMap(this.wsdl, this.wsdlMethod,
                            calcDate, calcDate, null, null, null, null, IndicationConst.QUERY_MONGODB_TYPE_PROVINCE);
                } catch (Exception e) {
                    LOGGER.error("获取指标[{}]-[{}]原始数据错误. WSDL -> {} method -> {}.",
                            calcDate, indicationEntity.getIndicationName(), this.wsdl, this.wsdlMethod, e);
                }
                Double countryUser = 0.0;
                for (IndicationProvinceEntity entity : PROVINCE_LIST) {
                    if (entity.getProvinceCode().equals(IndicationConst.COUNTRY_PROVINCE_CODE)) {
                        continue;
                    }
                    Long totalUser = 0L;
                    if (userMap.containsKey(entity.getProvinceCode())) {
                        JSONObject jsonValue = JSONObject.fromObject(userMap.get(entity.getProvinceCode()));
                        totalUser = IndicationUtils.getLong(jsonValue.get("totaluser"));
                    }
                    String formatUnit = IndicationUtils.formatUnit(totalUser, indicationEntity.getIndicationUnit());
                    submitData(itemEntity, entity.getProvinceCode(), dateStr, formatUnit, totalUser, false);
                    countryUser = IndicationUtils.formatSum(countryUser, totalUser);
                }
                String formatUnit = IndicationUtils.formatUnit(countryUser, indicationEntity.getIndicationUnit());
                submitData(itemEntity, IndicationConst.COUNTRY_PROVINCE_CODE, dateStr, formatUnit, countryUser, false);
            }
        }
    }

    @Override
    public void calcSpecialItem(IndicationAllItemEntity itemEntity) {

    }
}
