package com.aspire.real_mirror.indication.actual;

import com.aspire.common.WsdlUtils;
import com.aspire.mirror.entity.IndicationAllItemEntity;
import com.aspire.mirror.util.IndicationConst;
import com.aspire.mirror.util.IndicationUtils;
import net.sf.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 无线用户占比
 */
public class RtzNoLineUserHandler extends AbstractRealActualFactory<RtzNoLineUserHandler> {
    @Override
    public String getEndpointAddress() {
        return WsdlUtils.PROGRAM_INFO_SERVER;
    }

    @Override
    public String initWsdlMethod() {
        return "getStbAllIndex";
    }

    @Override
    public void initSDK() {
        for(IndicationAllItemEntity itemEntity : indicationEntity.getIndicationItemList()) {
            if (itemEntity.getIndicationItemName().equals("无线用户数")) {
                List<String> dateList = new ArrayList<String>();
                dateList.add(endTime.substring(0,8));
                Map<String, String> noLineMap = new HashMap<String, String>();
                try {
                    noLineMap = WsdlUtils.getWsdlServiceReturnMap(this.wsdl, this.wsdlMethod, dateList,
                            null, null, null, null, IndicationConst.QUERY_MONGODB_TYPE_ALL);
                } catch (Exception e) {
                    LOGGER.error("获取指标[{}]-[{}]原始数据错误. WSDL -> {} method -> {}.",
                            endTime, indicationEntity.getIndicationName(), this.wsdl, this.wsdlMethod, e);
                }
                String calcDay = endTime.substring(0,8);
                long lineUser = 0L, noLineUser = 0;
                Double allUser = 0.0;
                if (noLineMap != null && noLineMap.size() > 0) {
                    JSONObject jsonValue = JSONObject.fromObject(noLineMap.get(calcDay));
                    noLineUser = IndicationUtils.getLong(jsonValue.get("programuser_0"));
                    lineUser = IndicationUtils.getLong(jsonValue.get("programuser_1"));
                    allUser = IndicationUtils.formatSum(noLineUser, lineUser);
                }
                Double rate = IndicationUtils.formatDivide(noLineUser * 100, allUser);
                String formatUnit = IndicationUtils.formatUnit(rate, indicationEntity.getIndicationUnit());
                submitData(itemEntity, IndicationConst.COUNTRY_PROVINCE_CODE, endTime, formatUnit, rate, false);
            }
        }
    }

    @Override
    public void calcSpecialItem(IndicationAllItemEntity itemEntity) {

    }
}
