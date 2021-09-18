package com.aspire.mirror.alert.server.controller.operateLog;

import com.aspire.mirror.alert.api.dto.operateLog.AlertOperateLogResp;
import com.aspire.mirror.alert.api.service.operateLog.AlertOperateLogService;
import com.aspire.mirror.alert.server.biz.operateLog.IAlertOperateLogBiz;
import com.aspire.mirror.alert.server.dao.operateLog.po.AlertOperateLog;
import com.aspire.mirror.alert.server.vo.operateLog.AlertOperateLogDTO;
import com.aspire.mirror.alert.server.util.PayloadParseUtil;
import com.aspire.mirror.common.entity.PageResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
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
public class AlertOperateLogController implements AlertOperateLogService {
    @Autowired
    private IAlertOperateLogBiz alertOperateLogBiz;

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
    public PageResponse<AlertOperateLogResp> list(@RequestParam(value = "relationId", required = false) String relationId,
                                                  @RequestParam(value = "operateModel", required = false) String operateModel,
                                                  @RequestParam(value = "operateType", required = false) String operateType,
                                                  @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                                  @RequestParam(value = "pageSize", defaultValue = "50") int pageSize) {
        AlertOperateLogDTO alertOperateLogDTO = new AlertOperateLogDTO();
        alertOperateLogDTO.setRelationId(relationId);
        alertOperateLogDTO.setOperateModel(operateModel);
        alertOperateLogDTO.setOperateType(operateType);
        int begin = (pageNum - 1) * pageSize;
        alertOperateLogDTO.setBegin(begin);
        alertOperateLogDTO.setPageSize(pageSize);
        PageResponse<AlertOperateLogResp> pageResponse = new PageResponse<>();
        PageResponse<AlertOperateLog> page = alertOperateLogBiz.findPage(alertOperateLogDTO);
        pageResponse.setCount(page.getCount());
        List<AlertOperateLog> result = page.getResult();
        if (CollectionUtils.isNotEmpty(result)) {
            pageResponse.setResult(PayloadParseUtil.jacksonBaseParse(AlertOperateLogResp.class, result));
        }
        return pageResponse;
    }
}
