/**
 * 
 */
package com.migu.tsg.microservice.atomicservice.composite.common;

import java.io.UnsupportedEncodingException;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

/**
* 项目名称: rbac-service <br>
* 包: com.migu.tsg.microservice.atomicservice.common.helper <br>
* 类名称: RedisCacheHelper.java <br>
* 类描述: Redis缓存工具类<br>
* 创建人: WangSheng <br>
* 创建时间: 2017年10月2日上午11:07:31 <br>
* 版本: v1.0
*/
@Component
public class RedisCacheHelper {

    /**
     * 日志
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(RedisCacheHelper.class);

    @Autowired
    private RedisTemplate<?, ?> redisTemplate;
    
    /**
     * 过期时间,单位秒
     */
    @Value("${spring.redis.expiration:3600}")
    public int defaultExpiration;

    /**
     * 默认编码
     */
    private final static String DEFAULT_ENCODING = "utf-8";

    /**
     * redis数据库中key的数量 
     * @return 数量
     */
    public Long dbSize() {
        try {
            return redisTemplate.execute((RedisCallback<Long>) c -> c.dbSize());
        } catch (RedisConnectionFailureException e) {
            LOGGER.error("mothod[dbSize] The aop error for cannot get jedis connection{}", e);
        } catch (Exception e) {
            LOGGER.error("mothod[dbSize] The aop error for unknown issue{}", e);
        }
        return null;
    }

    /**
     * Ping
     * @return PONG
     */
    public String ping() {
        try {
            return redisTemplate.execute((RedisCallback<String>) c -> c.ping());
        } catch (RedisConnectionFailureException e) {
            LOGGER.error("mothod[ping] The aop error for cannot get jedis connection{}", e);
        } catch (Exception e) {
            LOGGER.error("mothod[ping] The aop error for unknown issue{}", e);
        }
        return null;
    }

    /** 
     * 删除所有指定数据库的数据 
     */
    public Long flushDB() {
        try {
            return redisTemplate.execute((RedisCallback<Long>) c -> {
                c.flushDb();
                return 1L;
            });
        } catch (RedisConnectionFailureException e) {
            LOGGER.error("mothod[flushDB] The aop error for cannot get jedis connection{}", e);
        } catch (Exception e) {
            LOGGER.error("mothod[flushDB] The aop error for unknown issue{}", e);
        }
        return null;
    }

    /**
     * 判断redis数据库是否有对应的key
     * @param key 不能为null
     * @return boolean
     */
    public boolean exist(String key) {
        try {
            return redisTemplate.execute((RedisCallback<Boolean>) c -> {
                try {
                    return c.exists(key.getBytes(DEFAULT_ENCODING));
                } catch (UnsupportedEncodingException e) {
                    LOGGER.error("method[exist] The character encoding is not supported:{}", e);
                    return false;
                }
            });
        } catch (RedisConnectionFailureException e) {
            LOGGER.error("mothod[exist] The aop error for cannot get jedis connection{}", e);
        } catch (Exception e) {
            LOGGER.error("mothod[exist] The aop error for unknown issue{}", e);
        }
        return false;
    }

    /**
     * 获得redis数据库所有匹配的key
     * @param pattern 正则表达式字符串
     * @return 匹配key的集合
     */
    public Set<String> keys(String pattern) {
        try {
            return redisTemplate.execute((RedisCallback<Set<String>>) c -> {
                try {
                    return c.keys(pattern.getBytes(DEFAULT_ENCODING)).stream().map(this::getUTF)
                            .filter(key -> StringUtils.isNotBlank(key)).collect(Collectors.toSet());
                } catch (UnsupportedEncodingException e) {
                    LOGGER.error("method[keys] The character encoding is not supported:{}", e);
                }
                return null;
            });
        } catch (RedisConnectionFailureException e) {
            LOGGER.error("mothod[keys] The aop error for cannot get jedis connection{}", e);
        } catch (Exception e) {
            LOGGER.error("mothod[keys] The aop error for unknown issue{}", e);
        }
        return null;
    }

    private String getUTF(byte[] data) {
        try {
            return new String(data, DEFAULT_ENCODING);
        } catch (UnsupportedEncodingException e) {
            LOGGER.error("method[getUTF] The character encoding is not supported:{}", e);
            return null;
        }
    }

    /**
     * 删除redis数据库指定的key
     * @param key 不能为null
     * @return 删除的key的数量
     */
    public Long delete(String key) {
        try {
            return redisTemplate.execute((RedisCallback<Long>) c -> {
                try {
                    return c.del(key.getBytes(DEFAULT_ENCODING));
                } catch (UnsupportedEncodingException e) {
                    LOGGER.error("method[delete] The character encoding is not supported:{}", e);
                }
                return null;
            });
        } catch (RedisConnectionFailureException e) {
            LOGGER.error("mothod[delete] The aop error for cannot get jedis connection{}", e);
        } catch (Exception e) {
            LOGGER.error("mothod[delete] The aop error for unknown issue{}", e);
        }
        return null;
    }

    /**
     * 写入缓存
     * @param key 不能为null
     * @param object value
     */
    @SuppressWarnings("unchecked")
    public void set(String key, Object object,int expiration,TimeUnit unit) {
        try {
           /* ((ValueOperations<Object, Object>) redisTemplate.opsForValue()).set(key, object, expiration,
                    TimeUnit.SECONDS);*/
        	if(0==expiration) {
        		expiration = defaultExpiration;
        		unit = TimeUnit.SECONDS;
        	}
            ((ValueOperations<Object, Object>) redisTemplate.opsForValue()).set(key, object, expiration,
            		unit);
        } catch (RedisConnectionFailureException e) {
            LOGGER.error("mothod[set] The aop error for cannot get jedis connection{}", e);
        } catch (Exception e) {
            LOGGER.error("mothod[set] The aop error for unknown issue{}", e);
        }
    }

    /**
     * 读取缓存
     * @param key 不能为null
     * @return value
     */
    @SuppressWarnings("unchecked")
    public Object get(String key) {
        try {
            /*
             *  ValueOperations　　 ——基本数据类型和实体类的缓存
             *  ListOperations　　 ——list的缓存
             *  SetOperations　　 ——set的缓存
             *  HashOperations　　 ——Map的缓存
             */
            return ((ValueOperations<Object, Object>) redisTemplate.opsForValue()).get(key);
        } catch (RedisConnectionFailureException e) {
            LOGGER.error("mothod[get] The aop error for cannot get jedis connection{}", e);
        } catch (Exception e) {
            LOGGER.error("mothod[get] The aop error for unknown issue{}", e);
        }
        return null;
    }

}
