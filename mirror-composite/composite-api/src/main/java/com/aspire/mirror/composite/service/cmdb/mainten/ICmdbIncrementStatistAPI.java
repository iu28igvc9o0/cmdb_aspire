package com.aspire.mirror.composite.service.cmdb.mainten;

import com.aspire.ums.cmdb.maintenance.payload.CmdbIncrementStatistRequest;
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
 * @ClassName ICmdbIncrementStatistAPI
 * @Description 对外接口类
 * @Author luowenbo
 * @Date 2020/2/19 16:31
 * @Version 1.0
 */
@Api(value = "维保增量统计类")
@RequestMapping("/${version}/cmdb/maintenance/statist")
public interface ICmdbIncrementStatistAPI {

    /*
     *  按照时间维度，来统计每个月的设备增量
     * */
    @PostMapping(value = "/time" )
    @ApiOperation(value = "按照时间维度，来统计每个月的设备增量", notes = "按照时间维度，来统计每个月的设备增量", tags = {"Cmdb Maintenance incrementStatist API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "保存成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    List<Map<String,Object>> statistIncrementByTime(@RequestBody CmdbIncrementStatistRequest req);
}
