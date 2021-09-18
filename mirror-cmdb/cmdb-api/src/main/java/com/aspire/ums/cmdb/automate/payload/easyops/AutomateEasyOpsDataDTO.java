package com.aspire.ums.cmdb.automate.payload.easyops;

import com.alibaba.fastjson.annotation.JSONField;
import com.aspire.ums.cmdb.automate.payload.base.BaseAutomateDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * @author fanwenhui
 * @date 2020-08-24 11:43
 * @description
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AutomateEasyOpsDataDTO <T extends BaseAutomateDTO> implements Serializable {
    private static final long serialVersionUID = -8680909138362485022L;

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
