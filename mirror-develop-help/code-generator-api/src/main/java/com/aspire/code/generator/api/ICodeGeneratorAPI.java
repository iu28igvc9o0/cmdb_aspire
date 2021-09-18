package com.aspire.code.generator.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: ICodeGeneratorAPI
 * Author:   zhu.juwang
 * Date:     2019/4/28 14:40
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Api(value = "代码生成接口类")
@RequestMapping("/develophelp")
public interface ICodeGeneratorAPI {


    /**
     * 连接数据库
     * @param collectRequest databaseUrl 数据库连接
     * @param collectRequest databaseUser 数据库用户名
     * @param collectRequest databasePwd 数据库密码
     */
    @PostMapping(value = "/connection")
    @ApiOperation(value = "连接数据库", notes = "连接数据库,连接成功返回所有数据表", tags = {"Generator API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "生成成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    List<String> connection(@RequestBody Map<String, Object> collectRequest) throws Exception;

    /**
     * 代码生成器
     * @param collectRequest databaseUrl 数据库连接
     * @param collectRequest databaseUser 数据库用户名
     * @param collectRequest databasePwd 数据库密码
     * @param collectRequest tableNames 需要生成的表名 多个以逗号分隔
     * @param collectRequest packagePath 生成的包路径
     * @return 生成的文件列表
     */
    @PostMapping(value = "/downloadBeans")
    @ApiOperation(value = "生成代码类", notes = "生成代码类", tags = {"Generator API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "生成成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    void generatorCode(@RequestBody Map<String, Object> collectRequest,
                               HttpServletResponse response) throws Exception ;

    /**
     * 生成 CURD
     * @param connectionRequest databaseUrl 数据库连接
     * @param connectionRequest databaseUser 数据库用户名
     * @param connectionRequest databasePwd 数据库密码
     * @param connectionRequest tableName 需要生成的表名
     * @param connectionRequest operator 操作方法
     * @return 生成的CURD
     */
    @PostMapping(value = "/generator/curd")
    @ApiOperation(value = "生成SQL语句", notes = "生成SQL语句, 支持select/insert/update/delete/all", tags = {"Generator API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "生成成功", response = String.class),
            @ApiResponse(code = 500, message = "内部错误")})
    String generatorCURD(@RequestBody Map<String, Object> connectionRequest);

}
