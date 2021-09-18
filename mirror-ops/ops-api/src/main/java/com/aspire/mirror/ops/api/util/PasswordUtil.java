package com.aspire.mirror.ops.api.util;

import java.security.SecureRandom;
import java.util.Random;

/**
 * 密码生成工具类
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.ops.api.util
 * 类名称:    PasswordUtil.java
 * 类描述:    密码生成工具类
 * 创建人:    JinSu
 * 创建时间:  2020/6/9 9:33
 * 版本:      v1.0
 */
public class PasswordUtil {
    public static String makeRandomPassword(int len){
        char charr[] = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890~@#$%^&*.?".toCharArray();
        StringBuilder sb = new StringBuilder();
        Random r = new SecureRandom();

        for (int x = 0; x < len; ++x) {
            sb.append(charr[r.nextInt(charr.length)]);
        }
        return sb.toString();
    }

    //获取验证过的随机密码
    public static String getRandomPassword(int len) {
        String result = null;
        result = makeRandomPassword(len);
        if (result.matches(".*[a-z]{1,}.*") && result.matches(".*[A-Z]{1,}.*") && result.matches(".*[0-9]{1,}.*") && result.matches(".*[~!@#$%^&*\\.?]{1,}.*")) {
            return result;
        }
        return getRandomPassword(len);
    }

    public static void main(String[] args) {
        System.out.println(getRandomPassword(20));
    }
}
