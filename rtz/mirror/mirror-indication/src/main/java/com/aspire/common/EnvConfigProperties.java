package com.aspire.common;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * env config
 * <p>
 * 项目名称:  咪咕微服务运营平台
 * 包:       com.migu.tsg.msp.atomicservice.dsproxy.common
 * 类名称:    com.migu.tsg.msp.atomicservice.dsproxy.common.EnvConfigProperties.java
 * 类描述:
 * 创建人:    zhu.juwang
 * 创建时间:  2017/09/29 10:44
 * 版本:      v1.0
 */
@ConfigurationProperties(prefix = "envconfig")
@Component
@Data
public class EnvConfigProperties {
    private AlertAgent alertAgent;
    private NationalWeb nationalWeb;
    private RealNationalWeb realNationalWeb;
    private FileCheck fileCheck;
    @Data
    public static class AlertAgent {
        private String server;
    }
    @Data
    public static class NationalWeb {
        private String wsdl;
        private String qName;
        private String autoSendTopic;
        private String sendTopic;
    }
    @Data
    public static class RealNationalWeb {
        private String wsdl;
        private String qName;
        private String autoSendTopic;
        private String sendTopic;
    }
    @Data
    public static class FileCheck {
        private String autoSendTopic;
        private String sendTopic;
    }
}
