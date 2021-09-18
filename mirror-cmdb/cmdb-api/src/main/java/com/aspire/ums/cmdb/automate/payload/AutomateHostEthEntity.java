package com.aspire.ums.cmdb.automate.payload;

import com.aspire.ums.cmdb.automate.payload.base.BaseAutomateEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author fanwenhui
 * @date 2020-08-24 15:08
 * @description 自动化主机模型——网卡信息
 */
@Data
@ToString(callSuper = true)
public class AutomateHostEthEntity extends BaseAutomateEntity implements Serializable {
    private static final long serialVersionUID = -8409427254385096616L;
    // 网卡
    private String name;
    // 状态
    private String status;
    // 关联IP
    private String ip;
    // 子网掩码
    private String mask;
    // 速度
    private String speed;
    // mac地址
    private String MAC;
    // 广播地址
    private String broadcast;
}
