package com.aspire.cdn.esdatawrap.biz.client.model;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.EqualsAndHashCode;

/** 
 *
 * 项目名称: esdatawrap6 
 * <p/>
 * 
 * 类名: WeixinCorpAccessTokenRepsonse
 * <p/>
 *
 * 类功能描述: 微信公众号access_token响应
 * <p/>
 *
 * @author	pengguihua
 *
 * @date	2020年5月18日  
 *
 * @version	V1.0 
 * <br/>
 *
 * <b>Copyright(c)</b> 2020 卓望公司-版权所有 
 *
 */
@Data
@EqualsAndHashCode(callSuper=true)
public class WeixinCorpAccessTokenRepsonse extends WeixinGeneralResponse {
	
	@JsonProperty("access_token")
	private String accessToken;
	
	@JsonProperty("expires_in")
	private Integer expires; 											// 失效时长(单位 s)
	
	private final Long markSeconds = Instant.now().getEpochSecond();	// 获取时间点(单位s)
	
	public boolean need2RefreshToken() {
		return markSeconds.longValue() + expires.intValue() - Instant.now().getEpochSecond() <= 60;
	}
}
