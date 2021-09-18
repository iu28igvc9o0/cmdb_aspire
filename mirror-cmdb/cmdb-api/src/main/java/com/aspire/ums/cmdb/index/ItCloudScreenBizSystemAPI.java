package com.aspire.ums.cmdb.index;

import com.aspire.ums.cmdb.index.payload.ItCloudScreenRequest;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;
import java.util.Map;

/**
 * @ClassName ItCloudScreenBizSystemAPI
 * @Description 租户利用率大屏——业务系统
 * @Author luowenbo
 * @Date 2020/3/23 14:37
 * @Version 1.0
 */
@RequestMapping("/cmdb/index")
public interface ItCloudScreenBizSystemAPI {
    /*
     *  依据条件，查询业务系统分配资源实体
     * */
    @RequestMapping(value = "/allocate/bizSystem", method = RequestMethod.POST)
    @ApiOperation(value = "依据条件，查询业务系统分配资源实体", notes = "依据条件，查询业务系统分配资源实体", tags = {"CMDB Index API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    List<Map<String,Object>> getResourceAllocateByBizSystem(@RequestBody ItCloudScreenRequest req);
    /*
     *  依据业务系统，查询免考核资源的详细信息
     * */
    @RequestMapping(value = "/bizSystem/notInspect", method = RequestMethod.POST)
    @ApiOperation(value = "依据业务系统，查询免考核资源的详细信息", notes = "依据业务系统，查询免考核资源的详细信息", tags = {"CMDB Index API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    List<Map<String,Object>> getBizSystemNotInpect(@RequestBody ItCloudScreenRequest req);

    /*
     *  全量导出数据
     * */
    @RequestMapping(value = "/export/all", method = RequestMethod.POST)
    @ApiOperation(value = "全量导出数据", notes = "全量导出数据", tags = {"CMDB Index API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    List<Map<String,Object>> allDataListExport(@RequestBody ItCloudScreenRequest req);

    /*
     *  导出部分低于利用率的数据数据
     * */
    @RequestMapping(value = "/export/part", method = RequestMethod.POST)
    @ApiOperation(value = "导出部分低于利用率的数据数据", notes = "导出部分低于利用率的数据数据", tags = {"CMDB Index API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    List<Map<String,Object>> partDataListExport(@RequestBody ItCloudScreenRequest req);
}
