package com.aspire.ums.cmdb.bpmx.sendOrder;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Data;

/**
 * bpm派单配置
 *
 * @author jiangxuwen
 * @date 2021/1/4 11:47
 */
@Component
@Data
public class BpmConfiguration {

    @Value("${ipclash.sendOrder.cmdb2bpm_general_account:10000005559006}")
    private String osa2BpmAccount;

    @Value("${ipclash.sendOrder.url:}")
    private String bpmDispatchOrderUrl;

    @Value("${bpmJwtTokenUrl:}")
    String bpmJwtTokenUrl;
}
