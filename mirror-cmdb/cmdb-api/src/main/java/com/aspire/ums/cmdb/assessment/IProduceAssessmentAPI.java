package com.aspire.ums.cmdb.assessment;

import com.aspire.ums.cmdb.assessment.payload.CmdbProduceAssessment;
import io.swagger.annotations.Api;
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
@Api(value = "设备厂家接口类")
@RequestMapping("/cmdb/assessment")
public interface IProduceAssessmentAPI {

    /**
     * 新增厂家设备评分
     * @return 故障事件信息
     */
    @RequestMapping(value = "/assessment/save", method = RequestMethod.POST)
    @ApiOperation(value = "新增厂家设备评分", notes = "新增厂家设备评分", tags = {"CMDB assessment API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "新增成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    Map<String, Object> save(@RequestBody List<CmdbProduceAssessment> produceAssessments);

    /**
     * 根据countid查询评分数据
     */
    @RequestMapping(value = "/assessment/listByCountIds", method = RequestMethod.POST)
    @ApiOperation(value = "根据countid查询评分数据", notes = "根据countid查询评分数据", tags = {"CMDB assessment API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    List<CmdbProduceAssessment> listByCountIds(List<String> countIds);

    /**
     * 批量审批所有信息
     */
    @RequestMapping(value = "/assessment/approval", method = RequestMethod.PUT)
    @ApiOperation(value = "批量审批所有信息", notes = "批量审批所有信息", tags = {"CMDB assessment API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "审批成功", response = void.class),
            @ApiResponse(code = 500, message = "审批错误")})
    Map<String, Object> approval(@RequestParam("status") Integer status,
                                 @RequestParam("province") String province,
                                 @RequestParam("quarter") String quarter
                                 );

    /**
     * 生成评分信息
     */
    @RequestMapping(value = "/assessment/makeevaluate", method = RequestMethod.POST)
    @ApiOperation(value = "生成评分信息", notes = "生成评分信息", tags = {"CMDB assessment API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "生成成功", response = void.class),
            @ApiResponse(code = 500, message = "生成错误")})
    Map<String, Object> makeEvaluate(@RequestParam("deviceType") String deviceType,
                                 @RequestParam("quarter") String quarter);

    /**
     * 导出评分信息
     */
    @RequestMapping(value = "/assessment/exportevaluate", method = RequestMethod.POST)
    @ApiOperation(value = "导出评分信息", notes = "导出评分信息", tags = {"CMDB assessment API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "导出成功", response = void.class),
            @ApiResponse(code = 500, message = "导出错误")})
    Map<String, Object> exportEvaluate(@RequestParam("deviceType") String deviceType,
                                     @RequestParam("quarter") String quarter);
}
