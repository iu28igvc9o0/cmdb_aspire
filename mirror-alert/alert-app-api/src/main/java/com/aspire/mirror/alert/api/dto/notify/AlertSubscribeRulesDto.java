package com.aspire.mirror.alert.api.dto.notify;

import com.fasterxml.jackson.annotation.JsonFormat;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;


import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@NoArgsConstructor
public class AlertSubscribeRulesDto {
    /**
     * 规则id
     */
    private String id;
    /**
     * 规则名称
     */
    private String subscribeRules;
    /**
     * 是否启用 0-关闭(默认) 1-启用
     */
    private String isOpen;
    /**
     * 通知类型 0-邮件/短信 1-邮件 2-短信
     */
    private String notifyType;
    /**
     * '告警通知类型(0：告警生成，1:告警清除与解除)'
     */
    private String notifyAlertType;
    /**
     * 最新发送时间
     */
   @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss" , timezone = "GMT+8")
    private Date curSendTime;
    /**
     * 告警级别
     */
    private String alertLevel;
    /**
     * 设备ip
     */
    private String deviceIp;
    /**
     * 资源池
     */
    private String idcType;
    /**
     * 监控项
     */

    private String itemKey;

    /**
     * 点击订阅告警的数量
     */
    private Integer count;
    /**
     * 告警内容
     */
    private String moniIndex;
    /**
     * 告警管理表中的id
     */

    private String managementId;

    @NotNull
    @JsonProperty("page_size")
    private Integer pageSize;

    @NotNull
    @JsonProperty("page_no")
    private Integer pageNo;
    /**
     * 当前维护人
     */
    private String  defensetor;
}
