package com.aspire.ums.cmdb.collectUnknown;

import com.aspire.ums.cmdb.allocate.payload.Result;
import com.aspire.ums.cmdb.collectUnknown.payload.CmdbCollectUnknown;
import com.aspire.ums.cmdb.collectUnknown.payload.CmdbCollectUnknownQuery;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;
import java.util.Map;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: ICollectUnkownAPI
 * Author:   hangfang
 * Date:     2019/10/10
 * Description: DESCRIPTION
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Api(value = "未知设备管理接口类")
@RequestMapping("/cmdb/collectUnknown")
public interface ICollectUnkownAPI {
    /**
     * 根据条件筛选未知设备
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation(value = "根据条件筛选未知采集设备", notes = "根据条件筛选未知采集设备", tags = {"CMDB CollectUnknown API"})
    @ApiResponses(value = {@ApiResponse(code = 204, message = "查询成功", response = void.class),
            @ApiResponse(code = 500, message = "内部错误")})
    Result<CmdbCollectUnknown> list(@RequestBody CmdbCollectUnknownQuery collectUnknownQuery);

    /**
     * 新增未知设备
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ApiOperation(value = "新增未知采集设备", notes = "新增未知采集设备", tags = {"CMDB CollectUnknown API"})
    @ApiResponses(value = {@ApiResponse(code = 204, message = "新增成功", response = void.class),
            @ApiResponse(code = 500, message = "内部错误")})
    Map<String, Object> insert(@RequestBody CmdbCollectUnknown collectUnknown);

    /**
     * 批量新增未知设备
     */
    @RequestMapping(value = "/saveByBatch", method = RequestMethod.POST)
    @ApiOperation(value = "批量新增未知设备", notes = "批量新增未知设备", tags = {"CMDB CollectUnknown API"})
    @ApiResponses(value = {@ApiResponse(code = 204, message = "新增成功", response = void.class),
            @ApiResponse(code = 500, message = "内部错误")})
    List<Map<String, Object>> insertByBatch(@RequestBody List<CmdbCollectUnknown> collectUnknown);

    /**
     * 更新未知设备
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ApiOperation(value = "更新未知采集设备", notes = "更新未知采集设备", tags = {"CMDB CollectUnknown API"})
    @ApiResponses(value = {@ApiResponse(code = 204, message = "更新成功", response = void.class),
            @ApiResponse(code = 500, message = "内部错误")})
    Map<String, Object> update(@RequestBody CmdbCollectUnknown collectUnknown);

    /**
     * 维护未知设备
     */
    @RequestMapping(value = "/maintainByBatch", method = RequestMethod.POST)
    @ApiOperation(value = "维护未知设备", notes = "维护未知设备", tags = {"CMDB CollectUnknown API"})
    @ApiResponses(value = {@ApiResponse(code = 204, message = "维护成功", response = void.class),
            @ApiResponse(code = 500, message = "内部错误")})
    Map<String, Object> maintain(@RequestBody CmdbCollectUnknown collectUnknown);
}
