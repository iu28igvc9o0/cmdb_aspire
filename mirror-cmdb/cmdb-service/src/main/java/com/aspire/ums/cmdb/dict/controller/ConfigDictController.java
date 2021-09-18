package com.aspire.ums.cmdb.dict.controller;

import com.aspire.ums.cmdb.allocate.payload.Result;
import com.aspire.ums.cmdb.dict.ICmdbDictAPI;
import com.aspire.ums.cmdb.dict.payload.ConfigDict;
import com.aspire.ums.cmdb.dict.payload.Dict;
import com.aspire.ums.cmdb.dict.service.ConfigDictService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
@RestController
@Slf4j
public class ConfigDictController implements ICmdbDictAPI {
    
    @Autowired
    private ConfigDictService configDictService;
    
    public Result<Dict> getAllConfigDictData(@RequestParam(value = "pageNum",required = false) int pageNum,
                                             @RequestParam(value = "startPageNum",required = false) int startPageNum,
                                             @RequestParam(value = "pageSize",required = false) int pageSize,
                                             @RequestParam(value = "pcode",required = false) String pcode,
                                             @RequestParam(value = "dictCode",required = false) String dictCode,
                                             @RequestParam(value = "dictNote",required = false) String dictNote,
                                             @RequestParam(value = "colName",required = false) String colName){
        return configDictService.getAllConfigDictData(pageNum,startPageNum,pageSize,pcode,dictCode,dictNote,colName);
    }
    
    public String insertCfgDict(@RequestBody Dict dict){
        return configDictService.insert(dict);
    }
    
    public String deleteCfgDictById(@RequestParam("dictId") String dictId){
        return configDictService.delete(dictId);
    }
    
    public String updateCfgDict(@RequestBody Dict dict){
        return configDictService.update(dict);
    }
    
    public Dict getById (@RequestParam("dictId") String dictId){
        return configDictService.getDictById(dictId);
    }
    
    public List<Dict> getAll(@RequestParam(value="dictId",required = false) String dictId){
        return configDictService.getDictAll(dictId);
    }
    
    public List<Map<String, Object>> exprotDict(
            @RequestParam(value = "pcode",required = false) String pcode,
            @RequestParam(value = "dictCode",required = false) String dictCode,
            @RequestParam(value = "dictNote",required = false) String dictNote,
            @RequestParam(value = "colName",required = false) String colName) {
        return configDictService.getDictExportData(pcode,dictCode,dictNote,colName);
    }
    
    public List<ConfigDict> getDicts() {
        return configDictService.selectDicts();
    }
    
    public List<String> getDictType() {
        return configDictService.getDictType();
    }
    
    public List<ConfigDict> getDictsByType(@RequestParam("type") String type,
                                           @RequestParam(value="pid",required = false) String pid,
                                           @RequestParam(value="pValue",required = false) String pValue,
                                           @RequestParam(value="pType",required = false) String pType) {
        return configDictService.selectDictsByType(type,pid,pValue,pType);
    }

    public List<ConfigDict> getDictTree(@PathVariable("col_name") String colName) {
        return configDictService.getDictTree(colName);
    }

    public List<Dict> getByIds (@RequestParam("ids") String dictIds) {
        List<Dict> dictList = configDictService.getDictByIds(Arrays.asList(dictIds.split(",")));
        return dictList;
    }
	/**
     * 查询所有的字典类型及描述
     * @return
     */
    public List<Map> getDistinctDictType() {
        return configDictService.getDistinctDictType();
    }
}
