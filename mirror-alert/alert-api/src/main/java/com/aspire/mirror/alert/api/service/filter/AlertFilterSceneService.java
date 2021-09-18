package com.aspire.mirror.alert.api.service.filter;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.aspire.mirror.alert.api.dto.filter.AlertFilterSceneResponse;
import com.aspire.mirror.alert.api.dto.filter.AlertFilterOptionDTO;
import com.aspire.mirror.alert.api.dto.filter.AlertFilterSceneDTO;
import com.aspire.mirror.common.entity.PageResponse;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * 历史告警服务
 * <p>
 * 项目名称: mirror平台 包: com.aspire.mirror.alert.api.metrics 类名称:
 * AlertsHisService.java 类描述: 历史告警服务 创建人: JinSu 创建时间: 2018/9/19 11:19 版本: v1.0
 */
@RequestMapping("/v1/alerts/filterScene")
public interface AlertFilterSceneService {

	/**
	 * 查询告警过滤列表
	 */
	@PostMapping(value = "/pageList", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "列表", notes = "获取历史告警列表", tags = { "AlertFilter API" })
	@ApiResponses(value = { @ApiResponse(code = 200, message = "成功", response = PageResponse.class),
			@ApiResponse(code = 500, message = "内部错误") })
	PageResponse<AlertFilterSceneDTO> pageList(@RequestBody AlertFilterSceneResponse pageRequset);

	/**
	 * 查询告警过滤详情
	 *
	 */
	@GetMapping(value = "/detail/{filterScene_id}")
	@ApiOperation(value = "详情", notes = "根据alert_id获取历史告警详情", tags = { "AlertFilter API" })
	@ApiResponses(value = { @ApiResponse(code = 200, message = "成功", response = AlertFilterSceneDTO.class),
			@ApiResponse(code = 500, message = "内部错误") })
	AlertFilterSceneDTO findByPrimaryKey(@PathVariable("filterScene_id") String filterSceneId);
	
	
	
	@GetMapping(value = "/check/{filterScene_name}")
	@ApiOperation(value = "详情", notes = "根据alert_id获取历史告警详情", tags = { "AlertFilter API" })
	@ApiResponses(value = { @ApiResponse(code = 200, message = "成功", response = AlertFilterSceneDTO.class),
			@ApiResponse(code = 500, message = "内部错误") })
	AlertFilterSceneDTO findByName(@PathVariable("filterScene_name") String filterSceneName, 
			@RequestParam("filter_id") String filterId);

	/**
	 * 新增告警过滤
	 */
	@PostMapping(value = "/create")
	@ApiOperation(value = "批量创建历史告警", notes = "批量创建历史告警", response = ResponseEntity.class, tags = { "AlertFilter API" })
	@ApiResponses(value = { @ApiResponse(code = 200, message = "返回", response = ResponseEntity.class),
			@ApiResponse(code = 500, message = "Unexpected error", response = ResponseEntity.class) })
	ResponseEntity<String> create(@RequestBody AlertFilterSceneDTO createRequest);

	/**
	 * 修改告警过滤
	 * 
	 */

	@PutMapping(value = "/update/{filterScene_id}")
	@ApiOperation(value = "修改", notes = "修改", response = ResponseEntity.class, tags = { "AlertFilter API" })
	@ApiResponses(value = { @ApiResponse(code = 200, message = "返回", response = ResponseEntity.class),
			@ApiResponse(code = 500, message = "Unexpected error", response = ResponseEntity.class) })
	ResponseEntity<String> update(@PathVariable("filterScene_id") String filterSceneId,
                                  @RequestBody AlertFilterSceneDTO updateRequest);
	
	/**
	 * 删除告警过滤
	 * 
	 */

	@DeleteMapping(value = "/delete/{filterScene_id}")
	@ApiOperation(value = "删除", notes = "删除", response = ResponseEntity.class, tags = { "AlertFilter API" })
	@ApiResponses(value = { @ApiResponse(code = 200, message = "返回", response = ResponseEntity.class),
			@ApiResponse(code = 500, message = "Unexpected error", response = ResponseEntity.class) })
	ResponseEntity<String> delete(@PathVariable("filterScene_id") String filterSceneId);

	@GetMapping(value = "/options")
	@ApiOperation(value = "删除", notes = "删除", response = AlertFilterOptionDTO.class, tags = { "AlertFilter API" })
	@ApiResponses(value = { @ApiResponse(code = 200, message = "返回", response = AlertFilterOptionDTO.class),
			@ApiResponse(code = 500, message = "Unexpected error", response = AlertFilterOptionDTO.class) })
	List<AlertFilterOptionDTO> getAllOptions();

	@GetMapping(value = "/options/{query_type}")
	@ApiOperation(value = "根据条件查询过滤选项", notes = "根据条件查询过滤选项", 
	response = AlertFilterOptionDTO.class, tags = { "AlertFilter API" })
	@ApiResponses(value = { @ApiResponse(code = 200, message = "返回", response = AlertFilterOptionDTO.class),
			@ApiResponse(code = 500, message = "Unexpected error", response = AlertFilterOptionDTO.class) })
	List<AlertFilterOptionDTO> getOptionsByQueryType(@PathVariable("query_type") String queryType);

}
