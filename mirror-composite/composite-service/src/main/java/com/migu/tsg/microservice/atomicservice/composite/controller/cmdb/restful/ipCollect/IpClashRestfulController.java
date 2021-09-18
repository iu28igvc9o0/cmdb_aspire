package com.migu.tsg.microservice.atomicservice.composite.controller.cmdb.restful.ipCollect;

import com.aspire.mirror.composite.service.cmdb.restful.ipCollect.IIpClashRestfulAPI;
import com.aspire.ums.cmdb.common.Result;
import com.aspire.ums.cmdb.common.ResultVo;
import com.aspire.ums.cmdb.ipCollect.payload.entity.CmdbIpClashCreateRequest;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb.ipCollect.CmdbIpClashClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: fanshenquan
 * @Datetime: 2020/5/30 10:54
 */
@Slf4j
@RestController
public class IpClashRestfulController implements IIpClashRestfulAPI {
    @Autowired
    private CmdbIpClashClient cmdbIpClashClient;

    @Override
    public ResultVo createIpClash(@RequestBody Result<CmdbIpClashCreateRequest> request) {
        log.info("createIpClash request is {}", request);
        return cmdbIpClashClient.createIpClash(request);
    }
}
