package com.aspire.ums.cmdb.v3.redis.web;

import com.aspire.ums.cmdb.redis.ICmdbRedisAPI;
import com.aspire.ums.cmdb.v3.redis.service.IRedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: CmdbRedisController
 * Author:   hangfang
 * Date:     2020/11/23
 * Description: CMDB redis 刷新控制
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@RestController
public class CmdbRedisController implements ICmdbRedisAPI {

    @Autowired
    private IRedisService redisService;

    @Override
    public Map<String, Object> refresh(@RequestParam("type") String redisType,
                                       @RequestParam("key") String redisKey) {
        Map<String, Object> result = new HashMap<>();
        if (redisService.syncRefresh(redisType, redisKey)) {
            result.put("success", true);
            result.put("message", "刷新成功");
        } else {
            result.put("success", false);
            result.put("message", "刷新失败");
        }
        return result;
    }
}
