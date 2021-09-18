package com.aspire.mirror.ops.api.service;

import com.aspire.mirror.ops.api.domain.GeneralResponse;
import com.aspire.mirror.ops.api.domain.OpsFile;
import com.aspire.mirror.ops.api.domain.OpsFileQueryModel;
import com.aspire.mirror.ops.api.domain.PageListQueryResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * TODO
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.ops.api.service
 * 类名称:    IOpsFileService.java
 * 类描述:    TODO
 * 创建人:    JinSu
 * 创建时间:  2020/6/9 20:36
 * 版本:      v1.0
 */
@Api(value = "ops文件管理")
@RequestMapping(value = "/v1/ops-service/opsFileManage/")
public interface IOpsFileService {
    @PostMapping(value = "/saveFile", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "保存文件信息", notes = "保存文件信息", response = GeneralResponse.class, tags = {"Group file API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "返回", response = GeneralResponse.class),
            @ApiResponse(code = 500, message = "Unexpected error")})
    GeneralResponse saveFile(@RequestBody OpsFile opsFile);

    @DeleteMapping(value = "/deleteFile", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "删除文件信息", notes = "删除文件信息", response = GeneralResponse.class, tags = {"Group file API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "返回", response = GeneralResponse.class),
            @ApiResponse(code = 500, message = "Unexpected error")})
    GeneralResponse deleteFile(@RequestParam("file_id") Long fileId);

//    @PutMapping(value = "/updateFile", produces = MediaType.APPLICATION_JSON_VALUE)
//    @ApiOperation(value = "修改文件信息", notes = "修改文件信息", response = GeneralResponse.class, tags = {"Group file API"})
//    @ApiResponses(value =  {@ApiResponse(code=200, message = "返回", response = GeneralResponse.class), @ApiResponse(code = 500, message = "Unexpected error")})
//    GeneralResponse updateFile(@RequestBody OpsFile opsFile);

    @GetMapping(value = "/getFileDetail", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "获取文件详情", notes = "获取文件详情", response = OpsFile.class, tags = {"Group file API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "返回", response = OpsFile.class),
            @ApiResponse(code = 500, message = "Unexpected error")})
    OpsFile getFileDetail(@RequestParam("file_id") Long fileId);

    @PostMapping(value = "/pageList", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "获取文件列表", notes = "获取文件列表", response = GeneralResponse.class, tags = {"Group file API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "返回", response = GeneralResponse.class),
            @ApiResponse(code = 500, message = "Unexpected error")})
    PageListQueryResult<OpsFile> pageList(@RequestBody OpsFileQueryModel opsFileQueryModel);
}
