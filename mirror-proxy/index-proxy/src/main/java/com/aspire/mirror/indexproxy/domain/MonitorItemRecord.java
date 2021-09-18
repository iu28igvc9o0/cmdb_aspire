package com.aspire.mirror.indexproxy.domain;

import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

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
@EqualsAndHashCode(of={"itemId"}, callSuper=false)
public class MonitorItemRecord extends BasicDataOperateAware implements Serializable {
    private static final long serialVersionUID 	= -6367448910493063682L;
    public static final String VALUE_TYPE_FLOAT = "0";
    public static final String VALUE_TYPE_UINT 	= "3";

    /**
     * 监控项ID
     */
    @JsonProperty("item_id")
    private String itemId;

    @JsonProperty("zabbix_item_id")
    private String zabbixItemId;

    /**
     * 监控项名称
     */
    private String name;

    /**
     * 监控项类型
     * SCRIPT-脚本（现有）
     * INDEX-监控指标（现有）
     */
    @Deprecated
    private String type;

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
     * 监控周期，单位分钟监 zabbix同步上来的指标该字段与zabbix一致
     */
    private String delay;

    /**
     * 状态：
     * ON-启用
     * OFF-禁用
     * NONSUPPORT-不支持（启用状态的监控项采集不到数据）（暂时不用）
     */
    private String status;

    /**
     * 监控项数据格式：
     * 0 - numeric float; 
	 * 1 - character; 
	 * 2 - log; 
	 * 3 - numeric unsigned; 
	 * 4 - text.
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
    
    public String getUnits() {
    	return StringUtils.isBlank(units) ? "" : units;
    }
} 
