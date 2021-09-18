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
package com.aspire.mirror.threshold.server.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


@Data
@Component
@ConfigurationProperties(prefix="dh.ftp")
public class DHFtpServerConfig {
	private String host;
	private int port;
	private String username;
	private String password;
}
