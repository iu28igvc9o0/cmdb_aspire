package com.aspire.mirror.inspection.server.util;

import java.nio.charset.Charset;

import org.apache.geronimo.mail.util.Base64;

/**
* OSA系统请求authtoken    <br/>
* Project Name:inspection-service
* File Name:OsaAuthTokenUtil.java
* Package Name:com.aspire.mirror.inspection.server.util
* ClassName: OsaAuthTokenUtil <br/>
* date: 2018年9月13日 下午1:49:33 <br/>
* @author pengguihua
* @version 
* @since JDK 1.6
*/ 
public final class OsaAuthTokenUtil {
	public static final String AUTH_PUBLIC_CODE = "2fb0a50c850573fd6a49fd458c717105c93c7437f8a120de79774f77";
	public static final String AUTH_SOURCE 		= "micro_service_inspection";
	public static final String JOIN_SIGN 		= "||";
	
	public static String generateOsaAuthToken() {
		String rawToken = AUTH_PUBLIC_CODE + JOIN_SIGN + AUTH_SOURCE + JOIN_SIGN + System.currentTimeMillis();
		String encodeToken = new String(Base64.encode(rawToken.getBytes(Charset.forName("UTF-8"))));    
		return encodeToken;
	}
}
