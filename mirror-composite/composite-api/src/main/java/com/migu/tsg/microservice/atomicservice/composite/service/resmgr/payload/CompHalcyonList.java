package com.migu.tsg.microservice.atomicservice.composite.service.resmgr.payload;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@JsonInclude(Include.NON_NULL)
public class CompHalcyonList {
    @JsonProperty("count")
    private Integer totalCount;

    @JsonProperty("page_size")
    private Integer pageSize;

    @JsonProperty("num_pages")
    private Integer pageCount;

    @JsonProperty("results")
    private List<CompHalcyonResourcesResponse> result;
}
