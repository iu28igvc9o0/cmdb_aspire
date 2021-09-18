package com.aspire.cdn.esdatawrap.biz.client;

import java.util.List;

import com.aspire.cdn.esdatawrap.biz.client.model.SyncItemResponse;
import com.aspire.cdn.esdatawrap.biz.umsalert.UmsAlertMessage;

import feign.Headers;
import feign.RequestLine;

/** 
 *
 * 项目名称: cdn-integrate 
 * <p/>
 * 
 * 类名: IAlertServiceClient
 * <p/>
 *
 * 类功能描述: TODO
 * <p/>
 *
 * @author	pengguihua
 *
 * @date	2020年6月22日  
 *
 * @version	V1.0 
 * <br/>
 *
 * <b>Copyright(c)</b> 2020 卓望公司-版权所有 
 *
 */
public interface IAlertServiceClient {
    public static final String H_CONTENT_TYPE = "Content-Type: application/json;charset=UTF-8";
    public static final String H_ACCEPT       = "Accept: application/json;charset=UTF-8";
    
    
    @Headers({H_CONTENT_TYPE, H_ACCEPT})
    @RequestLine("POST /v1/alerts/third/create")
    public SyncItemResponse postAlertMessage(UmsAlertMessage alertMsg);
    
    /** 
     * 功能描述: 批量告警上报; 注意： 批量接口会忽略错误的告警，返回成功  
     * <p>
     * @param alertMsgList
     * @return
     */
    @Headers({H_CONTENT_TYPE, H_ACCEPT})
    @RequestLine("POST /v1/alerts/third/batch")
    public SyncItemResponse batchPostAlertMessageList(List<UmsAlertMessage> alertMsgList);
}
