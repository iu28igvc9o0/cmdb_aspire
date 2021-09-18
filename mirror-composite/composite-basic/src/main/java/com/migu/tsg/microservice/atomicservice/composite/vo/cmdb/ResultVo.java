package com.migu.tsg.microservice.atomicservice.composite.vo.cmdb;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class ResultVo<A> {
    private int totalSize;
    private List<A> data;
    private Map<String, Map<String, String>> columns;

    public ResultVo() {}

    public ResultVo(int totalSize, List<A> data) {
        this.totalSize = totalSize;
        this.data = data;
    }
    public ResultVo(int totalSize, List<A> data, Map<String, Map<String, String>> columns) {
        this.totalSize = totalSize;
        this.data = data;
        this.columns = columns;
    }
}
