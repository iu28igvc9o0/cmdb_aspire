package com.aspire.ums.cmdb.util;

import org.apache.commons.codec.binary.Base64;
import org.springframework.util.StringUtils;

import java.io.UnsupportedEncodingException;

/**
 * Base64类 <br>
 * 功能：字符串的BASE64编码解码。
 */
public class Base64Util {

    /**
     * 将字符串转化为base64编码
     *
     * @param str
     * @return
     */
    public static String getBase64(String str) {
        String result = "";
        if (StringUtils.isEmpty(str)) {
            str = "";
        }
        try {
            byte[] bytes = Base64.encodeBase64(str.getBytes("utf-8"));
            result = new String(bytes, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 将 BASE64 编码的字符串 s 进行解码
     *
     * @param str
     * @return
     */
    public static String getFromBase64(String str) {
        String result = "";
        if (!StringUtils.isEmpty(str)) {
            try {
                byte[] convertBytes = Base64.decodeBase64(str.getBytes("utf-8"));
                result = new String(convertBytes, "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 将Base64编码的字符串解码
     *
     * @param str
     * @return
     */
    public static byte[] decodeBase64(String str) {
        byte[] resultBytes = null;
        if (!StringUtils.isEmpty(str)) {
            try {
                byte[] bytes = str.getBytes("utf-8");
                resultBytes = Base64.decodeBase64(bytes);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return resultBytes;
    }
}
