package com.aspire.mirror.util;

import com.aspire.mirror.entity.IndicationProvinceEntity;
import com.aspire.common.FactoryUtils;
import com.aspire.mirror.service.IIndicationProvinceService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: jw.zhu
 * Date: 2018/11/6
 * 软探针异常指标监控系统
 * com.aspire.mirror.util.IndicationUtils
 */
@Slf4j
public class IndicationUtils {

    /**
     * 获取指定日期 向前的position个自然日 如 20181224 20181223 20181222...
     * @param dateStr 指定日期
     * @param pattern 指定格式
     * @param position 指定的n个自然日
     * @return
     */
    public static List<String> getPrevDays(String dateStr, String pattern, int position) {
        Date date = null;
        try {
            if (dateStr.length() == 10 && !dateStr.contains("-")) {
                date = DateUtils.parseDate(dateStr, new String[]{IndicationConst.QUERY_MONGODB_DATE_PATTERN_0});
            } else {
                date = DateUtils.parseDate(dateStr, new String[]{IndicationConst.DATE_PATTERN, IndicationConst.QUERY_MONGODB_DATE_PATTERN});
            }
        } catch (ParseException e) {
            log.error("Parse date {} error.", dateStr, e);
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        List<String> days = new LinkedList<String>();
        if (position == 0) {
            position = 1;
        }
        for (int n = Math.abs(position); n > 0; n--) {
            if (dateStr.length() == 10 && !dateStr.contains("-")) {
                pattern = IndicationConst.QUERY_MONGODB_DATE_PATTERN_0;
            }
            String value = DateFormatUtils.format(calendar.getTime(), pattern);
            days.add(0,value);
            calendar.add(Calendar.DATE, -1);
        }
        return days;
    }

    /**
     * 获取指定前n天前的相同小时时间 如20181224010000 20181223010000 20181222010000...
     * @param dateStr 日期格式, 传入yyyyMMddHHmmss
     * @param position 前n天
     * @return
     */
    public static List<String> getPrevDaysHour(String dateStr, int position) {
        dateStr = TimeUtil.clearDateStrPattern(dateStr);
        if (dateStr.length() < 10) {
            return null;
        }
        String year = dateStr.substring(0,8);
        String hour = dateStr.substring(8,10);
        Date date = null;
        try {
            date = DateUtils.parseDate(year, new String[]{IndicationConst.QUERY_MONGODB_DATE_PATTERN});
        } catch (ParseException e) {
            log.error("Parse date {} error.", dateStr, e);
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        List<String> days = new LinkedList<String>();
        if (position == 0) {
            position = 1;
        }
        for (int n = Math.abs(position); n > 0; n--) {
            String value = DateFormatUtils.format(calendar.getTime(), IndicationConst.QUERY_MONGODB_DATE_PATTERN) + hour;
            days.add(0,value);
            calendar.add(Calendar.DATE, -1);
        }
        return days;
    }

    /**
     * 获取指定前n天前的相同小时时间 如20181224010000 20181223010000 20181222010000...
     * @param dateStr 日期格式, 传入yyyyMMddHHmmss
     * @param position 前n天
     * @return
     */
    public static List<String> getPrevDaysMinute(String dateStr, int position) {
        if (dateStr.length() < 10) {
            return null;
        }
        String year = dateStr.substring(0,8);
        String hour = dateStr.substring(8,10);
        String minute = dateStr.substring(10,12);
        Date date = null;
        try {
            date = DateUtils.parseDate(year, new String[]{IndicationConst.QUERY_MONGODB_DATE_PATTERN});
        } catch (ParseException e) {
            log.error("Parse date {} error.", dateStr, e);
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        List<String> days = new LinkedList<String>();
        if (position == 0) {
            position = 1;
        }
        for (int n = Math.abs(position); n > 0; n--) {
            String value = DateFormatUtils.format(calendar.getTime(), IndicationConst.QUERY_MONGODB_DATE_PATTERN) + hour + minute;
            days.add(0,value);
            calendar.add(Calendar.DATE, -1);
        }
        return days;
    }

    /**
     * 获取指定日期的前一天
     * @param dateStr
     * @param pattern
     * @return
     */
    public static List<String> getPrevDay(String dateStr, String pattern) {
        Date date = null;
        try {
            if (dateStr.length() == 10 && !dateStr.contains("-")) {
                date = DateUtils.parseDate(dateStr, new String[]{IndicationConst.QUERY_MONGODB_DATE_PATTERN_0});
            } else {
                date = DateUtils.parseDate(dateStr, new String[]{IndicationConst.DATE_PATTERN, IndicationConst.QUERY_MONGODB_DATE_PATTERN});
            }
        } catch (ParseException e) {
            log.error("Parse date {} error.", dateStr, e);
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, -1);
        List<String> days = new LinkedList<String>();
        if (dateStr.length() == 10 && !dateStr.contains("-")) {
            pattern = IndicationConst.QUERY_MONGODB_DATE_PATTERN_0;
        }
        String value = DateFormatUtils.format(calendar.getTime(), pattern);
        days.add(value);
        return days;
    }

    public static String parseDate(String dateStr, String pattern) {
        Date date = null;
        try {
            date = DateUtils.parseDate(dateStr, new String[]{IndicationConst.DATE_PATTERN, IndicationConst.QUERY_MONGODB_DATE_PATTERN});
        } catch (ParseException e) {
            log.error("Parse date {} error.", dateStr, e);
        }
        return DateFormatUtils.format(date, pattern);
    }

    public static void main(String[] a) {
        getPrevDaysHour("20180101050000", -10);
    }

    /**
     * 根据单位进行转化
     * @param value 原始值
     * @param unit 计算单位
     * @return
     */
    public static String formatUnit(Object value, String unit) {
        int decimal = 0;
        if (IndicationConst.INDICATION_ITEM_UNIT_TEN_THOUSAND.equals(unit)) {
            decimal = 4;
        }
        if (IndicationConst.INDICATION_ITEM_UNIT_PERCENT.equals(unit)) {
            decimal = 4;
        }
        if (IndicationConst.INDICATION_ITEM_UNIT_MS.equals(unit)) {
            decimal = 4;
        }
        if (IndicationConst.INDICATION_ITEM_UNIT_S.equals(unit)) {
            decimal = 4;
        }
        String formatValue = formatUnit(value, unit, decimal,1);
        if (decimal == 0 && StringUtils.isNotEmpty(formatValue)) {
            formatValue = formatValue.substring(0, formatValue.indexOf("."));
        }
        return formatValue;
    }

    /**
     * 根据单位进行转化
     * @param value 原始值
     * @param unit 计算单位
     * @return
     */
    public static String formatUnit(Object value, String unit, Integer decimal, Integer a) {
        int scale = 1;
        if (IndicationConst.INDICATION_ITEM_UNIT_TEN_THOUSAND.equals(unit)) {
            scale = 10000;
        }
        return IndicationUtils.formatRate(value, scale, 1, decimal);
    }

    /**
     * 获取所有省份
     * @return
     */
    public static List<IndicationProvinceEntity> getProvinceList() {
        IIndicationProvinceService provinceService = FactoryUtils.getBean(IIndicationProvinceService.class);
        return provinceService.getAllProvince();
    }

    /**
     * 求2位数比值
     * @param numerator
     * @param denominator
     * @return
     */
    public static String calculatePercent(Number numerator,Number denominator){
        DecimalFormat decimalFormat = new DecimalFormat("#.####");
        decimalFormat.setRoundingMode(RoundingMode.DOWN);
        if(denominator.doubleValue()==0){
            return "0";
        }
        if("-0".equals(decimalFormat.format(numerator.doubleValue()/denominator.doubleValue()))) {
            DecimalFormat decimalFormat2 = new DecimalFormat("#.####");
            return decimalFormat2.format(numerator.doubleValue()/denominator.doubleValue());
        }
        return decimalFormat.format(numerator.doubleValue()/denominator.doubleValue());
    }

    /**
     * 获取两数相除的结果 并保留{decimal}位小数
     * @param num1 结果1
     * @param num2 结果2
     * @param unit 单位
     * @param decimal 小数点位数
     * @return
     */
    public static String formatRate(Object num1, Object num2, int unit, Integer decimal) {
        if(null == num1 || null == num2){
            return "0";
        }
        if (Double.valueOf(num2 + "").equals(0.0)) {
            num2 = 1.0;
        }
        BigDecimal b1 = new BigDecimal(num1.toString());
        BigDecimal b2 = new BigDecimal(num2.toString());
        BigDecimal res = b1.divide(b2, 6, BigDecimal.ROUND_HALF_UP);
        if (decimal != null) {
            String pattern = "#.";
            while (decimal > 0) {
                pattern += "#";
                decimal --;
            }
            DecimalFormat decimalFormat = new DecimalFormat(pattern);
            return decimalFormat.format(res.doubleValue() * unit);
        }
        return String.valueOf(res.doubleValue() * unit);
    }

    public static Double formatSum(Object num1, Object num2) {
        BigDecimal b1 = new BigDecimal(num1.toString());
        BigDecimal b2 = new BigDecimal(num2.toString());
        BigDecimal res = b1.add(b2);
        return res.doubleValue();
    }

    public static Double formatDivide(Object num1, Object num2) {
        BigDecimal b1 = new BigDecimal(num1.toString());
        BigDecimal b2 = new BigDecimal(num2.toString());
        if (b2.doubleValue() == 0.0) {
            b2 = new BigDecimal(1);
        }
        BigDecimal res = b1.divide(b2, 6, BigDecimal.ROUND_HALF_UP);
        return res.doubleValue();
    }

    public static Double formatSubtract(Object num1, Object num2) {
        BigDecimal b1 = new BigDecimal(num1.toString());
        BigDecimal b2 = new BigDecimal(num2.toString());
        BigDecimal res = b1.subtract(b2);
        return res.doubleValue();
    }

    public static String formatScale(Object num1, int scale) {
        BigDecimal b1 = new BigDecimal(num1.toString());
        String pattern = "#.";
        while (scale > 0) {
            pattern += "#";
            scale --;
        }
        DecimalFormat decimalFormat = new DecimalFormat(pattern);
        return decimalFormat.format(b1.doubleValue());
    }

    /**
     * 将科学技术法转化为Double型
     * @param number
     * @return
     */
    public static String formatEToDouble(Object number) {
        if (number == null) {
            return "";
        }
        String value = String.valueOf(number);
        if (value.indexOf("E") > 0) {
            BigDecimal b1 = new BigDecimal(number.toString());
            return String.valueOf(b1.doubleValue());
        }
        if (value.endsWith(".0")) {
            return value.substring(0, value.length() - 2);
        }
        return value;
    }
    public static Long getLong(Object element){
        long result = 0;
        if(element != null && !element.equals("")){
            result = new BigDecimal(Double.valueOf(element+"")).setScale(0, BigDecimal.ROUND_HALF_UP).longValue();
        }
        return result;
    }

    /**
     * 省份信息
     */
    public static Map<String, String> PROVINCE_MAP = new LinkedHashMap<String, String>();
    static {
        //PROVINCE_MAP.put("000","全国");
        PROVINCE_MAP.put("010","北京");
        PROVINCE_MAP.put("021","上海");
        PROVINCE_MAP.put("022","天津");
        PROVINCE_MAP.put("023","重庆");
        PROVINCE_MAP.put("031","河北");
        PROVINCE_MAP.put("035","山西");
        PROVINCE_MAP.put("037","河南");
        PROVINCE_MAP.put("041","辽宁");
        PROVINCE_MAP.put("043","吉林");
        PROVINCE_MAP.put("045","黑龙江");
        PROVINCE_MAP.put("047","内蒙古");
        PROVINCE_MAP.put("051","江苏");
        PROVINCE_MAP.put("053","山东");
        PROVINCE_MAP.put("055","安徽");
        PROVINCE_MAP.put("057","浙江");
        PROVINCE_MAP.put("059","福建");
        PROVINCE_MAP.put("071","湖北");
        PROVINCE_MAP.put("073","湖南");
        PROVINCE_MAP.put("075","广东");
        PROVINCE_MAP.put("077","广西");
        PROVINCE_MAP.put("079","江西");
        PROVINCE_MAP.put("080","西藏");
        PROVINCE_MAP.put("081","四川");
        PROVINCE_MAP.put("085","贵州");
        PROVINCE_MAP.put("087","云南");
        PROVINCE_MAP.put("089","海南");
        PROVINCE_MAP.put("091","陕西");
        PROVINCE_MAP.put("093","甘肃");
        PROVINCE_MAP.put("095","宁夏");
        PROVINCE_MAP.put("097","青海");
        PROVINCE_MAP.put("099","新疆");
    }

    /**
     * 获取时间dateList  如果是日期和当前日期是一样的,依旧返回24小时
     */
    public static List<String> getDateListALL(String datetime, String timetype) {
        String today = TimeUtil.getToday("yyyyMMdd");
        List<String> datelist = new ArrayList<String>();
        if (StringUtils.isBlank(datetime)) {
            datetime = today;
        }
        datetime = TimeUtil.clearDateStrPattern(datetime);
        if (StringUtils.isNotBlank(timetype) && (timetype.contains("hour"))) {
            datelist = TimeUtil.add24hoursForOneDay(datetime);
        }
        else if (StringUtils.isNotBlank(timetype) && (timetype.contains("day"))) {
            datelist = TimeUtil.getWholeDayOfMonth(datetime,"yyyyMMdd");
        }
        else if (StringUtils.isNotBlank(timetype) && (timetype.contains("min"))) {
            String begintime=datetime+"0000";
            String endtime=datetime+"2359";
            datelist = TimeUtil.getDayListBetween_minu(begintime,endtime,"yyyyMMddHHmm",1440,"yyyyMMddHHmm");
        }else if (StringUtils.isNotBlank(timetype) && (timetype.contains("month"))) {
            datelist = TimeUtil.getAllMonth(datetime, "yyyyMM");
        }
        else {
            datetime = TimeUtil.clearDateStrPattern(datetime);
            datelist.add(datetime);
        }
        return datelist;
    }
}
