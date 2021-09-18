package com.aspire.mirror.ops.api.domain;

import com.aspire.mirror.ops.api.util.JsonUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * 步骤历史
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.ops.api.domain
 * 类名称:    OpsStepHis
 * 类描述:    步骤历史
 * 创建人:    JinSu
 * 创建时间:  2020/9/11 14:59
 * 版本:      v1.0
 */
@Data
public class OpsStepHis extends OpsStepDTO{
    private Long id;

    @JsonProperty("pipeline_his_id")
    private Long pipelineHisId;

    @JsonProperty("script_his_id")
    private Long scriptHisId;


    @JsonIgnore
    public String getFileSourceJson() {
        return fileSource == null ? null : JsonUtil.toJacksonJson(fileSource);
    }

    public void setFileSourceJson(String filesourceString) {
        if (StringUtils.isBlank(filesourceString)) {
            this.fileSource = null;
            return;
        }
        TypeReference<List<OpsFileSource>> typeRef = new TypeReference<List<OpsFileSource>>(){};
        this.fileSource = JsonUtil.jacksonConvert(filesourceString, typeRef);
    }

    @JsonIgnore
    public String getTargetExecObjectJson() {
        return targetExecObject == null ? null : JsonUtil.toJacksonJson(targetExecObject);
    }

    public void setTargetExecObjectJson(String targetExecObjectString) {
        if (StringUtils.isBlank(targetExecObjectString)) {
            this.targetExecObject = null;
            return;
        }
        TypeReference<List<TargetExecObject>> typeRef = new TypeReference<List<TargetExecObject>>(){};
        this.targetExecObject = JsonUtil.jacksonConvert(targetExecObjectString, typeRef);
    }
    @JsonIgnore
    public String getTargetHostsJson() {
        return targetHosts == null ? null : JsonUtil.toJacksonJson(targetHosts);
    }

    public void setTargetHostsJson(String targetHostsString) {
        if (StringUtils.isBlank(targetHostsString)) {
            this.targetHosts = null;
            return;
        }
        TypeReference<List<String>> typeRef = new TypeReference<List<String>>(){};
        this.targetHosts = JsonUtil.jacksonConvert(targetHostsString, typeRef);
    }

    @JsonIgnore
    public String getFileStoreSourceJson() {
        return fileStoreSource == null ? null : JsonUtil.toJacksonJson(fileStoreSource);
    }

    public void setFileStoreSourceJson(String fileStoreSource) {
        if (StringUtils.isBlank(fileStoreSource)) {
            this.fileStoreSource = null;
            return;
        }
        TypeReference<List<String>> typeRef = new TypeReference<List<String>>(){};
        this.fileStoreSource = JsonUtil.jacksonConvert(fileStoreSource, typeRef);
    }

    @JsonIgnore
    public String getReplaceAttrListJson() {
        return replaceAttrList == null ? null : JsonUtil.toJacksonJson(replaceAttrList);
    }

    public void setReplaceAttrListJson(String replaceAttrListJson) {
        if (StringUtils.isBlank(replaceAttrListJson)) {
            this.replaceAttrList = null;
            return;
        }
        TypeReference<List<ReplaceAttrDefine>> typeRef = new TypeReference<List<ReplaceAttrDefine>>(){};
        this.replaceAttrList = JsonUtil.jacksonConvert(replaceAttrListJson, typeRef);
    }

}
