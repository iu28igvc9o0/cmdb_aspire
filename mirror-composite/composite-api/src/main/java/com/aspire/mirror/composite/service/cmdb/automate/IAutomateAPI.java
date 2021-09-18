package com.aspire.mirror.composite.service.cmdb.automate;

import com.aspire.ums.cmdb.common.ResultVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * @author fanwenhui
 * @date 2020-09-15 11:28
 * @description
 */
@Api(value = "CMDB 自动化模型同步接口类")
@RequestMapping(value = "${version}/cmdb/autoMate")
public interface IAutomateAPI {

    @RequestMapping(value = "/instance/create", method = RequestMethod.POST)
    @ApiOperation(value = "自动化数据新增接口", notes = "自动化数据新增接口", tags = {"CMDB AutoMate API"})
    ResultVo create(String param);

    @PostMapping(value = "/instance/getAutomateHostDetail")
    @ApiOperation(value = "获取自动化主机配置信息详情", notes = "获取自动化主机配置信息详情", tags = {"CMDB AutoMate API"})
    ResultVo getAutomateHostDetail(@RequestBody Map<String,String> param);

    @GetMapping(value = "/instance/getAutomateColumns")
    @ApiOperation(value = "获取自动化主机字段信息", notes = "获取自动化主机字段信息", tags = {"CMDB AutoMate API"})
    List<Map<String, Object>> getAutomateColumns();

    @GetMapping(value = "/instance/buildExportHeaderList")
    @ApiOperation(value = "获取自动化主机字段信息", notes = "获取自动化主机字段信息", tags = {"CMDB AutoMate API"})
    Map<String,List<String>> buildExportHeaderList();

    @RequestMapping(value = "/instance/exportAutomate", method = RequestMethod.POST)
    @ApiOperation(value = "导出主机配置信息", notes = "导出主机配置信息", tags = {"CMDB AutoMate API"})
    Map<String, String> exportInstance(HttpServletResponse response,
                                       @RequestBody Map<String, Object> params,
                                       @RequestParam(value = "moduleType", required = false) String moduleType);

    @PostMapping(value = "/instance/findAutomateConfList")
    @ApiOperation(value = "查询主机IP对应的配置文件", notes = "查询主机IP对应的配置文件", tags = {"CMDB AutoMate API"})
    List<Map<String,String>> findAutomateConfList(@RequestBody Map<String,Object> param);

    @GetMapping(value = "/instance/automateDownload")
    @ApiOperation(value = "附件下载", notes = "附件下载", tags = {"CMDB AutoMate API"})
    void automateDownload(HttpServletRequest request, HttpServletResponse response,
                             @ApiParam(name = "fileId", value = "附件ID") @RequestParam String fileId) throws Exception;
}
