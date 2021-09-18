package com.aspire.mirror.scada.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * TODO
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.scada.config
 * 类名称:    FtpProperties.java
 * 类描述:    TODO
 * 创建人:    JinSu
 * 创建时间:  2020/1/14 16:58
 * 版本:      v1.0
 */
@Data
@ConfigurationProperties(prefix = "ftp")
@Component
public class FtpProperties {
    private String host;
    private Integer port;
    private String username;
    private String password;
}
