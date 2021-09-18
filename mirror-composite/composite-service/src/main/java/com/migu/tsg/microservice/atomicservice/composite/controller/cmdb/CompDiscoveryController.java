package com.migu.tsg.microservice.atomicservice.composite.controller.cmdb;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aspire.mirror.composite.service.cmdb.ICompDiscoveryService;
import com.aspire.mirror.composite.service.cmdb.payload.AutoDiscoveryRuleInsertVO;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb.CmdbDiscoveryClient;
import com.migu.tsg.microservice.atomicservice.composite.controller.CommonResourceController;
import com.migu.tsg.microservice.atomicservice.composite.controller.authcontext.ResAction;
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
 * FileName: CompQueryController
 * Author:   zhu.juwang
 * Date:     2019/3/12 15:43
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@RestController
public class CompDiscoveryController extends CommonResourceController implements ICompDiscoveryService {

    private Logger logger = LoggerFactory.getLogger(CompDiscoveryController.class);
    @Autowired
    private CmdbDiscoveryClient discoveryClient;

    @Override
    @ResAction(action = "view", resType = "collect")
    public JSONArray getModuleList() {
        if (logger.isInfoEnabled()) {
            logger.info("Request method getModuleList ...");
        }
        return discoveryClient.getModuleList();
    }

    @Override
    @ResAction(action = "view", resType = "collect")
    public JSONObject getRuleList(@PathVariable("moduleId") String moduleId,
                                  @RequestParam("pageNumber") Integer pageNumber,
                                  @RequestParam("pageSize") Integer pageSize, @RequestBody(required = false) JSONObject param) {
        if (logger.isInfoEnabled()) {
            logger.info("Request method getRuleList ... moduleId->{} pageNumber->{} pageSize->{} param->{}",
                    moduleId, pageNumber, pageSize, param);
        }
        return discoveryClient.getRuleList(moduleId, pageNumber, pageSize, param);
    }

    @Override
    @ResAction(action = "view", resType = "collect")
    public Map<String, String> insertRule(HttpServletResponse response, @PathVariable("moduleId") String moduleId,
                                          @RequestBody AutoDiscoveryRuleInsertVO ruleEntity) {
        if (logger.isInfoEnabled()) {
            logger.info("Request method insertRule ... moduleId->{} ruleEntity->{}",
                    moduleId, ruleEntity);
        }
        Map<String, String> returnMap = discoveryClient.insertRule(moduleId, ruleEntity);
        if (returnMap.get("code").equals("error")) {
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
        } else {
            response.setStatus(HttpStatus.SC_NO_CONTENT);
        }
        return returnMap;
    }

    @Override
    @ResAction(action = "view", resType = "collect")
    public Map<String, String> updateRule(HttpServletResponse response, @PathVariable("moduleId") String moduleId,
                                          @PathVariable("ruleId") String ruleId,
                                          @RequestBody AutoDiscoveryRuleInsertVO ruleEntity) {
        if (logger.isInfoEnabled()) {
            logger.info("Request method updateRule ... moduleId->{} ruleId->{} ruleEntity->{}",
                    moduleId, ruleId, ruleEntity);
        }
        Map<String, String> returnMap = discoveryClient.updateRule(moduleId, ruleId, ruleEntity);
        if (returnMap.get("code").equals("error")) {
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
        } else {
            response.setStatus(HttpStatus.SC_NO_CONTENT);
        }
        return returnMap;
    }

    @Override
    @ResAction(action = "view", resType = "collect")
    public Map<String, String> deleteRule(HttpServletResponse response, @PathVariable("moduleId") String moduleId,
                                          @RequestBody List<String> ruleIds) {
        if (logger.isInfoEnabled()) {
            logger.info("Request method deleteRule ... moduleId->{} ruleIds->{}",
                    moduleId, ruleIds);
        }
        Map<String, String> returnMap = discoveryClient.deleteRule(moduleId, ruleIds);
        if (returnMap.get("code").equals("error")) {
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
        } else {
            response.setStatus(HttpStatus.SC_NO_CONTENT);
        }
        return returnMap;
    }

    @Override
    public Map<String, String> exportRules(HttpServletResponse response, @PathVariable("moduleId") String moduleId,
                                           @RequestBody Map<String, Object> sendRequest) {
        Map<String, String> returnMap = new HashMap<>();
        try {
            JSONObject queryMap = new JSONObject();
            if (sendRequest.containsKey("query")) {
                queryMap = (JSONObject) JSON.toJSON(sendRequest.get("query"));
            }
            JSONObject returnJson = discoveryClient.getRuleList(moduleId, null, null, queryMap);
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
            String fileName = dateFormat.format(new Date()) + "新发现规则.xlsx";
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
