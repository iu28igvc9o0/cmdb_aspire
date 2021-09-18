package com.aspire.ums.cmdb.maintenance;

import com.alibaba.fastjson.JSONObject;
import com.aspire.ums.cmdb.common.Result;
import com.aspire.ums.cmdb.maintenance.payload.CmdbComponentInfo;
import com.aspire.ums.cmdb.maintenance.payload.CmdbComponentInfoQueryRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: ICmdbComponentInfoAPI
 * Author:   luowenbo
 * Date:     2020/2/7 15:43
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Api(value = "维保部件信息管理接口")
@RequestMapping("/cmdb/maintenance/component")
public interface ICmdbComponentInfoAPI {

    /*
     *  增加部件信息
     * */
    @PostMapping(value = "/save" )
    @ApiOperation(value = "增加部件信息", notes = "增加部件信息", tags = {"Cmdb Maintenance Component API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "保存成功", response = JSONObject.class),
            @ApiResponse(code = 500, message = "内部错误")})
    JSONObject save(@RequestBody CmdbComponentInfo obj);

    /*
     *  删除部件信息
     * */
    @PostMapping(value = "/delete" )
    @ApiOperation(value = "删除部件信息", notes = "删除部件信息", tags = {"Cmdb Maintenance Component API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "保存成功", response = JSONObject.class),
            @ApiResponse(code = 500, message = "内部错误")})
    JSONObject delete(@RequestBody CmdbComponentInfo obj);

    /*
     *  修改部件信息
     * */
    @PostMapping(value = "/update" )
    @ApiOperation(value = "修改部件信息", notes = "修改部件信息", tags = {"Cmdb Maintenance Component API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "保存成功", response = JSONObject.class),
            @ApiResponse(code = 500, message = "内部错误")})
    JSONObject update(@RequestBody CmdbComponentInfo obj);

    /*
     *  查询部件信息
     * */
    @PostMapping(value = "/list" )
    @ApiOperation(value = "查询部件信息", notes = "查询部件信息", tags = {"Cmdb Maintenance Component API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "保存成功", response = JSONObject.class),
            @ApiResponse(code = 500, message = "内部错误")})
    Result<CmdbComponentInfo> list(@RequestBody CmdbComponentInfoQueryRequest request);

}
