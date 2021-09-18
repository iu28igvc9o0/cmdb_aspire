package com.aspire.ums.cmdb.filemanage;

import com.alibaba.fastjson.JSONObject;
import com.aspire.ums.cmdb.common.Result;
import com.aspire.ums.cmdb.filemanage.payload.CmdbFileManage;
import com.aspire.ums.cmdb.filemanage.payload.CmdbFileManageQueryRequest;
import com.aspire.ums.cmdb.filemanage.payload.CmdbFileManageRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Api(value = "文件管理功能接口")
@RequestMapping("/cmdb/file/manage")
public interface ICmdbFileManageAPI {

    /**
     *  保存文件管理对象
     * @return
     */
    @PostMapping(value = "/save" )
    @ApiOperation(value = "保存文件管理对象", notes = "保存文件管理对象", tags = {"Cmdb ICmdbFileManage API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "保存成功", response = void.class),
            @ApiResponse(code = 500, message = "内部错误")})
    JSONObject insert(@RequestBody CmdbFileManage fileManage);

    /**
     *  修改文件管理对象
     * @return
     */
    @PostMapping(value = "/update" )
    @ApiOperation(value = "修改文件管理对象", notes = "修改文件管理对象", tags = {"Cmdb ICmdbFileManage API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "保存成功", response = void.class),
            @ApiResponse(code = 500, message = "内部错误")})
    JSONObject update(@RequestBody CmdbFileManageRequest fileManage);

    /**
     *  删除文件管理对象
     * @return
     */
    @PostMapping(value = "/delete" )
    @ApiOperation(value = "删除文件管理对象", notes = "删除文件管理对象", tags = {"Cmdb ICmdbFileManage API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "保存成功", response = void.class),
            @ApiResponse(code = 500, message = "内部错误")})
    JSONObject delete(@RequestBody CmdbFileManageRequest fileManage);

    /**
     *  获取文件管理对象列表
     * @return
     */
    @PostMapping(value = "/list" )
    @ApiOperation(value = "获取文件管理对象列表", notes = "获取文件管理对象列表", tags = {"Cmdb ICmdbFileManage API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "保存成功", response = CmdbFileManage.class),
            @ApiResponse(code = 500, message = "内部错误")})
    Result<CmdbFileManage> getFileManageList(@RequestBody CmdbFileManageQueryRequest request);

    /**
     *  下载文件,依据id
     * @return
     */
    @GetMapping(value = "/download/{id}" )
    @ApiOperation(value = "下载文件,依据id", notes = "下载文件,依据id", tags = {"Cmdb ICmdbFileManage API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "保存成功", response = JSONObject.class),
            @ApiResponse(code = 500, message = "内部错误")})
    CmdbFileManage getOneFile(@PathVariable("id") String id);

    /**
     *  根据文件类型，列出文件对象列表
     * @return
     */
    @PostMapping(value = "/listFileObj" )
    @ApiOperation(value = "根据文件类型，列出文件对象列表", notes = "根据文件类型，列出文件对象列表", tags = {"Cmdb ICmdbFileManage API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "保存成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    List<Map<String,Object>> getFileObjectList(@RequestBody String fileType);
}
