package com.aspire.mirror.composite.payload.mailAlert;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CompAlertMailReceiverResponse {
    private Integer id;
    private String receiver;
    private String password;
    @JsonProperty("mail_server")
    private String mailServer;
    @JsonProperty("receive_protocal")
    private Integer receiveProtocal;
    @JsonProperty("receive_port")
    private Integer receivePort;
    private Integer active;
    @JsonProperty("strategy_num")
    private Integer strategyNum;
    private String description;
    private Integer period;
    private String name;
}
