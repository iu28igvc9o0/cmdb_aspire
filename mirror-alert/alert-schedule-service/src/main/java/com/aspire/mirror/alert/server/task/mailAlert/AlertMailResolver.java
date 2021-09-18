package com.aspire.mirror.alert.server.task.mailAlert;

import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.aspire.mirror.alert.server.util.Md5Utils;
import com.aspire.mirror.alert.server.biz.model.AlertFieldBiz;
import com.aspire.mirror.alert.server.biz.helper.AlertsHandleV2Helper;
import com.aspire.mirror.alert.server.vo.alert.AlertsV2Vo;
import org.apache.commons.lang.StringUtils;
import org.htmlparser.Parser;
import org.htmlparser.util.ParserException;
import org.htmlparser.visitors.TextExtractingVisitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSON;
import com.aspire.mirror.alert.server.dao.mailAlert.AlertMailFilterDao;
import com.aspire.mirror.alert.server.dao.mailAlert.AlertMailFilterStrategyDao;
import com.aspire.mirror.alert.server.dao.mailAlert.AlertMailResolveRecordDao;
import com.aspire.mirror.alert.server.dao.mailAlert.po.AlertMailFilter;
import com.aspire.mirror.alert.server.dao.mailAlert.po.AlertMailFilterStrategy;
import com.aspire.mirror.alert.server.dao.mailAlert.po.AlertMailResolveRecord;
import com.aspire.mirror.alert.server.dao.mailAlert.po.AlertMailSubstance;

@Component
@ConditionalOnExpression("${middleware.configuration.switch.mailKafka:false}")
public class AlertMailResolver {

    private static final Logger logger = LoggerFactory.getLogger(AlertMailResolver.class);
    @Autowired
    private AlertMailFilterDao alertMailFilterDao;
    @Autowired
    private AlertMailResolveRecordDao resolveRecordDao;
    @Autowired
    private AlertMailFilterStrategyDao strategyDao;
    @Autowired
    private AlertFieldBiz alertFieldBiz;
    @Autowired
    private AlertsHandleV2Helper alertsHandleV2Helper;

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private static final int STRATEGY_MAIL_FIELD_SUBJECT = 0;
    private static final int STRATEGY_MAIL_FIELD_CONTENT = 1;
    private static final int STRATEGY_MAIL_FIELD_SENDER = 2;
    private static final int STRATEGY_MAIL_FIELD_SENDTIME = 3;

    private Validation valid(AlertMailSubstance substance, AlertMailFilter filter) {
        logger.info("#-- 邮箱账号: {} 基本校验-----> 开始 ------------", substance.getReceiver());
        boolean valid = true;
        String message = "";
        Validation validation = new Validation();
        //获取到发件邮箱
        String filterSender = StringUtils.trim(filter.getSender());
        //邮件过滤:标题包含关键字
        String filterTitleInclude = StringUtils.trim(filter.getTitleInclude());
        //邮件过滤:内容包含关键字
        String filterContentInclude = StringUtils.trim(filter.getContentInclude());
        if (StringUtils.isNotEmpty(substance.getSender())) {
            if (StringUtils.isNotEmpty(filterSender)) {
                //发送邮箱是否相等
                valid = substance.getSender().contains(filterSender);
                if (!valid) {
                    message += MessageFormat.format("#--> 发件人 {0} 不包含关键字 {1};", substance.getSender(), filterSender);
                }
            }
            if (StringUtils.isNotEmpty(filterTitleInclude) && valid) {
                //标题包含关键字是否相同
                String subject = StringUtils.isEmpty(substance.getSubject()) ? "" : substance.getSubject();
                valid = subject.contains(filterTitleInclude);
                if (!valid) {
                    message += MessageFormat.format("#--> 主题 {0} 不包含关键字 {1}; ", substance.getSubject(), filterTitleInclude);
                }
            }
            if (StringUtils.isNotEmpty(filterContentInclude) && valid) {
                //内容包含关键字是否相同
                String content = StringUtils.isEmpty(substance.getContent()) ? "" : substance.getContent();
                valid = content.contains(filterContentInclude);
                if (!valid) {
                    message += MessageFormat.format("#--> 内容 不包含关键字 {0}; ", filterTitleInclude);
                }
            }
        } else {
            valid = false;
        }
        logger.info(message);
        logger.info("#-- 邮箱账号: {} 基本校验-----> 结束 -- {} --", substance.getReceiver(), valid ? "通过" : "未通过");
        validation.setValid(valid);
        validation.setMessage(message);
        return validation;
    }

    /**
     * 正则提取
     *
     * @param regex 正则表达式
     * @param text  待解析字符串
     * @return
     */
    private String regExtract(String regex, String text) {
        if (StringUtils.isEmpty(regex)) {
            return text;
        }
        if (StringUtils.isEmpty(text)) {
            return null;
        }
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);
        try {
            while (matcher.find()) {
                return matcher.group();
            }
        } catch (Exception e) {
            logger.error("#==正则匹配出错！正则: {}, 文本: {}", regex, text, e);
        }
        return null;
    }

    /**
     * 过滤邮件，提取纯文本内容
     *
     * @param text
     * @return
     * @throws ParserException
     */
    private String getMailRawContent(String text) {
        String result = "";
        try {
            Parser parser = new Parser(text);
            TextExtractingVisitor visitor = new TextExtractingVisitor();
            parser.visitAllNodesWith(visitor);
            String content = visitor.getExtractedText();
            String filterBodyRegex = "body\\s\\{[\\s\\S]*?\\}";
            Pattern filterBodyPattern = Pattern.compile(filterBodyRegex, Pattern.CASE_INSENSITIVE);
            Matcher filterBodyMatcher = filterBodyPattern.matcher(content);
            result = filterBodyMatcher.replaceAll("");
        } catch (ParserException e) {
//            logger.error("解析邮件内容出错!", e);
        } finally {
            if (StringUtils.isEmpty(result)) {
                result = text;
            }
        }
        return result;
    }

    /**
     * 解析生成警报字段
     *
     * @param substance
     * @param strategy
     * @return
     */
    private String parseAlertField(AlertMailSubstance substance, AlertMailFilterStrategy strategy) throws ParseException, ParserException {
        String value = null;
        String matchVal = strategy.getFieldMatchValue();
        if (strategy.getMailField() == -1) { //自定义, 忽略邮件字段, 不走正则匹配
            return matchVal;
        } else { // 邮件信息
            //FieldMatchReg==自定义正则表达式
            String regex = strategy.getFieldMatchReg();
//            if (StringUtils.isNotEmpty(regex)) {
            //MailField==邮件的字段名, 0: 标题, 1: 内容, 2:发件人, 3:发件时间
            switch (strategy.getMailField()) {
                case STRATEGY_MAIL_FIELD_SUBJECT: //标题
                    //使用正则匹配, 0: 不使用, 1:使用
                    value = strategy.getUseReg() == 1 ? regExtract(regex, substance.getSubject()) : substance.getSubject();
                    break;
                case STRATEGY_MAIL_FIELD_CONTENT: //内容
                    value = strategy.getUseReg() == 1 ? regExtract(regex, substance.getContent()) : substance.getContent();
                    break;
                case STRATEGY_MAIL_FIELD_SENDER: //发件人
                    value = strategy.getUseReg() == 1 ? regExtract(regex, substance.getSender()) : substance.getSender();
                    break;
//                case STRATEGY_MAIL_FIELD_SENDTIME: //发件时间
//                    value = strategy.getUseReg() == 1 ? substance.getSendTime() : sdf.parse(strategy.getFieldMatchValue());
//                    break;
                default:
                    break;
            }
//            }
        }
        return value;
    }

    private String parseAlertLevel(AlertMailSubstance substance, AlertMailFilterStrategy strategy) {
        String value = null;
        String matchVal = strategy.getFieldMatchValue();
        if (strategy.getMailField() == -1) { //自定义, 忽略邮件字段, 不走正则匹配
            if (Pattern.matches("\\d+", matchVal)) {
                return matchVal;
            } else {
                logger.error("#===> alert-level 解析出错! 非法的数据类型, 不是整数: {}", matchVal);
            }
        } else { // 邮件信息
            //FieldMatchTarget==字段匹配后映射值
            String targetValue = strategy.getFieldMatchTarget();
            //MailField==邮件的字段名, 0: 标题, 1: 内容, 2:发件人, 3:发件时间
            switch (strategy.getMailField()) {
                case STRATEGY_MAIL_FIELD_SUBJECT: //标题
                    if (substance.getSubject().contains(matchVal)) {
                        value = targetValue;
                    }
                    break;
                case STRATEGY_MAIL_FIELD_CONTENT: //内容
                    if (substance.getContent().contains(matchVal)) {
                        value = targetValue;
                    }
                    break;
                case STRATEGY_MAIL_FIELD_SENDER: //发件人
                    if (substance.getSender().contains(matchVal)) {
                        value = targetValue;
                    }
                    break;
                default:
                    logger.error("#===> 未知的邮件字段: {}", strategy.getMailField());
                    break;
            }
        }
        return value;
    }

    private Date parseAlertMoniTime(AlertMailSubstance substance, AlertMailFilterStrategy strategy) throws ParseException, ParserException {
        Date value = substance.getSendTime();
        try {
            //配置字段的值
            String matchVal = strategy.getFieldMatchValue();
            if (strategy.getMailField() == -1) { //自定义, 忽略邮件字段, 不走正则匹配
                if (StringUtils.isNotEmpty(matchVal) && Pattern.matches("^\\d{4}-\\d{1,2}-\\d{1,2}\\s\\d{1,2}:\\d{1,2}:\\d{1,2}$", matchVal)) {
                    return sdf.parse(matchVal);
                }
            } else {
                //自定义正则表达式
                String regex = strategy.getFieldMatchReg();
                String temp = "";
                //邮件的字段名, 0: 标题, 1: 内容, 2:发件人, 3:发件时间
                switch (strategy.getMailField()) {
                    case STRATEGY_MAIL_FIELD_SUBJECT: //标题
                        //UseReg==使用正则匹配, 0: 不使用, 1:使用
                        temp = (strategy.getUseReg() == 1 && StringUtils.isNotEmpty(regex)) ? regExtract(regex, substance.getSubject()) : substance.getSubject();
                        break;
                    case STRATEGY_MAIL_FIELD_CONTENT: //内容
                        temp = (strategy.getUseReg() == 1 && StringUtils.isNotEmpty(regex)) ? regExtract(regex, substance.getContent()) : substance.getContent();
                        break;
                    case STRATEGY_MAIL_FIELD_SENDER: //发件人
                        temp = (strategy.getUseReg() == 1 && StringUtils.isNotEmpty(regex)) ? regExtract(regex, substance.getSender()) : substance.getSender();
                        break;
                    case STRATEGY_MAIL_FIELD_SENDTIME: //发件时间
                        temp = (strategy.getUseReg() == 1 && StringUtils.isNotEmpty(regex)) ? regExtract(regex, sdf.format(substance.getSendTime())) : sdf.format(substance.getSendTime());
                        break;
                    default:
                        break;
                }
                if (StringUtils.isNotEmpty(temp)) {
                    value = sdf.parse(temp);
                }
            }
        } catch (ParseException e) {
            logger.error("解析邮件内的日期出错!", e);
        }
        return value;
    }

    /**
     * 解析生成警报对象
     *
     * @param substance
     * @param strategyList
     * @return
     */
    private AlertsV2Vo parseAlert(AlertMailSubstance substance, List<AlertMailFilterStrategy> strategyList) {
        AlertsV2Vo alert = null;
        AlertMailSubstance innerSub = new AlertMailSubstance();
        //拷贝innerSub
        BeanUtils.copyProperties(substance, innerSub);
        try {
            //获取邮箱的内容
            String content = getMailRawContent(substance.getContent());
            innerSub.setContent(content);
            alert = new AlertsV2Vo();
            alert.setAlertType(AlertsV2Vo.ALERT_ACTIVE);
            String filterId = null;
            for (AlertMailFilterStrategy strategy : strategyList) {
                if (StringUtils.isEmpty(filterId)) {
                    filterId = strategy.getFilterId();
                }
                //AlertField==匹配警报的字段名
                switch (strategy.getAlertField().toLowerCase()) {
                    case "moni_index": //告警描述
                        String moniIndex = parseAlertField(innerSub, strategy);
                        //监控指标/内容
                        alert.setMoniIndex(moniIndex);
                        break;
                    case "cur_moni_time": //告警时间
                        Date curMonitime = parseAlertMoniTime(innerSub, strategy);
                        alert.setCurMoniTime(curMonitime == null ? innerSub.getSendTime() : curMonitime);
                        break;
                    case "moni_object": //告警指标
                        String moniObj = parseAlertField(innerSub, strategy);
                        alert.setMoniObject(moniObj);
                        alert.setItemId(Md5Utils.generateCheckCode(moniObj));
                        break;
                    case "device_ip": //设备IP
                        String deviceIp = parseAlertField(innerSub, strategy);
                        alert.setDeviceIp(deviceIp);
                        break;
                    case "alert_level": //告警级别, 1-提示 2-低 3-中 4-高 5-严重
                        String alertLevel = parseAlertLevel(innerSub, strategy);
                        alert.setAlertLevel(alertLevel);
                        break;
                    case "cur_moni_value": //当前监控值
                        String curMoniVal = parseAlertField(innerSub, strategy);
                        alert.setCurMoniValue(curMoniVal);
                        break;
                    case "idc_type": //资源池
                        String idcType = parseAlertField(innerSub, strategy);
                        alert.setIdcType(idcType);
                        break;
                    case "biz_sys": //业务系统
                        String bizSys = parseAlertField(innerSub, strategy);
                        alert.setBizSys(bizSys);
                        break;
                    case "device_class": //设备类型
                        String deviceClass = parseAlertField(innerSub, strategy);
                        alert.setDeviceClass(deviceClass);
                        break;
                    case "source_room": //机房
                        String sourceRoom = parseAlertField(innerSub, strategy);
                        alert.setSourceRoom(sourceRoom);
                        break;
                    case "object_type": //告警类型, 1-系统 2-业务
                        String objectType = parseAlertField(innerSub, strategy);
                        alert.setObjectType(objectType);
                        break;
                    default:
                        break;
                }
            }
            if (StringUtils.isEmpty(alert.getItemId())) {
                alert.setItemId(filterId);
            }
            alert.setSource("MAIL");
            alert.setAlertStartTime(innerSub.getSendTime());
            if (StringUtils.isEmpty(alert.getAlertLevel())) {
                logger.error("#===> 邮件解析，警报级别解析为空!");
                return null;
            }
            if (StringUtils.isEmpty(alert.getDeviceIp())) {
                alert.setObjectType(AlertsV2Vo.OBJECT_TYPE_BIZ);
            } else {
                alert.setObjectType(AlertsV2Vo.OBJECT_TYPE_DEVICE);
            }
//            if (StringUtils.isEmpty(alert.getMoniIndex())) { // 告警描述为空
//                logger.error("#===> 邮件解析，警报描述解析为空!");
//                return null;
//            }
        } catch (ParseException e) {
            logger.error("#===> 邮件警报实体解析失败 !", e);
        } catch (ParserException e) {
            logger.error("#===> 邮件警报实体解析->去除内容HTML标签失败 !", e);
        }
        return alert;
    }

    //    @Async
    void resolve(AlertMailSubstance substance) {
        logger.info("##==> 邮件实体: {}", JSON.toJSONString(substance));
        //根据收件邮箱和邮箱状态为启用时，查询出所有的数据
        List<AlertMailFilter> filterList = alertMailFilterDao.selectFilterByReceiver(substance.getReceiver());
        if (CollectionUtils.isEmpty(filterList)) {
            logger.error("#===> 不存在此收件邮箱: {} 对应的数据过滤规则 !", substance.getReceiver());
            return;
        }
        for (AlertMailFilter filter : filterList) {
            //对邮箱内容进行校验，获取到发件邮箱，邮件过滤:标题包含关键字，邮件过滤:内容包含关键字
            Validation validation = valid(substance, filter);
            if (!validation.isValid()) {
                continue;
            }
            //根据Filter主键ID去查询出
            List<AlertMailFilterStrategy> strategyList = strategyDao.selectStrategiesByFilterId(filter.getId());
            if (!CollectionUtils.isEmpty(strategyList)) {
                AlertsV2Vo alert = parseAlert(substance, strategyList);
                if (alert != null) {
                    // 保存 alert 记录
//                    ObjectMapper objectMapper = new ObjectMapper();
//                    String jsonString = "{}";
//                    try {
//                        jsonString = objectMapper.writeValueAsString(alert);
//                    } catch (JsonProcessingException e) {
//                    }
//                    JSONObject alertJson = JSONObject.parseObject(jsonString);
//                    List<AlertFieldRequestDTO> alertFieldList = alertFieldBiz.getModelField(AlertConfigConstants.REDIS_MODEL_ALERT);
//                    if (com.aspire.mirror.alert.server.util.StringUtils.isNotEmpty(alert.getDeviceIp())){
//                        // 根据  机房 + IP, 查找设备
//                        cmdbHelper.queryDeviceForAlertV2(alertJson, alertFieldList,alertFieldBiz.getModelField(AlertConfigConstants.REDIS_MODEL_DEVICE_INSTANCE));
//                    }
//                    String alertId = alertsBiz.insert(AlertV2CommonUtils.generateAlerts(alertJson, alertFieldList));;
                    //把数据存在alert_alerts表中
                    String alertId = alertsHandleV2Helper.handleAlert(alert);

                    logger.info("recipient alert: {}", JSON.toJSONString(alert));
                    Date resolveTime = new Date();
                    AlertMailResolveRecord resolveRecord = new AlertMailResolveRecord();
                    resolveRecord.setFilterId(filter.getId());
                    resolveRecord.setMailTitle(substance.getSubject());
                    resolveRecord.setMailContent(substance.getContent());
                    resolveRecord.setMailSender(substance.getSender());
                    resolveRecord.setMailReceiver(substance.getReceiver());
                    resolveRecord.setMailSendTime(substance.getSendTime());
                    resolveRecord.setResolveTime(resolveTime);
                    resolveRecord.setDeviceIp(alert.getDeviceIp());
                    resolveRecord.setMoniObject(alert.getMoniObject());
                    resolveRecord.setMoniIndex(alert.getMoniIndex());
                    resolveRecord.setAlertLevel(alert.getAlertLevel());
                    if (StringUtils.isEmpty(alertId)) {
                        alertId = "-1";
                    }
                    resolveRecord.setAlertId(alertId);
                    //把信息存在alert_mail_resolve_record
                    resolveRecordDao.insertResolveRecords(resolveRecord);
                    break;
                } else {
                    logger.error("#===> 邮件解析失败，未能解析成告警实例! substance: {}", JSON.toJSONString(substance));
                }
            } else {
                logger.info("#===> 邮件解析规则未配置! 过滤规则ID: {}", filter.getId());
            }
        }
    }

    @Autowired
    private ThreadPoolTaskExecutor taskExecutor;

    @KafkaListener(topics = {"ALERT_MAIL_SUBSTANCE"})
    private void consume(String data) {
        List<AlertMailSubstance> sbustanceList = JSON.parseArray(data, AlertMailSubstance.class);
        logger.info("kafka中的数据" + sbustanceList);
        if (!CollectionUtils.isEmpty(sbustanceList)) {
            for (AlertMailSubstance substance : sbustanceList) {
                taskExecutor.execute(() -> resolve(substance));
            }
        }
    }

    private class Validation {
        private boolean valid;
        private String message;

        public boolean isValid() {
            return valid;
        }

        public void setValid(boolean valid) {
            this.valid = valid;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}
