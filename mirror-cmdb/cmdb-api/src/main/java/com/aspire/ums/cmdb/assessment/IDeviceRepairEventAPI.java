package com.aspire.ums.cmdb.assessment;

import com.alibaba.fastjson.JSONObject;
import com.aspire.ums.cmdb.assessment.payload.CmdbDeviceRepairEvent;
import com.aspire.ums.cmdb.common.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: IDeviceRepairEventAPI
 * Author:   hangfang
 * Date:     2019/6/25
 * Description: DESCRIPTION
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */

@Api(value = "设备维修事件接口类")
@RequestMapping("/cmdb/device")
public interface IDeviceRepairEventAPI {

    /**
     * 查询所有设备维修事件
     * @return 设备量
     */
    @RequestMapping(value = "/repair/list", method = RequestMethod.POST)
    @ApiOperation(value = "查询所有设备维修事件", notes = "查询所有设备维修事件", tags = {"CMDB device API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = void.class),
            @ApiResponse(code = 500, message = "内部错误")})
    Result<CmdbDeviceRepairEvent> list(@RequestParam(value = "pageNum", required = false) Integer pageNum,
                                       @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                       @RequestBody CmdbDeviceRepairEvent deviceRepairEvent);


    /**
     * 存储设备维修事件
     */
    @RequestMapping(value = "/repair/save", method = RequestMethod.POST)
    @ApiOperation(value = "存储设备维修事件", notes = "存储设备维修事件", tags = {"CMDB device API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "存储成功", response = void.class),
            @ApiResponse(code = 500, message = "内部错误")})
    Map<String, Object> save(@RequestBody JSONObject data);

    /**
     * 删除设备维修事件
     */
    @RequestMapping(value = "/repair/delete/{id}", method = RequestMethod.DELETE)
    @ApiOperation(value = "删除设备维修事件", notes = "删除设备维修事件", tags = {"CMDB device API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "删除成功", response = void.class),
            @ApiResponse(code = 500, message = "内部错误")})
    Map<String, Object> delete(@PathVariable("id") String id);
}
