package com.aspire.mirror.ops.api.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

/**
 * 工作场景实体
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.ops.api.domain
 * 类名称:    OpsPipelineScenes.java
 * 类描述:    工作场景实体
 * 创建人:    JinSu
 * 创建时间:  2020/3/13 16:36
 * 版本:      v1.0
 */
@Data
public class OpsPipelineScenes extends OpsGroupObject{

    @JsonProperty("pipeline_scenes_id")
    @ApiModelProperty("作业场景ID")
    private Long pipelineScenesId;

    @JsonProperty("pipeline_id")
    @ApiModelProperty("作业ID")
    private Long pipelineId;

    @JsonProperty("scenes_name")
    @ApiModelProperty("场景名称")
    private String scenesName;

    @JsonProperty("scenes_desc")
    @ApiModelProperty("场景扩展")
    private String scenesDesc;

    @JsonProperty("scenes_picture")
    @ApiModelProperty("场景图")
    private String scenesPicture;

    @JsonProperty("creater")
    protected String	creater;

    @JsonProperty("create_time")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    protected Date createTime;

    @JsonProperty("updater")
    protected String	updater;

    @JsonProperty("update_time")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    protected Date		updateTime;

    @JsonProperty("group_id_string")
    protected String groupIdString;
}
