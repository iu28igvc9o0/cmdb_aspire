package com.aspire.mirror.composite.service.template.payload;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * TODO
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.composite.service.template.payload
 * 类名称:    CompMonitorDetailResponse.java
 * 类描述:    TODO
 * 创建人:    JinSu
 * 创建时间:  2018/9/6 13:48
 * 版本:      v1.0
 */
@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class CompMonitorDetailResponse {
    @JsonProperty("name")
    private String itemName;

    @JsonProperty("type")
    private String type;

    @JsonProperty("key_")
    private String itemKey;

    @JsonProperty("delay")
    private String delay;

    @JsonProperty("history")
    private Integer history;

    @JsonProperty("value_type")
    private String valueType;

    @JsonProperty("units")
    private String units;

    @JsonProperty("data_type")
    private String dataType;

    @Override
    public boolean equals(Object obj) {
        if (!this.getClass().isInstance(obj)) {
            return false;
        }
        CompMonitorDetailResponse other = this.getClass().cast(obj);
        if (this.getItemKey() == null) {
            return false;
        }
        return this.getItemKey().equals(other.getItemKey());
    }

    @Override
    public int hashCode() {
        int hash = 17;
        if (this.getItemKey() != null) {
            hash += 13 * this.getItemKey().hashCode();
        }
        return hash;
    }
}

