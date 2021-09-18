package com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb.restful.ipResource;

import java.util.Map;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.aspire.ums.cmdb.common.ResultVo;
import com.aspire.ums.cmdb.ipResource.payload.AssetInfoParam;
import com.aspire.ums.cmdb.ipResource.payload.AssetIpInfoParam;
import com.aspire.ums.cmdb.ipResource.payload.AutoAllocateIpParam;
import com.aspire.ums.cmdb.ipResource.payload.IpInfoParam;
import com.aspire.ums.cmdb.ipResource.payload.IpInfoUpdateParam;
import com.aspire.ums.cmdb.ipResource.payload.SegmentInfoParam;
import com.aspire.ums.cmdb.ipResource.payload.SegmentIpInfoParam;

/**
 * @Author: fanshenquan
 * @Datetime: 2020/6/28 18:13
 */
@FeignClient(value = "CMDB")
public interface CmdbIpResourceClient {

    @RequestMapping(value = "/cmdb/ipResource/condition/getDropDownBoxList", method = RequestMethod.POST)
    ResultVo getDropDownBoxList(@RequestParam(value = "queryType") String queryType);

    @RequestMapping(value = "/cmdb/ipResource/combobox/{path}", method = RequestMethod.POST)
    ResultVo getComboboxList(@PathVariable(value = "path") String path, @RequestParam(value = "pid", required = false) String pid);

    @RequestMapping(value = "/cmdb/ipResource/autoAllocate/getIpListByCount", method = RequestMethod.POST)
    ResultVo getIpListByCount(@RequestBody AutoAllocateIpParam param);

    @RequestMapping(value = "/cmdb/ipResource/list/getSegmentInfoList", method = RequestMethod.POST)
    ResultVo getSegmentInfoList(@RequestBody SegmentInfoParam param);

    @RequestMapping(value = "/cmdb/ipResource/list/getIpInfoInfoList", method = RequestMethod.POST)
    ResultVo getIpInfoInfoList(@RequestBody IpInfoParam param);

    @RequestMapping(value = "/cmdb/ipResource/list/getAssetInfoList", method = RequestMethod.POST)
    ResultVo getAssetInfoList(@RequestBody AssetInfoParam param);

    @RequestMapping(value = "/cmdb/ipResource/list/getAssetIpList", method = RequestMethod.POST)
    ResultVo getAssetIpList(@RequestBody AssetIpInfoParam param);

    @RequestMapping(value = "/cmdb/ipResource/list/getChangeIpList", method = RequestMethod.POST)
    ResultVo getChangeIpList(@RequestBody SegmentIpInfoParam param);

    @RequestMapping(value = "/cmdb/ipResource/update/updateIpInfo", method = RequestMethod.POST)
    ResultVo updateIpInfo(@RequestBody IpInfoUpdateParam param);

    @RequestMapping(value = "/cmdb/ipResource/update/delAssetInfoById", method = RequestMethod.POST)
    ResultVo delAssetInfoById(@RequestBody Map<String, String> param);

}
