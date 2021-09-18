package com.aspire.ums.cmdb.teamwork;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.aspire.mirror.common.entity.PageResponse;
import com.aspire.ums.cmdb.common.ResultVo;
import com.aspire.ums.cmdb.teamwork.payload.TeamworkLabelReq;
import com.aspire.ums.cmdb.teamwork.payload.TeamworkLabelVO;
import com.aspire.ums.cmdb.teamwork.payload.TeamworkMessageVO;
import com.aspire.ums.cmdb.teamwork.payload.TeamworkReq;
import com.aspire.ums.cmdb.teamwork.payload.TeamworkUserReq;
import com.aspire.ums.cmdb.teamwork.payload.TeamworkVO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(value = "协同作战")
@RequestMapping("/cmdb/teamword")
public interface ITeamworkAPI {
	 
	 	@PostMapping("/pageList")
	    @ApiOperation(value = "查询协同作战列表", notes = "查询协同作战列表", tags = {"CMDB teamword API"})
	    @ApiResponses(value = {@ApiResponse(code = 200, message = "新增成功", response = String.class),
	     @ApiResponse(code = 500, message = "内部错误")})
	 	 PageResponse<TeamworkVO> pageList(@RequestBody TeamworkReq pageRequset);
	 	
	 	

	 	@DeleteMapping("/deleteTeamwork")
	    @ApiOperation(value = "删除协同作战", notes = "删除协同作战", tags = {"CMDB teamword API"})
	    @ApiResponses(value = {@ApiResponse(code = 200, message = "新增成功", response = String.class),
	     @ApiResponse(code = 500, message = "内部错误")})
	 	ResultVo deleteTeamwork(@RequestParam(value="id") String id);


	 	@PostMapping("/createTeamwork")
	    @ApiOperation(value = "新增协同作战", notes = "新增协同作战", tags = {"CMDB teamword API"})
	    @ApiResponses(value = {@ApiResponse(code = 200, message = "新增成功", response = String.class),
	     @ApiResponse(code = 500, message = "内部错误")})
	 	ResultVo createTeamwork(@RequestBody TeamworkVO teamwork);


	 	@PutMapping("/updateTeamwork")
	    @ApiOperation(value = "更新协同作战", notes = "更新协同作战", tags = {"CMDB teamword API"})
	    @ApiResponses(value = {@ApiResponse(code = 200, message = "新增成功", response = String.class),
	     @ApiResponse(code = 500, message = "内部错误")})
		ResultVo updateTeamwork(@RequestBody TeamworkVO teamwork);


	 	@PostMapping("/getListByUser")
	    @ApiOperation(value = "根据用户账号查询协同作战列表", notes = "根据用户账号查询协同作战列表", tags = {"CMDB teamword API"})
	    @ApiResponses(value = {@ApiResponse(code = 200, message = "新增成功", response = String.class),
	     @ApiResponse(code = 500, message = "内部错误")})
		List<TeamworkVO> getListByUser(@RequestBody TeamworkUserReq req);



		@PutMapping("/updateTeamworkOnly")
	    @ApiOperation(value = "更新协同作战", notes = "更新协同作战", tags = {"CMDB teamword API"})
	    @ApiResponses(value = {@ApiResponse(code = 200, message = "新增成功", response = String.class),
	     @ApiResponse(code = 500, message = "内部错误")})
		ResultVo updateTeamworkOnly(@RequestBody TeamworkVO teamwork);


		@GetMapping("/detailTeamwor")
	    @ApiOperation(value = "查询协同作战详情", notes = "查询协同作战详情", tags = {"CMDB teamword API"})
	    @ApiResponses(value = {@ApiResponse(code = 200, message = "新增成功", response = String.class),
	     @ApiResponse(code = 500, message = "内部错误")})
		TeamworkVO detailTeamwor(@RequestParam(value="id", required = true) String id,@RequestParam(value="type")  int type);
		
		
		@PostMapping("/getMsg")
	    @ApiOperation(value = "查询聊天消息", notes = "查询聊天消息", tags = {"CMDB teamword API"})
	    @ApiResponses(value = {@ApiResponse(code = 200, message = "新增成功", response = String.class),
	     @ApiResponse(code = 500, message = "内部错误")})
		List<TeamworkMessageVO> getMsg(@RequestBody TeamworkLabelReq req);


		@PostMapping("/getMsgSummary")
	    @ApiOperation(value = "查询标签汇总内容", notes = "查询标签汇总内容", tags = {"CMDB teamword API"})
	    @ApiResponses(value = {@ApiResponse(code = 200, message = "新增成功", response = String.class),
	     @ApiResponse(code = 500, message = "内部错误")})
		List<TeamworkLabelVO> getMsgSummary(@RequestBody TeamworkLabelReq req);


		@PostMapping("/createMsg")
	    @ApiOperation(value = "保存消息", notes = "保存消息", tags = {"CMDB teamword API"})
	    @ApiResponses(value = {@ApiResponse(code = 200, message = "新增成功", response = String.class),
	     @ApiResponse(code = 500, message = "内部错误")})
		ResultVo createMsg(@RequestBody TeamworkMessageVO msgVO);
		
		@PostMapping("/labelSummary")
	    @ApiOperation(value = "标签汇总", notes = "标签汇总", tags = {"CMDB teamword API"})
	    @ApiResponses(value = {@ApiResponse(code = 200, message = "新增成功", response = String.class),
	     @ApiResponse(code = 500, message = "内部错误")})
		ResultVo labelSummary(@RequestBody TeamworkVO jacksonBaseParse);


		@PostMapping("/restartTeamwork")
	    @ApiOperation(value = "重启作战", notes = "重启作战", tags = {"CMDB teamword API"})
	    @ApiResponses(value = {@ApiResponse(code = 200, message = "新增成功", response = String.class),
	     @ApiResponse(code = 500, message = "内部错误")})
		ResultVo restartTeamwork(@RequestBody TeamworkVO teamwork);
		
}
