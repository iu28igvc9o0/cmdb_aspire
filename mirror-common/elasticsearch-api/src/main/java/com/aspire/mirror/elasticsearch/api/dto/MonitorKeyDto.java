package com.aspire.mirror.elasticsearch.api.dto;

import java.util.List;

import lombok.Data;

/**
 * @author baiwp
 * @title: ItemIndexDto
 * @projectName mirror-common
 * @description: TODO
 * @date 2019/7/2714:12
 */
@Data
public class MonitorKeyDto {
    private String moniObject;
    private String units;
    private String valueType;//0是history3history_unit
    private String key;
    private List<String> keys;
    private String itemId;
    //多端口查询对象
    private List<MonitorKeyDto> moniList;
}
