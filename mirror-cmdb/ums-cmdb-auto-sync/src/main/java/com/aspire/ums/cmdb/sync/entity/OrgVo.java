package com.aspire.ums.cmdb.sync.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 *
 */
@ApiModel
public class OrgVo {

    @ApiModelProperty(name="id",notes="组织ID",required=false)
    private String id;

    @ApiModelProperty(name="path",notes="组织id路径，包含层级关系",required=false)
    private String path;

    @ApiModelProperty(name="pathName",notes="组织名路径，包含层级关系",required=false)
    private String pathName;

    @ApiModelProperty(name="name",notes="组织名称",required=true)
    private String name;

    @ApiModelProperty(name="code",notes="组织代码",required=true)
    private String code;

    @ApiModelProperty(name="parentId",notes="父组织id",example="0")
    private String parentId;

    @ApiModelProperty(name="grade",notes="级别")
    private String grade;

    @ApiModelProperty(name="demId",notes="维度id",required=true)
    private String demId;

    @ApiModelProperty(name="orderNo",notes="排序号")
    private Long orderNo;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getPathName() {
        return pathName;
    }

    public void setPathName(String pathName) {
        this.pathName = pathName;
    }

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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getDemId() {
        return demId;
    }

    public void setDemId(String demId) {
        this.demId = demId;
    }

    public Long getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(Long orderNo) {
        this.orderNo = orderNo;
    }

    public String toString() {
        return "{"
                + "\""+"name"+"\""+":"+"\""+this.name+"\","
                +"\""+"code"+"\""+":"+"\""+this.code+"\","
                +"\""+"parentId"+"\""+":"+"\""+this.parentId+"\","
                +"\""+"grade"+"\""+":"+"\""+this.grade+"\","
                +"\""+"demId"+"\""+":"+"\""+this.demId+"\","
                +"\""+"orderNo"+"\""+":"+"\""+this.orderNo+"\""
                + "}";
    }
}
