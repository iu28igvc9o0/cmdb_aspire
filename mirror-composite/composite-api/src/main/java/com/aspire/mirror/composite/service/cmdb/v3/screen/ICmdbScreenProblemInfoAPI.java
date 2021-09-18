package com.aspire.mirror.composite.service.cmdb.v3.screen;

import com.alibaba.fastjson.JSONObject;
import com.aspire.ums.cmdb.common.Result;
import com.aspire.ums.cmdb.v3.screen.payload.CmdbScreenProblemInfo;
import com.aspire.ums.cmdb.v3.screen.payload.CmdbScreenProblemInfoRequest;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @projectName: CmdbScreenProblemInfoAPI
 * @description: 接口
 * @author: luowenbo
 * @create: 2020-07-03 13:35
 **/
@RequestMapping("/${version}/v3/cmdb/screen/problem")
public interface ICmdbScreenProblemInfoAPI {

    /*
     *  修改问题
     * */
    @PutMapping("/update")
    @ApiOperation(value = "修改问题", notes = "修改问题", tags = {"Cmdb Screen Problem API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = JSONObject.class),
            @ApiResponse(code = 500, message = "内部错误")})
    Map<String,Object> update(@RequestBody CmdbScreenProblemInfo req);

    /*
     *  添加问题
     * */
    @PostMapping("/save")
    @ApiOperation(value = "添加问题", notes = "添加问题", tags = {"Cmdb Screen Problem API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = JSONObject.class),
            @ApiResponse(code = 500, message = "内部错误")})
    Map<String,Object> save(@RequestBody CmdbScreenProblemInfo req);

    /*
     *  查询问题列表
     * */
    @PostMapping("/list")
    @ApiOperation(value = "查询问题列表", notes = "查询问题列表", tags = {"Cmdb Screen Problem API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = JSONObject.class),
            @ApiResponse(code = 500, message = "内部错误")})
    Result<CmdbScreenProblemInfo> list(@RequestBody CmdbScreenProblemInfoRequest req);

    /*
     *  查询具体问题详情，包括评论列表
     * */
    @GetMapping("/list/{id}")
    @ApiOperation(value = "查询具体问题详情，包括评论列表", notes = "查询具体问题详情，包括评论列表", tags = {"Cmdb Screen Problem API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = CmdbScreenProblemInfo.class),
            @ApiResponse(code = 500, message = "内部错误")})
    CmdbScreenProblemInfo listDetail(@PathVariable("id") String id);
}
