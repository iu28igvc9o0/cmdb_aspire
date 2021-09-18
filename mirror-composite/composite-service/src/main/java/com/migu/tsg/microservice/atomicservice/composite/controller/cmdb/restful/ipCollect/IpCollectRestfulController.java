package com.migu.tsg.microservice.atomicservice.composite.controller.cmdb.restful.ipCollect;

import com.aspire.mirror.composite.service.cmdb.restful.ipCollect.IIpCollectRestfulAPI;
import com.aspire.ums.cmdb.common.ResultVo;
import com.aspire.ums.cmdb.ipCollect.payload.vmware.InstanceCreateRequest;
import com.aspire.ums.cmdb.ipCollect.payload.vmware.InstanceDeleteRequest;
import com.aspire.ums.cmdb.ipCollect.payload.vmware.InstanceUpdateRequest;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb.ipCollect.CmdbIpCollectClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 存活IP、虚拟IP采集接口实现类
 *
 * @Author: fanshenquan
 * @Datetime: 2020/5/30 10:45
 */
@RestController
public class IpCollectRestfulController implements IIpCollectRestfulAPI {
    @Autowired
    private CmdbIpCollectClient cmdbIpCollectClient;

    @Override
    public ResultVo create(@RequestBody InstanceCreateRequest instanceCreateRequest) {
        return cmdbIpCollectClient.create(instanceCreateRequest);
    }

    @Override
    public ResultVo update(@RequestBody InstanceUpdateRequest instanceUpdateRequest) {
        return cmdbIpCollectClient.update(instanceUpdateRequest);
    }

    @Override
    public ResultVo delete(@RequestBody InstanceDeleteRequest instanceDeleteRequest) {
        return cmdbIpCollectClient.delete(instanceDeleteRequest);
    }
}
