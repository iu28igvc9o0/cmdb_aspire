package com.aspire.mirror.elasticsearch.api.dto.cmdb;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result<A> {

    private int count;

    private List<A> data;
}
