package com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb.networkcard;

import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.alibaba.fastjson.JSONObject;
import com.aspire.ums.cmdb.networkCard.payload.CmdbInstanceNetworkCard;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: NetworkCardProgramClient
 * Author:   luowenbo
 * Date:     2019/09/20 10:50
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@FeignClient(value = "CMDB")
public interface NetworkCardProgramClient {
    /*
     *  新增网卡信息
     *  @return 是否成功JSON
     * */
    @PostMapping(value = "/cmdb/networkCard/save" )
    JSONObject insertNetwordCard(@RequestBody CmdbInstanceNetworkCard cmdbInstanceNetworkCard);

    /*
     *  删除网卡信息
     *  @return 是否成功JSON
     * */
    @DeleteMapping(value = "/cmdb/networkCard/delete" )
    JSONObject deleteNetwordCardById(@RequestParam("id") String id);

    /*
     *  修改网卡信息
     *  @return 是否成功JSON
     * */
    @PostMapping(value = "/cmdb/networkCard/update" )
    JSONObject updateNetwordCard(@RequestBody CmdbInstanceNetworkCard cmdbInstanceNetworkCard);

    /*
     *  查询网卡信息
     *  @return 是否成功JSON
     * */
    @GetMapping(value = "/cmdb/networkCard/getByInstanceId" )
    List<CmdbInstanceNetworkCard> getNetwordCardByInstanceId(@RequestParam("instanceId") String instanceId);

    /*
     *  根据网卡名称查询网卡信息
     *  @return 是否成功JSON
     * */
    @GetMapping(value = "/cmdb/networkCard/getByName" )
    CmdbInstanceNetworkCard getNetwordCardByName(@RequestParam("name") String name);
}
