package com.aspire.real_mirror.indication.hour;

import com.aspire.common.WsdlUtils;
import com.aspire.mirror.entity.IndicationAllItemEntity;
import com.aspire.mirror.util.IndicationConst;
import com.aspire.mirror.util.IndicationUtils;
import net.sf.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 收视用户数（昨日）（个）
 */
public class RtzYesterdayViewingUserHandler extends AbstractRealHourFactory<RtzYesterdayViewingUserHandler> {

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
            if (itemEntity.getIndicationItemName().equals("收视用户数（昨日）")) {
                List<String> prevDays  = IndicationUtils.getPrevDay(endTime.substring(0, 8), IndicationConst.QUERY_MONGODB_DATE_PATTERN);
                List<String> dateList = IndicationUtils.getDateListALL(prevDays.get(0), "hour");
                Map<String, String> userMap = new HashMap<String, String>();
                try {
                    userMap = WsdlUtils.getWsdlServiceReturnMap(this.wsdl, this.wsdlMethod, dateList, null,
                            null, null, null, IndicationConst.QUERY_MONGODB_TYPE_COUNTRY);
                } catch (Exception e) {
                    LOGGER.error("获取指标[{}]-[{}]原始数据错误. WSDL -> {} method -> {}.",
                            endTime, indicationEntity.getIndicationName(), this.wsdl, this.wsdlMethod, e);
                }
                String hour = endTime.substring(8, 10);
                long programUser = 0L;
                if (userMap.containsKey(prevDays.get(0) + hour)) {
                    JSONObject jsonValue = JSONObject.fromObject(userMap.get(prevDays.get(0) + hour));
                    programUser = IndicationUtils.getLong(jsonValue.get("programuser"));
                }
                String formatUnit = IndicationUtils.formatUnit(programUser, indicationEntity.getIndicationUnit());
                submitData(itemEntity, IndicationConst.COUNTRY_PROVINCE_CODE, endTime, formatUnit, programUser, false);
            }
        }
    }

    @Override
    public void calcSpecialItem(IndicationAllItemEntity itemEntity) {

    }
}
