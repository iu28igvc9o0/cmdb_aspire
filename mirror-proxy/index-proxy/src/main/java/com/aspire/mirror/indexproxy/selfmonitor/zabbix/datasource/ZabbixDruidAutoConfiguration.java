package com.aspire.mirror.indexproxy.selfmonitor.zabbix.datasource;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import com.alibaba.druid.pool.DruidDataSource;
import com.aspire.mirror.indexproxy.config.ProxyIdentityConfig;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@ConditionalOnClass(DruidDataSource.class)
@AutoConfigureBefore(DataSourceAutoConfiguration.class)
@MapperScan(basePackages = "com.aspire.mirror.indexproxy.selfmonitor.zabbix.dao", 
			sqlSessionTemplateRef = "zabbixDb_SqlSessionTemplate")
public class ZabbixDruidAutoConfiguration {
	@Autowired
	private ProxyIdentityConfig proxyConfig;

	@Qualifier("zabbix")
	@Bean("zabbixDb_Datasource")
	public DataSource zabbixDbDatasource() {
		final DruidDataSource dataSource = new DruidDataSource();
		dataSource.setUrl(proxyConfig.getZabbixJdbcUrl());
		dataSource.setUsername(proxyConfig.getZabbixJdbcUserName());
		dataSource.setPassword(proxyConfig.getZabbixJdbcPasswd());
		Integer initSize = proxyConfig.getZabbixInitPoolSize();
		Integer maxSize = proxyConfig.getZabbixMaxPoolSize();
		if (initSize != null && initSize > 0) {
			dataSource.setInitialSize(initSize);
			log.info("The ZabbixDruidAutoConfiguration applys the init pool size {}.", initSize);
		}
		if (maxSize != null && maxSize > 0) {
			dataSource.setMaxActive(maxSize);
			log.info("The ZabbixDruidAutoConfiguration applys the max pool size {}.", maxSize);
		}
		try {
			dataSource.init();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return dataSource;
	}

//	@Qualifier("zabbix")
//	@Bean("zabbixDb_Servlet")
//	public ServletRegistrationBean zabbixDbServlet() {
//		final StatViewServlet druidServlet = new StatViewServlet();
//		final ServletRegistrationBean druidServletRegistration = new ServletRegistrationBean(druidServlet);
//		druidServletRegistration.addInitParameter("allow", "127.0.0.1");
//		druidServletRegistration.addUrlMappings("/zabbixDruid/*");
//		return druidServletRegistration;
//	}
	
	@Qualifier("zabbix")
	@Bean(name="zabbixDb_TransactionManager")
    public DataSourceTransactionManager zabbixDbTransactionManager(
    						@Qualifier("zabbixDb_Datasource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
	
	@Qualifier("zabbix")
	@Bean(name = "zabbixDb_SessionFactory")
	public SqlSessionFactory zabbixDbSessionFactory(
			@Qualifier("zabbixDb_Datasource") DataSource dataSource) throws Exception {
		SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
		bean.setDataSource(dataSource);
		
		PathMatchingResourcePatternResolver locationResolver = new PathMatchingResourcePatternResolver();
		List<Resource> allLocations = new ArrayList<Resource>();
		for (String location : proxyConfig.getZabbixMapperLocations()) {
			Resource[] locationArr = locationResolver.getResources(location);
			if (locationArr != null && locationArr.length > 0) {
				allLocations.addAll(Arrays.asList(locationArr));
			}
		}
		bean.setMapperLocations(allLocations.toArray(new Resource[allLocations.size()]));
		return bean.getObject();
	}
	
	@Qualifier("zabbix")
	@Bean(name = "zabbixDb_SqlSessionTemplate")
    public SqlSessionTemplate zabbixDbSqlSessionTemplate(
    		@Qualifier("zabbixDb_SessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}