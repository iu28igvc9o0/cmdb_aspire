package com.migu.tsg.microservice.atomicservice.composite.vo.alert;

import lombok.Data;

@Data
public class AlertFieldDetailVo {

    /**
     * 模型字段id
     */
    private String id;
    /**
     * 模型id
     */
    private String modelId;
    /**
     * 字段代码
     */
    private String fieldCode;
    /**
     * 字段名称
     */
    private String fieldName;
    /**
     * 字段类型
     */
    private String fieldType;
    /**
     * 是否锁定
     */
    private String isLock;
    /**
     * 数据类型
     */
    private String dataType;
    /**
     * 数据长度
     */
    private String dataLength;
    /**
     * 提示信息
     */
    private String dataTip;
    /**
     * ci代码
     */
    private String ciCode;
    /**
     * 是否是CI参数 0-否 1-是
     */
    private String isCiParams;
    /**
     * 参数名称
     */
    private String paramsName;
    /**
     * 参数代码
     */
    private String paramCode;
    /**
     * 是否是列表展示 0-否 1-是
     */
    private String isListShow;
    /**
     * 展示顺序
     */
    private int listShowSort;
    /**
     * 列表展示名称
     */
    private String listShowName;
    /**
     * 列表展示格式
     */
    private String listShowPattern;
    /**
     * 表列宽度
     */
    private String tableColumnWidth;
    /**
     * 是否是详情展示 0-否 1-是
     */
    private String isDetailShow;
    /**
     * 详情排序
     */
    private int detailShowSort;
    /**
     * 详情展示名称
     */
    private String detailShowName;
    /**
     * 详情展示格式
     */
    private String detailShowPattern;
    /**
     * 是否是查询参数 0-否 1-是
     */
    private String isQueryParam;
    /**
     * 查询顺序
     */
    private int queryParamSort;
    /**
     * 查询参数名称
     */
    private String queryParamName;
    /**
     * 查询参数格式
     */
    private String queryParamType;
    /**
     * 查询参数数据来源
     */
    private String queryParamSource;
    /**
     * 是否是衍生字段 0-否 1-是
     */
    private String isDeriveField;
    /**
     * 名称
     */
    private String deriveFieldName;
    /**
     * 排序
     */
    private String deriveFieldSort;
    /**
     * 格式
     */
    private String deriveFieldType;
    /**
     * 数据来源
     */
    private String deriveFieldSource;
    /**
     * 创建者
     */
    private String creator;
    /**
     * 创建时间
     */
    private String createTime;
    /**
     * 更新着
     */
    private String updater;
    /**
     * 更新时间
     */
    private String updateTime;

}
