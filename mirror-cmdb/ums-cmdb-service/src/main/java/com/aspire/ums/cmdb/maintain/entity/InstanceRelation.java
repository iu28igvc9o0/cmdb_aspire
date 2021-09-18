package com.aspire.ums.cmdb.maintain.entity;

import java.io.Serializable;
import java.util.Date;

public class InstanceRelation implements Serializable {

	private static final long serialVersionUID = 1L;
	private String id;
	private String sourceInstanceId;
	private String targerInstanceId;
	private String moduleRelationId;
	private String circleId;
	private Date insertTime;
	private Date updateTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSourceInstanceId() {
        return sourceInstanceId;
    }

    public void setSourceInstanceId(String sourceInstanceId) {
        this.sourceInstanceId = sourceInstanceId;
    }

    public String getTargerInstanceId() {
        return targerInstanceId;
    }

    public void setTargerInstanceId(String targerInstanceId) {
        this.targerInstanceId = targerInstanceId;
    }

    public String getModuleRelationId() {
        return moduleRelationId;
    }

    public void setModuleRelationId(String moduleRelationId) {
        this.moduleRelationId = moduleRelationId;
    }

    public String getCircleId() {
        return circleId;
    }

    public void setCircleId(String circleId) {
        this.circleId = circleId;
    }

    public Date getInsertTime() {
        return insertTime;
    }

    public void setInsertTime(Date insertTime) {
        this.insertTime = insertTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "InstanceRelation [sourceInstanceId=" + sourceInstanceId + ", targerInstanceId=" + targerInstanceId
                + ", moduleRelationId=" + moduleRelationId + ", circleId=" + circleId + ", insertTime=" + insertTime
                + ", updateTime=" + updateTime + "]";
    }

	

}