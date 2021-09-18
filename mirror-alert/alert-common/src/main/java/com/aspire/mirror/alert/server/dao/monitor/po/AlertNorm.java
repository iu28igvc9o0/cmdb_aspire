package com.aspire.mirror.alert.server.dao.monitor.po;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author hewang
 * @version 1.0
 * @date 2021/3/11 20:54
 */
@Data
public class AlertNorm {

    @JsonProperty("id")
    private Integer id;

    @JsonProperty("user_name")
    private String userName;

    @JsonProperty("indicator_name")
    private String indicatorName;

    @JsonProperty("indicator_key")
    private String indicatorKey;

    @JsonProperty("is_sort")
    private Integer isSort;

    @JsonProperty("is_delete")
    private Integer isDelete;

}
