package com.aspire.ums.cmdb.sync.entity;

import java.io.Serializable;

public class EIPUser implements Serializable {

    private String userLogin;
    private String userName;
    private String userCode;
    private String tel;
    private String mail;
    private String dept;
    private String userType;
    private String sort;
    private String timestamp;
    private String sysExceptionCode;
    private String sysExceptionDesc;
    private String timeStampNum;
    private String deptId;
    private String staff;

    public String getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getDept() {
        return dept;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

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

    public String getTimeStampNum() {
        return timeStampNum;
    }

    public void setTimeStampNum(String timeStampNum) {
        this.timeStampNum = timeStampNum;
    }

    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    public String getStaff() {
        return staff;
    }

    public void setStaff(String staff) {
        this.staff = staff;
    }
}
