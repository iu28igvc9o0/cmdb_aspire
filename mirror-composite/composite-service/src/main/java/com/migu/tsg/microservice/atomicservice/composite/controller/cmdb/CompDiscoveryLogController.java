package com.migu.tsg.microservice.atomicservice.composite.controller.cmdb;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aspire.mirror.composite.service.cmdb.ICompDiscoveryLogService;
import com.aspire.mirror.composite.service.cmdb.payload.CompAutoDiscoveryLogVO;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb.CmdbDiscoveryLogClient;
import com.migu.tsg.microservice.atomicservice.composite.controller.CommonResourceController;
import com.migu.tsg.microservice.atomicservice.composite.controller.util.ExportExcelUtil;
import org.apache.http.HttpStatus;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: CompDiscoveryLogController
 * Author:   HANGFANG
 * Date:     2019/4/23 15:43
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@RestController
public class CompDiscoveryLogController extends CommonResourceController implements ICompDiscoveryLogService {

    private Logger logger = LoggerFactory.getLogger(CompDiscoveryLogController.class);

    @Autowired
    private CmdbDiscoveryLogClient discoveryLogClient;

    @Override
    public JSONObject getLogList(@PathVariable("moduleId") String moduleId,
                                 @RequestParam("pageNumber") Integer pageNumber,
                                 @RequestParam("pageSize") Integer pageSize, @RequestBody JSONObject queryData) {
        if (logger.isInfoEnabled()) {
            logger.info("Request method getLogList ... moduleId->{} pageNumber->{} pageSize->{}  queryData->{}",
                    moduleId, pageNumber, pageSize, queryData);
        }
        return discoveryLogClient.getLogList(moduleId, pageNumber, pageSize, queryData);
    }

    @Override
    public Map<String, Object> shieldIp(HttpServletResponse response, @RequestBody List<CompAutoDiscoveryLogVO> discoveryLogs) {
        if (logger.isInfoEnabled()) {
            logger.info("Request method shieldIp ... discoveryLogs->{}",
                    discoveryLogs);
        }
        Map<String, Object> returnMap = discoveryLogClient.shieldIp(discoveryLogs);
        if (returnMap.get("code").equals("error")) {
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
        }
        return returnMap;
    }

    @Override
    public CompAutoDiscoveryLogVO getLogDetailById(@PathVariable("id") String logId) {
        if (logger.isInfoEnabled()) {
            logger.info("Request method getLogDetailById ... logId->{}",
                    logId);
        }
        return discoveryLogClient.getLogDetailById(logId);
    }

    @Override
    public Map<String, Object> bind(HttpServletResponse response, @PathVariable("instanceId") String instanceId,@RequestBody CompAutoDiscoveryLogVO discoveryLog) {
        if (logger.isInfoEnabled()) {
            logger.info("Request method bind ... instanceId->{} discoveryLog-> {}",
                    instanceId, discoveryLog);
        }
        Map<String, Object> returnMap = discoveryLogClient.bind(instanceId, discoveryLog);
        if (returnMap.get("code").equals("error")) {
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
        }
        return returnMap;
    }

    @Override
    public Map<String, Object> update(HttpServletResponse response, @PathVariable("id") String id, @RequestParam(value = "status") String status) {
        if (logger.isInfoEnabled()) {
            logger.info("Request method update ... id->{} status-> {}", id, status);
        }
        Map<String, Object> returnMap = discoveryLogClient.update(id, status);
        if (returnMap.get("code").equals("error")) {
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
        }
        return returnMap;
    }

    @Override
    public JSONObject listInstanceByModuleId(HttpServletResponse response, @PathVariable(value = "moduleId") String moduleId) {
        if (logger.isInfoEnabled()) {
            logger.info("Request method listInstanceByModuleId ... moduleId->{}", moduleId);
        }
        return discoveryLogClient.listInstanceByModuleId(moduleId);
    }

    @Override
    public Map<String, String> exportLogs(HttpServletResponse response, @PathVariable("moduleId") String moduleId,
                                           @RequestBody Map<String, Object> sendRequest) {
        Map<String, String> returnMap = new HashMap<>();
        try{
            JSONObject queryMap = new JSONObject();
            if (sendRequest.containsKey("queryData")) {
                queryMap = (JSONObject) JSON.toJSON(sendRequest.get("queryData"));
            }
            JSONObject returnJson = discoveryLogClient.getLogList(moduleId, null, null, queryMap);
            List<Map<String, String>> header = (List<Map<String, String>>) sendRequest.get("header");
            String[] keys = new String[header.size()];
            String[] titles = new String[header.size()];
            for (int i=0; i < header.size(); i++) {
                keys[i] = header.get(i).get("code");
                titles[i] = header.get(i).get("name");
            }
            List<Map<String, Object>> dataList = new LinkedList<>();
            if (returnJson.containsKey("dataList") && returnJson.get("dataList") != null) {
                dataList = (List<Map<String, Object>>) returnJson.get("dataList");
            }
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddhhmmss");
            String fileName = dateFormat.format(new Date()) + "新发现数据.xlsx";
            response.setHeader("Content-Disposition", "attachment;filename=".concat(String.valueOf(URLEncoder.encode(fileName, "UTF-8"))));
            response.setHeader("Connection", "close");
            response.setHeader("Content-Type", "application/vnd.ms-excel");
            if (sendRequest != null && sendRequest.size() > 0) {
                OutputStream os = response.getOutputStream();// 取得输出流
                ExportExcelUtil eeu = new ExportExcelUtil();
                Workbook book = new SXSSFWorkbook(128);
                eeu.exportExcel(book, 0, "新发现规则", titles, dataList, keys);
                book.write(os);
                os.flush();
                os.close();
            }
            returnMap.put("code", "success");
            response.setStatus(org.springframework.http.HttpStatus.NO_CONTENT.value());
        } catch (Exception e) {
            logger.error("导出Excel数据失败!", e);
            returnMap.put("code", "error");
            returnMap.put("message", e.getMessage());
            response.setStatus(org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        return returnMap;

    }

}
