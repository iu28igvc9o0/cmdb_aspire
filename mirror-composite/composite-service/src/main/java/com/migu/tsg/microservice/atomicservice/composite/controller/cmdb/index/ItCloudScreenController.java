package com.migu.tsg.microservice.atomicservice.composite.controller.cmdb.index;

import com.alibaba.fastjson.JSONObject;
import com.aspire.mirror.composite.service.cmdb.index.IItCloudScreenAPI;
import com.aspire.ums.cmdb.index.payload.ItCloudScreenRequest;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb.index.ItCloudScreenClient;
import com.migu.tsg.microservice.atomicservice.composite.common.excel2pdf.POIModuleUtils;
import com.migu.tsg.microservice.atomicservice.composite.controller.util.ExportExcelUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @ClassName ItCloudScreenController
 * @Description
 * @Author luowenbo
 * @Date 2020/2/27 14:11
 * @Version 1.0
 */
@RestController
@Slf4j
public class ItCloudScreenController implements IItCloudScreenAPI {

    @Autowired
    private ItCloudScreenClient itCloudScreenClient;

    @Autowired
    private POIModuleUtils poiModuleUtils;

    @Override
    public Map<String, Object> getResourceAllocateList(@RequestBody ItCloudScreenRequest req) {
        return itCloudScreenClient.getResourceAllocateList(req);
    }

    @Override
    public List<Map<String, Object>> getResourceAllocateByBizSystem(@RequestBody ItCloudScreenRequest req) {
        return itCloudScreenClient.getResourceAllocateByBizSystem(req);
    }

    @Override
    public List<Map<String, Object>> getBizSystemNotInpect(@RequestBody ItCloudScreenRequest req) {
        return itCloudScreenClient.getBizSystemNotInpect(req);
    }

    @Override
    public List<Map<String, Object>> getMaxUtilizationByMonth(@RequestBody ItCloudScreenRequest req) {
        return itCloudScreenClient.getMaxUtilizationByMonth(req);
    }

    @Override
    public List<Map<String, Object>> getMaxUtilizationByBizSystem(@RequestBody ItCloudScreenRequest req) {
        return itCloudScreenClient.getMaxUtilizationByBizSystem(req);
    }

    @Override
    public List<Map<String, Object>> getAvgUtilizationByMonth(@RequestBody ItCloudScreenRequest req) {
        return itCloudScreenClient.getAvgUtilizationByMonth(req);
    }

    @Override
    public List<Map<String, Object>> getAvgUtilizationByBizSystem(@RequestBody ItCloudScreenRequest req) {
        return itCloudScreenClient.getAvgUtilizationByBizSystem(req);
    }

    @Override
    public Map<String, Object> getMonthMaxUtilization(@RequestBody ItCloudScreenRequest req) {
        return itCloudScreenClient.getMonthMaxUtilization(req);
    }

    @Override
    public Map<String, Object> getMonthAvgUtilization(@RequestBody ItCloudScreenRequest req) {
        return itCloudScreenClient.getMonthAvgUtilization(req);
    }

    @Override
    public Map<String,String> getBizSystemCount(@RequestBody ItCloudScreenRequest req) {
        return itCloudScreenClient.getBizSystemCount(req);
    }

    @Override
    public Map<String, String> getPmBizSystemCount(@RequestBody ItCloudScreenRequest req) {
        return itCloudScreenClient.getPmBizSystemCount(req);
    }

    @Override
    public List<Map<String, Object>> getBizSystemListWithIdcType(@RequestBody ItCloudScreenRequest req) {
        return itCloudScreenClient.getBizSystemListWithIdcType(req);
    }

    @Override
    public Map<String, String> getVmBizSystemCount(@RequestBody ItCloudScreenRequest req) {
        return itCloudScreenClient.getVmBizSystemCount(req);
    }

    @Override
    public Map<String, String> getStoreBizSystemCount(@RequestBody ItCloudScreenRequest req) {
        return itCloudScreenClient.getStoreBizSystemCount(req);
    }

    @Override
    public List<Map<String, Object>> getStoreBizSystemListWithIdcType(@RequestBody ItCloudScreenRequest req) {
        return itCloudScreenClient.getStoreBizSystemListWithIdcType(req);
    }

    @Override
    public Map<String, Object> judgeStatus(@RequestBody ItCloudScreenRequest req) {
        return itCloudScreenClient.judgeStatus(req);
    }

    @Override
    public JSONObject exportAllInstanceData(@RequestBody ItCloudScreenRequest req, HttpServletResponse response) {
        JSONObject jsonObject = new JSONObject();
        try {
            List<Map<String, Object>> list = itCloudScreenClient.exportAllInstanceData(req);
            String fileName = "instanceDetialInfo.xlsx";
            response.setHeader("Content-Disposition", "attachment;filename=".concat(String.valueOf(URLEncoder.encode
                    (fileName, "UTF-8"))));
            response.setHeader("Connection", "close");
            response.setHeader("Content-Type", "application/vnd.ms-excel");
            String[] header = new String[] {"一级部门","二级部门","业务系统","管理IP地址","业务IP","资源池名称","设备类型",
                    "资源开通时间","资源考核时间","虚拟机类型","镜像操作系统","制造厂商","设备型号","项目名称","POD名称","机房位置"};
            String[] keys = new String[] {"department1","department2","bizSystem","ip","other_ip","idcType","device_type",
                    "insert_time","inspect_time","VM_type","device_os_type","device_mfrs","device_model","project_name","pod_name","roomId"};
            OutputStream os = response.getOutputStream();// 取得输出流
            ExportExcelUtil eeu = new ExportExcelUtil();
            Workbook book = new SXSSFWorkbook(128);
            eeu.exportExcel(book, 0, "资源列表", header, list, keys);
            book.write(os);
            os.flush();
            os.close();
            jsonObject.put("flag", "success");
        } catch (Exception e) {
            jsonObject.put("flag", "error");
            jsonObject.put("msg", e.getMessage());
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        return jsonObject;
    }

    @Override
    public JSONObject partDataListExport(@RequestBody ItCloudScreenRequest req, HttpServletResponse response) {
        JSONObject jsonObject = new JSONObject();
        try {
            List<Map<String, Object>> list = itCloudScreenClient.exportPartInstanceData(req);
            String fileName = "instanceDetialInfo.xlsx";
            response.setHeader("Content-Disposition", "attachment;filename=".concat(String.valueOf(URLEncoder.encode
                    (fileName, "UTF-8"))));
            response.setHeader("Connection", "close");
            response.setHeader("Content-Type", "application/vnd.ms-excel");
            String[] header = null;
            String[] keys = null;
            if(req.getModuleFlag() != null) {
                String deviceType = req.getDeviceType() + "已分配设备总量（台）";
                header = new String[] {"业务系统","联系人","联系人电话","二级部门","一级部门","资源池","POD池",deviceType,
                        "月度CPU资源均值利用率","月度内存均值利用率","月度CPU资源峰值利用率","月度内存峰值利用率"};
                keys = new String[] {"biz_system","business_concat","business_concat_phone","department2","department1",
                        "resource_pool","pod","total","cpuAvg","storeAvg","cpuMax","storeMax"};
            } else {
                header = new String[] {"业务系统","联系人","联系人电话","二级部门","一级部门","资源池","POD池",
                        "FCSAN月度资源利用率","IPSAN月度资源利用率","块存储月度资源利用率","备份存储月度资源利用率",
                        "文件存储月度资源利用率","对象存储月度资源均值利用率"};
                keys = new String[] {"biz_system","business_concat","business_concat_phone","department2","department1",
                        "resource_pool","pod","fcsan","ipsan","kcc","bfcc","wjcc","dxcc"};
            }
            OutputStream os = response.getOutputStream();// 取得输出流
            ExportExcelUtil eeu = new ExportExcelUtil();
            Workbook book = new SXSSFWorkbook(128);
            eeu.exportExcel(book, 0, "资源列表", header, list, keys);
            book.write(os);
            os.flush();
            os.close();
            jsonObject.put("flag", "success");
        } catch (Exception e) {
            jsonObject.put("flag", "error");
            jsonObject.put("msg", e.getMessage());
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        return jsonObject;
    }

    @Override
    public Map<String, Object> getCheckScoreList(@RequestBody ItCloudScreenRequest req) {
        return itCloudScreenClient.getCheckScoreList(req);
    }

    @Override
    public LinkedHashMap<String, Object> getSpecificDeviceByBz(@RequestBody ItCloudScreenRequest req) {
        return itCloudScreenClient.getSpecificDeviceByBz(req);
    }

    @Override
    public Map<String, String> exportSpecificDeviceByBz(@RequestBody ItCloudScreenRequest req) {
        Map<String, String> returnMap = new HashMap<>();
        String fileName = "利用率报表.xlsx";
        HttpServletResponse response = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getResponse();
        try {
            response.setHeader("Content-Disposition", "attachment;filename=".concat(String.valueOf(URLEncoder.encode(fileName, "UTF-8"))));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        response.setHeader("Connection", "close");
        response.setHeader("Content-Type", "application/vnd.ms-excel");
        String bizSystem = req.getBizSystem();
        List<String> keyList = Arrays.asList("bizSystem","deviceName","idcType","ip","serviceIp","cpu","memory");
        LinkedHashMap<String, String> headerMap = new LinkedHashMap<>();
        headerMap.put("bizSystem", "业务系统");
        headerMap.put("deviceName", "设备名称");
        headerMap.put("idcType", "资源池");
        headerMap.put("ip", "设备ip");
        headerMap.put("serviceIp", "业务ip");
        List<List<String>> titleLists = new ArrayList<>();
        List<LinkedHashMap<String, Object>> dataList = (List<LinkedHashMap<String, Object>>)itCloudScreenClient.getSpecificDeviceByBz(req).get("data");
        List<LinkedHashMap<String, Object>> resultList = new ArrayList<>();
        List<String> firstList = new ArrayList<>();
        List<String> secondList = new ArrayList<>();
        firstList.addAll(headerMap.values());
        secondList.addAll(headerMap.values());
//        headerMap.put("cpu", "CPU");
//        headerMap.put("memory", "内存");
         // 把日期key转化成datedisplay -- 1号、2号...
        if (dataList == null || dataList.size() == 0) {
            returnMap.put("code", "error");
            returnMap.put("message", "无可导出利用率数据");
            return returnMap;
        }
        for (String firstKey : dataList.get(0).keySet()) {
            if (!keyList.contains(firstKey) && !firstKey.equals("resourceId")) {
                Map<String, String> hasSecond = (Map<String, String>)dataList.get(0).get(firstKey);
                firstList.add(hasSecond.get("dateDisplay"));
                firstList.add(hasSecond.get("dateDisplay"));
                secondList.add("CPU");
                secondList.add("内存");
            }
        }
        for (Map<String, Object> data : dataList) {
            LinkedHashMap<String, Object> dataMap = new LinkedHashMap<>();
            for (String key : data.keySet()) {
                if (headerMap.containsKey(key)) {
                    dataMap.put(headerMap.get(key), data.get(key) == null ? "-" : data.get(key));
                } else if(!key.equals("resourceId")) {
                    Map<String, String> listMap = (Map<String, String>)data.get(key);
                    Map<String, String> tempTistMap = new HashMap<>();
                    tempTistMap.put("CPU", listMap.get("cpu"));
                    tempTistMap.put("内存", listMap.get("memory"));
                    dataMap.put(listMap.get("dateDisplay"), tempTistMap);
                }
                dataMap.put("业务系统", bizSystem);
            }
            resultList.add(dataMap);
        }
        titleLists.add(firstList);
        titleLists.add(secondList);
        ExportExcelUtil eeu = new ExportExcelUtil();
        Workbook book = new SXSSFWorkbook(128);
        try {
            OutputStream os = response.getOutputStream();// 取得输出流
            eeu.exportExcel(book, 0, "test" ,titleLists, resultList, null);
            book.write(os);
            os.flush();
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
            log.error("导出Excel数据失败!", e);
            returnMap.put("code", "error");
            returnMap.put("message", e.getMessage());
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        returnMap.put("code", "success");
        response.setStatus(HttpStatus.NO_CONTENT.value());
        return returnMap;
    }
}
