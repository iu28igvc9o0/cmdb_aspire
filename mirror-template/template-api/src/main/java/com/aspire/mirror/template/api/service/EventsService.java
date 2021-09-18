package com.aspire.mirror.template.api.service;

import com.aspire.mirror.template.api.dto.EventsCreateRequest;
import com.aspire.mirror.template.api.dto.EventsCreateResponse;
import com.aspire.mirror.template.api.dto.EventsUpdateRequest;
import com.aspire.mirror.template.api.dto.EventsUpdateResponse;
import com.aspire.mirror.template.api.dto.vo.EventsVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 事件对外暴露接口
 *
 * 项目名称:  mirror平台
 * 包:       com.aspire.mirror.template.api.service
 * 类名称:    EventsService.java
 * 类描述:    事件对外暴露接口层
 * 创建人:    JinSu
 * 创建时间:  2018-07-27 13:48:09
 */
@Api(value = "事件")
public interface EventsService{
    /**
     * 创建事件信息
     *
     * @param eventsCreateRequest 事件创建请求对象
     * @return EventsCreateResponse 事件创建响应对象
     */
    @PostMapping(value="/v1/events/insert", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value="创建事件",notes="创建事件",response=EventsCreateResponse.class,tags={ "/v1/events" })
    @ApiResponses(value = {
    @ApiResponse(code = 200, message = "返回", response =  EventsCreateResponse.class),
    @ApiResponse(code = 500, message = "Unexpected error", response = EventsCreateResponse.class) })
    EventsCreateResponse createdEvents(@RequestBody EventsCreateRequest eventsCreateRequest);
    /**
     * 根据主键删除单条事件信息
     *
     * @param eventId 主键
     * @@return Result 返回结果
     */
    @DeleteMapping(value = "/v1/events/{event_id}")
    @ApiOperation(value = "删除单条事件信息", notes = "删除单条事件信息",
    tags = {"/v1/events"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功")})
    ResponseEntity<String> deleteByPrimaryKey(@PathVariable("event_id") String eventId);

    /**
     * 根据主键删除多条事件信息
     *
     * @param eventIds 主键（以逗号分隔）
     * @@return Result 返回结果
     */
    @DeleteMapping(value = "/v1/events/batchDelete/{event_ids}")
    @ApiOperation(value = "删除多条事件信息", notes = "删除多条事件信息",
    tags = {"/v1/events"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功")})
    ResponseEntity<String> deleteByPrimaryKeyArrays(@PathVariable("event_ids") String eventIds);

    /**
     * 根据主键修改事件信息
     *
     * @param eventsUpdateRequest 事件修改请求对象
     * @return EventsUpdateResponse 事件修改响应对象
     */
    @PutMapping(value="/v1/events/{event_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value="修改事件",notes="修改事件",response=EventsUpdateResponse.class,tags={ "/v1/events" })
    @ApiResponses(value = {
    @ApiResponse(code = 200, message = "返回", response =  EventsUpdateResponse.class),
    @ApiResponse(code = 500, message = "Unexpected error", response = EventsUpdateResponse.class) })
    EventsUpdateResponse modifyByPrimaryKey(@PathVariable("event_id") String eventId, @RequestBody EventsUpdateRequest eventsUpdateRequest);

    /**
     * 根据主键查找事件详情信息
     *
     * @param eventId 事件主键
     * @return EventsVO 事件详情响应对象
     */
    @GetMapping(value = "/v1/events/{event_id}")
    @ApiOperation(value = "详情", notes = "根据eventId获取事件详情", tags = {"/v1/events"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功", response = EventsVO.class),
    @ApiResponse(code = 500, message = "内部错误")})
    EventsVO findByPrimaryKey(@PathVariable("event_id") String eventId);

    /**
     * 根据主键查询事件集合信息
     *
     * @param eventIds 事件主键(多个以逗号分隔)
     * @return List<EventsVO> 事件查询响应对象
     */
    @GetMapping(value = "/v1/events/list/{event_id}")
    @ApiOperation(value = "查询", notes = "查询", tags = {"/v1/events"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功"),
    @ApiResponse(code = 500, message = "内部错误")})
    List<EventsVO> listByPrimaryKeyArrays(@PathVariable("event_id") String eventIds);

}
