package com.aspire.ums.cmdb.v2.process.web;

import com.aspire.ums.cmdb.allocate.payload.Result;
import com.aspire.ums.cmdb.collectUnknown.payload.CmdbCollectUnknown;
import com.aspire.ums.cmdb.collectUnknown.payload.CmdbCollectUnknownQuery;
import com.aspire.ums.cmdb.common.Constants;
import com.aspire.ums.cmdb.instance.payload.ImportProcess;
import com.aspire.ums.cmdb.process.IProcessAPI;
import com.aspire.ums.cmdb.process.payload.CmdbProcessHandlerManager;
import com.aspire.ums.cmdb.util.UUIDUtil;
import com.aspire.ums.cmdb.v2.collectUnknown.service.CmdbCollectUnknownService;
import com.aspire.ums.cmdb.v2.process.ImportFactory;
import com.aspire.ums.cmdb.v2.process.export.service.IProcessService;
import com.aspire.ums.cmdb.v2.process.instance.UnknownFactory;
import com.aspire.ums.cmdb.v2.process.service.ICmdbProcessHandlerManagerService;
import com.aspire.ums.cmdb.v3.redis.service.IRedisService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: ProcessController
 * Author:   zhu.juwang
 * Date:     2019/6/11 9:54
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@RestController
@Slf4j
public class ProcessController implements IProcessAPI {
    @Autowired
    private IProcessService processService;
    @Autowired
    private IRedisService redisService;
    @Autowired
    private CmdbCollectUnknownService unknownService;
    @Autowired
    private ICmdbProcessHandlerManagerService handlerManagerService;
    @Override
    public Map<String, String> importProcess(@RequestParam("importType") String importType, @RequestBody Map<String, Object> importData) {
        log.info("Request ProcessController.importProcess importType -> {}", importType);
        Map<String, String> returnMap = new HashMap<>();
        CmdbProcessHandlerManager queryHandler = new CmdbProcessHandlerManager();
        queryHandler.setHandlerType(importType);
        CmdbProcessHandlerManager handlerManager = handlerManagerService.get(queryHandler);
        String processId = importData.get("processId").toString();
        if (handlerManager != null) {
            try {
                Class clz = Class.forName(handlerManager.getHandlerClass());
                Constructor constructor = clz.getConstructor();
                ImportFactory handlerFactory = (ImportFactory) constructor.newInstance();
                redisService.set(String.format(Constants.REDIS_PROCESS, processId), importData);
                redisService.set(String.format(Constants.REDIS_PROCESS_DETAIL, processId), new ImportProcess());
                handlerFactory.setImportType(importType);
                handlerFactory.setProcessId(processId);
//                handlerFactory.setHandlerFactory(handlerFactory);
                handlerFactory.run();
            } catch (Exception e) {
                returnMap.put("flag", "false");
                returnMap.put("msg", "无法解析处理类.");
                return returnMap;
            }
        }
        returnMap.put("processId", processId);
        returnMap.put("flag", "true");
        return returnMap;
    }

    @Override
    public Map<String, Object> unknownProcess(@RequestParam("username") String username, @RequestBody CmdbCollectUnknownQuery query) {
        Map<String, Object> returnMap = new HashMap<>();
        Result<CmdbCollectUnknown> result = unknownService.list(query);
        String processId = UUIDUtil.getUUID();
        redisService.set(String.format(Constants.REDIS_PROCESS, processId), result.getData());
        redisService.set(String.format(Constants.REDIS_PROCESS_DETAIL, processId), new ImportProcess());
        UnknownFactory unknownFactory = new UnknownFactory(processId, username);
        unknownFactory.run();
        returnMap.put("processId", processId);
        returnMap.put("flag", "true");
        return returnMap;
    }


    @Override
    public Map<String, Object> getImportProcess(@PathVariable("processId") String processId) {
        try {
            log.info("Request ProcessController.getImportProcess processId -> {}", processId);
            Map<String, Object> returnMap = new HashMap<>();
            Object process = redisService.get(String.format(Constants.REDIS_PROCESS_DETAIL, processId));
            if (process == null) {
                returnMap.put("flag", "false");
                returnMap.put("msg", "没有processId[" + processId + "]文件导入的进程");
            } else {
                returnMap.put("flag", "true");
                returnMap.put("data", new ObjectMapper().convertValue(process, ImportProcess.class));
            }
            return returnMap;
        } catch (Exception e) {
            log.error("Query file import process error. Cause: {}", e.getMessage(), e);
        }
        return null;
    }

    @Override
    public Map<String, Object> removeImportProcess(@PathVariable("processId") String processId) {
        Map<String, Object> returnMap = new HashMap<>();
        try {
            log.info("Request ProcessController.removeImportProcess processId -> {}", processId);
            redisService.asyncRemove(String.format(Constants.REDIS_PROCESS, processId));
            redisService.asyncRemove(String.format(Constants.REDIS_PROCESS_DETAIL, processId));
            redisService.asyncRemove(String.format(Constants.REDIS_PROCESS_ERROR, processId));
            returnMap.put("flag", "true");
            returnMap.put("data", new ImportProcess());
            return returnMap;
        } catch (Exception e) {
            log.error("Remove file import process error. Cause: {}", e.getMessage(), e);
            returnMap.put("flag", "false");
            returnMap.put("msg", e.getMessage());
        }
        return returnMap;
    }

    @Override
    public ImportProcess exportErrorFile(@PathVariable("processId") String processId) {
        try {
            log.info("Request ProcessController.exportErrorFile processId -> {}", processId);
            Object process = redisService.get(String.format(Constants.REDIS_PROCESS_DETAIL, processId));
            if (process == null) {
                throw new RuntimeException("导入进程" + processId + "已失效.");
            }
            Object errorList = redisService.get(String.format(Constants.REDIS_PROCESS_ERROR, processId));
            if (errorList == null) {
                throw new RuntimeException("导入进程" + processId + "无失败信息.");
            }
            ImportProcess importProcess = new ObjectMapper().convertValue(process, new TypeReference<ImportProcess>(){});
            importProcess.setErrorList(new ObjectMapper().convertValue(errorList, new TypeReference<List<Map<String, Object>>>(){}));
            return importProcess;
        } catch (Exception e) {
            log.error("Remove file import process error. Cause: {}", e.getMessage(), e);
        }
        return null;
    }

    @Override
    public List<Map<String, Object>> exportReport(@PathVariable("exportType") String exportType,
                                                  @RequestBody Map<String, Object> exportParams) {
        try {
            log.info("Request ProcessController.exportReport exportType -> {} exportParams -> {}", exportType, exportParams);
            return processService.exportReport(exportType, exportParams);
        } catch (Exception e) {
            log.error("Export excel report error. Cause: {}", e.getMessage(), e);
        }
        return null;
    }
}
