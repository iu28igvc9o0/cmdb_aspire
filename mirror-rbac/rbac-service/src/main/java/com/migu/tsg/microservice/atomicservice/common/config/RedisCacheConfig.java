/**
 * 
 */
package com.migu.tsg.microservice.atomicservice.common.config;

import java.nio.charset.Charset;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;
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
@EnableCaching
public class RedisCacheConfig extends CachingConfigurerSupport{
    
    @Autowired
    private RedisConnectionFactory redisConnectionFactory;
    
    /**
     * RedisTemplate配置
     * @param factory Redis连接工厂对象
     * @return RedisTemplate Redis模板对象
     */
    @Bean
    public RedisTemplate<String, Object> getRedisTemplate() {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(
                Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);
        redisTemplate.setKeySerializer(new StringKeyRedisSerializer());
        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
        redisTemplate.setHashKeySerializer(new StringKeyRedisSerializer());
        redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    private static class StringKeyRedisSerializer implements RedisSerializer<Object> {
        private static final Charset UTF8 = Charset.forName("utf-8");

        @Override
        public byte[] serialize(Object t) throws SerializationException {
            return t.toString().getBytes(UTF8);
        }

        @Override
        public Object deserialize(byte[] bytes) throws SerializationException {
            return new String(bytes, UTF8);
        }
    }
}
