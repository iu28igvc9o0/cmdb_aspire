package com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb.assessment;

import java.util.List;
import java.util.Map;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.aspire.ums.cmdb.assessment.payload.CmdbDeviceProduce;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: CmdbDeviceProduceClient
 * Author:   hangfang
 * Date:     2019/6/26
 * Description: DESCRIPTION
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@FeignClient(value = "CMDB")
public interface CmdbDeviceProduceClient {

    /**
     * 查询所有厂家信息
     * @return 所有厂家信息
     */
    @RequestMapping(value = "/cmdb/device/produce/list", method = RequestMethod.GET)
    List<CmdbDeviceProduce> list();


    /**
     * 存储厂家信息
     */
    @RequestMapping(value = "/cmdb/device/produce/save", method = RequestMethod.POST)
    Map<String, Object> save(@RequestBody List<CmdbDeviceProduce> deviceProduce);
}
