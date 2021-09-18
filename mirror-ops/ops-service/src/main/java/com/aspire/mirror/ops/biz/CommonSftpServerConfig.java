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
package com.aspire.mirror.ops.biz;

import com.aspire.mirror.ops.util.SshUtil;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

import java.nio.file.Path;

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
@ConfigurationProperties(prefix="ops.sftp-file-server")
public class CommonSftpServerConfig {
	private String	ipAddress;
	private Integer	port;
	private String	loginUser;
	private String	loginPass;
	private String 	rootDirectory;	// 起始根目录
	
	public String getRootDirectory() {
		return rootDirectory == null ? "/" : rootDirectory + "/";
	}


}
