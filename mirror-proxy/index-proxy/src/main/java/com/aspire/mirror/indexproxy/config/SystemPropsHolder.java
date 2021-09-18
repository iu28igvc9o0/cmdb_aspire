package com.aspire.mirror.indexproxy.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/** 
 *
 * 项目名称: index-proxy 
 * <p/>
 * 
 * 类名: SystemPropsHolder
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
@Configuration
public class SystemPropsHolder {
	
	@Bean
	@ConfigurationProperties(prefix = "selfMoni")
	public SelfMoniAlertLevelMapConfig selfMoniAlertLevelMapConfig() {
		return new SelfMoniAlertLevelMapConfig();
	}
	
	@Bean
	@ConfigurationProperties(prefix = "proxyIdentityConfig")
	public ProxyIdentityConfig proxyIdentityConfig() {
		return new ProxyIdentityConfig();
	}
}
