package com.aspire.mirror.ops.api.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * 检查脚本敏感性
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.ops.api.domain
 * 类名称:    CheckScriptRequst.java
 * 类描述:    检查脚本敏感性
 * 创建人:    JinSu
 * 创建时间:  2020/2/17 14:31
 * 版本:      v1.0
 */
@Data
public class CheckScriptRequst {
    @JsonProperty("script_list")
    private List<OpsScript> scriptList;
}
