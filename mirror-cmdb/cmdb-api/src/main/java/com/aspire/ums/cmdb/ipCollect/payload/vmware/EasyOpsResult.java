package com.aspire.ums.cmdb.ipCollect.payload.vmware;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * 云管VMware.
 *
 * @author jiangxuwen
 * @date 2020/3/9 15:25
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class EasyOpsResult<T extends BaseVmwareDTO> implements Serializable {

    private static final long serialVersionUID = -4491122201473139077L;
    @JsonProperty("code")
    @JSONField(name = "code")
    private Integer code;

    @JsonProperty("data")
    @JSONField(name = "data")
    private EasyOpsDataDTO<T> data;

    @JsonProperty("error")
    @JSONField(name = "error")
    private String error;

    @JsonProperty("message")
    @JSONField(name = "message")
    private String message;

}
