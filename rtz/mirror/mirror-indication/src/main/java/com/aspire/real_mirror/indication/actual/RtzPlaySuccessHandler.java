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
 * 播放成功率
 */
public class RtzPlaySuccessHandler extends AbstractRealActualFactory<RtzPlaySuccessHandler> {
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
            if (itemEntity.getIndicationItemName().equals("播放成功率")) {
                List<String> dateList = new ArrayList<String>();
                dateList.add(endTime.substring(0,8));
                Map<String, String> dataMap = new HashMap<String, String>();
                try {
                    dataMap = WsdlUtils.getWsdlServiceReturnMap(this.wsdl, this.wsdlMethod, dateList,
                            null, null, null, null, IndicationConst.QUERY_MONGODB_TYPE_ALL);
                } catch (Exception e) {
                    LOGGER.error("获取指标[{}]-[{}]原始数据错误. WSDL -> {} method -> {}.",
                            endTime, indicationEntity.getIndicationName(), this.wsdl, this.wsdlMethod, e);
                }
                String calcDay = endTime.substring(0,8);
                long playCntSum = 0L, playSuccessCntSum = 0;
                if (dataMap != null && dataMap.size() > 0) {
                    JSONObject jsonValue = JSONObject.fromObject(dataMap.get(calcDay));
                    playCntSum = IndicationUtils.getLong(jsonValue.get("playCntSum"));
                    playSuccessCntSum = IndicationUtils.getLong(jsonValue.get("playSuccessCntSum"));
                }
                Double playRate = IndicationUtils.formatDivide(playSuccessCntSum * 100, playCntSum);
                String formatUnit = IndicationUtils.formatUnit(playRate, indicationEntity.getIndicationUnit());
                submitData(itemEntity, IndicationConst.COUNTRY_PROVINCE_CODE, endTime, formatUnit, playRate, false);
            }
        }
    }

    @Override
    public void calcSpecialItem(IndicationAllItemEntity itemEntity) {

    }
}
