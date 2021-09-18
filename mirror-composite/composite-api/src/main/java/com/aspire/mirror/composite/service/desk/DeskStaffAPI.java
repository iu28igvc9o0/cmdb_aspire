package com.aspire.mirror.composite.service.desk;

import com.aspire.mirror.composite.service.desk.bpm.payload.CheckInReq;
import com.aspire.mirror.composite.service.desk.bpm.payload.DeskLogs;
import com.aspire.mirror.composite.service.desk.bpm.payload.PublicNoticeVo;
import com.aspire.mirror.composite.service.desk.bpm.payload.ServiceDeskWithZxgllcVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @projectName: DeskStaffAPI
 * @description: 接口
 * @author: menglinjie
 * @create: 2020-10-28 10:37
 **/
@Api("desk_staff")
@RequestMapping("${version}/desk")
public interface DeskStaffAPI {

	/**
	 *  依据流程Key获取最新的流程Id
	 */
	@RequestMapping(value="/getBpmDefId",method=RequestMethod.GET, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "根据流程key获取最新的流程定义id", httpMethod = "GET", notes = "根据流程key获取最新的流程定义id" ,tags = {"desk_staff API"})
	public Object getBpmDefId(@RequestParam String defKey) throws Exception;

	/**
	 *查询公告列表
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	@PostMapping(value = "/publicNotice/findList", produces = {"application/json; charset=utf-8"})
	@ApiOperation(value = "查询公告列表", httpMethod = "POST", notes = "查询公告列表",tags = {"desk_staff API"})
	public Object findList(@RequestBody PublicNoticeVo vo) throws Exception;

	/**
	 * 公告通知操作
	 * @return
	 * @throws Exception
	 */
	@GetMapping(value = "/publicNotice/sendNotice/{id}", produces = {"application/json; charset=utf-8"})
	@ApiOperation(value = "公告通知操作", httpMethod = "GET", notes = "查询公告列表",tags = {"desk_staff API"})
	public Object sendNotice(@PathVariable("id") String id) throws Exception;

	/**
	 * 公告类型下拉框
	 * @return
	 * @throws Exception
	 */
	@GetMapping(value = "/publicNotice/noticeType/list", produces = {"application/json; charset=utf-8"})
	@ApiOperation(value = "公告类型下拉框", httpMethod = "GET", notes = "公告类型下拉框",tags = {"desk_staff API"})
	public Object getNoticeTypeList();

	/**
	 * 导出公告列表数据
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	@PostMapping(value = "/publicNotice/export", produces = {"application/json; charset=utf-8"})
	@ApiOperation(value = "导出公告列表数据", httpMethod = "POST", notes = "导出公告列表数据",tags = {"desk_staff API"})
	public void exportNoticeList(@RequestBody PublicNoticeVo vo) throws Exception;

	/**
	 *首页查询公告列表
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	@PostMapping(value = "/publicNotice/findListHomePage", produces = {"application/json; charset=utf-8"})
	@ApiOperation(value = "首页查询公告列表", httpMethod = "POST", notes = "首页查询公告列表",tags = {"desk_staff API"})
	public Object findListHomePage(@RequestBody PublicNoticeVo vo) throws Exception;


	/**

	 * 值班签到签退
	 * @param req
	 * @return
	 */
	@PostMapping(value = "/dutyCheckIn", produces = {"application/json; charset=utf-8"})
	@ApiOperation(value = "值班签到签退", httpMethod = "POST", notes = "值班签到签退",tags = {"desk_staff API"})
	public Object dutyCheckIn(@RequestBody CheckInReq req);

	/**

	 * 新增系统操作日志
	 * @param req
	 * @return
	 */
	@PostMapping(value = "/saveDeskLogs", produces = {"application/json; charset=utf-8"})
	@ApiOperation(value = "新增系统操作日志", httpMethod = "POST", notes = "新增系统操作日志",tags = {"desk_staff API"})
	public Object saveDeskLogs(@RequestBody DeskLogs req);

	/**

	 * 公告列表_操作状态变更
	 * @return
	 * @throws Exception
	 */
	@PostMapping(value = "/operateStatus/{status}", produces = {"application/json; charset=utf-8"})
	@ApiOperation(value = "公告列表_操作状态变更", httpMethod = "POST", notes = "公告列表_操作状态变更",tags = {"desk_staff API"})
	public Object updateOperateStatus(@RequestBody Map<String,List<String>> mp,
									  @PathVariable("status") String status) throws Exception;

	/**	 *  服务事件工单-启动流程
	 */
	@PostMapping(value = "/zxgllc/start", produces = {"application/json; charset=utf-8"})
	@ApiOperation(value = "服务事件工单-启动流程", httpMethod = "POST", notes = "服务事件工单-启动流程",tags = {"desk_staff API"})
	public Object startWithZxbzdx(@RequestBody ServiceDeskWithZxgllcVo vo) throws Exception;

	/**
	 *  服务事件工单-保存草稿
	 */
	@PostMapping(value = "/zxgllc/startDraft", produces = {"application/json; charset=utf-8"})
	@ApiOperation(value = "服务事件工单-保存草稿", httpMethod = "POST", notes = "服务事件工单-保存草稿",tags = {"desk_staff API"})
	public Object startDraftWithZxbzdx(@RequestBody ServiceDeskWithZxgllcVo vo) throws Exception;

	/**
	 *  统计服务事件工单相关数量
	 */
	@GetMapping(value = "/zxgllc/statist", produces = {"application/json; charset=utf-8"})
	@ApiOperation(value = "统计服务事件工单相关数量", httpMethod = "GET", notes = "统计服务事件工单相关数量",tags = {"desk_staff API"})
	public Object statistWithZxbzdx() throws Exception;

	/**
	 *  服务事件工单列表
	 */
	@PostMapping(value = "/zxgllc/list", produces = {"application/json; charset=utf-8"})
	@ApiOperation(value = "服务事件工单列表", httpMethod = "POST", notes = "服务事件工单列表",tags = {"desk_staff API"})
	public Object listWithZxbzdx(@RequestBody Map<String,Object> req) throws Exception;

	/**
	 *查询流程实例列表
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	@PostMapping(value = "/runtime/getInstDetailListForDesk", produces = {"application/json; charset=utf-8"})
	@ApiOperation(value = "查询流程实例列表", httpMethod = "POST", notes = "查询流程实例列表",tags = {"desk_staff API"})
	public Object getInstDetailListForDesk(@RequestBody PublicNoticeVo vo) throws Exception;

	@RequestMapping(value="/remind/list", method= RequestMethod.POST, produces={"application/json; charset=utf-8" })
	@ApiOperation(value = "催办工单列表", httpMethod = "POST", notes = "催办工单列表",tags = {"desk_staff API"})
	public Object listWithRemind(@RequestBody Map<String,Object> req);

	@RequestMapping(value="/zxgllc/dropData", method= RequestMethod.GET, produces={"application/json; charset=utf-8" })
	@ApiOperation(value = "服务事件工单的下拉框数据", httpMethod = "GET", notes = "服务事件工单的下拉框数据",tags = {"desk_staff API"})
	public Object listDropDataWithZxbzdx(@RequestParam("aliasName") String aliasName,
										 @RequestParam("pageNo") Integer pageNo,
										 @RequestParam("pageSize") Integer pageSize);

	@RequestMapping(value="/zxgllc/dropData/department", method= RequestMethod.GET, produces={"application/json; charset=utf-8" })
	@ApiOperation(value = "服务事件工单的下拉框数据(针对部门信息)", httpMethod = "GET", notes = "服务事件工单的下拉框数据(针对部门信息)",tags = {"desk_staff API"})
	public Object listDropDataToDepWithZxbzdx(@RequestParam("parentId") String parentId);

	@RequestMapping(value="/zxgllc/dropData/userInfo", method= RequestMethod.GET, produces={"application/json; charset=utf-8" })
	@ApiOperation(value = "服务事件工单的下拉框数据(针对用户信息)", httpMethod = "GET", notes = "服务事件工单的下拉框数据(针对用户信息)",tags = {"desk_staff API"})
	public Object listDropToUserWithZxbzdx(@RequestParam(value = "search",required = false) String search,
										   @RequestParam(value = "pageNo",required = false) Integer pageNo,
										   @RequestParam(value = "pageSize",required = false) Integer pageSize);

	@RequestMapping(value="/remind/export", method= RequestMethod.POST, produces={"application/json; charset=utf-8" })
	@ApiOperation(value = "催办工单列表数据导出", httpMethod = "POST", notes = "催办工单列表数据导出",tags = {"desk_staff API"})
	public void exportRemindList(@RequestBody Map<String,Object> req);
}
