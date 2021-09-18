package com.aspire.ums.cmdb.config;

/**
 * @Title: DruidAutoConfiguration.java
 * @Package com.migu.tsg.microservice.atomicservice.order.config
 * @Description: DruidAuto configuration
 * Copyright: Copyright (c) 2017
 * Company:咪咕文化 tsg
 * @author tsg-frank
 * @date 2017年5月23日 下午3:32:38
 * @version V1.0
 */

import java.sql.SQLException;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.WebStatFilter;

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
     * 
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
        if (properties.getRemoveAbandonedTimeout() > 0) {
            dataSource.setRemoveAbandonedTimeout(properties.getRemoveAbandonedTimeout());
        }
        dataSource.setRemoveAbandoned(properties.isRemoveAbandoned());
        dataSource.setLogAbandoned(properties.isLogAbandoned());
        dataSource.setTestOnBorrow(properties.isTestOnBorrow());
        dataSource.setPoolPreparedStatements(properties.isPoolPreparedStatements());
        if (properties.getMaxPoolPreparedStatementPerConnectionSize() > 0) {
            dataSource.setMaxPoolPreparedStatementPerConnectionSize(properties.getMaxPoolPreparedStatementPerConnectionSize());
        }
        try {
            dataSource.setFilters("stat,log4j");
            if (StringUtils.isNotBlank(properties.getConnectionProperties())) {
                Properties properties1 = new Properties();
                properties1.setProperty("connectionProperties", properties.getConnectionProperties());
                dataSource.setConnectProperties(properties1);
            }
            dataSource.init();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return dataSource;
    }

    /**
     * 配置服务过滤器
     *
     * @return 返回过滤器配置对象
     */
    @Bean
    public FilterRegistrationBean statFilter() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean(new WebStatFilter());
        // 添加过滤规则
        filterRegistrationBean.addUrlPatterns("/*");
        // 忽略过滤格式
        filterRegistrationBean.addInitParameter("exclusions", "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*,");
        return filterRegistrationBean;
    }
}
