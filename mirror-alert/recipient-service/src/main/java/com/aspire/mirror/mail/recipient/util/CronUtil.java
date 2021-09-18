package com.aspire.mirror.mail.recipient.util;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: CronUtil
 * Author:   HANGFANG
 * Date:     2019/4/25 11:24
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
public class CronUtil {

    public static String createCron (Integer cycle, int cycleUnit) {
        String cron = null;
        CycleUnit unit = CycleUnit.getUnit(cycleUnit);
        switch (unit) {
            case MINUTE:
                cron = "0 */" + cycle + " * * * ?";
                break;
            case HOUR:
                cron = "0 0 */" + cycle + " * * ?";
                break;
            case DAY:
                cron = "0 0 0 */" + cycle + " * ?";
                break;
        }
        return cron;
    }
}

enum CycleUnit {
    MINUTE(0),HOUR(1),DAY(2);

    private int unit;
    CycleUnit(int code) {
        this.unit = code;
    }

    public static CycleUnit getUnit(int code) {
        switch (code) {
            case 0:
                return MINUTE;
            case 1:
                return HOUR;
            case 2:
                return DAY;
        }
        return null;
    }
}