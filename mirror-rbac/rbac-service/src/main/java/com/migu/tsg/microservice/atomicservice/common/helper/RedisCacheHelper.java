/**
 * 
 */
package com.migu.tsg.microservice.atomicservice.common.helper;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.migu.tsg.microservice.atomicservice.common.config.RedisProperties;

import lombok.Data;

/**
* 项目名称: rbac-service <br>
* 包: com.migu.tsg.microservice.atomicservice.common.helper <br>
* 类名称: RedisCacheHelper.java <br>
* 类描述: Redis缓存工具类<br>
* 创建人: WangSheng <br>
* 创建时间: 2017年10月2日上午11:07:31 <br>
* 版本: v1.0
*/
@Data
@Component
public class RedisCacheHelper {

    /**
     * 日志
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(RedisCacheHelper.class);

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private RedisProperties redisProperties;

    /**
     * 判断redis数据库是否有对应的key
     * @param key 不能为null
     * @return boolean
     */
    public boolean hasKey(String key) {
        try {
            return redisTemplate.hasKey(key);
        } catch (Exception e) {
            LOGGER.warn("RedisTemplate hasKey error : {}", e);
        }
        return false;
    }

    /**
     * 获得redis数据库所有匹配的keys
     * @param pattern 正则表达式字符串
     * @return 匹配key的集合
     */
    public Set<String> keys(String pattern) {
        try {
            return redisTemplate.keys(pattern);
        } catch (Exception e) {
            LOGGER.warn("RedisTemplate keys error : {}", e);
        }
        return null;
    }

    /**
     * 删除redis数据库指定的keys
     * @param keys 不能为null
     */
    public void delete(Set<String> keys) {
        try {
            redisTemplate.delete(keys);
        } catch (Exception e) {
            LOGGER.warn("RedisTemplate delete error : {}", e);
        }
    }
    
    /**
     * 删除redis数据库指定的key
     * @param key 不能为null
     */
    public void delete(String key) {
        try {
            redisTemplate.delete(key);
        } catch (Exception e) {
            LOGGER.warn("RedisTemplate delete error : {}", e);
        }
    }

    /**
     * 写入Hash缓存
     * @param key 不能为null
     * @param hashKey 不能为null
     * @param object value
     */
    public boolean put(String key, String hashKey, Object value) {
        try {
            redisTemplate.expire(key, redisProperties.getExpiration(), TimeUnit.SECONDS);
            redisTemplate.opsForHash().put(key, hashKey, value);
            return true;
        } catch (Exception e) {
            LOGGER.warn("RedisTemplate hash put error : {}", e);
            return false;
        }
    }

    /**
     * 读取Hash缓存所有的VALUE
     * @param key 不能为null
     * @return value
     */
    public List<Object> values(String key) {
        try {
            return redisTemplate.opsForHash().values(key);
        } catch (Exception e) {
            LOGGER.warn("RedisTemplate hash values error : {}", e);
        }
        return null;
    }

    /**
     * 读取Hash缓存
     * @param key 不能为null
     * @param hashKey 不能为null
     * @return value
     */
    public Object get(String key, String hashKey) {
        try {
            return redisTemplate.opsForHash().get(key, hashKey);
        } catch (Exception e) {
            LOGGER.warn("RedisTemplate hash get error : {}", e);
        }
        return null;
    }

    /**
     * 判断Hash缓存Key是否存在
     * @param key 不能为null
     * @param hashKey 不能为null
     * @return value
     */
    public boolean hasKey(String key, String hashKey) {
        try {
            return redisTemplate.opsForHash().hasKey(key, hashKey);
        } catch (Exception e) {
            LOGGER.warn("RedisTemplate hash hasKey error : {}", e);
        }
        return false;
    }

    /** 
     * 写入List缓存 
     * @param key 不能为null
     * @param value 值 
     * @return 
     */
    public boolean push(final String key, final Object value) {
        try {
            redisTemplate.expire(key, redisProperties.getExpiration(), TimeUnit.SECONDS);
            redisTemplate.opsForList().rightPush(key, value);
            return true;
        } catch (Exception e) {
            LOGGER.warn("RedisTemplate list rightPush error : {}", e);
            return false;
        }
    }

    /** 
     * 读取List缓存 
     * @param key 不能为null
     * @param start 开始 
     * @param end 结束  0 到 -1代表所有值 
     * @return value
     */
    public List<Object> range(final String key, final long start, final long end) {
        try {
            return redisTemplate.opsForList().range(key, start, end);
        } catch (Exception e) {
            LOGGER.warn("RedisTemplate list range error : {}", e);
            return null;
        }
    }
    
    /**
     * 向redis里存入数据和设置缓存时间  
     * @param key
     * @param obj
     * @param timeout
     */
	public <T> void add(String key, T obj, int timeout) {
		try {
			redisTemplate.opsForValue().set(key, obj, timeout, TimeUnit.SECONDS);
        } catch (Exception e) {
            LOGGER.warn("RedisTemplate add error : {}", e);
        }
	}
	
	/**
	 * 根据key获取缓存中的val
	 * @param key
	 * @return
	 */
	public Object getByKey(String key) {
		try {
			return redisTemplate.opsForValue().get(key);
        } catch (Exception e) {
            LOGGER.warn("RedisTemplate getByKey error : {}", e);
        }
		return null;
	}

}
