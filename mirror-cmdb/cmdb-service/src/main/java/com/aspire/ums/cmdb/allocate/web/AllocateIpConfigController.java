package com.aspire.ums.cmdb.allocate.web;

import com.aspire.ums.cmdb.allocate.IAllocateIpConfigAPI;
import com.aspire.ums.cmdb.allocate.payload.AllocateIpConfigDetail;
import com.aspire.ums.cmdb.allocate.payload.AllocateIpConfigListReq;
import com.aspire.ums.cmdb.allocate.payload.AllocateIpConfigRes;
import com.aspire.ums.cmdb.allocate.payload.Result;
import com.aspire.ums.cmdb.allocate.service.AllocateIpConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@Slf4j
public class AllocateIpConfigController implements IAllocateIpConfigAPI {

    @Autowired
    private AllocateIpConfigService allocateIpConfigService;

    public Result<AllocateIpConfigRes> getAllocateIpConfigData(@RequestParam(value = "pageNum",required = false) int pageNum,
                                                               @RequestParam(value = "startPageNum",required = false) int startPageNum,
                                                               @RequestParam(value = "pageSize",required = false) int pageSize,
                                                               @RequestParam(value = "vpnId",required = false) int vpnId,
                                                               @RequestParam(value = "networkId",required = false) int networkId,
                                                               @RequestParam(value = "bizSystem",required = false) String bizSystem,
                                                               @RequestParam(value = "ip",required = false) String ip,
                                                               @RequestParam(value = "privateIp",required = false) String privateIp,
                                                               @RequestParam(value = "isAdd",required = false) boolean isAdd) {
        return allocateIpConfigService.getAllocateIpConfigData(pageNum,startPageNum,pageSize,vpnId,networkId,bizSystem,ip,privateIp,isAdd);
    }

    public String insertAllocateIpConfig(@RequestBody List<AllocateIpConfigDetail> request, @RequestParam(value = "name") String name){
        return allocateIpConfigService.insertAllocateIpConfig(request,name);
    }

    public String deleteAllocateIpConfigById(@RequestParam(value = "ids") String ids, @RequestParam(value = "name") String name){
        return allocateIpConfigService.deleteAllocateIpConfigById(ids, name);
    }

    public List<Map<String, Object>> exportAllocateIpConfig(@RequestBody AllocateIpConfigListReq request) {
        return allocateIpConfigService.exportAllocateIpConfig(request);
    }

    public List<Map<String, Object>> getVpnData() {
        return allocateIpConfigService.getVpnData();
    }

    public List<Map<String, Object>> getNetworkById(@RequestParam(value = "id") long id) {
        return allocateIpConfigService.getNetworkById(id);
    }

}
