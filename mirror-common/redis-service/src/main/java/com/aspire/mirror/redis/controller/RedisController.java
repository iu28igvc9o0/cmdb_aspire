package com.aspire.mirror.redis.controller;

import com.aspire.mirror.redis.api.IRedisAPI;
import com.aspire.mirror.redis.api.payload.KeyValueRedisEntity;
import com.aspire.mirror.redis.service.IRedisService;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: RedisController
 * Author:   zhu.juwang
 * Date:     2019/12/17 20:08
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@RestController
@Slf4j
public class RedisController implements IRedisAPI {

    @Autowired
    private IRedisService redisService;

    @Override
    public boolean set(@RequestBody KeyValueRedisEntity keyValueRedisEntity) {
        log.info("Redis write -> {}", JSONObject.fromObject(keyValueRedisEntity));
        return redisService.set(keyValueRedisEntity);
    }

    @Override
    public boolean remove(@RequestBody List<String> keyList) {
        log.info("Redis remove -> {}", JSONArray.fromObject(keyList));
        return redisService.remove(keyList);
    }

    @Override
    public boolean removePattern(@RequestParam("pattern") String keyPattern) {
        log.info("Redis remove -> {}", keyPattern);
        return redisService.removePattern(keyPattern);
    }

    @Override
    public boolean remove(@RequestParam("key") String key) {
        log.info("Redis remove -> {}", key);
        return redisService.remove(key);
    }

    @Override
    public boolean exists(@RequestParam("key") String key) {
        return redisService.exists(key);
    }

    @Override
    public Object get(@RequestParam("key") String key) {
        log.info("Redis read -> {}", key);
        return redisService.get(key);
    }
}
