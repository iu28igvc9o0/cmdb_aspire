package com.migu.tsg.microservice.atomicservice.composite.service.jobs.payload;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class ListJobRequest {
    // 主键ID
    private String uuids;

    // 模糊搜索关键词
    private String search;

    // 当前页
    @JsonProperty("page")
    private Integer pageNo;
    // 每页大小
    @JsonProperty("page_size")
    private Integer pageSize;

}
