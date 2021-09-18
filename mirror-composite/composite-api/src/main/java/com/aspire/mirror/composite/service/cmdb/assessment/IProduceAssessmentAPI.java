package com.aspire.mirror.composite.service.cmdb.assessment;

import com.aspire.mirror.composite.service.cmdb.payload.CompITDeviceCoutRequest;
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
 * FileName: IProduceAssessmentAPI
 * Author:   hangfang
 * Date:     2019/7/10
 * Description: DESCRIPTION
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@RequestMapping("${version}/cmdb/assessment/")
public interface IProduceAssessmentAPI {

    /**
     * 查询所有故障事件信息
     * @return 故障事件信息
     */
    @RequestMapping(value = "/assessment/save", method = RequestMethod.POST)
    @ApiOperation(value = "新增厂家设备评分", notes = "新增厂家设备评分", tags = {"CMDB assessment API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "新增成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    Map<String, Object> save(@RequestBody CompITDeviceCoutRequest coutRequest);
    /**
     * 批量审批所有信息
     */
    @RequestMapping(value = "/assessment/approval", method = RequestMethod.PUT)
    @ApiOperation(value = "批量审批所有信息", notes = "批量审批所有信息", tags = {"CMDB assessment API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "审批成功", response = void.class),
            @ApiResponse(code = 500, message = "审批错误")})
    Map<String, Object> approval(@RequestParam("status") Integer status,
                                 @RequestParam("province") String province,
                                 @RequestParam("quarter") String quarter);

    /**
     * 生成评分信息
     */
    @RequestMapping(value = "/assessment/makeevaluate", method = RequestMethod.POST)
    @ApiOperation(value = "生成评分信息", notes = "生成评分信息", tags = {"CMDB assessment API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "生成成功", response = Map.class),
            @ApiResponse(code = 500, message = "生成错误")})
    Map<String, Object> makeEvaluate(@RequestParam("quarter") String quarter, @RequestParam("deviceType") String deviceType);

    /**
     * 导出评分信息
     */
    @RequestMapping(value = "/assessment/exportevaluate", method = RequestMethod.POST)
    @ApiOperation(value = "生成评分信息", notes = "生成评分信息", tags = {"CMDB assessment API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "生成成功", response = Map.class),
            @ApiResponse(code = 500, message = "生成错误")})
    Map<String, Object> exportEvaluate(@RequestParam("quarter") String quarter, @RequestParam("deviceType") String deviceType);
}
