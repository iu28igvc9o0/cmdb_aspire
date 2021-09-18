package com.aspire.mirror.alert.server.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

/**
 * @author baiwenping
 * @Title: AlertPingStatusConfig
 * @Package com.aspire.mirror.alert.server.config
 * @Description: TODO
 * @date 2021/3/3 14:13
 */
@ConfigurationProperties(prefix = "alert.ping")
@Data
@Service
public class AlertPingStatusProperties {
    private boolean flag = true;
    private String manageIpTitle = "管理网主机不可达";
    private String ipmiIpTitle = "IPMI主机不可达";
    private String serviceIpTitle = "业务网主机不可达";
    private String topic = "Ci_Zbx";
    private String cmdbYes = "11462";
    private String cmdbNo = "11463";
}
