package com.aspire.mirror.indication.hour.box;

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

/**
 * Created with IntelliJ IDEA.
 * User: jw.zhu
 * Date: 2018/11/6
 * 软探针异常指标监控系统 -- 日收视用户数（万）指标
 * DayViewerShipHandler
 */
public class DayViewerShipHandler extends AbstractHourIndicationFactory {
    @Override
    public String initWsdlMethod() {
        return "getViewingUser";
    }

    @Override
    public void initSDK() {
        for(IndicationAllItemEntity itemEntity : indicationEntity.getIndicationItemList()) {
            if (itemEntity.getIndicationItemName().equals("收视用户数")) {
                List<String> dateList = new ArrayList<String>();
                dateList.add(calcDate);
                Map<String, String> viewingUserMap = new HashMap<String, String>();
                try {
                    viewingUserMap = WsdlUtils.getWsdlServiceReturnMap(this.wsdl, this.wsdlMethod, dateList, null,
                            null, null, null, IndicationConst.QUERY_MONGODB_TYPE_PROVINCE);
                } catch (Exception e) {
                    LOGGER.error("获取指标[{}]-[{}]原始数据错误. WSDL -> {} method -> {}.",
                            dateList.get(0), indicationEntity.getIndicationName(), this.wsdl, this.wsdlMethod, e);
                }
                double total = 0.0;
                List<String> provinceList = new ArrayList<String>();
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
                    String formatUnit = IndicationUtils.formatUnit(programUser, indicationEntity.getIndicationUnit());
                    submitData(itemEntity, entity.getProvinceCode(), calcDate, formatUnit, formatUnit, false);
                    if(programUser == 0.0 && !isSkipProvince(entity.getProvinceCode())) {
                        provinceList.add(entity.getProvinceName());
                    }
                }
                String formatUnit = IndicationUtils.formatUnit(total, indicationEntity.getIndicationUnit());
                //全国的数据
                submitData(itemEntity, IndicationConst.COUNTRY_PROVINCE_CODE, calcDate, formatUnit, formatUnit, false);
                //发送邮件
                if(provinceList.size() > 0) {
                	String title = IndicationConst.HOURVIEWER_CALCULATE_TITLE_TEMPLATE;
                    String model = IndicationConst.HOURVIEWER_CALCULATE_CONTENT_TEMPLATE;
                    String year = calcDate.substring(0,4);
                    String month = calcDate.substring(4,6);
                    String day = calcDate.substring(6,8);
                    String hour = calcDate.substring(8,10);
                    String titleProvince = "全国";
                    String modelProvince = "全国";
                    if(provinceList.size()>1) {
                        titleProvince = provinceList.get(0)+"等";
                    }else {
                        titleProvince = provinceList.get(0);
                    }
                    for(int i=0;i<provinceList.size();i++) {
                        if(i==0) {
                            modelProvince = provinceList.get(0);
                        }else {
                            modelProvince = modelProvince + "、"+provinceList.get(i);
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
        return WsdlUtils.USER_INFO_SERVER;
    }
}
