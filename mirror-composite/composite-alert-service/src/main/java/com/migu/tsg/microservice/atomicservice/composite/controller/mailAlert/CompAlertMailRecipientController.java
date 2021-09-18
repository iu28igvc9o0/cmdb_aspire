package com.migu.tsg.microservice.atomicservice.composite.controller.mailAlert;

import com.aspire.mirror.alert.api.dto.mailAlert.AlertMailActionResp;
import com.aspire.mirror.alert.api.dto.mailAlert.AlertMailReceiverRequest;
import com.aspire.mirror.alert.api.dto.mailAlert.AlertMailReceiverResponse;
import com.aspire.mirror.common.entity.PageResponse;
import com.aspire.mirror.composite.payload.mailAlert.CompAlertMailActionResponse;
import com.aspire.mirror.composite.payload.mailAlert.CompAlertMailReceiverRequest;
import com.aspire.mirror.composite.payload.mailAlert.CompAlertMailReceiverResponse;
import com.aspire.mirror.composite.service.mailAlert.ICompAlertMailRecipientService;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.mailAlert.AlertMailRecipientServiceClient;
import org.apache.commons.collections.CollectionUtils;
import org.assertj.core.util.Lists;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CompAlertMailRecipientController implements ICompAlertMailRecipientService {

    @Autowired
    private AlertMailRecipientServiceClient recipientServiceClient;

    @Override
    public CompAlertMailActionResponse create(@RequestBody CompAlertMailReceiverRequest mailReceiverRequest) {
        AlertMailReceiverRequest receiverRequest = new AlertMailReceiverRequest();
        if (mailReceiverRequest != null) {
            BeanUtils.copyProperties(mailReceiverRequest, receiverRequest);
        }
        AlertMailActionResp result = recipientServiceClient.createAccount(receiverRequest);
        CompAlertMailActionResponse response = new CompAlertMailActionResponse();
        BeanUtils.copyProperties(result, response);
        return response;
    }

    @Override
    public CompAlertMailActionResponse modify(@RequestBody CompAlertMailReceiverRequest mailReceiverRequest) {
        AlertMailReceiverRequest receiverRequest = new AlertMailReceiverRequest();
        BeanUtils.copyProperties(mailReceiverRequest, receiverRequest);
        AlertMailActionResp result = recipientServiceClient.modifyAccount(receiverRequest);
        CompAlertMailActionResponse response = new CompAlertMailActionResponse();
        BeanUtils.copyProperties(result, response);
        return response;
    }

    @Override
    public CompAlertMailActionResponse delete(@PathVariable(value = "ids") String ids) {
        CompAlertMailActionResponse response = new CompAlertMailActionResponse();
        AlertMailActionResp result = recipientServiceClient.deleteAccount(ids);
        BeanUtils.copyProperties(result, response);
        return response;
    }

    @Override
    public CompAlertMailReceiverResponse get(@PathVariable(value = "id") String id) {
        CompAlertMailReceiverResponse response = new CompAlertMailReceiverResponse();
        AlertMailReceiverResponse result = recipientServiceClient.getAccount(id);
        if (result != null) {
            BeanUtils.copyProperties(result, response);
        }
        return response;
    }

    @Override
    public PageResponse<CompAlertMailReceiverResponse> select(@RequestBody CompAlertMailReceiverRequest mailReceiverRequest) {
        AlertMailReceiverRequest receiverRequest = new AlertMailReceiverRequest();
        BeanUtils.copyProperties(mailReceiverRequest, receiverRequest);
        PageResponse<CompAlertMailReceiverResponse> response = new PageResponse<>();
        PageResponse<AlertMailReceiverResponse> result = recipientServiceClient.selectAccount(receiverRequest);
        response.setCount(result.getCount());
        List<CompAlertMailReceiverResponse> list = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(result.getResult())) {
            for (AlertMailReceiverResponse receiverResponse : result.getResult()) {
                CompAlertMailReceiverResponse compAlertMailReceiverResponse = new CompAlertMailReceiverResponse();
                BeanUtils.copyProperties(receiverResponse, compAlertMailReceiverResponse);
                list.add(compAlertMailReceiverResponse);
            }
        }
        response.setResult(list);
        return response;
    }
}
