package com.aspire.cdn.esdatawrap.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.aspire.cdn.esdatawrap.config.model.AlertAppMobileNotifyConfigProps;
import com.aspire.cdn.esdatawrap.config.model.CdnSnsSdkConfigProps;
import com.aspire.cdn.esdatawrap.config.model.FocusDoaminReq404PecentConfigProps;
import com.aspire.cdn.esdatawrap.config.model.FocusDomainConfigProps;
import com.aspire.cdn.esdatawrap.config.model.MetricAlertConfigProps;
import com.aspire.cdn.esdatawrap.config.model.OpsReportConfigProps;
import com.aspire.cdn.esdatawrap.config.model.ProvinceEsUnlinkConfigProps;
import com.aspire.cdn.esdatawrap.config.model.Req5xxPecentConfigProps;
import com.aspire.cdn.esdatawrap.config.model.UmsAlertIntegrateProps;

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
	@ConfigurationProperties(prefix="cdnsnssdk")
	public CdnSnsSdkConfigProps cdnSnsSdkConfigProps() {
		return new CdnSnsSdkConfigProps();
	}
	
	@Bean
	@ConfigurationProperties(prefix="local.config.es-run-action.alert-app-mobile-notify")
	public AlertAppMobileNotifyConfigProps alertAppMobileNotifyConfigProps() {
		return new AlertAppMobileNotifyConfigProps();
	}
	
	@Bean
	@ConfigurationProperties(prefix="local.config.es-run-action.req5xx-percent")
	public Req5xxPecentConfigProps req5xxPecentConfigProps() {
		return new Req5xxPecentConfigProps();
	}
	
	@Bean
	@ConfigurationProperties(prefix="local.config.es-run-action.focus-domain-req404-percent")
	public FocusDoaminReq404PecentConfigProps focusDoaminReq4xxPecentConfigProps() {
		return new FocusDoaminReq404PecentConfigProps();
	}
	
	@Bean
	@ConfigurationProperties(prefix="local.config.es-run-action")
	public FocusDomainConfigProps focusDomainConfigProps() {
		return new FocusDomainConfigProps();
	}
	
	@Bean
	@ConfigurationProperties(prefix="local.config.es-run-action.ums-alert-integrate")
	public UmsAlertIntegrateProps umsAlertIntegrateProps() {
		return new UmsAlertIntegrateProps();
	}
	
	@Bean
	@ConfigurationProperties(prefix="local.config.es-run-action.province-es-unlink")
	public ProvinceEsUnlinkConfigProps provinceEsUnlinkConfigProps() {
		return new ProvinceEsUnlinkConfigProps();
	}
	
	@Bean
	@ConfigurationProperties(prefix="ops-report")
	public OpsReportConfigProps opsReportConfigProps() {
		return new OpsReportConfigProps();
	}
	
//	
//	@Bean
//	@ConfigurationProperties(prefix="local.config.es-run-action.alert-weixin-notify")
//	public AlertWeixinNotifyConfigProps alertWeixinNotifyConfigProps() {
//		return new AlertWeixinNotifyConfigProps();
//	}
	
	@Bean
	@ConfigurationProperties(prefix="matric-alert")
	public MetricAlertConfigProps metricAlertConfigProps() {
		return new MetricAlertConfigProps();
	}
}
