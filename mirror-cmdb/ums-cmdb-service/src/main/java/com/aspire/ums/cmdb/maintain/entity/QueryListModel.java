package com.aspire.ums.cmdb.maintain.entity;

import java.io.Serializable;
import java.util.List;
import java.util.Map;


public class QueryListModel implements Serializable {

	private static final long serialVersionUID = 1L;

	private String circleId;
	private String name;
	private String moduleId;
	private String tag;
    private String insertStartTime;
    private String insertEndTime;
    private String updateStartTime;
    private String updateEndTime;
    private Integer pageNumber;
    private Integer pageSize;
    private String sort;
    private String order;
	@SuppressWarnings("rawtypes")
    private List<Map> columCondition;
    public String getCircleId() {
        return circleId;
    }
    public void setCircleId(String circleId) {
        this.circleId = circleId;
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
    public String getTag() {
        return tag;
    }
    public void setTag(String tag) {
        this.tag = tag;
    }
    public String getInsertStartTime() {
        return insertStartTime;
    }
    public void setInsertStartTime(String insertStartTime) {
        this.insertStartTime = insertStartTime;
    }
    public String getInsertEndTime() {
        return insertEndTime;
    }
    public void setInsertEndTime(String insertEndTime) {
        this.insertEndTime = insertEndTime;
    }
    public String getUpdateStartTime() {
        return updateStartTime;
    }
    public void setUpdateStartTime(String updateStartTime) {
        this.updateStartTime = updateStartTime;
    }
    public String getUpdateEndTime() {
        return updateEndTime;
    }
    public void setUpdateEndTime(String updateEndTime) {
        this.updateEndTime = updateEndTime;
    }
    public Integer getPageNumber() {
        return pageNumber;
    }
    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }
    public Integer getPageSize() {
        return pageSize;
    }
    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
    public String getSort() {
        return sort;
    }
    public void setSort(String sort) {
        this.sort = sort;
    }
    public String getOrder() {
        return order;
    }
    public void setOrder(String order) {
        this.order = order;
    }
    @SuppressWarnings("rawtypes")
    public List<Map> getColumCondition() {
        return columCondition;
    }
    @SuppressWarnings("rawtypes")
    public void setColumCondition(List<Map> columCondition) {
        this.columCondition = columCondition;
    }
    @Override
    public String toString() {
        return "QueryListModel [circleId=" + circleId + ", name=" + name + ", moduleId=" + moduleId + ", tag=" + tag
                + ", insertStartTime=" + insertStartTime + ", insertEndTime=" + insertEndTime + ", updateStartTime="
                + updateStartTime + ", updateEndTime=" + updateEndTime + ", pageNumber=" + pageNumber + ", pageSize="
                + pageSize + ", sort=" + sort + ", order=" + order + ", columCondition=" + columCondition + "]";
    }
	
}
