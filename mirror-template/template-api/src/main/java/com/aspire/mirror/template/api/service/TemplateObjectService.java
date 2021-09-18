package com.aspire.mirror.template.api.service;

import com.aspire.mirror.template.api.dto.TemplateObjectBatchCreateRequest;
import com.aspire.mirror.template.api.dto.TemplateObjectCreateResponse;
import com.aspire.mirror.template.api.dto.TemplateObjectDetailResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * template_Object对外暴露接口
 *
 * 项目名称:  mirror平台
 * 包:       com.aspire.mirror.template.api.service
 * 类名称:    TemplateObjectService.java
 * 类描述:    template_Object对外暴露接口层
 * 创建人:    JinSu
 * 创建时间:  2018-07-27 13:48:08
 */
@Api(value = "template_Object")
public interface TemplateObjectService {
    /**
     * 根据模板ID删除模板关联对象
     * @param templateIds 模板ID
     * @return ResponseEntity 返回结果
     */
    @DeleteMapping(value = "/v1/templateObject/deleteByTemplateId/{template_ids}")
    @ApiOperation(value = "删除模板关联对象", notes = "删除单条巡检信息",tags = {"/v1/templateObject"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功")})
    ResponseEntity<String> deleteByTemplateIds(@PathVariable("template_ids") String templateIds);

    /**
     * 批量创建模板关联对象
     * @param batchCreateRequst 批量创建请求
     * @return ResponseEntity 批量创建请求返回
     */
    @PostMapping(value = "/v1/templateObject/batchCreate")
    @ApiOperation(value = "批量创建模板关联对象", notes = "批量创建模板关联对象",tags = {"/v1/templateObject"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功")})
    ResponseEntity<String> batchCreate(@RequestBody TemplateObjectBatchCreateRequest batchCreateRequst);

    /**
     * 根据模板ID查询
     * @param templateId 模板Id
     * @return TaskObjectDetailResponse
     */
    @GetMapping(value = "/v1/templateObject/findByTemplateId/{template_id}")
    @ApiOperation(value = "查询模板关联对象", notes = "查询模板关联对象",tags = {"/v1/templateObject"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功", response = TemplateObjectCreateResponse.class),
            @ApiResponse(code = 500, message = "内部错误")})
    List<TemplateObjectDetailResponse> findByTemplateId(@PathVariable("template_id") String templateId);

}
