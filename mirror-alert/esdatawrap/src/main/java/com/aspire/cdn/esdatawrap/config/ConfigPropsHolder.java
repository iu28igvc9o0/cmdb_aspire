package com.aspire.cdn.esdatawrap.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.aspire.cdn.esdatawrap.config.model.AlertWeixinNotifyConfigProps;
import com.aspire.cdn.esdatawrap.config.model.BytedanceAlertConfigProps;
import com.aspire.cdn.esdatawrap.config.model.CompressOverall5minProps;
import com.aspire.cdn.esdatawrap.config.model.MetricAlertConfigProps;
import com.aspire.cdn.esdatawrap.config.model.ReIndexConfigProps;

/** 
 *
 * 项目名称: esdatawrap6 
 * <p/>
 * 
 * 类名: ConfigPropsHolder
 * <p/>
 *
 * 类功能描述: TODO
 * <p/>
 *
 * @author	pengguihua
 *
 * @date	2020年5月12日  
 *
 * @version	V1.0 
 * <br/>
 *
 * <b>Copyright(c)</b> 2020 卓望公司-版权所有 
 *
 */
@Configuration
public class ConfigPropsHolder {
	
	@Bean
	@ConfigurationProperties(prefix="local.config.es-run-action.reindex")
	public ReIndexConfigProps reIndexConfigProps() {
		return new ReIndexConfigProps();
	}
	
	@Bean
	@ConfigurationProperties(prefix="local.config.es-run-action.bytedance-alert")
	public BytedanceAlertConfigProps bytedanceConfigProps() {
		return new BytedanceAlertConfigProps();
	}
	
	@Bean
	@ConfigurationProperties(prefix="local.config.es-run-action.compress-overall5min")
	public CompressOverall5minProps compressOverall5minProps() {
		return new CompressOverall5minProps();
	}
	
	@Bean
	@ConfigurationProperties(prefix="local.config.es-run-action.alert-weixin-notify")
	public AlertWeixinNotifyConfigProps alertWeixinNotifyConfigProps() {
		return new AlertWeixinNotifyConfigProps();
	}
	
	@Bean
	@ConfigurationProperties(prefix="matric-alert")
	public MetricAlertConfigProps metricAlertConfigProps() {
		return new MetricAlertConfigProps();
	}
}
