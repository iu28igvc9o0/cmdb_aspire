//package com.aspire.mirror.template.api.service;
//
//import com.aspire.mirror.template.api.dto.TemplateDeviceCreateRequest;
//import com.aspire.mirror.template.api.dto.TemplateDeviceCreateResponse;
//import com.aspire.mirror.template.api.dto.TemplateDeviceUpdateRequest;
//import com.aspire.mirror.template.api.dto.TemplateDeviceUpdateResponse;
//import com.aspire.mirror.template.api.dto.vo.TemplateDeviceVO;
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiOperation;
//import io.swagger.annotations.ApiResponse;
//import io.swagger.annotations.ApiResponses;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
///**
// * template_device对外暴露接口
// *
// * 项目名称:  mirror平台
// * 包:       com.aspire.mirror.template.api.service
// * 类名称:    TemplateDeviceService.java
// * 类描述:    template_device对外暴露接口层
// * 创建人:    JinSu
// * 创建时间:  2018-07-27 13:48:08
// */
//@Api(value = "template_device")
//public interface TemplateDeviceService{
//    /**
//     * 创建template_device信息
//     *
//     * @param templateDeviceCreateRequest template_device创建请求对象
//     * @return TemplateDeviceCreateResponse template_device创建响应对象
//     */
//    @PostMapping(value="/v1/template_device/insert", produces = MediaType.APPLICATION_JSON_VALUE)
//    @ApiOperation(value="创建template_device",notes="创建template_device",response=TemplateDeviceCreateResponse.class,tags={ "/v1/template_device" })
//    @ApiResponses(value = {
//    @ApiResponse(code = 200, message = "返回", response =  TemplateDeviceCreateResponse.class),
//    @ApiResponse(code = 500, message = "Unexpected error", response = TemplateDeviceCreateResponse.class) })
//    TemplateDeviceCreateResponse createdTemplateDevice(@RequestBody TemplateDeviceCreateRequest templateDeviceCreateRequest);
//    /**
//     * 根据主键删除单条template_device信息
//     *
//     * @param templateDeviceId 主键
//     * @@return Result 返回结果
//     */
//    @DeleteMapping(value = "/v1/template_device/{template_device}")
//    @ApiOperation(value = "删除单条template_device信息", notes = "删除单条template_device信息",
//    tags = {"/v1/template_device"})
//    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功")})
//    ResponseEntity<String> deleteByPrimaryKey(@PathVariable("template_device_id") String templateDeviceId);
//
//    /**
//     * 根据主键删除多条template_device信息
//     *
//     * @param templateDeviceIds 主键（以逗号分隔）
//     * @@return Result 返回结果
//     */
//    @DeleteMapping(value = "/v1/template_device/{template_device_id}")
//    @ApiOperation(value = "删除多条template_device信息", notes = "删除多条template_device信息",
//    tags = {"/v1/template_device"})
//    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功")})
//    ResponseEntity<String> deleteByPrimaryKeyArrays(@PathVariable("template_device_ids") String templateDeviceIds);
//
//    /**
//     * 根据主键修改template_device信息
//     *
//     * @param templateDeviceUpdateRequest template_device修改请求对象
//     * @return TemplateDeviceUpdateResponse template_device修改响应对象
//     */
//    @PutMapping(value="/v1/template_device/{template_device_id}", produces = MediaType.APPLICATION_JSON_VALUE)
//    @ApiOperation(value="修改template_device",notes="修改template_device",response=TemplateDeviceUpdateResponse.class,tags={ "/v1/template_device" })
//    @ApiResponses(value = {
//    @ApiResponse(code = 200, message = "返回", response =  TemplateDeviceUpdateResponse.class),
//    @ApiResponse(code = 500, message = "Unexpected error", response = TemplateDeviceUpdateResponse.class) })
//    TemplateDeviceUpdateResponse modifyByPrimaryKey(@PathVariable("template_device_id") String templateDeviceId, @RequestBody TemplateDeviceUpdateRequest templateDeviceUpdateRequest);
//
//    /**
//     * 根据主键查找template_device详情信息
//     *
//     * @param templateDeviceId template_device主键
//     * @return TemplateDeviceVO template_device详情响应对象
//     */
//    @GetMapping(value = "/v1/template_device/{template_device_id}")
//    @ApiOperation(value = "详情", notes = "根据templateDeviceId获取template_device详情", tags = {"/v1/template_device"})
//    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功", response = TemplateDeviceVO.class),
//    @ApiResponse(code = 500, message = "内部错误")})
//    TemplateDeviceVO findByPrimaryKey(@PathVariable("template_device_id") String templateDeviceId);
//
//    /**
//     * 根据主键查询template_device集合信息
//     *
//     * @param templateDeviceIds template_device主键(多个以逗号分隔)
//     * @return List<TemplateDeviceVO> template_device查询响应对象
//     */
//    @GetMapping(value = "/v1/template_device/list/{template_device_id}")
//    @ApiOperation(value = "查询", notes = "查询", tags = {"/v1/template_device"})
//    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功"),
//    @ApiResponse(code = 500, message = "内部错误")})
//    List<TemplateDeviceVO> listByPrimaryKeyArrays(@PathVariable("template_device_id") String templateDeviceIds);
//
//}
