package com.aspire.mirror.inspection.server.dao.po ;

import java.io.Serializable;
import java.util.List;

import com.aspire.mirror.inspection.api.dto.ReportItemExt;
import com.aspire.mirror.inspection.api.dto.ReportItemValue;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * inspection_report_item持久对象类
 *
 * 项目名称:  微服务运维平台
 * 包:       com.aspire.mirror.inspection.server.dao.po
 * 类名称:    ReportItem.java
 * 类描述:    inspection_report_item持久类，定义与表字段对应的属性
 * 创建人:    ZhangSheng
 * 创建时间:  2018-07-27 13:48:08
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportItem implements Serializable {
	
	private static final long	serialVersionUID	= -7049549482265656355L;

	public static final String	STATUS_NORMAL		= "0";
	public static final String	STATUS_EXCEPTION	= "1";
	public static final String	STATUS_NORESULT		= "2";

    public static final String	EXEC_STATUS_TRIGGER		= "2";

    /** 序列号 */
    private Long reportItemId;

    /** 报告ID */
    private String reportId;

    /** 监控项ID */
    private String itemId;
    
    private String objectType;
    
    private String objectId;
    
    private String serverType;
    
    private String key;

    /** 时间戳（秒） */
    private Integer clock;

    /** 巡检采集值 */
    private String value;

    /** 上一次巡检采集值 */
    private String preValue;

    /** 时间戳（小于一秒的时间） */
    private Integer ns;

    /** 状态：
0-正常
1-异常
2-无结果 */
    private String status;

    private String execStatus;

    /** 日志 */
//    private String log;

    /** 预留字段 */
    private String riId;

    private String name;

    /**
     * 结果项名称
     */
    private String resultName;

    private String resultDesc;

    private ReportItemExt reportItemExt;

    private List<ReportItemValue> reportItemValueList;
} 
