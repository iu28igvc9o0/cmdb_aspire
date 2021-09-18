package com.aspire.mirror.alert.server.biz.notify;

import com.aspire.mirror.alert.server.dao.notify.po.AlertNotifyTemplate;

import java.util.List;
import java.util.Map;

/**
 * @BelongsProject: mirror-alert
 * @BelongsPackage: com.aspire.mirror.alert.server.v2.biz
 * @Author: baiwenping
 * @CreateTime: 2020-03-05 14:57
 * @Description: ${Description}
 */
public interface IAlertNotifyTemplateBiz {
    /**
     * 根据名称查询通知模板
     * @param templateName
     * @return
     */
    List<AlertNotifyTemplate> selectByName (String templateName);

    /**
     * 新增模板
     * @param alertNotifyTemplate
     */
    void insert(AlertNotifyTemplate alertNotifyTemplate);

    /**
     * 修改模板
     * @param alertNotifyTemplate
     */
    void update(AlertNotifyTemplate alertNotifyTemplate);

    /**
     * 删除模板
     * @param templateName
     */
    void deleteByName (String templateName);

    /**
    * 根据模板发送短信
    * @auther baiwenping
    * @Description
    * @Date 17:53 2020/3/5
    * @Param [templateName, mobiles, params]
    * @return java.util.Map<java.lang.String,java.lang.Object>
    **/
    Map<String, String> sendSms(String templateName, List<String> mobiles, Map<String, Object> params);

    /**
    * 根据模板发送邮件
    * @auther baiwenping
    * @Description
    * @Date 17:53 2020/3/5
    * @Param [templateName, subject, receivers, params]
    * @return java.util.Map<java.lang.String,java.lang.Object>
    **/
    Map<String, String> sendEmail(String templateName, String subject, Boolean isHtml, List<String> receivers, Map<String, Object> params, List<Map<String, Object>> mergeList);

}
