package com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb.restful.automate;

import java.util.List;
import java.util.Map;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.aspire.ums.cmdb.instance.payload.CmdbInstance;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: IAutomateRestfulClient
 * Author:   zhu.juwang
 * Date:     2019/9/11 11:22
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@FeignClient(value = "CMDB")
public interface IAutomateRestfulClient {
    /**
     * 根据查询条件,获取主机列表
     * @param params 查询参数
     *   {"device_type": "设备类型", "idcType": "资源池名称"}
     */
    @PostMapping(value = "/cmdb/restful/automate/getInstanceList")
    List<CmdbInstance> getInstanceList(@RequestBody Map<String, Object> params);
}
