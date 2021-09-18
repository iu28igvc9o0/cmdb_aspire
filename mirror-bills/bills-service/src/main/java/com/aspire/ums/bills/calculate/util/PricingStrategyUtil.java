package com.aspire.ums.bills.calculate.util;

import java.math.BigDecimal;
import java.util.Calendar;

/**
 * @projectName: PricingStrategyUtil
 * @description: 计费策略工具类
 * @author: luowenbo
 * @create: 2020-07-31 13:48
 **/
public class PricingStrategyUtil {

    private static final int[] MONTH_STANDARD = {0,31,28,31,30,31,30,31,31,30,31,30,31};

    // 依据电脑本地时间，判断当前月的天数
    public static int getDayWithCrtMonth() {
        int rsDay;
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        rsDay = MONTH_STANDARD[month];
        // 二月 且 闰年
        if(month == 2 && ((year % 400 == 0 ) || (year % 100 != 0 && year % 4 == 0))) {
            rsDay += 1;
        }
        return rsDay;
    }

    // 计算每日单价
    public static String getDailyPrice(String price){
        BigDecimal priceBg = new BigDecimal(price);
        BigDecimal monthDay = new BigDecimal(getDayWithCrtMonth());
        BigDecimal dayPrice = priceBg.divide(monthDay,2,BigDecimal.ROUND_UP);
        return dayPrice.toPlainString();
    }


    // 计算策略 每日单价*配额*折扣
    public static String calDailyExpense(String quota, String price, String discount) {
        BigDecimal quotaBg = new BigDecimal(quota);
        BigDecimal discountBg = new BigDecimal(discount);
        // 每日单价
        BigDecimal dayPrice = new BigDecimal(getDailyPrice(price));
        // 最终计算
        BigDecimal expense = dayPrice.multiply(quotaBg).multiply(discountBg);
        return expense.toPlainString();
    }

    // 计算策略 每日单价*配额
    public static String calDailyExpenseWithoutDiscount(String quota, String price) {
        BigDecimal quotaBg = new BigDecimal(quota);
        // 每日单价
        BigDecimal dayPrice = new BigDecimal(getDailyPrice(price));
        // 最终计算
        BigDecimal expense = dayPrice.multiply(quotaBg);
        return expense.toPlainString();
    }
}
