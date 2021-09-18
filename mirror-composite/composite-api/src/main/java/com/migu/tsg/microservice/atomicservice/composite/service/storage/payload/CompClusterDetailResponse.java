package com.migu.tsg.microservice.atomicservice.composite.service.storage.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompClusterDetailResponse {
    /**
     * cluster主键Id
     */
    private String clusterId;

    /**
     * 空间
     */
    private String zone;

    /**
     * 主机
     */
    private ArrayList<Node> nodes;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Node {
        private String hostname;

        private ArrayList<String> devices;

        //该节点剩余Node大小
        private long size;
    }
}
