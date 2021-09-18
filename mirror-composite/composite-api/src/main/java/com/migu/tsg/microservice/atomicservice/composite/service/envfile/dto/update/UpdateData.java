package com.migu.tsg.microservice.atomicservice.composite.service.envfile.dto.update;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UpdateData {

    private List<List<String>> content;
    
    @JsonProperty("created_at")
    private String createdAt;
    
    private String description;
    
    private String name;
     
    @JsonProperty("updated_at")
    private String updatedAt;
    
    private String uuid;
}
