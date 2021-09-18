/**
 *
 * 项目名： ops-service 
 * <p/> 
 *
 * 文件名:  OsDistributionModel.java 
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

import lombok.Data;

/** 
 *
 * 项目名称: ops-service 
 * <p/>
 * 
 * 类名: OsDistributionModel
 * <p/>
 *
 * 类功能描述: 操作系统发行版本对象
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
public class OsDistributionModel {
	private Integer	id;
	private String	osType;		// linux | windows
	private String	osVendor;	// centos | redhat | fedora
	private String	version;	// 版本
}
