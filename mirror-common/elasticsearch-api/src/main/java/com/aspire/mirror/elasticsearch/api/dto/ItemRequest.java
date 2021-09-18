package com.aspire.mirror.elasticsearch.api.dto;

import lombok.Data;

/**
 * @author baiwp
 * @title: ItemRequest
 * @projectName mirror-common
 * @description: TODO
 * @date 2019/7/2419:25
 */
@Data
public class ItemRequest {
    private String[] ips;
    private String deviceClass;
    private String deviceType;
    private String bizSystem;
    private String roomId;
    private String idcType;
    private String item;
    private boolean multiQuery;//是否查询key下面的所有端口
}
