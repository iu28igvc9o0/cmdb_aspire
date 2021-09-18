package com.aspire.mirror.inspection.api.dto.vo ;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

//import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * inspection_report_item视图层对象
 *
 * 项目名称: 微服务运维平台
 * 包:      com.aspire.mirror.inspection.api.dto.vo
 * 类名称:   ReportItemVO.java
 * 类描述:   inspection_report_item视图层属性，属性范围>=表结构属性.
 * 创建人:   ZhangSheng
 * 创建时间: 2018-07-27 13:48:08
 */
@Data
//@AllArgsConstructor
@NoArgsConstructor
public class ReportItemVO implements Serializable {
	
	private static final long serialVersionUID = -8314058223505531748L;

    /** 序列号 */
	@JsonProperty("report_item_id")
    private String reportItemId;

    /** 报告ID */
	@JsonProperty("report_id")
    private String reportId;

    /** 监控项ID */
	@JsonProperty("item_id")
    private String itemId;

    /** 设备ID */
	@JsonProperty("device_id")
    private String deviceId;

    /** 时间戳（秒） */
    private Integer clock;

    /** 巡检采集值 */
    private String value;

    /** 上一次巡检采集值 */
    @JsonProperty("pre_value")
    private String preValue;

    /** 时间戳（小于一秒的时间） */
    private Integer ns;

    /** 状态：0-正常 1-异常 2-无结果 */
    private String status;

    /** 日志 */
    private String log;

    /** 预留字段 */
    @JsonProperty("ri_id")
    private String riId;

} 
