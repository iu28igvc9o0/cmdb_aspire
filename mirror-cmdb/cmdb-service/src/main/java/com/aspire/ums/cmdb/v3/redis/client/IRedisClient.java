package com.aspire.ums.cmdb.v3.redis.client;

import org.springframework.cloud.netflix.feign.FeignClient;

import com.aspire.mirror.redis.api.IRedisAPI;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: CmdbApprovalESClient
 * Author:   hangfang
 * Date:     2019/9/18
 * Description: DESCRIPTION
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@FeignClient(name = "REDIS-SERVICE", url = "http://10.1.203.100:26379")
public interface IRedisClient extends IRedisAPI {

}
