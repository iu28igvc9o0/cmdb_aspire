package com.aspire.ums.cmdb.maintain.entity;

import java.io.Serializable;
import java.util.Date;

public class ConfigLog implements Serializable {
  
        
    private static final long serialVersionUID = 1L;
    private String id;
	private String name;
	private String circleName;
	private String moduleName;
    private String circleId;
    private String action;
    private String owner;
    private String ownerName;
    private String desc;
    private String instanceId;
    private Date insertTime;

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getCircleName() {
        return circleName;
    }
    public void setCircleName(String circleName) {
        this.circleName = circleName;
    }
    public String getCircleId() {
        return circleId;
    }
    public void setCircleId(String circleId) {
        this.circleId = circleId;
    }
    public String getAction() {
        return action;
    }
    public void setAction(String action) {
        this.action = action;
    }
    public String getOwner() {
        return owner;
    }
    public void setOwner(String owner) {
        this.owner = owner;
    }
    public String getOwnerName() {
        return ownerName;
    }
    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }
    public String getDesc() {
        return desc;
    }
    public void setDesc(String desc) {
        this.desc = desc;
    }
    public Date getInsertTime() {
        return insertTime;
    }
    public void setInsertTime(Date insertTime) {
        this.insertTime = insertTime;
    }
    
    public String getModuleName() {
        return moduleName;
    }
    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }
    
    public String getInstanceId() {
        return instanceId;
    }
    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }
    @Override
    public String toString() {
        return "ConfigLog [name=" + name + ", circleName=" + circleName + ", circleId=" + circleId + ", action="
                + action + ", owner=" + owner + ", ownerName=" + ownerName + ", desc=" + desc + ", insertTime="
                + insertTime + "]";
    }
	

}