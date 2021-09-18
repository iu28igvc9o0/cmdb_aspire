package com.aspire.ums.cmdb.assessment;

import com.aspire.ums.cmdb.assessment.payload.CmdbServiceAssess;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 项目名称:
 * 包: com.aspire.ums.cmdb.assessment
 * 类名称:
 * 类描述:
 * 创建人: PJX
 * 创建时间: 2019/7/15 15:36
 * 版本: v1.0
 */
@RequestMapping("/cmdb/device")
public interface IServiceAssessAPI {
//    @GetMapping("/serviceAssess/insert")
//    @ApiOperation(value = "新增统计数据", notes = "新增统计数据", tags = {"CMDB Server Access API"})
//    @ApiResponses(value = {@ApiResponse(code = 200, message = "新增统计数据成功"),
//            @ApiResponse(code = 500, message = "内部错误")})
//    Map<String,Object> insert(@RequestParam(value = "quarter") String quarter);
    
    @PostMapping("/serviceAssess/queryAllData")
    @ApiOperation(value = "查询统计数据", notes = "查询统计数据", tags = {"CMDB Server Access API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询统计数据成功"),
            @ApiResponse(code = 500, message = "内部错误")})
    List<CmdbServiceAssess> queryAllData(@RequestParam(value = "device_type",required = false) String device_type,
                                           @RequestParam(value = "quarter") String quarter);
    
    @PostMapping("/serviceAssess/saveScore")
    @ApiOperation(value = "保存总得分", notes = "保存总得分", tags = {"CMDB Server Access API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "保存总得分成功"),
            @ApiResponse(code = 500, message = "内部错误")})
    Map<String,Object> saveScore(@RequestBody List<CmdbServiceAssess> list);

    /**
     * 获取厂商评分下设备类型评分状态
     * @param quarter 实例数据
     * @return
     */
    @RequestMapping(value = "/serviceAssess/getScoreDeviceTypeStatus", method = RequestMethod.GET)
    @ApiOperation(value = "获取厂商评分下设备类型评分状态", notes = "获取厂商评分下设备类型评分状态", tags = {"CMDB Server Access API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = void.class),
            @ApiResponse(code = 500, message = "内部错误")})
    List<Map<String, Object>> getScoreDeviceTypeStatus(@RequestParam("quarter") String quarter);
}
