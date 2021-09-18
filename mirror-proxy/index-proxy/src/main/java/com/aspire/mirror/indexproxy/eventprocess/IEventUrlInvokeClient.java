package com.aspire.mirror.indexproxy.eventprocess;

import feign.Headers;
import feign.RequestLine;

/**
* url回调接口    <br/>
* Project Name:index-proxy
* File Name:IEventUrlInvokeClient.java
* Package Name:com.aspire.mirror.indexproxy.eventprocess
* ClassName: IEventUrlInvokeClient <br/>
* date: 2018年8月21日 上午11:02:28 <br/>
* @author pengguihua
* @version 
* @since JDK 1.6
*/ 
interface IEventUrlInvokeClient {
    public static final String H_CONTENT_TYPE = "Content-Type: application/json;charset=UTF-8";
    public static final String H_TOKEN        = "Authorization: Token {token}";
    public static final String H_ACCEPT       = "Accept: application/json;charset=UTF-8";
    
	@Headers({H_ACCEPT, H_CONTENT_TYPE})
	@RequestLine("POST ")
	public String invokeUrl(Object bizObj);
}
