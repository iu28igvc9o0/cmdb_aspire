package com.aspire.mirror.alert.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/**
 * 告警同步响应封装对象    <br/>
 * Project Name:alert-api
 * File Name:AlertSyncResponseItem.java
 * Package Name:com.aspire.mirror.alert.api.dto
 * ClassName: AlertSyncResponseItem <br/>
 * date: 2018年9月20日 下午4:25:46 <br/>
 *
 * @author pengguihua
 * @since JDK 1.6
 */
@Data
public class AlertSyncResponseItem {
    public static final String RESP_CODE_OK = "0000";
    public static final String RESP_CODE_ERROR = "1000";

    @JsonProperty
    private Integer indexNum;                    // 监控项全局序列号
    @JsonProperty
    private String eventId;                    // 告警id
    @JsonProperty
    private String respCode;                    // 响应码
    @JsonProperty
    private String respDesc;                    // 错误信息

    @Override
    public boolean equals(Object obj) {
        if (!AlertSyncResponseItem.class.isInstance(obj)) {
            return false;
        }
        if (this.indexNum == null) {
            return false;
        }
        if (this.eventId == null) {
            return false;
        }
        AlertSyncResponseItem other = (AlertSyncResponseItem) obj;
        return this.indexNum.equals(other.getIndexNum()) && this.eventId.equals(other.getEventId());
    }

    @Override
    public int hashCode() {
        int hash = 17;
        hash = (indexNum == null) ? hash : hash + 13 * indexNum.intValue();
        hash = (eventId == null) ? hash : hash + 13 * eventId.hashCode();
        return hash;
    }
}
