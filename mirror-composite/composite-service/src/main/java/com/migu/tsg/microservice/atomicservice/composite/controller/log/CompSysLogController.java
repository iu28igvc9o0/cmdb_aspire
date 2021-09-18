package com.migu.tsg.microservice.atomicservice.composite.controller.log;

import com.aspire.mirror.common.entity.PageResponse;
import com.aspire.mirror.log.api.dto.*;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.log.ISysLogServiceClient;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.rabc.UserServiceClient;
import com.migu.tsg.microservice.atomicservice.composite.controller.CommonResourceController;
import com.migu.tsg.microservice.atomicservice.composite.controller.authcontext.RequestAuthContext;
import com.migu.tsg.microservice.atomicservice.composite.controller.util.EasyPoiUtil;
import com.migu.tsg.microservice.atomicservice.composite.service.logs.ICompSysLogService;
import com.migu.tsg.microservice.atomicservice.composite.service.logs.payload.*;
import com.migu.tsg.microservice.atomicservice.rbac.dto.vo.UserVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

import static com.migu.tsg.microservice.atomicservice.composite.controller.util.PayloadParseUtil.jacksonBaseParse;

/**
 * TODO
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.migu.tsg.microservice.atomicservice.composite.controller.log
 * 类名称:    CompSysLogController.java
 * 类描述:    TODO
 * 创建人:    JinSu
 * 创建时间:  2019/6/14 10:54
 * 版本:      v1.0
 */
@RestController
@Slf4j
public class CompSysLogController extends CommonResourceController implements ICompSysLogService {

    private static final String SUCCESS = "success";

    @Autowired
    private ISysLogServiceClient sysLogService;

    @Autowired
    private UserServiceClient userClient;

    /**
     * 查询系统日志
     * @param request 系统日志查询请求
     * @return PageResponse page返回对象
     */
    @Override
    public PageResponse<CompSysLogResponse> getLogData(@RequestBody CompSysLogSearchRequest request) {
        PageResponse<SysLogResponse> result = sysLogService.getLogData(jacksonBaseParse(SysLogSearchRequest.class, request));
        PageResponse<CompSysLogResponse> response = new PageResponse<>();
        response.setCount(result.getCount());
        List<CompSysLogResponse> list = jacksonBaseParse(CompSysLogResponse.class, result.getResult());
        response.setResult(list);
        return response;
    }

    /**
     * 导出
     * @param request 导出请求对象
     */
    @Override
    public void exportSysLog(@RequestBody SysLogExportRequest request, HttpServletResponse response) {
        SysLogSearchRequest req = jacksonBaseParse(SysLogSearchRequest.class, request);
        req.setIsExport("1");
        PageResponse<SysLogResponse> result = sysLogService.getLogData(req);
        List<CompSysLogResponse> list = jacksonBaseParse(CompSysLogResponse.class, result.getResult());
        EasyPoiUtil.exportExcel(list, "系统日志导出列表", "syslog", CompSysLogResponse.class, "syslog_export", true, response);
    }

    @Override
    public CompCreateLogFilterRuleResp createLogFilterRule(@RequestBody CompCreateLogFilterRuleReq request) {
        if (null == request) {
            log.error("[Composite]>>>create Log Filter Rule request is null");
            throw new RuntimeException("[Composite]>>>Create Log Filter Rule Request is null");
        }
        log.info("[Composite]>>>Create Log Filter Rule Request is {}", request);
        CompCreateLogFilterRuleResp response = null;
        try {
            response = jacksonBaseParse(CompCreateLogFilterRuleResp.class,
                    sysLogService.createLogFilterRule(jacksonBaseParse(CreateLogFilterRuleReq.class, request)));
            return response;
        } catch (Exception e) {
            log.error("[Composite]>>>Create Log Filter Rule Error is {}", e);
            throw new RuntimeException("[Composite]>>>Create Log Filter Rule Error is {}", e);
        }
    }

    @Override
    public CompLogFilterRuleDetail getLogFilterRuleDetail(@RequestParam("uuid") String uuid) {
        if (StringUtils.isBlank(uuid)) {
            log.error("[Composite]>>>Get Log Filter Rule Detail Uuid is null");
            throw new RuntimeException("[Composite]>>>Get Log Filter Rule Detail Uuid is null");
        }
        try {
            CompLogFilterRuleDetail response =
                    jacksonBaseParse(CompLogFilterRuleDetail.class, sysLogService.getLogFilterRuleDetail(uuid));
            log.info("[Composite]>>>Get Log Filter Rule Detail Response is {}", response);
            return response;
        } catch (Exception e) {
            log.error("[Composite]>>>Get Log Filter Rule Detail error is {}", e);
            throw new RuntimeException("[Composite]>>>Get Log Filter Rule Detail error is {}", e);
        }
    }

    @Override
    public List<CompLogFilterRuleDetail> getLogFilterRuleList(@RequestParam(value = "ruleType",required = false) String ruleType) {
        try {
            List<CompLogFilterRuleDetail> response = jacksonBaseParse(CompLogFilterRuleDetail.class, sysLogService.getLogFilterRuleList(ruleType));
            log.info("[Composite]>>>Get Log Filter Rule List Response is {}", response);
            return response;
        } catch (Exception e) {
            log.error("[Composite]>>>Get Log Filter Rule List error is {}", e);
            throw new RuntimeException("[Composite]>>>Get Log Filter Rule List error is {}", e);
        }
    }

    @Override
    public String deleteLogFilterRule(@RequestParam("uuid") String uuid) {
        if (StringUtils.isBlank(uuid)) {
            log.error("[Composite]>>>Delete Log Filter Rule Uuid is null");
            throw new RuntimeException("[Composite]>>>Delete Log Filter Rule Uuid is null");
        }
        try {
            sysLogService.deleteLogFilterRule(uuid);
            return SUCCESS;
        } catch (Exception e) {
            log.error("[Composite]>>>Delete Log Filter Rule Error is {}", e);
            throw new RuntimeException("[Composite]>>>Delete Log Filter Rule Error is {}", e);
        }
    }

    @Override
    public PageResponse<CompSysLogResponse> getLogDataByFilterRule(@RequestParam("uuid") String uuid,
                                                                   @RequestParam("pageNo") String pageNo,
                                                                   @RequestParam("pageSize") String pageSize) {
        if (StringUtils.isBlank(uuid)) {
            log.error("[Composite]>>>Get Log Data By Filter Rule Uuid is null");
            throw new RuntimeException("[Composite]>>>Get Log Data By Filter Rule Uuid is null");
        }
        PageResponse<CompSysLogResponse> response = new PageResponse<CompSysLogResponse>();
        try {
            PageResponse<SysLogResponse> logDataByFilterRule = sysLogService.getLogDataByFilterRule(uuid,pageNo,pageSize);
            response.setCount(logDataByFilterRule.getCount());
            response.setResult(jacksonBaseParse(CompSysLogResponse.class, logDataByFilterRule.getResult()));
        } catch (Exception e) {
            log.error("[Composite]>>>Get Log Data By Filter Rule Error is {}", e);
            throw new RuntimeException("[Composite]>>>Get Log Data By Filter Rule Error is {}", e);
        }
        return response;
    }

    @Override
    public String updateLogFilterRule(@RequestBody CompCreateLogFilterRuleReq request) {
        if (null == request) {
            log.error("[Composite]>>>Update Log Filter Rule request is null");
            throw new RuntimeException("[Composite]>>>Update Log Filter Rule Request is null");
        }
        log.info("[Composite]>>>Update Log Filter Rule Request is {}", request);
        try {
            sysLogService.updateLogFilterRule(jacksonBaseParse(CreateLogFilterRuleReq.class, request));
            return SUCCESS;
        } catch (Exception e) {
            log.error("[Composite]>>>Update Log Filter Rule Error is {}", e);
            throw new RuntimeException("[Composite]>>>Update Log Filter Rule Error is {}", e);
        }
    }

    @Override
    public CompCreateLogFilterRuleResp createLogAlertRule(@RequestBody CompCreateLogAlertRuleReq request) {
        if (null == request) {
            log.error("[Composite]>>>Create Log Alert Rule request is null");
            throw new RuntimeException("[Composite]>>>Create Log Alert Rule Request is null");
        }
        RequestAuthContext requestAuthContext = RequestAuthContext.currentRequestAuthContext();
        UserVO user = userClient.findByLdapId(requestAuthContext.getUser().getUsername());
        request.setCreator(user.getName());
        request.setUserName(requestAuthContext.getUser().getUsername());
        log.info("[Composite]>>>Create Log Alert Rule Request is {}", request);
        CompCreateLogFilterRuleResp response = null;
        try {
            response = jacksonBaseParse(CompCreateLogFilterRuleResp.class,
                    sysLogService.createLogAlertRule(jacksonBaseParse(CreateLogAlertRuleReq.class, request)));
            return response;
        } catch (Exception e) {
            log.error("[Composite]>>>Create Log Alert Rule Error is {}", e);
            throw new RuntimeException("[Composite]>>>Create Log Alert Rule Error is {}", e);
        }
    }

    @Override
    public CompLogAlertRuleDetail getLogAlertRuleDetail(@RequestParam("uuid") String uuid) {
        if (StringUtils.isBlank(uuid)) {
            log.error("[Composite]>>>Get Log Alert Rule Detail Uuid is null");
            throw new RuntimeException("[Composite]>>>Get Log Alert Rule Detail Uuid is null");
        }
        try {
            CompLogAlertRuleDetail response =
                    jacksonBaseParse(CompLogAlertRuleDetail.class, sysLogService.getLogAlertRuleDetail(uuid));
            log.info("[Composite]>>>Get Log Alert Rule Detail Response is {}", response);
            return response;
        } catch (Exception e) {
            log.error("[Composite]>>>Get Log Alert Rule Detail error is {}", e);
            throw new RuntimeException("[Composite]>>>Get Log Alert Rule Detail error is {}", e);
        }
    }

    @Override
    public PageResponse<CompLogAlertRuleDetail> getLogAlertRuleList(@RequestBody CompLogAlertRuleListReq request) {
        if (null == request) {
            log.error("[Composite]>>>Get Log Alert Rule List Request is null");
            throw new RuntimeException("[Composite]>>>Get Log Alert Rule List Request is null");
        }
        request.setBegin((request.getPageNo() - 1) * request.getPageSize());
        PageResponse<CompLogAlertRuleDetail> response = new PageResponse<CompLogAlertRuleDetail>();
        try {
            PageResponse<LogAlertRuleDetail> logAlertRuleList =
                    sysLogService.getLogAlertRuleList(jacksonBaseParse(LogAlertRuleListReq.class, request));
            response.setCount(logAlertRuleList.getCount());
            response.setResult(jacksonBaseParse(CompLogAlertRuleDetail.class,logAlertRuleList.getResult()));
            log.info("[Composite]>>>Get Log Filter Rule List Response is {}", response);
        } catch (Exception e) {
            log.error("[Composite]>>>Get Log Alert Rule List error is {}", e);
            throw new RuntimeException("[Composite]>>>Get Log Alert Rule List error is {}", e);
        }
        return response;
    }

    @Override
    public String deleteLogAlertRule(@RequestBody List<String> uuidList) {
        try {
            sysLogService.deleteLogAlertRule(uuidList);
            return SUCCESS;
        } catch (Exception e) {
            log.error("[Composite]>>>Delete Log Alert Rule Error is {}", e);
            throw new RuntimeException("[Composite]>>>Delete Log Alert Rule Error is {}", e);
        }
    }

    @Override
    public String updateLogAlertRule(@RequestBody CompCreateLogAlertRuleReq request) {
        if (null == request) {
            log.error("[Composite]>>>Update Log Alert Rule request is null");
            throw new RuntimeException("[Composite]>>>Update Log Alert Rule Request is null");
        }
        log.info("[Composite]>>>Update Log Alert Rule Request is {}", request);
        try {
            sysLogService.updateLogAlertRule(jacksonBaseParse(CreateLogAlertRuleReq.class, request));
            return SUCCESS;
        } catch (Exception e) {
            log.error("[Composite]>>>Update Log Alert Rule Error is {}", e);
            throw new RuntimeException("[Composite]>>>Update Log Alert Rule Error is {}", e);
        }
    }

    @Override
    public String openLogAlertRule(@RequestBody List<String> uuidList) {
        try {
            sysLogService.openLogAlertRule(uuidList);
            return SUCCESS;
        } catch (Exception e) {
            log.error("[Composite]>>>Open Log Alert Rule Error is {}", e);
            throw new RuntimeException("[Composite]>>>Open Log Alert Rule Error is {}", e);
        }
    }

    @Override
    public String closeLogAlertRule(@RequestBody List<String> uuidList) {
        try {
            sysLogService.closeLogAlertRule(uuidList);
            return SUCCESS;
        } catch (Exception e) {
            log.error("[Composite]>>>Close Log Alert Rule Error is {}", e);
            throw new RuntimeException("[Composite]>>>Close Log Alert Rule Error is {}", e);
        }
    }

    @Override
    public String checkName(@RequestParam("ruleName") String ruleName) {
        return sysLogService.checkName(ruleName);
    }
}
