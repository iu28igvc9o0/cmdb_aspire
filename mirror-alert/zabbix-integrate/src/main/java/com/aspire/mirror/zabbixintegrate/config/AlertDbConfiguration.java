package com.aspire.mirror.zabbixintegrate.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import org.apache.ibatis.mapping.DatabaseIdProvider;
import org.apache.ibatis.mapping.VendorDatabaseIdProvider;
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
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

@Configuration
@EnableConfigurationProperties(AlertDruidProperties.class)
@ConditionalOnClass(DruidDataSource.class)
@ConditionalOnProperty(prefix = "alertDruid", name = "url")
@AutoConfigureBefore(DataSourceAutoConfiguration.class)
@MapperScan(basePackages = "com.aspire.mirror.zabbixintegrate.daoAlert", sqlSessionFactoryRef = "alertSqlSessionFactory", sqlSessionTemplateRef = "alertSqlSessionTemplate")
public class AlertDbConfiguration {
    @Autowired
    private AlertDruidProperties dbConfig;

    /**
     * 配置数据源
     * @return DataSource 数据源
     */
    @Bean(name = "alertDataSource")
    public DataSource cmdbDataSource() {
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

        try {
            dataSource.init();
        } catch (SQLException e) {
             throw new RuntimeException("Init zabbix integrate database error.", e);
        }
        return dataSource;
    }

    /**
     * 注册Servlet
     * @return ServletRegistrationBean Servlet注册Bean对象
     */
    @Bean(name = "alertDruidServlet")
    public ServletRegistrationBean cmdbDruidServlet() {
        StatViewServlet druidServlet = new StatViewServlet();
        ServletRegistrationBean druidServletRegistration = new ServletRegistrationBean(druidServlet);
        druidServletRegistration.addInitParameter("allow", "127.0.0.1");
        druidServletRegistration.addUrlMappings("/alertDruid/*");
        return druidServletRegistration;
    }
    
    @Bean(name = "alertTransactionManager")
    public DataSourceTransactionManager cmdbTransactionManager(@Qualifier("alertDataSource") DataSource cmdbDataSource) {
        return new DataSourceTransactionManager(cmdbDataSource);
    }
    
    /**
     * 自动识别使用的数据库类型 
     * 在mapper.xml中databaseId的值就是跟这里对应
     */
    @Bean(name = "alertDatabaseIdProvider")
    public DatabaseIdProvider cmdbDatabaseIdProvider() {
    	DatabaseIdProvider databaseIdProvider = new VendorDatabaseIdProvider();
    	Properties properties = new Properties();
    	properties.setProperty("Oracle", "oracle");
    	properties.setProperty("MySQL", "mysql");
    	databaseIdProvider.setProperties(properties);
    	return databaseIdProvider;
    }
	
	@Bean(name = "alertSqlSessionFactory")
	public SqlSessionFactory cmdbSqlSessionFactory(@Qualifier("alertDataSource") DataSource cmdbDataSource) throws Exception {
		SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
		bean.setDataSource(cmdbDataSource);
		
		PathMatchingResourcePatternResolver locationResolver = new PathMatchingResourcePatternResolver();
		List<Resource> allLocations = new ArrayList<Resource>();
		for (String location : dbConfig.getMapperLocations()) {
			Resource[] locationArr = locationResolver.getResources(location);
			if (locationArr != null && locationArr.length > 0) {
				allLocations.addAll(Arrays.asList(locationArr));
			}
		}
		bean.setMapperLocations(allLocations.toArray(new Resource[allLocations.size()]));
		bean.setDatabaseIdProvider(cmdbDatabaseIdProvider());
		return bean.getObject();
	}
	
	@Bean(name = "alertSqlSessionTemplate")
    public SqlSessionTemplate cmdbSqlSessionTemplate(@Qualifier("alertSqlSessionFactory")SqlSessionFactory cmdbSqlSessionFactory) throws Exception {
        return new SqlSessionTemplate(cmdbSqlSessionFactory);
    }
}
