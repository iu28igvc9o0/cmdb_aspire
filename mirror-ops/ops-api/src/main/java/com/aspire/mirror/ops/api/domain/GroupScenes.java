package com.aspire.mirror.ops.api.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * TODO
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.ops.api.domain
 * 类名称:    GroupScenes.java
 * 类描述:    TODO
 * 创建人:    JinSu
 * 创建时间:  2020/3/13 18:45
 * 版本:      v1.0
 */
@Data
public class GroupScenes {
    @JsonProperty("group_id")
    @ApiModelProperty("分组ID")
    private Long groupId;

    @JsonProperty("group_name")
    @ApiModelProperty("分组名称")
    private String groupName;

    @JsonProperty("scenes_list")
    @ApiModelProperty("场景列表")
    private List<OpsPipelineScenes> scenesList;

}
