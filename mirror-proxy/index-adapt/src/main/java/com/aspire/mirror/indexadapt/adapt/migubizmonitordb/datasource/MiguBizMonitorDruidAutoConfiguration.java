package com.aspire.mirror.indexadapt.adapt.migubizmonitordb.datasource;

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
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;

@Configuration
@EnableConfigurationProperties(MiguBizMonitorDruidProperties.class)
@ConditionalOnClass(DruidDataSource.class)
@AutoConfigureBefore(DataSourceAutoConfiguration.class)
@ConditionalOnProperty(prefix = "indexAdapter.migubizmonitorDb", name = "switch")
@MapperScan(basePackages = "com.aspire.mirror.indexadapt.adapt.migubizmonitordb.dao", 
			sqlSessionTemplateRef = "migubizmonitorDb_SqlSessionTemplate")
public class MiguBizMonitorDruidAutoConfiguration {
	@Autowired
	private MiguBizMonitorDruidProperties dbConfig;

	@Qualifier("migubizmonitorDb")
	@Bean("migubizmonitorDb_Datasource")
	public DataSource bizMonitorDDatasource() {
		final DruidDataSource dataSource = new DruidDataSource();
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
			throw new RuntimeException(e);
		}
		return dataSource;
	}

	@Bean("migubizmonitorDb_Servlet")
	public ServletRegistrationBean migubizmonitorDbServlet() {
		final StatViewServlet druidServlet = new StatViewServlet();
		final ServletRegistrationBean druidServletRegistration = new ServletRegistrationBean(druidServlet);
		druidServletRegistration.addInitParameter("allow", "127.0.0.1");
		druidServletRegistration.addUrlMappings("/bizMonitorDruid/*");
		return druidServletRegistration;
	}
	
	@Bean("migubizmonitorDb_TransactionManager")
    public DataSourceTransactionManager migubizmonitorDbTransactionManager(
    						@Qualifier("migubizmonitorDb") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
	
	@Qualifier("migubizmonitorDb")
	@Bean("migubizmonitorDb_SessionFactory")
	public SqlSessionFactory migubizmonitorDbSessionFactory(
			@Qualifier("migubizmonitorDb") DataSource dataSource) throws Exception {
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
	
	@Bean("migubizmonitorDb_SqlSessionTemplate")
    public SqlSessionTemplate migubizmonitorDbSqlSessionTemplate(
    		@Qualifier("migubizmonitorDb") SqlSessionFactory sqlSessionFactory) throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}