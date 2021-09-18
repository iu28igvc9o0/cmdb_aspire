package com.aspire.mirror.indication.day.gateway;

import com.aspire.common.WsdlUtils;
import com.aspire.mirror.entity.IndicationAllItemEntity;
import com.aspire.mirror.entity.IndicationProvinceEntity;
import com.aspire.mirror.indication.day.AbstractDayIndicationFactory;
import com.aspire.mirror.util.IndicationConst;
import com.aspire.mirror.util.IndicationUtils;
import net.sf.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: jw.zhu
 * Date: 2018/11/10
 * 软探针异常指标监控系统 - 网关活跃用户数指标
 * ActiveUserHandler
 */
public class ActiveUserHandler extends AbstractDayIndicationFactory {
    @Override
    public String initWsdlMethod() {
        return "getTotalUserOsgi";
    }

    @Override
    public void initSDK() {
        for(IndicationAllItemEntity itemEntity : indicationEntity.getIndicationItemList()) {
            if (itemEntity.getIndicationItemName().equals("日活网关数")) {
                String dateStr = IndicationUtils.parseDate(calcDate, IndicationConst.QUERY_MONGODB_DATE_PATTERN);
                Map<String, String> activeMap = new HashMap<String, String>();
                try {
                    activeMap = WsdlUtils.getWsdlServiceReturnMap(this.wsdl, this.wsdlMethod,dateStr, dateStr,
                            null, null, null, IndicationConst.QUERY_MONGODB_TYPE_PROVINCE);
                } catch (Exception e) {
                    LOGGER.error("获取指标[{}]-[{}]原始数据错误. WSDL -> {} method -> {}.",
                            dateStr, indicationEntity.getIndicationName(), this.wsdl, this.wsdlMethod, e);
                }
                Double totalActiveUser = 0.0;
                for (IndicationProvinceEntity entity : PROVINCE_LIST) {
                    if (entity.getProvinceCode().equals(IndicationConst.COUNTRY_PROVINCE_CODE)) {
                        continue;
                    }
                    double activeUser = 0.0;
                    if (activeMap.containsKey(entity.getProvinceCode())) {
                        JSONObject json = JSONObject.fromObject(activeMap.get(entity.getProvinceCode()));
                        //日活网关数
                        activeUser = json.getDouble("activeuser");
                        totalActiveUser = IndicationUtils.formatSum(totalActiveUser, activeUser);
                    }
                    String formatValue = IndicationUtils.formatEToDouble(activeUser);
                    String formatUnit = IndicationUtils.formatUnit(formatValue, indicationEntity.getIndicationUnit());
                    submitData(itemEntity, entity.getProvinceCode(), dateStr, formatUnit, formatValue, false);
                }
                String formatValue = IndicationUtils.formatEToDouble(totalActiveUser);
                String formatUnit = IndicationUtils.formatUnit(formatValue, indicationEntity.getIndicationUnit());
                //全国的数据
                submitData(itemEntity, IndicationConst.COUNTRY_PROVINCE_CODE, dateStr, formatUnit, formatValue, false);
            }
        }
    }

    @Override
    public void calcSpecialItem(IndicationAllItemEntity itemEntity) {

    }

    @Override
    public String getEndpointAddress() {
        return WsdlUtils.OSGI_INFO_SERVER;
    }
}
