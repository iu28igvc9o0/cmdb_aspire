package com.aspire.mirror.alert.server.controller.bpm;

import com.aspire.mirror.alert.api.dto.bpm.AlertAutoOrderConfigDetail;
import com.aspire.mirror.alert.api.dto.bpm.AlertAutoOrderConfigReq;
import com.aspire.mirror.alert.api.dto.bpm.AlertAutoOrderLog;
import com.aspire.mirror.alert.api.dto.bpm.AlertAutoOrderLogReq;
import com.aspire.mirror.alert.api.service.bpm.AlertAutoOrderConfigService;
import com.aspire.mirror.alert.server.biz.bpm.AlertsAutoOrderConfigBiz;
import com.aspire.mirror.alert.server.vo.bpm.AlertAutoOrderConfigDetailVo;
import com.aspire.mirror.alert.server.vo.bpm.AlertAutoOrderConfigVo;
import com.aspire.mirror.alert.server.vo.bpm.AlertAutoOrderLogVo;
import com.aspire.mirror.alert.server.vo.bpm.AlertAutoOrderLogReqVo;
import com.aspire.mirror.alert.server.util.PayloadParseUtil;
import com.aspire.mirror.common.entity.PageResponse;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@Slf4j
public class AlertsAutoOrderConfigController implements AlertAutoOrderConfigService {

    @Autowired
    private AlertsAutoOrderConfigBiz biz;

    @Override
    public PageResponse<AlertAutoOrderConfigDetail> getAlertAutoOrderConfigList(@RequestParam(value = "configName", required = false) String configName,
                                                                                @RequestParam(value = "isOpen", required = false) String isOpen,
                                                                                @RequestParam(value = "startTime", required = false) String startTime,
                                                                                @RequestParam(value = "endTime", required = false) String endTime,
                                                                                @RequestParam(value = "orderType", required = false) String orderType,
                                                                                @RequestParam(value = "orderTimeInterval", required = false) String orderTimeInterval,
                                                                                @RequestParam(value = "pageNum", required = false) Integer pageNum,
                                                                                @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        PageResponse<AlertAutoOrderConfigDetail> response = new PageResponse<AlertAutoOrderConfigDetail>();
        PageResponse<AlertAutoOrderConfigDetailVo> alertAutoOrderConfigList =
                biz.getAlertAutoOrderConfigList(configName, isOpen, startTime, endTime, orderType, orderTimeInterval,pageNum, pageSize);
        response.setCount(alertAutoOrderConfigList.getCount());
        response.setResult(PayloadParseUtil.jacksonBaseParse(AlertAutoOrderConfigDetail.class, alertAutoOrderConfigList.getResult()));
        return response;
    }

    @Override
    public void createAlertAutoOrderConfig(@RequestBody AlertAutoOrderConfigReq request) {
        log.info("[AlertAutoOrderConfig] >>> Insert Alert Auto Order Config Request is {}", request);
        biz.createAlertAutoOrderConfig(PayloadParseUtil.jacksonBaseParse(AlertAutoOrderConfigVo.class,request));
    }

    @Override
    public Map<String, String> checkName(@RequestParam(value = "configName") String configName) {
        Map<String, String> map = Maps.newHashMap();
        map.put("check", biz.checkName(configName));
        return map;
    }

    @Override
    public void updateAlertAutoOrderConfig(@RequestBody AlertAutoOrderConfigReq request) {
        log.info("[AlertAutoOrderConfig] >>> Update Alert Auto Order Config Request is {}", request);
        biz.updateAlertAutoOrderConfig(PayloadParseUtil.jacksonBaseParse(AlertAutoOrderConfigVo.class,request));
    }

    @Override
    public AlertAutoOrderConfigDetail getAlertAutoOrderConfigDetail(@RequestParam("uuid") String uuid) {
        return PayloadParseUtil.jacksonBaseParse(AlertAutoOrderConfigDetail.class,
                biz.getAlertAutoOrderConfigDetail(uuid));
    }

    @Override
    public void deleteAlertAutoOrderConfig(@RequestBody List<String> uuidList) {
        biz.deleteAlertAutoOrderConfig(uuidList);
    }

    @Override
    public void updateAlertAutoOrderConfigStatus(@RequestBody List<String> uuidList,
                                                 @RequestParam("configStatus") String configStatus) {
        biz.updateAlertAutoOrderConfigStatus(uuidList,configStatus);
    }

    @Override
    public void copyAlertAutoOrderConfig(@RequestParam("uuid") String uuid,
                                         @RequestParam("userName") String userName) {
        biz.copyAlertAutoOrderConfig(uuid,userName);
    }

    @Override
    public PageResponse<AlertAutoOrderLog> getAlertAutoOrderLogList(@RequestBody AlertAutoOrderLogReq request) {
        PageResponse<AlertAutoOrderLog> response = new PageResponse<AlertAutoOrderLog>();
        PageResponse<AlertAutoOrderLogVo> alertAutoOrderLogList =
                biz.getAlertAutoOrderLogList(PayloadParseUtil.jacksonBaseParse(AlertAutoOrderLogReqVo.class, request));
        response.setCount(alertAutoOrderLogList.getCount());
        response.setResult(PayloadParseUtil.jacksonBaseParse(AlertAutoOrderLog.class,alertAutoOrderLogList.getResult()));
        return response;
    }

    @Override
    public List<Map<String, Object>> exportAlertAutoOrderLogList(@RequestBody AlertAutoOrderLogReq request) {
        return biz.exportAlertAutoOrderLogList(PayloadParseUtil.jacksonBaseParse(AlertAutoOrderLogReqVo.class,request));
    }

    @Override
    public ResponseEntity<String> alertAutoOrderSchedule() {
       return biz.alertAutoOrderSchedule();
    }
}
