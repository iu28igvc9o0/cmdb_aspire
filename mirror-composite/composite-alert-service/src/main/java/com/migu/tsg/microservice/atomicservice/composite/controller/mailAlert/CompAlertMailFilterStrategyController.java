package com.migu.tsg.microservice.atomicservice.composite.controller.mailAlert;

import com.aspire.mirror.alert.api.dto.mailAlert.*;
import com.aspire.mirror.common.entity.PageResponse;
import com.aspire.mirror.composite.payload.mailAlert.*;
import com.aspire.mirror.composite.service.mailAlert.ICompAlertMailFilterService;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.mailAlert.AlertMailFilterServiceClient;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.mailAlert.AlertsMailResolveServiceClient;
import org.assertj.core.util.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.util.List;

@RestController
public class CompAlertMailFilterStrategyController implements ICompAlertMailFilterService {
    private static final Logger logger = LoggerFactory.getLogger(CompAlertMailFilterStrategyController.class);
    @Autowired
    private AlertMailFilterServiceClient alertMailFilterServiceClient;
    @Autowired
    private AlertsMailResolveServiceClient alertsMailResolveServiceClient;

    @Override
    public CompAlertMailActionResponse create(@RequestBody CompAlertMailFilterRequest filterRequest) {
        CompAlertMailActionResponse response = new CompAlertMailActionResponse();
        AlertMailFilterRequest request = new AlertMailFilterRequest();
        if (filterRequest != null) {
            BeanUtils.copyProperties(filterRequest, request);
        }
        AlertMailActionResp actionResp = alertMailFilterServiceClient.saveFilterStrategy(request);
        BeanUtils.copyProperties(actionResp, response);
        return response;
    }

    @Override
    public CompAlertMailActionResponse modify(@RequestBody CompAlertMailFilterRequest filterRequest) {
        CompAlertMailActionResponse response = new CompAlertMailActionResponse();
        AlertMailFilterRequest request = new AlertMailFilterRequest();
        if (filterRequest != null) {
            BeanUtils.copyProperties(filterRequest, request);
        }
        AlertMailActionResp actionResp = alertMailFilterServiceClient.modifyAlertMailFilterStrategy(request);
        BeanUtils.copyProperties(actionResp, response);
        return response;
    }

    @Override
    public PageResponse<CompAlertMailFilterResponse> selectStrategies(
            @RequestBody CompAlertMailFilterQueryReq mailFilterQueryReq) throws ParseException {
        AlertMailFilterQueryReq req = new AlertMailFilterQueryReq();
        BeanUtils.copyProperties(mailFilterQueryReq, req);
        PageResponse<AlertMailFilterResponse> result = alertMailFilterServiceClient.selectFilterStrategies(req);
        PageResponse<CompAlertMailFilterResponse> response = new PageResponse<>();
        response.setCount(result.getCount());
        List<CompAlertMailFilterResponse> list = Lists.newArrayList();
        if (!CollectionUtils.isEmpty(result.getResult())) {
            for (AlertMailFilterResponse filterResponse : result.getResult()) {
                CompAlertMailFilterResponse compAlertMailFilterResponse = new CompAlertMailFilterResponse();
                BeanUtils.copyProperties(filterResponse, compAlertMailFilterResponse);
                list.add(compAlertMailFilterResponse);
            }
        }
        response.setResult(list);
        return response;
    }

    @Override
    public CompAlertMailFilterResponse selectById(@PathVariable("id") String id) {
        AlertMailFilterResponse filterResponse = alertMailFilterServiceClient.selectAlertMailFilterResponseById(id);
        CompAlertMailFilterResponse response = new CompAlertMailFilterResponse();
        if (filterResponse != null) {
            BeanUtils.copyProperties(filterResponse, response);
        }
        return response;
    }

    @Override
    public CompAlertMailActionResponse remove(@PathVariable("ids") String ids) {
        CompAlertMailActionResponse response = new CompAlertMailActionResponse();
        AlertMailActionResp actionResp  = alertMailFilterServiceClient.removeAlertMailFilterStrategy(ids);
        BeanUtils.copyProperties(actionResp, response);
        return response;
    }

    @Override
    public PageResponse<CompAlertMailResolveResponse> queryFilterRecords(@RequestBody CompAlertMailResolveRequest alertMailResolveRequest) throws ParseException {
        AlertMailResolveRequest resolveRequest = new AlertMailResolveRequest();
        BeanUtils.copyProperties(alertMailResolveRequest, resolveRequest);
        PageResponse<CompAlertMailResolveResponse> pageResponse = new PageResponse<>();
        PageResponse<AlertMailResolveResponse> result = alertsMailResolveServiceClient.query(resolveRequest);
        pageResponse.setCount(result.getCount());
        List<CompAlertMailResolveResponse> list = Lists.newArrayList();
        if (!CollectionUtils.isEmpty(result.getResult())) {
//            logger.info("------ resolve response: {}", JSON.toJSONString(result.getResult()));
            for (AlertMailResolveResponse alertMailResolveResponse : result.getResult()) {
                CompAlertMailResolveResponse resolveResponse = new CompAlertMailResolveResponse();
                BeanUtils.copyProperties(alertMailResolveResponse, resolveResponse);
                list.add(resolveResponse);
            }
        }
        pageResponse.setResult(list);
        return pageResponse;
    }
}
