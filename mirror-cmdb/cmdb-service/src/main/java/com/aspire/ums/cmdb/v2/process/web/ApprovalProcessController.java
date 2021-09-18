package com.aspire.ums.cmdb.v2.process.web;

import com.aspire.ums.cmdb.collectApproval.payload.CmdbApprovalUpdateReq;
import com.aspire.ums.cmdb.collectApproval.payload.CmdbCollectApproval;
import com.aspire.ums.cmdb.collectApproval.payload.CmdbCollectApprovalQuery;
import com.aspire.ums.cmdb.common.Constants;
import com.aspire.ums.cmdb.instance.payload.ImportProcess;
import com.aspire.ums.cmdb.process.IApprovalProcessAPI;
import com.aspire.ums.cmdb.util.StringUtils;
import com.aspire.ums.cmdb.util.UUIDUtil;
import com.aspire.ums.cmdb.v2.code.service.ICmdbCodeService;
import com.aspire.ums.cmdb.v2.collect.CollectConst;
import com.aspire.ums.cmdb.v2.collect.service.CmdbCollectApprovalService;
import com.aspire.ums.cmdb.v2.instance.service.ICmdbInstanceService;
import com.aspire.ums.cmdb.v2.module.service.ModuleService;
import com.aspire.ums.cmdb.v2.process.ApprovalFactory;
import com.aspire.ums.cmdb.v2.process.instance.BatchUpdateCIFactory;
import com.aspire.ums.cmdb.v3.redis.service.IRedisService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: ApprovalProcessController
 * Author:   hangfang
 * Date:     2019/9/23 9:54
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@RestController
@Slf4j
public class ApprovalProcessController implements IApprovalProcessAPI {

    @Autowired
    private CmdbCollectApprovalService approvalService;
    @Autowired
    private ICmdbInstanceService instanceService;
    @Autowired
    private ICmdbCodeService codeService;
    @Autowired
    private ModuleService moduleService;
    @Autowired
    private IRedisService redisService;

    @Override
    public Map<String, Object> approvalProcess(@RequestParam("userName") String userName, @RequestBody CmdbApprovalUpdateReq updateReq) {
        log.info("Request ApprovalProcessController.approvalProcess updateReq -> {}", updateReq);
        long startTime = System.currentTimeMillis();
        if (updateReq.getUpdateStatus() != CollectConst.APPROVE_STATUS_REFUSE.intValue() &&
            updateReq.getUpdateStatus() != CollectConst.APPROVE_STATUS_PASS.intValue()) {
            throw new RuntimeException("审核状态有误，请求检查审核状态");
        }
        List<CmdbCollectApproval> approvals;
        if (null != updateReq.getPartUpdateList()) {
            // 部分更新
            // todo修改数据获取方式（通过id数据过来）
//            approvals = updateReq.getPartUpdateList();
            approvals = approvalService.listByIds(updateReq.getPartUpdateList());
            log.info(">>>>>> 获取部分更新审核数据 耗时：{}", System.currentTimeMillis() - startTime);
            startTime = System.currentTimeMillis();
        } else {
            if (updateReq.getUpdateStatus() == null) {
                throw new RuntimeException("未告知审核状态!");
            }
            // 全量更新的话根据条件去捞数据
            CmdbCollectApprovalQuery allUpdateQuery = updateReq.getAllUpdateQuery();
            allUpdateQuery.setPageNum(null);
            allUpdateQuery.setPageSize(null);
            approvals =  approvalService.listSimpleByQuery(allUpdateQuery);
//            approvalService.listCount(allUpdateQuery);
            log.info(">>>>>> 获取全量更新审核数据 耗时：{}", System.currentTimeMillis() - startTime);
            startTime = System.currentTimeMillis();
        }
        Map<String, Object> returnMap = new HashMap<>();
        if (approvals.size() == 0) {
            returnMap.put("flag", "true");
            return returnMap;
        }
        String processId = UUIDUtil.getUUID();
        ImportProcess importProcess = new ImportProcess();
        importProcess.setSheetName("异常数据");
        importProcess.setAlreadyAddHandleData(new ArrayList<>());
        redisService.set(String.format(Constants.REDIS_PROCESS, processId), approvals);
        redisService.set(String.format(Constants.REDIS_PROCESS_DETAIL, processId), importProcess);
        ApprovalFactory approvalFactory = new ApprovalFactory(userName, updateReq.getUpdateStatus(),updateReq.getRefuseDesc(), processId, approvalService, redisService, moduleService);
        approvalFactory.run();
        returnMap.put("processId", processId);
        returnMap.put("flag", "true");
        return returnMap;
    }

    @Override
    public Map<String, Object> batchUpdateInstance(@RequestParam("userName") String userName,
                                                   @RequestParam("moduleId") String moduleId,
                                                   @RequestBody Map<String, Object> batchUpdate) {
        log.info("Request ApprovalProcessController.batchUpdateInstance batchUpdate -> {}", batchUpdate);
        Map<String, Object> returnMap = new HashMap<>();
        String processId = UUIDUtil.getUUID();
        ImportProcess importProcess = new ImportProcess();
        List<String> instanceIds = instanceService.getBatchUpdateApprovals(userName, moduleId, batchUpdate);
        Map<String, Object> updateInfo = new HashMap<>();
        updateInfo.put("module_id", moduleId);
        updateInfo.put("update", batchUpdate.get("update"));
        updateInfo.put("data", instanceIds);
        updateInfo.put("username", userName);
        importProcess.setSheetName("异常数据");
        redisService.set(String.format(Constants.REDIS_PROCESS, processId), updateInfo);
        redisService.set(String.format(Constants.REDIS_PROCESS_DETAIL, processId), importProcess);
        BatchUpdateCIFactory batchUpdateCIFactory = new BatchUpdateCIFactory(processId, approvalService, instanceService, codeService, redisService);
        batchUpdateCIFactory.run();
        returnMap.put("processId", processId);
        returnMap.put("flag", "true");
        returnMap.put("msg", instanceIds.size() + "台设备参数更新已录入配置审核");
        return returnMap;
    }

    @Override
    public Map<String, Object> getApprovalProcess(@PathVariable("processId") String processId) {
        try {
            log.info("Request ApprovalProcessController.getApprovalProcess processId -> {}", processId);
            Map<String, Object> returnMap = new HashMap<>();
            Object object = redisService.get(String.format(Constants.REDIS_PROCESS_DETAIL, processId));
            if (object == null) {
                returnMap.put("flag", "false");
                returnMap.put("msg", "没有processId[" + processId + "]审核的进程");
            } else {
                returnMap.put("flag", "true");
                returnMap.put("data", new ObjectMapper().convertValue(object, new TypeReference<ImportProcess>(){}));
            }
            return returnMap;
        } catch (Exception e) {
            log.error("Query file approval process error. Cause: {}", e.getMessage(), e);
        }
        return null;
    }

    @Override
    public Map<String, Object> removeApprovalProcess(@PathVariable("processId") String processId) {
        Map<String, Object> returnMap = new HashMap<>();
        try {
            log.info("Request ApprovalProcessController.removeApprovalProcess processId -> {}", processId);
            redisService.syncRemove(String.format(Constants.REDIS_PROCESS, processId));
            redisService.syncRemove(String.format(Constants.REDIS_PROCESS_DETAIL, processId));
            returnMap.put("flag", "true");
            returnMap.put("data", new ImportProcess());
            return returnMap;
        } catch (Exception e) {
            log.error("Remove file approval process error. Cause: {}", e.getMessage(), e);
            returnMap.put("flag", "false");
            returnMap.put("msg", e.getMessage());
        }
        return returnMap;
    }

    @Override
    public ImportProcess exportErrorFile(@PathVariable("processId") String processId) {
        try {
            log.info("Request ApprovalProcessController.exportErrorFile processId -> {}", processId);
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

}
