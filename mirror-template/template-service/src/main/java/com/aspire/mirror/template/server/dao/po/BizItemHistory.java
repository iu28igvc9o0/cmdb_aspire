package com.aspire.mirror.template.server.dao.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 包名:     com.aspire.webbas.announce.entity   
 * 类名:     BizItemHistory
 * 描述:     数据模型
 * 作者:     金素
 * 时间:     2018-11-15 16:47:38
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BizItemHistory implements Serializable {
	private static final long serialVersionUID = -2580796504585621419L;
	/**  */
	private String id;
	
	/** 监控项ID */
	private String itemId;
	
	/** 统计结果 */
	private String logValue;
	
	/** 创建时间 */
	private Date createTime;
	
	/** 统计开始时间 */
	private Date logStartTime;
	
	/** 主题ID */
	private String themeId;
	
	/** cron表达式 */
	private String cron;
	
}
