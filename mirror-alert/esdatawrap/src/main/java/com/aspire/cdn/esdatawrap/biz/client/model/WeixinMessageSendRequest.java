package com.aspire.cdn.esdatawrap.biz.client.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/** 
 *
 * 项目名称: esdatawrap6 
 * <p/>
 * 
 * 类名: WeixinMessageSendRequest
 * <p/>
 *
 * 类功能描述: 微信企业公众号消息发送请求对象
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
public class WeixinMessageSendRequest {
	@JsonProperty("touser")
	private String	toUser	= "@all";  		// @all
	
	@JsonProperty("msgtype")
	private String	msgType	= "text";
	
	@JsonProperty("agentid")
	private String	agentId;
	
	@JsonProperty("text")
	private final InnerText innerText = new InnerText();
	
	@JsonProperty("safe")
	private Integer	safe = 0;
	

	public void setInnerContent(String title, String content) {
		innerText.setContent(title + "\n" + content);
	}
	
	@Data
	private static class InnerText {
		private String content;
	}
}
