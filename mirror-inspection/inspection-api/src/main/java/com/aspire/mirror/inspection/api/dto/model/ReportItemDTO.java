package com.aspire.mirror.inspection.api.dto.model ;

import java.io.Serializable;
import java.util.List;

import com.aspire.mirror.inspection.api.dto.ReportItemExt;
import com.aspire.mirror.inspection.api.dto.ReportItemValue;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * inspection_report_item持久对象类
 *
 * 项目名称:  微服务运维平台
 * 包:       com.aspire.mirror.inspection.api.dto.model
 * 类名称:    ReportItemDTO.java
 * 类描述:    inspection_report_item业务类，定义与表字段对应的属性
 * 创建人:    ZhangSheng
 * 创建时间:  2018-07-27 13:48:08
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel
public class ReportItemDTO implements Serializable {
	
	private static final long serialVersionUID = -8860902635934746435L;
	
	public static final String	STATUS_NORMAL		= "0";
	public static final String	STATUS_EXCEPTION	= "1";
    public static final String	STATUS_NORESULT		= "2";
	public static final String	STATUS_ARTIFICIAL_JUDGMENT = "3";

    /** 序列号 */
    @ApiModelProperty(value = "序列号")
    private Long reportItemId;

    /** 报告ID */
    @ApiModelProperty(value = "报告ID")
    private String reportId;

    /** 监控项ID */
    @ApiModelProperty(value = "监控项ID")
    private String itemId;
    
    @ApiModelProperty("监控项key")
    private String key;
    
    @ApiModelProperty("监控项所归属服务类型")
    private String serverType;

    @ApiModelProperty(value = "所属对象Type")
    private String objectType;
    
    @ApiModelProperty(value = "所述对象ID")
    private String objectId;
    
    /** 时间戳（秒） */
    @ApiModelProperty(value = "时间戳（秒）")
    private Integer clock;

    /** 巡检采集值 */
    @ApiModelProperty(value = "巡检采集值")
    private String value;

    /** 上一次巡检采集值 */
    @ApiModelProperty(value = "上一次巡检采集值")
    private String preValue;

    /** 时间戳（小于一秒的时间） */
    @ApiModelProperty(value = "时间戳（小于一秒的时间）")
    private Integer ns;

    /** 状态：
0-正常
1-异常
2-无结果 */
    @ApiModelProperty(value = "状态：0-正常   1-异常   2-无结果 3-人工判断")
    private String status;

//    /** 脚本专属字段 */
//    @ApiModelProperty(value = "脚本执行状态 1成功 0失败")
//    private String execStatus;
//
//    /** 日志 */
//    @ApiModelProperty(value = "日志")
//    private String log;

    /** 预留字段 */
    @ApiModelProperty(value = "预留字段")
    private String riId;

    @ApiModelProperty(value = "监控项名称")
    private String name;

    @ApiModelProperty(value = "报告元素扩展")
    private ReportItemExt reportItemExt;

    @ApiModelProperty(value = "报告元素值")
    private List<ReportItemValue> reportItemValueList;

    private String resultName;

    private String resultDesc;

    private String scriptParam;

    private String customizeParam;

} 
