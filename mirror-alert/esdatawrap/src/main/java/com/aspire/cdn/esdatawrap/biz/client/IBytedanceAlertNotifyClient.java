package com.aspire.cdn.esdatawrap.biz.client;

import feign.Headers;
import feign.Param;
import feign.RequestLine;

/** 
 *
 * 项目名称: esdatawrap 
 * <p/>
 * 
 * 类名: IBytedanceAlertNotifyClient
 * <p/>
 *
 * 类功能描述: TODO
 * <p/>
 *
 * @author	pengguihua
 *
 * @date	2020年6月13日  
 *
 * @version	V1.0 
 * <br/>
 *
 * <b>Copyright(c)</b> 2020 卓望公司-版权所有 
 *
 */
public interface IBytedanceAlertNotifyClient {
    public static final String H_CONTENT_TYPE = "Content-Type: application/json;charset=UTF-8";
    public static final String H_ACCEPT       = "Accept: application/json;charset=UTF-8";
    
    
    @Headers({H_CONTENT_TYPE, H_ACCEPT})
    @RequestLine("POST /video/cdn/manufacture/alert/report?user_name={username}&timestamp={sendtime}&sign={sign}")
    public String notifyBytedanceAlert(
    	@Param("username") String username, @Param("sendtime") String sendtime, @Param("sign") String sign, String requestBody);
    
}
