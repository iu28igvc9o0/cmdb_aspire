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
 * 活跃用户量处理类
 */
public class RtzActiveUserHandler extends AbstractRealDayFactory<RtzActiveUserHandler> {
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
            if (itemEntity.getIndicationItemName().equals("软探针活跃量（日）")) {
                List<String> prevDays = IndicationUtils.getPrevDays(calcDate, IndicationConst.QUERY_MONGODB_DATE_PATTERN, 0);
                String dateStr = prevDays.get(0);
                Map<String, String> activeUserMap = new HashMap<String, String>();
                try {
                    activeUserMap = WsdlUtils.getWsdlServiceReturnMap(this.wsdl, this.wsdlMethod,
                            calcDate, calcDate, null, null, null, null, IndicationConst.QUERY_MONGODB_TYPE_PROVINCE);
                } catch (Exception e) {
                    LOGGER.error("获取指标[{}]-[{}]原始数据错误. WSDL -> {} method -> {}.",
                            dateStr, indicationEntity.getIndicationName(), this.wsdl, this.wsdlMethod, e);
                }
                Double countryActiveUser = 0.0;
                for (IndicationProvinceEntity entity : PROVINCE_LIST) {
                    if (entity.getProvinceCode().equals(IndicationConst.COUNTRY_PROVINCE_CODE)) {
                        continue;
                    }
                    Long activeUser = 0L;
                    if (activeUserMap.containsKey(entity.getProvinceCode())) {
                        JSONObject jsonValue = JSONObject.fromObject(activeUserMap.get(entity.getProvinceCode()));
                        activeUser = IndicationUtils.getLong(jsonValue.get("activeuser"));
                    }
                    countryActiveUser = IndicationUtils.formatSum(countryActiveUser, activeUser);
                    String formatUnit = IndicationUtils.formatUnit(activeUser, indicationEntity.getIndicationUnit());
                    submitData(itemEntity, entity.getProvinceCode(), dateStr, formatUnit, activeUser, false);
                }
                String formatUnit = IndicationUtils.formatUnit(countryActiveUser, indicationEntity.getIndicationUnit());
                submitData(itemEntity, IndicationConst.COUNTRY_PROVINCE_CODE, prevDays.get(0), formatUnit, countryActiveUser, false);
            }
        }
    }

    @Override
    public void calcSpecialItem(IndicationAllItemEntity itemEntity) {

    }
}
