package com.aspire.mirror.alert.api.dto.model;

import lombok.Data;

import java.util.Date;

@Data
public class AlertModelRequest {

    /**
     * 模型id
     */
    private String id;
    /**
     * 模型名称
     */
    private String modelName;
    /**
     * 父id
     */
    private String parentId;
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
     * 更新者
     */
    private String updater;
}
