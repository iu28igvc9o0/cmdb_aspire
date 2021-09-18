package com.aspire.mirror.elasticsearch.api.service.theme;

import com.aspire.mirror.elasticsearch.api.dto.theme.BizTheme;
import com.aspire.mirror.elasticsearch.api.dto.theme.BizThemeDimData;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 主题数据服务
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.elasticsearch.api.service.theme
 * 类名称:    IThemeDataService.java
 * 类描述:    主题数据服务
 * 创建人:    JinSu
 * 创建时间:  2019/12/11 10:38
 * 版本:      v1.0
 */
@RequestMapping("${version}/themeEsData")
public interface IThemeDataService {

    @PutMapping("/updateMappingByIndexName")
    void updateMappingByIndexName(@RequestBody Map<String, Object> param);

    @PostMapping("/createThemeData")
    void createThemeEsData(@RequestBody List<Map<String, Object>> map);

    @GetMapping("/getNewLogTime")
    String getLastNewLogTime();

    @GetMapping("/getNewLogThemeData")
    List<String> getNewLogThemeData(@RequestParam(value = "flushTime") String flushTime, @RequestParam(value =
            "lastTime") String lastTime);

    @PostMapping("/getThemeDataHis")
    Map<String, Object> getThemeDataHis(@RequestBody BizTheme bizTheme);

    @GetMapping("/themeHistoryInfo")
    List<Object[]> themeHistoryInfo(@RequestParam(value = "indexName") String indexName, @RequestParam(value =
            "themeCode") String themeCode, @RequestParam(value = "startTime") Long startTime, @RequestParam(value =
            "endTime") Long endTime);

    @GetMapping("/getThemeData")
    Map<String, Object> getThemeData(@RequestParam(value = "indexName") String indexName, @RequestParam(value =
            "startTime") long startTime, @RequestParam(value = "endTime") long endTime,  @RequestParam(value =
            "host", required = false) String host, @RequestParam(value = "bizCode", required = false)  String bizCode, @RequestParam(value = "themeCode") String
                                             themeCode);
    @GetMapping("/getItemValueByItemId")
    String getItemValueByItemId(@RequestParam("itemId") String itemId, @RequestParam(value = "sysCode", required = false) String sysCode);

    @GetMapping("/getThemeCalcResult")
    List<Map<String, Object>> getThemeCalcResult(@RequestParam("itemId") String itemId, @RequestParam("themeId")
            String themeId, @RequestParam("startTime") long startTime, @RequestParam("endTime") long endTime,
                                                 @RequestParam("sortName") String sortName, @RequestParam("nameMap")
                                                         String nameMapString);
}
