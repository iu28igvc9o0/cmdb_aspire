package com.migu.tsg.microservice.atomicservice.composite.service.envfile.dto.create;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class EnvFileCreateRequest {

    private List<List<String>> content;
    
    private String description;
    
    private String name;
    
    @JsonProperty("space_name")
    private String spaceName = "";
    
    private String namespace;
}
