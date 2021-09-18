package com.aspire.ums.cmdb.client;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

/**
 * @ClassName ICmdbToAlterESClient
 * @Description cmdb这边调用告警alter那边的数据
 * @Author luowenbo
 * @Date 2020/5/26 14:07
 * @Version 1.0
 */
@FeignClient(name = "ELASTICSEARCH-SERVICE")
//@FeignClient(value = "ELASTICSEARCH-SERVICE", url = "http://10.12.70.39:28103/")
public interface ICmdbToAlterESClient {

    /*
    *  获取CMDB中业务系统每一天的性能指标数据
    * */
    @PostMapping("/v1/report/day2/getBizSytemDataByDay")
    List<Map<String,Object>> getBizSystemDetailInfo(@RequestBody Map<String,Object> monthRequest);

    /*
    *  获取有性能数据的设备IP
    * */
    @PostMapping("/v1/monitorKpi/query")
    List<Map<String,Object>> getIpWithHaveData(@RequestBody Map<String,Object> req);
}
