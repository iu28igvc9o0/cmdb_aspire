package com.aspire.mirror.composite.payload.model;

import lombok.Data;

@Data
public class ICompAlertModelDetail {

    /**
     * 模型id
     */
    private String id;
    /**
     * 父id
     */
    private String parentId;
    /**
     * 模型名称
     */
    private String modelName;
    /**
     * 模型名称
     */
    private String parentName;
    /**
     * 表名
     */
    private String tableName;
    /**
     * 排序
     */
    private int sort;
    /**
     * 描述
     */
    private String description;
    /**
     * 创建者
     */
    private String creator;
    /**
     * 创建时间
     */
    private String createTime;
    /**
     * 更新者
     */
    private String updater;
    /**
     * 更新时间
     */
    private String updateTime;

}
