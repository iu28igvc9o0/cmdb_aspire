package com.aspire.ums.cmdb.v2.index.util;

import com.alibaba.fastjson.JSONObject;

import java.text.SimpleDateFormat;
import java.util.*;

/*
*  辅助工具类
* */
public class SuppleUtil {

    public static Map<String,String> flagList;
    /*
    * 最近三天标识
    * */
    public static Map<String,String> lastThreeDayFlag(){
        Map<String,String> result = new HashMap<>();
        Calendar ca = Calendar.getInstance();
        for(int i=0;i<3;i++){
            ca.setTime(new Date());
            ca.add(Calendar.DATE, -i);
            Date resultDate = ca.getTime();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            result.put(sdf.format(resultDate),"0");
        }
        return result;
    }

    /*
    * 最近三月标识
    * */
    public static Map<String,String> lastThreeMonthFlag(){
        Map<String,String> result = new HashMap<>();
        Calendar ca = Calendar.getInstance();
        for(int i=0;i<3;i++){
            ca.setTime(new Date());
            ca.add(Calendar.MONTH, -i);
            Date resultDate = ca.getTime();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
            result.put(sdf.format(resultDate),"0");
        }
        return result;
    }

    /*
     * 最近三年标识
     * */
    private static Map<String, String> lastThreeYearFlag() {
        Map<String,String> result = new HashMap<>();
        Calendar ca = Calendar.getInstance();
        for(int i=0;i<3;i++){
            ca.setTime(new Date());
            ca.add(Calendar.YEAR, -i);
            Date resultDate = ca.getTime();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
            result.put(sdf.format(resultDate),"0");
        }
        return result;
    }

    /*
    *   重置标识
    * */
    public static void backToStartPoint(Map<String,String> data){
        for(String key : data.keySet()){
            data.put(key,"0");
        }
    }

    /*
    *   补充和封装数据
    * */
    public static List<Map<String, Object>> packageData(String flag ,List<Map<String,Object>> rsList) {
        Map<String,String> flagList = null;
        // 将时间段作为整个类的变量
        if("DAY".equals(flag)) {
            flagList = lastThreeDayFlag();
        } else if("MONTH".equals(flag)) {
            flagList = lastThreeMonthFlag();
        } else if("YEAR".equals(flag)) {
            flagList = lastThreeYearFlag();
        } else {
            // luowenbo 2020.07.17 null引用漏洞
            flagList = new HashMap<>();
        }
        SuppleUtil.flagList = flagList;
        List<Map<String, Object>> result = new ArrayList<>();
        int len = rsList.size();
        JSONObject obj = new JSONObject();
        for (int i = 0; i < len; i++) {
            // 取到前五个部门后，提前结束for循环
            String crtDep = rsList.get(i).get("department1").toString();
            Object tmpDate = rsList.get(i).get("currentDate");
            Object tmpNum = rsList.get(i).get("deviceNumber");
            obj.put("department", crtDep);
            // 只有既有日期又有数字的才能进行计算
            if(tmpDate != null && tmpNum != null) {
                obj.put(tmpDate.toString(), tmpNum.toString());
                // 将已经放入Obj的时间，标识为1 。 默认情况标识都为0
                flagList.put(tmpDate.toString(), "1");
            }
            // 最后一行数据做特殊处理
            if (i + 1 == len) {
                for (Map.Entry<String, String> entry : flagList.entrySet()) {
                    if ("0".equals(entry.getValue())) {
                        obj.put(entry.getKey(), "0");
                    }
                }
                result.add(obj);
                break;
            }
            String nxtDep = rsList.get(i + 1).get("department1").toString();
            // 如果当前和下一个部门名称不同，将进行数据补充，并且加入返回列表
            if (!crtDep.equals(nxtDep)) {
                // 只取前五个部门的数据
                for (Map.Entry<String, String> entry : flagList.entrySet()) {
                    if ("0".equals(entry.getValue())) {
                        obj.put(entry.getKey(), "0");
                    }
                }
                result.add(obj);
                obj = new JSONObject();
                // 新的一轮，重置标识为0
                backToStartPoint(flagList);
            }
        }
        return getSortDataTop5(result);
    }


    /*
    *  排序取出前十
    * */
    private static List<Map<String, Object>> getSortDataTop5(List<Map<String,Object>> srcList){
        Collections.sort(srcList, new Comparator<Map<String, Object>>(){
            @Override
            public int compare(Map<String, Object> o1, Map<String, Object> o2) {
                int num1 = countFlagTotal(o1);
                int num2 = countFlagTotal(o2);
                return num2-num1;
            }
        });
        if (srcList.size() <= 10) {
            return srcList;
        }
        return srcList.subList(0,10);
    }

    /*
    *  统计标识总数
    * */
    public static int countFlagTotal(Map<String, Object> obj){
        int sum = 0;
        for(String key : SuppleUtil.flagList.keySet()) {
            sum = sum + Integer.parseInt(obj.get(key).toString());
        }
        return sum;
    }

    /*
    * 获取上个月的字符串，例如：参数为"2020-03",返回"2020-02"
    * */
    public static String getPreMonth(String crtMonth) {
        int curYear = Integer.parseInt(crtMonth.split("-")[0]);
        int curMonth = Integer.parseInt(crtMonth.split("-")[1]);
        // 一月份，上个月是去年的12月
        if(curMonth == 1) {
            return curYear + "-" + 12;
        } else {
            String suffix = (curMonth-1) > 9 ? ""+(curMonth-1) : "0"+(curMonth-1);
            return curYear + "-" + suffix;
        }
    }

    /*
    *  根据年份和月份返回月份的天数
    *  @param monthlyTime : 例如 2020-03
    * */
    public static int getDayByMonthAndYear(String monthlyTime) {
        int[] monthDay = {0,31,28,31,30,31,30,31,31,30,31,30,31};
        int year = Integer.parseInt(monthlyTime.split("-")[0]);
        int month = Integer.parseInt(monthlyTime.split("-")[1]);
        // 有月份计算月份的日期，且判断闰年
        if(year%400==0 || (year%4==0 && year%100!=0)) {
            if(month == 2) {
                monthDay[month] = 29;
            }
        }
        return monthDay[month];
    }

    public static void main(String[] args) {
        Map<String,String> result = SuppleUtil.lastThreeMonthFlag();
        //SuppleUtil.backToStartPoint(result);
        System.out.println("sss");
    }
}
