package com.aspire.mirror.composite.service.common;

import com.aspire.mirror.composite.service.common.payload.CompCodeDictResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 字典暴露服务
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.composite.service.common
 * 类名称:    ICompDictService.java
 * 类描述:    /**
 * 创建人:    JinSu
 * 创建时间:  2018/11/9 16:40
 * 版本:      v1.0
 */
@Api("字典管理")
@RequestMapping("${version}/dict")
public interface ICompDictService {

    @GetMapping(value = "/listAll")
    @ApiOperation(value = "字典数据列表", notes = "字典数据列表", tags = {"Dict API"})
    CompCodeDictResponse listAll();

}
