package com.aspire.mirror.elasticsearch.server.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
* 项目名称: rbac-service <br>
* 包: com.migu.tsg.microservice.atomicservice.common.config <br>
* 类名称: RedisProperties.java <br>
* 类描述: REDIS配置对象<br>
* 创建人: WangSheng <br>
* 创建时间: 2017年12月8日下午5:26:21 <br>
* 版本: v1.0
*/
@Data
@ConfigurationProperties(prefix = "spring.redis")
public class RedisProperties {

    /**
     * 为了规范redis键值名，避免冲突，方面redis中间件附件
     * 因此请大家按如下规则进行key的命名，统一采用如下前缀：
     * 大模块名—子模块名—自定义功能名，举例：
     *    monitor-log-XXXX，监控-日志-XXXX功能key
     *    res-service-XXXX，资源-服务-XXXX功能key
     *    
     * 用户权限缓存KEY前缀
     */

    /**
     * 缓存KEY分割标识
     */
    private String redisKeySplit = "~";

    /**
     * 过期时间,单位秒
     */
    private int expiration = 3600;

    /**
     * 是否使用REDIS缓存
     */
    private Boolean usable = false;

}
