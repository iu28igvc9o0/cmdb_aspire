package com.aspire.mirror.alert.server.controller.mailAlert;

import com.alibaba.fastjson.JSONObject;
import com.aspire.mirror.alert.api.dto.mailAlert.AlertMailActionResp;
import com.aspire.mirror.alert.api.dto.mailAlert.AlertMailReceiverRequest;
import com.aspire.mirror.alert.api.dto.mailAlert.AlertMailReceiverResponse;
import com.aspire.mirror.alert.api.service.mailAlert.AlertMailRecipientService;
import com.aspire.mirror.alert.server.biz.mailAlert.AlertsMailResolveBiz;
import com.aspire.mirror.alert.server.dao.mailAlert.po.AlertMailRecipient;
import com.aspire.mirror.common.entity.PageRequest;
import com.aspire.mirror.common.entity.PageResponse;
import com.aspire.mirror.common.util.FieldUtil;
import com.google.common.collect.Lists;
import org.apache.axis.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class AlertMailRecipientController implements AlertMailRecipientService {

    private static final Logger logger = LoggerFactory.getLogger(AlertMailRecipientController.class);

    @Autowired
    private AlertsMailResolveBiz alertsMailResolveBiz;

    @Override
    public AlertMailActionResp createAccount(@RequestBody AlertMailReceiverRequest mailReceiverRequest) {
        logger.info("### ======> recipient: {}", JSONObject.toJSONString(mailReceiverRequest));
        AlertMailActionResp resp = new AlertMailActionResp();
        List<AlertMailRecipient> alertMailRecipientInDB = alertsMailResolveBiz.selectRecipientByAccount(mailReceiverRequest.getReceiver());
        if (!CollectionUtils.isEmpty(alertMailRecipientInDB)) {
            resp.setStatus(AlertMailActionResp.failed);
            resp.setMessage("已经存在该收件地址的记录!");
        }
        AlertMailRecipient alertMailRecipient = new AlertMailRecipient();
        BeanUtils.copyProperties(mailReceiverRequest, alertMailRecipient);
        alertsMailResolveBiz.insertRecipient(alertMailRecipient);
        resp.setStatus(AlertMailActionResp.success);
        return resp;
    }

    @Override
    public AlertMailActionResp modifyAccount(@RequestBody AlertMailReceiverRequest mailReceiverRequest) {
        AlertMailActionResp resp = new AlertMailActionResp();
        if (mailReceiverRequest.getId() == null) {
            resp.setStatus(AlertMailActionResp.failed);
            resp.setMessage("账号ID为空!");
            return resp;
        }
        AlertMailRecipient oldAlertMailrecipientInDB = alertsMailResolveBiz.selectRecipientById(mailReceiverRequest.getId());
        String oldReceiver = oldAlertMailrecipientInDB.getReceiver();
        String newReceiver = mailReceiverRequest.getReceiver();
        List<AlertMailRecipient> alertMailRecipientInDB = alertsMailResolveBiz.selectRecipientByAccount(mailReceiverRequest.getReceiver());
        if (CollectionUtils.isEmpty(alertMailRecipientInDB)) { // 更改了收件邮箱
            resp = modifyAccountWithReceiverRequest(mailReceiverRequest);
        } else if (alertMailRecipientInDB.size() == 1) {
            if (newReceiver.equals(oldReceiver)) { // 未更改收件邮箱
                resp = modifyAccountWithReceiverRequest(mailReceiverRequest);
            } else {
                resp.setStatus(AlertMailActionResp.failed);
                resp.setMessage("已存在一条邮箱为 " + newReceiver + " 的记录!");
            }
        } else {
            resp.setStatus(AlertMailActionResp.failed);
            resp.setMessage("已存在多条邮箱为 " + newReceiver + " 的记录!");
        }
        return resp;
    }

    private AlertMailActionResp modifyAccountWithReceiverRequest(AlertMailReceiverRequest mailReceiverRequest) {
        AlertMailActionResp resp = new AlertMailActionResp();
        AlertMailRecipient alertMailRecipient = new AlertMailRecipient();
        BeanUtils.copyProperties(mailReceiverRequest, alertMailRecipient);
        alertsMailResolveBiz.updateRecipientById(alertMailRecipient);
        resp.setStatus(AlertMailActionResp.success);
        return resp;
    }

    @Override
    public AlertMailActionResp deleteAccount(@PathVariable(value = "ids") String ids) {
        AlertMailActionResp resp = new AlertMailActionResp();
        if (StringUtils.isEmpty(ids)) {
            resp.setMessage("ID 为空!");
            resp.setStatus(AlertMailActionResp.failed);
            return resp;
        }
        String[] idArray = ids.split(",");
        for (String id : idArray) {
            Integer recipientId = Integer.parseInt(id);
            alertsMailResolveBiz.removeRecipientById(recipientId);
        }
        resp.setStatus(AlertMailActionResp.success);
        return resp;
    }

    @Override
    public AlertMailReceiverResponse getAccount(@PathVariable(value = "id") String id) {
        Integer recipientId = StringUtils.isEmpty(id) ? null : Integer.parseInt(id);
        if (recipientId == null) {
            return null;
        }
        AlertMailRecipient alertMailRecipient = alertsMailResolveBiz.selectRecipientById(recipientId);
        if (alertMailRecipient == null) {
            return null;
        }
        AlertMailReceiverResponse alertMailReceiverResponse = new AlertMailReceiverResponse();
        BeanUtils.copyProperties(alertMailRecipient, alertMailReceiverResponse);
        return alertMailReceiverResponse;
    }

    @Override
    public PageResponse<AlertMailReceiverResponse> selectAccount(@RequestBody AlertMailReceiverRequest mailReceiverRequest) {
        PageRequest page = new PageRequest();
        BeanUtils.copyProperties(mailReceiverRequest, page);
        Map<String, Object> map = FieldUtil.getFiledMap(mailReceiverRequest);
        for (String key : map.keySet()) {
            page.addFields(key, map.get(key));
        }
        PageResponse<AlertMailRecipient> pageResponse = alertsMailResolveBiz.selectRecipients(page);
        List<AlertMailReceiverResponse> results = Lists.newArrayList();
        if (!CollectionUtils.isEmpty(pageResponse.getResult())) {
            for (AlertMailRecipient alertMailRecipient : pageResponse.getResult()) {
                AlertMailReceiverResponse alertMailReceiverResponse = new AlertMailReceiverResponse();
                BeanUtils.copyProperties(alertMailRecipient, alertMailReceiverResponse);
                results.add(alertMailReceiverResponse);
            }
        }
        PageResponse<AlertMailReceiverResponse> response = new PageResponse<>();
        response.setCount(pageResponse.getCount());
        response.setResult(results);
        return response;
    }
}
