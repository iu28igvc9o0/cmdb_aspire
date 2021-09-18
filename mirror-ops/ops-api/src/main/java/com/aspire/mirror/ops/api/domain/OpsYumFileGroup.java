/**
 *
 * 项目名： ops-api 
 * <p/> 
 *
 * 文件名:  YumSourceGroup.java 
 * <p/>
 *
 * 功能描述: TODO 
 * <p/>
 *
 * @author	pengguihua
 *
 * @date	2019年11月22日 
 *
 * @version	V1.0
 * <p/>
 *
 *<b>Copyright(c)</b> 2019 卓望公司-版权所有<br/>
 *   
 */
package com.aspire.mirror.ops.api.domain;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

/** 
 *
 * 项目名称: ops-api 
 * <p/>
 * 
 * 类名: OpsYumFileGroup
 * <p/>
 *
 * 类功能描述: yum文件分组
 * <p/>
 *
 * @author	pengguihua
 *
 * @date	2019年11月22日  
 *
 * @version	V1.0 
 * <br/>
 *
 * <b>Copyright(c)</b> 2019 卓望公司-版权所有 
 *
 */
@Data
public class OpsYumFileGroup extends OpsGroupObject{
	public static final Long	ROOT_GROUP_ID	= -1L;
	public static final int		FIRST_LEVEL		= 1;
	
	protected Long				groupId;
	protected String			groupName;
	protected Long				parentGroupId;
	protected int				level;
	protected String			description;
	protected String			creater;
	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	// @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	protected Date				createTime;
	protected String			lastUpdater;
	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	// @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	protected Date				lastUpdateTime;
}
