package com.aspire.fileCheck;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Api(value = "文件缺失完整性检测", description = "文件缺失完整性检测API")
@RequestMapping(value = "/fileMissingIntegrityCheck")
public interface FileMissingIntegrityCheckAPI {

    /**
     * 获取文件缺失完整性检测异常数据
     * @return
     */
    @RequestMapping(value = "/getFileMissingIntegrityCheckData", method = RequestMethod.GET)
    @ApiOperation(value = "获取文件缺失完整性检测异常数据", notes = "获取文件缺失完整性检测异常数据", response = Object.class, tags = {
            "File Missing Integrity Check API" })
    @ApiResponses(value = { @ApiResponse(code = 200, message = "查询成功"),
            @ApiResponse(code = 500, message = "查询数据异常") })
    Object getFileMissingIntegrityCheckData(@RequestParam(value = "type") String type,
                             @RequestParam(value = "province",required = false) String province,
                             @RequestParam(value = "date",required = false) String date,
                             @RequestParam(value = "period",required = false) String period,
                             @RequestParam(value = "missing",required = false) String missing,
                             @RequestParam(value = "fileIndication", required = false) String fileIndication,
                             @RequestParam(value = "currentPage") Integer currentPage,
                             @RequestParam(value = "pageSize") Integer pageSize);

    /**
     * 获取文件缺失完整性状态
     * @return
     */
    @RequestMapping(value = "/getFileMissingIntegrityCheckStatus", method = RequestMethod.GET)
    @ApiOperation(value = "获取文件缺失完整性状态", notes = "获取文件缺失完整性状态", response = Object.class, tags = {
            "File Missing Integrity Check API" })
    @ApiResponses(value = { @ApiResponse(code = 200, message = "查询成功"),
            @ApiResponse(code = 500, message = "查询数据异常") })
    Object getFileMissingIntegrityCheckStatus();
}
