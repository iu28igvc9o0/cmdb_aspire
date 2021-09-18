package com.aspire.mirror.composite.service.cmdb.instance;

import com.aspire.ums.cmdb.common.Result;
import com.aspire.ums.cmdb.instance.payload.CmdbInstancePortQuery;
import com.aspire.ums.cmdb.instance.payload.CmdbInstancePortRelation;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: IInstancePortRelAPI
 * Author:   hangfang
 * Date:     2019/9/5
 * Description: DESCRIPTION
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@RequestMapping("${version}/cmdb/instancePortRelation/")
public interface IInstancePortRelAPI {
    /**
     * 根据资源池获取ci信息
     */
    @RequestMapping(value = "/getInstanceIpByPool", method = RequestMethod.GET)
    @ApiOperation(value = "获取CI表头", notes = "获取CI表头", tags = {"CMDB InstancePort API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    List<Map<String, String>> getInstanceIpByPool(@RequestParam("pool") String pool);

    /**
     * 根据端口和id信息确认唯一
     */
    @RequestMapping(value = "/selectByPortAndId", method = RequestMethod.POST)
    @ApiOperation(value = "根据端口和id信息确认唯一", notes = "根据端口和id信息确认唯一", tags = {"CMDB InstancePort API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    Boolean selectByPortAndId(@RequestBody CmdbInstancePortRelation portRelation);
    /**
     * 获取端口关联设备列表
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation(value = "获取端口关联设备列表", notes = "获取端口关联设备列表", tags = {"CMDB InstancePort API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    Result<CmdbInstancePortRelation> list(@RequestBody CmdbInstancePortQuery instancePortQuery);


    /**
     * 删除端口关联设备列表
     */
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    @ApiOperation(value = "删除端口关联设备", notes = "删除端口关联设备列表", tags = {"CMDB InstancePort API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "删除成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    Map<String, Object> delete(@RequestParam("id") String id);

    /**
     * 新增端口关联设备列表
     */
    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    @ApiOperation(value = "新增端口关联设备列表", notes = "新增端口关联设备列表", tags = {"CMDB InstancePort API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "新增成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    Map<String, Object> insert(@RequestBody CmdbInstancePortRelation instancePortRelation);
}
