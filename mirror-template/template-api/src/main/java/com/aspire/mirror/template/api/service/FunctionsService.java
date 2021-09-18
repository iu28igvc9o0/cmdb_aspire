//package com.aspire.mirror.template.api.service;
//
//import com.aspire.mirror.template.api.dto.FunctionsCreateRequest;
//import com.aspire.mirror.template.api.dto.FunctionsCreateResponse;
//import com.aspire.mirror.template.api.dto.FunctionsUpdateRequest;
//import com.aspire.mirror.template.api.dto.FunctionsUpdateResponse;
//import com.aspire.mirror.template.api.dto.vo.FunctionsVO;
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
// * functions对外暴露接口
// *
// * 项目名称:  mirror平台
// * 包:       com.aspire.mirror.template.api.service
// * 类名称:    FunctionsService.java
// * 类描述:    functions对外暴露接口层
// * 创建人:    JinSu
// * 创建时间:  2018-07-27 13:48:08
// */
//@Api(value = "functions")
//public interface FunctionsService{
//    /**
//     * 创建functions信息
//     *
//     * @param functionsCreateRequest functions创建请求对象
//     * @return FunctionsCreateResponse functions创建响应对象
//     */
//    @PostMapping(value="/v1/functions/insert", produces = MediaType.APPLICATION_JSON_VALUE)
//    @ApiOperation(value="创建functions",notes="创建functions",response=FunctionsCreateResponse.class,tags={ "/v1/functions" })
//    @ApiResponses(value = {
//    @ApiResponse(code = 200, message = "返回", response =  FunctionsCreateResponse.class),
//    @ApiResponse(code = 500, message = "Unexpected error", response = FunctionsCreateResponse.class) })
//    FunctionsCreateResponse createdFunctions(@RequestBody FunctionsCreateRequest functionsCreateRequest);
//    /**
//     * 根据主键删除单条functions信息
//     *
//     * @param functionId 主键
//     * @@return Result 返回结果
//     */
//    @DeleteMapping(value = "/v1/functions/{functions}")
//    @ApiOperation(value = "删除单条functions信息", notes = "删除单条functions信息",
//    tags = {"/v1/functions"})
//    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功")})
//    ResponseEntity<String> deleteByPrimaryKey(@PathVariable("function_id") String functionId);
//
//    /**
//     * 根据主键删除多条functions信息
//     *
//     * @param functionIds 主键（以逗号分隔）
//     * @@return Result 返回结果
//     */
//    @DeleteMapping(value = "/v1/functions/{function_id}")
//    @ApiOperation(value = "删除多条functions信息", notes = "删除多条functions信息",
//    tags = {"/v1/functions"})
//    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功")})
//    ResponseEntity<String> deleteByPrimaryKeyArrays(@PathVariable("function_ids") String functionIds);
//
//    /**
//     * 根据主键修改functions信息
//     *
//     * @param functionsUpdateRequest functions修改请求对象
//     * @return FunctionsUpdateResponse functions修改响应对象
//     */
//    @PutMapping(value="/v1/functions/{function_id}", produces = MediaType.APPLICATION_JSON_VALUE)
//    @ApiOperation(value="修改functions",notes="修改functions",response=FunctionsUpdateResponse.class,tags={ "/v1/functions" })
//    @ApiResponses(value = {
//    @ApiResponse(code = 200, message = "返回", response =  FunctionsUpdateResponse.class),
//    @ApiResponse(code = 500, message = "Unexpected error", response = FunctionsUpdateResponse.class) })
//    FunctionsUpdateResponse modifyByPrimaryKey(@PathVariable("function_id") String functionId, @RequestBody FunctionsUpdateRequest functionsUpdateRequest);
//
//    /**
//     * 根据主键查找functions详情信息
//     *
//     * @param functionId functions主键
//     * @return FunctionsVO functions详情响应对象
//     */
//    @GetMapping(value = "/v1/functions/{function_id}")
//    @ApiOperation(value = "详情", notes = "根据functionId获取functions详情", tags = {"/v1/functions"})
//    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功", response = FunctionsVO.class),
//    @ApiResponse(code = 500, message = "内部错误")})
//    FunctionsVO findByPrimaryKey(@PathVariable("function_id") String functionId);
//
//    /**
//     * 根据主键查询functions集合信息
//     *
//     * @param functionIds functions主键(多个以逗号分隔)
//     * @return List<FunctionsVO> functions查询响应对象
//     */
//    @GetMapping(value = "/v1/functions/list/{function_id}")
//    @ApiOperation(value = "查询", notes = "查询", tags = {"/v1/functions"})
//    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功"),
//    @ApiResponse(code = 500, message = "内部错误")})
//    List<FunctionsVO> listByPrimaryKeyArrays(@PathVariable("function_id") String functionIds);
//
//}
