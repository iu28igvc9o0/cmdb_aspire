package com.migu.tsg.microservice.atomicservice.composite.service.configfile.dto.create;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.migu.tsg.microservice.atomicservice.composite.service.configfile.dto.list.Item;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@JsonInclude(Include.NON_NULL)
public class ConfigCreateRequest {

    private List<Item> content;
    
    private String description;
    
    private String name;
    
    private String namespace;
    
    @JsonProperty("space_name")
    private String spaceName;
}
