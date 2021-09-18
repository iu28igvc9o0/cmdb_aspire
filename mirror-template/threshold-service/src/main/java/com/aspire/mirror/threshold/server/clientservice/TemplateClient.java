package com.aspire.mirror.threshold.server.clientservice;

import com.aspire.mirror.threshold.server.domain.MirrorHostVO;
import com.aspire.mirror.threshold.server.domain.DynamicModelBatchCreateVO;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@FeignClient("template-service")
public interface TemplateClient {
    @GetMapping(value = "/v1/hosts/getHostByIpAndPool", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "获取自监控主机信息", notes = "获取自监控主机信息", response = MirrorHostVO.class, tags = {"Item API"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回", response = MirrorHostVO.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = MirrorHostVO.class)})
    MirrorHostVO getHostByIpAndPool(@RequestParam("ip") String ip, @RequestParam("pool") String pool);


    @PostMapping(value = "/v1/triggers/batchDynamicModelData", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "创建动态阈值数据", notes = "创建动态阈值数据", tags = {"/v1/triggers"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功"),
            @ApiResponse(code = 500, message = "内部错误")})
    Map<String, String> batchDynamicModelData(@RequestBody DynamicModelBatchCreateVO dynamicModelBatchCreateVO);
}
