package com.aspire.real_mirror.indication.actual;

import com.aspire.common.WsdlUtils;
import com.aspire.mirror.entity.IndicationAllItemEntity;
import com.aspire.mirror.util.IndicationConst;
import com.aspire.mirror.util.IndicationUtils;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * EPG响应成功率
 */
@Slf4j
public class RtzEpgSuccessHandler extends AbstractRealActualFactory<RtzEpgSuccessHandler> {
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
            if (itemEntity.getIndicationItemName().equals("EPG响应成功率")) {
                List<String> dateList = new ArrayList<String>();
                dateList.add(endTime.substring(0,8));
                Map<String, String> egpMap = new HashMap<String, String>();
                try {
                    egpMap = WsdlUtils.getWsdlServiceReturnMap(this.wsdl, this.wsdlMethod, dateList,
                            null, null, null, null, IndicationConst.QUERY_MONGODB_TYPE_ALL);
                } catch (Exception e) {
                    LOGGER.error("获取指标[{}]-[{}]原始数据错误. WSDL -> {} method -> {}.",
                            endTime, indicationEntity.getIndicationName(), this.wsdl, this.wsdlMethod, e);
                }
                String calcDay = endTime.substring(0,8);
                long epgReqCntSum = 0L, epgReqReachCntSum = 0;
                if (egpMap != null && egpMap.size() > 0) {
                    JSONObject jsonValue = JSONObject.fromObject(egpMap.get(calcDay));
                    epgReqCntSum = IndicationUtils.getLong(jsonValue.get("epgReqCntSum"));
                    epgReqReachCntSum = IndicationUtils.getLong(jsonValue.get("epgReqReachCntSum"));
                }
                Double successRate = IndicationUtils.formatDivide(epgReqReachCntSum * 100, epgReqCntSum);
                String formatUnit = IndicationUtils.formatUnit(successRate, indicationEntity.getIndicationUnit());
                submitData(itemEntity, IndicationConst.COUNTRY_PROVINCE_CODE, endTime, formatUnit, successRate, false);
            }
        }
    }

    @Override
    public void calcSpecialItem(IndicationAllItemEntity itemEntity) {

    }
}
