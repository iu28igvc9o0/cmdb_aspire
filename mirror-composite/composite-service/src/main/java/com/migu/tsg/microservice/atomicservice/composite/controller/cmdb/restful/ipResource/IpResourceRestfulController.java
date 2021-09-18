package com.migu.tsg.microservice.atomicservice.composite.controller.cmdb.restful.ipResource;

import com.aspire.mirror.composite.service.cmdb.restful.ipResource.IpResourceRestfulAPI;
import com.aspire.ums.cmdb.common.ResultVo;
import com.aspire.ums.cmdb.ipResource.payload.*;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb.restful.ipResource.CmdbIpResourceClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author fanwenhui
 * @date 2020-06-16 14:12
 * @description
 */
@Slf4j
@RestController
public class IpResourceRestfulController implements IpResourceRestfulAPI {
    @Autowired
    private CmdbIpResourceClient ipResourceClient;

    @Override
    public ResultVo getDropDownBoxList(@RequestParam(value = "queryType") String queryType) {
        return ipResourceClient.getDropDownBoxList(queryType);
    }

    @Override
    public ResultVo getComboboxList(@PathVariable(value = "path") String path, @RequestParam(value = "pid", required = false) String pid) {
        return ipResourceClient.getComboboxList(path, pid);
    }

    @Override
    public ResultVo getIpListByCount(@RequestBody AutoAllocateIpParam param) {
        return ipResourceClient.getIpListByCount(param);
    }

    @Override
    public ResultVo getSegmentInfoList(@RequestBody SegmentInfoParam param) {
        return ipResourceClient.getSegmentInfoList(param);
    }

    @Override
    public ResultVo getIpInfoInfoList(@RequestBody IpInfoParam param) {
        return ipResourceClient.getIpInfoInfoList(param);
    }

    @Override
    public ResultVo getAssetInfoList(@RequestBody AssetInfoParam param) {
        return ipResourceClient.getAssetInfoList(param);
    }

    @Override
    public ResultVo getAssetIpList(@RequestBody AssetIpInfoParam param) {
        return ipResourceClient.getAssetIpList(param);
    }

    @Override
    public ResultVo getChangeIpList(@RequestBody SegmentIpInfoParam param) {
        return ipResourceClient.getChangeIpList(param);
    }

    @Override
    public ResultVo updateIpInfo(@RequestBody IpInfoUpdateParam param) {
        return ipResourceClient.updateIpInfo(param);
    }

    @Override
    public ResultVo delAssetInfoById(@RequestBody Map<String, String> param) {
        return ipResourceClient.delAssetInfoById(param);
    }
}
