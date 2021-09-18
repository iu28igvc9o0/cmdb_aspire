package com.aspire.mirror.composite.service.desk.bpm;

import com.aspire.mirror.composite.service.desk.bpm.payload.BpmRemindRecordVo;
import com.aspire.mirror.composite.service.order.payload.PageBean;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.aspire.mirror.composite.service.desk.bpm.payload.DeskOrderReq;

import io.swagger.annotations.ApiOperation;

/**
 * @projectName: BpmInstAPI
 * @description: 接口
 * @author: tongzhihong
 * @create: 2020-09-14 10:37
 **/
@Api("desk_bpm")
@RequestMapping("${version}/bpm/runTime/instance")
public interface BpmInstAPI {
	/**
	 *获取工单数据
	 * @param req
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/getOrderList", method = RequestMethod.POST, produces = {"application/json; charset=utf-8"})
	@ApiOperation(value = "获取工单数据", httpMethod = "POST", notes = "获取工单数据",tags = {"desk API "})
	public Object getOrderList(@RequestBody DeskOrderReq req) throws Exception;

	@RequestMapping(value = "/getOrderListNew", method = RequestMethod.POST, produces = {"application/json; charset=utf-8"})
	@ApiOperation(value = "获取工单数据(包含工单统计数量)", httpMethod = "POST", notes = "获取工单数据(包含工单统计数量)",tags = {"desk API "})
	public Object getOrderListNew(@RequestBody DeskOrderReq req) throws Exception;

	/**
	 *催办
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/reminder", method = RequestMethod.POST, produces = {"application/json; charset=utf-8"})
	@ApiOperation(value = "催办", httpMethod = "POST", notes = "催办",tags = {"desk API "})
	public Object reminder(@RequestBody BpmRemindRecordVo vo) throws Exception;

}
