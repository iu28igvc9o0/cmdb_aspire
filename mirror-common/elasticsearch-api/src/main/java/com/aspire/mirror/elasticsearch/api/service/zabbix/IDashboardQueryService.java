package com.aspire.mirror.elasticsearch.api.service.zabbix;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.aspire.mirror.elasticsearch.api.dto.DashboardResponse;
import com.aspire.mirror.elasticsearch.api.dto.DataSetRequest;
import com.aspire.mirror.elasticsearch.api.dto.DataSetsDto;

/**
 * @author baiwp
 * @title: IItemService
 * @projectName mirror-common
 * @description: TODO
 * @date 2019/6/219:52
 */
@RequestMapping("${version}/dashboard")
public interface IDashboardQueryService {


    @PostMapping(value = "",consumes=MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    DashboardResponse getDashboardJsonList(@RequestBody DataSetsDto dataSetsDto)  throws Exception;

    @PostMapping(value = "queryDataList",consumes=MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    DashboardResponse queryDataList(@RequestBody DataSetRequest dataSetRequest) throws ParseException;

    @PostMapping(value = "queryLatestData",consumes=MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	Map<String, Object> queryLatestData(@RequestBody DataSetRequest dataSetRequest) throws ParseException;

    @PostMapping(value = "queryTermDatas",consumes=MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	List<Map<String, Object>> queryTermDatas(@RequestBody DataSetRequest dataSetRequest) throws ParseException;
}
