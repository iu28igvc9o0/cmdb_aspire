package com.aspire.mirror.inspection.api.dto ;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang.StringUtils;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * inspection_report_item详情对象类
 *
 * 项目名称:  微服务运维平台
 * 包:       com.aspire.mirror.inspection.api.dto
 * 类名称:    ReportItemDetailResponse.java
 * 类描述:    inspection_report_item创建响应对象
 * 创建人:    ZhangSheng
 * 创建时间:  2018-07-27 13:48:08
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportItemDetailResponse implements Serializable {
	
	private static final long serialVersionUID = -7779729223636124416L;

    /** 序列号 */
	@JsonProperty("report_item_id")
    private String reportItemId ;

    /** 报告ID */
	@JsonProperty("report_id")
    private String reportId ;

    /** 监控项ID */
	@JsonProperty("item_id")
    private String itemId ;

//    /** 设备ID */
//	@JsonProperty("device_id")
//    private String deviceId ;
	
	@JsonProperty("object_type")
	private String objectType;
	
	@JsonProperty("object_id")
	private String objectId;

    /** 时间戳（秒） */
    private Integer clock ;

    /** 巡检采集值 */
    private String value ;

    /** 上一次巡检采集值 */
    @JsonProperty("pre_value")
    private String preValue ;

    /** 时间戳（小于一秒的时间） */
    private Integer ns ;

    /** 状态：0-正常 1-异常  2-无结果 */
    private String status ;

    @JsonProperty("exec_status")
    private String execStatus;
    
	@JsonProperty("status_label")
	private String statusLabel;

    /** 日志 */
    private String log ;

    /** 预留字段 */
    @JsonProperty("ri_id")
    private String riId ;

    private String name;

    @JsonProperty("device_name")
    private String deviceName;

    @JsonProperty("biz_system")
    private String bizSystem;

    @JsonProperty("idc_type")
    private String idcType;

    private String ip;

    public String getStatusLabel() {
    	if (StringUtils.isBlank(status)) {
    		return "";
    	}
    	if ("0".equals(status)) {
    		return "正常";
    	}
    	if ("1".equals(status)) {
    		return "异常";
    	}
    	if ("2".equals(status)) {
    		return "无结果";
    	}
        if ("3".equals(status)) {
            return "人工判断";
        }
    	return "";
    }

    @JsonProperty("report_item_value_list")
    private List<ReportItemValue> reportItemValueList;

    /**
     * 结果项名称
     */
    @JsonProperty("result_name")
    private String resultName;

    /**
     * 结果描述
     */
    @JsonProperty("result_desc")
    private String resultDesc;

    @JsonProperty("report_item_ext")
    private ReportItemExt reportItemExt;
}
