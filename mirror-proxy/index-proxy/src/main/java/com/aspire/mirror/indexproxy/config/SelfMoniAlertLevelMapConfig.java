package com.aspire.mirror.indexproxy.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

/** 
 *
 * 项目名称: index-proxy 
 * <p/>
 * 
 * 类名: SelfMoniAlertLevelMapConfig
 * <p/>
 *
 * 类功能描述: TODO
 * <p/>
 *
 * @author	pengguihua
 *
 * @date	2020年12月9日  
 *
 * @version	V1.0 
 * <br/>
 *
 * <b>Copyright(c)</b> 2020 卓望公司-版权所有 
 *
 */
@Data
@ConfigurationProperties(prefix = "selfMoni")
public class SelfMoniAlertLevelMapConfig {
	private final Map<String, String> alertLevelMap = new HashMap<>();
}
