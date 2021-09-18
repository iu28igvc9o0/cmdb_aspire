package com.aspire.mirror.alert.server.controller.mailAlert;

import com.aspire.mirror.alert.api.dto.mailAlert.*;
import com.aspire.mirror.alert.api.service.mailAlert.AlertMailFilterService;
import com.aspire.mirror.alert.server.biz.mailAlert.AlertsMailFilterBiz;
import com.aspire.mirror.alert.server.biz.mailAlert.AlertsMailStrategyBiz;
import com.aspire.mirror.alert.server.dao.mailAlert.po.AlertMailFilter;
import com.aspire.mirror.alert.server.dao.mailAlert.po.AlertMailFilterStrategy;
import com.aspire.mirror.common.entity.PageRequest;
import com.aspire.mirror.common.entity.PageResponse;
import com.aspire.mirror.common.util.FieldUtil;
import com.google.common.collect.Lists;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
public class AlertMailFilterStrategyController implements AlertMailFilterService {
    @Autowired
    private AlertsMailFilterBiz alertsMailFilterService;
    @Autowired
    private AlertsMailStrategyBiz alertsMailStrategyService;

    private void persistStrategies(String filterId, List<AlertMailFilterStrategyRequest> strategyRequestList) {
        if (CollectionUtils.isEmpty(strategyRequestList)) {
            return;
        }
        List<AlertMailFilterStrategy> strategyList = Lists.newArrayList();
        for (AlertMailFilterStrategyRequest strategyRequest : strategyRequestList) {
            AlertMailFilterStrategy strategy = new AlertMailFilterStrategy();
            strategy.setFilterId(strategyRequest.getFilterId());
            strategy.setAlertField(strategyRequest.getAlertField());
            strategy.setUseReg(strategyRequest.getUseReg());
            strategy.setMailField(strategyRequest.getMailField());
            if (strategyRequest.getMailField() == -1) { // 自定义
                strategy.setFieldMatchValue(strategyRequest.getFieldMatchValue());
            } else { // 邮件
                if (strategyRequest.getUseReg() == 0) { // 不使用正则
                    String fieldMatchValue = strategyRequest.getAlertField().equals("alert_level") ?
                            strategyRequest.getFieldMatchValue() : strategyRequest.getFieldMatchReg();
                    strategy.setFieldMatchValue(fieldMatchValue);
                } else {
                    if (StringUtils.isEmpty(strategyRequest.getFieldMatchTarget())) {
                        strategy.setFieldMatchReg(strategyRequest.getFieldMatchReg());
                    } else {
                        strategy.setFieldMatchValue(strategyRequest.getFieldMatchValue());
                    }
                }
            }
            strategy.setFieldMatchTarget(strategyRequest.getFieldMatchTarget());
            strategy.setFilterId(filterId);
            strategyList.add(strategy);
        }
        alertsMailStrategyService.removeStrategiesByFilterIds(Arrays.asList(new String[]{filterId}));
        alertsMailStrategyService.batchInsertStrategies(strategyList);
    }

    @Override
    public AlertMailActionResp saveFilterStrategy(@RequestBody AlertMailFilterRequest filterRequest) {
        AlertMailActionResp resp = new AlertMailActionResp();
        AlertMailFilter mailFilter = new AlertMailFilter();
        BeanUtils.copyProperties(filterRequest, mailFilter);
        String name = mailFilter.getName();
        if(StringUtils.isNotBlank(name)) {
        	Integer recipientId= mailFilter.getRecipientId();
        	AlertMailFilter v = alertsMailFilterService.selectMailFilterByRecipientId(recipientId+"", name,null);
        	if(null!=v) {
        		resp.setStatus(AlertMailActionResp.failed);
        		resp.setMessage("名称已存在！");
        		return resp;
        	}
        }
        String filterId = UUID.randomUUID().toString();
        mailFilter.setId(filterId);
        alertsMailFilterService.insertAlertsMailFilter(mailFilter);
        persistStrategies(filterId, filterRequest.getStrategyRequestList());
        resp.setStatus(AlertMailActionResp.success);
        return resp;
    }

//    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public PageResponse<AlertMailFilterResponse> selectFilterStrategies(@RequestBody AlertMailFilterQueryReq queryReq)
            throws ParseException {
        PageRequest page = new PageRequest();
        BeanUtils.copyProperties(queryReq, page);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if (StringUtils.isNotEmpty(queryReq.getSendTimeRangeStart())) {
            queryReq.setSendStartTime(sdf.parse(queryReq.getSendTimeRangeStart()));
        } else {
            queryReq.setSendStartTime(null);
        }
        if (StringUtils.isNotEmpty(queryReq.getSendTimeRangeEnd())) {
            queryReq.setSendEndTime(sdf.parse(queryReq.getSendTimeRangeEnd()));
        } else {
            queryReq.setSendEndTime(null);
        }
        Map<String, Object> map = FieldUtil.getFiledMap(queryReq);
        for (String key : map.keySet()) {
            page.addFields(key, map.get(key));
        }
        PageResponse<AlertMailFilter> pageList = alertsMailFilterService.select(page);
        List<AlertMailFilterResponse> resultList = Lists.newArrayList();
        if (!CollectionUtils.isEmpty(pageList.getResult())) {
            for (AlertMailFilter alertMailFilter : pageList.getResult()) {
                AlertMailFilterResponse alertMailFilterResponse = new AlertMailFilterResponse();
                BeanUtils.copyProperties(alertMailFilter, alertMailFilterResponse);
                resultList.add(alertMailFilterResponse);
            }
        }
        PageResponse<AlertMailFilterResponse> response = new PageResponse<>();
        response.setCount(pageList.getCount());
        response.setResult(resultList);
        return response;
    }

    @Override
    public AlertMailFilterResponse selectAlertMailFilterResponseById(@PathVariable("id") String id) {
        AlertMailFilter alertMailFilter = alertsMailFilterService.selectById(id);
        if (alertMailFilter == null) {
            return null;
        }
        List<AlertMailFilterStrategy> strategyList = alertsMailStrategyService.selectStrategiesByFilterId(id);
        AlertMailFilterResponse response = new AlertMailFilterResponse();
        BeanUtils.copyProperties(alertMailFilter, response);
        List<AlertMailFilterStrategyResponse> strategyResponseList = Lists.newArrayList();
        if (!CollectionUtils.isEmpty(strategyList)) {
            for (AlertMailFilterStrategy alertMailFilterStrategy : strategyList) {
                AlertMailFilterStrategyResponse strategyResponse = new AlertMailFilterStrategyResponse();
                BeanUtils.copyProperties(alertMailFilterStrategy, strategyResponse);
                strategyResponseList.add(strategyResponse);
            }
        }
        response.setStrategyRequestList(strategyResponseList);
        return response;
    }

    @Override
    public AlertMailActionResp modifyAlertMailFilterStrategy(@RequestBody AlertMailFilterRequest filterRequest) {
        AlertMailActionResp resp = new AlertMailActionResp();
        AlertMailFilter alertMailFilter = new AlertMailFilter();
        BeanUtils.copyProperties(filterRequest, alertMailFilter);
        String name = alertMailFilter.getName();
        if(StringUtils.isNotBlank(name)) {
        	Integer recipientId=alertMailFilter.getRecipientId();
        	String id =  alertMailFilter.getId();
        	AlertMailFilter v = alertsMailFilterService.selectMailFilterByRecipientId(recipientId+"", name,id);
        	if(null!=v) {
        		resp.setStatus(AlertMailActionResp.failed);
        		resp.setMessage("名称已存在！");
        		return resp;
        	}
        }
        
        alertsMailFilterService.updateAlertsMailFilter(alertMailFilter);
        String filterId = filterRequest.getId();
        persistStrategies(filterId, filterRequest.getStrategyRequestList());
        resp.setStatus(AlertMailActionResp.success);
        return resp;
    }

    @Override
    public AlertMailActionResp removeAlertMailFilterStrategy(@PathVariable("ids") String ids) {
        AlertMailActionResp resp = new AlertMailActionResp();
        if (StringUtils.isEmpty(ids)) {
            resp.setMessage("采集策略 ID 为空!");
            resp.setStatus(AlertMailActionResp.failed);
            return resp;
        }
        List<String> filterIdList = Arrays.asList(ids.split(","));
        // 删除 resolve_filter 记录
        alertsMailFilterService.removeAlertsMailFilter(filterIdList);
        // 删除 filter_strategy 记录
        alertsMailStrategyService.removeStrategiesByFilterIds(filterIdList);
        resp.setStatus(AlertMailActionResp.success);
        return resp;
    }
}
