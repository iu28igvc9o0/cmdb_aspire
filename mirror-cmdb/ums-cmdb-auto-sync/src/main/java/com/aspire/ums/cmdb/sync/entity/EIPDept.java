package com.aspire.ums.cmdb.sync.entity;

import java.io.Serializable;

public class EIPDept implements Serializable {

    private String sort;
    private String timestamp;
    private String sysExceptionCode;
    private String sysExceptionDesc;
    private String fullName;
    private String parentId;
    private String deptName;
    private String timeStampNum;
    private String fullId;
    private String deptId;

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getSysExceptionCode() {
        return sysExceptionCode;
    }

    public void setSysExceptionCode(String sysExceptionCode) {
        this.sysExceptionCode = sysExceptionCode;
    }

    public String getSysExceptionDesc() {
        return sysExceptionDesc;
    }

    public void setSysExceptionDesc(String sysExceptionDesc) {
        this.sysExceptionDesc = sysExceptionDesc;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getTimeStampNum() {
        return timeStampNum;
    }

    public void setTimeStampNum(String timeStampNum) {
        this.timeStampNum = timeStampNum;
    }

    public String getFullId() {
        return fullId;
    }

    public void setFullId(String fullId) {
        this.fullId = fullId;
    }

    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }
}
