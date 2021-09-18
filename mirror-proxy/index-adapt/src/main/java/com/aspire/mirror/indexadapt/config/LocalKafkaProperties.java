package com.aspire.mirror.indexadapt.config;

import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
* 本地机房kafka属性映射类    <br/>
* Project Name:index-proxy
* File Name:LocalKafkaProperties.java
* Package Name:com.aspire.mirror.indexadapt.config
* ClassName: LocalKafkaProperties <br/>
* date: 2018年10月15日 下午5:32:18 <br/>
* @author pengguihua
* @version 
* @since JDK 1.6
*/ 
@ConfigurationProperties(prefix = "local.kafka")
public class LocalKafkaProperties extends KafkaProperties {
	
	/**
	* 获取本地kafka配置的默认分区数. <br/>
	*
	* 作者： pengguihua
	* @return
	*/  
	public Integer getKafkaConfigPartionCount() {
		return super.getListener().getConcurrency();
	}
}
