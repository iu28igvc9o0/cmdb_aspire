package com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb.assessment;

import java.util.Map;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.alibaba.fastjson.JSONObject;
import com.aspire.ums.cmdb.assessment.payload.CmdbDeviceRepairEvent;
import com.aspire.ums.cmdb.common.Result;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: CmdbDeviceRepairEventClient
 * Author:   hangfang
 * Date:     2019/6/25
 * Description: DESCRIPTION
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */

@FeignClient(value = "CMDB")
public interface CmdbDeviceRepairEventClient {

    /**
     * 查询所有设备维修事件
     * @return 设备量
     */
    @RequestMapping(value = "/cmdb/device/repair/list", method = RequestMethod.POST)
    Result<CmdbDeviceRepairEvent> list(@RequestParam(value = "pageNum", required = false) Integer pageNum,
                                       @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                       @RequestBody CmdbDeviceRepairEvent deviceRepairEvent);


    /**
     * 存储设备维修事件
     */
    @RequestMapping(value = "/cmdb/device/repair/save", method = RequestMethod.POST)
    Map<String, Object> save(@RequestBody JSONObject data);

    /**
     * 存储设备维修事件
     */
    @RequestMapping(value = "/cmdb/device/repair/delete/{id}", method = RequestMethod.DELETE)
    Map<String, Object> delete(@PathVariable("id") String id);
}
