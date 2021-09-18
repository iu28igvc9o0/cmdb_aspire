package com.aspire.mirror.indexproxy.selfmonitor.zabbix.domain;

import lombok.Data;

/** 
 *
 * 项目名称: index-proxy 
 * <p/>
 * 
 * 类名: ZbxItemHistoryRecord
 * <p/>
 *
 * 类功能描述: TODO
 * <p/>
 *
 * @author	pengguihua
 *
 * @date	2020年10月30日  
 *
 * @version	V1.0 
 * <br/>
 *
 * <b>Copyright(c)</b> 2020 卓望公司-版权所有 
 *
 */
@Data
public class ZbxItemHistoryRecord {
	private Long	itemId;
	private String	itemKey;
	private Integer	clock;
	private Float	value;
	private Long	sequenceNo;
}
