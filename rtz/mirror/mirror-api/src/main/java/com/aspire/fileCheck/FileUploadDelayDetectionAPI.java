package com.aspire.fileCheck;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Api(value = "文件上传延时检测", description = "文件上传延时检测API")
@RequestMapping(value = "/fileUploadDelayDetection")
public interface FileUploadDelayDetectionAPI {

    /**
     * 获取文件上传延时检测异常数据
     * @return
     */
    @RequestMapping(value = "/getDetectionData", method = RequestMethod.GET)
    @ApiOperation(value = "获取文件上传延时检测异常数据", notes = "获取文件上传延时检测异常数据", response = Object.class, tags = {
            "File Upload Delay Detection API" })
    @ApiResponses(value = { @ApiResponse(code = 200, message = "查询成功"),
            @ApiResponse(code = 500, message = "查询数据异常") })
    Object getIndicationData(@RequestParam(value = "type") String type,
                             @RequestParam(value = "province",required = false) String province,
                             @RequestParam(value = "date",required = false) String date,
                             @RequestParam(value = "period",required = false) String period,
                             @RequestParam(value = "detection",required = false) String detection,
                             @RequestParam(value = "fileIndication", required = false) String fileIndication);

    /**
     * 获取文件上传延时状态
     * @return
     */
    @RequestMapping(value = "/getFileUploadDetectionStatus", method = RequestMethod.GET)
    @ApiOperation(value = "获取文件上传延时状态", notes = "获取文件上传延时状态", response = Object.class, tags = {
            "File Upload Delay Detection API" })
    @ApiResponses(value = { @ApiResponse(code = 200, message = "查询成功"),
            @ApiResponse(code = 500, message = "查询数据异常") })
    Object getFileUploadDetectionStatus();

    /**
     * 获取文件检测时间段
     * @return
     */
    @RequestMapping(value = "/getFileCheckTimePeriod", method = RequestMethod.GET)
    @ApiOperation(value = "获取文件检测时间段", notes = "获取文件检测时间段", response = Object.class, tags = {
            "File Upload Delay Detection API" })
    @ApiResponses(value = { @ApiResponse(code = 200, message = "查询成功"),
            @ApiResponse(code = 500, message = "查询数据异常") })
    Object getFileCheckTimePeriod();

    /**
     * 手动进行文件上传延时检测
     * @return
     */
    @RequestMapping(value = "/getLatestRunningPeriod", method = RequestMethod.POST)
    @ApiOperation(value = "获取JOB运行的最后时间段", notes = "获取JOB运行的最后时间段", response = Object.class, tags = {
            "File Upload JOBS API" })
    @ApiResponses(value = { @ApiResponse(code = 200, message = "查询成功"),
            @ApiResponse(code = 500, message = "查询失败") })
    Object getLatestRunningPeriod(@RequestParam(value = "jobName") String jobName);
}
