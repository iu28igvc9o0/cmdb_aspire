package com.migu.tsg.microservice.atomicservice.composite.config;

/**
 * @Title: DruidAutoConfiguration.java
 * @Package com.migu.tsg.microservice.atomicservice.order.config
 * @Description: DruidAuto configuration
 * Copyright: Copyright (c) 2017
 * Company:咪咕文化 tsg
 * 
 * @author tsg-frank
 * @date 2017年5月23日 下午3:32:38
 * @version V1.0
 */
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;

/**
 * Created by chu on 2017/3/2.
 */
@Configuration
@EnableConfigurationProperties(DruidProperties.class)
@ConditionalOnClass(DruidDataSource.class)
@ConditionalOnProperty(prefix = "druid", name = "url")
@AutoConfigureBefore(DataSourceAutoConfiguration.class)
public class DruidAutoConfiguration {

	@Autowired
	private DruidProperties properties;

	
	/**
	* dataSource:(数据源). <br/>
	*
	* 作者： yangshilei
	* @return
	*/   
	@Bean
	public DataSource dataSource() {
		final DruidDataSource dataSource = new DruidDataSource();
		dataSource.setUrl(properties.getUrl());
		dataSource.setUsername(properties.getUsername());
		dataSource.setPassword(properties.getPassword());
		if (properties.getInitialSize() > 0) {
			dataSource.setInitialSize(properties.getInitialSize());
		}
		if (properties.getMaxActive() > 0) {
			dataSource.setMaxActive(properties.getMaxActive());
		}
		if (properties.getMaxWait() > 0) {
			dataSource.setMaxWait(properties.getMaxWait());
		}
		if (properties.getMinIdle() > 0) {
			dataSource.setMinIdle(properties.getMinIdle());
		}
		if (properties.getValidationQuery() != null) {
			dataSource.setValidationQuery(properties.getValidationQuery());
		}
		dataSource.setTestOnBorrow(properties.isTestOnBorrow());

		try {
			dataSource.init();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return dataSource;
	}

	
	/**
	* druidServlet:(这里用一句话描述这个方法的作用). <br/>
	*
	* 作者： yangshilei
	* @return
	*/
	@Bean
	public ServletRegistrationBean druidServlet() {
		final StatViewServlet druidServlet = new StatViewServlet();
		final ServletRegistrationBean druidServletRegistration = new ServletRegistrationBean(
				druidServlet);
		druidServletRegistration.addInitParameter("allow", "127.0.0.1");
		druidServletRegistration.addUrlMappings("/druid/*");
		return druidServletRegistration;
	}
}