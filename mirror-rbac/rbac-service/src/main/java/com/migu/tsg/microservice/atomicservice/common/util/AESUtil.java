/*
 * All rights Reserved, Designed By Aspire
 * Copyright:    Copyright(C) 2016-2020
 * Company       Aspire Ltd.
 */
package com.migu.tsg.microservice.atomicservice.common.util;

import java.io.UnsupportedEncodingException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

/**
 * AES加密工具类
 *
 * @company
 * @author zengweixiong
 * @date 2019年10月25日
 */
public class AESUtil {
    //用于生成key
    private static final String ALLCHAR = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    //算法
    private static String TRANSFORMAT = "AES/ECB/PKCS5Padding";

    public static String encrypt(String sSrc, String key) throws Exception {
        if (sSrc != null && sSrc.length() != 0) {
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance(TRANSFORMAT);
            cipher.init(1, skeySpec);
            byte[] encrypted = cipher.doFinal(sSrc.getBytes("UTF-8"));
            return new String(Base64.encodeBase64(encrypted), "UTF-8");
        } else {
            return "";
        }
    }

    public static String decrypt(String sSrc, String key) throws Exception {
        if (sSrc != null && sSrc.length() != 0) {
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance(TRANSFORMAT);
            cipher.init(2, skeySpec);
            byte[] encrypted = Base64.decodeBase64(sSrc);
            byte[] original = cipher.doFinal(encrypted);
            return new String(original, "UTF-8");
        } else {
            return "";
        }
    }

    /**
     * 返回一个定长的随机字符串(只包含大小写字母、数字)
     *
     * @param length 随机字符串长度
     * @return 随机字符串
     */
    public static String generateString(int length) {
        StringBuffer sb = new StringBuffer();
        SecureRandom random = new SecureRandom();

        for (int i = 0; i < length; i++) {
            sb.append(ALLCHAR.charAt(random.nextInt(ALLCHAR.length())));
        }
        return sb.toString();
    }

    public static String encodeKeyToBase64(String key) {
        String keyBase64 = "";
        try {
            keyBase64 = new String(Base64.encodeBase64(key.getBytes("utf-8")), "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return keyBase64;
    }

    public static String decodeBase64ToKey(String keyBase64) {
        String key = "";
        try {
            key = new String(Base64.decodeBase64(keyBase64), "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return key;
    }
}