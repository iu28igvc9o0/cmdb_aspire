package com.aspire.mirror.theme.server;

import com.aspire.mirror.common.util.DateUtil;
import io.krakens.grok.api.Grok;
import io.krakens.grok.api.GrokCompiler;
import io.krakens.grok.api.Match;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * TODO
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.theme.server.biz.handler
 * 类名称:    com.aspire.mirror.theme.server.GrokTest.java
 * 类描述:    TODO
 * 创建人:    JinSu
 * 创建时间:  2018/12/18 10:46
 * 版本:      v1.0
 */
public class GrokTest {
    public static void main(String[] args) throws ScriptException {
        GrokCompiler grokCompiler = GrokCompiler.newInstance();
        grokCompiler.registerDefaultPatterns();
//        final Grok grok = grokCompiler.compile("%{(?<test>[a-zA-Z0-9._-]+):bb}");
//        final Grok grok = grokCompiler.compile("%{CUSTOMWORD:date}\\|%{CUSTOMWORD:province}\\|%{CUSTOMWORD:city" +
//                "}\\|%{GREEDYDATA:physicalStationName}\\|%{GREEDYDATA:LogicalStationName}\\|%{CUSTOMWORD:stationType" +
//                "}\\|%{CUSTOMWORD:lac}\\|%{CUSTOMWORD:ci}\\|%{CUSTOMWORD:frequency}\\|%{CUSTOMWORD:isForDCS1800" +
//                "}\\|%{NUMBER:frequencyNum}\\|%{NUMBER:tchSum}\\|%{NUMBER:staticPdchNum}\\|%{NUMBER:erlSum" +
//                "}\\|%{GREEDYDATA:erlVoiceRate}\\|%{GREEDYDATA:erlDataRate}\\|%{NUMBER:voiceCallNum}\\|%{NUMBER" +
//                ":voiceUseNum}\\|%{NUMBER:voiceCallNumByChange}\\|%{NUMBER:voiceOverflowNum}\\|%{NUMBER:sdcchCallNum" +
//                "}\\|%{NUMBER:sdcchUseNum}\\|%{NUMBER:dropCallNum}\\|%{NUMBER:changeNum}\\|%{NUMBER:changeSuccessNum" +
//                "}\\|%{NUMBER:GSMVoieBy5}\\|%{NUMBER:GSMVoieBy7}\\|%{NUMBER:TBFUpSuccessSum}\\|%{NUMBER:TBFUpTrySum" +
//                "}\\|%{NUMBER:TBFDownSuccessSum}\\|%{NUMBER:TBFDownTrySum}", true);
////        final Grok grok = grokCompiler.compile("%{DATESTAMP:timestamp}");
//        final Grok grok = grokCompiler.compile("%{DATESTAMP:timestamp}");
//        String logMsg = "20190101|北京|北京|天安门|天安门ah_001|宏站|12131|456|GAM900|是|4|4|4|4|4|4|7|8|9|10|11|12|13|14|15|16|17|18|19|20|21";
//        Match grokMatch = grok.match(logMsg);
//        Map<String, Object> result = grokMatch.capture();
//        System.out.println(result);
//        String[] aa = "".split(",");
//        System.out.println(aa);
//        String year = null, month = null, monthDay = null, hour, minute, second;
//        List<String> yearList = (List) result.get("YEAR");
//        for (String item : yearList) {
//            if (item != null) {
//                year = item;
//                break;
//            }
//        }
//        List<String> monthList = (List) result.get("MONTHNUM");
//        for (String item : monthList) {
//            if (item != null) {
//                month = item;
//                break;
//            }
//        }
//        List<String> dayList = (List) result.get("MONTHDAY");
//        for (String item : dayList) {
//            if (item != null) {
//                monthDay = item;
//                break;
//            }
//        }
//        hour = (String) result.get("HOUR");
//        minute = (String) result.get("MINUTE");
//        second = (String) result.get("SECOND");
//        final Calendar c = Calendar.getInstance();
//        if (year != null && month != null && monthDay != null) {
//            c.set(Calendar.YEAR, Integer.valueOf(year));
//            c.set(Calendar.MONTH, Integer.valueOf(month) - 1);
//            c.set(Calendar.DAY_OF_MONTH, Integer.valueOf(monthDay));
//            c.set(Calendar.HOUR_OF_DAY, hour == null ? 0 : Integer.valueOf(hour));
//            c.set(Calendar.MINUTE, minute == null ? 0 : Integer.valueOf(minute));
//            if (second != null && second.contains(".")) {
//                String[] secondSplit = second.split("\\.");
//                c.set(Calendar.SECOND, Integer.valueOf(secondSplit[0]));
//                c.set(Calendar.MILLISECOND, Integer.valueOf(secondSplit[1]));
//            } else {
//                c.set(Calendar.SECOND, second == null ? 0 : Integer.valueOf(second));
//            }
//        }
//        Date date = c.getTime();
//        System.out.println(DateUtil.format(date, DateUtil.DATE_TIME_FORMAT));
        final Grok grok = grokCompiler.compile("%{CUSTOMWORD:date}\\|%{GREEDYDATA:aa}");
        String logMsg = "20190131|";
        Match grokMatch = grok.match(logMsg);
        Map<String, Object> resultMap = grokMatch.capture();
        System.out.println(resultMap);
//        List<Integer> year = (List) resultMap.get("YEAR");
//        System.out.println(year);
//        System.out.println(resultMap);
//        System.out.println(grokMatch.isNull());
//        String aa = null;
//        System.out.println(aa.equals("2"));
//
//        ScriptEngine jse = new ScriptEngineManager().getEngineByName("JavaScript");
//        System.out.println(Float.valueOf(jse.eval("1.2-1.02").toString()));

    }
}
