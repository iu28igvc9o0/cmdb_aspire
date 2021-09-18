package com.aspire.mirror.eureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
* 项目名称: eureka-server
* 包: com.aspire.mirror.eureka
* 类名称: EurekaApplication.java
* 类描述: 注册中心
* 创建人: zhouli
* 创建时间: 2018年7月26日下午5:58:40
* 版本: v1.0
* SpringBootApplication 启动
*/
@SpringBootApplication
@EnableEurekaServer
public class EurekaApplication {
	/**
	 * @param args 参数
	 */
	public static void main(final String[] args) {
		SpringApplication.run(EurekaApplication.class, args);
	}
}
