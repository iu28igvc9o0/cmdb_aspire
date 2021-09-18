package com.aspire.ums.cmdb.schema;

import com.aspire.ums.cmdb.schema.payload.ColumnInfo;
import com.aspire.ums.cmdb.schema.payload.TableInfo;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;
import java.util.Map;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: ISchemaAPI
 * Author:   zhu.juwang
 * Date:     2019/5/29 8:34
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@RequestMapping("/cmdb/schema")
public interface ISchemaAPI {
    /**
     * 获取表名列表
     */
    @RequestMapping(value = "/getTables", method = RequestMethod.GET)
    @ApiOperation(value = "获取所有的表名", notes = "获取所有的表名", tags = {"CMDB Schema API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    List<TableInfo> getTableList();

    /**
     * 获取表名列表
     */
    @RequestMapping(value = "/getColumns/{tableName}", method = RequestMethod.GET)
    @ApiOperation(value = "根据表名查询所有列名", notes = "根据表名查询所有列名", tags = {"CMDB Schema API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    List<ColumnInfo> getColumnsByTableName(@PathVariable("tableName") String tableName);

    /**
     * 检测SQL是否错误
     */
    @RequestMapping(value = "/checkSql", method = RequestMethod.POST)
    @ApiOperation(value = "检测SQL是否错误", notes = "检测SQL是否错误", tags = {"CMDB Schema API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    Map<String, String> checkSql(@RequestBody Map<String, String> params);
}
