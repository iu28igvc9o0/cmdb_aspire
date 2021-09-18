package com.aspire.ums.cmdb.automate.payload;

import com.alibaba.fastjson.annotation.JSONField;
import com.aspire.ums.cmdb.automate.payload.easyops.AutomateHostDataDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author fanwenhui
 * @date 2020-08-24 11:15
 * @description 自动化主机实例
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AutomateHostDTO implements Serializable {
    @JsonIgnore
    @JSONField(serialize = false)
    private static final long serialVersionUID = -3481500592284303246L;

    @JsonProperty("code")
    @JSONField(name = "code")
    private int code;

    @JsonProperty("error")
    @JSONField(name = "error")
    private String error;

    @JsonProperty("message")
    @JSONField(name = "message")
    private String message;

    @JsonProperty("data")
    @JSONField(name = "data")
    private AutomateListDataDTO data;

    @JsonIgnore
    @JSONField(serialize = false)
    private transient String requestBody;
}
