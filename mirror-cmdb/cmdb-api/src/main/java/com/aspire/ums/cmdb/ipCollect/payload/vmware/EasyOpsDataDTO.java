package com.aspire.ums.cmdb.ipCollect.payload.vmware;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * xxxxxx
 *
 * @author jiangxuwen
 * @date 2020/3/9 15:26
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class EasyOpsDataDTO<T extends BaseVmwareDTO> implements Serializable {

    private static final long serialVersionUID = -7290333145740657356L;
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

}
