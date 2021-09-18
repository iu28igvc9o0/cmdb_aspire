package com.aspire.ums.cmdb.dict;

import com.aspire.ums.cmdb.allocate.payload.Result;
import com.aspire.ums.cmdb.dict.payload.ConfigDict;
import com.aspire.ums.cmdb.dict.payload.Dict;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: ICmdbCodeAPI
 * Author:   zhu.juwang
 * Date:     2019/5/13 18:43
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@RequestMapping("/cmdb/configDict")
public interface ICmdbDictAPI {

    @GetMapping("/list")
    Result<Dict> getAllConfigDictData(@RequestParam(value = "pageNum",required = false) int pageNum,
                                             @RequestParam(value = "startPageNum",required = false) int startPageNum,
                                             @RequestParam(value = "pageSize",required = false) int pageSize,
                                             @RequestParam(value = "pcode",required = false) String pcode,
                                             @RequestParam(value = "dictCode",required = false) String dictCode,
                                             @RequestParam(value = "dictNote",required = false) String dictNote,
                                             @RequestParam(value = "colName",required = false) String colName);

    @PostMapping("/add")
    String insertCfgDict(@RequestBody Dict dict);

    @DeleteMapping("/delete")
    String deleteCfgDictById(@RequestParam("dictId") String dictId);

    @PostMapping("/update")
    String updateCfgDict(@RequestBody Dict dict);

    @GetMapping("/getById")
    Dict getById (@RequestParam("dictId") String dictId);

    @GetMapping("/getAll")
    List<Dict> getAll(@RequestParam(value="dictId",required = false) String dictId);

    @PostMapping("/export")
    List<Map<String, Object>> exprotDict(
            @RequestParam(value = "pcode",required = false) String pcode,
            @RequestParam(value = "dictCode",required = false) String dictCode,
            @RequestParam(value = "dictNote",required = false) String dictNote,
            @RequestParam(value = "colName",required = false) String colName);

    @GetMapping("/getDicts")
    List<ConfigDict> getDicts();

    /**
     * 获取字典值列表
     * @param type 字典类型
     * @param pid 父字典ID
     * @param pValue 父字典值
     * @param pType 父字典类型
     * @return 字典值列表
     */
    @GetMapping("/getDictsByType")
    List<ConfigDict> getDictsByType(@RequestParam("type") String type,
                                           @RequestParam(value="pid",required = false) String pid,
                                           @RequestParam(value="pValue",required = false) String pValue,
                                           @RequestParam(value="pType",required = false) String pType);
    @GetMapping("/getTree/{col_name}")
    List<ConfigDict> getDictTree(@PathVariable("col_name") String colName);

    @GetMapping("/getByIds")
    List<Dict> getByIds (@RequestParam("ids") String dictIds);

    @GetMapping("/getDistinctDictType")
    List<Map> getDistinctDictType();
}
