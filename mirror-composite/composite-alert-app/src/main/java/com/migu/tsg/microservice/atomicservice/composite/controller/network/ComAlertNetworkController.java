package com.migu.tsg.microservice.atomicservice.composite.controller.network;


import com.aspire.mirror.alert.api.dto.network.AlertNormResponse;
import com.aspire.mirror.composite.payload.network.ComAlertNormResponse;
import com.aspire.mirror.composite.payload.network.ComNetworkIndicatorResponse;
import com.aspire.mirror.composite.payload.network.ComNetworkPageData;
import com.aspire.mirror.composite.payload.network.QueryField;
import com.aspire.mirror.composite.service.network.IComAlertNetworkService;
import com.aspire.mirror.elasticsearch.api.dto.network.NetworkIndicatorRequest;
import com.google.common.collect.Lists;

import com.migu.tsg.microservice.atomicservice.composite.clientservice.network.AlertNetworkServiceClient;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.network.EsNetworkIndicatorServiceClient;
import com.migu.tsg.microservice.atomicservice.composite.common.FtpService;
import com.migu.tsg.microservice.atomicservice.composite.controller.util.ExportExcelUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author hewang
 * @version 1.0
 * @date 2021-03-03
 */
@RestController
@Slf4j
public class ComAlertNetworkController implements IComAlertNetworkService {

    @Autowired
    private AlertNetworkServiceClient alertNetworkServiceClient;

    @Autowired
    private FtpService ftpService;


    @Autowired
    private EsNetworkIndicatorServiceClient esService;


    @Override
    public Map<String, Object> queryNetworkPortIndicators(@RequestParam(value = "indicatorName", required = false) String indicatorName,
                                                          @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                                          @RequestParam(value = "pageSize", defaultValue = "50") Integer pageSize) {
        Map<String, Object> map = new HashMap<>(16);
        List<AlertNormResponse> indicators = alertNetworkServiceClient.queryNetworkPortIndicators(indicatorName, pageNum, pageSize);
        map.put("data", indicators);
        return map;
    }


    @Override
    public Map<String, String> addNetworkPortIndicators(@RequestBody List<ComAlertNormResponse> responses) {
        Map<String, String> map = new HashMap<>(16);
        if (CollectionUtils.isEmpty(responses)) {
            log.info("addNetworkPortIndicators req is null");
            map.put("code", "false");
            map.put("message", "req is null");
            return map;
        }
        List<AlertNormResponse> list = new ArrayList<>();
        try {
            for (ComAlertNormResponse response : responses) {
                AlertNormResponse alertNormResponse = new AlertNormResponse();
                BeanUtils.copyProperties(alertNormResponse, response);
                list.add(alertNormResponse);
            }
            String message = alertNetworkServiceClient.addNetworkPortIndicators(list);
            map.put("code", "true");
            map.put("message", message);
        } catch (Exception e) {
            log.error("addNetworkPortIndicators error is {}", e.getMessage());
            map.put("code", "false");
            map.put("message", e.getMessage());
        }
        return map;
    }

    @Override
    public Map<String, Object> queryTopReportTypeConfiguration(@RequestParam(value = "userName", required = false) String userName) {
        List<AlertNormResponse> alertNormResponses = alertNetworkServiceClient.queryTopReportTypeConfiguration(userName);
        Map<String, Object> map = new HashMap<>(16);
        map.put("data", alertNormResponses);
        return map;
    }

    @Override
    public Map<String, String> updateTopReportTypeConfiguration(@RequestBody List<ComAlertNormResponse> responses) {
        Map<String, String> map = new HashMap<>(16);
        if (CollectionUtils.isEmpty(responses)) {
            log.info("updateTopReportTypeConfiguration req is null");
            map.put("code", "false");
            map.put("message", "req is null");
            return map;
        }
        List<AlertNormResponse> list = new ArrayList<>();
        try {
            for (ComAlertNormResponse response : responses) {
                AlertNormResponse alertNormResponse = new AlertNormResponse();
                BeanUtils.copyProperties(response, alertNormResponse);
                list.add(alertNormResponse);
            }
            String message = alertNetworkServiceClient.updateTopReportTypeConfiguration(list);
            map.put("code", message);
            map.put("message", message);
        } catch (Exception e) {
            log.error("updateTopReportTypeConfiguration error is {}", e.getMessage());
            map.put("code", "false");
            map.put("message", e.getMessage());
        }
        return map;
    }

    @Override
    public Map<String, String> deleteTopReportTypeConfiguration(@RequestParam(value = "id", required = false) Integer id) {
        Map<String, String> map = new HashMap<>(16);
        if (id.equals(null) && id == 0) {
            log.info("deleteTopReportTypeConfiguration req is null");
            map.put("code", "false");
            map.put("message", "req is null");
            return map;
        }
        try {
            String message = alertNetworkServiceClient.deleteTopReportTypeConfiguration(id);
            map.put("code", message);
            map.put("message", message);
        } catch (Exception e) {
            log.error("deleteTopReportTypeConfiguration error is {}", e.getMessage());
            map.put("code", "false");
            map.put("message", e.getMessage());
        }
        return map;
    }

    @Override
    public Map<String, String> exportNetworkPortDeviceList(@RequestBody ComNetworkIndicatorResponse request) {
        Map<String, String> map = new HashMap<>(16);
        //设置表头
        List<String> headerList = Lists.newArrayList();
        List<String> keyList = Lists.newArrayList();
        keyList.add("host");
        keyList.add("port");
        headerList.add("设备IP");
        headerList.add("端口名称");
        List<QueryField> params = request.getParams();
        for (QueryField param : params) {
            keyList.add(param.getFieldValue());
            headerList.add(param.getFieldName());
        }
        //设置表格数据
        List<Map<String, Object>> pageResult = new ArrayList<>();
        NetworkIndicatorRequest indicatorRequest = new NetworkIndicatorRequest();
        BeanUtils.copyProperties(request, indicatorRequest);
        List<Map<String, String>> maps = esService.queryNetworkPortDeviceList(indicatorRequest);
        for (Map<String, String> dataMap : maps) {
            Map<String, Object> hashMap = new HashMap<>(16);
            for (String s : keyList) {
                if (dataMap.containsKey(s)) {
                    hashMap.put(s, dataMap.get(s));
                } else {
                    hashMap.put(s, "0");
                }
            }
            pageResult.add(hashMap);
        }

        String title = "网络设备端口导出列表";
        String fileName = title + ".xlsx";
        //excel constuct
        ExportExcelUtil eeu = new ExportExcelUtil();
        SXSSFWorkbook book = new SXSSFWorkbook(128);
        book.setCompressTempFiles(true);
        try {
            eeu.exportExcel(book, 0, title, headerList.toArray(new String[0]), pageResult, keyList.toArray(new String[0]));
        } catch (Exception e) {
            log.error("generate excel failed,", e);
            map.put("code", "0005");
            map.put("message", e.getMessage());
            return map;
        }

        //主动释放资源
        pageResult.clear();
        ByteArrayOutputStream ops = null;
        ByteArrayInputStream in = null;
        log.info("-----2.create excel use-----: {} ms", (System.currentTimeMillis()));
        try {
            ops = new ByteArrayOutputStream();
            book.write(ops);
            byte[] b = ops.toByteArray();
            in = new ByteArrayInputStream(b);
            map = ftpService.uploadtoFTP(fileName, in);
            ops.flush();
            log.info("-----3.upload excel to ftp use-----: {} ms", (System.currentTimeMillis()));
        } catch (Exception e) {
            log.error("导出excel失败，失败原因：", e);
            map.put("code", "0005");
            map.put("message", e.getMessage());
        } finally {
            IOUtils.closeQuietly(book);
            IOUtils.closeQuietly(ops);
            IOUtils.closeQuietly(in);
            return map;
        }

    }

    @Override
    public Map<String, String> exportDataPageDetail(@RequestBody ComNetworkPageData data) {
        Map<String, String> map = new HashMap<>(16);
        //设置表头
        List<String> headerList = Lists.newArrayList();
        List<String> keyList = Lists.newArrayList();
        keyList.add("datetime");
        keyList.add("value");
        headerList.add("监控时间");
        headerList.add("监控值");
        List<Map<String, Object>> mapList = esService.queryExportDataPageDetail(data.getIdcType(), data.getRoomId(), data.getItem(), data.getHost(), data.getPort(), data.getResourceId(), data.getStartTime(), data.getEndTime(), data.getPageSize(), data.getPageNum());
        String title = "设备指标导出列表";
        String fileName = title + ".xlsx";
        //excel constuct
        ExportExcelUtil eeu = new ExportExcelUtil();
        SXSSFWorkbook book = new SXSSFWorkbook(128);
        book.setCompressTempFiles(true);
        try {
            eeu.exportExcel(book, 0, title, headerList.toArray(new String[0]), mapList, keyList.toArray(new String[0]));
        } catch (Exception e) {
            log.error("generate excel failed,", e);
            map.put("code", "0005");
            map.put("message", e.getMessage());
            return map;
        }

        //主动释放资源
        mapList.clear();
        ByteArrayOutputStream ops = null;
        ByteArrayInputStream in = null;
        log.info("-----2.create excel use-----: {} ms", (System.currentTimeMillis()));
        try {
            ops = new ByteArrayOutputStream();
            book.write(ops);
            byte[] b = ops.toByteArray();
            in = new ByteArrayInputStream(b);
            map = ftpService.uploadtoFTP(fileName, in);
            ops.flush();
            log.info("-----3.upload excel to ftp use-----: {} ms", (System.currentTimeMillis()));
        } catch (Exception e) {
            log.error("导出excel失败，失败原因：", e);
            map.put("code", "0005");
            map.put("message", e.getMessage());
        } finally {
            IOUtils.closeQuietly(book);
            IOUtils.closeQuietly(ops);
            IOUtils.closeQuietly(in);
            return map;
        }

    }

}
