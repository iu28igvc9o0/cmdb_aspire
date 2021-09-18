package com.aspire.mirror.indication.realnational.minute;

import com.aspire.mirror.util.IndicationConst;
import com.aspire.mirror.util.WSDLUtil;
import net.sf.json.JSONObject;

/**
 * Created with IntelliJ IDEA. User: jw.zhu Date: 2018/11/10 软探针异常指标监控系统 -
 * 无卡顿花屏收视用户占比指标 FirstLoadAvgHandler
 */
public class ScreenBlurredAlarmHandler extends AbstractMinuteFactory {

    @Override
    public void callWsdl() {
        String calcTime = this.endTime.substring(0,12);
        WSDLUtil wsdlUtil = new WSDLUtil();
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        super.SDK_RESULT_MAP = wsdlUtil.getWsdlServiceReturnMap(classLoader, wsdl, itemEntity.getMethod(),
                calcTime, calcTime, null, null, null, null, "datetime:alarmuser", "pause");
    }

    @Override
    public void executePrimaryItem() {
        String calcTime = this.endTime.substring(0,12);
        Long kartunnums = 0l;
        if (this.SDK_RESULT_MAP !=null && this.SDK_RESULT_MAP.containsKey(calcTime)) {
            JSONObject json = JSONObject.fromObject(this.SDK_RESULT_MAP.get(calcTime));
            //无卡顿花屏时长占比
            kartunnums = json.getLong("kartunnums");
        }
        super.addItemResult(this.endTime, IndicationConst.COUNTRY_PROVINCE_CODE, itemEntity.getMirrorColumn(), kartunnums);
    }

    @Override
    public void executeOtherItem() {

    }
}
