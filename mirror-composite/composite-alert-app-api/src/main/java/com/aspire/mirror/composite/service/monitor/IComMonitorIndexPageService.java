package com.aspire.mirror.composite.service.monitor;

import com.aspire.mirror.composite.payload.monitor.ComIdcTypePhysicalReq;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@Api(value = "告警统计")
@RequestMapping(value = "/${version}/alerts/indexPage")
public interface IComMonitorIndexPageService {

    /*****************************************监控首页************************************************************/
    @PostMapping(value = "/idcTypeDeviceUsedRate", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "资源池-资源利用率", notes = "告警类型分布查询", response = Map.class, tags = {"AlertIndexPage API"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回", response = Map.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = Map.class)})
    Map<String,Map<String,Long>> idcTypeDeviceUsedRate(@RequestBody ComIdcTypePhysicalReq comIdcTypePhysicalReq) throws Exception;



    @PostMapping(value = "/bizSystemDeviceUsedRate", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "租户-资源利用率分布", notes = "告警类型分布查询", response = Map.class, tags = {"AlertIndexPage API"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回", response = Map.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = Map.class)})
    Map<String,Map<String,Long>> bizSystemDeviceUsedRate(@RequestBody ComIdcTypePhysicalReq comIdcTypePhysicalReq) throws Exception;



    @PostMapping(value = "/deviceUsedRateTrends", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "资源利用率趋势图", notes = "告警类型分布查询", response = Map.class, tags = {"AlertIndexPage API"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回", response = Map.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = Map.class)})
	Map<String, Object> deviceUsedRateTrends(@RequestBody ComIdcTypePhysicalReq comIdcTypePhysicalReq) throws Exception;



    @PostMapping(value = "/bizSystemDeviceUsedRateLow", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "租户top5最低排序", notes = "告警类型分布查询", response = Map.class, tags = {"AlertIndexPage API"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回", response = Map.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = Map.class)})
    List<Map<String,Object>> bizSystemDeviceUsedRateLow(@RequestBody ComIdcTypePhysicalReq comIdcTypePhysicalReq) throws Exception;



    @GetMapping(value = "/bizSystemDeviceUsedRateTopN", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "首页业务系统topN", notes = "告警类型分布查询", response = Map.class, tags = {"AlertIndexPage API"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回", response = Map.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = Map.class)})
	Map<String, Map<String, Long>> bizSystemDeviceUsedRateTopN(
			@RequestParam(value = "startDate",required=false) String startDate, @RequestParam(value = "endDate",required=false) String endDate,
			@RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "order") String order) throws Exception;



    @GetMapping(value = "/department1DeviceUsedRateTopN", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "首页租户topN", notes = "告警类型分布查询", response = Map.class, tags = {"AlertIndexPage API"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回", response = Map.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = Map.class)})
	Map<String, Map<String, Long>> department1DeviceUsedRateTopN(
			@RequestParam(value = "startDate",required=false) String startDate, @RequestParam(value = "endDate",required=false) String endDate,
			@RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "order") String order) throws Exception;

    /**
     * 实时资源利用率topN
     * @param kpi
     * @param idcType
     * @param size
     * @return
     */
    @GetMapping(value = "/devicePusedTopN/{kpi}")
    @ApiOperation(value = "实时资源利用率topN", notes = "实时资源利用率topN",
            response = Map.class, tags = {"AlertIndexPage API"})
    List<Map<String, Object>> devicePusedTopN (@PathVariable("kpi") String kpi,
                                               @RequestParam(name = "idcType", required = false) String idcType,
                                               @RequestParam(name = "size", defaultValue = "10") int size);
}
