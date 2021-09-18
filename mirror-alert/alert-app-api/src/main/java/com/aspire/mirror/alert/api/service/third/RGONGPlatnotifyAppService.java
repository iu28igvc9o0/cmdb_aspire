package com.aspire.mirror.alert.api.service.third;

import java.net.UnknownHostException;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

import com.aspire.mirror.alert.api.dto.third.*;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

public interface RGONGPlatnotifyAppService {

 
    @PostMapping("/v1/alerts/platnotifyApp/register")
    @ApiOperation(value = "通告对象注册",notes = "告警上报（第三方）",tags = {"Third API"})
    @ApiResponses(value = {
            @ApiResponse(code = 200,message = "成功"),
            @ApiResponse(code = 500,message = "操作失败")
    })
    CommonResp registerPlatnotifyApp(@RequestBody PlatnotifyAppReq thirdCreateAlertReq, @RequestParam(value="ip") String ip)throws Exception;

  
    @PostMapping("/v1/alerts/platnotifyApp/getAll")
    @ApiOperation(value = "通告对象获取",notes = "批量告警上报（第三方）",tags = {"Third API"})
    @ApiResponses(value = {
            @ApiResponse(code = 200,message = "成功"),
            @ApiResponse(code = 500,message = "内部错误")
    })
    List<PlatnotifyApp> getPlatnotifyApp(@RequestParam(value="ip") String ip)throws Exception;
    
 
    @PostMapping("/v1/alerts/platnotifyApp/getByName")
    @ApiOperation(value = "通告对象查询",notes = "批量告警上报（第三方）",tags = {"Third API"})
    @ApiResponses(value = {
            @ApiResponse(code = 200,message = "成功"),
            @ApiResponse(code = 500,message = "内部错误")
    })
    PlatnotifyApp getByName(@RequestParam(value="app_name") String app_name,
    		@RequestParam(value="ip") String ip)throws Exception;
    
    @PostMapping("/v1/alerts/platnotifyApp/update")
    @ApiOperation(value = "通告对象修改",notes = "批量告警上报（第三方）",tags = {"Third API"})
    @ApiResponses(value = {
            @ApiResponse(code = 200,message = "成功"),
            @ApiResponse(code = 500,message = "内部错误")
    })
    CommonResp update(@RequestBody PlatnotifyAppReq thirdCreateAlertReq,
    		@RequestParam(value="ip") String ip)throws Exception;
    
    @PostMapping("/v1/alerts/platnotifyApp/delete")
    @ApiOperation(value = "通告对象删除",notes = "批量告警上报（第三方）",tags = {"Third API"})
    @ApiResponses(value = {
            @ApiResponse(code = 200,message = "成功"),
            @ApiResponse(code = 500,message = "内部错误")
    })
    CommonResp delete(@RequestParam(value="app_name") String app_name,
    		@RequestParam(value="ip") String ip) throws Exception;
    
    //level:INFO WARN ERROR,时间格式：yyyyMMdd HH:mm:ss
    @PostMapping("/v1/alerts/platnotifyApp/alertReport/{level}")
    @ApiOperation(value = "告警上报",notes = "批量告警上报（第三方）",tags = {"Third API"})
    @ApiResponses(value = {
            @ApiResponse(code = 200,message = "成功"),
            @ApiResponse(code = 500,message = "内部错误")
    })
    CommonResp alertReport(@RequestBody List<PlatnotifyAppReportInfo> platnotifyAppReportList, HttpServletRequest request
    		, @PathVariable("level") String level)throws UnknownHostException;


    @PostMapping("/v1/alerts/platnotifyApp/alertReport/getSubject")
    @ApiOperation(value = "告警上报",notes = "批量告警上报（第三方）",tags = {"Third API"})
    @ApiResponses(value = {
            @ApiResponse(code = 200,message = "成功"),
            @ApiResponse(code = 500,message = "内部错误")
    })
	SubjectsEnum[] getSubject() throws Exception;
}
