package com.aspire.mirror.composite.service.template;

import com.aspire.mirror.composite.service.template.payload.CompTriggersDetailResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * 触发器暴露服务
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.composite.service.template
 * 类名称:    ICompTriggersService.java
 * 类描述:    触发器暴露服务
 * 创建人:    JinSu
 * 创建时间:  2018/11/14 13:37
 * 版本:      v1.0
 */
@Api(value = "触发器信息管理")
@RequestMapping("/${version}/triggers")
public interface ICompTriggersService {
    /**
     * 根据模板查询触发器列表
     * @param templateId 模板ID
     * @return List<TriggersVO> 触发器查询响应对象
     */
    @GetMapping(value = "/listByTemplateId/{template_id}")
    @ApiOperation(value = "查询", notes = "查询", tags = {"/v1/triggers"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功"),
            @ApiResponse(code = 500, message = "内部错误")})
    List<CompTriggersDetailResponse> listByTemplateId(@PathVariable("template_id") String templateId);
}
