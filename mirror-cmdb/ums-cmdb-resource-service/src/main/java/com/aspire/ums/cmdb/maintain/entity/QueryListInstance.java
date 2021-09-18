package com.aspire.ums.cmdb.maintain.entity;


import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * TODO
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.ums.cmdb.maintain.entity
 * 类名称:    QueryListInstance.java
 * 类描述:    TODO
 * 创建人:    JinSu
 * 创建时间:  2018/8/16 14:56
 * 版本:      v1.0
 */

public class QueryListInstance implements Serializable {

    private static final long serialVersionUID = 3037134422628920387L;
    private Integer pageNumber;
    private Integer pageSize;
    private String sort;
    private String order;
    private String moduleId;
    private String name;
    private String roomId;
    private String ip;
    private List<String> instanceList;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getRoomId() {
        return roomId;
    }
    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getModuleId() {
        return moduleId;
    }

    public void setModuleId(String moduleId) {
        this.moduleId = moduleId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getInstanceList() {
        return instanceList;
    }

    public void setInstanceList(List<String> instanceList) {
        this.instanceList = instanceList;
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
}
