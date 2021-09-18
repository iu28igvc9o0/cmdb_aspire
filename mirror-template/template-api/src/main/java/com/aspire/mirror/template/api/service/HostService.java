package com.aspire.mirror.template.api.service;

import com.aspire.mirror.template.api.dto.GeneralResponse;
import com.aspire.mirror.template.api.dto.HostBatchCreateRequest;
import com.aspire.mirror.template.api.dto.ItemsBatchCreateRequest;
import com.aspire.mirror.template.api.dto.ItemsCreateResponse;
import com.aspire.mirror.template.api.dto.vo.MirrorHostVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(value = "监控设备")
public interface HostService {
    /**
     * 批量创建主机信息
     *
     * @param request 主机批量创建请求对象
     * @return  监控项创建响应对象
     */
    @PostMapping(value = "/v1/hosts/batchCreate", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "创建自监控主机信息", notes = "创建自监控主机信息", response = GeneralResponse.class, tags = {"Item API"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回", response = GeneralResponse.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = GeneralResponse.class)})
    GeneralResponse batchCreateHosts(@RequestBody HostBatchCreateRequest request);

    @GetMapping(value = "/v1/hosts/getHostByIpAndPool", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "获取自监控主机信息", notes = "获取自监控主机信息", response = MirrorHostVO.class, tags = {"Item API"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回", response = MirrorHostVO.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = MirrorHostVO.class)})
    MirrorHostVO getHostByIpAndPool(@RequestParam("ip") String ip, @RequestParam("pool") String pool);

    @GetMapping(value = "/v1/hosts/getMonitorHostByProxyIdentity/{proxyIdentity}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "获取自监控主机信息", notes = "获取自监控主机信息", response = MirrorHostVO.class, tags = {"Item API"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回", response = MirrorHostVO.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = MirrorHostVO.class)})
    List<MirrorHostVO> getMonitorHostByProxyIdentity(@PathVariable("proxyIdentity") String proxyIdentity);
}
