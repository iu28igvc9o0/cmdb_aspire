package com.aspire.ums.cmdb.allocate.web;

import com.aspire.ums.cmdb.allocate.entity.AllocateIpConfigDetail;
import com.aspire.ums.cmdb.allocate.entity.AllocateIpConfigListReq;
import com.aspire.ums.cmdb.allocate.entity.AllocateIpConfigRes;
import com.aspire.ums.cmdb.allocate.entity.Result;
import com.aspire.ums.cmdb.allocate.service.AllocateIpConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/cmdb/allocateIpConfig")
public class AllocateIpConfigController {

    @Autowired
    private AllocateIpConfigService allocateIpConfigService;

    @GetMapping("/list")
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

    @PostMapping("/add")
    public String insertAllocateIpConfig(@RequestBody List<AllocateIpConfigDetail> request, @RequestParam("name") String name){
        return allocateIpConfigService.insertAllocateIpConfig(request,name);
    }

    @DeleteMapping("/delete")
    public String deleteAllocateIpConfigById(@RequestParam("ids") String ids, @RequestParam("name") String name){
        return allocateIpConfigService.deleteAllocateIpConfigById(ids, name);
    }

    @RequestMapping(value = "/export", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Map<String, Object>> exportAllocateIpConfig(@RequestBody AllocateIpConfigListReq request) {
        return allocateIpConfigService.exportAllocateIpConfig(request);
    }

    @GetMapping("/getVpnData")
    public List<Map<String, Object>> getVpnData() {
        return allocateIpConfigService.getVpnData();
    }

    @GetMapping("/getNetworkById")
    public List<Map<String, Object>> getNetworkById(@RequestParam long id) {
        return allocateIpConfigService.getNetworkById(id);
    }

}
