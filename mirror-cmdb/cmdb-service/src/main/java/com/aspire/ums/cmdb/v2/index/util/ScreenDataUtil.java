package com.aspire.ums.cmdb.v2.index.util;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName ScreenDataUtil
 * @Description 大屏数据格式处理
 * @Author luowenbo
 * @Date 2020/4/21 9:28
 * @Version 1.0
 */
public class ScreenDataUtil {

    // 封装响应数据（参数是LIST的）
    public static Map<String, Object> transferDataByList(String validType,List<Map<String,Object>> data){
        Map<String, Object> rs = new HashMap<>();
        if(null != data && !data.isEmpty()) {
            rs.put("flag",true);
            rs.put("data",data);
            return rs;
        } else {
            rs.put("flag",false);
            // 没有数据
            if("CHART".equals(validType)) {
                rs.put("errorMsg","性能数据正在计算中");
            } else if("TABLE".equals(validType)) {
                rs.put("errorMsg","空空如也");
            } else {
                rs.put("errorMsg", null);
            }
            return rs;
        }
    }

    // 封装响应数据（参数是Map的）
    public static Map<String, Object> transferDataByMap(String validType,Map<String,Object> data){
        Map<String, Object> rs = new HashMap<>();
        if(null != data && !data.isEmpty()) {
            rs.put("flag",true);
            rs.put("data",data);
            return rs;
        } else {
            rs.put("flag",false);
            // 没有数据
            if("CHART".equals(validType)) {
                rs.put("errorMsg","性能数据正在计算中");
            } else if("TABLE".equals(validType)) {
                rs.put("errorMsg","空空如也");
            } else {
                rs.put("errorMsg",null);
            }
            return rs;
        }
    }


    // 租户存储资源利用率月份数据组装
    public static Map<String, Object> transferStoreData(List<Map<String, Object>> data){
        if(data == null || data.isEmpty()) {
            Map<String, Object> rs = new HashMap<>();
            Map<String, Object> tmp = new HashMap<>();
            tmp.put("alt",0);
            rs.put("inside",tmp);
            rs.put("outside",tmp);
            rs.put("total",tmp);
            return rs;
        }
        Map<String, Object> rs = new HashMap<>();
        // 初始化封装
        for(int i=1;i<=2;i++) {
            Map<String, Object> tmp = new HashMap<>();
            tmp.put("useAlt",0);
            tmp.put("hasAlt",0);
            tmp.put("alt",0);
            if(i == 1) {
                tmp.put("depType","内部租户");
                rs.put("inside",tmp);
            } else {
                tmp.put("depType","外部租户");
                rs.put("outside",tmp);
            }
        }
        BigDecimal totalUse = new BigDecimal("0");
        BigDecimal totalHas = new BigDecimal("0");
        BigDecimal zero = new BigDecimal("0");
        for(Map<String, Object> item : data) {
            totalUse = MathCalculateUtil.bigDecimalAdd(totalUse,item.get("useAlt").toString());
            totalHas = MathCalculateUtil.bigDecimalAdd(totalHas,item.get("hasAlt").toString());
            String depTypeKey = "内部租户".equals(item.get("depType").toString())?"inside":"outside";
            rs.put(depTypeKey,item);
        }
        Map<String, Object> tmp = new HashMap<>();
        tmp.put("depType","总数");
        if(totalHas.compareTo(zero) == 0) {
            tmp.put("alt",zero);
        } else {
            BigDecimal alt = totalUse.multiply(new BigDecimal("100")).divide(totalHas,2,BigDecimal.ROUND_HALF_UP);
            tmp.put("alt",alt);
        }
        tmp.put("useAlt",totalUse);
        tmp.put("hasAlt",totalHas);
        rs.put("total",tmp);
        return rs;
    }

    /*
    *  封装均峰值内存和CPU上下月的数据
    * */
    public static Map<String, Object> transferCpuAndStoreData(Map<String, Object> crt,Map<String, Object> pre) {
        Map<String, Object> rs = new HashMap<>();
        Object crtCpuMax = crt.get("cpu") == null ? 0:crt.get("cpu");
        Object preCpuMax = pre.get("cpu") == null ? 0:pre.get("cpu");
        Object crtStoreMax = crt.get("store") == null ? 0:crt.get("store");
        Object preStoreMax = pre.get("store") == null ? 0:pre.get("store");
        rs.put("cpu",crtCpuMax);
        rs.put("store",crtStoreMax);
        rs.put("preCpu",preCpuMax);
        rs.put("preStore",preStoreMax);
        return rs;
    }

    /*
    *  在内外部门中求两个月分别的最大值
    * */
    public static Map<String, Object> getCpuAndStoreMax(Map<String, Object> in,Map<String, Object> out) {
        Map<String, Object> rs = new HashMap<>();
        rs.put("cpu",MathCalculateUtil.bigDecimalMax(in.get("cpu").toString(),out.get("cpu").toString()));
        rs.put("store",MathCalculateUtil.bigDecimalMax(in.get("store").toString(),out.get("store").toString()));
        rs.put("preCpu",MathCalculateUtil.bigDecimalMax(in.get("preCpu").toString(),out.get("preCpu").toString()));
        rs.put("preStore",MathCalculateUtil.bigDecimalMax(in.get("preStore").toString(),out.get("preStore").toString()));
        return rs;
    }

    /*
     *  在内外部门中求两个月的权重均值
     * */
    public static Map<String, Object> getCpuAndStoreAvg(Map<String, Object> in,Map<String, Object> out) {
        Map<String, Object> rs = new HashMap<>();
        String inTotal = in.get("total").toString();
        String inPreTotal = in.get("preTotal").toString();
        String outTotal = out.get("total").toString();
        String outPreTotal = out.get("preTotal").toString();
        rs.put("cpu",MathCalculateUtil.bigDecimalAvg(in.get("cpu").toString(),inTotal,out.get("cpu").toString(),outTotal));
        rs.put("store",MathCalculateUtil.bigDecimalAvg(in.get("store").toString(),inTotal,out.get("store").toString(),outTotal));
        rs.put("preCpu",MathCalculateUtil.bigDecimalAvg(in.get("preCpu").toString(),inPreTotal,out.get("preCpu").toString(),outPreTotal));
        rs.put("preStore",MathCalculateUtil.bigDecimalAvg(in.get("preStore").toString(),inPreTotal,out.get("preStore").toString(),outPreTotal));
        return rs;
    }

    // 分数的加法
    // calType为ALL表示总分指标，PART表示小项指标
    // scoreType 为分数类型，不同类型阀值不同
    // threshold为阀值，总和加起来不会超过阀值
    public static String scoreAdd(String totalV ,String addV,String scoreType,String calType) {
        BigDecimal t = new BigDecimal(totalV);
        BigDecimal a = new BigDecimal(addV);
        BigDecimal rs = t.add(a);
        int threshold;
        if("ALL".equals(calType)) {
            threshold = 3;
        } else {
            if("cpuMax".equals(scoreType)) {
                // 均峰值指标
                threshold = 2;
            } else if ("deploy".equals(scoreType) || "inform".equals(scoreType)) {
                // 上线部署时间和通知上线时间指标
                threshold = 1;
            } else {
                threshold = 2;
            }
        }
        if(rs.intValue() >= threshold) {
            return threshold + ".0";
        }
        return rs.toPlainString();
    }

    /*
    *  分数累计得分，没有阀值，一直相加
    * */
    public static String scoreAdd(String totalV , String addV) {
        BigDecimal t = new BigDecimal(totalV);
        BigDecimal a = new BigDecimal(addV);
        BigDecimal rs = t.add(a);
        return rs.toPlainString();
    }


    /*
    *  得出累计考核月份，如当前月2020-04月份，返回2020-03,2020-02,2020-01月
    * */
    public static String[] calMonthArray(String monthlyTime) {
        List<String> rs = new ArrayList<>();
        // 切割字符串
        String year = monthlyTime.split("-")[0];
        String month = monthlyTime.split("-")[1];
        // 转化月份
        int m = Integer.parseInt(month);
        for(int i=1;i<=m;i++) {
            String rsMonth = i < 10 ? "0" + i : "" + i;
            rs.add(year + "-" + rsMonth);
        }
        return rs.toArray(new String[rs.size()]);
    }


    // 根据规则计算CPU均峰值的扣分数值
    public static String calCpuMaxScore(String cpuMax){
        // 四舍五入取整再计算扣分数值
        BigDecimal tMax = new BigDecimal(cpuMax);
        int i = tMax.setScale(0,BigDecimal.ROUND_HALF_UP).intValue();
        if(i >= 30) {
            return "0";
        }
        BigDecimal thirty = new BigDecimal("30");
        BigDecimal a = new BigDecimal(String.valueOf(i));
        BigDecimal c = new BigDecimal("0.4");
        BigDecimal b = thirty.subtract(a);
        BigDecimal e = b.multiply(c);
        if(e.intValue() >= 2) {
            return "2.0";
        }
        return e.toPlainString();
    }

    // 依据key值返回不同的中文消息
    public static String getValidateMsgByKey(String key) {
        String rs;
        switch (key) {
            case "cpuMax" : rs = "#均峰值CPU"; break;
            case "storeMax" : rs = "#均峰值内存"; break;
            case "cpuAvg" : rs = "#均值CPU"; break;
            case "storeAvg" : rs = "#均值内存"; break;
            default:
                rs = "数据缺失";
        }
        return rs;
    }

    // 比较两个数值String，返回大的String
    public static String compareStringToMax(String a1,String a2) {
        BigDecimal b1 = new BigDecimal(a1);
        BigDecimal b2 = new BigDecimal(a2);
        // b1 > b2
        if(b1.compareTo(b2) == 1){
            return a1;
        } else {
            return a2;
        }
    }

    // String 除法
    public static String dev(String fm,String fz) {
        if("0".equals(fm)) {
            return "0";
        }
        BigDecimal bfm = new BigDecimal(fm);
        BigDecimal bfz = new BigDecimal(fz);
        BigDecimal rs = bfz.divide(bfm,2,BigDecimal.ROUND_HALF_UP);
        // BigDecimal rs = bfz.divide(bfm,2);
        return rs.toPlainString();
    }
}
