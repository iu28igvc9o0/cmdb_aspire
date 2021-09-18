package com.aspire.mirror.composite.service.configManagement;

import com.aspire.mirror.composite.service.configManagement.payload.CompModuleInfoCreateRequest;
import com.aspire.mirror.composite.service.configManagement.payload.ModuleInfoPayload;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * TODO
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.composite.service.configManagement
 * 类名称:    IModuleInfoService.java
 * 类描述:    TODO
 * 创建人:    JinSu
 * 创建时间:  2019/10/17 15:27
 * 版本:      v1.0
 */
public interface IModuleInfoService {
    /**
     * 创建信息
     *
     * @param moduleInfo 动作创建请求对象
     * @return ModuleCustomizedCreateResponse 动作创建响应对象
     */
    @PostMapping(value = "/v1/moduleInfo/insert", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "创建", notes = "创建", response = ModuleInfoPayload.class, tags = {"moduleCustomized"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回", response = ModuleInfoPayload.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = ModuleInfoPayload.class)})
    ModuleInfoPayload saveModuleInfo(@RequestBody CompModuleInfoCreateRequest moduleInfo);


    @GetMapping(value = "/v1/moduleInfo/getModuleInfoByCode")
    @ApiOperation(value = "详情", notes = "根据模块编码获取模块配置数据详情", tags = {"moduleCustomized"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功", response = ModuleInfoPayload.class),
            @ApiResponse(code = 500, message = "内部错误")})
    ModuleInfoPayload findByCode(@RequestParam("moduleCode") String moduleCode);
}
