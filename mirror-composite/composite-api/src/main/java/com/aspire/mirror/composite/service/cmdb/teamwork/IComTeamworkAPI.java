package com.aspire.mirror.composite.service.cmdb.teamwork;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.aspire.mirror.common.entity.PageResponse;
import com.aspire.mirror.composite.service.cmdb.teamwork.payload.ComResultVo;
import com.aspire.mirror.composite.service.cmdb.teamwork.payload.ComTeamworkMessageVO;
import com.aspire.mirror.composite.service.cmdb.teamwork.payload.ComTeamworkReq;
import com.aspire.mirror.composite.service.cmdb.teamwork.payload.ComTeamworkVO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(value = "协同作战")
@RequestMapping("${version}/cmdb/teamword")
public interface IComTeamworkAPI {
	 
	 	@PostMapping("/pageList")
	    @ApiOperation(value = "查询协同作战列表", notes = "查询协同作战列表", tags = {"CMDB teamword API"})
	    @ApiResponses(value = {@ApiResponse(code = 200, message = "新增成功", response = String.class),
	     @ApiResponse(code = 500, message = "内部错误")})
	 	 PageResponse<ComTeamworkVO> pageList(@RequestBody ComTeamworkReq pageRequset);
	 	
	 	

	 	@DeleteMapping("/deleteTeamwork")
	    @ApiOperation(value = "删除协同作战", notes = "删除协同作战", tags = {"CMDB teamword API"})
	    @ApiResponses(value = {@ApiResponse(code = 200, message = "新增成功", response = String.class),
	     @ApiResponse(code = 500, message = "内部错误")})
	 	ComResultVo deleteTeamwork(@RequestParam(value="id") String id);


	 	@PostMapping("/createTeamwork")
	    @ApiOperation(value = "创建协同作战", notes = "创建协同作战", tags = {"CMDB teamword API"})
	    @ApiResponses(value = {@ApiResponse(code = 200, message = "新增成功", response = String.class),
	     @ApiResponse(code = 500, message = "内部错误")})
	 	ComResultVo createTeamwork(@RequestBody ComTeamworkVO teamwork);


	 	@PutMapping("/updateTeamwork")
	    @ApiOperation(value = "更新协同作战", notes = "更新协同作战", tags = {"CMDB teamword API"})
	    @ApiResponses(value = {@ApiResponse(code = 200, message = "新增成功", response = String.class),
	     @ApiResponse(code = 500, message = "内部错误")})
		ComResultVo updateTeamwork(@RequestBody ComTeamworkVO teamwork);


	 	@PutMapping("/closeTeamwork")
	    @ApiOperation(value = "关闭协同作战", notes = "关闭协同作战", tags = {"CMDB teamword API"})
	    @ApiResponses(value = {@ApiResponse(code = 200, message = "新增成功", response = String.class),
	     @ApiResponse(code = 500, message = "内部错误")})
		ComResultVo closeTeamwork(ComTeamworkVO teamwork);


	 	@PostMapping("/importTeamWorkMsg")
	    @ApiOperation(value = "上传消息图片", notes = "上传消息图片", tags = {"CMDB teamword API"})
	    @ApiResponses(value = {@ApiResponse(code = 200, message = "新增成功", response = String.class),
	     @ApiResponse(code = 500, message = "内部错误")})
		ComResultVo importTeamWorkMsg(ComTeamworkMessageVO msgVO, @PathVariable(value = "filename") MultipartFile file);


		@PutMapping("/labelSummary")
	    @ApiOperation(value = "标签汇总", notes = "标签汇总", tags = {"CMDB teamword API"})
	    @ApiResponses(value = {@ApiResponse(code = 200, message = "新增成功", response = String.class),
	     @ApiResponse(code = 500, message = "内部错误")})
		ComResultVo labelSummary(ComTeamworkVO teamwork);


		@PutMapping("/restartTeamwork")
	    @ApiOperation(value = "重启协同", notes = "重启协同", tags = {"CMDB teamword API"})
	    @ApiResponses(value = {@ApiResponse(code = 200, message = "新增成功", response = String.class),
	     @ApiResponse(code = 500, message = "内部错误")})
		ComResultVo restartTeamwork(ComTeamworkVO teamwork);
}
