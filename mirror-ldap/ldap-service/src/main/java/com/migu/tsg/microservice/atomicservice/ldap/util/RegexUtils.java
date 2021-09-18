package com.migu.tsg.microservice.atomicservice.ldap.util;

import java.util.Arrays;

import org.apache.commons.lang.StringUtils;

/**
 * 项目名称: ldap-service <br>
 * 包: com.migu.tsg.microservice.atomicservice.ldap.util <br>
 * 类名称: RegexUtil.java <br>
 * 类描述: 正则表达式工具类 <br>
 * 创建人: WangSheng <br>
 * 创建时间: 2017年8月1日上午9:48:22 <br>
 * 版本: v1.0
 */
public class RegexUtils {

    /**
     * 验证数组邮箱是否为合法的邮箱字符串
     * @param emails 邮箱集合
     * @return true则合法,false则不合法
     */
    public static boolean hasMatchesRegexEmails(final String... emails) {
        if (emails == null || emails.length <= 0) {
            return false;
        }
        String pattern = "^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+([-|_]?[a-z0-9A-Z]+)*\\.)+[a-zA-Z]{2,}$";
        return Arrays.stream(emails).allMatch(email -> StringUtils.trimToEmpty(email).matches(pattern));
    }

    /**
     * 验证数组手机是否为合法的手机字符串
     * @param emails 手机集合
     * @return true则合法,false则不合法
     */
    public static boolean hasMatchesRegexMobiles(final String... mobiles) {
        if (mobiles == null || mobiles.length <= 0) {
            return false;
        }
        String pattern = "^((17[0-9])|(14[0-9])|(13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$";
        return Arrays.stream(mobiles).allMatch(mobile -> StringUtils.trimToEmpty(mobile).matches(pattern));
    }

    /**
     * 验证数组用户名称是否为合法的名称字符串
     * @param usernames 用户名称集合
     * @return true则合法,false则不合法
     */
    public static boolean hasMatchesRegexUsernames(final String... usernames) {
        if (usernames == null || usernames.length <= 0) {
            return false;
        }
        String pattern = "^[a-zA-Z]\\w{2,20}$";
        return Arrays.stream(usernames)
                .allMatch(username -> StringUtils.trimToEmpty(username).matches(pattern));
    }

    /**
     * 验证数组用户密码是否为合法的密码字符串
     * @param passwords 用户密码集合
     * @return true则合法,false则不合法
     */
    public static boolean hasMatchesRegexPasswords(final String... passwords) {
        if (passwords == null || passwords.length <= 0) {
            return false;
        }
        String pattern = "^[\\s\\S]{6,100}$";
        return Arrays.stream(passwords)
                .allMatch(password -> StringUtils.trimToEmpty(password).matches(pattern));
    }
}
