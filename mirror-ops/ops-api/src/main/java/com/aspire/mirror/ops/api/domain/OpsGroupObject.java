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
 * 类名称:    OpsGroupObject.java
 * 类描述:    TODO
 * 创建人:    JinSu
 * 创建时间:  2020/3/11 10:55
 * 版本:      v1.0
 */
@Data
public class OpsGroupObject {
    @ApiModelProperty("分组ID列表")
    @JsonProperty("group_id_list")
    private List<Long> groupIdList;

    @ApiModelProperty("分组列表")
    @JsonProperty("group_relation_list")
    private List<GroupRelationDetail> groupRelationList;

}
