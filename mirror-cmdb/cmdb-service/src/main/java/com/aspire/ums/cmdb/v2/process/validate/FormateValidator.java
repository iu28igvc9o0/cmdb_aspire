package com.aspire.ums.cmdb.v2.process.validate;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: FormateValidator
 * Author:   luowenbo
 * Date:     2019/9/23 14:45
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
public class FormateValidator {

    /*
    *  验证IP格式
    * */
    public static void isIpFormate(String columnName, Object value)  throws RuntimeException {
        if(value == null || "".equals(value)) {
            return;
        }
        String regex = "((25[0-5]|2[0-4]\\d|[01]?\\d\\d?)\\.){3}(25[0-5]|2[0-4]\\d|[01]?\\d\\d?)$|^([\\da-fA-F]{1,4}:){6}((25[0-5]|2[0-4]\\d|[01]?\\d\\d?)\\.){3}(25[0-5]|2[0-4]\\d|[01]?\\d\\d?)$|^::([\\da-fA-F]{1,4}:){0,4}((25[0-5]|2[0-4]\\d|[01]?\\d\\d?)\\.){3}(25[0-5]|2[0-4]\\d|[01]?\\d\\d?)$|^([\\da-fA-F]{1,4}:):([\\da-fA-F]{1,4}:){0,3}((25[0-5]|2[0-4]\\d|[01]?\\d\\d?)\\.){3}(25[0-5]|2[0-4]\\d|[01]?\\d\\d?)$|^([\\da-fA-F]{1,4}:){2}:([\\da-fA-F]{1,4}:){0,2}((25[0-5]|2[0-4]\\d|[01]?\\d\\d?)\\.){3}(25[0-5]|2[0-4]\\d|[01]?\\d\\d?)$|^([\\da-fA-F]{1,4}:){3}:([\\da-fA-F]{1,4}:){0,1}((25[0-5]|2[0-4]\\d|[01]?\\d\\d?)\\.){3}(25[0-5]|2[0-4]\\d|[01]?\\d\\d?)$|^([\\da-fA-F]{1,4}:){4}:((25[0-5]|2[0-4]\\d|[01]?\\d\\d?)\\.){3}(25[0-5]|2[0-4]\\d|[01]?\\d\\d?)$|^([\\da-fA-F]{1,4}:){7}[\\da-fA-F]{1,4}$|^:((:[\\da-fA-F]{1,4}){1,6}|:)$|^[\\da-fA-F]{1,4}:((:[\\da-fA-F]{1,4}){1,5}|:)$|^([\\da-fA-F]{1,4}:){2}((:[\\da-fA-F]{1,4}){1,4}|:)$|^([\\da-fA-F]{1,4}:){3}((:[\\da-fA-F]{1,4}){1,3}|:)$|^([\\da-fA-F]{1,4}:){4}((:[\\da-fA-F]{1,4}){1,2}|:)$|^([\\da-fA-F]{1,4}:){5}:([\\da-fA-F]{1,4})?$|^([\\da-fA-F]{1,4}:){6}:";
        if(!value.toString().matches(regex)) {
            throw new RuntimeException("列[" + columnName + "]的IP格式不正确");
        }
    }

    /*
     *  验证MAC格式
     * */
    public static void isMacFormate(String columnName, Object value)  throws RuntimeException {
        if(value == null || "".equals(value)) {
            return;
        }
        String regex = "([A-Fa-f0-9]{2}-){5,7}[A-Fa-f0-9]{2}$|^([A-Fa-f0-9]{2}){5,7}[A-Fa-f0-9]{2}$|^([A-Fa-f0-9]{2}:){5,7}[A-Fa-f0-9]{2}$|^([A-Fa-f0-9]{2} ){5,7}[A-Fa-f0-9]{2}";
        if(!value.toString().matches(regex)) {
            throw new RuntimeException("列[" + columnName + "]的MAC格式不正确");
        }
    }

    /*
     *  验证手机格式
     * */
    public static void isPhoneFormate(String columnName, Object value)  throws RuntimeException {
        if(value == null || "".equals(value)) {
            return;
        }
        String regex = "1(3|4|5|6|7|8|9)\\d{9}";
        if(!value.toString().matches(regex)) {
            throw new RuntimeException("列[" + columnName + "]的手机格式不正确");
        }
    }

    /*
     *  验证邮箱格式
     * */
    public static void isEmailFormate(String columnName, Object value)  throws RuntimeException {
        if(value == null || "".equals(value)) {
            return;
        }
        String regex = "([a-zA-Z]|[0-9])(\\w|\\-)+@[a-zA-Z0-9]+\\.([a-zA-Z]{2,4})";
        if(!value.toString().matches(regex)) {
            throw new RuntimeException("列[" + columnName + "]的邮箱格式不正确");
        }
    }
}
