package com.aspire.mirror.httpMonitor;

import javax.sql.DataSource;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author lupeng
 * @EnableTransactionManagement 启注解事务管理
 * @EnableDiscoveryClient 让服务发现服务器,使用服务器.Spring cloud 实现服务发现
 * @SpringBootApplication 启动入口
 */
@EnableScheduling
@EnableTransactionManagement(proxyTargetClass = true)
@SpringBootApplication
@MapperScan(basePackages = {"com.aspire.mirror.httpMonitor.dao"})
public class HttpProxyApplication {
	
	@Bean
    public PlatformTransactionManager txManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
	
	public static void main(String[] args) {
		new SpringApplicationBuilder(HttpProxyApplication.class).web(true).run(args);
	}

}
