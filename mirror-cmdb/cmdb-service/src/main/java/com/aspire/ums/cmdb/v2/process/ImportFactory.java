package com.aspire.ums.cmdb.v2.process;

import com.aspire.ums.cmdb.common.Constants;
import com.aspire.ums.cmdb.dict.payload.ConfigDict;
import com.aspire.ums.cmdb.dict.service.ConfigDictService;
import com.aspire.ums.cmdb.helper.JDBCHelper;
import com.aspire.ums.cmdb.instance.payload.ImportProcess;
import com.aspire.ums.cmdb.schema.service.SchemaService;
import com.aspire.ums.cmdb.sqlManage.CmdbSqlManage;
import com.aspire.ums.cmdb.util.SpringUtils;
import com.aspire.ums.cmdb.v2.cache.CacheConst;
import com.aspire.ums.cmdb.v3.redis.service.IRedisService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.exceptions.TooManyResultsException;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司 FileName: ImportFactory Author: zhu.juwang Date: 2019/6/10 19:48 Description: ${DESCRIPTION}
 * History: <author> <time> <version> <desc> 作者姓名 修改时间 版本号 描述
 */
@Data
@Slf4j
@NoArgsConstructor
public abstract class ImportFactory {
    /**
     * 处理进程ID
     */
    private String processId;

    /**
     * 处理类型
     */
    private String importType;

    /**
     * 导入进程类
     */
    private ImportProcess importProcess;

    /**
     * 数据库查询服务类
     */
    private SchemaService schemaService;

    /**
     * 查询数据字典服务类
     */
    private ConfigDictService configDictService;

    /**
     * Redis服务类
     */
    private IRedisService redisService;

    /**
     * JDBC连接处理类
     */
    private JDBCHelper jdbcHelper;

    /**
     * 导入参数
     */
    private Map<String, String> processParams;

    /**
     * 需要处理的数据集
     */
    private List<Map<String, String>> dataList;



    /**
     * 初始化信息
     */
    public void init() {
        if (importType == null) {
            throw new RuntimeException("importType不能为空.");
        }
        if (processId == null) {
            throw new RuntimeException("processId不能为空.");
        }
        initSpringBean();
        initParams();
        initBasic();
    }

    /**
     * 初始化Spring Beans
     */
    public abstract void initSpringBean();

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
    public Map<String, Object> getProcessImportData(String processId) {
        Object object = getRedisService().get(String.format(Constants.REDIS_PROCESS, processId));
        if (object == null) {
            return new HashMap<>();
        }
        return new ObjectMapper().convertValue(object, new TypeReference<Map<String, Object>>(){});
    }

    public List<Map<String, Object>> getProcessImportError(String processId) {
        Object object = getRedisService().get(String.format(Constants.REDIS_PROCESS_ERROR, processId));
        if (object == null) {
            return new LinkedList<>();
        }
        return new ObjectMapper().convertValue(object, new TypeReference<List<Map<String, Object>>>(){});
    }

    /**
     * 初始化导入参数及数据
     */
    private void initParams() {
        importProcess = getActualProcessDetail(this.processId);
        //CacheConst.CACHE_IMPORT_DETAIL.getOrDefault(processId, new ImportProcess());
        // 第一次 初始化信息
        if (StringUtils.isEmpty(importProcess.getProcessId())) {
            importProcess.setProcessId(processId);
            importProcess.setStartTime(new Date());
        }
        this.setErrorSheetName();
        Map<String, Object> importData = this.getProcessImportData(this.processId);
        processParams = (Map<String, String>) importData.get("processParams");
        dataList = (List<Map<String, String>>) importData.get("dataList");
        if (importData.containsKey("comboDataMap") && importData.get("comboDataMap") != null) {
            importProcess.setComboDataMap((Map<String, List<Map<String, Object>>>) importData.get("comboDataMap"));
        }
        if (importData.containsKey("headerList") && importData.get("headerList") != null) {
            importProcess.setHeaderList((List<String>) importData.get("headerList"));
        }
        // 设置需要处理的总数
        if (dataList != null && dataList.size() > 0) {
            importProcess.setTotalRecord(dataList.size());
        } else {
            importProcess.setTotalRecord(0);
        }
        getRedisService().set(String.format(Constants.REDIS_PROCESS_DETAIL, processId), importProcess);
    }

    /**
     * 初始化数据字典等信息
     */
    public void initBasic() {}

    /**
     * 设置下载失败文件的sheet名称
     */
    public void setErrorSheetName() {
        importProcess.setSheetName("异常数据");
    }

    /**
     * 查询 字典值数据
     * 
     * @param querySQL
     *            查询SQL
     * @return
     */
    public List<Map<String, Object>> getDictList(String querySQL) {
        CmdbSqlManage cmdbSqlManage = new CmdbSqlManage();
        cmdbSqlManage.setChartSql(querySQL);
        List<Map<String, Object>> list = getJDBCHelper().getQueryList(cmdbSqlManage, null, null, null, null);
        if (list != null && list.size() > 0) {
            return list;
        } else {
            return new ArrayList<>();
        }
    }

    public SchemaService getSchemaService() {
        if (this.schemaService == null) {
            this.schemaService = SpringUtils.getBean(SchemaService.class);
        }
        return this.schemaService;
    }

    public ConfigDictService getConfigDictService() {
        if (this.configDictService == null) {
            this.configDictService = SpringUtils.getBean(ConfigDictService.class);
        }
        return this.configDictService;
    }

    public JDBCHelper getJDBCHelper() {
        if (this.jdbcHelper == null) {
            this.jdbcHelper = SpringUtils.getBean(JDBCHelper.class);
        }
        return this.jdbcHelper;
    }

    public IRedisService getRedisService() {
        if (this.redisService == null) {
            this.redisService = SpringUtils.getBean(IRedisService.class);
        }
        return this.redisService;
    }

    /**
     * 查询数据字典数据
     * 
     * @param colName
     *            数据字典类型
     * @param pid
     *            父类型ID
     * @param pValue
     *            父类型值
     * @param pType
     *            父类型类型
     * @return
     */
    public List<Map<String, String>> getDictList(String colName, String pid, String pValue, String pType) {
        List<Map<String, String>> returnList = new ArrayList<>();
        List<ConfigDict> dicts = getConfigDictService().selectDictsByType(colName, pid, pValue, pType);
        dicts.forEach(dict -> {
            Map<String, String> dictMap = new HashMap<>();
            dictMap.put("id", dict.getId());
            dictMap.put("key", dict.getName());
            dictMap.put("value", dict.getValue());
            returnList.add(dictMap);
        });
        return returnList;
    }

    /**
     * 验证信息
     */
    public void valid() {}
    /**
     * 数据处理
     * 
     * @param dataMap
     *            逐行处理数据方法
     */
    public abstract void execute(Map<String, String> dataMap);

    /**
     * 执行成功
     * 
     * @param importProcess
     *            当前进度类
     */
    public void executeSuccess(ImportProcess importProcess) {
        importProcess.setSuccessCount(importProcess.getSuccessCount() + 1);
    }

    /**
     * 导入失败行处理
     * 
     * @param importProcess
     *            当前进度类
     * @param dataMap
     *            处理行数据
     * @param throwable
     *            异常类
     */
    public void executeFailed(ImportProcess importProcess, Map<String, String> dataMap, Throwable throwable) {
        importProcess.setErrorCount(importProcess.getErrorCount() + 1);
        Map<String, Object> copyMap = new LinkedHashMap<>();
        copyMap.putAll(dataMap);
        if (throwable instanceof TooManyResultsException) {
            copyMap.put("失败原因", "数据库中数据记录过多, 请检测数据." + throwable.getLocalizedMessage());
        } else {
            copyMap.put("失败原因", throwable.getLocalizedMessage());
        }
        List<Map<String, Object>> errorList = getProcessImportError(importProcess.getProcessId());
        formatErrorReason(copyMap);
        errorList.add(copyMap);
        // 刷新redis中数据
        getRedisService().set(String.format(Constants.REDIS_PROCESS_ERROR, importProcess.getProcessId()), errorList);
    }

    /**
     * 对处理行数据处理
     * 
     * @param dataMap
     *            处理行数据
     */
    public void formatRowDataMap(Map<String, String> dataMap) {}

    /**
     * 处理失败数据
     * 
     * @param errorLineData
     *            错误行数据
     */
    public void formatErrorReason(Map<String, Object> errorLineData) {}

    /**
     * 计算速度
     * 
     * @param importProcess
     *            当前进度类
     */
    public void calcHandlerSpeed(ImportProcess importProcess) {
        // 计算导入速度
        double speed = (new Date().getTime() - importProcess.getStartTime().getTime()) / 1000 * 1.0;
        // 计算导入剩余时间
        Integer leaveTime = (int) Math.ceil((speed / importProcess.getProcessCount() * 1.0)
                * (importProcess.getTotalRecord() - importProcess.getProcessCount()));
        importProcess.setLeaveTime(Math.abs(leaveTime));
        importProcess.setProcessCount(importProcess.getProcessCount() + 1);
        // 导入结束
        if (importProcess.getProcessCount().equals(importProcess.getTotalRecord())) {
            importProcess.setEndTime(new Date());
        }
    }

    /**
     * 开始执行入口
     */
    public void run() {
        // 初始化信息
        this.init();
        // 验证信息
        this.valid();
        // 开启线程 逐行处理数据
        if (dataList != null && dataList.size() > 0) {
            log.info("Import processId -> {}, import type -> {}", processId, importType);
            new Thread(() -> {
                for (Map<String, String> dataMap : dataList) {
                    try {
                        // 如果手工终止了导入进程, 则线程不再继续
                    if (StringUtils.isEmpty(getActualProcessDetail(importProcess.getProcessId()).getProcessId())) {
                        return;
                    }
                    formatRowDataMap(dataMap);
                    execute(dataMap);
                    executeSuccess(importProcess);
                } catch (Exception e) {
                    log.error("导入失败.", e);
                    executeFailed(importProcess, dataMap, e);
                }
                calcHandlerSpeed(importProcess);
                afterRowHandler(dataMap);
                getRedisService().set(String.format(Constants.REDIS_PROCESS_DETAIL, processId), importProcess);
            }
            afterImportHandler();
            log.info("Import finish, successed {} failed {}.", importProcess.getSuccessCount(), importProcess.getErrorCount());
        }   ).start();
        }

    }

    /**
     * 每行处理完以后, 回调事件
     */
    public void afterRowHandler(Map<String, String> dataMap) {}

    /**
     * 所有数据导入完成以后, 回调事件
     */
    public void afterImportHandler() {}

    /**
     * 获取真正的Key值
     * 
     * @param mapKey
     * @return
     */
    public String getRealMapKey(String mapKey) {
        return mapKey.replace("[必填]", "").replace("【必填】", "").replace("(必填)", "").replace("（必填）", "");
    }
}
