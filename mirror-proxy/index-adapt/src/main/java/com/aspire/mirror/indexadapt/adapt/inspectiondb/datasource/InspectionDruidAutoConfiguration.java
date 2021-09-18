package com.aspire.mirror.indexadapt.adapt.inspectiondb.datasource;

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
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
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
@EnableConfigurationProperties(InspectionDruidProperties.class)
@ConditionalOnClass(DruidDataSource.class)
@AutoConfigureAfter(DataSourceAutoConfiguration.class)
@ConditionalOnProperty(prefix = "indexAdapter.inspectionDb", name = "switch")
@MapperScan(basePackages = "com.aspire.mirror.indexadapt.adapt.inspectiondb.dao", 
			sqlSessionTemplateRef = "inspectionDb_SqlSessionTemplate")
public class InspectionDruidAutoConfiguration {
	@Autowired
	private InspectionDruidProperties dbConfig;

	@Bean("inspectionDb_Datasource")
	public DataSource inspectionDbDatasource() {
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

	@Bean("inspectionDb_Servlet")
	public ServletRegistrationBean inspectionDbServlet() {
		final StatViewServlet druidServlet = new StatViewServlet();
		final ServletRegistrationBean druidServletRegistration = new ServletRegistrationBean(druidServlet);
		druidServletRegistration.addInitParameter("allow", "127.0.0.1");
		druidServletRegistration.addUrlMappings("/inspectionDruid/*");
		return druidServletRegistration;
	}
	
	@Bean(name = "inspectionDb_TransactionManager")
    public DataSourceTransactionManager inspectionDbTransactionManager(
    						@Qualifier("inspectionDb_Datasource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
	
	@Bean(name = "inspectionDb_SessionFactory")
	public SqlSessionFactory inspectionDbSessionFactory(
			@Qualifier("inspectionDb_Datasource") DataSource dataSource) throws Exception {
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
	
	@Bean(name = "inspectionDb_SqlSessionTemplate")
    public SqlSessionTemplate inspectionDbSqlSessionTemplate(
    		@Qualifier("inspectionDb_SessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}