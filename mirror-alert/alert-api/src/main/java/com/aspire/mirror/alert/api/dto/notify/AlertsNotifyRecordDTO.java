package com.aspire.mirror.alert.api.dto.notify;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Data
@NoArgsConstructor
public class AlertsNotifyRecordDTO {
    /**
     * 发送地址(手机号)
     */
    private String addresses;
    /**
     * 发送者名称
     */
    private String senderName;
    /**
     * 发送信息
     */
    private String message;
    /**
     * 业务类型
     */
    private String alertIds;
    /**
     * 发送状态
     * 0 失败，1 成功
     */
    private Integer status;
    /**
     * 创建时间
     */
    private Date createTime;
}
