package com.aspire.ums.cmdb.sync.payload;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.Lists;

/**
 * 部门组织机构
 *
 * @author jiangxuwen
 * @date 2020/6/10 11:46
 */
public class SysOrgDTO implements Serializable {

    private static final long serialVersionUID = -3250896702153541094L;

    @JsonProperty("data")
    private DataEntity data;

    @JsonProperty("msgSerial")
    private Integer msgSerial;

    @JsonProperty("uuid")
    private String uuid;

    @JsonProperty("timestamp")
    private String timestamp;

    public void setData(DataEntity data) {
        this.data = data;
    }

    public void setMsgSerial(Integer msgSerial) {
        this.msgSerial = msgSerial;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public DataEntity getData() {
        return data;
    }

    public Integer getMsgSerial() {
        return msgSerial;
    }

    public String getUuid() {
        return uuid;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public static class DataEntity {

        @JsonProperty("orgList")
        private List<OrgListEntity> orgList;

        public void setOrgList(List<OrgListEntity> orgList) {
            this.orgList = orgList;
        }

        public List<OrgListEntity> getOrgList() {
            return orgList;
        }

        public static class OrgListEntity {

            @JsonProperty("byAppid")
            private String byAppid;

            @JsonProperty("orgCategory")
            private Integer orgCategory;

            @JsonProperty("departmentId")
            private String departmentId;

            @JsonProperty("enterpriseOrgId")
            private String enterpriseOrgId;

            @JsonProperty("orgId")
            private Long orgId;

            @JsonProperty("orgType")
            private String orgType;

            @JsonProperty("recordAddTime")
            private String recordAddTime;

            @JsonProperty("orgCode")
            private String orgCode;

            @JsonProperty("stopTime")
            private String stopTime;

            @JsonProperty("id")
            private Long id;

            @JsonProperty("openTime")
            private String openTime;

            @JsonProperty("recordUpdateTime")
            private String recordUpdateTime;

            @JsonProperty("operateFlag")
            private String operateFlag;

            @JsonProperty("orgName")
            private String orgName;

            @JsonProperty("operateType")
            private String operateType;

            @JsonProperty("htxlFlag")
            private String htxlFlag;

            @JsonProperty("orgFullName")
            private String orgFullName;

            @JsonProperty("managerId")
            private String managerId;

            @JsonProperty("nodeType")
            private String nodeType;

            @JsonProperty("sortSeq")
            private Integer sortSeq;

            @JsonProperty("isOpen")
            private String isOpen;

            @JsonProperty("parentOrgId")
            private Long parentOrgId;

            @JsonProperty("orgAttitude")
            private String orgAttitude;

            @JsonProperty("byType")
            private String byType;

            @JsonProperty("orgDesc")
            private String orgDesc;

            @JsonIgnore
            private List<OrgListEntity> children = Lists.newArrayList();

            public List<OrgListEntity> getChildren() {
                return children;
            }

            public void setChildren(List<OrgListEntity> children) {
                this.children = children;
            }

            public String getByAppid() {
                return byAppid;
            }

            public void setByAppid(String byAppid) {
                this.byAppid = byAppid;
            }

            public Integer getOrgCategory() {
                return orgCategory;
            }

            public void setOrgCategory(Integer orgCategory) {
                this.orgCategory = orgCategory;
            }

            public String getDepartmentId() {
                return departmentId;
            }

            public void setDepartmentId(String departmentId) {
                this.departmentId = departmentId;
            }

            public String getEnterpriseOrgId() {
                return enterpriseOrgId;
            }

            public void setEnterpriseOrgId(String enterpriseOrgId) {
                this.enterpriseOrgId = enterpriseOrgId;
            }

            public Long getOrgId() {
                return orgId;
            }

            public void setOrgId(Long orgId) {
                this.orgId = orgId;
            }

            public String getOrgType() {
                return orgType;
            }

            public void setOrgType(String orgType) {
                this.orgType = orgType;
            }

            public String getRecordAddTime() {
                return recordAddTime;
            }

            public void setRecordAddTime(String recordAddTime) {
                this.recordAddTime = recordAddTime;
            }

            public String getOrgCode() {
                return orgCode;
            }

            public void setOrgCode(String orgCode) {
                this.orgCode = orgCode;
            }

            public String getStopTime() {
                return stopTime;
            }

            public void setStopTime(String stopTime) {
                this.stopTime = stopTime;
            }

            public Long getId() {
                return id;
            }

            public void setId(Long id) {
                this.id = id;
            }

            public String getOpenTime() {
                return openTime;
            }

            public void setOpenTime(String openTime) {
                this.openTime = openTime;
            }

            public String getRecordUpdateTime() {
                return recordUpdateTime;
            }

            public void setRecordUpdateTime(String recordUpdateTime) {
                this.recordUpdateTime = recordUpdateTime;
            }

            public String getOperateFlag() {
                return operateFlag;
            }

            public void setOperateFlag(String operateFlag) {
                this.operateFlag = operateFlag;
            }

            public String getOrgName() {
                return orgName;
            }

            public void setOrgName(String orgName) {
                this.orgName = orgName;
            }

            public String getOperateType() {
                return operateType;
            }

            public void setOperateType(String operateType) {
                this.operateType = operateType;
            }

            public String getHtxlFlag() {
                return htxlFlag;
            }

            public void setHtxlFlag(String htxlFlag) {
                this.htxlFlag = htxlFlag;
            }

            public String getOrgFullName() {
                return orgFullName;
            }

            public void setOrgFullName(String orgFullName) {
                this.orgFullName = orgFullName;
            }

            public String getManagerId() {
                return managerId;
            }

            public void setManagerId(String managerId) {
                this.managerId = managerId;
            }

            public String getNodeType() {
                return nodeType;
            }

            public void setNodeType(String nodeType) {
                this.nodeType = nodeType;
            }

            public Integer getSortSeq() {
                return sortSeq;
            }

            public void setSortSeq(Integer sortSeq) {
                this.sortSeq = sortSeq;
            }

            public String getIsOpen() {
                return isOpen;
            }

            public void setIsOpen(String isOpen) {
                this.isOpen = isOpen;
            }

            public Long getParentOrgId() {
                return parentOrgId;
            }

            public void setParentOrgId(Long parentOrgId) {
                this.parentOrgId = parentOrgId;
            }

            public String getOrgAttitude() {
                return orgAttitude;
            }

            public void setOrgAttitude(String orgAttitude) {
                this.orgAttitude = orgAttitude;
            }

            public String getByType() {
                return byType;
            }

            public void setByType(String byType) {
                this.byType = byType;
            }

            public String getOrgDesc() {
                return orgDesc;
            }

            public void setOrgDesc(String orgDesc) {
                this.orgDesc = orgDesc;
            }
        }
    }
}
