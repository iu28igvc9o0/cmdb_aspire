package com.aspire.mirror.composite.service.cmdb.networkCard;

import com.alibaba.fastjson.JSONObject;
import com.aspire.ums.cmdb.networkCard.payload.CmdbInstanceNetworkCard;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: INetworkCardProgramAPI
 * Author:   luowenbo
 * Date:     2019/9/20 10:44
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Api(value = "网卡信息管理接口")
@RequestMapping("/${version}/cmdb/networkCard")
public interface INetworkCardProgramAPI {

    /*
     *  新增网卡信息
     *  @return 是否成功JSON
     * */
    @PostMapping(value = "/save" )
    @ApiOperation(value = "添加网卡信息", notes = "添加网卡信息", tags = {"CMDB NetworkCardProgram API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "添加成功", response = String.class),
            @ApiResponse(code = 500, message = "内部错误")})
    JSONObject insertNetwordCard(@RequestBody CmdbInstanceNetworkCard cmdbInstanceNetworkCard);

    /*
     *  删除网卡信息
     *  @return 是否成功JSON
     * */
    @DeleteMapping(value = "/delete" )
    @ApiOperation(value = "删除网卡信息", notes = "删除网卡信息", tags = {"CMDB NetworkCardProgram API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "删除成功", response = String.class),
            @ApiResponse(code = 500, message = "内部错误")})
    JSONObject deleteNetwordCardById(@RequestParam("id") String id);

    /*
     *  修改网卡信息
     *  @return 是否成功JSON
     * */
    @PostMapping(value = "/update" )
    @ApiOperation(value = "修改网卡信息", notes = "修改网卡信息", tags = {"CMDB NetworkCardProgram API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "修改成功", response = String.class),
            @ApiResponse(code = 500, message = "内部错误")})
    JSONObject updateNetwordCard(@RequestBody CmdbInstanceNetworkCard cmdbInstanceNetworkCard);

    /*
     *  查询网卡信息
     *  @return 是否成功JSON
     * */
    @GetMapping(value = "/getByInstanceId" )
    @ApiOperation(value = "获取网卡信息", notes = "获取网卡信息", tags = {"CMDB NetworkCardProgram API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "获取成功", response = String.class),
            @ApiResponse(code = 500, message = "内部错误")})
    List<CmdbInstanceNetworkCard> getNetwordCardByInstanceId(@RequestParam("instanceId") String instanceId);

    /*
     *  根据网卡名称查询网卡信息
     *  @return 是否成功JSON
     * */
    @GetMapping(value = "/getByName" )
    @ApiOperation(value = "修改网卡信息", notes = "修改网卡信息", tags = {"CMDB NetworkCardProgram API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "修改成功", response = String.class),
            @ApiResponse(code = 500, message = "内部错误")})
    CmdbInstanceNetworkCard getNetwordCardByName(@RequestParam("name") String name);
}
