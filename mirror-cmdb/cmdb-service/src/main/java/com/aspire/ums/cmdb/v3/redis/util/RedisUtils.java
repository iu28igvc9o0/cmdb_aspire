package com.aspire.ums.cmdb.v3.redis.util;

import com.aspire.mirror.redis.api.payload.KeyValueRedisEntity;
import com.aspire.ums.cmdb.util.SpringUtils;
import com.aspire.ums.cmdb.v3.redis.client.IRedisClient;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.concurrent.TimeUnit;

/**
 * Copyright (C), 2015-2020, 卓望数码有限公司
 * FileName: RedisUtils
 * Author:   zhu.juwang
 * Date:     2020/1/20 18:06
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
public class RedisUtils<T> {
    //转化对象
    private static ObjectMapper objectMapper = new ObjectMapper();
    private static IRedisClient redisClient;

    public static void set(String key, Object value) {
        getRedisClient().set(new KeyValueRedisEntity(key, value, null, null));
    }

    public static void set(String key, Object value, Long expireTime, TimeUnit timeUnit) {
        getRedisClient().set(new KeyValueRedisEntity(key, value, expireTime, timeUnit));
    }

    public T get(String key) {
        Object object = getRedisClient().get(key);
        return (new ObjectMapper()).convertValue(object, new TypeReference<T>(){});
    }

    private static IRedisClient getRedisClient() {
        if (redisClient == null) {
            redisClient = SpringUtils.getBean(IRedisClient.class);
        }
        return redisClient;
    }



}
