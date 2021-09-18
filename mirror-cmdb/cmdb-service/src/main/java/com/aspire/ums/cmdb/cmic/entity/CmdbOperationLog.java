package com.aspire.ums.cmdb.cmic.entity;

import java.util.Date;

import lombok.Data;

@Data
public class CmdbOperationLog {

	private String id;
	/**
	 * 操作内容
	 */
	private String content;
	/**
	 * 操作菜单url
	 */
	private String menuUrl;
	/**
	 * 功能模块
	 */
	private String funModel;
	/**
	 * 操作ip
	 */
	private String remoteAddr;
	/**
	 * 操作帐号
	 */
	private String createBy;
	/**
	 * 操作时间
	 */
	private Date createDate;

}
