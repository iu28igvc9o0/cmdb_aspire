package com.aspire.cdn.esdatawrap.biz.client;

import com.aspire.cdn.esdatawrap.biz.client.model.WeixinMessageSendRequest;
import com.aspire.cdn.esdatawrap.biz.client.model.WeixinCorpAccessTokenRepsonse;
import com.aspire.cdn.esdatawrap.biz.client.model.WeixinGeneralResponse;

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
 * 类功能描述: 微信企业公众号请求client
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
public interface IWeixinCorpClient {
    public static final String H_CONTENT_TYPE = "Content-Type: application/json;charset=UTF-8";
    public static final String H_TOKEN        = "Authorization: Token {token}";
    public static final String H_ACCEPT       = "Accept: application/json;charset=UTF-8";
    
    @Headers({H_ACCEPT})
    @RequestLine("GET /cgi-bin/gettoken?corpid={corpId}&corpsecret={secret}")
    public WeixinCorpAccessTokenRepsonse fetchAccessToken(@Param("corpId")String corpid, @Param("secret") String secret);
    
    @Headers({H_CONTENT_TYPE, H_ACCEPT})
    @RequestLine("POST /cgi-bin/message/send?access_token={accessToken}")
    public WeixinGeneralResponse sendWeixinCorpMessage(@Param("accessToken")String accessToken, WeixinMessageSendRequest alertMsg);
    
}
