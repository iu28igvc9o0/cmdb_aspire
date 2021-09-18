package com.aspire.cdn.esdatawrap.biz.report.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

/** 
 *
 * 项目名称: cdn-b2b-integrate 
 * <p/>
 * 
 * 类名: AlertStatisticReportItem
 * <p/>
 *
 * 类功能描述: 告警统计相关报表对象; 注意, 告警统计的几个报表， 为了简单，都使用本对象，但本对象中的属性，在不同的报表可能不需要，根据业务填充即可；
 * <p/>
 *
 * @author	pengguihua
 *
 * @date	2020年8月27日  
 *
 * @version	V1.0 
 * <br/>
 *
 * <b>Copyright(c)</b> 2020 卓望公司-版权所有 
 *
 */
@Data
@JsonInclude(Include.NON_EMPTY)
public class AlertStatisticReportItem {
	private String	provinceName;		// 省份
	private String	businessSource;		// 来源业务系统 B2B | OTT
	private String	manufacture;		// 平面
	private String	alertTitle;			// 告警主题
	private String	reqDomain;			// 域名
	private String	cpName;				// cpName
	private Integer	occurCount;			// 告警统计次数
}
