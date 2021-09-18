package com.aspire.mirror.composite.service.cmdb.index;

import com.alibaba.fastjson.JSONObject;
import com.aspire.ums.cmdb.index.payload.ItCloudScreenExportRequest;
import com.aspire.ums.cmdb.index.payload.ItCloudScreenValidateRequest;
import com.aspire.ums.cmdb.index.payload.ScreenValidate;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @ClassName ItCloudScreenValidateAPI
 * @Description 大屏数据校验对外暴露的接口层
 * @Author luowenbo
 * @Date 2020/5/6 17:21
 * @Version 1.0
 */
@RequestMapping("${version}/cmdb/index/overview")
public interface ItCloudScreenValidateAPI {
    /*
     *  验证数据完整性和准确性
     * */
    @RequestMapping(value = "/validate", method = RequestMethod.POST)
    @ApiOperation(value = "验证数据完整性和准确性", notes = "验证数据完整性和准确性", tags = {"CMDB Index Overview API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = JSONObject.class),
            @ApiResponse(code = 500, message = "内部错误")})
    JSONObject validate(@RequestBody ItCloudScreenValidateRequest req);

    /*
     *  查询，按照新增时间列出前5条
     * */
    @RequestMapping(value = "/validate/list", method = RequestMethod.POST)
    @ApiOperation(value = "查询，按照新增时间列出前5条", notes = "查询，按照新增时间列出前5条", tags = {"CMDB Index Overview API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    List<ScreenValidate> listAll();

    /*
     *  导出数据的生成
     * */
    @RequestMapping(value = "/excel/create", method = RequestMethod.POST)
    @ApiOperation(value = "导出数据的生成", notes = "导出数据的生成", tags = {"CMDB Index Overview API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    JSONObject createExcel(@RequestBody ItCloudScreenExportRequest req);

    /*
     *  数据下载
     * */
    @RequestMapping(value = "/excel/export", method = RequestMethod.POST)
    @ApiOperation(value = "数据下载", notes = "数据下载", tags = {"CMDB Index Overview API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    JSONObject exportExcel(@RequestBody ItCloudScreenExportRequest req);
}
