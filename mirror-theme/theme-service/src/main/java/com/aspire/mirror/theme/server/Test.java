package com.aspire.mirror.theme.server;

import com.aspire.mirror.common.util.DateUtil;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.jayway.jsonpath.DocumentContext;
import org.apache.commons.lang.time.DateUtils;
import org.apache.tomcat.util.security.MD5Encoder;
import org.bouncycastle.crypto.digests.MD5Digest;
import sun.misc.BASE64Decoder;
import sun.security.provider.MD5;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * TODO
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.common.util
 * 类名称:    Test.java
 * 类描述:    TODO
 * 创建人:    JinSu
 * 创建时间:  2018/11/27 18:41
 * 版本:      v1.0
 */
public class Test {
    public static String stampToDate(String s) {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long lt = new Long(s);
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);
        return res;
    }

    public static void main(String[] args) throws IOException {
//
//        DocumentContext jsonCtx = JsonUtil.buildDefaultJsonPathContext(permission.getConstraints());
//        List<Map<String, String>> cons = jsonCtx.read("$");
//        cons.stream().forEach(con->{
//            con.entrySet().stream().forEach(item->{
//                if(item.getValue().contains(",")) {
//                    Set<String> itemSet=null;
//                    String key = item.getKey();
//                    if(!returnMap.containsKey(key)) {
//                        itemSet=new HashSet<String>();
//                    }else {
//                        itemSet=returnMap.get(key);
//                    }
//                    itemSet.addAll(new HashSet<>(Arrays.asList(item.getValue().split(","))));
//                    returnMap.put(key, itemSet);
//                }
//            });
//        });
//        BASE64Decoder decoder = new BASE64Decoder();
//        System.out.println(stampToDate("1555862400000"));
//        String password = "1qaz!QAZ";
//        byte[] b = decoder.decodeBuffer(password);
//        password = new String(b, "GBK");
//        System.out.println(password);
//
//        final Calendar c = Calendar.getInstance();
//        c.set(Calendar.HOUR_OF_DAY, 23);
//        c.set(Calendar.MINUTE, 59);
//        c.set(Calendar.SECOND, 59);
//        c.set(Calendar.MILLISECOND, 999);
//        System.out.println(DateUtil.format(c.getTime(), DateUtil.DATE_TIME_FORMAT));
        // TYJS20180128|广东省|深圳市|1月份|1500|100
//        Gson gson = new Gson();

//        String aa = gson.toJson("[{\"code\":\"rtz_item_value\",\"name\":\"直播用户占比（%）\",\"type\":\"0\",
// \"exp\":\"A\"}," +
//                "{\"code\":\"prevItemValue\",\"name\":\"前一天数据\",\"type\":\"0\",\"exp\":\"B\"}," +
//                "{\"code\":\"sevenAvgValue\",\"name\":\"七日均值\",\"type\":\"0\",\"exp\":\"C\"}," +
//                "{\"code\":\"everyChangeValue\",\"name\":\"逐日变动值\",\"type\":\"0\",\"exp\":\"A-B\"}," +
//                "{\"code\":\"everyChangeRate\",\"name\":\"逐日变动率\",\"type\":\"0\",\"exp\":\"(A-B)/B\"}," +
//                "{\"code\":\"exceptionReason\",\"name\":\"异常原因\",\"type\":\"0\",\"exp\":\"A>100?'超过上限!':'';" +
//                "A<=0?'低于下限':'';A-B>10?'超过变动值上限':'';A-B<-10?'低于变动值下限':''\"}]");
//        System.out.println(aa);
//        List<String> rsList = Lists.newArrayList();
//        System.out.println("aa"+Joiner.on(",").join(rsList));
//        ScriptEngine jse = new ScriptEngineManager().getEngineByName
// ("JavaScript");
////        Float floatRes = 0.0f;
////        System.out.println(floatRes.isNaN());
////        System.out.println("计算结果值：{}" + floatRes);
////        BigDecimal b = new BigDecimal(floatRes);
////        floatRes = b.setScale(4, BigDecimal.ROUND_HALF_UP).floatValue();
////        System.out.println("四舍五入精确到四位小数结果值：{}" + floatRes);
////        System.out.println(floatRes.toString());
//        final Calendar c = Calendar.getInstance();
//        Date groupTime = DateUtil.getDate("20190224230000", DateUtil.DATE_TIME_FORMAT);
//        c.setTime(groupTime);
//        c.add(Calendar.DATE, -1);
//        System.out.println("c"+c.getTime());
          System.out.print(MD5Encoder.encode("1qaz!QAZ".getBytes()));
    }
}
