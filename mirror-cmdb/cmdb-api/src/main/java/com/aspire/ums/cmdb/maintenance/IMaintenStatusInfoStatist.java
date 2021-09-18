package com.aspire.ums.cmdb.maintenance;

import com.aspire.ums.cmdb.common.Result;
import com.aspire.ums.cmdb.maintenance.payload.MaintenStatistAnalysisRequest;
import com.aspire.ums.cmdb.maintenance.payload.MaintenStatusInfoStatistRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;

@Api(value = "维保状态信息统计接口类")
@RequestMapping("/cmdb/mainten/statistic")
public interface IMaintenStatusInfoStatist {

    /**
     *  以维保项目在建、已建、过保、即将过保的4个维度去统计维保数量和维保设备数量
     * @return 模型列表
     */
    @PostMapping(value = "/statusInfo" )
    @ApiOperation(value = "以维保项目在建、已建、过保、即将过保的4个维度去统计维保数量和维保设备数量", notes = "以维保项目在建、已建、过保、即将过保的4个维度去统计维保数量和维保设备数量", tags = {"CMDB IMaintenStatusInfoStatist API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = String.class),
            @ApiResponse(code = 500, message = "内部错误")})
    List<Map<String,Object>> statistMaintenStatusInfo(@RequestBody MaintenStatusInfoStatistRequest request);

    /**
     *  查看统计的维保项目
     * @return 模型列表
     */
    @PostMapping(value = "/maintenList" )
    @ApiOperation(value = "查看统计的维保项目", notes = "查看统计的维保项目", tags = {"CMDB IMaintenStatusInfoStatist API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = String.class),
            @ApiResponse(code = 500, message = "内部错误")})
    Result<Map<String,Object>> getMaintenProjectList(@RequestBody MaintenStatusInfoStatistRequest request);
}
