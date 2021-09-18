package com.aspire.ums.cmdb.v2.index.util;

import java.math.BigDecimal;

/**
 * @ClassName MathCalculateUtil
 * @Description 数字计算工具类
 * @Author luowenbo
 * @Date 2020/4/17 9:54
 * @Version 1.0
 */
public class MathCalculateUtil {

    /*
    *  BigDecimal 做数据集合的总合，即可算整数也可算浮点数
    * */
    public static BigDecimal bigDecimalAdd(BigDecimal tv, String addValue){
        BigDecimal av = new BigDecimal(addValue);
        return tv.add(av);
    }

    /*
    *   BigDecimal 两两比较取大
    * */
    public static BigDecimal bigDecimalMax(String a,String b) {
        BigDecimal av = new BigDecimal(a);
        BigDecimal bv = new BigDecimal(b);
        if(av.compareTo(bv) == 1) {
            // av 大于 bv
            return av;
        } else {
            return bv;
        }
    }

    /*
     *   BigDecimal 两个数，求权重均值,最后取两位小数
     * */
    public static BigDecimal bigDecimalAvg(String a,String aTotal,String b,String bTotal) {
        BigDecimal av = new BigDecimal(a);
        BigDecimal bv = new BigDecimal(b);
        BigDecimal atv = new BigDecimal(aTotal);
        BigDecimal btv = new BigDecimal(bTotal);
        BigDecimal zero = new BigDecimal("0");
        /*
        *  (av*atv + bv*btv) / (atv+btv)
        * */
        BigDecimal four = atv.add(btv);
        if(four.compareTo(zero) == 0) {
            return zero;
        }
        BigDecimal one =  av.multiply(atv);
        BigDecimal two = bv.multiply(btv);
        BigDecimal three = one.add(two);
        BigDecimal rs = three.divide(four,2, BigDecimal.ROUND_HALF_UP);
        return rs;
    }
}
