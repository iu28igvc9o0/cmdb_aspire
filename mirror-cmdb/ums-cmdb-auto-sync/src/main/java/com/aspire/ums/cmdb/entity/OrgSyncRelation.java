package com.aspire.ums.cmdb.entity;

import java.io.Serializable;

/**
 * 组织同步关联信息表(OrgSyncRelation)实体类
 *
 * @author makejava
 * @since 2020-12-03 15:04:34
 */
public class OrgSyncRelation implements Serializable {
    private static final long serialVersionUID = 935708323742164589L;
    /**
     * 主键
     */
    private String id;
    /**
     * 数据来源
     */
    private String syncSource;
    /**
     * 同步到那个表
     */
    private String syncDestinationTable;
    /**
     * 来源字段名
     */
    private String sourceFieldName;
    /**
     * 目标字段名
     */
    private String destinationFieldName;


    public String getSyncSource() {
        return syncSource;
    }

    public void setSyncSource(String syncSource) {
        this.syncSource = syncSource;
    }

    public String getSyncDestinationTable() {
        return syncDestinationTable;
    }

    public void setSyncDestinationTable(String syncDestinationTable) {
        this.syncDestinationTable = syncDestinationTable;
    }

    public String getSourceFieldName() {
        return sourceFieldName;
    }

    public void setSourceFieldName(String sourceFieldName) {
        this.sourceFieldName = sourceFieldName;
    }

    public String getDestinationFieldName() {
        return destinationFieldName;
    }

    public void setDestinationFieldName(String destinationFieldName) {
        this.destinationFieldName = destinationFieldName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}