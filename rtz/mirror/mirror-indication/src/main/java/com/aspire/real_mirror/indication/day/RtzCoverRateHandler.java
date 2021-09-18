package com.aspire.real_mirror.indication.day;

import com.aspire.common.EnvConfigProperties;
import com.aspire.common.FactoryUtils;
import com.aspire.common.WsdlUtils;
import com.aspire.mirror.entity.IndicationAllItemEntity;
import com.aspire.mirror.util.IndicationConst;
import com.aspire.mirror.util.IndicationUtils;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class RtzCoverRateHandler extends AbstractRealDayFactory<RtzCoverRateHandler> {
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
            if (itemEntity.getIndicationItemName().equals("软探针覆盖率")) {
                List<String> provinceList = new ArrayList<String>(IndicationUtils.PROVINCE_MAP.keySet());
                Map<String, String> totalMap = new HashMap<String, String>();
                try {
                    totalMap = WsdlUtils.getWsdlServiceReturnMap(this.wsdl,this.wsdlMethod,calcDate, calcDate,
                            null, null, null, null, IndicationConst.QUERY_MONGODB_TYPE_PROVINCE);
                } catch (Exception e) {
                    LOGGER.error("获取指标[{}]-[{}]原始数据错误. WSDL -> {} method -> {}.",
                            calcDate, indicationEntity.getIndicationName(), this.wsdl, "getTotalUserOsgi", e);
                }
                List<Object> configList = null;
                try {
                    EnvConfigProperties envConfigProperties = FactoryUtils.getBean(EnvConfigProperties.class);
                    configList = WsdlUtils.getWsdlServiceReturnList(envConfigProperties.getRealNationalWeb().getWsdl() + WsdlUtils.STB_CONFIG_SERVER,"getStbConfigCntByCodeList", "province", provinceList);
                } catch (Exception e) {
                    LOGGER.error("获取指标[{}]-[{}]原始数据错误. WSDL -> {} method -> {}.",
                            calcDate, indicationEntity.getIndicationName(), this.wsdl, "getStbConfigCntByCodeList", e);
                }
                double totalActiveUser = 0, coverRate = 0, totalNumber = 0;
                for (int index = 0; index < provinceList.size(); index ++) {
                    double number = Double.parseDouble(String.valueOf(configList.get(index)));
                    double activeUser = 0;
                    if (totalMap != null && totalMap.containsKey(provinceList.get(index))) {
                        JSONObject userJson = JSONObject.fromObject(totalMap.get(provinceList.get(index)));
                        activeUser = userJson.containsKey( "activeuser" ) ? userJson.getDouble( "activeuser" ):0;
                        totalActiveUser = IndicationUtils.formatSum( totalActiveUser, activeUser);
                    }
                    if (number != 0) {
                        coverRate = IndicationUtils.formatDivide( activeUser * 100 , number);
                        totalNumber = IndicationUtils.formatSum(totalNumber, number);
                    }
                    String formatUnit = IndicationUtils.formatUnit(coverRate, indicationEntity.getIndicationUnit());
                    submitData(itemEntity, provinceList.get(index), calcDate, formatUnit, coverRate, false);
                }
                double totalCoverRate = 0;
                if (totalNumber != 0) {
                    totalCoverRate = IndicationUtils.formatDivide( totalActiveUser * 100 , totalNumber );
                }
                String formatUnit = IndicationUtils.formatUnit(totalCoverRate, indicationEntity.getIndicationUnit());
                submitData(itemEntity, IndicationConst.COUNTRY_PROVINCE_CODE, calcDate, formatUnit, totalCoverRate, false);
            }
        }
    }

    @Override
    public void calcSpecialItem(IndicationAllItemEntity itemEntity) {

    }
}
