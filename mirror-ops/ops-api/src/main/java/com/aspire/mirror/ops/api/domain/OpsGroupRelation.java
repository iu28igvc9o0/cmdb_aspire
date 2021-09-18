package com.aspire.mirror.ops.api.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 分组关系实体
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.ops.api.domain
 * 类名称:    OpsGroupRelation.java
 * 类描述:    分组关系实体
 * 创建人:    JinSu
 * 创建时间:  2020/3/5 14:23
 * 版本:      v1.0
 */
@Data
@NoArgsConstructor
public class OpsGroupRelation {
    @ApiModelProperty("分组关系ID")
    @JsonProperty("group_relation_id")
    private Long groupRelationId;

    @ApiModelProperty("分组ID")
    @JsonProperty("group_id")
    private Long groupId;

    @ApiModelProperty("对象类型")
    @JsonProperty("object_type")
    private String objectType;

    @ApiModelProperty("对象ID")
    @JsonProperty("object_id")
    private Long objectId;

    public OpsGroupRelation(Long groupId, String objectType, Long objectId) {
        this.groupId = groupId;
        this.objectType = objectType;
        this.objectId = objectId;
    }
}
