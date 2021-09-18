package com.aspire.ums.cmdb.instance.payload;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.*;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: ImportProcess
 * Author:   zhu.juwang
 * Date:     2019/5/27 21:23
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties
public class ImportProcess {
    /**
     * 处理进程ID
     */
    private String processId;
    /**
     * sheet页名称
     */
    private String sheetName;
    /**
     * 总记录数
     */
    private Integer totalRecord;

    /**
     * 已处理记录数
     */
    private Integer processCount;
    /**
     * 成功记录数
     */
    private Integer successCount;
    /**
     * 失败记录数
     */
    private Integer errorCount;
    /**
     * 操作用户
     */
    private String operatorUser;
    /**
     * 下载文件目录
     */
    private String exportFilePath;


    /**
     * 失败记录
     */
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private List<Map<String, Object>> errorList;

    /**
     * 已处理新增数据
     */
    private List<String> alreadyAddHandleData ;
    /**
     * sheet2中的数据源
     */
    private Map<String, List<Map<String, Object>>> comboDataMap;
    /**
     * Excel表头
     */
    private List<String> headerList;
    /**
     * 预计剩余处理时间
     */
    private Integer leaveTime;

    /**
     * 处理开始时间 距离开始时间处理超过6小时未结束的 系统自动回收
     */
    private Date startTime;
    /**
     * 处理结束时间  处理时长超过6个小时后 系统自动回收
     */
    private Date endTime;
    /**
     * 处理进程提示
     */
    private String processTip;

    public String getProcessTip() {
        return processTip;
    }

    public void setProcessTip(String processTip) {
        this.processTip = processTip;
    }

    public Integer getTotalRecord() {
        return totalRecord == null ? 0 : totalRecord;
    }

    public Integer getProcessCount() {
        return processCount == null ? 0 : processCount;
    }

    public Integer getSuccessCount() {
        return successCount  == null ? 0 : successCount;
    }

    public Integer getErrorCount() {
        return errorCount == null ? 0 : errorCount;
    }

    public List<Map<String, Object>> getErrorList() {
        return errorList == null ? new LinkedList<>() : errorList;
    }

    public String getProcessId() {
        return processId;
    }

    public void setProcessId(String processId) {
        this.processId = processId;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) { this.endTime = endTime; }

    public Integer getLeaveTime() {
        return leaveTime == null ? 0 : leaveTime;
    }

    public void setLeaveTime(Integer leaveTime) {
        this.leaveTime = leaveTime;
    }

    public String getSheetName() { return sheetName; }

    public String getOperatorUser() {return operatorUser;}

    public Map<String, List<Map<String, Object>>> getComboDataMap() {
        return this.comboDataMap;
    }

    public List<String> getHeaderList() {
        return this.headerList;
    }

    public String getExportFilePath() {
        return exportFilePath;
    }

    public void setExportFilePath(String exportFilePath) {
        this.exportFilePath = exportFilePath;
    }

    public synchronized void addAlreadyAddHandleData(String approveKey) {
        if (this.alreadyAddHandleData == null) {
            this.alreadyAddHandleData = new ArrayList<>();
        }
        this.alreadyAddHandleData.add(approveKey);
    }
    public void setAlreadyAddHandleData(List<String> alreadyAddHandleData) {
        this.alreadyAddHandleData = alreadyAddHandleData;
    }

    public List<String> getAlreadyAddHandleData() {
        return alreadyAddHandleData;
    }
}
