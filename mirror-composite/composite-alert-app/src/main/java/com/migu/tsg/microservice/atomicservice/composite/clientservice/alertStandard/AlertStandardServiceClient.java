package com.migu.tsg.microservice.atomicservice.composite.clientservice.alert;

import com.aspire.mirror.alert.api.service.alertStandard.AlertStandardService;
import feign.codec.Encoder;
import feign.form.spring.SpringFormEncoder;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.HttpMessageConverters;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.cloud.netflix.feign.support.SpringEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

/**
 * @projectName: AlertStandardServiceClient
 * @description: 接口
 * @author: luowenbo
 * @create: 2020-06-11 17:48
 **/
@FeignClient(value = "${mirror.feign.alert-app.value}", path = "${mirror.feign.alert-app.path:}",configuration = AlertStandardServiceClient.MultipartSupportConfig.class)
public interface AlertStandardServiceClient extends AlertStandardService {

    @Configuration
    class MultipartSupportConfig {
        @Autowired
        private ObjectFactory<HttpMessageConverters> messageConverters;


        @Bean
        @Scope("prototype")
        public Encoder feignEncoder() {
            return new SpringFormEncoder(new SpringEncoder(messageConverters));
        }
    }
}
