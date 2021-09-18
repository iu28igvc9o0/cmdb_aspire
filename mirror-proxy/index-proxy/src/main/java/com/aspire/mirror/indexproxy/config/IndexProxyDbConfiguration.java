package com.aspire.mirror.indexproxy.config;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;

@Configuration
@EnableConfigurationProperties(IndexProxyDruidProperties.class)
@ConditionalOnClass(DruidDataSource.class)
@ConditionalOnProperty(prefix = "indexProxyDb", name = "url")
@AutoConfigureBefore(DataSourceAutoConfiguration.class)
@MapperScan(basePackages = "com.aspire.mirror.indexproxy.dao", sqlSessionTemplateRef = "sqlSessionTemplate")
public class IndexProxyDbConfiguration {
    @Autowired
    private IndexProxyDruidProperties dbConfig;

    /**
     * 配置数据源
     * @return DataSource 数据源
     */
    @Bean("dataSource")
    public DataSource dataSource() {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUrl(dbConfig.getUrl());
        dataSource.setUsername(dbConfig.getUsername());
        dataSource.setPassword(dbConfig.getPassword());
        if (dbConfig.getInitialSize() > 0) {
            dataSource.setInitialSize(dbConfig.getInitialSize());
        }
        if (dbConfig.getMaxActive() > 0) {
            dataSource.setMaxActive(dbConfig.getMaxActive());
        }
        if (dbConfig.getMaxWait() > 0) {
            dataSource.setMaxWait(dbConfig.getMaxWait());
        }
        if (dbConfig.getMinIdle() > 0) {
            dataSource.setMinIdle(dbConfig.getMinIdle());
        }
        if (dbConfig.getValidationQuery() != null) {
            dataSource.setValidationQuery(dbConfig.getValidationQuery());
        }
        dataSource.setTestOnBorrow(dbConfig.isTestOnBorrow());
        if (StringUtils.isNotBlank(dbConfig.getDriverClassName())) {
        	dataSource.setDriverClassName(dbConfig.getDriverClassName().trim());
        }
        try {
            dataSource.init();
        } catch (SQLException e) {
             throw new RuntimeException("Init indexproxy database error.", e);
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
    
    @Primary
    @Bean(name = "transactionManager")
    public DataSourceTransactionManager transactionManager(
    						@Qualifier("dataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
	
	@Bean(name = "sessionFactory")
	public SqlSessionFactory sessionFactory(
			@Qualifier("dataSource") DataSource dataSource) throws Exception {
		SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
		bean.setDataSource(dataSource);
		
		PathMatchingResourcePatternResolver locationResolver = new PathMatchingResourcePatternResolver();
		List<Resource> allLocations = new ArrayList<Resource>();
		for (String location : dbConfig.getMapperLocations()) {
			Resource[] locationArr = locationResolver.getResources(location);
			if (locationArr != null && locationArr.length > 0) {
				allLocations.addAll(Arrays.asList(locationArr));
			}
		}
		bean.setMapperLocations(allLocations.toArray(new Resource[allLocations.size()]));
		return bean.getObject();
	}
	
	@Bean(name = "sqlSessionTemplate")
    public SqlSessionTemplate sqlSessionTemplate(
    		@Qualifier("sessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}
