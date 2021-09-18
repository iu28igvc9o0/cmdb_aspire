package com.migu.tsg.microservice.atomicservice.composite.service.envfile.dto.update;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class EnvFileUpdateResponse {

    @JsonProperty("env_file")
    private UpdateData envfile;
}
