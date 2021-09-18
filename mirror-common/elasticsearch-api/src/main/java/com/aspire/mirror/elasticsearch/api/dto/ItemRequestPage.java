package com.aspire.mirror.elasticsearch.api.dto;

import lombok.Data;

/**
 * TODO
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.elasticsearch.api.dto
 * 类名称:    ItemRequestPage.java
 * 类描述:    TODO
 * 创建人:    JinSu
 * 创建时间:  2019/9/4 11:39
 * 版本:      v1.0
 */
@Data
public class ItemRequestPage extends ItemRequest{
    private String moniObject;

    private Integer pageNum;

    private Integer pageSize;

    private String name;
}
