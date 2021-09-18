package com.aspire.mirror.alert.api.service.bpm;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/v2/alerts/bpm")
@Api("告警BPM")
public interface AlertBpmService {

    @GetMapping(value = "/alertConfirmByOrderId")
    @ApiOperation(value = "根据工单号告警确认", notes = "根据工单号告警确认", tags = {"Alert Bpm API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功")})
    ResponseEntity<String> alertConfirmByOrderId(@RequestParam("username") String username,
                                                 @RequestParam("orderId") String orderId,
                                                 @RequestParam("content") String content);

    @GetMapping(value = "/alertRemoveByOrderId")
    @ApiOperation(value = "根据工单号告警清除", notes = "根据工单号告警清除", tags = {"Alert Bpm API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功")})
    ResponseEntity<String> alertRemoveByOrderId(@RequestParam("username") String username,
                                                @RequestParam("orderId") String orderId,
                                                @RequestParam("content") String content);

    @PutMapping(value = "/updateOrderByOrderId")
    @ApiOperation(value = "工单类型升级调优工单", notes = "工单类型升级调优工单", tags = {"Alert Bpm API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功")})
    ResponseEntity<String> updateOrderByOrderId(@RequestParam("username") String username,
                                                @RequestParam("oldOrderId") String oldOrderId,
                                                @RequestParam("newOrderId") String newOrderId);

    /**
     * 同步工单处理状态
     * @param orderId
     * @param execTime
     * @param status
     * @return
     */
    @PutMapping(value = "/updateOrderStatus/{orderId}")
    @ApiOperation(value = "同步工单处理状态", notes = "同步工单处理状态", tags = {"Alert Bpm API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功")})
    ResponseEntity<String> updateOrderStatus(@PathVariable("orderId") String orderId,
                                             @RequestParam(value = "execUser", required = false) String execUser,
                                             @RequestParam(value = "account", required = false) String account,
                                             @RequestParam("execTime") String execTime,
                                             @RequestParam("status") String status);
}
