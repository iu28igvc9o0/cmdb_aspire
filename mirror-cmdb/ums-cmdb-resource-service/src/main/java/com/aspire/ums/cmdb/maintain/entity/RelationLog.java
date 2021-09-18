package com.aspire.ums.cmdb.maintain.entity;

import java.io.Serializable;
import java.util.Date;

public class RelationLog implements Serializable {
  
        
    private static final long serialVersionUID = 1L;
    private String id;
	private String name;
	private String circleId;
	private String instanceId;
    private String relationName;
    private String targetName;
    private String action;
    private String owner;
    private String ownerName;
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
    public String getInstanceId() {
        return instanceId;
    }
    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }
    public String getRelationName() {
        return relationName;
    }
    public void setRelationName(String relationName) {
        this.relationName = relationName;
    }
    public String getTargetName() {
        return targetName;
    }
    public void setTargetName(String targetName) {
        this.targetName = targetName;
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
    public Date getInsertTime() {
        return insertTime;
    }
    public void setInsertTime(Date insertTime) {
        this.insertTime = insertTime;
    }
    
    public String getCircleId() {
        return circleId;
    }
    public void setCircleId(String circleId) {
        this.circleId = circleId;
    }
    @Override
    public String toString() {
        return "RelationLog [name=" + name + ", instanceId=" + instanceId + ", relationName=" + relationName
                + ", targetName=" + targetName + ", action=" + action + ", owner=" + owner + ", ownerName=" + ownerName
                + ", insertTime=" + insertTime + "]";
    }
  
	

}