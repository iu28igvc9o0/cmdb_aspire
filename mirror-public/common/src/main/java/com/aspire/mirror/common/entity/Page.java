package com.aspire.mirror.common.entity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 分页查询对象
 * <p>
 * 项目名称:  咪咕微服务运营平台-CICD
 * 包:       com.migu.tsg.msp.microservice.atomicservice.cicd.commom.page
 * 类名称:    Page.java
 * 类描述:    分页查询对象
 * 创建人:    WuFan
 * 创建时间:  2017/08/23 22:27
 * 版本:      v1.0
 */
public class Page implements Serializable {

    private static final long serialVersionUID = 4770650175496403340L;
    /** 每页条数 */
    private Integer pageSize = 20;

    /** 第几页 */
    private int pageNo = 1;

    private Integer begin = 0;

    /** 排序的字段 */
    private String sortName;

    /** 排序类型 */
    private String order;

    /** 组合排序字段,会加 order dir 后 */
    private String assembleOrderBy;

    private Map<String, Object> params = new HashMap<String, Object>();

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    /**
     * @return Integer
     */
    public Integer getBegin() {
        if (pageNo <= 1) {
            begin = 0;
        } else {
            begin = (pageNo - 1) * pageSize;
        }
        return begin;
    }

    public void setBegin(Integer begin) {
        this.begin = begin;
    }

    public String getSortName() {
        return sortName;
    }

    public void setSortName(String sortName) {
        this.sortName = sortName;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getAssembleOrderBy() {
        return assembleOrderBy;
    }

    public void setAssembleOrderBy(String assembleOrderBy) {
        this.assembleOrderBy = assembleOrderBy;
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }
}