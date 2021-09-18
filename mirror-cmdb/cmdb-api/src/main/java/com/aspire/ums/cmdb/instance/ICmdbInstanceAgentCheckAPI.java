package com.aspire.ums.cmdb.instance;

import com.alibaba.fastjson.JSONObject;
import com.aspire.ums.cmdb.common.Result;
import com.aspire.ums.cmdb.instance.payload.CmdbInstanceAgentCheck;
import com.aspire.ums.cmdb.instance.payload.CmdbInstanceAgentCheckQuery;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @ClassName ICmdbInstanceAgentCheckAPI
 * @Description
 * @Author luowenbo
 * @Date 2020/5/26 16:07
 * @Version 1.0
 */
@RequestMapping("/cmdb/agent")
public interface ICmdbInstanceAgentCheckAPI {

    /**
     * 查询未采集性能数据的Agent设备
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation(value = "查询未采集性能数据的Agent设备", notes = "查询未采集性能数据的Agent设备", tags = {"CMDB Agent API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    Result<CmdbInstanceAgentCheck> list(@RequestBody CmdbInstanceAgentCheckQuery query);

    /**
     * 批量删除
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation(value = "批量删除", notes = "批量删除", tags = {"CMDB Agent API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "删除成功", response = JSONObject.class),
            @ApiResponse(code = 500, message = "内部错误")})
    JSONObject batchDelete(@RequestBody CmdbInstanceAgentCheckQuery query);
}
