package com.aspire.ums.cmdb.automate;

import com.aspire.ums.cmdb.common.ResultVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author fanwenhui
 * @date 2020-08-21 17:17
 * @description 自动化模型同步接口
 */
@Api(value = "CMDB 自动化模型同步接口类")
@RequestMapping("/cmdb/autoMate")
public interface IAutomateAPI {

    @RequestMapping(value = "/instance/create", method = RequestMethod.POST)
    @ApiOperation(value = "自动化数据新增接口", notes = "自动化数据新增接口", tags = {"CMDB AutoMate API"})
    ResultVo create(@RequestBody String param);

    @RequestMapping(value = "/instance/update", method = RequestMethod.POST)
    @ApiOperation(value = "自动化数据修改接口", notes = "自动化数据修改接口", tags = {"CMDB AutoMate API"})
    ResultVo update(@RequestBody String param);

    @RequestMapping(value = "/instance/delete", method = RequestMethod.POST)
    @ApiOperation(value = "自动化数据删除接口", notes = "自动化数据删除接口", tags = {"CMDB AutoMate API"})
    ResultVo delete(@RequestBody String param);

    @PostMapping(value = "/instance/getAutomateHostDetail")
    @ApiOperation(value = "获取自动化主机配置信息详情", notes = "获取自动化主机配置信息详情", tags = {"CMDB AutoMate API"})
    ResultVo getAutomateHostDetail(@RequestBody Map<String,String> param);

    @GetMapping(value = "/instance/getAutomateColumns")
    @ApiOperation(value = "获取自动化主机字段信息", notes = "获取自动化主机字段信息", tags = {"CMDB AutoMate API"})
    List<Map<String, Object>> getAutomateColumns();

    @GetMapping(value = "/instance/syncModule4Redis")
    @ApiOperation(value = "自动化模型录入redis", notes = "自动化模型录入redis", tags = {"CMDB AutoMate API"})
    ResultVo syncModule4Redis();

    @GetMapping(value = "/instance/buildExportHeaderList")
    @ApiOperation(value = "获取自动化主机字段信息", notes = "获取自动化主机字段信息", tags = {"CMDB AutoMate API"})
    Map<String,List<String>> buildExportHeaderList();

    @GetMapping(value = "/instance/synAutomateConfFile")
    @ApiOperation(value = "主机配置文件同步", notes = "主机配置文件同步", tags = {"CMDB AutoMate API"})
    ResultVo synAutomateConfFile();

    @PostMapping(value = "/instance/findAutomateConfList")
    @ApiOperation(value = "查询主机IP对应的配置文件", notes = "查询主机IP对应的配置文件", tags = {"CMDB AutoMate API"})
    List<Map<String,String>> findAutomateConfList(@RequestBody Map<String,Object> param);

}
