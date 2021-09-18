package com.aspire.mirror.indication.hour.gateway;

import com.aspire.common.WsdlUtils;
import com.aspire.mirror.entity.IndicationAllItemEntity;
import com.aspire.mirror.entity.IndicationProvinceEntity;
import com.aspire.mirror.indication.hour.AbstractHourIndicationFactory;
import com.aspire.mirror.util.IndicationConst;
import com.aspire.mirror.util.IndicationUtils;
import net.sf.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActiveUserHandler extends AbstractHourIndicationFactory {

    @Override
    public String initWsdlMethod() {
        return "getTotalUserOsgi";
    }

    @Override
    public void initSDK() {
        for(IndicationAllItemEntity itemEntity : indicationEntity.getIndicationItemList()) {
            if (itemEntity.getIndicationItemName().equals("活跃用户数")) {
                Map<String, String> activeMap = new HashMap<String, String>();
                try {
                    activeMap = WsdlUtils.getWsdlServiceReturnMap(this.wsdl, this.wsdlMethod, calcDate, calcDate, null, null, null,
                            IndicationConst.QUERY_MONGODB_TYPE_PROVINCE);
                } catch (Exception e) {
                    LOGGER.error("获取指标[{}]-[{}]原始数据错误. WSDL -> {} method -> {}.",
                            calcDate, indicationEntity.getIndicationName(), this.wsdl, this.wsdlMethod, e);
                }
                double totalActiveUser = 0.0;
                List<String> sendAlarmProvince = new ArrayList<String>();
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
                    if(activeUser == 0.0 && !isSkipProvince(entity.getProvinceCode())) {
                        sendAlarmProvince.add(entity.getProvinceName());
                    }
                    String formatValue = IndicationUtils.formatEToDouble(activeUser);
                    String formatUnit = IndicationUtils.formatUnit(formatValue, indicationEntity.getIndicationUnit());
                    submitData(itemEntity, entity.getProvinceCode(), calcDate, formatUnit, formatValue, false);
                }
                String formatValue = IndicationUtils.formatEToDouble(totalActiveUser);
                String formatUnit = IndicationUtils.formatUnit(formatValue, indicationEntity.getIndicationUnit());
                //全国的数据
                submitData(itemEntity, IndicationConst.COUNTRY_PROVINCE_CODE, calcDate, formatUnit, formatValue, false);
                //发送告警邮件
                if((sendAlarmProvince.size()>0)) {
                	String title = IndicationConst.HOURVIEWER_CALCULATE_TITLE_TEMPLATE;
                    String model = IndicationConst.HOURVIEWER_CALCULATE_CONTENT_TEMPLATE;
                    String year = calcDate.substring(0,4);
                    String month = calcDate.substring(4,6);
                    String day = calcDate.substring(6,8);
                    String hour = calcDate.substring(8,10);
                    String titleProvince;
                    String modelProvince = "全国";
                    if(sendAlarmProvince.size()>1) {
                        titleProvince = sendAlarmProvince.get(0)+"等";
                    }else {
                        titleProvince = sendAlarmProvince.get(0);
                    }
                    for(int i=0;i<sendAlarmProvince.size();i++) {
                        if(i==0) {
                            modelProvince = sendAlarmProvince.get(0);
                        }else {
                            modelProvince = modelProvince + "、"+sendAlarmProvince.get(i);
                        }
                    }
                    title = String.format(title, year, month,day,hour,indicationEntity.getIndicationCatalog(),titleProvince);
                    model = String.format(model, year, month,day,hour,indicationEntity.getIndicationCatalog(),modelProvince);
                    sendEmail(title, model);
                }
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
