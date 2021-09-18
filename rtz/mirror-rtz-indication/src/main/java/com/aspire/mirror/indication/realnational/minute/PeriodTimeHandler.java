package com.aspire.mirror.indication.realnational.minute;

import com.aspire.mirror.util.IndicationConst;
import com.aspire.mirror.util.TimeUtil;
import com.aspire.mirror.util.WSDLUtil;
import net.sf.json.JSONObject;
import org.apache.commons.lang.time.DateUtils;

import java.text.ParseException;
import java.util.Date;

/**
 * Created with IntelliJ IDEA. User: jw.zhu Date: 2018/11/10 软探针异常指标监控系统 -
 * 无卡顿花屏收视用户占比指标 FirstLoadAvgHandler
 */
public class PeriodTimeHandler extends AbstractMinuteFactory {

    @Override
    public void callWsdl() {

    }

    @Override
    public void executePrimaryItem() {
        String hour = this.endTime.substring(8,10);
	    String minute = this.endTime.substring(10,12);
	    String periodTime = hour+":"+minute;
        for (String provinceCode : super.PROVINCE_LIST) {
            super.addItemResult(super.endTime, provinceCode, itemEntity.getMirrorColumn(), periodTime);
        }
        super.addItemResult(this.endTime, IndicationConst.COUNTRY_PROVINCE_CODE, itemEntity.getMirrorColumn(), periodTime);
    }

    @Override
    public void executeOtherItem() {

    }
}
