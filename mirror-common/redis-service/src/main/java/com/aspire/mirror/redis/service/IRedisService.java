package com.aspire.mirror.redis.service;

import com.aspire.mirror.redis.api.payload.KeyValueRedisEntity;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: IRedisService
 * Author:   zhu.juwang
 * Date:     2019/12/17 17:28
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
public interface IRedisService {
    /**
     * 写入缓存
     * @param keyValueRedisEntity .key redis缓存key
     * @param keyValueRedisEntity .value redis缓存值
     * @return
     */
    boolean set(KeyValueRedisEntity keyValueRedisEntity);

    /**
     * 批量删除对应的value
     * @param keyList redis的key, 可传多个
     */
    boolean remove(List<String> keyList);

    /**
     * 批量删除对应的value
     * @param keyPattern key规则
     */
    boolean removePattern(String keyPattern);

    /**
     * 删除对应的value
     * @param key redis key值
     */
    boolean remove(String key);

    /**
     * 判断缓存中是否有对应的value
     * @param key redis key
     * @return
     */
    boolean exists(String key);

    /**
     * 读取缓存数据
     * @param key redis key
     * @return
     */
    Object get(String key);

    /**
     * 增加hash结构的redis数据
     * @param key redis key
     * @param hashKey hash值key
     * @param value 数据值
     */
    void hashSet(String key, Object hashKey, Object value);

    /**
     * 获取hash结构的redis数据
     * @param key redis key
     * @param hashKey hash值key
     * @return
     */
    Object hashGet(String key, Object hashKey);

    /**
     * 增加列表结构的redis数据
     * @param key redis key值
     * @param value redis value
     */
    void listSet(String key, Object value);

    /**
     * 获取列表结构的redis数据
     * @param key redis key值
     * @param start 开始位置
     * @param end 结束位置, -1时 为最后一个位置
     * @return
     */
    List<Object> listGet(String key, long start, long end);


    Set<Object> collectionGet(String key);

    void arrayListSet(String key, Object value, double scoure);

    Set<Object> arrayListGet(String key, double source, double source1);
}
