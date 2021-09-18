package com.aspire.mirror.elasticsearch.api.service.indexPage;

import com.aspire.mirror.elasticsearch.api.dto.DevicePusedTopN;
import com.aspire.mirror.elasticsearch.api.dto.IdcTypePhysicalReq;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author baiwp
 * @title: IItemService
 * @projectName mirror-common
 * @description: TODO
 * @date 2019/6/219:52
 */
@RequestMapping("${version}/indexPage")
public interface IIndexPageService {


    @PostMapping(value = "/idcTypeDeviceUsedRate",consumes=MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    Map<String,Map<String,Long>> idcTypeDeviceUsedRate(@RequestBody IdcTypePhysicalReq idcTypePhysicalReq)  throws Exception;
    
    @PostMapping(value = "/bizSystemDeviceUsedRate",consumes=MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    Map<String,Map<String,Long>> bizSystemDeviceUsedRate(@RequestBody IdcTypePhysicalReq idcTypePhysicalReq)  throws Exception;

    /**
	 * 资源利用率趋势图
	 */
    @PostMapping(value = "/deviceUsedRateTrends",consumes=MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	Map<String, Object> deviceUsedRateTrends(@RequestBody IdcTypePhysicalReq idcTypePhysicalReq) throws Exception;

	/**
	 * (科管监控首页)资源利用率趋势图
	 */
	@PostMapping(value = "/deviceUsedRateTrendsForKG",consumes=MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	Map<String, Object> deviceUsedRateTrendsForKG(@RequestBody IdcTypePhysicalReq idcTypePhysicalReq) throws Exception;
    
    @PostMapping(value = "/bizSystemDeviceUsedRateLow",consumes=MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    List<Map<String,Object>> bizSystemDeviceUsedRateLow(@RequestBody IdcTypePhysicalReq idcTypePhysicalReq)  throws Exception;

	/**
	 * (科管监控首页)业务资源利用率
	 */
	@PostMapping(value = "/bizSystemDeviceUsedRateForKG",consumes=MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	List<Map<String,Object>> bizSystemDeviceUsedRateForKG(@RequestBody IdcTypePhysicalReq idcTypePhysicalReq)  throws Exception;

    @GetMapping(value = "/bizSystemDeviceUsedRateTopN")
	Map<String, Map<String, Long>> bizSystemDeviceUsedRateTopN(@RequestParam(value = "startDate")String startDate,@RequestParam(value = "endDate") String endDate
			,@RequestParam(value = "size",required=false) Integer size,@RequestParam(value = "groupCol") String groupCol,
			@RequestParam(value = "order")String order) throws Exception;

    @PostMapping(value = "/deviceUtilization")
	Map<String, Object> deviceUtilization(@RequestBody IdcTypePhysicalReq idcTypePhysicalReq) throws Exception;
    
    /**
     * 资源池-网络带宽资源
     * @return
     * @throws Exception
     */
	/*
	 * @PostMapping(value =
	 * "/idcTypeNetWorkBandwidth",consumes=MediaType.APPLICATION_JSON_VALUE,
	 * produces = MediaType.APPLICATION_JSON_VALUE) Map
	 * idcTypeNetWorkBandwidth(@RequestBody IdcTypePhysicalReq idcTypePhysicalReq)
	 * throws Exception;
	 */

	@PostMapping(value = "/storageUsedRateForKG", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "磁盘利用率", notes = "磁盘利用率", response = Map.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "返回", response = Map.class),
			@ApiResponse(code = 500, message = "Unexpected error", response = Map.class)})
	Map<String, Object> storageUsedRateForKG(@RequestBody Map<String,String> param);

	/**
	 * 存储资源使用总览
	 * @param param
	 * @return
	 */
	@PostMapping(value = "/storageUseView", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "存储资源使用总览", notes = "存储资源使用总览", response = Map.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "返回", response = Map.class),
			@ApiResponse(code = 500, message = "Unexpected error", response = Map.class)})
	Map<String, Object> storageUseView(@RequestBody Map<String,String> param);

	/**
	 * 实时资源利用率topN
	 * @param kpi
	 * @return
	 */
	@PostMapping(value = "/devicePusedTopN/{kpi}")
	List<Map<String, Object>> devicePusedTopN (@PathVariable("kpi") String kpi,
											   @RequestBody DevicePusedTopN devicePusedTopN);

	/**
	 * 根据resourceId和key获取实时监控数据列表
	 * @param paramsList
	 * @return
	 */
	@PostMapping(value = "/getKpiListByKey")
	List<Map<String, Object>> getKpiListByKey (@RequestBody List<Map<String, Object>> paramsList);

	/**
	 * 根据resourceId和key获取实时监控数据列表
	 * @param paramsList
	 * @return
	 */
	@PostMapping(value = "/getKpiListForItem/{resourceId}")
	List<Map<String, Object>> getKpiListForItem (@PathVariable("resourceId") String resourceId,@RequestBody List<String> paramsList);
}
