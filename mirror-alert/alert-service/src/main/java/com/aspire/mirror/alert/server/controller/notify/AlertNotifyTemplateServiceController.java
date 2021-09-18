package com.aspire.mirror.alert.server.controller.notify;

import com.aspire.mirror.alert.api.dto.notify.EmailNotifyParams;
import com.aspire.mirror.alert.api.dto.notify.SmsNotifyParams;
import com.aspire.mirror.alert.api.service.notify.AlertNotifyTemplateService;
import com.aspire.mirror.alert.server.biz.notify.IAlertNotifyTemplateBiz;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @BelongsProject: mirror-alert
 * @BelongsPackage: com.aspire.mirror.alert.server.v2.controller
 * @Author: baiwenping
 * @CreateTime: 2020-03-05 15:25
 * @Description: ${Description}
 */
@Slf4j
@RestController
public class AlertNotifyTemplateServiceController implements AlertNotifyTemplateService {
    @Autowired
    private IAlertNotifyTemplateBiz alertNotifyTemplateBiz;

    /**
     * @param templateName
     * @param smsNotifyParams
     * @return java.util.Map<java.lang.String,java.lang.Object>
     * @auther baiwenping
     * @Description
     * @Date 15:22 2020/3/5
     * @Param [templateName, params]
     */
    @Override
    public Map<String, String> sendSms(@PathVariable("template_name") String templateName,
                                       @RequestBody SmsNotifyParams smsNotifyParams) {

        return alertNotifyTemplateBiz.sendSms(templateName, smsNotifyParams.getMobiles(),
                smsNotifyParams.getParams());
    }

    /**
     * @param templateName
     * @param emailNotifyParams
     * @return java.util.Map<java.lang.String,java.lang.Object>
     * @auther baiwenping
     * @Description
     * @Date 15:22 2020/3/5
     * @Param [templateName, params]
     */
    @Override
    public Map<String, String> sendEmail(@PathVariable("template_name") String templateName,
                                         @RequestBody EmailNotifyParams emailNotifyParams) {

        return alertNotifyTemplateBiz.sendEmail(templateName, emailNotifyParams.getSubject(),false,
                emailNotifyParams.getReceivers(), emailNotifyParams.getParams(), emailNotifyParams.getMergeList());
    }
}
