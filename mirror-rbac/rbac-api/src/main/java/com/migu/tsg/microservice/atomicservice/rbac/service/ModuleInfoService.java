package com.migu.tsg.microservice.atomicservice.rbac.service;

import com.migu.tsg.microservice.atomicservice.rbac.dto.ModuleCustomizedCreateRequest;
import com.migu.tsg.microservice.atomicservice.rbac.dto.ModuleCustomizedCreateResponse;
import com.migu.tsg.microservice.atomicservice.rbac.dto.ModuleInfoCreateRequest;
import com.migu.tsg.microservice.atomicservice.rbac.dto.vo.ModuleInfoVO;
import io.swagger.annotations.Api;
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
 * 包:        com.migu.tsg.microservice.atomicservice.rbac.service
 * 类名称:    ModuleInfoService.java
 * 类描述:    TODO
 * 创建人:    JinSu
 * 创建时间:  2019/10/17 16:01
 * 版本:      v1.0
 */
@Api(value = "")
public interface ModuleInfoService {
    /**
     * 创建信息
     *
     * @param moduleInfo 动作创建请求对象
     * @return ModuleCustomizedCreateResponse 动作创建响应对象
     */
    @PostMapping(value = "/v1/moduleInfo/insert", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "创建", notes = "创建", response = ModuleInfoVO.class, tags = {"moduleCustomized"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回", response = ModuleInfoVO.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = ModuleInfoVO.class)})
    ModuleInfoVO saveModuleInfo(@RequestBody ModuleInfoCreateRequest moduleInfo);


    @GetMapping(value = "/v1/moduleInfo/getModuleInfoByCode")
    @ApiOperation(value = "详情", notes = "根据模块编码获取模块配置数据详情", tags = {"moduleCustomized"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功", response = ModuleInfoVO.class),
            @ApiResponse(code = 500, message = "内部错误")})
    ModuleInfoVO findByCode(@RequestParam("moduleCode") String moduleCode);

}
