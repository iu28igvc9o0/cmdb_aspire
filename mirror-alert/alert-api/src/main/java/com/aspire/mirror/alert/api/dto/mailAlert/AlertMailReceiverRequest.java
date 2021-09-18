package com.aspire.mirror.alert.api.dto.mailAlert;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class AlertMailReceiverRequest {

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
    private String description;
    private Integer period;
    private Integer unit;
    @NotNull
    @JsonProperty("page_size")
    private Integer pageSize = 20;
    @NotNull
    @JsonProperty("page_no")
    private Integer pageNo = 1;
    @NotEmpty
    @JsonProperty("sort_name")
    private String sortName;
    
    private String name;
}
