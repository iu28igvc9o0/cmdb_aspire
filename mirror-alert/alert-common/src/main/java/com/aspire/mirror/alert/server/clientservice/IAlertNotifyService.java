package com.aspire.mirror.alert.server.clientservice;

import com.aspire.mirror.alert.server.clientservice.payload.AlertNotifyWrap;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@FeignClient(value="rtz-alert-notify")
public interface IAlertNotifyService {
	
	@PutMapping(value = "/v1/rtz/alertNotify/")
	@ApiOperation(value = "软探针告警通知", notes = "软探针告警通知", tags = { "Alerts Notify" })
	@ApiResponses(value = { @ApiResponse(code = 204, message = "OK"), @ApiResponse(code = 500, message = "Unexpected error") })
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void rtzAlertNotify(@RequestBody AlertNotifyWrap alertMsg);
}
