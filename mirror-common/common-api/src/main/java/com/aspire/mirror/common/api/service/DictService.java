package com.aspire.mirror.common.api.service;

import com.aspire.mirror.common.api.DictResponse;
import com.aspire.mirror.common.api.dto.model.ConfigDict;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 字典服务
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.common.api.service
 * 类名称:    DictService.java
 * 类描述:    字典服务
 * 创建人:    JinSu
 * 创建时间:  2018/11/9 17:45
 * 版本:      v1.0
 */
@Api("字典API")
public interface DictService {

    @GetMapping(value = "/v1/dict/listAll")
    @ApiOperation(value = "字典数据列表", notes = "字典数据列表", tags = {"Dict API"})
    DictResponse findAll();

    @GetMapping(value = "/v1/dict/list")
    @ApiOperation(value = "字典数据列表", notes = "字典数据列表", tags = {"Dict API"})
    DictResponse findList();

    @RequestMapping(value = "/cmdb/configDict/getDictsByType", method = RequestMethod.GET)
    List<ConfigDict> getDictsByType(@RequestParam("type") String type,
                                    @RequestParam(value = "pid", required = false) String pid,
                                    @RequestParam(value = "pValue", required = false) String pValue,
                                    @RequestParam(value = "pType", required = false) String pType);

}
