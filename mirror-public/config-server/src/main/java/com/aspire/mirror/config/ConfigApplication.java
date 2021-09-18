package com.aspire.mirror.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.config.server.EnableConfigServer;


/**
* 项目名称: config-server
* 包:  com.aspire.mirror.config
* 类名称: ConfigApplication.java
* 类描述:声明一个Config Server
* 创建人: JinSu
* 创建时间: 2018年7月26日下午6:05:11
* 版本: v1.0
*/
@EnableConfigServer
@EnableDiscoveryClient
@SpringBootApplication(exclude={DataSourceAutoConfiguration.class,HibernateJpaAutoConfiguration.class})
public class ConfigApplication {
	/**
	 * @param args main方法
	 */
	public static void main(final String[] args) {
		SpringApplication.run(ConfigApplication.class, args);
	}
}
