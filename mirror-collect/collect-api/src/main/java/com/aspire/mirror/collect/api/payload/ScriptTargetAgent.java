package com.aspire.mirror.collect.api.payload;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * TODO
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.collect.api.payload
 * 类名称:    ScriptTargetAgent.java
 * 类描述:    TODO
 * 创建人:    JinSu
 * 创建时间:  2019/11/13 15:38
 * 版本:      v1.0
 */
@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ScriptTargetAgent {
    @JsonProperty("agent_host")
    private String agentHost;

    @JsonProperty("result_message")
    private ObjectItemInfo resultMessage;
}
