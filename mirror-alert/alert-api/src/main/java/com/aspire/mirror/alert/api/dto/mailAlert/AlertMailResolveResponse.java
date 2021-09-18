package com.aspire.mirror.alert.api.dto.mailAlert;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class AlertMailResolveResponse {
    private Integer id;
    @JsonProperty("mail_title")
    private String mailTitle;
    @JsonProperty("mail_content")
    private String mailContent;
    @JsonProperty("mail_sender")
    private String mailSender;
    @JsonProperty("mail_receiver")
    private String mailReceiver;
    @JsonProperty("mail_send_time")
    private Date mailSendTime;
    @JsonProperty("resolve_time")
    private Date resolveTime;
    @JsonProperty("device_ip")
    private String deviceIp;
    @JsonProperty("moni_object")
    private String moniObject;
    @JsonProperty("moni_index")
    private String moniIndex;
    @JsonProperty("alert_level")
    private String alertLevel;
    @JsonProperty("alert_id")
    private String alertId;
}
