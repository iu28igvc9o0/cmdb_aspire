package com.aspire.mirror.indexproxy;

import java.util.concurrent.CountDownLatch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.alicp.jetcache.anno.config.EnableCreateCacheAnnotation;
import com.aspire.mirror.indexproxy.util.Beans;

/**
* 应用入口    <br/>
* Project Name:index-proxy
* File Name:IndexProxyApplication.java
* Package Name:com.aspire.mirror.indexproxy
* ClassName: IndexProxyApplication <br/>
* date: 2018年8月21日 上午11:06:33 <br/>
* @author pengguihua
* @version 
* @since JDK 1.6
*/ 
@EnableTransactionManagement(proxyTargetClass=true)
@SpringBootApplication(exclude= {DataSourceAutoConfiguration.class, KafkaAutoConfiguration.class})
@EnableCreateCacheAnnotation
//@EnableDiscoveryClient
//@EnableFeignClients
public class IndexProxyApplication {

	private static final CountDownLatch	LATCH = new CountDownLatch(1);
	
    public static void main(String[] args) throws Exception {
    	Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			@Override
			public void run() {
				LATCH.countDown();
			}
		}));
    	
        SpringApplication app = new SpringApplication(IndexProxyApplication.class);
//        app.setWebEnvironment(false);
        app.addListeners(new Beans()); // new EnvLoadedListerner()
        app.run(args);
        LATCH.await();
    }
}
