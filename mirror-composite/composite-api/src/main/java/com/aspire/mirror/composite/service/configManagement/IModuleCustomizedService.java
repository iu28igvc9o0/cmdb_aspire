package com.aspire.mirror.composite.service.configManagement;

import com.aspire.mirror.composite.service.configManagement.payload.ModuleCustomizedPayload;
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
public interface IModuleCustomizedService {
	/**
     * 创建信息
     *
     * @param ModuleCustomizedPayload 动作创建请求对象
     * @return ModuleCustomizedCreateResponse 动作创建响应对象
     */
    @PostMapping(value = "/v1/moduleCustomized/insert", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "创建", notes = "创建", response = ModuleCustomizedPayload.class, tags = {"moduleCustomized"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回", response = ModuleCustomizedPayload.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = ModuleCustomizedPayload.class)})
    ModuleCustomizedPayload saveModuleCustomized(@RequestBody ModuleCustomizedPayload moduleCustomizedCreateRequest);
    
   /**
     * 根据主键删除单作信息
     *
     * @param actionId 主键
     * @@return Result 返回结果
     */
    @DeleteMapping(value = "/v1/moduleCustomized/{user_id}")
    @ApiOperation(value = "删除单条信息", notes = "删除单条信息",
            tags = {"moduleCustomized"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功")})
    ResponseEntity<String> deleteByPrimaryKey(@PathVariable("user_id") String moduleCustomizedId);
    
            
    /**
     * 根据主键查作详情信息
     *
     * @param moduleCustomizedId 动作主键
     * @return ModuleCustomizedVO 动作详情响应对象
     */
    @GetMapping(value = "/v1/moduleCustomized/{user_id}")
    @ApiOperation(value = "详情", notes = "根据moduleCustomizedId获取动作详情", tags = {"moduleCustomized"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功", response = ModuleCustomizedPayload.class),
            @ApiResponse(code = 500, message = "内部错误")})
    ModuleCustomizedPayload findByPrimaryKey(@PathVariable("user_id") String moduleCustomizedId);


    /**
     * 根据主键查询动作集合信息
     *
     * @param moduleCustomizedId 动作主键
     * @return ModuleCustomizedVO 动作详情响应对象
     */
    @PostMapping(value = "/v1/moduleCustomized/select")
    @ApiOperation(value = "查询", notes = "查询", tags = {"moduleCustomized"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功"),
            @ApiResponse(code = 500, message = "内部错误")})
    List<ModuleCustomizedPayload> select(@RequestBody ModuleCustomizedPayload moduleCustomized);

}
