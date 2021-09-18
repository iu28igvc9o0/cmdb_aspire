package com.aspire.mirror.composite.service.alert;

import com.aspire.mirror.composite.service.alert.payload.ComIdcTypePhysicalReq;
import com.aspire.ums.cmdb.instance.payload.CmdbDeviceTypeCount;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Api("科管监控首页")
@RequestMapping("/${version}/alerts/kg/monitor/index")
public interface ICompKGMonitorIndexService {

    /**
     * 查询服务器数量
     */
    @RequestMapping(value = "/queryServiceCount", method = RequestMethod.GET)
    @ApiOperation(value = "查询服务器数量", notes = "查询服务器数量", tags = {"KG Monitor Index API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "获取成功", response = CmdbDeviceTypeCount.class),
            @ApiResponse(code = 500, message = "获取成功")})
    List<CmdbDeviceTypeCount> queryServiceCount();

    /**
     * 资源利用率
     */
    @RequestMapping(value = "/deviceUsedRate", method = RequestMethod.POST)
    @ApiOperation(value = "资源利用率", notes = "资源利用率", tags = {"KG Monitor Index API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "获取成功", response = Map.class),
            @ApiResponse(code = 500, message = "获取成功")})
    Map<String, Object> deviceUsedRate(@RequestBody Map<String,String> param);

    /**
     * 告警总览
     */
    @RequestMapping(value = "/getAlertView", method = RequestMethod.POST)
    @ApiOperation(value = "告警总览", notes = "告警总览", tags = {"KG Monitor Index API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = Map.class),
            @ApiResponse(code = 500, message = "内部错误")})
    List<Map<String, Object>> getAlertView(@RequestBody Map<String,Object> param);


    @PostMapping(value = "/bizSystemDeviceUsedRate", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "业务资源利用率", notes = "业务资源利用率", response = Map.class, tags = {"KG Monitor Index API"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回", response = Map.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = Map.class)})
    List<Map<String,Object>> bizSystemDeviceUsedRate(@RequestBody ComIdcTypePhysicalReq comIdcTypePhysicalReq) throws Exception;

    @PostMapping(value = "/deviceUsedRateTrends", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "资源利用率趋势图", notes = "资源利用率趋势图", response = Map.class, tags = {"KG Monitor Index API"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回", response = Map.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = Map.class)})
    Map<String, Object> deviceUsedRateTrends(@RequestBody ComIdcTypePhysicalReq comIdcTypePhysicalReq) throws Exception;

    @PostMapping(value = "/storageUseView", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "存储资源使用总览", notes = "存储资源使用总览", response = Map.class, tags = {"KG Monitor Index API"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回", response = Map.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = Map.class)})
    Map<String, Object> storageUseView(@RequestBody Map<String,String> param);


    @GetMapping(value = "/getSegmentUseList", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "获取网段地址", notes = "获取网段地址", response = Map.class, tags = {"KG Monitor Index API"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回", response = Map.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = Map.class)})
    List<Map<String, String>> getSegmentUseList();


    @PostMapping(value = "/storageUsedRateForKG", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "磁盘利用率", notes = "磁盘利用率", response = Map.class, tags = {"KG Monitor Index API"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回", response = Map.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = Map.class)})
    Map<String, Object> storageUsedRateForKG(@RequestBody Map<String,String> param);

}
