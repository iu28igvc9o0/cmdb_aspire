package com.migu.tsg.microservice.atomicservice.composite;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PaginateResponse {
    @JsonProperty("count")
    private Integer totalCount;

    @JsonProperty("page_size")
    private Integer pageSize;

    @JsonProperty("num_pages")
    private Integer pageCount;

    @JsonProperty("results")
    List<?> results;
}
