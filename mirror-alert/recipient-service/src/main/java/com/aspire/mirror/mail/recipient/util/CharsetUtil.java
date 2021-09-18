package com.aspire.mirror.mail.recipient.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CharsetUtil {

    private static final Logger logger = LoggerFactory.getLogger(CharsetUtil.class);

    public static final String MAIL_CONTENT_PART_CHARSET = "ISO8859-1";

    private static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
            return true;
        }
        return false;
    }

    public static boolean isMessyCode(String strName) {
        Pattern p = Pattern.compile("\\s*|\t*|\r*|\n*");
        Matcher m = p.matcher(strName);
        String after = m.replaceAll("");
        String temp = after.replaceAll("\\p{P}", "");
//        temp = temp.replace("�", "");
        temp = temp.replace("*", "");
//        temp = temp.replace("?", "");
        char[] ch = temp.trim().toCharArray();
        float chLength = 0 ;
        float count = 0;
        for (int i = 0; i < ch.length; i++) {
            char c = ch[i];
            if (!Character.isLetterOrDigit(c)) {
                if (!isChinese(c)) {
                    count = count + 1;
                }
                chLength++;
            }
        }
        float result = count / chLength ;
        if (result > 0.4) {
            return true;
        } else {
            return false;
        }
    }

    public static String transMailCode(String text) {
        String message = text;
        try {
            if (isMessyCode(message)) {
                message = new String(text.getBytes(MAIL_CONTENT_PART_CHARSET), "UTF-8");
            }
            if (isMessyCode(message)) {
                message = new String(text.getBytes(MAIL_CONTENT_PART_CHARSET), "GBK");
            }
//            if (isMessyCode(message)) {
//                message = new String(text.getBytes("GBK"));
//            }
//            if (isMessyCode(message)) {
//                message = new String(text.getBytes("GBK"), "UTF-8");
//            }
//            if (isMessyCode(message)) {
//                message = text;
//            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return message;
    }
}
