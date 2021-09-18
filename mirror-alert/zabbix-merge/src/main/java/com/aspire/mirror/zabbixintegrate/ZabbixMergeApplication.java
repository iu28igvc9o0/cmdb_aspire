package com.aspire.mirror.zabbixintegrate;

import java.util.concurrent.CountDownLatch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
* 搴旂敤鍏ュ彛    <br/>
* Project Name:zabbix-integrate
* File Name:ZabbixIntegrateApplication.java
* Package Name:com.aspire.mirror.zabbixintegrate
* ClassName: ZabbixIntegrateApplication <br/>
* date: 2018骞�10鏈�18鏃� 涓嬪崍1:57:46 <br/>
* @author pengguihua
* @version 
* @since JDK 1.6
*/ 
@EnableScheduling
@EnableTransactionManagement(proxyTargetClass=true)
@SpringBootApplication(exclude= {DataSourceAutoConfiguration.class})
public class ZabbixMergeApplication  extends SpringBootServletInitializer {
	private static final CountDownLatch	LATCH = new CountDownLatch(1);
	
    public static void main(String[] args) throws Exception {
		/*
		 * Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
		 * 
		 * @Override public void run() { LATCH.countDown(); } }));
		 * 
		 * SpringApplication app = new SpringApplication(ZabbixMergeApplication.class);
		 * app.setWebEnvironment(false); app.addListeners(new Beans()); // new
		 * EnvLoadedListerner() app.run(args); LATCH.await();
		 */
    	 SpringApplication.run(ZabbixMergeApplication.class, args);
    }
    
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(ZabbixMergeApplication.class);
    }
}
