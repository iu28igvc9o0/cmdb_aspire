package com.aspire.mirror.alert.server.biz.notify.impl;

import com.aspire.mirror.alert.server.biz.helper.EmailSendHelper;
import com.aspire.mirror.alert.server.biz.helper.SmSendHelper;
import com.aspire.mirror.alert.server.biz.notify.IAlertNotifyTemplateBiz;
import com.aspire.mirror.alert.server.constant.AlertConfigConstants;
import com.aspire.mirror.alert.server.dao.notify.AlertNotifyTemplateMapper;
import com.aspire.mirror.alert.server.dao.notify.po.AlertNotifyTemplate;
import com.aspire.mirror.common.util.DateUtil;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * @BelongsProject: mirror-alert
 * @BelongsPackage: com.aspire.mirror.alert.server.v2.biz.impl
 * @Author: baiwenping
 * @CreateTime: 2020-03-05 14:58
 * @Description: ${Description}
 */
@Service
@Slf4j
public class AlertNotifyTemplateBizImpl implements IAlertNotifyTemplateBiz {
    @Autowired
    private AlertNotifyTemplateMapper alertNotifyTemplateMapper;
    @Autowired
    private SmSendHelper smSendHelper;
    @Autowired
    private EmailSendHelper emailSendHelper;

    /**
     * 根据名称查询通知模板
     *
     * @param templateName
     * @return
     */
    @Override
    public List<AlertNotifyTemplate> selectByName(String templateName) {
        return alertNotifyTemplateMapper.selectByName(templateName);
    }

    /**
     * 新增模板
     *
     * @param alertNotifyTemplate
     */
    @Override
    public void insert(AlertNotifyTemplate alertNotifyTemplate) {
        alertNotifyTemplate.setId(UUID.randomUUID().toString());
        alertNotifyTemplateMapper.insert(alertNotifyTemplate);
    }

    /**
     * 修改模板
     *
     * @param alertNotifyTemplate
     */
    @Override
    public void update(AlertNotifyTemplate alertNotifyTemplate) {
        alertNotifyTemplateMapper.update(alertNotifyTemplate);
    }

    /**
     * 删除模板
     *
     * @param templateName
     */
    @Override
    public void deleteByName(String templateName) {
        alertNotifyTemplateMapper.deleteByName(templateName);
    }

    /**
     * 根据模板发送短信
     *
     * @param templateName
     * @param mobiles
     * @param params
     * @return java.util.Map<java.lang.String,java.lang.Object>
     * @auther baiwenping
     * @Description
     * @Date 17:53 2020/3/5
     * @Param [templateName, mobiles, params]
     */
    @Override
    public Map<String, String> sendSms(String templateName, List<String> mobiles, Map<String, Object> params) {
        Map<String, String> map = getTemplate(AlertConfigConstants.MESSAGE_TYPE_SMS, templateName, params, null);
        if (!StringUtils.isEmpty(map.get("code"))) {
            return map;
        }
        String smsTemplate = map.get("content");
        boolean flag = smSendHelper.send(mobiles, smsTemplate);
        if (flag) {
            map.put("code", "0000");
            map.put("desc", "success");
        } else {
            map.put("code", "0003");
            map.put("desc", "send sms failed!");
        }
        return map;
    }

    /**
     * 根据模板发送邮件
     *
     * @param templateName
     * @param subject
     * @param receivers
     * @param params
     * @return java.util.Map<java.lang.String ,java.lang.Object>
     * @auther baiwenping
     * @Description
     * @Date 17:53 2020/3/5
     * @Param [templateName, subject, receivers, params]
     */
    @Override
    public Map<String, String> sendEmail(String templateName, String subject, Boolean isHtml, List<String> receivers, Map<String, Object> params, List<Map<String, Object>> mergeList) {
        Map<String, String> map = getTemplate(AlertConfigConstants.MESSAGE_TYPE_EMAIL, templateName, params, mergeList);
        if (!StringUtils.isEmpty(map.get("code"))) {
            return map;
        }
        //组装邮件的主题，因为告警有多个，所以只取第一个告警内容和数据库的模板向融合
        StringBuilder sb = new StringBuilder();
        List<AlertNotifyTemplate> alertNotifyTemplates = alertNotifyTemplateMapper.selectByName(templateName);
        if (CollectionUtils.isEmpty(alertNotifyTemplates)) {
            log.error("temlate name is {}, message template not exsit!", templateName);
            map.put("code", "0001");
            map.put("desc", "message template not exsit!");
            return map;
        }
        AlertNotifyTemplate alertNotifyTemplate = alertNotifyTemplates.get(0);
        String subject1 = alertNotifyTemplate.getSubject();
        if (!CollectionUtils.isEmpty(mergeList)) {
//            for (int i = 0; i < mergeList.size(); i++) {
                Map<String, Object> stringObjectMap = mergeList.get(0);
                String tempTmeplate1 = subject1;
                for (Map.Entry<String, Object> mapz : stringObjectMap.entrySet()) {
                    String key = mapz.getKey();
                    Object value = mapz.getValue();
                    if (value == null) {
                        continue;
                    }
                    if (value instanceof String) {
                        tempTmeplate1 = tempTmeplate1.replaceAll("@\\{" + key + "\\}", (String) value);
                    } else if (value instanceof Integer || value instanceof Double || value instanceof Float
                            || value instanceof Long || value instanceof Boolean) {
                        tempTmeplate1 = tempTmeplate1.replaceAll("@\\{" + key + "\\}", value + "");
                    } else if (value instanceof Date) {
                        tempTmeplate1 = tempTmeplate1.replaceAll("@\\{" + key + "\\}", DateUtil.format((Date) value, "yyyy-MM-dd HH:mm:ss"));
                    }
                }
                sb.append(tempTmeplate1);
                log.info("subject的值" + sb.toString());
            }
//        }
        String smsTemplate = map.get("content");
        try {
            emailSendHelper.sendMail(sb.toString().replaceAll("@\\{[^\\}.]*\\}", ""), smsTemplate, isHtml, receivers.toArray(new String[0]));
        } catch (Exception e) {
            log.error("send email failed ", e);
            map.put("code", "0003");
            map.put("desc", "send sms failed!");
            return map;
        }
        map.put("code", "0000");
        map.put("desc", "success");
        return map;
    }

    /**
     * 根据模板获取消息体
     *
     * @param type
     * @param templateName
     * @param params
     * @return
     */
    private Map<String, String> getTemplate(String type, String templateName, Map<String, Object> params, List<Map<String, Object>> mergeList) {
        Map<String, String> map = Maps.newHashMap();
        String templateMessage = "";
        List<AlertNotifyTemplate> alertNotifyTemplates = alertNotifyTemplateMapper.selectByName(templateName);
        // 判断消息模板是否为空
        if (CollectionUtils.isEmpty(alertNotifyTemplates)) {
            log.error("temlate name is {}, message template not exsit!", templateName);
            map.put("code", "0001");
            map.put("desc", "message template not exsit!");
            return map;
        }
        // 根据消息类型获取消息体
        AlertNotifyTemplate alertNotifyTemplate = alertNotifyTemplates.get(0);
        if (AlertConfigConstants.MESSAGE_TYPE_EMAIL.equals(type)) {
            templateMessage = alertNotifyTemplate.getEmailTemplate();
            //填充合并发送的数据
            if (AlertConfigConstants.YES.equals(alertNotifyTemplate.getIsEmailMerge())) {
                String emailMergeTemplate = alertNotifyTemplate.getEmailMergeTemplate();
                String subject = alertNotifyTemplate.getSubject();
                if (templateMessage.indexOf("@{collectionPosition}") > -1) {
                    templateMessage = templateMessage.replaceFirst("@\\{collectionPosition\\}", getMergeMessages(emailMergeTemplate, mergeList, subject));
                } else {
                    templateMessage += getMergeMessages(emailMergeTemplate, mergeList, subject);
                }
            }
        } else if (AlertConfigConstants.MESSAGE_TYPE_SMS.equals(type)) {
            templateMessage = alertNotifyTemplate.getSmsTemplate();
        }
        //
        if (StringUtils.isEmpty(templateMessage)) {
            log.error("temlate name is {},message type is {}, message template is null!", templateName, type);
            map.put("code", "0002");
            map.put("desc", type + " template is null!");
            return map;
        }

        //根据参数填充内容
        if (params != null) {
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();
                if (value == null) {
                    continue;
                }
                if (value instanceof String) {
                    Locale.setDefault(Locale.CHINA);
                    templateMessage = templateMessage.replaceAll("@\\{" + key + "\\}", (String) value);
                } else if ((key.toLowerCase().endsWith("time") || key.toLowerCase().endsWith("date")) &&
                        (value instanceof Integer || value instanceof Long)) {
                    templateMessage = templateMessage.replaceAll("@\\{" + key + "\\}", DateUtil.format(new Date((Long) value), "yyyy-MM-dd HH:mm:ss"));
                } else if (value instanceof Integer || value instanceof Double || value instanceof Float
                        || value instanceof Long || value instanceof Boolean) {
                    templateMessage = templateMessage.replaceAll("@\\{" + key + "\\}", value + "");
                } else if (value instanceof Date) {
                    templateMessage = templateMessage.replaceAll("@\\{" + key + "\\}", DateUtil.format((Date) value, "yyyy-MM-dd HH:mm:ss"));
                }

            }

        }
        templateMessage = templateMessage.replaceAll("@\\{[^\\}.]*\\}", "");

        map.put("content", templateMessage);
        return map;
    }

    /**
     * 获取合并的数据部分
     *
     * @return java.lang.String
     * @auther baiwenping
     * @Description
     * @Date 14:49 2020/3/6
     * @Param [mergeTemplate, mergeList]
     **/
    private String getMergeMessages(String mergeTemplate, List<Map<String, Object>> mergeList, String subject) {
        StringBuilder sb = new StringBuilder();
        //sb.append("您好：告警通知信息如下，请尽快处理：").append(System.getProperty("line.separator"));
        if (StringUtils.isEmpty(mergeTemplate)) {
            return sb.toString();
        }
        if (!CollectionUtils.isEmpty(mergeList)) {
            for (Map<String, Object> merge : mergeList) {
                String tempTmeplate = mergeTemplate;
                for (Map.Entry<String, Object> entry : merge.entrySet()) {
                    String key = entry.getKey();
                    Object value = entry.getValue();
                    if (value == null) {
                        continue;
                    }
                    if (value instanceof String) {
                        tempTmeplate = tempTmeplate.replaceAll("@\\{" + key + "\\}", (String) value);
                    } else if (value instanceof Integer || value instanceof Double || value instanceof Float
                            || value instanceof Long || value instanceof Boolean) {
                        tempTmeplate = tempTmeplate.replaceAll("@\\{" + key + "\\}", value + "");
                    } else if (value instanceof Date) {
                        tempTmeplate = tempTmeplate.replaceAll("@\\{" + key + "\\}", DateUtil.format((Date) value, "yyyy-MM-dd HH:mm:ss"));
                    }

                }
                sb.append(tempTmeplate);
            }
        }
        return sb.toString();
    }

}
