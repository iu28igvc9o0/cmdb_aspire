package com.migu.tsg.microservice.atomicservice.composite.service.envfile.dto.create;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class EnvFileCreateResponse {
    
    @JsonProperty("env_file")
    private CreatedData envfile;
}
