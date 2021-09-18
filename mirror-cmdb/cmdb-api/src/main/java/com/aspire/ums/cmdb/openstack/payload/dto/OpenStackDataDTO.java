package com.aspire.ums.cmdb.openstack.payload.dto;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * xxxxxx
 *
 * @author jiangxuwen
 * @date 2020/3/9 15:26
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OpenStackDataDTO<T extends BaseOpenStackDTO> implements Serializable {

    private static final long serialVersionUID = -922739052173527081L;

    @JsonProperty("total")
    @JSONField(name = "total")
    private Integer totalCount;

    @JsonProperty("page")
    @JSONField(name = "page")
    private Integer currentPageNo;

    @JsonProperty("page_size")
    @JSONField(name = "page_size")
    private Integer pageSize;

    @JsonProperty("list")
    @JSONField(name = "list")
    private List<T> dataList;

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
}
