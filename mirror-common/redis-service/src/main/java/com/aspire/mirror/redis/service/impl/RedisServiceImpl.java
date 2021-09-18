package com.aspire.mirror.redis.service.impl;

import com.aspire.mirror.redis.api.payload.KeyValueRedisEntity;
import com.aspire.mirror.redis.service.IRedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: RedisServiceImpl
 * Author:   zhu.juwang
 * Date:     2019/12/17 17:27
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Service
@Slf4j
public class RedisServiceImpl implements IRedisService {
    @Autowired
    private RedisTemplate redisTemplate;
    /**
     * 写入缓存设置时效时间
     * @param keyValueRedisEntity key redis缓存key
     * @param keyValueRedisEntity value redis缓存值
     * @param keyValueRedisEntity expireTime redis缓存时间
     * @param keyValueRedisEntity timeUnit redis缓存时间 单位
     * @return
     */
    @Override
    public boolean set(KeyValueRedisEntity keyValueRedisEntity) {
        boolean result = false;
        try {
            ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
            operations.set(keyValueRedisEntity.getKey(), keyValueRedisEntity.getValue());
            if (keyValueRedisEntity.getExpireTime() != null) {
                TimeUnit timeUnit = TimeUnit.SECONDS;
                if (keyValueRedisEntity.getTimeUnit() == null) {
                    timeUnit = keyValueRedisEntity.getTimeUnit();
                }
                redisTemplate.expire(keyValueRedisEntity.getKey(), keyValueRedisEntity.getExpireTime(), timeUnit);
            }
            result = true;
        } catch (Exception e) {
            log.error("Write to redis error, Cause : {}", e.getMessage(), e);
        }
        log.info("Redis set result -> {}", result);
        return result;
    }

    /**
     * 批量删除对应的value
     * @param keyList
     */
    @Override
    public boolean remove(List<String> keyList) {
        boolean result = false;
        try {
            if (keyList != null) {
                for (String key : keyList) {
                    remove(key);
                }
            }
            result = true;
        } catch (Exception e) {
            log.error("Write to redis error, Cause : {}", e.getMessage(), e);
        }
        log.info("Redis remove result -> {}", result);
        return result;
    }

    /**
     * 批量删除key
     * @param keyPattern key规则
     */
    @Override
    public boolean removePattern(final String keyPattern) {
        boolean result = false;
        try {
            Set<Serializable> keys = redisTemplate.keys(keyPattern + "*");
            if (keys.size() > 0){
                redisTemplate.delete(keys);
            }
            result = true;
        } catch (Exception e) {
            log.error("Write to redis error, Cause : {}", e.getMessage(), e);
        }
        log.info("Redis removePattern result -> {}", result);
        return result;
    }

    /**
     * 删除对应的value
     * @param key
     */
    @Override
    public boolean remove(final String key) {
        boolean result = false;
        try {
            if (exists(key)) {
                redisTemplate.delete(key);
            }
            result = true;
        } catch (Exception e) {
            log.error("Write to redis error, Cause : {}", e.getMessage(), e);
        }
        log.info("Redis remove result -> {}", result);
        return result;
    }

    /**
     * 判断缓存中是否有对应的value
     * @param key
     * @return
     */
    @Override
    public boolean exists(final String key) {
        return redisTemplate.hasKey(key);
    }

    /**
     * 读取缓存
     * @param key
     * @return
     */
    @Override
    public Object get(final String key) {
        ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
        Object value = operations.get(key);
        log.info("Redis red result -> {}", value);
        return value;
    }
    /**
     * 哈希 添加
     * @param key
     * @param hashKey
     * @param value
     */
    @Override
    public void hashSet(String key, Object hashKey, Object value){
        HashOperations<String, Object, Object> hash = redisTemplate.opsForHash();
        hash.put(key,hashKey,value);
    }
    /**
     * 哈希获取数据
     * @param key
     * @param hashKey
     * @return
     */
    @Override
    public Object hashGet(String key, Object hashKey){
        HashOperations<String, Object, Object>  hash = redisTemplate.opsForHash();
        return hash.get(key,hashKey);
    }
    /**
     * 列表添加
     * @param k
     * @param v
     */
    @Override
    public void listSet(String k, Object v){
        ListOperations<String, Object> list = redisTemplate.opsForList();
        list.rightPush(k,v);
    }
    /**
     * 列表获取
     * @param k
     * @param l
     * @param l1
     * @return
     */
    @Override
    public List<Object> listGet(String k, long l, long l1){
        ListOperations<String, Object> list = redisTemplate.opsForList();
        return list.range(k,l,l1);
    }
    /**
     * 集合添加
     * @param key
     * @param value
     */
    public void collectionSet(String key,Object value){
        SetOperations<String, Object> set = redisTemplate.opsForSet();
        set.add(key,value);
    }
    /**
     * 集合获取
     * @param key
     * @return
     */
    @Override
    public Set<Object> collectionGet(String key){
        SetOperations<String, Object> set = redisTemplate.opsForSet();
        return set.members(key);
    }
    /**
     * 有序集合添加
     * @param key
     * @param value
     * @param source
     */
    @Override
    public void arrayListSet(String key, Object value, double source){
        ZSetOperations<String, Object> zset = redisTemplate.opsForZSet();
        zset.add(key,value,source);
    }
    /**
     * 有序集合获取
     * @param key
     * @param source
     * @param source1
     * @return
     */
    @Override
    public Set<Object> arrayListGet(String key, double source, double source1){
        ZSetOperations<String, Object> zset = redisTemplate.opsForZSet();
        return zset.rangeByScore(key, source, source1);
    }
}
