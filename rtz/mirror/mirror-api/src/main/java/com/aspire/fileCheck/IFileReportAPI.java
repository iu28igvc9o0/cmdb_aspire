package com.aspire.fileCheck;

import net.sf.json.JSONArray;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.RequestParam;

@Api(value = "文件检测", description = "文件检测API")
@RequestMapping(value = "/fileCheck")
public interface IFileReportAPI {
    @RequestMapping(value = "getFileReport", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    @ApiOperation(value = "获取网关指标数据", notes = "获取网关指标数据", response = String.class, tags = {
            "file check"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功"),
            @ApiResponse(code = 500, message = "查询数据异常")})
    String getFileReport(String catolog, String provinceCode, String period, String uploadDate, String fileIndication);

    @RequestMapping(value = "getPerodDict", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    @ApiOperation(value = "获取网关指标数据", notes = "获取网关指标数据", response = String.class, tags = {
            "file check"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功"),
            @ApiResponse(code = 500, message = "查询数据异常")})
    String getPeriodDict(String type);

    @RequestMapping(value = "getOtherFileReport", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    @ApiOperation(value = "获取网关指标数据", notes = "获取网关指标数据", response = String.class, tags = {
            "file check"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功"),
            @ApiResponse(code = 500, message = "查询数据异常")})
    String getOtherFileReport(String catolog, String provinceCode, String period, String uploadDate, String fileIndication);

    @RequestMapping(value = "checkOtherFile", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    @ApiOperation(value = "获取网关指标数据", notes = "获取网关指标数据", response = String.class, tags = {
            "file check"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功"),
            @ApiResponse(code = 500, message = "查询数据异常")})
    public void checkOtherFile();

    @RequestMapping(value = "check", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    @ApiOperation(value = "获取网关指标数据", notes = "获取网关指标数据", response = String.class, tags = {
            "file check"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功"),
            @ApiResponse(code = 500, message = "查询数据异常")})
    void check();

    @RequestMapping(value = "checkAndEmail", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    @ApiOperation(value = "获取网关指标数据", notes = "获取网关指标数据", response = String.class, tags = {
            "file check"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功"),
            @ApiResponse(code = 500, message = "查询数据异常")})
    void checkAndEmail(String catalog);

    @RequestMapping(value = "setFileCount", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    @ApiOperation(value = "设置指标阈值", notes = "设置指标阈值", response = String.class, tags = {
            "file check"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功"),
            @ApiResponse(code = 500, message = "查询数据异常")})
    void setFileCount(String startDate, String endDate);

    @RequestMapping(value = "getFileIndicationByCatalog", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    @ApiOperation(value = "根据分类获取指标列表", notes = "根据分类获取指标列表", response = String.class, tags = {
            "file check"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功"),
            @ApiResponse(code = 500, message = "查询数据异常")})
    JSONArray getFileIndicationByCatalog(@RequestParam("catalog") String catalog);
}
