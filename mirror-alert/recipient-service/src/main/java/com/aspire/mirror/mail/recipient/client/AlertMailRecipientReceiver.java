package com.aspire.mirror.mail.recipient.client;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.aspire.mirror.mail.recipient.dao.AlertMailRecipientDao;
import com.aspire.mirror.mail.recipient.po.AlertMailFailed;
import com.aspire.mirror.mail.recipient.po.AlertMailRecipient;
import com.aspire.mirror.mail.recipient.po.AlertMailSubstance;
import com.aspire.mirror.mail.recipient.service.AlertMailReceiveService;
import com.google.common.collect.Lists;
import com.sun.mail.imap.IMAPFolder;
import com.sun.mail.pop3.POP3Folder;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import javax.mail.*;
import javax.mail.internet.MimeUtility;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: AlertMailReceiver
 * Author:   liangjun
 * Date:     2019/5/31 13:54
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Component
public class AlertMailRecipientReceiver {

    private static final Logger logger = LoggerFactory.getLogger(AlertMailRecipientReceiver.class);
    @Autowired
    private AlertMailRecipientDao alertMailRecipientDao;

    private final static Pattern MAIL_PATTERN = Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
    private static final String MAIL_SERVER_PROTOCAL_POP3 = "pop3";
    private static final String MAIL_SERVER_PROTOCAL_IMAP = "imap";
    private static final String MAIL_CONTENT_CHARSET = "iso-8859-1";
    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;
    @Value("${kafka.topic.alert_mail_substance:ALERT_MAIL_SUBSTANCE}")
    private String topic;
    @Autowired
    private AlertMailReceiveService alertMailReceiveService;

    private void read(String host, Integer port, Integer protocal, String account, String password) {
        String serverProtocal = (protocal == null || protocal == 0) ? MAIL_SERVER_PROTOCAL_POP3 : MAIL_SERVER_PROTOCAL_IMAP;
        List<AlertMailSubstance> substanceList = Lists.newArrayList();
        switch (serverProtocal) {
            case MAIL_SERVER_PROTOCAL_POP3:
                substanceList = pop(host, port, account, password);
                break;
            case MAIL_SERVER_PROTOCAL_IMAP:
                substanceList = imap(host, port, account, password);
                break;
            default:
                logger.error("#===> 未知邮件服务器协议: protocal: {}", protocal);
                break;
        }
        if (CollectionUtils.isNotEmpty(substanceList)) {
            String jsonObj = JSON.toJSONString(substanceList, SerializerFeature.WriteMapNullValue);
            ListenableFuture<SendResult<String, Object>> future = kafkaTemplate.send(topic, jsonObj);
            List<AlertMailSubstance> finalSubstanceList = substanceList;
            future.addCallback(new ListenableFutureCallback<SendResult<String, Object>>() {
                @Override
                public void onFailure(Throwable e) {
                    logger.error("#===> 数据发送失败: topic: {} message: {}", topic, jsonObj, e);
                }

                @Override
                public void onSuccess(SendResult<String, Object> result) {
                    logger.info("#===> 数据发送成功, {}", result.toString());
                    try {
                        alertMailRecipientDao.batchSaveSubstance(finalSubstanceList);
                    } catch (Exception e) {
                        logger.error("#===> 保存收件邮箱 {} 接收的邮件记录出错 !", account, e);
                    }
                }
            });
        }
    }

    private List<AlertMailSubstance> pop(String host, Integer port, String account, String password) {
        List<AlertMailSubstance> list = Lists.newArrayList();
        Properties properties = new Properties();
        properties.setProperty("mail.store.protocol", MAIL_SERVER_PROTOCAL_POP3);
        properties.setProperty("mail.pop3.host", host);
        properties.setProperty("mail.pop3.port", Integer.toString(port));
        Store store = null;
        Folder folder = null;
        try {
            Session session = Session.getInstance(properties);
            session.setDebug(true);
            store = session.getStore(MAIL_SERVER_PROTOCAL_POP3);
            // connect account = account.substring(0, account.indexOf("@")
            store.connect(host, account, password);
            folder = store.getFolder("INBOX");
            folder.open(Folder.READ_WRITE);
            logger.info("#====> 邮件总数: {}",  folder.getMessageCount());
            list = getMailSubstances(account, folder, folder.getMessages());
        } catch (Exception e) {
            logger.error("#====> 邮件收取出错：host: {}, port: {} protocal: {}, account: {}",
                    host, port, MAIL_SERVER_PROTOCAL_POP3, account, e);
        } finally {
            try {
                if (folder != null) {
                    folder.close(true);
                }
                if (store != null) {
                    store.close();
                }
            } catch (MessagingException e) {
                logger.error("#===> 释放 pop3 邮件连接失败!", e);
            }
        }
        return list;
    }

    private List<AlertMailSubstance> imap(String host, Integer port, String account, String password) {
        List<AlertMailSubstance> list = Lists.newArrayList();
        Properties properties = new Properties();
        properties.setProperty("mail.store.protocol", MAIL_SERVER_PROTOCAL_IMAP);
        properties.setProperty("mail.imap.host", host);
        properties.setProperty("mail.imap.port", Integer.toString(port));
        Store store = null;
        Folder folder = null;
        try {
            Session session = Session.getInstance(properties);
            store = session.getStore(MAIL_SERVER_PROTOCAL_IMAP);
            store.connect(account, password);
            folder = store.getFolder("INBOX");
            folder.open(Folder.READ_WRITE);
            list = getMailSubstances(account, folder, folder.getMessages());
        } catch (Exception e) {
            logger.error("获取账号:{} 的邮件发生异常!", account, e);
        } finally {
            try {
                if (folder != null) {
                    folder.close(true);
                }
                if (store != null) {
                    store.close();
                }
            } catch (MessagingException e) {
                logger.error("#===> 释放 imap 邮件连接失败!", e);
            }
        }
        return list;
    }

    private List<AlertMailSubstance> getMailSubstances(String account, Folder folder, Message[] messages) {
        List<AlertMailSubstance> list = Lists.newArrayList();
        AlertMailFailed failed = null;
        for (Message message : messages) {
            String uid = null;
            String method = MAIL_SERVER_PROTOCAL_IMAP;
            String sender = "";
            try {
                if (folder instanceof POP3Folder) {
                    method = MAIL_SERVER_PROTOCAL_POP3;
                    POP3Folder inbox = (POP3Folder) folder;
                    uid = inbox.getUID(message);
                } else if (folder instanceof IMAPFolder) {
                    IMAPFolder inbox = (IMAPFolder) folder;
                    uid = Long.toString(inbox.getUID(message));
                }
                int count = alertMailRecipientDao.countSubstance(account, uid);
                if (count > 0) {
                    logger.debug("#====> 邮箱:{} 已经存在 uid: {} 的邮件记录, 此邮件将不被解析!", account, uid);
                } else {
                    AlertMailSubstance substance = new AlertMailSubstance();
                    if (message.getFrom() == null || message.getFrom().length == 0) {
                        substance = null;
                    }
                    if (substance != null) {
                        sender = parseMailSender(message.getFrom()[0].toString());
                        String subject = MimeUtility.decodeText(new String(message.getSubject().getBytes(MAIL_CONTENT_CHARSET)));
                        Date sendTime = message.getSentDate();
                        substance.setUid(uid);
                        substance.setReceiver(account);
                        substance.setSender(sender);
                        substance.setSubject(subject);

                        StringBuilder contentBuilder = new StringBuilder();
                        parseMailContent(message, contentBuilder);
                        substance.setContent(contentBuilder.toString());
                        substance.setSendTime(sendTime);
                        list.add(substance);
                    }
                }
            } catch (Exception e) {
                String errMsg = e.getMessage();
                failed = new AlertMailFailed();
                failed.setReceiver(account);
                failed.setMethod(method);
                failed.setSender(sender);
                failed.setUid(uid);
                failed.setMessage(errMsg);
                logger.error("", e);
            } finally {
                if (failed != null) {
                    alertMailRecipientDao.insertFailedRecord(failed);
                }
            }
        }
        return list;
    }

    /**
     * 解析邮件文本
     * @param part
     * @param builder
     * @throws MessagingException
     * @throws IOException
     */
    private void parseMailContent(Part part, StringBuilder builder) throws MessagingException, IOException {
        // 获得邮件的MimeType类型
        String contentType = part.getContentType();
        boolean conName = contentType.indexOf("name") != -1 ? true : false;
        logger.info("邮件 ContentType: {}", contentType);
        if (part.isMimeType("text/plain") && conName == false) {
            builder.append(String.valueOf(part.getContent()));
        } else if (part.isMimeType("text/html") && conName == false) {
            builder.append((String) part.getContent());
        } else if (part.isMimeType("multipart/*")) {
            Multipart multipart = (Multipart) part.getContent();
            int counts = multipart.getCount();
            for (int i = 0; i < counts; i++) {
                parseMailContent(multipart.getBodyPart(i), builder);
            }
        } else if (part.isMimeType("message/rfc822")) {
            parseMailContent((Part) part.getContent(), builder);
        }
    }

//    private String getMailTextContent(Message message) throws MessagingException, IOException {
//        StringBuffer reader = new StringBuffer();
//        String contentType = message.getContentType();
//        if (contentType.startsWith("text/plain")) {
//            getMailTextContent(message, reader,true);
//        } else {
//            getMailTextContent(message, reader,false);
//        }
//        return reader.toString();
//    }

    /**
     * 过滤邮箱
     * @param origin
     * @return
     */
    private String parseMailSender(String origin) {
        Matcher matcher = MAIL_PATTERN.matcher(origin);
        while (matcher.find()) {
            String email = matcher.group();
            if (StringUtils.isNotEmpty(email)) {
                return email;
            }
        }
        return origin;
    }

    public void produce(AlertMailRecipient recipient) {
        List<AlertMailSubstance> substanceList = Lists.newArrayList();
        String protocal = (recipient.getReceiveProtocal() == null || recipient.getReceiveProtocal() == 0) ?
                MAIL_SERVER_PROTOCAL_POP3 :
                MAIL_SERVER_PROTOCAL_IMAP;
        switch (protocal) {
            case MAIL_SERVER_PROTOCAL_POP3:
                substanceList = alertMailReceiveService.pop(recipient);
                break;
            case MAIL_SERVER_PROTOCAL_IMAP:
                substanceList = alertMailReceiveService.imap(recipient);
                break;
            default:
                logger.error("#===> 未知邮件服务器协议: protocal: {}", protocal);
                break;
        }
//        if (CollectionUtils.isEmpty(substanceList)) {
//            return;
//        }
//        logger.info("#====> 邮箱账号: {} =================>> 提交邮件实体 <<===开始", recipient.getReceiver());
//        if (alertMailReceiveService.submitMailSubstance(substanceList)) {
//            logger.info("#====> 邮箱账号: {} 共成功提交了 {} 封待处理邮件实体!", recipient.getReceiver(), substanceList.size());
//        }
//        logger.info("#====> 邮箱账号: {} =================>> 提交邮件实体 <<===结束", recipient.getReceiver());
    }
}
