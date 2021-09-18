package com.aspire.cdn.esdatawrap.biz.client;

import java.util.List;

import com.aspire.cdn.esdatawrap.biz.metricalert.MetricAlert;

import feign.Headers;
import feign.RequestLine;

/** 
 *
 * 项目名称: cdn-ott-integrate 
 * <p/>
 * 
 * 类名: IMetricAlertIntegrateClient
 * <p/>
 *
 * 类功能描述: TODO
 * <p/>
 *
 * @author	pengguihua
 *
 * @date	2020年7月11日  
 *
 * @version	V1.0 
 * <br/>
 *
 * <b>Copyright(c)</b> 2020 卓望公司-版权所有 
 *
 */
public interface IMetricAlertIntegrateClient {
    public static final String H_CONTENT_TYPE = "Content-Type: application/json;charset=UTF-8";
    public static final String H_ACCEPT       = "Accept: application/json;charset=UTF-8";
    
    @Headers({H_CONTENT_TYPE})
    @RequestLine("POST /esdatawrap/metricAlert/pushMetricAlert")
    public void pushMetricAlert(MetricAlert metricAlert);
    
    @Headers({H_CONTENT_TYPE})
    @RequestLine("POST /esdatawrap/metricAlert/pushMetricAlertBatch")
    public void pushMetricAlertBatch(List<MetricAlert> metricAlertList);
}
