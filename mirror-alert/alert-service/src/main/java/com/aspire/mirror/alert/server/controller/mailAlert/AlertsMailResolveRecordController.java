package com.aspire.mirror.alert.server.controller.mailAlert;

import com.aspire.mirror.alert.api.dto.mailAlert.AlertMailResolveRequest;
import com.aspire.mirror.alert.api.dto.mailAlert.AlertMailResolveResponse;
import com.aspire.mirror.alert.api.service.mailAlert.AlertsMailResolveService;
import com.aspire.mirror.alert.server.biz.mailAlert.AlertsMailStrategyBiz;
import com.aspire.mirror.alert.server.dao.mailAlert.po.AlertMailResolveRecord;
import com.aspire.mirror.common.entity.PageRequest;
import com.aspire.mirror.common.entity.PageResponse;
import com.aspire.mirror.common.util.FieldUtil;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

@RestController
public class AlertsMailResolveRecordController implements AlertsMailResolveService {
    @Autowired
    private AlertsMailStrategyBiz alertsMailStrategyService;
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    @Override
    public PageResponse<AlertMailResolveResponse> query(@RequestBody AlertMailResolveRequest resolveRequest) throws ParseException {
        if (StringUtils.isNotEmpty(resolveRequest.getGatherTimeRangeStart())) {
            resolveRequest.setGatherTimeStart(sdf.parse(resolveRequest.getGatherTimeRangeStart()));
        } else {
            resolveRequest.setGatherTimeStart(null);
        }
        if (StringUtils.isNotEmpty(resolveRequest.getGatherTimeRangeEnd())) {
            resolveRequest.setGatherTimeEnd(sdf.parse(resolveRequest.getGatherTimeRangeEnd()));
        } else {
            resolveRequest.setGatherTimeEnd(null);
        }
        if (StringUtils.isNotEmpty(resolveRequest.getSendTimeRangeStart())) {
            resolveRequest.setSendTimeStart(sdf.parse(resolveRequest.getSendTimeRangeStart()));
        } else {
            resolveRequest.setSendTimeStart(null);
        }
        if (StringUtils.isNotEmpty(resolveRequest.getSendTimeRangeEnd())) {
            resolveRequest.setSendTimeEnd(sdf.parse(resolveRequest.getSendTimeRangeEnd()));
        } else {
            resolveRequest.setSendTimeEnd(null);
        }
        PageRequest page = new PageRequest();
        BeanUtils.copyProperties(resolveRequest, page);
        Map<String, Object> map = FieldUtil.getFiledMap(resolveRequest);
        for (String key : map.keySet()) {
            page.addFields(key, map.get(key));
        }
        PageResponse<AlertMailResolveRecord> pageList = alertsMailStrategyService.selectResolveRecords(page);
        List<AlertMailResolveResponse> list = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(pageList.getResult())) {
            for (AlertMailResolveRecord record : pageList.getResult()) {
                AlertMailResolveResponse resolveRecord = new AlertMailResolveResponse();
                BeanUtils.copyProperties(record, resolveRecord);
                list.add(resolveRecord);
            }
        }
        PageResponse<AlertMailResolveResponse> response = new PageResponse<>();
        response.setCount(pageList.getCount());
        response.setResult(list);
        return response;
    }
}
