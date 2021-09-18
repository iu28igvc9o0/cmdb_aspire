package com.aspire.ums.bills.util;

import java.math.BigDecimal;

/**
 * @projectName: BigDecimalCalculateUtils
 * @description: BigDecimal 计算工具类
 * @author: luowenbo
 * @create: 2020-08-05 14:42
 **/
public class BigDecimalCalculateUtils {

    /**
     *  BigDecimal 新增
     * @param adda
     * @param addb
     * @return String
     */
    public static String stringAddWithBigDecimal(String adda,String addb) {
        BigDecimal a = new BigDecimal(adda);
        BigDecimal b = new BigDecimal(addb);
        return a.add(b).toPlainString();
    }

    /**
     *  BigDecimal 新增
     * @param adda
     * @param addb
     * @return Double
     */
    public static Double doubleAddWithBigDecimal(String adda,String addb) {
        BigDecimal a = new BigDecimal(adda);
        BigDecimal b = new BigDecimal(addb);
        return a.add(b).doubleValue();
    }

    /**
     *  BigDecimal 减法
     * @param subA
     * @param subB
     * @return Double
     */
    public static Double doubleSubWithBigDecimal(String subA,String subB) {
        BigDecimal a = new BigDecimal(subA);
        BigDecimal b = new BigDecimal(subB);
        return a.subtract(b).doubleValue();
    }
}
