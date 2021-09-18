package com.aspire.mirror.template.api.dto;

import lombok.Data;

import java.util.Map;

/**
 * TODO
 * <p>
 * 项目名称:  mirror平台
 * 类名称:    ItemSyncRequest
 * 类描述:    TODO
 * 创建人:    JinSu
 * 创建时间:  2020/11/16 20:31
 * 版本:      v1.0
 */
@Data
public class ItemSyncRequest {
    private String proxyIdentity;
    private Map<String, String> itemIdMap;
}
