package com.aspire.ums.cmdb.util;

import org.apache.commons.collections.MapUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Copyright (C), 2015-2020, 卓望数码有限公司
 * FileName: ResultUtils
 * Author:   zhu.juwang
 * Date:     2020/3/10 11:22
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
public class ResultUtils {

    // 失败标识
    public static final String ERROR = "error";
    // 成功标识
    public static final String SUCCESS = "success";
    // 返回信息
    public static final String MSG = "msg";

    /**
     * 返回map
     * @param flag 成功标识
     * @param reason 原因
     * @return
     */
    public static Map<String, String> parseMap(String flag, String reason) {
        return parseMap(new HashMap<>(), flag, reason);
    }

    /**
     * 返回map
     * @param flag 成功标识
     * @param reason 原因
     * @return
     */
    public static Map<String, String> parseMap(Map<String, String> retMap, String flag, String reason) {
        if (flag.equals(ERROR)) {
            retMap.put(ERROR, ERROR);
            retMap.put(MSG, reason);
        } else {
            retMap.put(SUCCESS, SUCCESS);
        }
        return retMap;
    }
}
