package com.aspire.mirror.inspection.server.clientservice;

import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.aspire.mirror.inspection.server.clientservice.payload.ItemsDetailResponse;
import com.aspire.mirror.inspection.server.clientservice.payload.MonitorApiServerConfig;
import com.aspire.mirror.inspection.server.clientservice.payload.TriggersDetailResponse;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "TEMPLATE-SERVICE")
public interface TemplateApiClient {
	
	/**
	 * 根据模板id,查询模板下的监控项. <br/>
	 *
	 * @param joinedTempIds
	 * @return
	 */
	@GetMapping(value = "/v1/items/listByTemplate/{joinedTempIds}")
	List<ItemsDetailResponse> listItemsByTemplateIds(@PathVariable("joinedTempIds") String joinedTempIds);
   
	/**
     * 根据主键查询monitor_items集合信息
     *
     * @param itemIds monitor_items主键(多个以逗号分隔)
     * @return List<ItemsVO> monitor_items查询响应对象
     */
    @GetMapping(value = "/v1/items/listItem", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    List<ItemsDetailResponse> listItemsByIdArr(@RequestParam("item_ids") String itemIds);
    
    /**
     * 根据主键查询触发器集合信息
     *
     * @param triggerIds 触发器主键(多个以逗号分隔)
     * @return List<TriggersVO> 触发器查询响应对象
     */
    @GetMapping(value = "/v1/triggers/list/{item_ids}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    List<TriggersDetailResponse> listTriggersByItemIdArr(@PathVariable("item_ids") String itemIds);
    
    /**
    * 根据指定的机房id，查询对应的apiServer信息. <br/>
    *
    * 作者： pengguihua
    * @param room_ids
    * @return
    */  
    @GetMapping(value = "/v1/api_server_config/listByRoomIds/{room_ids}", 
    						produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    List<MonitorApiServerConfig> listApiSvrConfigsByRoomIds(@PathVariable("room_ids") String roomIds);
}