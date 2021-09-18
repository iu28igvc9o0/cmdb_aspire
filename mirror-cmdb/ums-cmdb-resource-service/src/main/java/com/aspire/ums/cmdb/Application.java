package com.aspire.ums.cmdb;

import javax.sql.DataSource;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author lupeng
 * @EnableTransactionManagement 启注解事务管理
 * @EnableDiscoveryClient 让服务发现服务器,使用服务器.Spring cloud 实现服务发现
 *
 */
@EnableTransactionManagement 
@EnableDiscoveryClient
@SpringBootApplication
@MapperScan("com.aspire.ums.cmdb.*.mapper")
public class Application {
	
    /**
     * dataSource 框架自动注入
     * @param dataSource
     * @return
     */
    @Bean
    public PlatformTransactionManager txManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
    
	public static void main(String[] args) {
		new SpringApplicationBuilder(Application.class).web(true).run(args);
	}

}
