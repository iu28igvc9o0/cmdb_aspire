package com.migu.tsg.microservice.atomicservice.composite.service.space.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Quota {
    
    private String hard;

    private String name; 

    private Double used=0d;

    private String uuid;
 
    private String _id;
  
    private String created_at;
 
    private String updated_at;
}
