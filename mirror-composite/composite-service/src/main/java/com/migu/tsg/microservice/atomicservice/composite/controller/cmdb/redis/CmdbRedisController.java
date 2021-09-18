package com.migu.tsg.microservice.atomicservice.composite.controller.cmdb.redis;

import com.aspire.mirror.composite.service.cmdb.redis.ICmdbRedisAPI;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb.redis.CmdbRedisClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: CmdbRedisController
 * Author:   hangfang
 * Date:     2020/11/23
 * Description: DESCRIPTION
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@RefreshScope
@RestController
@Slf4j
public class CmdbRedisController implements ICmdbRedisAPI {

    @Autowired
    private CmdbRedisClient redisClient;
    @Override
    public Map<String, Object> refresh(@RequestParam("type") String redisType,
                                       @RequestParam("key") String redisKey) {
        return redisClient.refresh(redisType, redisKey);
    }
}
