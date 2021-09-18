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
 * Date: 2018/11/10
 * 软探针异常指标监控系统 - EPG响应成功率指标
 * EpgLazyResponseHandler
 */
public class EpgSuccessResponseHandler extends AbstractDayIndicationFactory {
    @Override
    public String initWsdlMethod() {
        return "getEpgRespInfo";
    }

    @Override
    public void initSDK() {
        for(IndicationAllItemEntity itemEntity : indicationEntity.getIndicationItemList()) {
            if (itemEntity.getIndicationItemName().equals("EPG响应成功率")) {
                List<String> prevDays = IndicationUtils.getPrevDays(calcDate, IndicationConst.QUERY_MONGODB_DATE_PATTERN, 0);
                String dateStr = prevDays.get(0);
                Map<String, String> epgMap = new HashMap<String, String>();
                try {
                    epgMap =WsdlUtils.getWsdlServiceReturnMap(this.wsdl, this.wsdlMethod,prevDays, null,
                            null, null, null, IndicationConst.QUERY_MONGODB_TYPE_PROVINCE);
                } catch (Exception e) {
                    LOGGER.error("获取指标[{}]-[{}]原始数据错误. WSDL -> {} method -> {}.",
                            dateStr, indicationEntity.getIndicationName(), this.wsdl, this.wsdlMethod, e);
                }
                Double totalEpgReqReachCntSum = 0.0;
                Double totalEpgReqCntSum = 0.0;
                for (IndicationProvinceEntity entity : PROVINCE_LIST) {
                    if (entity.getProvinceCode().equals(IndicationConst.COUNTRY_PROVINCE_CODE)) {
                        continue;
                    }
                    double epgReqReachCntSum = 0.0;
                    double epgReqCntSum = 0.0;
                    if (epgMap.containsKey(entity.getProvinceCode())) {
                        JSONObject epgJson = JSONObject.fromObject(epgMap.get(entity.getProvinceCode()));
                        epgReqReachCntSum = epgJson.getDouble("epgReqReachCntSum");
                        epgReqCntSum = epgJson.getDouble("epgReqCntSum");
                        totalEpgReqReachCntSum = IndicationUtils.formatSum(totalEpgReqReachCntSum, epgReqReachCntSum);
                        totalEpgReqCntSum = IndicationUtils.formatSum(totalEpgReqCntSum, epgReqCntSum);
                    }
                    String epgSuccess = IndicationUtils.formatRate(epgReqReachCntSum, epgReqCntSum, 100, null);
                    String formatUnit = IndicationUtils.formatUnit(epgSuccess, indicationEntity.getIndicationUnit());
                    submitData(itemEntity, entity.getProvinceCode(), dateStr, formatUnit, epgSuccess, false);
                }
                String totalEpgSuccess = IndicationUtils.formatRate(totalEpgReqReachCntSum, totalEpgReqCntSum, 100, null);
                String formatUnit = IndicationUtils.formatUnit(totalEpgSuccess, indicationEntity.getIndicationUnit());
                //全国的数据
                submitData(itemEntity, IndicationConst.COUNTRY_PROVINCE_CODE, prevDays.get(0), formatUnit, totalEpgSuccess, false);
            }
        }
    }

    @Override
    public void calcSpecialItem(IndicationAllItemEntity itemEntity) {

    }

    @Override
    public String getEndpointAddress() {
        return WsdlUtils.PROGRAM_INFO_SERVER;
    }
}
