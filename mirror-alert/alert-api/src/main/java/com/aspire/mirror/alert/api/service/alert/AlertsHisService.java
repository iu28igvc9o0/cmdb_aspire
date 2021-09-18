package com.aspire.mirror.alert.api.service.alert;

import com.aspire.mirror.alert.api.dto.alert.AlertGenResp;
import com.aspire.mirror.alert.api.dto.alert.AlertsDetailResponse;
import com.aspire.mirror.alert.api.dto.alert.AlertsHisDetailResponse;
import com.aspire.mirror.alert.api.dto.alert.AlertsHisPageRequset;
import com.aspire.mirror.common.entity.PageResponse;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 历史告警服务
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.alert.api.metrics
 * 类名称:    AlertsHisService.java
 * 类描述:    历史告警服务
 * 创建人:    JinSu
 * 创建时间:  2018/9/19 11:19
 * 版本:      v1.0
 */
public interface AlertsHisService {
    /**
     * 历史告警上报记录
     *
     * @param alertId 告警Id
     * @return
     */
    @GetMapping(value = "/v1/alerts_his/alertGenerateList")
    @ApiOperation(value = "详情", notes = "根据alert_id获取上报记录", tags = {"AlertHis API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功", response = AlertsDetailResponse.class),
            @ApiResponse(code = 500, message = "内部错误")})
    PageResponse<AlertGenResp> alertGenerateList(@RequestParam("alert_id") String alertId,
                                                 @RequestParam("page_no") String pageNo,
                                                 @RequestParam("page_size") String pageSize);
    
   
    /**
     * 历史告警上报excel 下载
     *
     * @param alertId 告警Id
     * @return
     */
    @GetMapping(value = "/v1/alerts_his/alertGenerateDownload")
    @ApiOperation(value = "详情", notes = "根据alert_id获取告警上报", tags = {"AlertHis API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功", response = AlertsDetailResponse.class),
            @ApiResponse(code = 500, message = "内部错误")})
    List<AlertGenResp> alertGenerateDownload(@RequestParam("alert_id") String alertId  );

    /**
     * 修改历史告警备注
     *  
     */
    
    @GetMapping(value = "/v1/alerts_his/updateNote/{alert_id}")
    @ApiOperation(value = "修改告警备注", notes = "修改告警备注", tags = {"AlertHis API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功")})
    ResponseEntity<String>  updateNote( @PathVariable("alert_id") String alertId, @RequestParam("note") String note );


    /**
     * 当前告警详情-历史告警列表
     * @param pageRequset 查询page对象
     * @return PageResponse 列表返回对象
     */
    @PostMapping(value = "/v1/alerts_his/getAlertHisList")
    @ApiOperation(value = "获取历史告警列表", notes = "获取历史告警列表", tags = {"AlertHis API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功", response = PageResponse.class),
            @ApiResponse(code = 500, message = "内部错误")})
    PageResponse<AlertsHisDetailResponse> getAlertHisList(@RequestBody AlertsHisPageRequset pageRequset);
    
    
    /**
     * 历史告警关联数据
     *
     * @param alertId 告警Id
     * @return
     */
    @GetMapping(value = "/v1/alerts_his/alertAssociatedData")
    @ApiOperation(value = "详情", notes = "根据alert_id获取关联数据", tags = {"AlertHis API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功", response = AlertsDetailResponse.class),
            @ApiResponse(code = 500, message = "内部错误")})
    PageResponse<Map<String,Object>> alertRelateData(@RequestParam("alert_id") String alertId,
    		@RequestParam("alert_type") int alertType
    		,@RequestParam(value="source" ,required =false ) String source
    		,@RequestParam(value="pageSize" ,required =false )   Integer pageSize
    		,@RequestParam(value="pageNo" ,required =false ) Integer pageNo);
}
