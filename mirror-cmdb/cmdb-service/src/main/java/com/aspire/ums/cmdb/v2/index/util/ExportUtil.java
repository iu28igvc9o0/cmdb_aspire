package com.aspire.ums.cmdb.v2.index.util;

import com.alibaba.fastjson.*;
import com.aspire.ums.cmdb.index.payload.ItCloudScreenExportRequest;
import com.aspire.ums.cmdb.maintenance.util.ExportExcelUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import scala.annotation.meta.param;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @ClassName ExportUtil
 * @Description
 * @Author luowenbo
 * @Date 2020/5/19 9:33
 * @Version 1.0
 */
@Slf4j
public class ExportUtil {

    private final static String MAP = "MAP";
    private final static String LIST = "LIST";

    /*
    *  对返回结果Object对象，进行解析封装
    * */
    public static List<Map<String,String>> transferJSONObject(Object json,String resFlag,String rspType,String rspKey) {
        List<Map<String,String>> rs = new ArrayList<>();
        // 返回的数据直接就是一个JSONArray数组
        if(null == resFlag && null == rspKey) {
            if (MAP.equals(rspType)) {
                Map<String, String> tmp = JSONObject.parseObject(json.toString(), new TypeReference<Map<String, String>>(){});
                rs.add(tmp);
            } else if(LIST.equals(rspType)) {
                rs = JSONArray.parseObject(json.toString(),List.class);
            }
        } else {
            // 返回的数据是一个JSON对象
            JSONObject tmpJsonObject = JSONObject.parseObject(json.toString());
            // 当首层封装了一层标识层
            if(null != resFlag && tmpJsonObject.containsKey(resFlag)) {
                if(!tmpJsonObject.getBoolean(resFlag)) {
                    return new ArrayList<>();
                }
            }
            String keyValue = rspKey;
            if(rspKey.indexOf(":") != -1) {
                String[] s = rspKey.split(":");
                // 最后一个关键值才是要导出的数据
                keyValue = s[s.length - 1];
                for(int i=0;i<s.length - 1;i++) {
                    tmpJsonObject = tmpJsonObject.getJSONObject(s[i]);
                }
            }
            if(MAP.equals(rspType)) {
                tmpJsonObject = tmpJsonObject.getJSONObject(keyValue);
                Map<String, String> tmp = JSONObject.parseObject(tmpJsonObject.toJSONString(), new TypeReference<Map<String, String>>(){});
                rs.add(tmp);
            } else if(LIST.equals(rspType)) {
                JSONArray array = tmpJsonObject.getJSONArray(keyValue);
                rs = JSONArray.parseObject(array.toJSONString(),List.class);
            }
        }
        return rs;
    }

    /*
    *  将数据写进Excel文件，且将文件上传到FTP服务器
    *  srcMp格式:
    *           {
    *               "sheetName":{
    *                   "excelHeader":"",   // excel表头
    *                   "data":[{}]
    *               }
    *           }
    * */
    public static ByteArrayInputStream uploadExcel(Map<String,Map<String,Object>> srcMp){
        ByteArrayInputStream in = null;
        try {
            int index = 0;
            // 将数据写入excel
            ExportExcelUtil eeu = new ExportExcelUtil();
            Workbook book = new SXSSFWorkbook(128);
            for(Map.Entry<String,Map<String,Object>> item : srcMp.entrySet()) {
                Map<String,Object> value = item.getValue();
                // 获取表头数据
                Map<String, String> excelHeaderMp = JSONObject.parseObject(value.get("excelHeader").toString(), new TypeReference<Map<String, String>>(){});
                final List<String> header = new LinkedList<>();
                final List<String> keys = new LinkedList<>();
                for(Map.Entry<String,String> hd : excelHeaderMp.entrySet()) {
                    keys.add(hd.getKey());
                    header.add(hd.getValue());
                }
                // 获取表格数据
                List<Map<String, Object>> dataList = (List<Map<String, Object>>) value.get("data");
                eeu.exportExcel(book, index++, item.getKey(), header.toArray(new String[header.size()]),
                        dataList, keys.toArray(new String[keys.size()]));
            }
            ByteArrayOutputStream ops = null;
            try {
                ops = new ByteArrayOutputStream();
                book.write(ops);
                byte[] b = ops.toByteArray();
                in = new ByteArrayInputStream(b);
                ops.flush();
            } catch (Exception e) {
                log.error("导出excel失败，失败原因：", e);
            } finally {
                IOUtils.closeQuietly(book);
                IOUtils.closeQuietly(ops);
                IOUtils.closeQuietly(in);
            }
        } catch (Exception e) {
            log.error("导出Excel数据失败!", e);
        }
        return in;
    }

    /*
    *  依据传的参数生成导出文件的唯一性值
    * */
    public static String createFileUnqiue(ItCloudScreenExportRequest req){
        String exportType = req.getExportType();
        String department1 = req.getDepartment1() == null ? "" : "-" + req.getDepartment1();
        String department2 = req.getDepartment2() == null ? "" : "-" + req.getDepartment2();
        String idcType = req.getIdcType() == null ? "" : "-" + req.getIdcType();
        String excludeDep = req.getExcludeDep() == null ? "" : "-" + req.getExcludeDep();
        String monthlyTime = req.getMonthlyTime() == null ? "" : "-" + req.getMonthlyTime();
        return exportType + department1 + department2 + idcType + excludeDep + monthlyTime;
    }

    public static JSONObject mergeParams(JSONObject param,ItCloudScreenExportRequest req) {
        if(null == param) {
            param = new JSONObject();
        }
        if(req.getDepartment1() != null) {
            param.put("department1",req.getDepartment1());
        }
        if(req.getDepartment2() != null) {
            param.put("department2",req.getDepartment2());
        }
        if(req.getIdcType() != null) {
            param.put("idcType",req.getIdcType());
        }
        if(req.getExcludeDep() != null) {
            param.put("excludeDep",req.getExcludeDep());
        }
        if(req.getMonthlyTime() != null) {
            if("idcType".equals(req.getExportType())) {
                param.put("month",req.getMonthlyTime());
            } else {
                param.put("monthlyTime",req.getMonthlyTime());
            }
        }
        return param;
    }


    public static String joinParams(String param,ItCloudScreenExportRequest req) {
        try {
            if(null != param) {
                String joinStr = null;
                // 多个参数
                if(param.indexOf("&") != -1) {
                    String[] one = param.split("&");
                    boolean flag = true;
                    for(int i=0;i<one.length;i++) {
                        String[] two = one[i].split("=");
                        if(flag) {
                            joinStr = two[0] + "=" + URLEncoder.encode(two[1],"utf-8");
                            flag = false;
                        } else {
                            joinStr = joinStr + "&" + two[0] + "=" + URLEncoder.encode(two[1],"utf-8");
                        }
                    }
                } else {
                    // 编码中文
                    String[] strs = param.split("=");
                    joinStr = strs[0] + "=" + URLEncoder.encode(strs[1],"utf-8");
                }
                String idcType = req.getIdcType() == null ? "&idcType=" : "&idcType=" +  URLEncoder.encode(req.getIdcType(),"utf-8");
                String month = req.getMonthlyTime() == null ? "&month=" : "&month=" +  req.getMonthlyTime();
                param = joinStr + idcType + month;
            } else {
                String idcType = req.getIdcType() == null ? "idcType=" : "idcType=" +  URLEncoder.encode(req.getIdcType(),"utf-8");
                String month = req.getMonthlyTime() == null ? "&month=" : "&month=" +  req.getMonthlyTime();
                param = idcType + month;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return param;
    }
}
