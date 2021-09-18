package com.aspire.real_mirror.indication.minute;

import com.aspire.common.WsdlUtils;
import com.aspire.mirror.entity.IndicationAllItemEntity;
import com.aspire.mirror.util.IndicationConst;
import com.aspire.mirror.util.TimeUtil;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.apache.commons.lang.time.DateUtils;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA. User: jw.zhu Date: 2018/11/10 软探针异常指标监控系统 -
 * 无卡顿花屏收视用户占比指标 FirstLoadAvgHandler
 */
@Slf4j
public class ScreenBlurredAlarmHandler extends AbstractMinuteFactory {
    @Override
    public String getEndpointAddress() {
        return WsdlUtils.USER_INFO_SERVER;
    }

    @Override
    public String initWsdlMethod() {
        return "getAlarmInfoSplitNew";
    }

    @Override
    public void initSDK() {
        for(IndicationAllItemEntity itemEntity : indicationEntity.getIndicationItemList()) {
            if (itemEntity.getIndicationItemName().equals("告警数")) {
                String calcTime = this.calcDate.substring(0,12);
                Map<String, String> dataMap = new HashMap<String, String>();
                try {
                    dataMap = WsdlUtils.getWsdlServiceReturnMap(this.wsdl, this.wsdlMethod,
                            calcTime, calcTime, null, null, null, null, "datetime:alarmuser", "pause");
                } catch (Exception e) {
                    LOGGER.error("获取指标[{}]-[{}]原始数据错误. WSDL -> {} method -> {}.",
                            calcTime, indicationEntity.getIndicationName(), this.wsdl, this.wsdlMethod, e);
                }
                Long kartunnums = 0l;
                if (dataMap !=null && dataMap.containsKey(calcTime)) {
                    JSONObject json = JSONObject.fromObject(dataMap.get(calcTime));
                    //无卡顿花屏时长占比
                    kartunnums = json.getLong("kartunnums");
                }
                submitData(itemEntity, IndicationConst.COUNTRY_PROVINCE_CODE, this.calcDate, kartunnums, kartunnums, false);
            }
            if (itemEntity.getIndicationItemName().equals("时间段")) {
                Date date = null;
                try {
                    date =  DateUtils.parseDate(this.calcDate, new String[] {IndicationConst.QUERY_MONGODB_DATE_PATTERN_2});
                } catch (ParseException e) {
                    log.error("时间格式错误{}", this.calcDate, e);
                }
                String curPeriod = TimeUtil.formatDateToPeriod(date);
                submitData(itemEntity, IndicationConst.COUNTRY_PROVINCE_CODE, this.calcDate, curPeriod, curPeriod, false);
            }
            if (itemEntity.getIndicationItemName().equals("异常时间点")) {
                String hour = this.calcDate.substring(8,10);
                String minute = this.calcDate.substring(10,12);
                String periodTime = hour+":"+minute;
                submitData(itemEntity, IndicationConst.COUNTRY_PROVINCE_CODE, this.calcDate, periodTime, periodTime, false);
            }
        }
    }

    @Override
    public void calcSpecialItem(IndicationAllItemEntity itemEntity) {

    }
}
