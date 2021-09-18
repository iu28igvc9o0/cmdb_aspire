package com.aspire.ums.cmdb.util;

import com.aspire.ums.cmdb.collect.CollectConst;

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

    public static String createCron (Integer cycle, String cycleUnit) {
        String cron = null;
        switch (cycleUnit) {
            case CollectConst.TIME_MINUTE:
                cron = "0 */" + cycle + " * * * ?";
                break;
            case CollectConst.TIME_HOUR:
                cron = "0 0 */" + cycle + " * * ?";
                break;
            case CollectConst.TIME_DAY:
                cron = "0 0 0 */" + cycle + " * ?";
                break;
        }
        return cron;
    }
}
