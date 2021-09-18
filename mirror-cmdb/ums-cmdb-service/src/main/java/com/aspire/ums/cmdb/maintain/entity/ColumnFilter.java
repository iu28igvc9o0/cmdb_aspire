package com.aspire.ums.cmdb.maintain.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

public class ColumnFilter implements Serializable {
  
        
    private static final long serialVersionUID = 1L;
    private String id;
	private String menuType;
	private String moduleId;
    private String columnInfo;
    private Date insertTime;
    private Date updateTime;
    @SuppressWarnings("rawtypes")
    private Map columnMap;
	public ColumnFilter() {
		super();
	}
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getMenuType() {
        return menuType;
    }
    public void setMenuType(String menuType) {
        this.menuType = menuType;
    }
    public String getModuleId() {
        return moduleId;
    }
    public void setModuleId(String moduleId) {
        this.moduleId = moduleId;
    }
    public String getColumnInfo() {
        return columnInfo;
    }
    public void setColumnInfo(String columnInfo) {
        this.columnInfo = columnInfo;
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
    @SuppressWarnings("rawtypes")
    public Map getColumnMap() {
        return columnMap;
    }
    @SuppressWarnings("rawtypes")
    public void setColumnMap(Map columnMap) {
        this.columnMap = columnMap;
    }

}