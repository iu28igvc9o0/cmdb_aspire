package com.migu.tsg.microservice.atomicservice.common.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import feign.hystrix.FallbackFactory;

/**
 * 项目名称: rbac-service <br>
 * 包: com.migu.tsg.microservice.atomicservice.common.client <br>
 * 类名称: HystrixClientFactory.java <br>
 * 类描述: 暂时不使用,后面优化使用 <br>
 * 创建人: WangSheng <br>
 * 创建时间: 2017年8月26日下午6:54:44 <br>
 * 版本: v1.0
 */
// @Component
public class HystrixClientFactory implements FallbackFactory<LdapServiceClient> {

    /**
     * 日志
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(HystrixClientFactory.class);

    public LdapServiceClient create(Throwable cause) {
        LOGGER.info("fallback reason was: {}", cause.getMessage());
        return null;
    }
}
