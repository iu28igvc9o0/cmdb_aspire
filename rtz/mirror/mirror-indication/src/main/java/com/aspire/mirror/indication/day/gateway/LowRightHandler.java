package com.aspire.mirror.indication.day.gateway;

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
 * 软探针异常指标监控系统 - 网关弱光指标
 * LowRightHandler
 */
public class LowRightHandler extends AbstractDayIndicationFactory {
    @Override
    public String initWsdlMethod() {
        return "getPonInfo";
    }

    @Override
    public void initSDK() {
        for(IndicationAllItemEntity itemEntity : indicationEntity.getIndicationItemList()) {
            if (itemEntity.getIndicationItemName().equals("接收光功率达标率")) {
                List<String> prevDays = IndicationUtils.getPrevDays(calcDate, IndicationConst.QUERY_MONGODB_DATE_PATTERN, 0);
                String dateStr = prevDays.get(0);
                Map<String, String> screenMap = new HashMap<String, String>();
                try {
                    screenMap = WsdlUtils.getWsdlServiceReturnMap(this.wsdl, this.wsdlMethod, prevDays, null,
                            null, null, null, IndicationConst.QUERY_MONGODB_TYPE_PROVINCE);
                } catch (Exception e) {
                    LOGGER.error("获取指标[{}]-[{}]原始数据错误. WSDL -> {} method -> {}.",
                            dateStr, indicationEntity.getIndicationName(), this.wsdl, this.wsdlMethod, e);
                }
                Double totalDBUser = 0.0;
                Double totalActiveUser = 0.0;
                JSONObject screenJson = JSONObject.fromObject(screenMap.get(calcDate));
                for (IndicationProvinceEntity entity : PROVINCE_LIST) {
                    if (entity.getProvinceCode().equals(IndicationConst.COUNTRY_PROVINCE_CODE)) {
                        continue;
                    }
                    double resetRate = 0.0;
                    if (screenJson.containsKey(entity.getProvinceCode())) {
                        JSONObject json = screenJson.getJSONObject(entity.getProvinceCode());
                        //接收光功率达标率(%)
                        Double dbUser = json.getDouble("allPONRX27dbuser");
                        Double activeUser = json.getDouble("activeuser");
                        totalDBUser = IndicationUtils.formatSum(totalDBUser, dbUser);
                        totalActiveUser = IndicationUtils.formatSum(totalActiveUser, activeUser);
                        String rate = IndicationUtils.formatRate(dbUser, activeUser, 100, 4);
                        resetRate = IndicationUtils.formatSubtract(100, rate);
                    }
                    submitData(itemEntity, entity.getProvinceCode(), dateStr, resetRate, resetRate, false);
                }
                String rate = IndicationUtils.formatRate(totalDBUser, totalActiveUser, 100, 4);
                double resetRate = IndicationUtils.formatSubtract(100, rate);
                submitData(itemEntity, IndicationConst.COUNTRY_PROVINCE_CODE, prevDays.get(0), resetRate, resetRate, false);
            }
        }
    }

    @Override
    public void calcSpecialItem(IndicationAllItemEntity itemEntity) {
        if (itemEntity.getIndicationItemName().equals("日活网关数")) {
            String dateStr = IndicationUtils.parseDate(calcDate, IndicationConst.QUERY_MONGODB_DATE_PATTERN);
            Map<String, String> activeMap = new HashMap<String, String>();
            try {
                activeMap = WsdlUtils.getWsdlServiceReturnMap(this.wsdl, "getTotalUserOsgi",dateStr, dateStr,
                        null, null, null, IndicationConst.QUERY_MONGODB_TYPE_PROVINCE);
            } catch (Exception e) {
                LOGGER.error("获取指标[{}]-[{}]原始数据错误. 接口: IPlatformFactory.getNationalOsgiInfo.getTotalUserOsgi().",
                        dateStr, indicationEntity.getIndicationName(), e);
            }
            for (IndicationProvinceEntity entity : PROVINCE_LIST) {
                if (entity.getProvinceCode().equals(IndicationConst.COUNTRY_PROVINCE_CODE)) {
                    continue;
                }
                String formatValue = "0.0";
                if (activeMap.containsKey(entity.getProvinceCode())) {
                    JSONObject json = JSONObject.fromObject(activeMap.get(entity.getProvinceCode()));
                    //日活网关数
                    double activeUser = json.getDouble("activeuser");
                    formatValue = IndicationUtils.formatRate(activeUser, 10000, 1, 4);
                }
                submitData(itemEntity, entity.getProvinceCode(), dateStr, formatValue, formatValue, false);
            }
        }
    }

    @Override
    public String getEndpointAddress() {
        return WsdlUtils.OSGI_INFO_SERVER;
    }
}
