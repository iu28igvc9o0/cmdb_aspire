package com.aspire.mirror.composite.service.inspection.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * TODO
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.composite.service.inspection.payload
 * 类名称:    CompInspectionDetail.java
 * 类描述:    TODO
 * 创建人:    JinSu
 * 创建时间:  2018/8/14 13:54
 * 版本:      v1.0
 */
@Data
@NoArgsConstructor
@ToString
public class CompInspectionDetail implements Serializable {

    private static final long serialVersionUID = 523021533312399384L;
    /**
     * 名称
     */
//    @Excel(name="名称")
    private String name;

    /**
     * ip
     */
//    @Excel(name="IP")
    private String ip;

    /**
     * 设备大类
     */
//    @Excel(name="设备大类")
    @JsonProperty("device_class")
    private String deviceClass;

    /**
     * 设备小类
     */
//    @Excel(name="设备小类")
    @JsonProperty("device_type")
    private String deviceType;

    @JsonProperty("device_name")
    private String deviceName;

    /**
     * 访问时间
     */
//    @Excel(name="访问时间")
    @JsonProperty("visit_status")
    private String visitStatus;

    /**
     * 指标名称
     */
//    @Excel(name="指标名称")
    @JsonProperty("item_name")
    private String itemName;

    /**
     * 单位描述
     */
//    @Excel(name="单位描述")
    private String unit;

    /**
     * 表达式
     */
//    @Excel(name="表达式")
    private String expression;

    /**
     * 巡检值
     */
//    @Excel(name="巡检值")
    private String value;

    /**
     * 上次巡检值
     */
//    @Excel(name="上次巡检值")
    @JsonProperty("pre_value")
    private String preValue;

    /**
     * 状态
     */
    private String status;

    @JsonProperty("status_label")
    private String statusLabel;

    /** 监控项ID */
    @JsonProperty("item_id")
    private String itemId ;

    /** 设备ID */
    @JsonProperty("device_id")
    private String deviceId ;

    @JsonProperty("object_id")
    private String objectId;

    @JsonProperty("object_type")
    private String objectType;

    @JsonProperty("idc_type")
    private String idcType;

    @JsonProperty("biz_system")
    private String bizSystem;

    private String log ;

    @JsonProperty("result_name")
    private String resultName;

    @JsonProperty("result_desc")
    private String resultDesc;

    @JsonProperty("biz_group")
    private String bizGroup;

    @JsonProperty("item_group")
    private String itemGroup;

}
