package com.aspire.mirror.composite.service.template;

import com.aspire.mirror.common.entity.PageResponse;
import com.aspire.mirror.composite.service.template.payload.*;
import com.migu.tsg.microservice.atomicservice.composite.service.common.payload.BaseResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 模板信息对外暴露接口
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.migu.tsg.microservice.atomicservice.composite.service.template
 * 类名称:    ICompTemlateService.java
 * 类描述:   模板信息对外暴露接口
 * 创建人:    JinSu
 * 创建时间:  2018/8/1 20:43
 * 版本:      v1.0
 */
@Api(value = "模板信息管理")
@RequestMapping("${version}/template")
public interface ICompTemlateService {
    /**
     * 创建模板信息
     *
     * @param createRequest 模板创建请求对象
     * @return CompTemplateCreateResponse 模板创建响应对象
     */
    @PostMapping(value = "/", produces = "application/json;charset=UTF-8")
    @ApiOperation(value = "创建模板信息", notes = "创建模板信息",
            tags = {"template API"}, response = CompTemplateCreateRequest.class)
    @ApiResponses(value = {@ApiResponse(code = 201, message = "create a new template"
            , response = CompTemplateCreateResponse.class)})
    CompTemplateCreateResponse createdTemplate(@RequestBody CompTemplateCreateRequest createRequest);
    /**
     * 根据主键删除多条模板信息
     *
     * @param templateIds 主键（以逗号分隔）
     * @@return Result 返回结果
     */
    @DeleteMapping(value = "/{template_ids}")
    @ApiOperation(value = "删除多条模板信息", notes = "删除多条模板信息",
            tags = {"template API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功")})
    BaseResponse deleteByPrimaryKeyArrays(@PathVariable("template_ids") String templateIds);

    /**
     * 根据主键修改模板信息
     *
     * @param updateRequest 模板修改请求对象
     * @return CompTemplateUpdateResponse 模板修改响应对象
     */
    @PutMapping(value="/{template_id}", produces = "application/json;charset=UTF-8")
    @ApiOperation(value="修改模板",notes="修改模板",response=CompTemplateUpdateResponse.class,tags={ "template API" })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回", response =  CompTemplateUpdateResponse.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = CompTemplateUpdateResponse.class) })
    CompTemplateUpdateResponse modifyByPrimaryKey(@PathVariable("template_id") String templateId, @RequestBody CompTemplateUpdateRequest updateRequest);

    /**
     * 根据主键查找模板详情信息
     *
     * @param templateId 模板主键
     * @return TemplateDetailResponse 模板详情响应对象
     */
    @GetMapping(value = "/{template_id}")
    @ApiOperation(value = "详情", notes = "根据templateId获取模板详情", tags = {"template API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功", response = CompTemplateDetailResponse.class),
            @ApiResponse(code = 500, message = "内部错误")})
    CompTemplateDetailResponse findByPrimaryKey(@PathVariable("template_id") String templateId);
//
    /**
     * 根据page对象查询模板列表
     *
     * @param request 模板请求页对象
     * @return TemplateVO 模板详情响应对象
     */
    @PostMapping(value = "/list")
    @ApiOperation(value = "列表", notes = "获取模板列表", tags = {"template API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功", response = PageResponse.class),
            @ApiResponse(code = 500, message = "内部错误")})
    PageResponse<CompTemplatePageResponse> list(@RequestBody CompTemplatePageRequest request);

    /**
     * 根据templateId拷贝模板信息
     *
     * @param templateId 模板ID
     * @return CompTemplateCreateResponse 模板详情响应对象
     */
    @PostMapping(value = "/copy/{template_id}")
    @ApiOperation(value = "列表", notes = "获取模板列表", tags = {"template API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功", response = PageResponse.class),
            @ApiResponse(code = 500, message = "内部错误")})
    CompTemplateCreateResponse copy(@PathVariable("template_id") String templateId);

    /**
     * zabbix模板列表
     *
     * @param room 机房
     * @return TemplateVO 模板详情响应对象
     */
    @PostMapping(value = "/zbxTemplateList/{room}")
    @ApiOperation(value = "列表", notes = "获取模板列表", tags = {"template API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功", response = PageResponse.class),
            @ApiResponse(code = 500, message = "内部错误")})
    List<ZabbixTemplateDetailResponse> zbxTemplateList(@PathVariable("room") String room);
}
