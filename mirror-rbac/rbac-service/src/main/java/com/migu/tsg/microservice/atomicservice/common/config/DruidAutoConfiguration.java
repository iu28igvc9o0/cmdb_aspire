package com.migu.tsg.microservice.atomicservice.common.config;

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
import com.migu.tsg.microservice.atomicservice.common.exception.CustomizedException;

/**
 * 项目名称: rbac-service <br>
 * 包: com.migu.tsg.microservice.atomicservice.common.config <br>
 * 类名称: DruidAutoConfiguration.java <br>
 * 类描述: 数据库连接池 <br>
 * 创建人: WangSheng <br>
 * 创建时间: 2017年8月22日上午10:08:34 <br>
 * 版本: v1.0
 */
@Configuration
@EnableConfigurationProperties(DruidProperties.class)
@ConditionalOnClass(DruidDataSource.class)
@ConditionalOnProperty(prefix = "druid", name = "url")
@AutoConfigureBefore(DataSourceAutoConfiguration.class)
// @MapperScan(basePackages =
// "com.migu.tsg.microservice.atomicservice.rbac.dao")
public class DruidAutoConfiguration {

    @Autowired
    private DruidProperties properties;

    /**
     * 配置数据源
     * @return DataSource 数据源
     */
    @Bean
    public DataSource dataSource() {
        DruidDataSource dataSource = new DruidDataSource();
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
            throw new CustomizedException(e);
        }
        return dataSource;
    }

    /**
     * 注册Servlet
     * @return ServletRegistrationBean Servlet注册Bean对象
     */
    @Bean
    public ServletRegistrationBean druidServlet() {
        StatViewServlet druidServlet = new StatViewServlet();
        ServletRegistrationBean druidServletRegistration = new ServletRegistrationBean(druidServlet);
        druidServletRegistration.addInitParameter("allow", "127.0.0.1");
        druidServletRegistration.addUrlMappings("/druid/*");
        return druidServletRegistration;
    }

}
