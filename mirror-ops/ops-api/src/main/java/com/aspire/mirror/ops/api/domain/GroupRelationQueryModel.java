package com.aspire.mirror.ops.api.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * TODO
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.ops.api.domain
 * 类名称:    GroupRelationQueryModel.java
 * 类描述:    TODO
 * 创建人:    JinSu
 * 创建时间:  2020/3/5 14:34
 * 版本:      v1.0
 */
@Data
public class GroupRelationQueryModel extends OpsGroupRelation{
    @JsonProperty("object_name_like")
    @ApiModelProperty("对象名称")
    private String objectNameLike;

    @JsonProperty("group_name_like")
    @ApiModelProperty("分组名称")
    private String groupNameLike;


    @JsonProperty("object_name")
    @ApiModelProperty("对象名称")
    private String objectName;


    @JsonIgnore
    private List<Long> groupIdList;

    @JsonProperty("group_parent_id")
    private Long groupParentId;

    @JsonProperty("page_size")
    private Integer	pageSize;		// 如果为null， 则查询全部数据
    @JsonProperty("page_no")
    private Integer	pageNo;			// 从0开始

    public Integer getStartIdx() {
        if (pageSize == null) {
            return null;
        }
        return (pageNo == null || pageNo <= 0 ? 0 : pageNo - 1) * pageSize;
    }
}