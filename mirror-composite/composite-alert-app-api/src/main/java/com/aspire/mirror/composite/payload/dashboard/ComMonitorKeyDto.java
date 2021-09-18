package com.aspire.mirror.composite.payload.dashboard;

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
public class ComMonitorKeyDto {
    private String moniObject;
    private String units;
    private String valueType;//0是history3history_unit
    private String key;
    private List<String> keys;

    //itemId
    private String itemId;

    private List<ComMonitorKeyDto> moniList;
}
