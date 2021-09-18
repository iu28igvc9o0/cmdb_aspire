package com.aspire.mirror.composite.service.cmdb.restful.ipCollect;

import com.aspire.ums.cmdb.common.Result;
import com.aspire.ums.cmdb.common.ResultVo;
import com.aspire.ums.cmdb.ipCollect.payload.entity.CmdbIpClashCreateRequest;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @Author: fanshenquan
 * @Datetime: 2020/5/29 18:13
 */
@RequestMapping(value = "${version}/cmdb/restful/ipClash")
public interface IIpClashRestfulAPI {

    @RequestMapping(value = "/createIpClash", method = RequestMethod.POST)
    ResultVo createIpClash(@RequestBody Result<CmdbIpClashCreateRequest> request);
}
