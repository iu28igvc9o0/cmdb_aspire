package com.aspire.ums.cmdb.v3.redis.service;

import java.util.concurrent.TimeUnit;

/**
 * Copyright (C), 2015-2020, 卓望数码有限公司
 * FileName: IRedisService
 * Author:   zhu.juwang
 * Date:     2020/1/20 17:23
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
public interface IRedisService {
    /**
     * 缓存redis
     * @param key key
     * @param value 值
     */
    boolean set(String key, Object value);

    /**
     * 缓存redis
     * @param key key
     * @param value 值
     * @param expireTime 失效时长
     * @param timeUnit 单位
     */
    boolean set(String key, Object value, Long expireTime, TimeUnit timeUnit);

    /**
     * 获取redis值
     * @param key key
     * @return
     */
    Object get(String key);

    /**
     * 同步移除redis缓存
     * @param key
     */
    boolean syncRemove(String key);

    /**
     * 按照REDIS KEY规则删除缓存
     * @param key
     * @return
     */
    boolean syncRemovePattern(String key);

    /**
     * 异步移除redis缓存
     * @param key
     */
    void asyncRemove(String key);

    /**
     * 同步刷新REDIS缓存
     * @param redisType REDIS类型
     * @param object 更新信息
     * @return
     */
    boolean syncRefresh(final String redisType, final Object object);

    /**
     * 异步刷新REDIS缓存
     * @param redisType REDIS类型
     * @param object 更新信息
     * @return
     */
    void asyncRefresh(final String redisType, final Object object);
}
