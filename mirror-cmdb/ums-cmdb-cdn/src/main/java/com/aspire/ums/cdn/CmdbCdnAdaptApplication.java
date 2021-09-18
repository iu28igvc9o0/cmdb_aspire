package com.aspire.ums.cdn;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/** 
 *
 * 项目名称: ums-cmdb-cdn 
 * <p/>
 * 
 * 类名: CmdbCdnAdaptApplication
 * <p/>
 *
 * 类功能描述: CDN接入CMDB
 * <p/>
 *
 * @author	pengguihua
 *
 * @date	2019年9月2日  
 *
 * @version	V1.0 
 * <br/>
 *
 * <b>Copyright(c)</b> 2019 卓望公司-版权所有 
 *
 */
@EnableScheduling
@EnableFeignClients
@EnableDiscoveryClient
@EnableTransactionManagement(proxyTargetClass = true)
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class CmdbCdnAdaptApplication {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(CmdbCdnAdaptApplication.class, args);
    }
}
