package com.migu.tsg.microservice.atomicservice.composite.service.event.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@JsonInclude(Include.NON_NULL)
public class EventLogList {
    
    @JsonProperty("total_items")
    private int totalItems;
   
    @JsonProperty("total_pages")
    private int totalPages;
    
    private List<EventInfo>results;
}
