package com.aspire.mirror.inspection.api.dto ;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * inspection_report_item新增对象类
 *
 * 项目名称: 微服务运维平台
 * 包:      com.aspire.mirror.inspection.api.dto
 * 类名称:   ReportItemCreateRequest.java
 * 类描述:   inspection_report_item创建请求对象
 * 创建人:   ZhangSheng
 * 创建时间: 2018-07-27 13:48:08
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportItemCreateRequest implements Serializable {
	
	private static final long serialVersionUID = -7663338744063544645L;

    /** 序列号 */
	@JsonProperty("report_item_id")
    private String reportItemId;

    /** 报告ID */
	@JsonProperty("report_id")
    private String reportId;

    /** 监控项ID */
	@JsonProperty("item_id")
    private String itemId;

    /** 监控项key */
    private String key;


    @JsonProperty("object_type")
    private String objectType;

    @JsonProperty("object_id")
    private String objectId;

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
//    /** 脚本专属字段 */
//    @JsonProperty("exec_status")
//    private String execStatus;
//    /** 日志 */
//    private String log;

    /** 预留字段 */
    @JsonProperty("ri_id")
    private String riId;

    @JsonProperty("server_type")
    private String serverType;

    @JsonProperty("report_item_ext")
    private ReportItemExt reportItemExt;
} 
