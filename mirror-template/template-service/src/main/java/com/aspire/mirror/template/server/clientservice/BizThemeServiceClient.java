package com.aspire.mirror.template.server.clientservice;

import com.aspire.mirror.template.server.entity.BizThemeDetail;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 业务主题客户端
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.migu.tsg.microservice.atomicservice.composite.clientservice.theme
 * 类名称:    BizThemeServiceClient.java
 * 类描述:    业务主题客户端
 * 创建人:    JinSu
 * 创建时间:  2018/10/24 19:44
 * 版本:      v1.0
 */
@FeignClient(value = "THEME-SERVICE")
public interface BizThemeServiceClient {
    /**
     * 查询详情
     * @param themeId 主题ID
     * @return BizThemeDetail 主题详情返回
     */
    @GetMapping(value = "/v1/theme/{theme_id}")
    BizThemeDetail findByPrimaryKey(@PathVariable("theme_id") String themeId);
}
