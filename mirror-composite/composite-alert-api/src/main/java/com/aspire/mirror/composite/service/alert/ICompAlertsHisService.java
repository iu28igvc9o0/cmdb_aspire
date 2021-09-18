package com.aspire.mirror.composite.service.alert;

import java.util.Map;
import com.aspire.mirror.composite.payload.alert.CompAlertGenResp;
import com.aspire.mirror.composite.payload.alert.CompAlertsHisDetailResponse;
import com.aspire.mirror.composite.payload.alert.CompAlertsHisPageRequset;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.aspire.mirror.common.entity.PageResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * 历史告警暴露服务
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.composite.service.alert
 * 类名称:    ICompAlertsHisService.java
 * 类描述:    历史告警暴露服务
 * 创建人:    JinSu
 * 创建时间:  2018/9/19 20:03
 * 版本:      v1.0
 */
@Api(value = "历史告警信息管理")
@RequestMapping("/${version}/alerts_his")
public interface ICompAlertsHisService {
    /**
     * 历史告警上报分页
     *
     * @param alertId 告警Id
     * @return
     */
    @GetMapping(value = "/alertGenerateList")
    @ApiOperation(value = "详情", notes = "根据alert_id获取上报记录", tags = {"Alerts His API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功", response = CompAlertGenResp.class),
            @ApiResponse(code = 500, message = "内部错误")})
    PageResponse<CompAlertGenResp> alertGenerateList(@RequestParam("alert_id") String alertId,
                                                     @RequestParam("page_no") String pageNo,
                                                     @RequestParam("page_size") String pageSize);
   
    
    
    /**
     * 历史告警上报excel 下载
     *
     * @param alertId 告警Id
     * @return
     */
    @GetMapping(value = "/alertGenerateDownload")
    @ApiOperation(value = "详情", notes = "根据alert_id获取告警上报", tags = {"Alerts His API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功" ),
            @ApiResponse(code = 500, message = "内部错误")})
    void alertGenerateDownload(@RequestParam("alert_id") String alertId  );
    
    
    
    
    /**
     * 修改历史告警备注
     */
    @GetMapping(value="/updateNote/{alert_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value="根据alert_id修改告警备注",notes="修改告警备注",tags={ "Alerts His API" })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回"),
            @ApiResponse(code = 500, message = "Unexpected error") })
    ResponseEntity<String> updateNote(@PathVariable("alert_id") String alertId, @RequestParam("note") String note  );


    /**
     * 当前告警详情-历史告警列表
     * @param pageRequset 查询page对象
     * @return PageResponse 列表返回对象
     */
    @PostMapping(value = "/getAlertHisList")
    @ApiOperation(value = "获取历史告警列表", notes = "获取历史告警列表", tags = {"Alerts His API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功", response = PageResponse.class),
            @ApiResponse(code = 500, message = "内部错误")})
    PageResponse<CompAlertsHisDetailResponse> getAlertHisList(@RequestBody CompAlertsHisPageRequset pageRequset);


    /**
     *  当前告警详情-先关影响列表
     * @param alertId
     * @return
     */
    @GetMapping(value = "/alertRelateData")
    @ApiOperation(value = "获取相关影响列表", notes = "获取先关影响列表", tags = {"Alerts His API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功", response = PageResponse.class),
            @ApiResponse(code = 500, message = "内部错误")})
    PageResponse<Map<String,Object>> alertRelateData(@RequestParam("alert_id") String alertId,
    		@RequestParam("alert_type") int alertType
    		,@RequestParam(value="source" ,required =false ) String source
    		,@RequestParam(value="pageSize" ,required =false )   Integer pageSize
    		,@RequestParam(value="pageNo" ,required =false ) Integer pageNo);


    /**
     *  当前告警详情-先关影响列表导出
     * @param alertId
     * @return
     */
    @GetMapping(value = "/exportAlertRelateData")
    @ApiOperation(value = "导出相关影响列表", notes = "导出先关影响列表", tags = {"Alerts His API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功", response = PageResponse.class),
            @ApiResponse(code = 500, message = "内部错误")})
	void exportAlertRelateData(@RequestParam("alert_id") String alertId,
    		@RequestParam("alert_type") int alertType
    		,@RequestParam(value="source" ,required =false ) String source);
    
}
