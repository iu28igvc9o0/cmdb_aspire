package com.aspire.mirror.composite.service.opsmanage;

import java.util.Map;

import com.aspire.mirror.ops.api.domain.OpsFile;
import com.aspire.mirror.ops.api.domain.OpsFileQueryModel;
import com.aspire.mirror.ops.api.domain.PageListQueryResult;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.aspire.mirror.ops.api.domain.GeneralResponse;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

/** 
 *
 * 项目名称: composite-api 
 * <p/>
 * 
 * 类名: ICompOpsFileManageService
 * <p/>
 *
 * 类功能描述: TODO
 * <p/>
 *
 * @author	pengguihua
 *
 * @date	2020年4月29日  
 *
 * @version	V1.0 
 * <br/>
 *
 * <b>Copyright(c)</b> 2020 卓望公司-版权所有 
 *
 */
@Api(value = "ops文件管理")
@RequestMapping(value = "/v1/ops-service/opsFileManage/")
public interface ICompOpsFileManageService {
	
	@PostMapping(value = "/downloadFile", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "下载文件", notes = "下载文件", response = GeneralResponse.class, tags = {"Ops File Manage service API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "返回"),
            			   @ApiResponse(code = 500, message = "Unexpected error")})
	void downloadFile(@RequestBody Map<String, String> downParam);

	@PostMapping(value = "/convergeDownloadFile", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "汇聚下载文件", notes = "汇聚下载文件", response = GeneralResponse.class, tags = {"Ops File Manage service API"})
	@ApiResponses(value = {@ApiResponse(code = 200, message = "返回"),
			@ApiResponse(code = 500, message = "Unexpected error")})
	void convergeDownloadFile(@RequestBody Map<String, String> downParam);

	@PostMapping(value = "/saveFile", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "保存文件信息", notes = "保存文件信息", response = GeneralResponse.class, tags = {"Ops File Manage service API"})
	@ApiResponses(value = {@ApiResponse(code = 200, message = "返回", response = GeneralResponse.class),
			@ApiResponse(code = 500, message = "Unexpected error")})
	GeneralResponse saveFile(@RequestBody OpsFile opsFile);

	@DeleteMapping(value = "/deleteFile", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "删除文件信息", notes = "删除文件信息", response = GeneralResponse.class, tags = {"Ops File Manage service API"})
	@ApiResponses(value = {@ApiResponse(code = 200, message = "返回", response = GeneralResponse.class),
			@ApiResponse(code = 500, message = "Unexpected error")})
	GeneralResponse deleteFile(@RequestParam("file_id") Long fileId);

//    @PutMapping(value = "/updateFile", produces = MediaType.APPLICATION_JSON_VALUE)
//    @ApiOperation(value = "修改文件信息", notes = "修改文件信息", response = GeneralResponse.class, tags = {"Group file API"})
//    @ApiResponses(value =  {@ApiResponse(code=200, message = "返回", response = GeneralResponse.class), @ApiResponse(code = 500, message = "Unexpected error")})
//    GeneralResponse updateFile(@RequestBody OpsFile opsFile);

	@GetMapping(value = "/getFileDetail", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "获取文件详情", notes = "获取文件详情", response = OpsFile.class, tags = {"Ops File Manage service API"})
	@ApiResponses(value = {@ApiResponse(code = 200, message = "返回", response = OpsFile.class),
			@ApiResponse(code = 500, message = "Unexpected error")})
	OpsFile getFileDetail(@RequestParam("file_id") Long fileId);

	@PostMapping(value = "/pageList", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "获取文件列表", notes = "获取文件列表", response = GeneralResponse.class, tags = {"Ops File Manage service API"})
	@ApiResponses(value = {@ApiResponse(code = 200, message = "返回", response = GeneralResponse.class),
			@ApiResponse(code = 500, message = "Unexpected error")})
	PageListQueryResult<OpsFile> pageList(@RequestBody OpsFileQueryModel opsFileQueryModel);

	@PostMapping(value = "/uploadFile", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "上传yum本地文件", notes = "上传yum本地文件", response = GeneralResponse.class, tags = {"Ops File Manage service API"})
	@ApiResponses(value = {@ApiResponse(code = 200, message = "返回", response = GeneralResponse.class),
			@ApiResponse(code = 500, message = "Unexpected error")})
	GeneralResponse uploadFile(@RequestParam("file") MultipartFile file);
}
