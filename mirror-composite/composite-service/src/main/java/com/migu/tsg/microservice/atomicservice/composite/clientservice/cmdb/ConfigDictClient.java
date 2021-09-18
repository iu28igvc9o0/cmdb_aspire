package com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb;

import java.util.List;
import java.util.Map;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.aspire.mirror.composite.service.inspection.payload.ConfigDict;
import com.aspire.mirror.composite.service.inspection.payload.Dict;
import com.aspire.mirror.composite.service.inspection.payload.Result;

/**
 * 项目名称:
 * 包: com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb
 * 类名称:
 * 类描述:
 * 创建人: PJX
 * 创建时间: 2019/4/15 14:20
 * 版本: v1.0
 */
@FeignClient(value = "CMDB")
public interface ConfigDictClient {
    
    @RequestMapping(value = "/cmdb/configDict/list", method = RequestMethod.GET)
    Result<Dict> getAllConfigDictData(@RequestParam(value = "pageNum",required = false) int pageNum,
                                             @RequestParam(value = "startPageNum",required = false) int startPageNum,
                                             @RequestParam(value = "pageSize",required = false) int pageSize,
                                             @RequestParam(value = "pcode",required = false) String pcode,
                                             @RequestParam(value = "dictCode",required = false) String dictCode,
                                             @RequestParam(value = "dictNote",required = false) String dictNote,
                                             @RequestParam(value = "colName",required = false) String colName);
    
    @RequestMapping(value = "/cmdb/configDict/add", method = RequestMethod.POST)
    String insertCfgDict(@RequestBody Dict dict);
    
    @RequestMapping(value = "/cmdb/configDict/delete", method = RequestMethod.DELETE)
    String deleteCfgDictById(@RequestParam(value = "dictId") String dictId);
    
    @RequestMapping(value = "/cmdb/configDict/update", method = RequestMethod.POST)
    String updateCfgDict(@RequestBody Dict dict);
    
    @RequestMapping(value = "/cmdb/configDict/getById", method = RequestMethod.GET)
    Dict getById(@RequestParam(value = "dictId") String dictId);
    
    @RequestMapping(value = "/cmdb/configDict/getAll", method = RequestMethod.GET)
    List<Dict> getAll(@RequestParam(value = "dictId",required = false) String dictId);
    
    @RequestMapping(value = "/cmdb/configDict/export", method = RequestMethod.POST)
    List<Map<String, Object>> exportDict(@RequestParam(value = "pcode",required = false) String pcode,
                                                @RequestParam(value = "dictCode",required = false) String dictCode,
                                                @RequestParam(value = "dictNote",required = false) String dictNote,
                                                @RequestParam(value = "colName",required = false) String colName);
    
    @RequestMapping(value = "/cmdb/configDict/getDicts", method = RequestMethod.GET)
    List<ConfigDict> getDicts();
    
    @RequestMapping(value = "/cmdb/configDict/getDictsByType", method = RequestMethod.GET)
    List<ConfigDict> getDictsByType(@RequestParam("type") String type,
                                           @RequestParam(value="pid",required = false) String pid,
                                           @RequestParam(value="pValue",required = false) String pValue,
                                           @RequestParam(value="pType",required = false) String pType);

    @RequestMapping(value = "/cmdb/configDict/getTree/{col_name}", method = RequestMethod.GET)
    List<ConfigDict> getTree(@PathVariable("col_name") String colName);

    @RequestMapping(value = "/cmdb/configDict/getByIds", method = RequestMethod.GET)
    List<Dict> getByIds(@RequestParam("ids") String ids);
    
    @RequestMapping(value = "/cmdb/configDict/getDictType", method = RequestMethod.GET)
    List<String> getDictType();

    @GetMapping("/cmdb/configDict/getDistinctDictType")
    List<Map> getDistinctDictType();
}
