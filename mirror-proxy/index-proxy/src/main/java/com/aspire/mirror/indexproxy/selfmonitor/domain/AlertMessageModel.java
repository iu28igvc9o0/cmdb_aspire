package com.aspire.mirror.indexproxy.selfmonitor.domain;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/** 
 *
 * 项目名称: index-proxy 
 * <p/>
 * 
 * 类名: AlertMessageModel
 * <p/>
 *
 * 类功能描述: TODO
 * <p/>
 *
 * @author	pengguihua
 *
 * @date	2020年11月10日  
 *
 * @version	V1.0 
 * <br/>
 *
 * <b>Copyright(c)</b> 2020 卓望公司-版权所有 
 *
 */
@Data
public class AlertMessageModel {
	public static final String MONI_RESULT_ACTIVE = "1";	// 告警
	public static final String MONI_RESULT_REVOKE = "2";	// 消警
	private Integer	indexNum;
	private String	moniResult;
	private String	deviceIP;
	private String	monitorObject;
	private String	monitorIndex;
	private String	moniIndexValue;
	private String	alertLevel;
	@JsonFormat(timezone = "GMT+8", pattern = "yyyy.MM.dd HH:mm:ss")
	private Date	curMoniTime;
	private String	curMoniValue;
	private String	businessSystem;
	private String	subject;
	private String	source;
	@JsonFormat(timezone = "GMT+8", pattern = "yyyy.MM.dd HH:mm:ss")
	private Date	alertStartTime;
	private String	itemKey;
	private String	keyComment;
	@JsonProperty("alert_id")
	private String	alertId;
	@JsonProperty("z_itemId")
	private Long	zItemId;
}
