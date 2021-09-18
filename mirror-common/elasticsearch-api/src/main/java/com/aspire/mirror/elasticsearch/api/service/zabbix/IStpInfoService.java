package com.aspire.mirror.elasticsearch.api.service.zabbix;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.aspire.mirror.elasticsearch.api.dto.RouteDeviceInfo;
import com.aspire.mirror.elasticsearch.api.dto.RouteQueryRequest;
import com.aspire.mirror.elasticsearch.api.dto.StpDeviceInfo;

/**
 * lldp信息服务
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.elasticsearch.api.service.zabbix
 * 类名称:    ILLdpInfoService.java
 * 类描述:    lldp信息服务
 * 创建人:    JinSu
 * 创建时间:  2019/9/18 9:54
 * 版本:      v1.0
 */
@RequestMapping("${version}/stp")
public interface IStpInfoService {
	@GetMapping("queryStpInfo")
	StpDeviceInfo queryStpInfo(@RequestParam(value = "idcType")String idcType
			,@RequestParam(value = "ip")String ip,@RequestParam(value = "indexDate",required=false) String indexDate);

	@PostMapping("queryRouteInfo")
	RouteDeviceInfo queryRouteInfo(@RequestBody RouteQueryRequest queryRequest);
}
