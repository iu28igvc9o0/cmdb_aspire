package com.aspire.mirror.ops.api.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang.StringUtils;

import java.util.List;
import java.util.Set;

/**
 * TODO
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.ops.api
 * 类名称:    OpsParamValue.java
 * 类描述:    TODO
 * 创建人:    JinSu
 * 创建时间:  2020/6/15 20:12
 * 版本:      v1.0
 */
@Data
@NoArgsConstructor
public class OpsParamValue {
    @JsonProperty("param_value_id")
    private Long paramValueId;
    @JsonProperty("param_code")
    private String paramCode;
    @JsonProperty("param_value")
    private String paramValue;
    @JsonProperty("step_instance_id")
    private Long stepInstanceId;
    @JsonProperty("pipeline_instance_id")
    private Long pipelineInstanceId;

    @JsonProperty("old_param_value")
    private String oldParamValue;
    /**
     * 是否已生效   0无效/1生效
     */
    @JsonProperty("is_valid")
    private String isValid;
    @JsonProperty("agent_ip")
    private String agentIp;

    @JsonProperty("device_user_name")
    private String deviceUserName;

    private Boolean isExportEffective = false;

    private Set<String> agentIpList;


    public OpsParamValue(String paramCode, String paramValue, Long stepInstanceId, Long pipelineInstanceId, String agentIp, String isValid, String oldParamValue, String deviceUserName) {
        this.paramCode = paramCode;
        this.paramValue = paramValue;
        this.stepInstanceId = stepInstanceId;
        this.pipelineInstanceId = pipelineInstanceId;
        this.agentIp = agentIp;
        this.isValid = isValid;
        this.oldParamValue = oldParamValue;
        if (StringUtils.isNotEmpty(deviceUserName)) {
            this.deviceUserName = deviceUserName;
        } else {
            this.deviceUserName = "root";
        }
    }
}
