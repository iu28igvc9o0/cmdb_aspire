package com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb.restful.common;

import java.util.Map;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.aspire.ums.cmdb.common.Result;
import com.aspire.ums.cmdb.restful.payload.StatisticRequestEntity;

/**
 * Copyright (C), 2015-2020, 卓望数码有限公司
 * FileName: ICommonRestfulClient
 * Author:   zhu.juwang
 * Date:     2020/4/13 17:18
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@FeignClient(value = "CMDB")
public interface ICommonRestfulClient {
    /**
     * 根据查询条件,获取主机列表
     * @param params 查询参数
     *   {"device_type": "设备类型", "idcType": "资源池名称"}
     */
    @RequestMapping(value = "/cmdb/restful/common/instance/list", method = RequestMethod.POST)
    Result<Map<String, Object>> getInstanceList(@RequestBody Map<String, Object> params);

    /**
     * 根据查询条件,获取主机列表
     * @param params 查询参数
     *   {"device_type": "设备类型", "idcType": "资源池名称"}
     */
    @RequestMapping(value = "/cmdb/restful/common/instance/detail", method = RequestMethod.POST)
    Map<String, Object> getInstanceDetail(@RequestBody Map<String, Object> params);

    /**
     * 统计资源设备
     * @param statisticRequestEntity 查询参数
     */
    @RequestMapping(value = "/cmdb/restful/common/instance/statistics", method = RequestMethod.POST)
    Object getInstanceStatistics(@RequestBody StatisticRequestEntity statisticRequestEntity);
}
