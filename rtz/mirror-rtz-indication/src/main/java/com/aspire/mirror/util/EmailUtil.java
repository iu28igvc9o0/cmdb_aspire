package com.aspire.mirror.util;

import lombok.extern.slf4j.Slf4j;

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
        if (!latestTime.equals("") && (Long.parseLong(calcTime) - Long.parseLong(latestTime) >= 1)) {
            if (retryCount > 4) {
                valid = false;
            }
            retryCount ++;
        }
        value = calcTime + "_" + retryCount;
        retryMap.put(key, value);
        return valid;
    }

    public static boolean isRestoreAlarm(String key, String calcTime) {
        String value = retryMap.get(key);
        boolean valid = false;
        int retryCount = 0;
        String latestTime = "";
        if (value != null) {
            String[] strings = value.split("_");
            latestTime = strings[0];
            retryCount = Integer.parseInt(strings[1]);
        }
        if (!latestTime.equals("") && (Long.parseLong(calcTime) - Long.parseLong(latestTime) == 1)) {
            if (retryCount > 0) {
                valid = true;
            }
        }
        retryMap.put(key, calcTime + "_0");
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
