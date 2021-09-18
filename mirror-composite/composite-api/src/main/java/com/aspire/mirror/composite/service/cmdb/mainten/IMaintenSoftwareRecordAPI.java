package com.aspire.mirror.composite.service.cmdb.mainten;

import com.alibaba.fastjson.JSONObject;
import com.aspire.ums.cmdb.common.PageBean;
import com.aspire.ums.cmdb.maintenance.payload.CmdbMaintenanceSoftwareRecord;
import com.aspire.ums.cmdb.maintenance.payload.CmdbMaintenanceSoftwareRecordQuery;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: RepairEventImportFactory
 * Author:   hangfang
 * Date:     2019/8/4
 * Description: DESCRIPTION
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Api(value = "软件维保使用管理接口")
@RequestMapping("${version}/cmdb/maintenance/software/record")
public interface IMaintenSoftwareRecordAPI {

    /**
     *  获取使用列表
     * @return
     */
    @PostMapping(value = "/list" )
    @ApiOperation(value = "获取使用列表", notes = "获取使用列表", tags = {"Cmdb Maintenance Software Record API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = JSONObject.class),
            @ApiResponse(code = 500, message = "内部错误")})
    PageBean<CmdbMaintenanceSoftwareRecord> list(@RequestBody CmdbMaintenanceSoftwareRecordQuery query);


    /**
     *  新增或更新
     * @return
     */
    @PostMapping(value = "/saveAndUpdate" )
    @ApiOperation(value = "新增或更新", notes = "新增或更新", tags = {"Cmdb Maintenance Software Record API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "新增/更新成功", response = JSONObject.class),
            @ApiResponse(code = 500, message = "内部错误")})
    Map<String, Object> saveAndUpdate(@RequestBody List<CmdbMaintenanceSoftwareRecord> records);

    /**
     *  删除软件维保使用
     * @return
     */
    @DeleteMapping(value = "/delete" )
    @ApiOperation(value = "删除软件维保使用", notes = "删除软件维保使用", tags = {"Cmdb Maintenance Software Record API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "删除成功", response = JSONObject.class),
            @ApiResponse(code = 500, message = "内部错误")})
    Map<String, Object> delete(@RequestBody  JSONObject data);

    /**
     *  下载软件维保使用模板
     * @return
     */
    @GetMapping(value = "/downloadTemplate" )
    @ApiOperation(value = "下载软件维保使用模板", notes = "下载软件维保使用模板", tags = {"Cmdb Maintenance Software Record API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "下载成功", response = JSONObject.class),
            @ApiResponse(code = 500, message = "内部错误")})
    Map<String, Object> downloadTemplate(HttpServletResponse response);


    /**
     *  导出软件维保使用数据
     * @return
     */
    @PostMapping(value = "/exportData" )
    @ApiOperation(value = "导出软件维保使用数据", notes = "导出软件维保使用数据", tags = {"Cmdb Maintenance Software Record API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "导出成功", response = JSONObject.class),
            @ApiResponse(code = 500, message = "内部错误")})
    Map<String, Object> exportData(@RequestBody CmdbMaintenanceSoftwareRecordQuery query, HttpServletResponse response);


}
