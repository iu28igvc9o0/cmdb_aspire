package com.aspire.mirror.composite.service.cmdb;

import com.aspire.mirror.composite.service.inspection.payload.CompAllocateIpConfigDetail;
import com.aspire.mirror.composite.service.inspection.payload.CompAllocateIpConfigListReq;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Api(value = "IP分配管理配置")
@RequestMapping("${version}/cmdb/allocateIp")
public interface ICompAllocateIpConfigService {

    /**
     * 查询IP分配管理配置列表
     */
    @GetMapping(value = "/list")
    @ApiOperation(value = "查询IP分配管理配置列表", notes = "查询IP分配管理配置列表", tags = {"CMDB AllocateIpConfig API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功"),
                           @ApiResponse(code = 500, message = "内部错误")})
    Object getAllocateIpConfigData(@RequestParam(value = "pageNum",required = false) int pageNum,
                                                        @RequestParam(value = "startPageNum",required = false) int startPageNum,
                                                        @RequestParam(value = "pageSize",required = false) int pageSize,
                                                        @RequestParam(value = "vpnId",required = false) int vpnId,
                                                        @RequestParam(value = "networkId",required = false) int networkId,
                                                        @RequestParam(value = "bizSystem",required = false) String bizSystem,
                                                        @RequestParam(value = "ip",required = false) String ip,
                                                        @RequestParam(value = "privateIp",required = false) String privateIp,
                                                        @RequestParam(value = "isAdd",required = false) boolean isAdd);

    /**
     * 添加IP分配管理配置
     */
    @PostMapping(value = "/add")
    @ApiOperation(value = "添加IP分配管理配置", notes = "添加IP分配管理配置", tags = {"CMDB AllocateIpConfig API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功"),
            @ApiResponse(code = 500, message = "内部错误")})
    String insertAllocateIpConfig(@RequestBody CompAllocateIpConfigDetail request);

    /**
     * 删除IP分配管理配置
     */
    @DeleteMapping(value = "/delete")
    @ApiOperation(value = "删除IP分配管理配置", notes = "删除IP分配管理配置", tags = {"CMDB AllocateIpConfig API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功"),
            @ApiResponse(code = 500, message = "内部错误")})
    String deleteAllocateIpConfigById(@RequestParam("ids") String ids);

    /**
     * 导出IP分配管理配置
     */
    @RequestMapping(value = "/export", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "导出IP分配管理配置", notes = "导出IP分配管理配置", tags = {"CMDB AllocateIpConfig API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功"),
            @ApiResponse(code = 500, message = "内部错误")})
    void exportAllocateIpConfig(@RequestBody CompAllocateIpConfigListReq request);

    /**
     * 获取域名
     */
    @GetMapping(value = "/getVpnData")
    @ApiOperation(value = "获取域名", notes = "获取域名", tags = {"CMDB AllocateIpConfig API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功"),
            @ApiResponse(code = 500, message = "内部错误")})
    List<Map<String, Object>> getVpnData();

    /**
     * 获取域名
     */
    @GetMapping(value = "/getNetworkById")
    @ApiOperation(value = "获取网段", notes = "获取网段", tags = {"CMDB AllocateIpConfig API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功"),
            @ApiResponse(code = 500, message = "内部错误")})
    List<Map<String, Object>> getNetworkById(@RequestParam long id);

}
