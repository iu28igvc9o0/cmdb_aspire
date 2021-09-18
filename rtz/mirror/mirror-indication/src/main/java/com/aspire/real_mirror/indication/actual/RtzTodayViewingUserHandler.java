package com.aspire.real_mirror.indication.actual;

import com.aspire.common.WsdlUtils;
import com.aspire.mirror.entity.IndicationAllItemEntity;
import com.aspire.mirror.entity.IndicationProvinceEntity;
import com.aspire.mirror.util.IndicationConst;
import com.aspire.mirror.util.IndicationUtils;
import net.sf.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 收视用户数（今日）（个）
 */
public class RtzTodayViewingUserHandler extends AbstractRealActualFactory<RtzTodayViewingUserHandler> {
    @Override
    public String getEndpointAddress() {
        return WsdlUtils.USER_INFO_SERVER;
    }

    @Override
    public String initWsdlMethod() {
        return "getViewingUser";
    }

    @Override
    public void initSDK() {
        for(IndicationAllItemEntity itemEntity : indicationEntity.getIndicationItemList()) {
            if (itemEntity.getIndicationItemName().equals("收视用户数")) {
                List<String> dateList = new ArrayList<String>();
                dateList.add(endTime.substring(0,8));
                Map<String, String> dataMap = new HashMap<String, String>();
                try {
                    dataMap = WsdlUtils.getWsdlServiceReturnMap(this.wsdl, this.wsdlMethod,
                            dateList, null, null, null, null, IndicationConst.QUERY_MONGODB_TYPE_PROVINCE);
                } catch (Exception e) {
                    LOGGER.error("获取指标[{}]-[{}]原始数据错误. WSDL -> {} method -> {}.",
                            endTime, indicationEntity.getIndicationName(), this.wsdl, this.wsdlMethod, e);
                }
                Double totalProgramUser = 0.0;
                for (IndicationProvinceEntity entity : PROVINCE_LIST) {
                    if (entity.getProvinceCode().equals(IndicationConst.COUNTRY_PROVINCE_CODE)) {
                        continue;
                    }
                    long programUser = 0L;
                    if (dataMap != null && dataMap.containsKey(entity.getProvinceCode())) {
                        JSONObject jsonValue = JSONObject.fromObject(dataMap.get(entity.getProvinceCode()));
                        programUser = IndicationUtils.getLong(jsonValue.get("programuser"));
                    }
                    String formatUnit = IndicationUtils.formatUnit(programUser, indicationEntity.getIndicationUnit());
                    submitData(itemEntity, entity.getProvinceCode(), endTime, formatUnit, programUser, false);
                    totalProgramUser = IndicationUtils.formatSum(totalProgramUser, programUser);
                }
                String formatUnit = IndicationUtils.formatUnit(totalProgramUser, indicationEntity.getIndicationUnit());
                submitData(itemEntity, IndicationConst.COUNTRY_PROVINCE_CODE, endTime, formatUnit, totalProgramUser, false);
            }
        }
    }

    @Override
    public void calcSpecialItem(IndicationAllItemEntity itemEntity) {

    }
}
