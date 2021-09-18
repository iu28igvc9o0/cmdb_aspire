package com.aspire.mirror.ops.api.domain;

import com.aspire.mirror.common.auth.GeneralAuthConstraintsAware;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.List;

/**
 * 分组实体
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.ops.api.domain
 * 类名称:    OpsGroup.java
 * 类描述:    分组实体
 * 创建人:    JinSu
 * 创建时间:  2020/3/5 11:38
 * 版本:      v1.0
 */
@Data
@EqualsAndHashCode(of= {"groupName"})
public class OpsGroup extends GeneralAuthConstraintsAware {
    @JsonProperty("group_id")
    @ApiModelProperty(value = "分组ID")
    private Long groupId;

    @JsonProperty("parent_id")
    @ApiModelProperty(value = "父ID")
    private Long parentId;

    @JsonProperty("group_name")
    @ApiModelProperty(value = "分组名称")
    private String groupName;

    @JsonProperty("group_desc")
    @ApiModelProperty(value = "分组描述")
    private String groupDesc;

    @JsonProperty("creater")
    @ApiModelProperty(value = "创建人")
    protected String	creater;

    @ApiModelProperty(value = "创建时间")
    @JsonProperty("create_time")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    protected Date createTime;

    @ApiModelProperty(value = "修改人")
    @JsonProperty("updater")
    protected String	updater;

    @ApiModelProperty(value = "修改时间")
    @JsonProperty("update_time")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    protected Date		updateTime;

    @ApiModelProperty(value = "子组列表")
    @JsonProperty("sub_group_list")
    private List<OpsGroup> subGroupList;

    @ApiModelProperty(value = "对象列表")
    @JsonProperty("object_list")
    private List<GroupRelationDetail> objectId;

    @ApiModelProperty(value = "是否可用")
    private boolean disabled = false;

    private boolean top=false;
}
