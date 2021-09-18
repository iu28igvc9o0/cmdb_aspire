package com.aspire.mirror.indexadapt.adapt.mirrorbizmonitordb.datasource;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
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
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Configuration
@EnableConfigurationProperties(MiguBizMonitorDruidProperties.class)
@ConditionalOnClass(DruidDataSource.class)
@AutoConfigureAfter(DataSourceAutoConfiguration.class)
@ConditionalOnProperty(prefix = "indexAdapter.mirrorbizmonitorDb", name = "switch")
@MapperScan(basePackages = "com.aspire.mirror.indexadapt.adapt.mirrorbizmonitordb.dao",
			sqlSessionTemplateRef = "mirrorbizmonitorDb_SqlSessionTemplate")
public class MirrorBizMonitorDruidAutoConfiguration {
	@Autowired
	private MiguBizMonitorDruidProperties dbConfig;

	@Qualifier("mirrorbizmonitorDb")
	@Bean("mirrorbizmonitorDb_Datasource")
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
	
	@Bean("mirrorbizmonitorDb_Servlet")
	public ServletRegistrationBean mirrorbizmonitorDbServlet() {
		final StatViewServlet druidServlet = new StatViewServlet();
		final ServletRegistrationBean druidServletRegistration = new ServletRegistrationBean(druidServlet);
		druidServletRegistration.addInitParameter("allow", "127.0.0.1");
		druidServletRegistration.addUrlMappings("/bizMonitorDruid/*");
		return druidServletRegistration;
	}
	
	@Bean(name = "mirrorbizmonitorDb_TransactionManager")
    public DataSourceTransactionManager mirrorbizmonitorDbTransactionManager(
    						@Qualifier("mirrorbizmonitorDb") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
	
	@Qualifier("mirrorbizmonitorDb")
	@Bean(name = "mirrorbizmonitorDb_SessionFactory")
	public SqlSessionFactory mirrorbizmonitorDbSessionFactory(
			@Qualifier("mirrorbizmonitorDb") DataSource dataSource) throws Exception {
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
	
	@Bean(name = "mirrorbizmonitorDb_SqlSessionTemplate")
    public SqlSessionTemplate mirrorbizmonitorDbSqlSessionTemplate(
    		@Qualifier("mirrorbizmonitorDb") SqlSessionFactory sqlSessionFactory) throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}