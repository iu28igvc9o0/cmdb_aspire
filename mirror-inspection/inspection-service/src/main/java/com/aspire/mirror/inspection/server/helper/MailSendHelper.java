package com.aspire.mirror.inspection.server.helper;

import static java.lang.String.format;

import java.io.File;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
* 邮件发送helper   <br/>
* Project Name:inspection-service
* File Name:MailSendHelper.java
* Package Name:com.aspire.mirror.inspection.server.helper
* ClassName: MailSendHelper <br/>
* date: 2018年8月24日 下午5:54:04 <br/>
* @author pengguihua
* @version 
* @since JDK 1.6
*/ 
@Slf4j
@Component
public class MailSendHelper {
	@Autowired
	private JavaMailSender	mailSender;

	@Value("${spring.mail.username}")
	private String			username;

	@Value("${spring.mail.switch:false}")
	private Boolean			mailSwitch;

	private ExecutorService	threadPool	
			= new ThreadPoolExecutor(5, 10, 5, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(100));
	
	// 异步发送
	public void asycSendMail(final String subject, final String text, final String... receivers) {
		asycSendMail(subject, text, (File)null, receivers);
	}
	
	// 异步发送
	public void asycSendMail(final String subject, final String text, final File attachFile, final String... receivers) {
		if (attachFile == null) {
			asycSendMail(subject, text, (Pair<String, File>) null, receivers);
		} else {
			asycSendMail(subject, text, Pair.of(attachFile.getName(), attachFile), receivers);
		}
	}
	
	// 异步发送
	public void asycSendMail(String subject, String text, Pair<String, File> attachInfo, String... receivers) {
		if (!mailSwitch) {
			log.info("As the mail switch is off, the mail send is omit.");
			return;
		}
		threadPool.submit(new Runnable() {
			public void run() {
				try { 
					if (attachInfo == null) {
						sendMail(subject, text, receivers);
					} else {
						sendMail(subject, text, attachInfo, receivers);
					}
				} catch (Exception e) {
					String tipMsg 
						= format("Send mail to %s with subject %s failed.", StringUtils.join(receivers), subject);
					log.error(tipMsg, e);
				}
			}
		});
	}

	public void sendMail(
			String subject, String text, String... receivers) throws Exception {
		sendMail(subject, text, (File)null, receivers);
	}
	
	public void sendMail(String subject, String text, File attachFile, String... receivers) throws Exception {
		if (attachFile == null) {
			sendSimpleMail(subject, text, receivers);
			return;
		}
		sendMemiMail(subject, text, Pair.of(attachFile.getName(), attachFile), receivers);
	}
	
	public void sendMail(
			String subject, String text, Pair<String, File> attachInfo, String... receivers) throws Exception {
		sendMemiMail(subject, text, attachInfo, receivers);
	}
	
	private void sendSimpleMail(String subject, String text, String... receivers) throws MailException {
		if (!mailSwitch) {
			log.info("As the mail switch is off, the mail send is omit.");
			return;
		}
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom(username);
		message.setTo(receivers);
		message.setSubject(subject);
		message.setText(text);
		mailSender.send(message);
		log.info("Send mail to {} with subject {} is successfully", StringUtils.join(receivers), subject);
	}
	
	private void sendMemiMail(
		   String subject, String text, Pair<String, File> attachInfo, String... receivers) throws MessagingException {
		if (!mailSwitch) {
			log.info("As the mail switch is off, the mail send is omit.");
			return;
		}
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, true);
		helper.setSubject(subject);
		helper.setText(text);
		helper.setTo(receivers);
		helper.addAttachment(attachInfo.getLeft(), new FileSystemResource(attachInfo.getRight()));
		mailSender.send(message);
		log.info("Send mail to {} with subject {} is successfully", StringUtils.join(receivers), subject);
	}
}
