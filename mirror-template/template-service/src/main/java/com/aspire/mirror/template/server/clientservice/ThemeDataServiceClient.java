package com.aspire.mirror.template.server.clientservice;

import java.util.List;
import java.util.Map;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
    @GetMapping("/v1/themeEsData/getItemValueByItemId")
    String getItemValueByItemId(@RequestParam("itemId") String itemId, @RequestParam(value = "sysCode", required = false) String sysCode);

    @GetMapping("/v1/themeEsData/getThemeCalcResult")
    List<Map<String, Object>> getThemeCalcResult(@RequestParam("itemId") String itemId, @RequestParam("themeId")
            String themeId, @RequestParam("startTime") long startTime, @RequestParam("endTime") long endTime,
                                                 @RequestParam("sortName") String sortName, @RequestParam("nameMap")
                                                         String nameMapString);
}
