package com.aspire.mirror.composite.service.operateLog;

import com.aspire.mirror.common.entity.PageResponse;
import com.aspire.mirror.composite.payload.operateLog.CompAlertOperateLogResp;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author baiwp
 * @title: AlertOperateLogService
 * @projectName mirror-alert
 * @description: TODO
 * @date 2019/8/1510:51
 */
@RequestMapping("/${version}/alerts/log")
public interface ICompAlertOperateLogService {

    /**
     * 查询告警操作日志列表
     *
     * @param relationId
     * @param operateModel
     * @param operateType
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "查询告警操作日志列表", notes = "查询告警操作日志列表", response = PageResponse.class, tags = {"AlertLog API"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回", response = PageResponse.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = PageResponse.class)})
    PageResponse<CompAlertOperateLogResp> list(@RequestParam(value = "relationId", required = false) String relationId,
                                               @RequestParam(value = "operateModel", required = false) String operateModel,
                                               @RequestParam(value = "operateType", required = false) String operateType,
                                               @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                               @RequestParam(value = "pageSize", defaultValue = "50") int pageSize);
}
