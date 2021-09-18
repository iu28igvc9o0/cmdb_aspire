package com.aspire.mirror.indexadapt;

import com.aspire.mirror.indexadapt.util.Beans;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.concurrent.CountDownLatch;

/**
 * 应用入口    <br/>
 * Project Name:index-proxy
 * File Name:IndexAdaptApplication.java
 * Package Name:com.aspire.mirror.indexadapt
 * ClassName: IndexAdaptApplication <br/>
 * date: 2018年8月21日 上午11:06:33 <br/>
 *
 * @author pengguihua
 * @since JDK 1.6
 */
@EnableScheduling
@EnableTransactionManagement(proxyTargetClass = true)
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class, KafkaAutoConfiguration.class})
public class IndexAdaptApplication extends SpringBootServletInitializer {

    private static final CountDownLatch LATCH = new CountDownLatch(1);

    public static void main(String[] args) throws Exception {
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                LATCH.countDown();
            }
        }));
        System.setProperty("es.set.netty.runtime.available.processors", "false");
        SpringApplication app = new SpringApplication(IndexAdaptApplication.class);
//        app.setWebEnvironment(false);
        app.addListeners(new Beans()); // new EnvLoadedListerner()
        app.run(args);
        LATCH.await();
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(IndexAdaptApplication.class);
    }
}
