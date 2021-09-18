package com.aspire.mirror.template.api.service;

import com.aspire.mirror.common.entity.PageResponse;
import com.aspire.mirror.template.api.dto.*;
import com.aspire.mirror.template.api.dto.vo.ZabbixTemplateSyncVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 模板对外暴露接口
 *
 * 项目名称:  mirror平台
 * 包:       com.aspire.mirror.template.api.service
 * 类名称:    TemplateService.java
 * 类描述:    模板对外暴露接口层
 * 创建人:    JinSu
 * 创建时间:  2018-07-27 13:48:08
 */
@Api(value = "template")
public interface TemplateService{
    /**
     * 创建模板信息
     *
     * @param templateCreateRequest 模板创建请求对象
     * @return TemplateCreateResponse 模板创建响应对象
     */
    @PostMapping(value="/v1/template/create", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value="创建模板",notes="创建模板",response=TemplateCreateResponse.class,tags={ "Template API" })
    @ApiResponses(value = {
    @ApiResponse(code = 200, message = "返回", response =  TemplateCreateResponse.class),
    @ApiResponse(code = 500, message = "Unexpected error", response = TemplateCreateResponse.class) })
    TemplateCreateResponse createdTemplate(@RequestBody TemplateCreateRequest templateCreateRequest);


    /**
     * 根据主键删除多条模板信息
     *
     * @param templateIds 主键（以逗号分隔）
     * @@return Result 返回结果
     */
    @DeleteMapping(value = "/v1/template/{template_ids}")
    @ApiOperation(value = "删除多条模板信息", notes = "删除多条模板信息",
    tags = {"Template API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功")})
    ResponseEntity<String> deleteByPrimaryKeyArrays(@PathVariable("template_ids") String templateIds);

    /**
     * 根据主键修改模板信息
     *
     * @param templateUpdateRequest 模板修改请求对象
     * @return TemplateUpdateResponse 模板修改响应对象
     */
    @PutMapping(value="/v1/template/{template_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value="修改模板",notes="修改模板",response=TemplateUpdateResponse.class,tags={ "Template API" })
    @ApiResponses(value = {
    @ApiResponse(code = 200, message = "返回", response =  TemplateUpdateResponse.class),
    @ApiResponse(code = 500, message = "Unexpected error", response = TemplateUpdateResponse.class) })
    TemplateUpdateResponse modifyByPrimaryKey(@PathVariable("template_id") String templateId, @RequestBody TemplateUpdateRequest templateUpdateRequest);

    /**
     * 根据主键查找模板详情信息
     *
     * @param templateId 模板主键
     * @return TemplateDetailResponse 模板详情响应对象
     */
    @GetMapping(value = "/v1/template/{template_id}")
    @ApiOperation(value = "详情", notes = "根据templateId获取模板详情", tags = {"Template API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功", response = TemplateDetailResponse.class),
    @ApiResponse(code = 500, message = "内部错误")})
    TemplateDetailResponse findByPrimaryKey(@PathVariable("template_id") String templateId);

    /**
     * 根据名称查找模板详情信息
     *
     * @param templateName 模板名称
     * @return TemplateDetailResponse 模板详情响应对象
     */
    @GetMapping(value = "/v1/template/findByName/{template_name}")
    @ApiOperation(value = "详情", notes = "根据templateId获取模板详情", tags = {"Template API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功", response = TemplateDetailResponse.class),
            @ApiResponse(code = 500, message = "内部错误")})
    TemplateDetailResponse findByName(@PathVariable("template_name") String templateName);

    /**
     * 根据page对象查询模板列表
     *
     * @param request 模板请求页对象
     * @return TemplateVO 模板详情响应对象
     */
    @PostMapping(value = "/v1/template/pageList")
    @ApiOperation(value = "列表", notes = "获取模板列表", tags = {"Template API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功", response = PageResponse.class),
            @ApiResponse(code = 500, message = "内部错误")})
    PageResponse<TemplateDetailResponse> pageList(@RequestBody TemplatePageRequest request);

    /**
     * 根据主键查询模板集合信息
     *
     * @param templateIds 模板主键(多个以逗号分隔)
     * @return List<TemplateDetailResponse> 模板查询响应对象
     */
    @GetMapping(value = "/v1/template/list/{template_ids}")
    @ApiOperation(value = "查询", notes = "查询", tags = {"Template API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功"),
    @ApiResponse(code = 500, message = "内部错误")})
    List<TemplateDetailResponse> listByPrimaryKeyArrays(@PathVariable("template_ids") String templateIds);

    /**
     * 根据templateId拷贝模板信息
     *
     * @param templateId 模板ID
     * @return CompTemplateCreateResponse 模板详情响应对象
     */
    @PostMapping(value = "/v1/template/copy/{template_id}")
    @ApiOperation(value = "复制模板", notes = "复制模板", tags = {"Template API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功", response = PageResponse.class),
            @ApiResponse(code = 500, message = "内部错误")})
    TemplateCreateResponse copy(@PathVariable("template_id") String templateId);

    @PostMapping(value = "/v1/template/zabbixTemplateSync")
    @ApiOperation(value = "zabbix模板上报", notes = "zabbix模板上报", tags = {"Template API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功", response = PageResponse.class),
            @ApiResponse(code = 500, message = "内部错误")})
    GeneralResponse zabbixTemplateSync(@RequestBody ZabbixTemplateSyncVo templateSyncVo);

}
