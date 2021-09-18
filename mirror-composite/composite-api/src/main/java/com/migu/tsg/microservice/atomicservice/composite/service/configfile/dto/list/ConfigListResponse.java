package com.migu.tsg.microservice.atomicservice.composite.service.configfile.dto.list;

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
public class ConfigListResponse {

    @JsonProperty("count")
    private Integer totalCount;

    private Integer next;
    
    @JsonProperty("page_size")
    private Integer pageSize;

    @JsonProperty("num_pages")
    private Integer pageCount;

    private Integer previouse;
    
    @JsonProperty("results")
    private List<ConfigItem> results;
}
