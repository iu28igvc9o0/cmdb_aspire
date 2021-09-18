package com.migu.tsg.microservice.atomicservice.composite.controller.operateLog;

import com.aspire.mirror.alert.api.dto.operateLog.AlertOperateLogResp;
import com.aspire.mirror.common.entity.PageResponse;
import com.aspire.mirror.composite.payload.operateLog.CompAlertOperateLogResp;
import com.aspire.mirror.composite.service.operateLog.ICompAlertOperateLogService;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.operateLog.AlertOperateLogServiceClient;
import com.migu.tsg.microservice.atomicservice.composite.controller.util.PayloadParseUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author baiwp
 * @title: AlertOperateLogController
 * @projectName mirror-alert
 * @description: TODO
 * @date 2019/8/1614:21
 */
@Slf4j
@RestController
public class CompAlertOperateLogController implements ICompAlertOperateLogService {
    @Autowired
    private AlertOperateLogServiceClient alertOperateLogServiceClient;

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
    public PageResponse<CompAlertOperateLogResp> list(@RequestParam(value = "relationId", required = false) String relationId,
                                                      @RequestParam(value = "operateModel", required = false) String operateModel,
                                                      @RequestParam(value = "operateType", required = false) String operateType,
                                                      @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                                      @RequestParam(value = "pageSize", defaultValue = "50") int pageSize) {
        PageResponse<AlertOperateLogResp> list = alertOperateLogServiceClient.list(relationId, operateModel, operateType, pageNum, pageSize);
        List<CompAlertOperateLogResp> compAlertOperateLogRespList = PayloadParseUtil.jacksonBaseParse(CompAlertOperateLogResp.class, list.getResult());
        PageResponse<CompAlertOperateLogResp> page = new PageResponse<>();
        page.setCount(list.getCount());
        page.setResult(compAlertOperateLogRespList);
        return page;
    }
}
