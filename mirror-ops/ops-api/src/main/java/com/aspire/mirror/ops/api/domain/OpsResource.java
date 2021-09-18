package com.aspire.mirror.ops.api.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

/**
 * TODO
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.ops.api.domain
 * 类名称:    OpsResource.java
 * 类描述:    TODO
 * 创建人:    JinSu
 * 创建时间:  2020/3/19 14:27
 * 版本:      v1.0
 */
@Data
@NoArgsConstructor
public class OpsResource {
    public static final String RESOURCE_TYPE_GROUP = "group";

    @JsonProperty("resource_id")
    @ApiModelProperty(value = "资源ID")
    private String resourceId;

    @JsonProperty("parent_id")
    @ApiModelProperty(value = "父ID")
    private String parentId;

    @JsonProperty("resource_name")
    @ApiModelProperty(value = "资源名称")
    private String resourceName;

    @JsonProperty("resource_type")
    @ApiModelProperty(value = "资源类型")
    private String resourceType;

    @ApiModelProperty(value = "子组列表")
    @JsonProperty("sub_resource_list")
    private List<OpsResource> subResourceList;

    public OpsResource(String resourceId, String parentId, String resourceName, String resourceType) {
        this.resourceId = resourceId;
        this.parentId = parentId;
        this.resourceName = resourceName;
        this.resourceType = resourceType;
    }

//    public List<OpsResource> getSubGroupList() {
//        return subResourceList.stream().filter(item -> item.getResourceType().equals(OpsResource.RESOURCE_TYPE_GROUP)).collect(Collectors.toList());
//    }
}
