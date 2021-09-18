package com.aspire.ums.cmdb.automate.payload.easyops;

import com.alibaba.fastjson.annotation.JSONField;
import com.aspire.ums.cmdb.automate.payload.base.BaseAutomateDTO;
import com.aspire.ums.cmdb.ipCollect.payload.vmware.EasyOpsDataDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author fanwenhui
 * @date 2020-08-24 11:39
 * @description
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AutomateEasyOpsResult<T extends BaseAutomateDTO> implements Serializable {
    private static final long serialVersionUID = 3461414672212198428L;

    @JsonProperty("code")
    @JSONField(name = "code")
    private Integer code;

    @JsonProperty("data")
    @JSONField(name = "data")
    private AutomateEasyOpsDataDTO<T> data;

    @JsonProperty("error")
    @JSONField(name = "error")
    private String error;

    @JsonProperty("message")
    @JSONField(name = "message")
    private String message;
}
