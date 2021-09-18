package com.aspire.cdn.esdatawrap.biz.client;

import feign.Headers;
import feign.Param;
import feign.RequestLine;

/** 
 *
 * 项目名称: esdatawrap6 
 * <p/>
 * 
 * 类名: IWeixinCorpClient
 * <p/>
 *
 * 类功能描述: CDN点播质量数据请求客户端
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
public interface ICdnSnsSdkClient {
	public static final String H_CONTENT_TYPE = "Content-Type: application/json;charset=UTF-8";
    public static final String H_ACCEPT_ENCODE  = "Accept-Encoding: gzip";
    
    @Headers({H_ACCEPT_ENCODE, H_CONTENT_TYPE})
    @RequestLine("GET /video/cdn/quality_v4/realtime/?timestamp={timestamp}&user_name={userName}&sign={sign}&app_name={appName}")
    public Object realtimeQualityData(@Param("timestamp")Long timestamp, @Param("userName") String userName, 
    		@Param("sign") String sign, @Param("appName") String appName);
    
    @Headers({H_ACCEPT_ENCODE, H_CONTENT_TYPE})
    @RequestLine("GET /video/cdn/quality_v4/interval/?timestamp={timestamp}&user_name={userName}&sign={sign}&app_name={appName}"
    		+ "&start_time={startTime}&end_time={endTime}")
    public Object spantimeQualityData(@Param("timestamp")Long timestamp, @Param("userName") String userName, 
    	@Param("sign") String sign, @Param("appName") String appName, @Param("startTime")Long startTime, @Param("endTime")Long endTime );
    
}
