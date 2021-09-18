package com.aspire.mirror.alert.api.service.notify;

import com.aspire.mirror.alert.api.dto.notify.EmailNotifyParams;
import com.aspire.mirror.alert.api.dto.notify.SmsNotifyParams;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

/**
 * @BelongsProject: mirror-alert
 * @BelongsPackage: com.aspire.mirror.alert.api.v2.service
 * @Author: baiwenping
 * @CreateTime: 2020-03-05 15:16
 * @Description: ${Description}
 */
@RequestMapping("/v2/alerts/notify_template")
@Api(value = "通知模板")
public interface AlertNotifyTemplateService {
    /** 
    * 
    * @auther baiwenping
    * @Description 
    * @Date 15:22 2020/3/5
    * @Param [templateName, params]
    * @return java.util.Map<java.lang.String,java.lang.Object>
    **/
    @PostMapping("/sms/{template_name}")
    @ApiOperation(value = "根据模板发送短信", notes = "根据模板发送短信",
            response = Map.class, tags = {"AlertNotifyService API"})
    Map<String, String> sendSms (@PathVariable("template_name") String templateName,
                                 @RequestBody SmsNotifyParams smsNotifyParams);

    /** 
    * 
    * @auther baiwenping
    * @Description 
    * @Date 15:22 2020/3/5
    * @Param [templateName, params]
    * @return java.util.Map<java.lang.String,java.lang.Object>
    **/
    @PostMapping("/email/{template_name}")
    @ApiOperation(value = "根据模板发送邮件", notes = "根据模板发送邮件",
            response = Map.class, tags = {"AlertNotifyService API"})
    Map<String, String> sendEmail (@PathVariable("template_name") String templateName,
                                   @RequestBody EmailNotifyParams emailNotifyParams);
}
