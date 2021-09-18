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
public class ListResponse {

    private Integer count;
    
    private Integer next;
    
    private Integer previous;
 
    private Integer page_size;
 
    private Integer num_pages;
    
    private List<Result> results;
}
