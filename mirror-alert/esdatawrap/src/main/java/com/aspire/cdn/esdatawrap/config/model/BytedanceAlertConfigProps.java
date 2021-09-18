package com.aspire.cdn.esdatawrap.config.model;

import java.nio.charset.Charset;
import java.time.Instant;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

import com.aspire.cdn.esdatawrap.util.Md5Util;

import lombok.Data;

/** 
 *
 * 项目名称: esdatawrap6 
 * <p/>
 * 
 * 类名: BytedanceAlertConfigProps
 * <p/>
 *
 * 类功能描述:  
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
@Data
@ConfigurationProperties(prefix="local.config.es-run-action.bytedance-alert")
public class BytedanceAlertConfigProps {
	private Integer			minReqTotalCount;
	private Float			triggerPercentFloor4xx;
	private Float			triggerPercentFloor5xx;
	private List<String>	domainList;
	private String			apiUsername				= "mobile_cdn";
	private String			apiToken				= "1ccac75d170146999971aa238a02fd92";
	private String			notifyUrlBase			= "http://cdnlog.snssdk.com";
	private String			signTemplate			= "ts%suser%stoken%s";
	
	public String getBytedanceSignMd5(Instant time) {
		String timeStr = time.toString();
		String sign = String.format(signTemplate, timeStr, apiUsername, apiToken);
		return Md5Util.getMD5String(sign.getBytes(Charset.forName("UTF-8")));
	}
}
