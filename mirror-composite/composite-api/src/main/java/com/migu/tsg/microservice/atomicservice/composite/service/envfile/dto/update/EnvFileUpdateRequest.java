package com.migu.tsg.microservice.atomicservice.composite.service.envfile.dto.update;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class EnvFileUpdateRequest {

    private List<List<String>> content;
    
    private String description;
    
    private String name;
    
    private String namespace;
    
    @JsonProperty("origin_name")
    private String originName;
}
