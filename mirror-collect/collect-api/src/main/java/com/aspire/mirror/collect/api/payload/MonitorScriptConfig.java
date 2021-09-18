package com.aspire.mirror.collect.api.payload;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;

/**
 * TODO
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.collect.api.payload
 * 类名称:    MonitorScriptConfig.java
 * 类描述:    TODO
 * 创建人:    JinSu
 * 创建时间:  2019/11/13 14:28
 * 版本:      v1.0
 */
@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class MonitorScriptConfig {
    @JsonProperty("script_id")
    private String scriptId;

    @JsonProperty("script_param")
    private String scriptParam;

    @JsonProperty("customize_param")
    private String customizeParam;

    @JsonProperty("timeout")
    private Integer timeout;

    @JsonProperty(value = "callback_type")
    private String callbackType = "kafka";

    @JsonProperty(value = "callback_target")
    private String callbackTarget = "topic_script_collect";

    @JsonProperty(value="target_ops_user")
    private String targetOpsUser = "aspire";

    @JsonProperty("target_agent_list")
    private List<ScriptTargetAgent> targetAgentList;

}
