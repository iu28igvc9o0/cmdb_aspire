package com.aspire.ums.cmdb.v2.process.validate;

import com.alibaba.fastjson.JSONArray;
import com.aspire.ums.cmdb.util.StringUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: NumberValidator
 * Author:   zhu.juwang
 * Date:     2019/8/5 17:57
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
public class NumberValidator extends EmptyValidator{

    /**
     * 判断数字
     * @param value
     * @throws RuntimeException
     */
    public static void validator(String columnName, Object value) throws RuntimeException {
        validator(columnName, value, null);
    }
    /**
     * 判断数字
     * @param value
     * @throws RuntimeException
     */
    public static void validator(String columnName, Object value, String numberType) throws RuntimeException {
        if (numberType == null || numberType.equals("")) {
            numberType = "int";
        }
        if (numberType.toLowerCase(Locale.ENGLISH).equals("int")) {
            try {
                Integer.parseInt(String.valueOf(value));
            } catch (Exception e) {
                throw new RuntimeException("列[" + columnName + "]数量格式错误, 请输入整数");
            }
        } else {
            try {
                Double.parseDouble(String.valueOf(value));
            } catch (Exception e) {
                throw new RuntimeException("列[" + columnName + "]数量格式错误, 请输入数字");
            }
        }
    }

    /*
    *   数字验证,有取值范围
    * */
    public static void validateWithScope(String columnName,Object value,Integer begin,Integer end) {
        if(value == null || "".equals(value.toString())) {
            return;
        }
        // 排除浮点数
        if(value.toString().indexOf(".") != -1) {
            throw new RuntimeException("列[" + columnName + "]不可为浮点数，请输入正整数");
        }
        int v = 0;
        try {
            v = Integer.parseInt(String.valueOf(value));
        } catch (Exception e) {
            throw new RuntimeException("列[" + columnName + "]数量格式错误, 请输入正整数,且保证不要超过" + Integer.MAX_VALUE);
        }
        if(v < begin || v > end) {
            throw new RuntimeException("列[" + columnName + "]值不是" + begin +"~" + end + "范围内的整数");
        }
    }

    /*
    *  数字验证，数字为正数(正整数、正浮点数、0)
    * */
    public static void validatePositive(String columnName,Object value) {
        if(value == null || "".equals(value.toString())) {
            return;
        }
        try {
            Pattern pattern = Pattern.compile("^[\\+]?(0|[1-9]\\d*)(\\.\\d+)?$");
            boolean rst = pattern.matcher(value.toString()).matches();
            if(!rst) {
                throw new RuntimeException("列[" + columnName + "]数量格式错误, 请输入正数");
            }
        } catch (Exception e) {
            throw new RuntimeException("列[" + columnName + "]数量格式错误, 请输入正数");
        }
    }

    /*
     *  数字验证，数字为正数(正整数、正浮点数、0)
     *  浮点数只支持N位小数点,且取值区间在[begin,end]
     * */
    public static void validatePositiveLimitN(String columnName,Object value,Integer n,String begin,String end) {
        if(value == null || "".equals(value.toString())) {
            return;
        }
        String num = value.toString();
        Pattern pattern = Pattern.compile("^[\\+]?(0|[1-9]\\d*)(\\.\\d{1,"+ n +"})?$");
        boolean rst = pattern.matcher(value.toString()).matches();
        if(!rst) {
            if(num.indexOf('.') != -1 && num.substring(num.lastIndexOf('.')).length() >= n) {
               throw new RuntimeException("列[" + columnName + "]文本格式数值浮点数不可超过"+ n +"位");
            }
            throw new RuntimeException("列[" + columnName + "]数量格式错误, 请输入正数");
        }
        // 处理数据范围
        BigDecimal b = new BigDecimal(begin);
        BigDecimal e = new BigDecimal(end);
        BigDecimal nv = new BigDecimal(num);
        if(nv.compareTo(b) == -1 || nv.compareTo(e) == 1) {
            throw new RuntimeException("列[" + columnName + "]数据值不在设置[" + begin +"," + end + "]范围内");
        }
    }

    /*
     *  数字判定，数字只能为特定列表中的值
     * */
    public static void validateWithList(String columnName, Object value, List<Integer> numList) {
        boolean flag = false;
        try {
            int v = Integer.parseInt(String.valueOf(value));
            for(Integer item : numList) {
                if(item.intValue() == v) {
                    flag = true;
                    break;
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("列[" + columnName + "]数量格式错误，请输入整数");
        }
        if(!flag) {
            throw new RuntimeException("列["+columnName+"]值[" + value + "]不在可选范围. 可选值["+ JSONArray.toJSONString(numList) +"]");
        }
    }

    /*
     *  数字验证，数字为正整数
     * */
    public static void validatePositiveInteger(String columnName,Object value) {
        if(value == null || "".equals(value.toString())) {
            return;
        }
        try {
            Pattern pattern = Pattern.compile("^[+]{0,1}(\\d+)$");
            boolean rst = pattern.matcher(value.toString()).matches();
            if(!rst) {
                throw new RuntimeException("列[" + columnName + "]数量格式错误, 请输入正整数");
            }
        } catch (Exception e) {
            throw new RuntimeException("列[" + columnName + "]数量格式错误, 请输入正整数");
        }
    }

    /*
     *  数字验证，数字为正整数(大于0)
     * */
    public static void validatePositiveIntegerNotZero(String columnName,Object value) {
        if(value == null || "".equals(value.toString())) {
            return;
        }
        try {
            Pattern pattern = Pattern.compile("^[1-9]\\d*$");
            boolean rst = pattern.matcher(value.toString()).matches();
            if(!rst) {
                throw new RuntimeException("列[" + columnName + "]数量格式错误, 请输入大于0正整数");
            }
        } catch (Exception e) {
            throw new RuntimeException("列[" + columnName + "]数量格式错误, 请输入大于0正整数");
        }
    }


}
