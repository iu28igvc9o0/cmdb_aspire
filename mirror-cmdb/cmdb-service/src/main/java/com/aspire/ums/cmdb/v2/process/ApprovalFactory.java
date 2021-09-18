package com.aspire.ums.cmdb.v2.process;

import com.alibaba.fastjson.JSONObject;
import com.aspire.mirror.common.util.BeanUtil;
import com.aspire.ums.cmdb.cmic.util.EventThreadUtils;
import com.aspire.ums.cmdb.collectApproval.payload.CmdbCollectApproval;
import com.aspire.ums.cmdb.common.Constants;
import com.aspire.ums.cmdb.instance.payload.ImportProcess;
import com.aspire.ums.cmdb.util.SpringUtils;
import com.aspire.ums.cmdb.v2.collect.CollectConst;
import com.aspire.ums.cmdb.v2.collect.service.CmdbCollectApprovalService;
import com.aspire.ums.cmdb.v2.module.service.ModuleService;
import com.aspire.ums.cmdb.v3.redis.service.IRedisService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import java.util.*;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: ApprovalFactory
 * Author:   hangfang
 * Date:     2019/9/23
 * Description: DESCRIPTION
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Data
@Slf4j
@AllArgsConstructor
public class ApprovalFactory implements Runnable {
    private String username;
    private Integer approvalStatus;
    private String aprrovalDesc;
    private String processId;
    private CmdbCollectApprovalService approvalService;
    /**
     * Redis服务类
     */
    private IRedisService redisService;

    private ModuleService moduleService;


    /**
     * 获取实时的进程信息
     * @return
     */
    public ImportProcess getActualProcessDetail(String processId) {
        Object object = getRedisService().get(String.format(Constants.REDIS_PROCESS_DETAIL, processId));
        if (object == null) {
            return new ImportProcess();
        }
        return new ObjectMapper().convertValue(object, new TypeReference<ImportProcess>(){});
    }

    /**
     * 导入的完整信息
     * @param processId
     * @return
     */
    public List<CmdbCollectApproval> getProcessImportData(String processId) {
        Object object = getRedisService().get(String.format(Constants.REDIS_PROCESS, processId));
        if (object == null) {
            return new ArrayList<>();
        }
        return new ObjectMapper().convertValue(object, new TypeReference<List<CmdbCollectApproval>>(){});
    }

    public List<Map<String, Object>> getProcessImportError(String processId) {
        Object object = getRedisService().get(String.format(Constants.REDIS_PROCESS_ERROR, processId));
        if (object == null) {
            return new LinkedList<>();
        }
        return new ObjectMapper().convertValue(object, new TypeReference<List<Map<String, Object>>>(){});
    }

    @Override
    public void run() {
        if (processId == null) {
            throw new RuntimeException("processId不能为空.");
        }
        final ImportProcess importProcess = this.getActualProcessDetail(processId);
        if (StringUtils.isEmpty(importProcess.getProcessId())) {
            importProcess.setProcessId(processId);
            importProcess.setStartTime(new Date());
            getRedisService().set(String.format(Constants.REDIS_PROCESS_DETAIL, processId), importProcess);
            List<CmdbCollectApproval> approvals = this.getProcessImportData(processId);
            importProcess.setTotalRecord(approvals.size());
            getRedisService().set(String.format(Constants.REDIS_PROCESS_DETAIL, importProcess.getProcessId()), importProcess);
            log.info("Approve change item config processId -> {}, need handler config size {}", processId, approvals.size());
            // 将信息按照instance_id分组, 将相同的instance_id 使用一条信息更新
            Map<String, List<CmdbCollectApproval>> instanceMap = new LinkedHashMap<>();
            long start = System.currentTimeMillis();
            for (CmdbCollectApproval app : approvals) {
                if (!instanceMap.containsKey(app.getInstanceId())) {
                    instanceMap.put(app.getInstanceId(), new ArrayList<>());
                }
                List<CmdbCollectApproval> approvalList = instanceMap.get(app.getInstanceId());
                approvalList.add(app);
            }
            log.info(">>>>>> 按CI归类审核数据 耗时：{}", System.currentTimeMillis() - start);
            start = System.currentTimeMillis();
            // 更新或删除CI数据
            for (String instanceId : instanceMap.keySet()) {
                log.info(">>>>>> 查询redis 耗时：{}", System.currentTimeMillis() - start);
                start = System.currentTimeMillis();
                EventThreadUtils.NORMAL_POOL.execute(() -> {
                    if(StringUtils.isEmpty(getActualProcessDetail(processId).getProcessId())) {
                        return;
                    }
                    long startTime = System.currentTimeMillis();
                    List<CmdbCollectApproval> approvalList = instanceMap.get(instanceId);
                    List<Map<String, Object>> returnList = new ArrayList<>();
                    if ("add".equals(approvalList.get(0).getApprovalType())) {
                        if (checkAlreadyHanlde(importProcess, approvalList.get(0))) {
                            Map<String, Object> returnMap = new HashMap<>();
                            returnMap.put("data", approvalList.get(0));
                            returnMap.put("flag", "error");
                            returnMap.put("msg", "CI审核失败:同批次数据重复.");
                            returnList.add(returnMap);
                        } else {
                            returnList = approvalService.approve(username, approvalStatus,aprrovalDesc, approvalList);
                        }
                    } else {
                        returnList = approvalService.approve(username, approvalStatus,aprrovalDesc, approvalList);
                    }
                    log.info(">>>>>> 审核中 {}条CI数据 耗时：{}", approvalList.size() ,System.currentTimeMillis() - startTime);
                    startTime = System.currentTimeMillis();
                    toHandleResult(approvalList, returnList);
                    log.info(">>>>>> 处理结果 耗时：{}", System.currentTimeMillis() - startTime);
                });
            }
        }
    }

    private boolean checkAlreadyHanlde(ImportProcess process ,CmdbCollectApproval approval) {
        if (approvalStatus == CollectConst.APPROVE_STATUS_REFUSE.intValue()) {
            return false;
        }
        // 判断是否被其他线程处理
        Map<String, Object> data= JSONObject.parseObject(approval.getResourceData(), Map.class);
        // 查线程是否有重复处理
        List<String> primaryKeys = moduleService.getModulePrimaryKeys(data.get("module_id").toString(), data);
        StringBuilder existKey = new StringBuilder();
        for (String key : primaryKeys) {
            existKey.append(key).append("_").append(data.get(key)).append(";");
        }
        if (process.getAlreadyAddHandleData().contains(existKey.toString())) {
            return true;
        } else {
            process.addAlreadyAddHandleData(existKey.toString());
            return false;
        }
    }

    private synchronized void toHandleResult(List<CmdbCollectApproval> approvalList, List<Map<String, Object>> returnList) {
        if(StringUtils.isEmpty(getActualProcessDetail(processId).getProcessId())) {
            return;
        }
        ImportProcess importProcess = this.getActualProcessDetail(processId);
        int successCount = approvalList.size() - returnList.size();
        if (returnList.size() > 0) {
            for (Map<String, Object> approveInfo : returnList) {
                Map<String, Object> copyMap = new LinkedHashMap<>();
                copyMap.putAll(BeanUtil.toMap(approveInfo.get("data")));
                copyMap.put("失败原因", approveInfo.get("msg"));
                List<Map<String, Object>> errorList = getProcessImportError(importProcess.getProcessId());
                errorList.add(copyMap);
                getRedisService().set(String.format(Constants.REDIS_PROCESS_ERROR, importProcess.getProcessId()), errorList);
            }
        }
        importProcess.setSuccessCount(importProcess.getSuccessCount() + successCount);
        importProcess.setErrorCount(importProcess.getErrorCount() + returnList.size());
        //计算导入速度
        double speed = (System.currentTimeMillis() - importProcess.getStartTime().getTime()) / 1000 * 1.0;
        importProcess.setProcessCount(importProcess.getProcessCount() + approvalList.size());
        //计算导入剩余时间
        Integer leaveTime = (int) Math.ceil((speed / importProcess.getProcessCount() * 1.0) * (importProcess.getTotalRecord() - importProcess.getProcessCount()));
        importProcess.setLeaveTime(Math.abs(leaveTime));
        //导入结束
        if (importProcess.getProcessCount().equals(importProcess.getTotalRecord())) {
            importProcess.setEndTime(new Date());
        }
        getRedisService().set(String.format(Constants.REDIS_PROCESS_DETAIL, processId), importProcess);
    }



}
