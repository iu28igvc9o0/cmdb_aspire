/**
 * 
 */
package com.migu.tsg.microservice.atomicservice.composite.config;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 项目名称: rbac-service <br>
 * 包: com.migu.tsg.microservice.atomicservice.common.config <br>
 * 类名称: RedisCacheConfig.java <br>
 * 类描述: Redis缓存配置<br>
 * 创建人: WangSheng <br>
 * 创建时间: 2017年9月16日下午2:49:24 <br>
 * 版本: v1.0
 */
@Configuration
@EnableCaching // 启用缓存,这个注解很重要
public class RedisCacheConfig extends CachingConfigurerSupport {
    
    /**
     *  过期时间,单位秒
     */
    @Value("${spring.redis.expiration:3600}")
    public int expiration;

    /**
     * 生成key策略
     */
    private static final KeyGenerator keyGenerator = new KeyGenerator() {
        public Object generate(Object target, Method method, Object... params) {
            StringBuilder sb = new StringBuilder();
            sb.append(target.getClass().getName());
            sb.append(method.getName());
            for (Object obj : params) {
                if (obj != null) {
                    sb.append(obj.toString());
                }
            }
            return sb.toString();
        }

    };

    /**
     * 生成key的策略
     * @return KeyGenerator Key策略对象
     */
    @Bean
    public KeyGenerator keyGenerators() {
        return keyGenerator;
    }

    /**
     * 管理缓存
     * @param redisTemplate Redis模板对象
     * @return CacheManager 缓存管理器
     */
    @Bean
    public CacheManager cacheManager(RedisTemplate<?, ?> redisTemplate) {
        RedisCacheManager rcm = new RedisCacheManager(redisTemplate);
        // 设置缓存过期时间
        rcm.setDefaultExpiration(expiration);// 单位秒
        // 设置value/cacheNames的过期时间
        Map<String, Long> map = new HashMap<String, Long>();
        map.put("test", 60L);
        rcm.setExpires(map);
        return rcm;
    }

    /**
     * RedisTemplate配置
     * @param factory Redis连接工厂对象
     * @return RedisTemplate Redis模板对象
     */
    @Bean
    public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory factory) {
        StringRedisTemplate template = new StringRedisTemplate(factory);
        Jackson2JsonRedisSerializer<?> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(
                Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);
        template.setValueSerializer(jackson2JsonRedisSerializer);
        template.afterPropertiesSet();
        return template;
    }

}
