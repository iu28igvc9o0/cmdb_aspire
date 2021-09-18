package com.aspire.ums.cmdb.dict.controller;

import com.aspire.ums.cmdb.allocate.entity.Result;
import com.aspire.ums.cmdb.dict.entity.ConfigDict;
import com.aspire.ums.cmdb.dict.entity.Dict;
import com.aspire.ums.cmdb.dict.service.ConfigDictService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: ConfigDictController
 * Author:   ws
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@RefreshScope
@RestController
@Slf4j
@RequestMapping("/cmdb/configDict")
public class ConfigDictController {
    
    @Autowired
    private ConfigDictService configDictService;
    
    @GetMapping("/list")
    public Result<Dict> getAllConfigDictData(@RequestParam(value = "pageNum",required = false) int pageNum,
                                             @RequestParam(value = "startPageNum",required = false) int startPageNum,
                                             @RequestParam(value = "pageSize",required = false) int pageSize,
                                             @RequestParam(value = "pcode",required = false) String pcode,
                                             @RequestParam(value = "dictCode",required = false) String dictCode,
                                             @RequestParam(value = "dictNote",required = false) String dictNote,
                                             @RequestParam(value = "colName",required = false) String colName){
        return configDictService.getAllConfigDictData(pageNum,startPageNum,pageSize,pcode,dictCode,dictNote,colName);
    }
    
    @PostMapping("/add")
    public String insertCfgDict(@RequestBody Dict dict){
        return configDictService.insert(dict);
    }
    
    @DeleteMapping("/delete")
    public String deleteCfgDictById(@RequestParam("dictId") String dictId){
        return configDictService.delete(dictId);
    }
    
    @PostMapping("/update")
    public String updateCfgDict(@RequestBody Dict dict){
        return configDictService.update(dict);
    }
    
    @GetMapping("/getById")
    public Dict getById (@RequestParam("dictId") String dictId){
        return configDictService.getDictById(dictId);
    }
    
    @GetMapping("/getAll")
    public List<Dict> getAll(@RequestParam(value="dictId",required = false) String dictId){
        return configDictService.getDictAll(dictId);
    }
    
    @PostMapping("/export")
    public List<Map<String, Object>> exprotDict(
            @RequestParam(value = "pcode",required = false) String pcode,
            @RequestParam(value = "dictCode",required = false) String dictCode,
            @RequestParam(value = "dictNote",required = false) String dictNote,
            @RequestParam(value = "colName",required = false) String colName) {
        return configDictService.getDictExportData(pcode,dictCode,dictNote,colName);
    }
    
    @GetMapping("/getDicts")
    public List<ConfigDict> getDicts() {
        return configDictService.selectDicts();
    }
    
    @GetMapping("/getDictType")
    public List<String> getDictType() {
        return configDictService.getDictType();
    }
    
    @GetMapping("/getDictsByType")
    public List<ConfigDict> getDictsByType(@RequestParam("type") String type,
                                           @RequestParam(value="pid",required = false) String pid,
                                           @RequestParam(value="pValue",required = false) String pValue,
                                           @RequestParam(value="pType",required = false) String pType) {
        return configDictService.selectDictsByType(type,pid,pValue,pType);
    }

    @GetMapping("/getTree")
    public List<ConfigDict> getDictTree(@RequestParam("col_name") String colName, @RequestParam(value="ids", required = false) String ids) {
        return configDictService.getDictTree(colName, ids);
    }
    @GetMapping("/getByIds")
    public List<Dict> getByIds (@RequestParam("ids") String dictIds) {
        List<Dict> dictList = configDictService.getDictByIds(Arrays.asList(dictIds.split(",")));
        return dictList;
    }
}
