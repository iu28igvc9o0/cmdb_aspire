package com.aspire.mirror.util;

import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.apache.commons.lang.time.DateUtils;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class EmailUtil {

    private static Map<String, String> retryMap = new HashMap<String, String>();

    public static boolean isContinueAlarm(String key, String calcTime) {
        String value = retryMap.get(key);
        boolean valid = true;
        int retryCount = 1;
        String latestTime = "";
        if (value != null) {
            String[] strings = value.split("_");
            latestTime = strings[0];
            retryCount = Integer.parseInt(strings[1]);
        }
        if (!latestTime.equals("")) {
            try {
                Date calcDate = DateUtils.parseDate(calcTime, new String[]{IndicationConst.QUERY_MONGODB_DATE_PATTERN_2});
                Date latestDate = DateUtils.parseDate(latestTime, new String[]{IndicationConst.QUERY_MONGODB_DATE_PATTERN_2});
                if (calcDate.getTime() - latestDate.getTime() == 60 * 1000) {
                    // 连续发送5次邮件, 取消提醒
                    if (retryCount > 4) {
                        valid = false;
                    }
                    retryCount ++;
                }
            } catch (ParseException e) {
                log.error("Parse date error.", e);
            }
        }
        retryMap.put(key, calcTime + "_" + retryCount);
        log.info("分钟告警数据集合 -> {}", JSONObject.fromObject(retryMap).toString());
        return valid;
    }

    public static boolean isRestoreAlarm(String key, String calcTime) {
        String value = retryMap.get(key);
        boolean valid = false;
        int retryCount = 0;
        if (value != null) {
            String[] strings = value.split("_");
            retryCount = Integer.parseInt(strings[1]);
        }
        if (retryCount > 0) {
            valid = true;
            retryCount = 0;
            retryMap.put(key, calcTime + "_" + retryCount);
        }
        return valid;
    }

    public static void main(String[] a) {
        boolean c = isContinueAlarm("1","2019011522");
        System.out.println(retryMap.get("1") + "-" + c);
        c = isContinueAlarm("1","2019011523");
        System.out.println(retryMap.get("1") + "-" + c);
        c = isContinueAlarm("1","2019011524");
        System.out.println(retryMap.get("1") + "-" + c);
        c = isContinueAlarm("1","2019011525");
        System.out.println(retryMap.get("1") + "-" + c);
        c = isContinueAlarm("1","2019011526");
        System.out.println(retryMap.get("1") + "-" + c);
        c = isContinueAlarm("1","2019011527");
        System.out.println(retryMap.get("1") + "-" + c);
        c = isRestoreAlarm("1","2019011528");
        System.out.println(retryMap.get("1") + "-" + c);
    }

}
