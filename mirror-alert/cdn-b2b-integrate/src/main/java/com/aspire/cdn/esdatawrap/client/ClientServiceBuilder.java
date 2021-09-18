package com.aspire.cdn.esdatawrap.client;

import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;

import feign.Feign;
import feign.Logger;
import feign.Request.Options;
import feign.Retryer;
import feign.httpclient.ApacheHttpClient;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import feign.slf4j.Slf4jLogger;

/**
* 基于feign调用的客户端接口构造工厂
* Project Name:jarchemist-service
* File Name:ClientServiceBuilder.java
* Package Name:com.migu.tsg.microservice.atomicservice.jalchemist.client
* ClassName: ClientServiceBuilder <br/>
* date: 2018年3月1日 下午5:04:51 <br/>
* 
* @author pengguihua
* @version 
* @since JDK 1.6
*/
public final class ClientServiceBuilder {
    private static final int CONNECT_TIMEOUT_MILLS = 2000; 
    private static final int READ_TIMEOUT_MILLS    = 300000; 
    
	/**
     * buildClientService:构造feign客户端服务接口. <br/>
     *
     * 作者： pengguihua
     * @param clientInterface
     * @param targetUrl
     * @return
     */
    public static <T> T buildClientService(Class<T> clientInterface, String targetUrl) {
        if (StringUtils.isBlank(targetUrl)) {
            throw new RuntimeException("The targetUrl to build the feign client is null or blank.");
        }
        return Feign.builder()
        		.client(new ApacheHttpClient())
                .logger(new Slf4jLogger())
                .logLevel(Logger.Level.FULL)
                .errorDecoder(new FeignErrorDecoder())
                .encoder(new JacksonEncoder())
                .decoder(new JacksonDecoder())
                .options(new Options(CONNECT_TIMEOUT_MILLS, TimeUnit.MILLISECONDS, READ_TIMEOUT_MILLS, TimeUnit.MILLISECONDS, true))
                .retryer(new Retryer.Default(1000, 3000, 2))
                .target(clientInterface, targetUrl);
    }
}
