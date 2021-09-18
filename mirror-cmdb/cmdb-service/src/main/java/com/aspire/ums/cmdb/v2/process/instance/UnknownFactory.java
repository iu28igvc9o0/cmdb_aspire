package com.aspire.ums.cmdb.v2.process.instance;

import com.aspire.mirror.common.util.BeanUtil;
import com.aspire.ums.cmdb.collectUnknown.payload.CmdbCollectUnknown;
import com.aspire.ums.cmdb.common.Constants;
import com.aspire.ums.cmdb.instance.payload.ImportProcess;
import com.aspire.ums.cmdb.util.SpringUtils;
import com.aspire.ums.cmdb.v2.collectUnknown.service.CmdbCollectUnknownService;
import com.aspire.ums.cmdb.v3.redis.service.IRedisService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import java.util.*;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: RepairEventImportFactory
 * Author:   hangfang
 * Date:     2019/12/14
 * Description: DESCRIPTION
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Slf4j
@NoArgsConstructor
public class UnknownFactory implements Runnable {
    private String username;
    private String processId;
    /**
     * Redis服务类
     */
    private IRedisService redisService;
    public IRedisService getRedisService() {
        if (this.redisService == null) {
            this.redisService = SpringUtils.getBean(IRedisService.class);
        }
        return this.redisService;
    }

    public UnknownFactory(String processId, String username) {
        this.processId = processId;
        this.username = username;
    }

    private CmdbCollectUnknownService unknownService;
    private CmdbCollectUnknownService getUnknownService() {
        if (unknownService == null) {
            unknownService = SpringUtils.getBean(CmdbCollectUnknownService.class);
        }
        return unknownService;
    }

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
            importProcess.setSheetName("异常数据");
            new Thread(() -> {
                Object object = getRedisService().get(String.format(Constants.REDIS_PROCESS, processId));
                List<CmdbCollectUnknown> unknowns;
                if (object == null) {
                    unknowns = new ArrayList<>();
                } else {
                    unknowns = new ObjectMapper().convertValue(object, new TypeReference<List<CmdbCollectUnknown>>(){});
                }
                importProcess.setTotalRecord(unknowns.size());
                getRedisService().set(String.format(Constants.REDIS_PROCESS_DETAIL, importProcess.getProcessId()), importProcess);
                log.info("Approve change item config processId -> {}, need handler config size {}", processId, unknowns.size());
                for (CmdbCollectUnknown collectUnknown : unknowns) {
                    try {
                        if(StringUtils.isEmpty(getActualProcessDetail(processId).getProcessId())) {
                            break;
                        }
                        collectUnknown.setHandleUser(username);
                        getUnknownService().maintain(collectUnknown);
                        importProcess.setSuccessCount(importProcess.getSuccessCount() + 1);
                    } catch (Exception e) {
                        importProcess.setErrorCount(importProcess.getErrorCount() + 1);
                        Map<String, Object> copyMap = new LinkedHashMap<>();
                        copyMap.putAll(BeanUtil.toMap(collectUnknown));
                        copyMap.put("失败原因", e.getMessage());
                        List<Map<String, Object>> errorList = this.getProcessImportError(importProcess.getProcessId());
                        errorList.add(copyMap);
                        // 刷新redis中数据
                        getRedisService().set(String.format(Constants.REDIS_PROCESS_ERROR, importProcess.getProcessId()), errorList);
                    }
                    //计算导入速度
                    double speed = (new Date().getTime() - importProcess.getStartTime().getTime()) / 1000 * 1.0;
                    //计算导入剩余时间
                    Integer leaveTime = (int) Math.ceil((speed / importProcess.getProcessCount() * 1.0) * (importProcess.getTotalRecord() - importProcess.getProcessCount()));
                    importProcess.setLeaveTime(Math.abs(leaveTime));
                    importProcess.setProcessCount(importProcess.getProcessCount() + 1);
                    //导入结束
                    if (importProcess.getProcessCount().equals(importProcess.getTotalRecord())) {
                        importProcess.setEndTime(new Date());
                    }
                    getRedisService().set(String.format(Constants.REDIS_PROCESS_DETAIL, importProcess.getProcessId()), importProcess);
                }
            }).start();
        }
    }
}
