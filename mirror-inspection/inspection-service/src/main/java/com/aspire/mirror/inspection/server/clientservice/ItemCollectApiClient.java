package com.aspire.mirror.inspection.server.clientservice ;

import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.aspire.mirror.inspection.server.clientservice.payload.ObjectItemValueWrap;
import com.aspire.mirror.inspection.server.clientservice.payload.FetchObjectItemsValueRequest;

/**
* 监控项相关数据收集服务接口    <br/>
* Project Name:collect-api
* File Name:ItemCollectApiClient.java
* Package Name:com.aspire.mirror.collect.api
* ClassName: ItemCollectApiClient <br/>
* date: 2018年9月4日 上午9:58:42 <br/>
* @author pengguihua
* @version 
* @since JDK 1.6
*/ 
@FeignClient("collect-service")
public interface ItemCollectApiClient {
    /**
     * 获取设备的监控项列表的值
     * @param taskId
     */
    @GetMapping(value = "/v1/itemDatas/deviceItems/fetchValues")
    List<ObjectItemValueWrap> fetchObjectItemsValues(@RequestBody FetchObjectItemsValueRequest request);
}
