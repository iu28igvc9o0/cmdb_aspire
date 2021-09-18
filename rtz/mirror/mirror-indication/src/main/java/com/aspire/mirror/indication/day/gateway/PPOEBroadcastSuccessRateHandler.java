package com.aspire.mirror.indication.day.gateway;

import com.aspire.common.WsdlUtils;
import com.aspire.mirror.entity.IndicationAllItemEntity;
import com.aspire.mirror.entity.IndicationProvinceEntity;
import com.aspire.mirror.indication.day.AbstractDayIndicationFactory;
import com.aspire.mirror.util.IndicationConst;
import com.aspire.mirror.util.IndicationUtils;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: jw.zhu
 * Date: 2018/11/10
 * 软探针异常指标监控系统 - 宽带拨号成功率（%）指标
 * PPOEBroadcastSuccessRateHandler
 */
@Slf4j
public class PPOEBroadcastSuccessRateHandler extends AbstractDayIndicationFactory {

    private Map<String, String> getSource(List<String> dataList, String areacode, String stbcode, String cpcode, String province,
                                          String type) {
        String dateStr = dataList.get(0);
        Map<String, String> ppoeMap = new HashMap<String, String>();
        List<String> pppoereasons = new ArrayList<String>();
        try {
            List<Object> reasonList = WsdlUtils.getWsdlServiceReturnList(this.wsdl, "getPppoeReason");
            if (reasonList != null && reasonList.size() > 0) {
                for (Object reason : reasonList) {
                    if (reason != null && reason instanceof String) {
                        pppoereasons.add(reason.toString());
                    }
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        try {
            ppoeMap = WsdlUtils.getWsdlServiceReturnMap(this.wsdl, this.wsdlMethod, dataList, areacode,
                    stbcode, cpcode, province, type, pppoereasons);
        } catch (Exception e) {
            LOGGER.error("获取指标[{}]-[{}]原始数据错误. WSDL -> {} method -> {}.",
                    dateStr, indicationEntity.getIndicationName(), this.wsdl, this.wsdlMethod, e);
        }
        return ppoeMap;
    }

    @Override
    public String initWsdlMethod() {
        return "getPppoeErrorCnt";
    }

    @Override
    public void initSDK() {
        for(IndicationAllItemEntity itemEntity : indicationEntity.getIndicationItemList()) {
            if (itemEntity.getIndicationItemName().equals("宽带拨号成功率")) {
                List<String> prevDays = IndicationUtils.getPrevDays(calcDate, IndicationConst.QUERY_MONGODB_DATE_PATTERN, 0);
                String dateStr = prevDays.get(0);
                Map<String, String> ppoeMap = getSource(prevDays, null, null, null, null,
                        IndicationConst.QUERY_MONGODB_TYPE_PROVINCE);
                List<IndicationProvinceEntity> provinceList = provinceService.getAllProvince();
                if (!ppoeMap.isEmpty()) {
                    JSONObject ppoeJson = JSONObject.fromObject(ppoeMap.get(dateStr));
                    double totalErrorNone = 0;
                    double totalAll = 0;
                    for (IndicationProvinceEntity entity : provinceList) {
                        if (!ppoeJson.containsKey(entity.getProvinceCode())) {
                            submitData(itemEntity, entity.getProvinceCode(), dateStr, 0, 0, false);
                            continue;
                        }
                        JSONObject provinceJSON = ppoeJson.getJSONObject(entity.getProvinceCode());
                        if (provinceJSON.size() > 0) {
                            Double errorNone = 0.0;
                            Double errorNoneAll = mapValueCnt(provinceJSON);
                            if (provinceJSON.containsKey("ERROR_NONE")) {
                                errorNone = provinceJSON.getDouble("ERROR_NONE");
                            }
                            totalErrorNone = IndicationUtils.formatSum(totalErrorNone, errorNone);
                            totalAll = IndicationUtils.formatSum(totalAll, errorNoneAll);
                            String value = IndicationUtils.formatRate(errorNone, errorNoneAll, 100, null);
                            String formatValue = IndicationUtils.formatScale(value, 4);
                            submitData(itemEntity, entity.getProvinceCode(), dateStr, formatValue, value, false);
                        }
                    }
                    String value = IndicationUtils.formatRate(totalErrorNone, totalAll, 100, null);
                    String formatValue = IndicationUtils.formatScale(value, 4);
                    submitData(itemEntity, IndicationConst.COUNTRY_PROVINCE_CODE, dateStr, formatValue, value, false);
                } else {
                    for (IndicationProvinceEntity entity : provinceList) {
                        String value = "0";
                        submitData(itemEntity, entity.getProvinceCode(), dateStr, value, value, false);
                    }
                }
            }
        }
    }

    @Override
    public void calcSpecialItem(IndicationAllItemEntity itemEntity) {
        if (itemEntity.getIndicationItemName().equals("宽带拨号总次数")) {
            List<String> prevDays = IndicationUtils.getPrevDays(calcDate, IndicationConst.QUERY_MONGODB_DATE_PATTERN, 0);
            String dateStr = prevDays.get(0);
            List<IndicationProvinceEntity> provinceList = provinceService.getAllProvince();
            Map<String, String> ppoeMap = getSource(prevDays, null, null, null, null,
                    IndicationConst.QUERY_MONGODB_TYPE_PROVINCE);
            if (!ppoeMap.isEmpty()) {
                JSONObject ppoeJson = JSONObject.fromObject(ppoeMap.get(dateStr));
                double totalAll = 0;
                for (IndicationProvinceEntity entity : provinceList) {
                    if (!ppoeJson.containsKey(entity.getProvinceCode())) {
                        submitData(itemEntity, entity.getProvinceCode(), dateStr, 0, 0, false);
                        continue;
                    }
                    JSONObject provinceJSON = ppoeJson.getJSONObject(entity.getProvinceCode());
                    if (provinceJSON.size() > 0) {
                        double errorNoneAll = mapValueCnt(provinceJSON);
                        totalAll = IndicationUtils.formatSum(totalAll, errorNoneAll);
                        submitData(itemEntity, entity.getProvinceCode(), dateStr, errorNoneAll, errorNoneAll, false);
                    }
                }
                submitData(itemEntity, IndicationConst.COUNTRY_PROVINCE_CODE, dateStr, totalAll, totalAll, false);
            } else {
                for (IndicationProvinceEntity entity : provinceList) {
                    String value = "0";
                    submitData(itemEntity, entity.getProvinceCode(), dateStr, value, value, false);
                }
            }
        }
    }

    private Double mapValueCnt(JSONObject jsonObject){
        Double result = 0.0;
        if (jsonObject.size() > 0) {
            Iterator keys = jsonObject.keys();
            while (keys.hasNext()) {
                String key = keys.next().toString();
                if (StringUtils.isBlank(key))
                    continue;
                if (StringUtils.isNotBlank(key) && key.length() > 4) {
                    String keyStr = key.substring(key.length() - 4, key.length());
                    if (keyStr.toLowerCase().contains("ipv6")) {
                        continue;
                    }
                }
                result = IndicationUtils.formatSum(result, jsonObject.getDouble(key));
            }
        }
        return result;
    }

    @Override
    public String getEndpointAddress() {
        return WsdlUtils.OSGI_ALARM_INFO_SERVER;
    }
}
