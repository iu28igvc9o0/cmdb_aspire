package com.aspire.mirror.elasticsearch.api.service.zabbix;

import java.text.ParseException;
import java.util.Map;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.aspire.mirror.elasticsearch.api.dto.MonthReportRequest;
import com.aspire.mirror.elasticsearch.api.dto.TenantRateRequest;

import io.swagger.annotations.ApiOperation;

/**
 * @author baiwp
 * @title: IItemService
 * @projectName mirror-common
 * @description: TODO
 * @date 2019/6/219:52
 */
@RequestMapping("${version}/resourceScreen")
public interface IResourceRateScreenService {


	
    @PostMapping("/idcTypRate")
    @ApiOperation(value = "一级it云资源利用率", notes = "一级it云资源利用率", tags = {"IResourceRateScreenService API"})
    Map<String, Object> queryData(@RequestBody MonthReportRequest monthReportRequest)throws ParseException;

    @PostMapping("/departmentRate")
    @ApiOperation(value = "租户资源利用率", notes = "租户资源利用率", tags = {"IResourceRateScreenService API"})
	Map<String, Map<String,String>> queryDepartmentData(@RequestBody TenantRateRequest raterequest) throws ParseException;

    @PostMapping("/queryDepartmentUseRateData")
    @ApiOperation(value = "p2.6资源利用率", notes = "p2.6资源利用率", tags = {"IResourceRateScreenService API"})
	Map<String, Double> queryDepartmentUseRateData(TenantRateRequest raterequest) throws ParseException;
  
}
