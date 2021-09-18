package com.aspire.mirror.indexproxy.basicdatasync.client;

import com.aspire.mirror.indexproxy.domain.*;
import feign.Headers;
import feign.Param;
import feign.RequestLine;

import java.util.List;

/**
 * phoenix服务调用接口
 * Project Name:jarchemist-service
 * File Name:IPhoenixClient.java
 * Package Name:com.migu.tsg.microservice.atomicservice.jalchemist.client
 * ClassName: IPhoenixClient <br/>
 * date: 2018年3月1日 下午5:22:14 <br/>
 *
 * @author pengguihua
 * @since JDK 1.6
 */
//@FeignClient("template-service")
public interface IBasicDataSyncClient {
    public static final String H_CONTENT_TYPE = "Content-Type: application/json;charset=UTF-8";
    public static final String H_TOKEN = "Authorization: Token {token}";
    public static final String H_ACCEPT = "Accept: application/json;charset=UTF-8";

    @Headers({H_CONTENT_TYPE, H_ACCEPT})
    @RequestLine("GET /v1/basicDataSync/templateList/{syncSeq}/{proxyIdentity}")
    public BasicDataSyncResponse<MonitorTemplateRecord> syncMonitorTemplateList(@Param("syncSeq") int startSyncSeq, @Param("proxyIdentity") String proxyIdentity);

    @Headers({H_CONTENT_TYPE, H_ACCEPT})
    @RequestLine("GET /v1/basicDataSync/itemList/{syncSeq}/{proxyIdentity}")
    public BasicDataSyncResponse<MonitorItemRecord> syncMonitorItemList(@Param("syncSeq") int startSyncSeq, @Param("proxyIdentity") String proxyIdentity);

    @Headers({H_CONTENT_TYPE, H_ACCEPT})
    @RequestLine("GET /v1/basicDataSync/triggerList/{syncSeq}/{proxyIdentity}")
    public BasicDataSyncResponse<MonitorTriggerRecord> syncMonitorTriggerList(@Param("syncSeq") int startSyncSeq, @Param("proxyIdentity") String proxyIdentity);

    @Headers({H_CONTENT_TYPE, H_ACCEPT})
    @RequestLine("GET /v1/basicDataSync/triggerDynamicModelList/{syncSeq}/{proxyIdentity}")
    public BasicDataSyncResponse<MonitorDynamicThresholdRecord> syncMonitorTriggerDynamicModelList(@Param("syncSeq") int startSyncSeq, @Param("proxyIdentity") String proxyIdentity);

    @Headers({H_CONTENT_TYPE, H_ACCEPT})
    @RequestLine("GET /v1/basicDataSync/actionList/{syncSeq}/{proxyIdentity}")
    public BasicDataSyncResponse<MonitorActionRecord> syncMonitorActionList(@Param("syncSeq") int startSyncSeq, @Param("proxyIdentity") String proxyIdentity);

    @Headers({H_CONTENT_TYPE, H_ACCEPT})
    @RequestLine("GET /v1/hosts/getMonitorHostByProxyIdentity/{proxyIdentity}")
    public List<MirrorHostVO> getMonitorHostByProxyIdentity(@Param("proxyIdentity") String proxyIdentity);
}
