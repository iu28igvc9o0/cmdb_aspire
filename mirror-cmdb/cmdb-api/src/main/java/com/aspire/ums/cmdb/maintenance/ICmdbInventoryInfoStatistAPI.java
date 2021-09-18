package com.aspire.ums.cmdb.maintenance;

import com.aspire.ums.cmdb.common.Result;
import com.aspire.ums.cmdb.maintenance.payload.CmdbInventoryInfoStatistRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;

/**
 * FileName: ICmdbInventoryInfoStatistAPI
 * Author:   luowenbo
 * Date:     2020/2/16 15:53
 * Description: 维保设备信息与cmdb存量信息比对 控制层接口类
 */

@Api(value = "维保设备信息与cmdb存量信息比对接口")
@RequestMapping("/cmdb/maintenance/inventory")
public interface ICmdbInventoryInfoStatistAPI {

    /**
     *  页面第一层接口
     * @return
     */
    @PostMapping(value = "/first" )
    @ApiOperation(value = "页面第一层接口", notes = "页面第一层接口", tags = {"Cmdb Maintenance inventory API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "保存成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    List<Map<String,Object>> firstLayer();

    /**
     *  页面第二层接口
     * @return
     */
    @PostMapping(value = "/second" )
    @ApiOperation(value = "页面第二层接口", notes = "页面第二层接口", tags = {"Cmdb Maintenance inventory API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "保存成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    Result<Map<String,Object>> secondLayer(@RequestBody CmdbInventoryInfoStatistRequest req);

    /**
     *  页面第三层接口
     * @return
     */
    @PostMapping(value = "/third" )
    @ApiOperation(value = "页面第三层接口", notes = "页面第三层接口", tags = {"Cmdb Maintenance inventory API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "保存成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    Result<Map<String,Object>> thirdLayer(@RequestBody CmdbInventoryInfoStatistRequest req);
}
