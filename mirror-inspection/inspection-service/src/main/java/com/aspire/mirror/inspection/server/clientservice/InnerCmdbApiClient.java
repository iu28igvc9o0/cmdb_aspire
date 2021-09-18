package com.aspire.mirror.inspection.server.clientservice;

import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.aspire.mirror.inspection.server.clientservice.payload.InnerCmdbDeviceDetail;

/**
* 微服务内部CMDB服务接口    <br/>
* Project Name:inspection-service
* File Name:InnerCmdbApiClient.java
* Package Name:com.aspire.mirror.inspection.server.clientservice
* ClassName: InnerCmdbApiClient <br/>
* date: 2018年8月27日 下午5:57:42 <br/>
* @author pengguihua
* @version 
* @since JDK 1.6
*/ 
@FeignClient(value="cmdb")
public interface InnerCmdbApiClient {
	/**
	* 使用指定的设备id, 查询设备详情, 多个设备id参数使用英文逗号分隔. <br/>
	*
	* 作者： pengguihua
	* @param joinedIds
	* @return
	*/  
	@GetMapping(value = "/cmdb/instance/listInstanceBaseInfo/{device_ids}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	List<InnerCmdbDeviceDetail> listDeviceDetailsByIdArr(@PathVariable("device_ids") String joinedIds);
}
