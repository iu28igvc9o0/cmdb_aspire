/**
 *
 * 项目名： ops-service 
 * <p/> 
 *
 * 文件名:  CommonSftpServerConfig.java 
 * <p/>
 *
 * 功能描述: TODO 
 * <p/>
 *
 * @author	pengguihua
 *
 * @date	2019年10月30日 
 *
 * @version	V1.0
 * <p/>
 *
 *<b>Copyright(c)</b> 2019 卓望公司-版权所有<br/>
 *   
 */
package com.migu.tsg.microservice.atomicservice.composite.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/** 
 *
 * 项目名称: ops-service 
 * <p/>
 * 
 * 类名: CommonSftpServerConfig
 * <p/>
 *
 * 类功能描述: TODO
 * <p/>
 *
 * @author	pengguihua
 *
 * @date	2019年10月30日  
 *
 * @version	V1.0 
 * <br/>
 *
 * <b>Copyright(c)</b> 2019 卓望公司-版权所有 
 *
 */
@Data
@Component
@ConfigurationProperties(prefix="vul.ftp")
public class VulftpServerConfig {

	private String host;
	private int port;
	private String username;
	private String password;

}
