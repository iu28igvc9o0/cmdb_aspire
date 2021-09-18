package com.aspire.mirror.composite.service.configManagement;

import com.aspire.mirror.composite.service.configManagement.payload.ModuleCustomizedPayload;
import com.aspire.mirror.composite.service.configManagement.payload.ModuleCustomizedViewPayload;

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
 
 @Api(value = "IModuleCustomizedViewService")
public interface IModuleCustomizedViewService {
	/**
     * 创建信息
     *
     * @param ModuleCustomizedPayload 动作创建请求对象
     * @return ModuleCustomizedCreateResponse 动作创建响应对象
     */
    @PostMapping(value = "/v1/moduleCustomizedView/insert")
    @ApiOperation(value = "创建视图", notes = "创建", tags = {"moduleCustomizedView"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功", response = ResponseEntity.class),
            @ApiResponse(code = 500, message = "内部错误")})
    ResponseEntity<String> saveModuleCustomizedView(@RequestBody ModuleCustomizedViewPayload moduleCustomizedCreateRequest);
    
    
    /**
     * 更新信息
     *
     * @param ModuleCustomizedPayload 动作创建请求对象
     * @return ModuleCustomizedCreateResponse 动作创建响应对象
     */
    @PostMapping(value = "/v1/moduleCustomizedView/update")
    @ApiOperation(value = "更新视图", notes = "更新", tags = {"moduleCustomizedView"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功", response = ResponseEntity.class),
            @ApiResponse(code = 500, message = "内部错误")})
    ResponseEntity<String> updateModuleCustomizedView(@RequestBody ModuleCustomizedViewPayload moduleCustomizedupdateRequest);
    
    
   /**
     * 根据主键删除单作信息
     *
     * @param actionId 主键
     * @@return Result 返回结果
     */
    @DeleteMapping(value = "/v1/moduleCustomizedView/{id}")
    @ApiOperation(value = "删除单条信息", notes = "删除单条信息",
            tags = {"moduleCustomizedView"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功")})
    ResponseEntity<String> deleteByPrimaryKey(@PathVariable("id") String id);
    

    /**
     * 根据主键查询动作集合信息
     *
     * @param moduleCustomizedId 动作主键
     * @return ModuleCustomizedVO 动作详情响应对象
     */
    @PostMapping(value = "/v1/moduleCustomizedView/select")
    @ApiOperation(value = "查询", notes = "查询", tags = {"moduleCustomizedView"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功"),
            @ApiResponse(code = 500, message = "内部错误")})
    List<ModuleCustomizedViewPayload> select(@RequestBody ModuleCustomizedViewPayload  m);

    @PostMapping(value = "/v1/moduleCustomizedView/design")
    @ApiOperation(value = "设计视图", notes = "设计视图", tags = {"moduleCustomizedView"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功")})
	ResponseEntity<String> designView(@RequestBody ModuleCustomizedViewPayload m);

}
