package com.aspire.mirror.composite.payload.notify;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotEmpty;
import java.util.List;

@Data
@NoArgsConstructor
public class UpdateAlertSubscribeRulesRequest {
    /**
     * 规则id
     */
    private String uuid;
    /**
     * // 告警配置名称
     */
    private String subscribeRules;
    /**
     * 通知对象
     */
    private List<Reciver> reciverList;
    /**
     * // 是否启用
     */

    private int isOpen;
    /**
     * // 告警通知类型
     */
    private String notifyAlertType;
    /**
     * // 通知类型
     */

    private String notifyType;
    /**
     * // 重发间隔时间
     */
    private String recurrenceInterval;
    /**
     * // 重发间隔单位
     */

    private String recurrenceIntervalUtil;
    /**
     * // 是否重发
     */

    private int isRecurrenceInterval;
    /**
     * // 重发次数
     */

    private Integer recurrenceCount;
    /**
     * // 发送方式
     */

    private int emailType;
    /**
     * // 最近发送时间
     */

    private String curSendTime;
    /**
     * // 最近发送时间
     */

    private String resendTime;
    /**
     * // 当前维护人
     */

    private String creator;
    /**
     * // 执行时间段 0-全天 1-区间
     */

    private String period;
    /**
     * 配置起始执行时间
     */
    private String startPeriod;
    /**
     * 配置执行结束
     */
    private String endPeriod;
    /**
     * //邮件的内容
     */

    private String emialContent;
    /**
     * //邮件的主题
     */

    private String subject;
    /**
     * //短信的内容
     */

    private String smsContent;
    /**
     * 用户选择的告警id
     */
    @JsonProperty("alertIds")
    @NotEmpty
    private String alertIds;
    /**
     * 创建人
     */
    private String defensetor;
}
