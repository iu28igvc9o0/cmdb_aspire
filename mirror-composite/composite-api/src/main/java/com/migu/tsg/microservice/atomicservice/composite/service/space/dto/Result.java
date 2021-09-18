package com.migu.tsg.microservice.atomicservice.composite.service.space.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Result {
    
    private String uuid;

    private String description;

    private String name;
    
    private String status;
 
    private String created_at;
 
    private String created_by;
 
    private List<String> resource_actions;
     
    private String updated_at;
}
