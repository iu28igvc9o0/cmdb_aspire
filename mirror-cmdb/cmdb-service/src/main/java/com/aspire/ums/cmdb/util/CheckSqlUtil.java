package com.aspire.ums.cmdb.util;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: CheckSqlUtil
 * Author:   hangfang
 * Date:     2020/5/21
 * Description: DESCRIPTION
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
public class CheckSqlUtil {
    public static void checkSql(String sql) {
        // 做一次防注入过滤
        String temSql = sql.toLowerCase();
        String badKeyWords = "'|exec|insert|delete|update|*|%|chr|mid|master|truncate|char|declare|;|-|+";
        String[] badKeys = badKeyWords.split("\\|");
        String[] sqlWords = temSql.split(" ");
        for (String badKey : badKeys) {
            for (String sqlWord : sqlWords) {
                if (badKey.equals(sqlWord)) {
                    throw new RuntimeException("SQL检测失败, 禁止使用敏感字符. " + sql);
                }
            }
        }
    }
}
