package com.aspire.ums.cmdb.automate.payload;

import com.alibaba.fastjson.annotation.JSONField;
import com.aspire.ums.cmdb.automate.payload.easyops.AutomateHostDataDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * @author fanwenhui
 * @date 2020-10-15 14:07
 * @description
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AutomateListDataDTO implements Serializable {
    private static final long serialVersionUID = 3574615409761237719L;

    @JsonProperty("total")
    @JSONField(name = "total")
    private int total;

    @JsonProperty("page")
    @JSONField(name = "page")
    private int page;

    @JsonProperty("page_size")
    @JSONField(name = "page_size")
    private int page_size;

    @JsonProperty("list")
    @JSONField(name = "list")
    private List<AutomateHostDataDTO> list;

}
