package com.aspire.mirror.composite.service.cmdb.restful.ipCollect;

import com.aspire.ums.cmdb.common.ResultVo;
import com.aspire.ums.cmdb.ipCollect.payload.vmware.InstanceCreateRequest;
import com.aspire.ums.cmdb.ipCollect.payload.vmware.InstanceDeleteRequest;
import com.aspire.ums.cmdb.ipCollect.payload.vmware.InstanceUpdateRequest;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * 存活IP、虚拟IP采集接口
 * @Author: fanshenquan
 * @Datetime: 2020/5/30 10:41
 */
@RequestMapping(value = "${version}/cmdb/restful/ipCollect")
public interface IIpCollectRestfulAPI {

    @RequestMapping(value = "/instance/create", method = RequestMethod.POST)
    ResultVo create(@RequestBody InstanceCreateRequest instanceCreateRequest);

    @RequestMapping(value = "/instance/update", method = RequestMethod.POST)
    ResultVo update(@RequestBody InstanceUpdateRequest instanceUpdateRequest);

    @RequestMapping(value = "/instance/delete", method = RequestMethod.POST)
    ResultVo delete(@RequestBody InstanceDeleteRequest instanceDeleteRequest);
}
