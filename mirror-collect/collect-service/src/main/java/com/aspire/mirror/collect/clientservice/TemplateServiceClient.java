package com.aspire.mirror.collect.clientservice ;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

/**
* 监控项相关数据收集服务接口    <br/>
* Project Name:collect-api
* File Name:TemplateServiceClient.java
* Package Name:com.aspire.mirror.collect.api
* ClassName: TemplateServiceClient <br/>
* date: 2018年9月4日 上午9:58:42 <br/>
* @author pengguihua
* @version 
* @since JDK 1.6
*/ 
@FeignClient("template-service")
public interface TemplateServiceClient {
	
	@GetMapping(value = "/v1/items/findLastUpValueByItemId/{item_id}")
	String findLastUpValueByItemId(
			@PathVariable("item_id") String itemId, @RequestParam(value = "sys_code", required = false) String sysCode);

}
