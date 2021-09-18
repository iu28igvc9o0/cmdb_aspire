package com.aspire.ums.cmdb.assessment;

import com.alibaba.fastjson.JSONObject;
import com.aspire.ums.cmdb.allocate.payload.Result;
import com.aspire.ums.cmdb.assessment.payload.CmdbProblemEvent;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 项目名称:
 * 包: com.aspire.ums.cmdb.faultEvent
 * 类名称:
 * 类描述: 故障事件信息
 * 创建人: PJX
 * 创建时间: 2019/6/25 20:12
 * 版本: v1.0
 */
@RequestMapping("/cmdb/device")
public interface IProblemEventAPI {
    
    /**
     * 查询所有故障事件信息
     * @return 故障事件信息
     */
    @GetMapping("/problemEvent/list")
    @ApiOperation(value = "获取故障事件信息列表", notes = "获取故障事件信息列表", tags = {"CMDB Problem Event API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    Result<CmdbProblemEvent> getAllData(@RequestParam(value = "pageNum", required = false) int pageNum,
                                        @RequestParam(value = "pageSize", required = false) int pageSize,
                                        @RequestParam(value = "province", required = false) String province,
                                        @RequestParam(value = "quarter", required = false) String quarter,
                                        @RequestParam(value = "createUsername", required = false) String createUsername);
    
    /**
     * 新增更新故障事件信息
     */
    @PostMapping("/problemEvent/insertOrUpdate")
    @ApiOperation(value = "新增或更新", notes = "新增或更新", tags = {"CMDB Problem Event API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "新增或更新成功"),
            @ApiResponse(code = 500, message = "内部错误")})
    Map<String, Object> insertOrUpdate(@RequestBody JSONObject data);
    
    /**
     * 删除故障事件信息
     */
    @DeleteMapping("/problemEvent/delete/{id}")
    @ApiOperation(value = "删除", notes = "删除", tags = {"CMDB Problem Event API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "删除成功"),
            @ApiResponse(code = 500, message = "内部错误")})
    Map<String, Object> delete(@PathVariable("id") String id);
}
