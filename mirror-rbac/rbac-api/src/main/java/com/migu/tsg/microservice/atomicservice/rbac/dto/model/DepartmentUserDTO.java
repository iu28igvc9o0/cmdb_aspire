package com.migu.tsg.microservice.atomicservice.rbac.dto.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 部门-人员
 *
 * @author jiangxuwen
 * @date 2020/12/11 15:52
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel
public class DepartmentUserDTO implements Serializable {

    private static final long serialVersionUID = -6582732205266420563L;

    /** 部门或用户的id */
    @ApiModelProperty(value = "id")
    @JsonProperty("uuid")
    private String uuid;

    /** 父级Id */
    @ApiModelProperty(value = "parentId")
    @JsonProperty("parent_id")
    private String parentId;

    /** 部门或人员名称 */
    @ApiModelProperty(value = "name")
    @JsonProperty("name")
    private String name;

    /** 用户名 */
    @ApiModelProperty(value = "code")
    @JsonProperty("code")
    private String code;

    /** 类型: user用户、department部门 */
    @ApiModelProperty(value = "type")
    @JsonProperty("type")
    private String type;
}
