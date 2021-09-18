package com.migu.tsg.microservice.atomicservice.composite.controller.cmdb.restful.cloud;

import com.aspire.mirror.composite.service.cmdb.restful.cloud.ICloudRestfulAPI;
import com.aspire.ums.cmdb.common.CloudResult;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb.restful.cloud.ICloudRestfulClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: CloudRestfulController
 * Author:   hangfang
 * Date:     2021/1/20
 * Description: DESCRIPTION
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@RestController
@Slf4j
public class CloudRestfulController implements ICloudRestfulAPI {

    @Autowired
    private ICloudRestfulClient cloudRestfulClient;

    @Override
    public CloudResult<Map<String, Object>> getCRCfgZYC(@RequestBody Map<String, Object> params) {
        return cloudRestfulClient.getCRCfgZYC(params);
    }

    @Override
    public CloudResult<Map<String, Object>> getCRCfgFWQ(@RequestBody Map<String, Object> params) {
        return cloudRestfulClient.getCRCfgFWQ(params);
    }

    @Override
    public CloudResult<Map<String, Object>> getCRCfgXNJ(@RequestBody Map<String, Object> params) {
        return cloudRestfulClient.getCRCfgXNJ(params);
    }

    @Override
    public CloudResult<Map<String, Object>> getCRCfgKCC(@RequestBody Map<String, Object> params) {
        return cloudRestfulClient.getCRCfgKCC(params);
    }

    @Override
    public CloudResult<Map<String, Object>> getCRCfgLJS(@RequestBody Map<String, Object> params) {
        return cloudRestfulClient.getCRCfgLJS(params);
    }

    @Override
    public CloudResult<Map<String, Object>> getCRCBIZ(@RequestBody Map<String, Object> params) {
        return cloudRestfulClient.getCRCBIZ(params);
    }
}
