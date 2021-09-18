package com.aspire.mirror.inspection.server.clientservice.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * monitor_items详情对象类
 * <p>
 * 项目名称:  mirror平台
 * 包:       com.aspire.mirror.template.api.dto
 * 类名称:    ItemsDetailResponse.java
 * 类描述:    monitor_items创建响应对象
 * 创建人:    JinSu
 * 创建时间:  2018-07-27 13:48:08
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemsDetailResponse implements Serializable {

    private static final long serialVersionUID = -6367448910493063682L;

    /**
     * 监控项ID
     */
    @JsonProperty("item_id")
    private String itemId;

    /**
     * 监控项名称
     */
    private String name;

    /**
     * 监控项类型
     * SCRIPT-脚本（现有）
     * INDEX-监控指标（现有）
    private String type;
     */

    /**
     * 模版ID
     */
    @JsonProperty("template_id")
    private String templateId;

    /**
     * 监控键值
     */
    private String key;

    /**
     * 监控周期，单位分钟
     */
    private String delay;

    /**
     * 保留时间，单位天
     */
    private Integer history;

    /**
     * 状态：
     * ON-启用
     * OFF-禁用
     * NONSUPPORT-不支持（启用状态的监控项采集不到数据）（暂时不用）
     */
    private String status;

    /**
     * 监控项数据格式：
     * FLOAT-浮点数
     * STR-字符串
     * LOG-日志
     * UINT64-整数
     * TEXT-文本
     */
    @JsonProperty("value_type")
    private String valueType;

    /**
     * 单位
     */
    private String units;

    /**
     * 错误信息
     */
    private String error;

    /**
     * 监控项数据类型
     * DECIMAL-十进制
     * OCTAL-八进制
     * HEXADECIMAL-十六进制
     * BOOLEAN-布尔值
     */
    @JsonProperty("data_type")
    private String dataType;

    /**
     * 接入监控系统类型（目前为zabbix）
     * MIRROR
     * ZABBIX
     * NONE
     */
    @JsonProperty("sys_type")
    private String sysType;

    /**
     * 模板名称
     */
    @JsonProperty("template_name")
    private String templateName;

    @JsonProperty("item_group")
    private String itemGroup;

    @JsonProperty("biz_group")
    private String bizGroup;

    @JsonProperty("script_param")
    private String scriptParam;

    @JsonProperty("customize_param")
    private String customizeParam;
}
