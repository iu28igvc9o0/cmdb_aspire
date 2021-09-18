package com.migu.tsg.microservice.atomicservice.composite.service.ping.payload;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @program: msp-composite
 * @Description:
 * @author: YangShiLei
 * @create: 2018-04-16 15:33
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PingResponse {

    @JsonProperty("X-Docker-Registry-Version")
    private String registryVersion;

    @JsonProperty("X-Docker-Registry-Standalone")
    private Boolean registryStandalone;

}
