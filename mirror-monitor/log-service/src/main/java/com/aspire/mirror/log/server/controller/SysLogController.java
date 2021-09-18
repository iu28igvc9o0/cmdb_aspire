package com.aspire.mirror.log.server.controller;

import com.aspire.mirror.common.entity.PageResponse;
import com.aspire.mirror.log.api.dto.*;
import com.aspire.mirror.log.api.service.ISysLogService;
import com.aspire.mirror.log.server.biz.SysLogBiz;
import com.google.common.collect.Maps;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;


/**
 * 系统日志服务控制层
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.log.server.controller
 * 类名称:    SysLogController.java
 * 类描述:    系统日志服务控制层
 * 创建人:    JinSu
 * 创建时间:  2019/6/14 15:09
 * 版本:      v1.0
 */
@RestController
public class SysLogController implements ISysLogService {

    Logger logger = LoggerFactory.getLogger(SysLogController.class);

    @Autowired
    private SysLogBiz sysLogBiz;

    @Override
    public PageResponse<SysLogResponse> getLogData(@RequestBody SysLogSearchRequest request) {
        if (request == null) {
            logger.error("get syslog data param is null");
            throw new RuntimeException("get syslog data param is null");
        }
        return sysLogBiz.getLogData(request);
    }

    @Override
    public CreateLogFilterRuleResp createLogFilterRule(@RequestBody CreateLogFilterRuleReq request) {
        CreateLogFilterRuleResp response = new CreateLogFilterRuleResp();
        if (null == request) {
            logger.error("[logService]>>>Create Log Filter Rule Request is null");
            throw new RuntimeException("[logService]>>>Create Log Filter Rule Request is null");
        }
        logger.info("[logService]>>>Create Log Filter Rule Request is {}", request);
        try {
            LogFilterRuleDetail logFilterRuleDetailByName = sysLogBiz.getLogFilterRuleDetailByName(request.getName());
            if (null != logFilterRuleDetailByName) {
                response.setStatus("error");
                response.setResult("过滤规则名称已存在,请重新输入!");
            } else {
                response.setStatus("success");
                response.setResult(sysLogBiz.createLogFilterRule(request));
            }
        } catch (Exception e) {
            logger.error("[logService]>>>Create Log Filter Rule Error is {}", e);
            throw new RuntimeException("[logService]>>>Create Log Filter Rule Error is  {}", e);
        }
        return response;
    }

    @Override
    public LogFilterRuleDetail getLogFilterRuleDetail(@RequestParam("uuid") String uuid) {
        if (StringUtils.isBlank(uuid)) {
            logger.error("Get Log Filter Rule Detail Uuid is null");
            throw new RuntimeException("Get Log Filter Rule Detail Uuid is null");
        }
        logger.info("[logService]>>>Get Log Filter Rule Detail Uuid is {}", uuid);
        LogFilterRuleDetail logFilterRuleDetail = null;
        try {
            logFilterRuleDetail = sysLogBiz.getLogFilterRuleDetail(uuid);
            logger.info("[logService]>>>Get Log Filter Rule Detail Response is {}", logFilterRuleDetail);
        } catch (Exception e) {
            logger.error("[logService]>>>Get Log Filter Rule Detail Error is {}", e);
            throw new RuntimeException("[logService]>>>Get Log Filter Rule Detail Error is {}", e);
        }
        return logFilterRuleDetail;
    }

    @Override
    public List<LogFilterRuleDetail> getLogFilterRuleList(@RequestParam(value = "ruleType",required = false) String ruleType) {
        try {
            List<LogFilterRuleDetail> response = sysLogBiz.getLogFilterRuleList(ruleType);
            logger.info("[logService]>>>Get Log Filter Rule List Response is {}", response);
            return response;
        } catch (Exception e) {
            logger.error("[logService]>>>Get Log Filter Rule List Error is {}", e);
            throw new RuntimeException("[logService]>>>Get Log Filter Rule List Error is {}", e);
        }
    }

    @Override
    public void deleteLogFilterRule(@RequestParam("uuid") String uuid) {
        if (StringUtils.isBlank(uuid)) {
            logger.error("Delete Log Filter Rule Uuid is null");
            throw new RuntimeException("Delete Log Filter Rule Uuid is null");
        }
        try {
            sysLogBiz.deleteLogFilterRule(uuid);
        } catch (Exception e) {
            logger.error("[logService]>>>Delete Log Filter Rule Error is {}", e);
            throw new RuntimeException("[logService]>>>Delete Log Filter Rule Error is {}", e);
        }
    }


    @Override
    public PageResponse<SysLogResponse> getLogDataByFilterRule(@RequestParam("uuid") String uuid,
                                                               @RequestParam("pageNo") String pageNo,
                                                               @RequestParam("pageSize") String pageSize) {
        if (StringUtils.isBlank(uuid)) {
            logger.error("[logService]>>>Get Log Filter Rule Detail Uuid is null");
            throw new RuntimeException("[logService]>>>Get Log Filter Rule Detail Uuid is null");
        }
        PageResponse<SysLogResponse> logDataByFilterRule = new PageResponse<SysLogResponse>();
        try {
            logDataByFilterRule = sysLogBiz.getLogDataByFilterRule(uuid,pageNo,pageSize);
        } catch (Exception e) {
            logger.error("[logService]>>>Get Log Data By Filter Rule Error is {}", e);
            throw new RuntimeException("[logService]>>>Get Log Data By Filter Rule Error is {}", e);
        }
        return logDataByFilterRule;
    }

    @Override
    public void updateLogFilterRule(@RequestBody CreateLogFilterRuleReq request) {
        if (null == request) {
            logger.error("[logService]>>>Update Log Filter Rule Request is null");
            throw new RuntimeException("[logService]>>>Update Log Filter Rule Request is null");
        }
        logger.info("[logService]>>>Update Log Filter Rule Request is {}", request);
        try {
            sysLogBiz.deleteLogFilterRule(request.getUuid());
            sysLogBiz.createLogFilterRule(request);
        } catch (Exception e) {
            logger.error("[logService]>>>Update Log Filter Rule Error is {}", e);
            throw new RuntimeException("[logService]>>>Update Log Filter Rule Error is  {}", e);
        }
    }

    @Override
    public CreateLogFilterRuleResp createLogAlertRule(@RequestBody CreateLogAlertRuleReq request) {
        CreateLogFilterRuleResp response = new CreateLogFilterRuleResp();
        if (null == request) {
            logger.error("[logService]>>>Create Log Alert Rule Request is null");
            throw new RuntimeException("[logService]>>>Create Log Alert Rule Request is null");
        }
        logger.info("[logService]>>>Create Log Alert Rule Request is {}", request);
        try {
            LogAlertRuleDetail logAlertRuleDetail = sysLogBiz.getLogAlertRuleDetailByName(request.getName());
            if (null != logAlertRuleDetail) {
                response.setStatus("error");
                response.setResult("过滤规则名称已存在,请重新输入!");
            } else {
                response.setStatus("success");
                response.setResult(sysLogBiz.createLogAlertRule(request));
            }
        } catch (Exception e) {
            logger.error("[logService]>>>Create Log Alert Rule Error is {}", e);
            throw new RuntimeException("[logService]>>>Create Log Alert Rule Error is  {}", e);
        }
        return response;
    }

    @Override
    public LogAlertRuleDetail getLogAlertRuleDetail(@RequestParam("uuid") String uuid) {
        if (StringUtils.isBlank(uuid)) {
            logger.error("Get Log Alert Rule Detail Uuid is null");
            throw new RuntimeException("Get Log Alert Rule Detail Uuid is null");
        }
        logger.info("[logService]>>>Get Log Alert Rule Detail Uuid is {}", uuid);
        LogAlertRuleDetail logAlertRuleDetail = null;
        try {
            logAlertRuleDetail = sysLogBiz.getLogAlertRuleDetail(uuid);
            logger.info("[logService]>>>Get Log Alert Rule Detail Response is {}", logAlertRuleDetail);
        } catch (Exception e) {
            logger.error("[logService]>>>Get Log Alert Rule Detail Error is {}", e);
            throw new RuntimeException("[logService]>>>Get Log Alert Rule Detail Error is {}", e);
        }
        return logAlertRuleDetail;
    }

    @Override
    public PageResponse<LogAlertRuleDetail> getLogAlertRuleList(@RequestBody LogAlertRuleListReq request) {
        if (null == request) {
            logger.error("[logService]>>>Get Log Alert Rule List Request is null");
            throw new RuntimeException("[logService]>>>Get Log Alert Rule List Request is null");
        }
        logger.info("[logService]>>>Get Log Alert Rule List Request is {}", request);
        PageResponse<LogAlertRuleDetail> response = new PageResponse<LogAlertRuleDetail>();
        try {
            response = sysLogBiz.getLogAlertRuleList(request);
        } catch (Exception e) {
            logger.error("[logService]>>>Get Log Alert Rule List Error is {}", e);
            throw new RuntimeException("[logService]>>>Get Log Alert Rule List Error is {}", e);
        }
        return response;
    }

    @Override
    public void deleteLogAlertRule(@RequestBody List<String> uuidList) {
        try {
            sysLogBiz.deleteLogAlertRule(uuidList);
        } catch (Exception e) {
            logger.error("[logService]>>>Delete Log Alert Rule Error is {}", e);
            throw new RuntimeException("[logService]>>>Delete Log Alert Rule Error is {}", e);
        }
    }

    @Override
    public void updateLogAlertRule(@RequestBody CreateLogAlertRuleReq request) {
        if (null == request) {
            logger.error("[logService]>>>Update Log Alert Rule Request is null");
            throw new RuntimeException("[logService]>>>Update Log Alert Rule Request is null");
        }
        logger.info("[logService]>>>Update Log Filter Rule Request is {}", request);
        try {
            sysLogBiz.updateLogAlertRule(request);
        } catch (Exception e) {
            logger.error("[logService]>>>Update Log Alert Rule Error is {}", e);
            throw new RuntimeException("[logService]>>>Update Log Alert Rule Error is  {}", e);
        }
    }

    @Override
    public void openLogAlertRule(@RequestBody List<String> uuidList) {
        Map<String,Object> open = Maps.newHashMap();
        open.put("runStatus","1");
        open.put("uuidList",uuidList);
        try {
            sysLogBiz.updateRunStatusByUuid(open);
        } catch (Exception e) {
            logger.error("[logService]>>>Open Log Alert Rule Error is {}", e);
            throw new RuntimeException("[logService]>>>Open Log Alert Rule Error is {}", e);
        }
    }

    @Override
    public void closeLogAlertRule(@RequestBody List<String> uuidList) {
        Map<String,Object> close = Maps.newHashMap();
        close.put("runStatus","0");
        close.put("uuidList",uuidList);
        try {
            sysLogBiz.updateRunStatusByUuid(close);
        } catch (Exception e) {
            logger.error("[logService]>>>Close Log Alert Rule Error is {}", e);
            throw new RuntimeException("[logService]>>>Close Log Alert Rule Error is {}", e);
        }
    }

    @Override
    public void insertLogAlertLinked(@RequestBody CreateLogAlertLinkedReq request) {
        sysLogBiz.insertLogAlertLinked(request);
    }

    @Override
    public String checkName(@RequestParam("ruleName") String ruleName) {
        LogAlertRuleDetail logAlertRuleDetailByName = sysLogBiz.getLogAlertRuleDetailByName(ruleName);
        return null == logAlertRuleDetailByName ? null : logAlertRuleDetailByName.getUuid();
    }
}
