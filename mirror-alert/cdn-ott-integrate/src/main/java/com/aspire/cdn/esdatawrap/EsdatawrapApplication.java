package com.aspire.cdn.esdatawrap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;

/** 
 *
 * 项目名称: esdatawrap 
 * <p/>
 * 
 * 类名: EsdatawrapApplication
 * <p/>
 *
 * 类功能描述: TODO
 * <p/>
 *
 * @author	pengguihua
 *
 * @date	2020年5月6日  
 *
 * @version	V1.0 
 * <br/>
 *
 * <b>Copyright(c)</b> 2020 卓望公司-版权所有 
 *
 */
@SpringBootApplication(exclude={DruidDataSourceAutoConfigure.class, DataSourceAutoConfiguration.class})
@EnableScheduling
public class EsdatawrapApplication {

	public static void main(String[] args) {
		SpringApplication springApp = new SpringApplication(EsdatawrapApplication.class);
		springApp.setWebApplicationType(WebApplicationType.NONE);
		springApp.run(args);
	}

}
