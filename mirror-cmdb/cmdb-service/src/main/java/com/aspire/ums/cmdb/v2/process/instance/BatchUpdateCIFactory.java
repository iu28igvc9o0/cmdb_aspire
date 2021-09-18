package com.aspire.ums.cmdb.v2.process.instance;

import com.aspire.mirror.common.util.BeanUtil;
import com.aspire.ums.cmdb.collectUnknown.payload.CmdbCollectUnknown;
import com.aspire.ums.cmdb.common.Constants;
import com.aspire.ums.cmdb.instance.payload.ImportProcess;
import com.aspire.ums.cmdb.v2.cache.CacheConst;
import com.aspire.ums.cmdb.v2.code.service.ICmdbCodeService;
import com.aspire.ums.cmdb.v2.collect.service.CmdbCollectApprovalService;
import com.aspire.ums.cmdb.v2.instance.service.ICmdbInstanceService;
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
 * FileName: mirror-master
 * Author:   hangfang
 * Date:     2020/3/22
 * Description: DESCRIPTION
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Data
@Slf4j
@AllArgsConstructor
public class BatchUpdateCIFactory implements Runnable  {
    private String processId;
    private CmdbCollectApprovalService approvalService;
    private ICmdbInstanceService instanceService;
    private ICmdbCodeService codeService;
    private IRedisService redisService;

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
            new Thread(() -> {
                Object object = getRedisService().get(String.format(Constants.REDIS_PROCESS, processId));
                Map<String, Object> updateInfo;
                if (object == null) {
                    updateInfo = new HashMap<>();
                } else {
                    updateInfo = new ObjectMapper().convertValue(object, new TypeReference<Map<String, Object>>(){});
                }
                List<String> instanceIds = (List<String>)updateInfo.get("data");
                String username = updateInfo.get("username").toString();
                List<Map<String,String>> update = (List<Map<String, String>>)  updateInfo.get("update");
                String moduleId = updateInfo.get("module_id").toString();
                Map<String, Object> updateMap = new HashMap<>();
                for (Map<String, String> updateFiled : update) {
                    updateMap.put(updateFiled.get("field"), updateFiled.get("value"));
                }
                if (instanceIds == null) {
                    instanceIds = new ArrayList<>();
                }
                importProcess.setTotalRecord(instanceIds.size());
                log.info("Batch upate ci to approvel change item config processId -> {}, need handler config size {}", processId, instanceIds.size());
                // 插入审核数据
                Integer successCount = 0;
                for (String instanceId : instanceIds) {
                    updateMap.put("module_id", moduleId);
                    List<Map<String, Object>> returnList = new ArrayList<>();
                    if(StringUtils.isEmpty(getActualProcessDetail(processId).getProcessId())) {
                        break;
                    }
                    try {
                        instanceService.updateInstance(instanceId, username, updateMap, "批量修正");
                        successCount++;
                    } catch (Exception e) {
                        Map<String, Object> returnMap = new LinkedHashMap<>();
                        returnMap.put("id", instanceId);
                        returnMap.put("flag", "error");
                        returnMap.put("msg", e.getMessage());
                        returnList.add(returnMap);
                    }
                    if (returnList.size() > 0) {
                        for (Map<String, Object> error : returnList) {
                            Map<String, Object> copyMap = new LinkedHashMap<>();
                            copyMap.put("id", error.get("id"));
                            copyMap.put("失败原因", error.get("msg"));
                            List<Map<String, Object>> errorList = this.getProcessImportError(importProcess.getProcessId());
                            errorList.add(copyMap);
                            // 刷新redis中数据
                            getRedisService().set(String.format(Constants.REDIS_PROCESS_ERROR, importProcess.getProcessId()), errorList);
                        }
                    }
                    importProcess.setSuccessCount(successCount);
                    importProcess.setErrorCount(importProcess.getErrorCount() + returnList.size());
                    //计算导入速度
                    double speed = (new Date().getTime() - importProcess.getStartTime().getTime()) / 1000 * 1.0;
                    importProcess.setProcessCount(importProcess.getProcessCount() + 1);
                    //计算导入剩余时间
                    Integer leaveTime = (int) Math.ceil((speed / importProcess.getProcessCount() * 1.0) * (importProcess.getTotalRecord() - importProcess.getProcessCount()));
                    importProcess.setLeaveTime(Math.abs(leaveTime));
                    //导入结束
                    if (importProcess.getProcessCount().equals(importProcess.getTotalRecord())) {
                        importProcess.setEndTime(new Date());
                    }
                    getRedisService().set(String.format(Constants.REDIS_PROCESS_DETAIL, processId), importProcess);
                }
            }).start();
        }
    }
}
