package com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb.cmic;

import java.util.Map;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Copyright (C), 2015-2020, 卓望数码有限公司
 * FileName: ICmdbIPAPI
 * Author:   zhu.juwang
 * Date:     2020/5/27 10:33
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@FeignClient(value = "CMDB")
public interface ICmdbIPClient {

    /**
     * 获取指定IP类型指定网段的IP地址使用情况
     * @param conditions 查询条件
     * @return
     */
    @RequestMapping(value = "/cmdb/cmic/ip/statisticsIpUseInfo", method = RequestMethod.POST)
    Map<String, Object> statisticsIpUseInfo(@RequestBody(required = false) Map<String, Object> conditions);
}
