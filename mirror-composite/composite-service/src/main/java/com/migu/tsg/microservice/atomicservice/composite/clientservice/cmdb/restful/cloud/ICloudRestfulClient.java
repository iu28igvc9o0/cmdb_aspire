package com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb.restful.cloud;

import com.aspire.ums.cmdb.common.CloudResult;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: ICloudRestfulClient
 * Author:   hangfang
 * Date:     2021/1/20
 * Description: DESCRIPTION
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@FeignClient(value = "CMDB")
public interface ICloudRestfulClient {

    /**
     * 根据查询条件,获取资源池数据
     * @param params 查询参数
     *   {"begintime": "", "endtime": ""}
     */
    @RequestMapping(value = "/RESTForQW/getCRCfgZYC", method = RequestMethod.POST)
    CloudResult<Map<String, Object>> getCRCfgZYC(@RequestBody Map<String, Object> params);

    /**
     * 根据查询条件,获取服务器数据 设备类型为X86服务器
     * @param params 查询参数
     *   {"begintime": "", "endtime": ""}
     */
    @RequestMapping(value = "/RESTForQW/getCRCfgFWQ", method = RequestMethod.POST)
    CloudResult<Map<String, Object>> getCRCfgFWQ(@RequestBody Map<String, Object> params);
    /**
     * 根据查询条件,获取云主机数据
     * @param params 查询参数
     *   {"begintime": "", "endtime": ""}
     */
    @RequestMapping(value = "/RESTForQW/getCRCfgXNJ", method = RequestMethod.POST)
    CloudResult<Map<String, Object>> getCRCfgXNJ(@RequestBody Map<String, Object> params);
    /**
     * 根据查询条件,获取分布式块存储数据
     * @param params 查询参数
     *   {"begintime": "", "endtime": ""}
     */
    @RequestMapping(value = "/RESTForQW/getCRCfgKCC", method = RequestMethod.POST)
    CloudResult<Map<String, Object>> getCRCfgKCC(@RequestBody Map<String, Object> params);
    /**
     * 根据查询条件,获取裸金属服务器数据，设备类型为X86服务器并节点类型为计算节点
     * @param params 查询参数
     *   {"begintime": "", "endtime": ""}
     */
    @RequestMapping(value = "/RESTForQW/getCRCfgLJS", method = RequestMethod.POST)
    CloudResult<Map<String, Object>> getCRCfgLJS(@RequestBody Map<String, Object> params);
    /**
     * 根据查询条件,获取业务系统数据
     * @param params 查询参数
     *   {"begintime": "", "endtime": ""}
     */
    @RequestMapping(value = "/RESTForQW/getCRCBIZ", method = RequestMethod.POST)
    CloudResult<Map<String, Object>> getCRCBIZ(@RequestBody Map<String, Object> params);


}
