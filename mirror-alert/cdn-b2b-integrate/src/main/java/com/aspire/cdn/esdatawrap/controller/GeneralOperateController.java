package com.aspire.cdn.esdatawrap.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aspire.cdn.esdatawrap.biz.client.model.SyncItemResponse;
import com.aspire.cdn.esdatawrap.biz.generaloperate.GeneralOperateBiz;
import com.aspire.cdn.esdatawrap.biz.generaloperate.model.AppClientInfo;
import com.aspire.cdn.esdatawrap.biz.generaloperate.model.HostPingRequestBody;
import com.aspire.cdn.esdatawrap.biz.generaloperate.model.HostPingResult;
import com.aspire.cdn.esdatawrap.common.GeneralResponse;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/** 
 *
 * 项目名称: cdn-b2b-integrate 
 * <p/>
 * 
 * 类名: GeneralOperateController
 * <p/>
 *
 * 类功能描述: 一般操作接口
 * <p/>
 *
 * @author	pengguihua
 *
 * @date	2020年8月13日  
 *
 * @version	V1.0 
 * <br/>
 *
 * <b>Copyright(c)</b> 2020 卓望公司-版权所有 
 *
 */
@Api(value = "一般操作API")
@RequestMapping(value = "/v1/cdnIntegrate/generalOperate")
@ConditionalOnExpression("!'none'.equals('${spring.main.web-application-type}')")
@RestController
public class GeneralOperateController {
	@Autowired
	private GeneralOperateBiz generalOperateBiz;
	
	@PostMapping(value = "/ping", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "对主机进行ping测试", notes = "对主机进行ping测试", tags = {"Genearal operate service API"})
	@ApiResponses(value = {@ApiResponse(code = 200, message = "返回", response = SyncItemResponse.class),
            @ApiResponse(code = 500, message = "Unexpected error")})
	public List<HostPingResult> pingHostList(@RequestBody HostPingRequestBody reqBody) {
		return generalOperateBiz.pingHostList(reqBody);
	}
	
	
	@PostMapping(value = "/pushAppClientInfo", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "上报app客户端信息", notes = "上报app客户端信息", tags = {"Genearal operate service API"})
	@ApiResponses(value = {@ApiResponse(code = 200, message = "返回", response = GeneralResponse.class),
            @ApiResponse(code = 500, message = "Unexpected error")})
	public GeneralResponse pushAppClientInfo(@RequestBody AppClientInfo clientInfo) {
		if (clientInfo.getRefreshTime() == null) {
			clientInfo.setRefreshTime(System.currentTimeMillis());
		}
		return generalOperateBiz.pushAppClientInfo(clientInfo);
	}
}
