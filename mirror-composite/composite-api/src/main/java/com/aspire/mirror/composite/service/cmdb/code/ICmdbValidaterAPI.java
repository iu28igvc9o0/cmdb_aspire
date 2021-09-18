package com.aspire.mirror.composite.service.cmdb.code;

import com.aspire.ums.cmdb.code.payload.CmdbCodeValidate;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: ICmdbCodeAPI
 * Author:   zhu.juwang
 * Date:     2019/5/13 18:43
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@RequestMapping("${version}/cmdb/code/validate")
public interface ICmdbValidaterAPI {

    /**
     * 控件验证方法列表
     *
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ApiOperation(value = "控件验证方法列表", notes = "控件验证方法列表", tags = {"CMDB Code Validate API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = void.class),
            @ApiResponse(code = 500, message = "内部错误")})
    List<CmdbCodeValidate> list();

//    /**
//     * 验证控件编码或名称是否已经存在
//     * return {"flag": true} / {"flag": false}
//     */
//    @RequestMapping(value = "/valid", method = RequestMethod.GET)
//    @ApiOperation(value = "验证控件编码或名称是否已经存在", notes = "验证控件编码或名称是否已经存在", tags = {"CMDB Code Control API"})
//    @ApiResponses(value = {@ApiResponse(code = 200, message = "验证成功", response = void.class),
//            @ApiResponse(code = 500, message = "内部错误")})
//    Map<String, String> valid(@RequestParam("filed") String filed, @RequestParam("value") String value);

//    /**
//     * 新增控件信息
//     * @param controlType 控件信息
//     */
//    @RequestMapping(value = "/saveControl", method = RequestMethod.POST)
//    @ApiOperation(value = "新增控件信息", notes = "新增控件信息", tags = {"CMDB Code Control API"})
//    @ApiResponses(value = {@ApiResponse(code = 204, message = "新增成功", response = void.class),
//            @ApiResponse(code = 500, message = "内部错误")})
//    void saveControl(HttpServletResponse response, @RequestBody CmdbControlType controlType);

//    /**
//     * 修改码表信息
//     * @param cmdbCode 码表信息
//     */
//    @RequestMapping(value = "/updateCode", method = RequestMethod.POST)
//    @ApiOperation(value = "修改码表信息", notes = "修改码表信息", tags = {"CMDB Code Control API"})
//    @ApiResponses(value = {@ApiResponse(code = 204, message = "修改成功", response = void.class),
//            @ApiResponse(code = 500, message = "内部错误")})
//    void updateCode(HttpServletResponse response, @RequestBody CmdbCode cmdbCode);
}
