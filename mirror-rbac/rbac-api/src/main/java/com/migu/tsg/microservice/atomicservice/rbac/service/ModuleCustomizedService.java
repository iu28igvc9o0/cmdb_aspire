package com.migu.tsg.microservice.atomicservice.rbac.service;

import com.aspire.mirror.common.entity.PageResponse;
import com.aspire.mirror.common.entity.PageResult;
import com.migu.tsg.microservice.atomicservice.rbac.dto.ModuleCustomizedCreateRequest;
import com.migu.tsg.microservice.atomicservice.rbac.dto.ModuleCustomizedCreateResponse;
import com.migu.tsg.microservice.atomicservice.rbac.dto.ModuleCustomizedPageRequest;
import com.migu.tsg.microservice.atomicservice.rbac.dto.ModuleCustomizedUpdateRequest;
import com.migu.tsg.microservice.atomicservice.rbac.dto.ModuleCustomizedUpdateResponse;
import com.migu.tsg.microservice.atomicservice.rbac.dto.model.ModuleCustomizedDTO;
import com.migu.tsg.microservice.atomicservice.rbac.dto.vo.ModuleCustomizedVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * 对外暴露接口
 * <p>
 * 项目名称:  mirror平台
 * 包:     com.aspire.mirror.configManagement.entity   
 * 类名称:     ModuleCustomized
 * 类描述:     对外暴露接口层
 * 创建人:     曾祥华
 * 创建时间:     2019-07-17 14:04:59
 */
 
 @Api(value = "")
public interface ModuleCustomizedService {
	/**
     * 创建信息
     *
     * @param moduleCustomizedCreateRequest 动作创建请求对象
     * @return ModuleCustomizedCreateResponse 动作创建响应对象
     */
    @PostMapping(value = "/v1/moduleCustomized/insert", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "创建", notes = "创建", response = ModuleCustomizedCreateResponse.class, tags = {"/v1/moduleCustomized"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回", response = ModuleCustomizedCreateResponse.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = ModuleCustomizedCreateResponse.class)})
    ModuleCustomizedCreateResponse createdModuleCustomized(@RequestBody ModuleCustomizedCreateRequest
                                                                   moduleCustomizedCreateRequest);

   /**
     * 根据主键删除单作信息
     *
     * @param moduleCustomizedId 主键
     * @@return Result 返回结果
     */
    @DeleteMapping(value = "/v1/moduleCustomized/{user_id}")
    @ApiOperation(value = "删除单条信息", notes = "删除单条信息",
            tags = {"/v1/moduleCustomized"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功")})
    ResponseEntity<String> deleteByPrimaryKey(@PathVariable("user_id") String moduleCustomizedId);

    /**
     * 根据主键修改信息
     *
     * @param moduleCustomizedUpdateRequest 修改请求对象
     * @return ModuleCustomizedUpdateResponse 修改响应对象
     */
    @PutMapping(value = "/v1/moduleCustomized", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "修改", notes = "修改", response = ModuleCustomizedUpdateResponse.class, tags = {"/v1/moduleCustomized"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回", response = ModuleCustomizedUpdateResponse.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = ModuleCustomizedUpdateResponse.class)})
    ModuleCustomizedUpdateResponse modifyByPrimaryKey(@RequestBody
                                                              ModuleCustomizedUpdateRequest moduleCustomizedUpdateRequest);
            
    /**
     * 根据主键查作详情信息
     *
     * @param moduleCustomizedId 动作主键
     * @return ModuleCustomizedVO 动作详情响应对象
     */
    @GetMapping(value = "/v1/moduleCustomized/{user_id}")
    @ApiOperation(value = "详情", notes = "根据moduleCustomizedId获取动作详情", tags = {"/v1/moduleCustomized"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功", response = ModuleCustomizedVO.class),
            @ApiResponse(code = 500, message = "内部错误")})
    ModuleCustomizedVO findByPrimaryKey(@PathVariable("user_id") String moduleCustomizedId);

    /**
     * 根据主键查询动作集合信息
     *
     * @param moduleCustomizedIds 动作主键
     * @return ModuleCustomizedVO 动作详情响应对象
     */
    @GetMapping(value = "/v1/{moduleCustomized/list/{user_ids}")
    @ApiOperation(value = "查询", notes = "查询", tags = {"/v1/moduleCustomized"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功"),
            @ApiResponse(code = 500, message = "内部错误")})
    List<ModuleCustomizedVO> listByPrimaryKeyArrays(@PathVariable("user_ids") String moduleCustomizedIds);
    

    /**
     * 根据主键查询动作集合信息
     *
     * @param moduleCustomized 动作主键
     * @return ModuleCustomizedVO 动作详情响应对象
     */
    @PostMapping(value = "/v1/moduleCustomized/select")
    @ApiOperation(value = "查询", notes = "查询", tags = {"/v1/moduleCustomized"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功"),
            @ApiResponse(code = 500, message = "内部错误")})
    List<ModuleCustomizedDTO> select(@RequestBody ModuleCustomizedCreateRequest moduleCustomized);


    /**
    * 根据page对象查询监控项
    *
    * @param request 分页查询监控对象
    * @return PageResult<ModuleCustomizedResponse> page返回对象
    */
    @PostMapping(value = "/v1/configManagement/pageModuleCustomizedList")
    @ApiOperation(value = "分页查询", notes = "分页查询", tags = {"/v1/moduleCustomized"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功", response = PageResponse.class),
    @ApiResponse(code = 500, message = "内部错误")})
    PageResult<ModuleCustomizedDTO> pageList(@RequestBody ModuleCustomizedPageRequest request);
}
