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
 * 直播用户占比（%）
 */
public class RtzLiveProgramUserHandler extends AbstractRealActualFactory<RtzLiveProgramUserHandler> {

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
            if (itemEntity.getIndicationItemName().equals("直播用户占比")) {
                List<String> dateList = new ArrayList<String>();
                dateList.add(endTime.substring(0,8));
                Map<String, String> liveMap = new HashMap<String, String>();
                try {
                    liveMap = WsdlUtils.getWsdlServiceReturnMap(this.wsdl, this.wsdlMethod, dateList,
                            null, null, null, null, IndicationConst.QUERY_MONGODB_TYPE_ALL);
                } catch (Exception e) {
                    LOGGER.error("获取指标[{}]-[{}]原始数据错误. WSDL -> {} method -> {}.",
                            endTime, indicationEntity.getIndicationName(), this.wsdl, this.wsdlMethod, e);
                }
                String calcDay = endTime.substring(0,8);
                long liveUser = 0L, vodUser = 0;
                Double allUser = 0.0;
                if (liveMap != null && liveMap.size() > 0) {
                    JSONObject jsonValue = JSONObject.fromObject(liveMap.get(calcDay));
                    liveUser = IndicationUtils.getLong(jsonValue.get("programuser_live"));
                    vodUser = IndicationUtils.getLong(jsonValue.get("programuser_vod"));
                    allUser = IndicationUtils.formatSum(liveUser, vodUser);
                }
                Double rate = IndicationUtils.formatDivide(liveUser * 100, allUser);
                String formatUnit = IndicationUtils.formatUnit(rate, indicationEntity.getIndicationUnit());
                submitData(itemEntity, IndicationConst.COUNTRY_PROVINCE_CODE, endTime, formatUnit, rate, false);
            }
        }
    }

    @Override
    public void calcSpecialItem(IndicationAllItemEntity itemEntity) {

    }
}
