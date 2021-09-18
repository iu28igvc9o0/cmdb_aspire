package com.aspire.ums.cmdb.maintain.entity;

import java.io.Serializable;

public class MaintainView implements Serializable {
  
        
    private static final long serialVersionUID = 1L;
    private String id;
	private String circleId;
	private String name;
    private Integer sort;
    private String defaultView;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public String getDefaultView() {
        return defaultView;
    }

    public void setDefaultView(String defaultView) {
        this.defaultView = defaultView;
    }

    @Override
    public String toString() {
        return "MaintainView [circleId=" + circleId + ", name=" + name + ", sort=" + sort + ", defaultView="
                + defaultView + "]";
    }
	


}