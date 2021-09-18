package com.aspire.ums.cmdb.maintain.entity;

import java.io.Serializable;

/**
 * TODO
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.ums.cmdb.maintain.entity
 * 类名称:    InstanceBaseColumn.java
 * 类描述:    TODO
 * 创建人:    JinSu
 * 创建时间:  2018/8/16 17:14
 * 版本:      v1.0
 */
public class InstanceBaseColumn implements Serializable {
    private static final long serialVersionUID = -5240471523419693581L;
    private String instanceId;
    private String name;
    private String moduleId;
    private String insertTime;
    private String updateTime;
    private String moduleName;
    private String roomId;
    private String roomName;
    private String bizSystemName;
    private String ip;
    private String bizSystem;
    private Integer isDelete;

    public Integer getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getBizSystemName() {
        return bizSystemName;
    }

    public void setBizSystemName(String bizSystemName) {
        this.bizSystemName = bizSystemName;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getModuleId() {
        return moduleId;
    }

    public void setModuleId(String moduleId) {
        this.moduleId = moduleId;
    }

    public String getInsertTime() {
        return insertTime;
    }

    public void setInsertTime(String insertTime) {
        this.insertTime = insertTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getBizSystem() {
        return bizSystem;
    }

    public void setBizSystem(String bizSystem) {
        this.bizSystem = bizSystem;
    }
}
