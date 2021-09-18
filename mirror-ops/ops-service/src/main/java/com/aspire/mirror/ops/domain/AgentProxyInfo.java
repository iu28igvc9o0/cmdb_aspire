/**
 *
 * 项目名： ops-service 
 * <p/> 
 *
 * 文件名:  AgentProxyInfo.java 
 * <p/>
 *
 * 功能描述: TODO 
 * <p/>
 *
 * @author	pengguihua
 *
 * @date	2019年10月21日 
 *
 * @version	V1.0
 * <p/>
 *
 *<b>Copyright(c)</b> 2019 卓望公司-版权所有<br/>
 *   
 */
package com.aspire.mirror.ops.domain;

import java.util.Date;

import lombok.Data;

/** 
 *
 * 项目名称: ops-service 
 * <p/>
 * 
 * 类名: AgentProxyInfo
 * <p/>
 *
 * 类功能描述: agent proxy 
 * <p/>
 *
 * @author	pengguihua
 *
 * @date	2019年10月21日  
 *
 * @version	V1.0 
 * <br/>
 *
 * <b>Copyright(c)</b> 2019 卓望公司-版权所有 
 *
 */
@Data
public class AgentProxyInfo {
	private Long	id;				// id
	private String	pool;			// 资源池
	private String	proxyIdentity;	// 标识name
	private String	description;
	private Date	updateTime;		
}
