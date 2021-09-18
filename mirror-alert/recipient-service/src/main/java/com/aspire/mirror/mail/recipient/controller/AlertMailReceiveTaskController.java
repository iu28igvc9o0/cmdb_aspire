package com.aspire.mirror.mail.recipient.controller;

import com.aspire.mirror.mail.recipient.service.AlertMailReceiverService;
import com.aspire.mirror.mail.recipient.service.AlertMailRecipientService;
import com.aspire.mirror.mail.recipient.vo.AlertMailRecipientTaskResponse;
import com.aspire.mirror.mail.recipient.vo.AlertMailRecipientTaskRule;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;

@RestController
@RequestMapping(value = "/v1/alerts/recipient")
public class AlertMailReceiveTaskController {

    @Autowired
    private AlertMailRecipientService recipientService;
    @Autowired
    private AlertMailReceiverService receiverService;

    @PostMapping(value = "/task")
    public AlertMailRecipientTaskResponse create(@RequestBody AlertMailRecipientTaskRule taskRule) {
        recipientService.addTriggerTask(taskRule);
        receiverService.insert(taskRule);
        return AlertMailRecipientTaskResponse.success();
    }

    @PutMapping(value = "/task")
    public AlertMailRecipientTaskResponse modify(@RequestBody AlertMailRecipientTaskRule taskRule) {
        recipientService.resetTriggerTask(taskRule);
        receiverService.update(taskRule);
        return AlertMailRecipientTaskResponse.success();
    }

    @DeleteMapping(value = "/task/{id}")
    public AlertMailRecipientTaskResponse remove(@PathVariable(value = "id") Integer id) {
        recipientService.cancelTriggerTask(id);
        receiverService.delete(id);
        return AlertMailRecipientTaskResponse.success();
    }
}
