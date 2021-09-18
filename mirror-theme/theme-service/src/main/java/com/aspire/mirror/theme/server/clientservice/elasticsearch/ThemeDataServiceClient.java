package com.aspire.mirror.theme.server.clientservice.elasticsearch;

import java.util.List;
import java.util.Map;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.aspire.mirror.theme.server.dao.po.BizTheme;

/**
 * TODO
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.theme.server.clientservice.elasticsearch
 * 类名称:    ThemeDataServiceClient.java
 * 类描述:    TODO
 * 创建人:    JinSu
 * 创建时间:  2019/12/12 10:29
 * 版本:      v1.0
 */
@FeignClient(value = "ELASTICSEARCH-SERVICE", url = "http://10.1.203.100:28103")
public interface ThemeDataServiceClient {

    @PutMapping("/v1/themeEsData/updateMappingByIndexName")
    void updateMappingByIndexName(@RequestBody Map<String, Object> param);

    @PostMapping("/v1/themeEsData/createThemeData")
    void createThemeEsData(@RequestBody List<Map<String, Object>> map);

    @GetMapping("/v1/themeEsData/getNewLogTime")
    String getLastNewLogTime();

    @GetMapping("/v1/themeEsData/getNewLogThemeData")
    List<String> getNewLogThemeData(@RequestParam(value = "flushTime") String flushTime, @RequestParam(value =
            "lastTime") String lastTime);

    @PostMapping("/v1/themeEsData/getThemeDataHis")
    Map<String, Object> getThemeDataHis(@RequestBody BizTheme bizTheme);

    @GetMapping("/v1/themeEsData/themeHistoryInfo")
    List<Object[]> themeHistoryInfo(@RequestParam(value = "indexName") String indexName, @RequestParam(value =
            "themeCode") String themeCode, @RequestParam(value = "startTime") Long startTime, @RequestParam(value =
            "endTime") Long endTime);

    @GetMapping("/v1/themeEsData/getThemeData")
    Map<String, Object> getThemeData(@RequestParam(value = "indexName") String indexName, @RequestParam(value =
            "startTime") long startTime, @RequestParam(value = "endTime") long endTime,  @RequestParam(value =
            "host", required = false) String host, @RequestParam(value = "bizCode", required = false)  String bizCode, @RequestParam(value = "themeCode") String
            themeCode);
}
