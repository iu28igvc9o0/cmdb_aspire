/**
 *
 * 项目名： ops-api 
 * <p/> 
 *
 * 文件名:  YumFileServerSource.java 
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
 * 项目名称: ops-api 
 * <p/>
 * 
 * 类名: YumFileServerSource
 * <p/>
 *
 * 类功能描述: yum文件远程传输服务器源
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
public class YumFileServerSource {
	private String	serverIP;
	private Integer	serverPort;
	private String	loginUser;
	private String	loginPass;
	private String 	sourceFilePath;
	private String	protocol;
}
