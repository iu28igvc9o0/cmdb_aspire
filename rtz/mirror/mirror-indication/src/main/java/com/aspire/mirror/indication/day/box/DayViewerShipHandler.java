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
 * Date: 2018/11/6
 * 软探针异常指标监控系统 -- 日收视用户数（万）指标
 * DayViewerShipHandler
 */
public class DayViewerShipHandler extends AbstractDayIndicationFactory {
    @Override
    public String initWsdlMethod() {
        return "getTotalUserStb";
    }

    @Override
    public void initSDK() {
        for(IndicationAllItemEntity itemEntity : indicationEntity.getIndicationItemList()) {
            if (itemEntity.getIndicationItemName().equals("软探针部署数")) {
                List<String> prevDays = IndicationUtils.getPrevDays(calcDate, IndicationConst.QUERY_MONGODB_DATE_PATTERN, 0);
                String dateStr = prevDays.get(0);
                Map<String, String> totalUserMap = new HashMap<String, String>();
                try {
                    totalUserMap = WsdlUtils.getWsdlServiceReturnMap(this.wsdl, this.wsdlMethod, dateStr, dateStr, null,
                            null, null, null, IndicationConst.QUERY_MONGODB_TYPE_PROVINCE);
                } catch (Exception e) {
                    LOGGER.error("获取指标[{}]-[{}]原始数据错误. WSDL -> {} method -> {}.",
                            dateStr, indicationEntity.getIndicationName(), this.wsdl, this.wsdlMethod, e);
                }
                Double countryUser = 0.0;
                for (IndicationProvinceEntity entity : PROVINCE_LIST) {
                    if (entity.getProvinceCode().equals(IndicationConst.COUNTRY_PROVINCE_CODE)) {
                        continue;
                    }
                    double totalUser = 0.0;
                    if (totalUserMap.containsKey(entity.getProvinceCode())) {
                        JSONObject totalJson = JSONObject.fromObject(totalUserMap.get(entity.getProvinceCode()));
                        //软探针部署数
                        totalUser = totalJson.getDouble("totaluser");
                    }
                    countryUser = IndicationUtils.formatSum(countryUser, totalUser);
                    String formatValue = IndicationUtils.formatEToDouble(totalUser);
                    String formatUnit = IndicationUtils.formatUnit(totalUser, indicationEntity.getIndicationUnit());
                    submitData(itemEntity, entity.getProvinceCode(), dateStr, formatUnit, formatValue, false);
                }
                String formatValue = IndicationUtils.formatEToDouble(countryUser);
                String formatUnit = IndicationUtils.formatUnit(formatValue, indicationEntity.getIndicationUnit());
                submitData(itemEntity, IndicationConst.COUNTRY_PROVINCE_CODE, dateStr, formatUnit, formatValue, false);
            } else if (itemEntity.getIndicationItemName().equals("收视用户数")) {
                List<String> prevDays = IndicationUtils.getPrevDays(calcDate, IndicationConst.QUERY_MONGODB_DATE_PATTERN, 0);
                Map<String, String> viewingUserMap = new HashMap<String, String>();
                try {
                    viewingUserMap = WsdlUtils.getWsdlServiceReturnMap(this.wsdl, "getViewingUser", prevDays, null,
                            null, null, null, IndicationConst.QUERY_MONGODB_TYPE_PROVINCE);
                } catch (Exception e) {
                    LOGGER.error("获取指标[{}]-[{}]原始数据错误. 接口: IPlatformFactory.getNationalUserInfo.getViewingUser().",
                            prevDays.get(0), indicationEntity.getIndicationName(), e);
                }
                double total = 0.0;
                for (IndicationProvinceEntity entity : PROVINCE_LIST) {
                    if (entity.getProvinceCode().equals(IndicationConst.COUNTRY_PROVINCE_CODE)) {
                        continue;
                    }
                    double programUser = 0.0;
                    if (viewingUserMap.containsKey(entity.getProvinceCode())) {
                        JSONObject programJson = JSONObject.fromObject(viewingUserMap.get(entity.getProvinceCode()));
                        //收视用户数
                        programUser = programJson.getDouble("programuser");
                        total = IndicationUtils.formatSum(total, programUser);
                    }
                    String formatValue = IndicationUtils.formatEToDouble(programUser);
                    String formatUnit = IndicationUtils.formatUnit(formatValue, indicationEntity.getIndicationUnit());
                    submitData(itemEntity, entity.getProvinceCode(), prevDays.get(0), formatUnit, formatValue, false);
                }
                String formatValue = IndicationUtils.formatEToDouble(total);
                String formatUnit = IndicationUtils.formatUnit(formatValue, indicationEntity.getIndicationUnit());
                //全国的数据
                submitData(itemEntity, IndicationConst.COUNTRY_PROVINCE_CODE, prevDays.get(0), formatUnit, formatUnit, false);
            } else if (itemEntity.getIndicationItemName().equals("有线率")) {
                List<String> prevDays = IndicationUtils.getPrevDays(calcDate, IndicationConst.QUERY_MONGODB_DATE_PATTERN, 0);
                String dateStr = prevDays.get(0);
                Map<String, String> viewingUserMap = new HashMap<String, String>();
                try {
                    viewingUserMap = WsdlUtils.getWsdlServiceReturnMap(this.wsdl, "getViewingUser",prevDays, null,
                            null, null, null, IndicationConst.QUERY_MONGODB_TYPE_PROVINCE);
                } catch (Exception e) {
                    LOGGER.error("获取指标[{}]-[{}]原始数据错误. 接口: IPlatformFactory.getNationalUserInfo.getViewingUser().",
                            prevDays.get(0), indicationEntity.getIndicationName(), e);
                }
                Double countryTotal = 0.0;
                Double countryUser = 0.0;
                for (IndicationProvinceEntity entity : PROVINCE_LIST) {
                    if (entity.getProvinceCode().equals(IndicationConst.COUNTRY_PROVINCE_CODE)) {
                        continue;
                    }
                    double programUser_1 = 0.0;
                    double total = 0.0;
                    if (viewingUserMap.containsKey(entity.getProvinceCode())) {
                        JSONObject programJson = JSONObject.fromObject(viewingUserMap.get(entity.getProvinceCode()));
                        //无线收视用户数(个)
                        double programUser_0 = programJson.getDouble("programuser_0");
                        //有线收视用户数(个)
                        programUser_1 = programJson.getDouble("programuser_1");
                        total = IndicationUtils.formatSum(programUser_0, programUser_1);
                    }
                    //有线率
                    String formatValue = IndicationUtils.formatRate(programUser_1, total, 100, 4);
                    submitData(itemEntity, entity.getProvinceCode(), dateStr, formatValue, formatValue, false);
                    countryUser = IndicationUtils.formatSum(countryUser, programUser_1);
                    countryTotal = IndicationUtils.formatSum(countryTotal, total);
                }
                String formatValue = IndicationUtils.formatRate(countryUser, countryTotal, 100, 4);
                submitData(itemEntity, IndicationConst.COUNTRY_PROVINCE_CODE, dateStr, formatValue, formatValue, false);
            }
        }
    }

    @Override
    public void calcSpecialItem(IndicationAllItemEntity itemEntity) {
        if (itemEntity.getIndicationItemName().equals("收视活跃率")) {
            List<String> prevDays = IndicationUtils.getPrevDays(calcDate, IndicationConst.QUERY_MONGODB_DATE_PATTERN, 0);
            String dateStr = prevDays.get(0);
            Map<String, String> totalUserMap = new HashMap<String, String>();
            try {
                totalUserMap = WsdlUtils.getWsdlServiceReturnMap(this.wsdl, this.wsdlMethod,dateStr, dateStr, null,
                        null, null, null, IndicationConst.QUERY_MONGODB_TYPE_PROVINCE);
            } catch (Exception e) {
                LOGGER.error("获取指标[{}]-[{}]原始数据错误. 接口: IPlatformFactory.getNationalUserInfo.getViewingUser().",
                        dateStr, indicationEntity.getIndicationName(), e);
            }
            Double countryUser = 0.0;
            for (IndicationProvinceEntity provinceEntity : PROVINCE_LIST) {
                if (!totalUserMap.containsKey(provinceEntity.getProvinceCode())) {
                    continue;
                }
                JSONObject totalJson = JSONObject.fromObject(totalUserMap.get(provinceEntity.getProvinceCode()));
                //软探针部署数
                Double totalUser = totalJson.getDouble("totaluser");
                countryUser = IndicationUtils.formatSum(countryUser, totalUser);
                String todayValue = getItemData(calcDate, provinceEntity.getProvinceCode(), PRIMARY_ITEM_ID);
                if (todayValue == null) {
                    LOGGER.error("指标[{}]-省份[{}]未获取到收视用户数, 无法计算收视活跃率", indicationEntity.getIndicationName(),
                            provinceEntity.getProvinceCode());
                    continue;
                }
                String formatUnit = IndicationUtils.formatUnit(totalUser, indicationEntity.getIndicationUnit());
                String activeRate = IndicationUtils.formatRate(todayValue, formatUnit, 100, 4);
                submitData(itemEntity, provinceEntity.getProvinceCode(), calcDate, activeRate, activeRate, false);
            }
            String todayValue = getItemData(calcDate, IndicationConst.COUNTRY_PROVINCE_CODE, PRIMARY_ITEM_ID);
            String formatValue = IndicationUtils.formatRate(todayValue, countryUser, 100, 4);
            submitData(itemEntity, IndicationConst.COUNTRY_PROVINCE_CODE, dateStr, formatValue, formatValue, false);
        }
        if (itemEntity.getIndicationItemName().equals("全国权重")) {
            Double total = 0.0;
            for (IndicationProvinceEntity provinceEntity : PROVINCE_LIST) {
                //全国不计算权重
                if (provinceEntity.getProvinceCode().equals(IndicationConst.COUNTRY_PROVINCE_CODE)) {
                    continue;
                }
                String value = getItemData(calcDate, provinceEntity.getProvinceCode(), PRIMARY_ITEM_ID);
                if (value == null) {
                    LOGGER.error("指标[{}]未获取到收视用户数, 无法计算权重", indicationEntity.getIndicationName());
                    total = null;
                    continue;
                }
                total = IndicationUtils.formatSum(total, value);
            }
            for (IndicationProvinceEntity provinceEntity : PROVINCE_LIST) {
                //全国不计算权重
                if (provinceEntity.getProvinceCode().equals(IndicationConst.COUNTRY_PROVINCE_CODE)) {
                    continue;
                }
                String value = getItemData(calcDate, provinceEntity.getProvinceCode(), PRIMARY_ITEM_ID);
                if (value == null) {
                    LOGGER.error("指标[{}]未获取到收视用户数, 无法计算权重", indicationEntity.getIndicationName());
                    value = "0.0";
                }
                String weight = IndicationUtils.formatRate(value, total, 100, 4);
                submitData(itemEntity, provinceEntity.getProvinceCode(), calcDate, weight, weight, false);
            }
        }
    }

    @Override
    public String getEndpointAddress() {
        return WsdlUtils.USER_INFO_SERVER;
    }
}
