/**
 *
 * 项目名： ops-api 
 * <p/> 
 *
 * 文件名:  IYumManageService.java 
 * <p/>
 *
 * 功能描述: TODO 
 * <p/>
 *
 * @author	pengguihua
 *
 * @date	2019年11月21日 
 *
 * @version	V1.0
 * <p/>
 *
 *<b>Copyright(c)</b> 2019 卓望公司-版权所有<br/>
 *   
 */
package com.aspire.mirror.ops.api.service;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.aspire.mirror.ops.api.domain.GeneralResponse;
import com.aspire.mirror.ops.api.domain.OpsYumFileGroup;
import com.aspire.mirror.ops.api.domain.OpsYumFileGroupQueryModel;
import com.aspire.mirror.ops.api.domain.OpsYumFileModel;
import com.aspire.mirror.ops.api.domain.OpsYumFileQueryModel;
import com.aspire.mirror.ops.api.domain.OsDistributionModel;
import com.aspire.mirror.ops.api.domain.PageListQueryResult;
import com.aspire.mirror.ops.api.domain.YumFileServerSource;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/** 
 *
 * 项目名称: ops-api 
 * <p/>
 * 
 * 类名: IYumManageService
 * <p/>
 *
 * 类功能描述: TODO
 * <p/>
 *
 * @author	pengguihua
 *
 * @date	2019年11月21日  
 *
 * @version	V1.0 
 * <br/>
 *
 * <b>Copyright(c)</b> 2019 卓望公司-版权所有 
 *
 */
@Api(value = "yum服务管理")
@RequestMapping(value = "/v1/ops-service/yumManage/")
public interface IYumManageService {
	
	@GetMapping(value = "/fetchOsDistributionList", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "获取操作系统发行版本列表", notes = "获取操作系统发行版本列表", response = GeneralResponse.class, tags = {"Yum Manage service API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "返回", response = GeneralResponse.class),
            			   @ApiResponse(code = 500, message = "Unexpected error")})
	List<OsDistributionModel> fetchOsDistributionList();
	
	@PostMapping(value = "/queryYumFileGroupTreeList", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "查询yum文件分组列表", notes = "查询yum文件分组列表", response = GeneralResponse.class, tags = {"Yum Manage service API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "返回", response = GeneralResponse.class),
            			   @ApiResponse(code = 500, message = "Unexpected error")})
	public List<OpsYumFileGroup> queryYumFileGroupTreeList(@RequestBody OpsYumFileGroupQueryModel param);
	
	@PostMapping(value = "/createYumFileGroup", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "创建yum文件分组", notes = "创建yum文件分组", response = GeneralResponse.class, tags = {"Yum Manage service API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "返回", response = GeneralResponse.class),
            			   @ApiResponse(code = 500, message = "Unexpected error")})
	public GeneralResponse createYumFileGroup(@RequestBody OpsYumFileGroup group);
	
	@PutMapping(value = "/updateYumFileGroup", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "修改yum文件分组", notes = "修改yum文件分组", response = GeneralResponse.class, tags = {"Yum Manage service API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "返回", response = GeneralResponse.class),
            			   @ApiResponse(code = 500, message = "Unexpected error")})
	public GeneralResponse updateYumFileGroup(@RequestBody OpsYumFileGroup group);
	
	@DeleteMapping(value = "/removeYumFileGroup/{yumFileGroupId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "删除yum文件分组", notes = "删除yum文件分组", response = GeneralResponse.class, tags = {"Yum Manage service API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "返回", response = GeneralResponse.class),
            			   @ApiResponse(code = 500, message = "Unexpected error")})
	public GeneralResponse removeYumFileGroup(@PathVariable("yumFileGroupId") Long yumFileGroupId);
	
	@PostMapping(value = "/uploadYumLocalFile", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "上传yum本地文件", notes = "上传yum本地文件", response = GeneralResponse.class, tags = {"Yum Manage service API"})
	@ApiResponses(value = {@ApiResponse(code = 200, message = "返回", response = GeneralResponse.class),
			@ApiResponse(code = 500, message = "Unexpected error")})
	GeneralResponse uploadYumLocalFile(@RequestParam("file") MultipartFile file);
	
	@PostMapping(value = "/transferYumFile", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "远程机器传输yum文件", notes = "远程机器传输yum文件", response = GeneralResponse.class, tags = {"Yum Manage service API"})
	@ApiResponses(value = {@ApiResponse(code = 200, message = "返回", response = GeneralResponse.class),
			@ApiResponse(code = 500, message = "Unexpected error")})
	GeneralResponse transferYumFile(@RequestBody YumFileServerSource serverSource);
	
	@PostMapping(value = "/queryYumSourceList", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "查询yum源列表", notes = "查询yum源列表", response = GeneralResponse.class, tags = {"Yum Manage service API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "返回", response = GeneralResponse.class),
            			   @ApiResponse(code = 500, message = "Unexpected error")})
	public PageListQueryResult<OpsYumFileModel> queryYumSourceList(@RequestBody OpsYumFileQueryModel param);
	
	@PostMapping(value = "/createYumSourceModel", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "创建yum源记录", notes = "创建yum源记录", response = GeneralResponse.class, tags = {"Yum Manage service API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "返回", response = GeneralResponse.class),
            			   @ApiResponse(code = 500, message = "Unexpected error")})
	public GeneralResponse createYumSourceModel(@RequestBody OpsYumFileModel yumSource);
	
	@PutMapping(value = "/updateYumSourceModel", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "修改yum源记录", notes = "修改yum源记录", response = GeneralResponse.class, tags = {"Yum Manage service API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "返回", response = GeneralResponse.class),
            			   @ApiResponse(code = 500, message = "Unexpected error")})
	public GeneralResponse updateYumSourceModel(@RequestBody OpsYumFileModel yumSource);
	
	@DeleteMapping(value = "/removeYumSource/{yumSourceId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "移除yum源文件记录", notes = "移除yum源文件记录", response = GeneralResponse.class, tags = {"Yum Manage service API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "返回", response = GeneralResponse.class),
            			   @ApiResponse(code = 500, message = "Unexpected error")})
	public GeneralResponse removeYumSourceModel(@PathVariable("yumSourceId") Long yumSourceId);
	
	@PostMapping(value = "/queryYumConfigList", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "查询yum配置列表", notes = "查询yum配置列表", response = GeneralResponse.class, tags = {"Yum Manage service API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "返回", response = GeneralResponse.class),
            			   @ApiResponse(code = 500, message = "Unexpected error")})
	public PageListQueryResult<OpsYumFileModel> queryYumConfigList(@RequestBody OpsYumFileQueryModel param);
	
	@PostMapping(value = "/createYumConfigModel", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "创建yum配置记录", notes = "创建yum配置记录", response = GeneralResponse.class, tags = {"Yum Manage service API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "返回", response = GeneralResponse.class),
            			   @ApiResponse(code = 500, message = "Unexpected error")})
	public GeneralResponse createYumConfigModel(@RequestBody OpsYumFileModel yumConfig);
	
	@PutMapping(value = "/updateYumConfigModel", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "修改yum配置记录", notes = "修改yum配置记录", response = GeneralResponse.class, tags = {"Yum Manage service API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "返回", response = GeneralResponse.class),
            			   @ApiResponse(code = 500, message = "Unexpected error")})
	public GeneralResponse updateYumConfigModel(@RequestBody OpsYumFileModel yumConfig);
	
	@DeleteMapping(value = "/removeYumConfig/{yumConfigId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "移除yum配置记录", notes = "移除yum配置记录", response = GeneralResponse.class, tags = {"Yum Manage service API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "返回", response = GeneralResponse.class),
            			   @ApiResponse(code = 500, message = "Unexpected error")})
	public GeneralResponse removeYumConfigModel(@PathVariable("yumConfigId") Long yumConfigId);
}
