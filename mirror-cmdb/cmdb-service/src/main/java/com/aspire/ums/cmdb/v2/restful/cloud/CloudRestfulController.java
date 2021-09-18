package com.aspire.ums.cmdb.v2.restful.cloud;

import com.aspire.ums.cmdb.common.CloudResult;
import com.aspire.ums.cmdb.restful.cloud.ICloudRestfulAPI;
import com.aspire.ums.cmdb.v2.restful.service.ICloudRestfulService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: CloudRestfulController
 * Author:   hangfang
 * Date:     2021/1/13
 * Description: DESCRIPTION
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@RestController
@Slf4j
public class CloudRestfulController implements ICloudRestfulAPI {

    @Autowired
    private ICloudRestfulService cloudRestfulService;

    @Override
    public CloudResult<Map<String, Object>> getCRCfgZYC(@RequestBody Map<String, Object> params) {
        return cloudRestfulService.getCRCfgZYC(params);
    }

    @Override
    public CloudResult<Map<String, Object>> getCRCfgFWQ(@RequestBody Map<String, Object> params) {
        return cloudRestfulService.getCRCfgFWQ(params);
    }

    @Override
    public CloudResult<Map<String, Object>> getCRCfgXNJ(@RequestBody Map<String, Object> params) {
        return cloudRestfulService.getCRCfgXNJ(params);
    }

    @Override
    public CloudResult<Map<String, Object>> getCRCfgKCC(@RequestBody Map<String, Object> params) {
        return cloudRestfulService.getCRCfgKCC(params);
    }

    @Override
    public CloudResult<Map<String, Object>> getCRCfgLJS(@RequestBody Map<String, Object> params) {
        return cloudRestfulService.getCRCfgLJS(params);
    }

    @Override
    public CloudResult<Map<String, Object>> getCRCBIZ(@RequestBody Map<String, Object> params) {
        return cloudRestfulService.getCRCBIZ(params);
    }
}
