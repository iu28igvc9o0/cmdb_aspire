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
    private FamilyOpen familyOpen;
    private InterFace interFace;
    private AlertAgent alertAgent;
    private nationalWeb nationalWeb;
    private RealNationalWeb realNationalWeb;
    @Data
    public static class FamilyOpen {
        private String national;
        private String nationalReal;
        private String qName;
    }
    @Data
    public static class InterFace {
        private String interfaceRoot;
        private String indicationList;
        private String indicationResult;
        private String themeCalcItem;
        private String postThemeData;
        private String updateItems;
        private String themeInfo;
    }
    @Data
    public static class AlertAgent {
        private String server;
    }
    @Data
    public static class nationalWeb {
        private String autoSendTopic;
        private String sendTopic;
    }
    @Data
    public static class RealNationalWeb {
        private String autoSendTopic;
        private String sendTopic;
    }
}
