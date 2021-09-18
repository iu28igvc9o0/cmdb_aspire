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
import com.aspire.cdn.esdatawrap.biz.thirdpartyalert.ThirdPartyAlertBody;
import com.aspire.cdn.esdatawrap.biz.thirdpartyalert.ThirdpartyAlertReceiveBiz;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/** 
 *
 * 项目名称: cdn-b2b-integrate 
 * <p/>
 * 
 * 类名: ThirdPartyAlertReceiveController
 * <p/>
 *
 * 类功能描述: 第三方告警接入
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
@Api(value = "第三方告警接入API")
@RequestMapping(value = "/v1/cdnIntegrate/thirdpartyAlert")
@ConditionalOnExpression("!'none'.equals('${spring.main.web-application-type}') && ${thirdpartyAlert.switch:false}")
@RestController
public class ThirdPartyAlertReceiveController {
	@Autowired
	private ThirdpartyAlertReceiveBiz syncBiz;
	
	@PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "接收单条第三方告警", notes = "接收单条第三方告警", tags = {"Third party alert sync service API"})
	@ApiResponses(value = {@ApiResponse(code = 200, message = "返回", response = SyncItemResponse.class),
            @ApiResponse(code = 500, message = "Unexpected error")})
	public SyncItemResponse thirdpartyAlertSync(@RequestBody ThirdPartyAlertBody alertBody) {
		return syncBiz.thirdpartyAlertSync(alertBody);
	}
	
	@PostMapping(value = "/batch", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "批量接收第三方告警", notes = "批量接收第三方告警", tags = {"Third party alert sync service API"})
	@ApiResponses(value = {@ApiResponse(code = 200, message = "返回", response = SyncItemResponse.class),
            @ApiResponse(code = 500, message = "Unexpected error")})
	public SyncItemResponse thirdpartyAlertBatchSync(@RequestBody List<ThirdPartyAlertBody> alertList) {
		return syncBiz.thirdpartyAlertBatchSync(alertList);
	}
}
