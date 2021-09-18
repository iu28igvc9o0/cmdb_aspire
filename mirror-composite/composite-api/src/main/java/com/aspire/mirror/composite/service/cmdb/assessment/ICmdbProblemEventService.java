package com.aspire.mirror.composite.service.cmdb.assessment;

import com.alibaba.fastjson.JSONObject;
import com.aspire.ums.cmdb.allocate.payload.Result;
import com.aspire.ums.cmdb.assessment.payload.CmdbProblemEvent;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 项目名称:
 * 包: com.aspire.mirror.composite.service.cmdb
 * 类名称:
 * 类描述: 故障事件信息
 * 创建人: PJX
 * 创建时间: 2019/6/25 21:52
 * 版本: v1.0
 */
@Api(value = "故障事件信息")
@RequestMapping("${version}/cmdb/device")
public interface ICmdbProblemEventService {
    /**
     * 查询所有故障事件信息
     * @param pageNum
     * @param pageSize
     * @param province
     * @param createUsername
     * @return
     */
    @GetMapping("/problemEvent/list")
    @ApiOperation(value = "获取故障事件信息列表", notes = "获取故障事件信息列表", tags = {"CMDB Problem Event API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    Result<CmdbProblemEvent> getAllData(@RequestParam(value = "pageNum", required = false) int pageNum,
                                        @RequestParam(value = "pageSize", required = false) int pageSize,
                                        @RequestParam(value = "province", required = false) String province,
                                        @RequestParam(value = "quarter", required = false) String quarter,
                                        @RequestParam(value = "page", required = false) String page,
                                        @RequestParam(value = "createUsername", required = false) String createUsername);
    
    /**
     * 新增更新故障事件信息
     * @param data
     * @return
     */
    @PostMapping("/problemEvent/insertOrUpdate")
    @ApiOperation(value = "新增或更新", notes = "新增或更新", tags = {"CMDB Problem Event API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "新增或更新成功"),
            @ApiResponse(code = 500, message = "内部错误")})
    Map<String, Object> insertOrUpdate(@RequestBody JSONObject data);
    
    @DeleteMapping("/problemEvent/delete/{id}")
    @ApiOperation(value = "删除", notes = "删除", tags = {"CMDB Problem Event API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "删除成功"),
            @ApiResponse(code = 500, message = "内部错误")})
    Map<String, Object> delete(@PathVariable("id") String id);
}
