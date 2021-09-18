package com.migu.tsg.microservice.atomicservice.composite.controller.cmdb;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aspire.mirror.composite.service.cmdb.ICompCollectService;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb.CmdbCollectClient;
import com.migu.tsg.microservice.atomicservice.composite.controller.CommonResourceController;
import com.migu.tsg.microservice.atomicservice.composite.controller.authcontext.ResAction;
import com.migu.tsg.microservice.atomicservice.composite.controller.util.ExportExcelUtil;
import org.apache.ibatis.annotations.Param;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: CollectController
 * Author:   zhu.juwang
 * Date:     2019/3/12 15:43
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@RestController
public class CompCollectController extends CommonResourceController implements ICompCollectService {

    private Logger logger = LoggerFactory.getLogger(CompCollectController.class);
    @Autowired
    private CmdbCollectClient collectClient;

    @Override
    public JSONArray getDictList(@PathVariable("code") String code) {
        if (logger.isInfoEnabled()) {
            logger.info("Request method getDictList ... code - > {}", code);
        }
        return collectClient.getDictList(code);
    }

    @Override
    @ResAction(action = "view", resType = "collect")
    public JSONArray getModuleList() {
        if (logger.isInfoEnabled()) {
            logger.info("Request method getModuleList ...");
        }
        return collectClient.getModuleList();
    }

    @Override
    @ResAction(action = "view", resType = "collect")
    public JSONArray getCollectsByModuleId(@PathVariable("moduleId") String moduleId) {
        if (logger.isInfoEnabled()) {
            logger.info("Request method getCollectsByModuleId ... moduleId - > {}", moduleId);
        }
        return collectClient.getCollectsByModuleId(moduleId);
    }

    @Override
    @ResAction(action = "view", resType = "collect")
    public JSONArray getFormsByModuleId(@PathVariable("moduleId") String moduleId) {
        if (logger.isInfoEnabled()) {
            logger.info("Request method getFormsByModuleId ... moduleId - > {}", moduleId);
        }
        return collectClient.getFormsByModuleId(moduleId);
    }

    @Override
    @ResAction(action = "view", resType = "collect")
    public JSONObject getCollectRecordByCollectId(@PathVariable("collectId") String collectId, @Param("pageNumber") Integer pageNumber,
                                                  @Param("pageSize") Integer pageSize) {
        if (logger.isInfoEnabled()) {
            logger.info("Request method getCollectRecordByCollectId ... collectId - > {} pageNumber -> {} pageSize -> {}",
                    collectId, pageNumber, pageSize);
        }
        return collectClient.getCollectRecordByCollectId(collectId, pageNumber, pageSize);
    }

    @Override
    @ResAction(action = "insert", resType = "collect")
    public Map<String, String> saveCollect(HttpServletResponse response, @PathVariable("moduleId") String moduleId, @RequestBody JSONObject requestInfo) {
        if (logger.isInfoEnabled()) {
            logger.info("Request method saveCollect ... request body - > {} ", requestInfo.toString());
        }
        Map<String, String> returnMap = collectClient.saveCollect(moduleId, requestInfo);
        if (returnMap.get("code").equals("success")) {
            response.setStatus(HttpStatus.NO_CONTENT.value());
        } else {
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        return returnMap;
    }

    @Override
    @ResAction(action = "delete", resType = "collect")
    public Map<String, String> deleteCollect(@PathVariable("collectId") String collectId) {
        if (logger.isInfoEnabled()) {
            logger.info("Request method deleteCollect ... collectId - > {} ", collectId);
        }
        return collectClient.deleteCollect(collectId);
    }

    @Override
    @ResAction(action = "view", resType = "collect")
    public JSONObject getChangeLogs(@PathVariable("moduleId") String moduleId, @RequestBody Map<String, Object> requestInfo) {
        if (logger.isInfoEnabled()) {
            logger.info("Request method getChangeLogs ... moduleId -> {} request body - > {} ", moduleId, requestInfo.toString());
        }
        return collectClient.getChangeLogs(moduleId, requestInfo);
    }

    @Override
    @ResAction(action = "view", resType = "collect")
    public Map<String, Object> getChangeLogDetail(@PathVariable("batchId") String batchId) {
        if (logger.isInfoEnabled()) {
            logger.info("Request method getChangeLogDetail ... batchId - > {} ", batchId);
        }
        return collectClient.getChangeLogDetail(batchId);
    }

    @Override
    public Map<String, String> sendNotice(HttpServletResponse response, @RequestBody JSONObject sendRequest) {
        if (logger.isInfoEnabled()) {
            logger.info("Request method sendNotice ... request body - > {} ", sendRequest.toString());
        }
        Map<String, String> returnMap = collectClient.sendNotice(sendRequest);
        if (returnMap.get("code").equals("success")) {
            response.setStatus(HttpStatus.NO_CONTENT.value());
        } else {
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        return returnMap;
    }

    @Override
    public Map<String, String> exportCollectException(HttpServletResponse response, @PathVariable("moduleId") String moduleId,
                                                      @RequestBody Map<String, Object> sendRequest) {
        Map<String, String> returnMap = new HashMap<>();
        try {
            JSONObject returnJson = collectClient.getChangeLogs(moduleId, sendRequest);
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
            String fileName = dateFormat.format(new Date()) + "配置异常列表.xlsx";
            response.setHeader("Content-Disposition", "attachment;filename=".concat(String.valueOf(URLEncoder.encode(fileName, "UTF-8"))));
            response.setHeader("Connection", "close");
            response.setHeader("Content-Type", "application/vnd.ms-excel");
            if (sendRequest != null && sendRequest.size() > 0) {
                //String[] titles = sendRequest.get(0).keySet().toArray(new String[sendRequest.get(0).keySet().size()]);
                OutputStream os = response.getOutputStream();// 取得输出流
                ExportExcelUtil eeu = new ExportExcelUtil();
                Workbook book = new SXSSFWorkbook(128);
                eeu.exportExcel(book, 0, "配置异常列表", titles, dataList, keys);
                book.write(os);
                os.flush();
                os.close();
            }
            returnMap.put("code", "success");
            response.setStatus(HttpStatus.NO_CONTENT.value());
        } catch (Exception e) {
            logger.error("导出Excel数据失败!", e);
            returnMap.put("code", "error");
            returnMap.put("message", e.getMessage());
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        return returnMap;
    }
}
