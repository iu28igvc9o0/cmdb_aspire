package com.aspire.ums.cmdb.common;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class Result<A> {
    private int totalSize;
    private List<A> data;
    private Map<String, Map<String, String>> columns;

    public Result() {}

    public Result(int totalSize, List<A> data) {
        this.totalSize = totalSize;
        this.data = data;
    }
    public Result(int totalSize, List<A> data, Map<String, Map<String, String>> columns) {
        this.totalSize = totalSize;
        this.data = data;
        this.columns = columns;
    }
}
