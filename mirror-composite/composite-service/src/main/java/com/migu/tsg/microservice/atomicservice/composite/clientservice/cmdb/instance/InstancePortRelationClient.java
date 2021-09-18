package com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb.instance;

import java.util.List;
import java.util.Map;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.aspire.ums.cmdb.common.Result;
import com.aspire.ums.cmdb.instance.payload.CmdbInstancePortQuery;
import com.aspire.ums.cmdb.instance.payload.CmdbInstancePortRelation;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: InstancePortRelationClient
 * Author:   hangfang
 * Date:     2019/9/8
 * Description: DESCRIPTION
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@FeignClient(value = "CMDB")
public interface InstancePortRelationClient {

    /**
     * 根据资源池获取ci信息
     */
    @RequestMapping(value = "/cmdb/instancePortRelation/getInstanceIpByPool", method = RequestMethod.GET)
    List<Map<String, String>> getInstanceIpByPool(@RequestParam("pool") String pool);

    /**
     * 根据端口和id信息确认唯一
     */
    @RequestMapping(value = "/cmdb/instancePortRelation/selectByPortAndId", method = RequestMethod.POST)
    Boolean selectByPortAndId(@RequestBody CmdbInstancePortRelation portRelation);
    /**
     * 获取端口关联设备列表
     */
    @RequestMapping(value = "/cmdb/instancePortRelation/list", method = RequestMethod.POST)
    Result<CmdbInstancePortRelation> list(@RequestBody CmdbInstancePortQuery instancePortQuery);


    /**
     * 删除端口关联设备列表
     */
    @RequestMapping(value = "/cmdb/instancePortRelation/delete", method = RequestMethod.DELETE)
    Map<String, Object> delete(@RequestParam("id") String id);

    /**
     * 新增端口关联设备列表
     */
    @RequestMapping(value = "/cmdb/instancePortRelation/insert", method = RequestMethod.POST)
    Map<String, Object> insert(@RequestBody CmdbInstancePortRelation instancePortRelation);
}
