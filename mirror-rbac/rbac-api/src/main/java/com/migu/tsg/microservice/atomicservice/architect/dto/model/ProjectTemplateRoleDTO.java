/**
 * 
 */
package com.migu.tsg.microservice.atomicservice.architect.dto.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
* 项目名称: rbac-api <br>
* 包: com.migu.tsg.microservice.atomicservice.architect.dto.model <br>
* 类名称: ProjectTemplateRoleDTO.java <br>
* 类描述: 项目模板之角色<br>
* 创建人: WangSheng <br>
* 创建时间: 2017年10月13日上午9:39:07 <br>
* 版本: v1.0
*/
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProjectTemplateRoleDTO {
    
    /**
     * 资源ID，必须在同一个项目模版内是唯一的
     */
    private Integer id;
    
    /**
     * 是否必须创建资源的选项
     */
    private Boolean required;
    
    /**
     * 资源名称，可以带 [name] placeholder 来自动分配项目名称
     */
    private String name;
    
    /**
     * 其它资源依赖关系，为json 数组保存，内容是其它资源ID字段 [1,2]
     */
    @JsonProperty("depends_on")
    private List<Integer> dependsOn;
    
    /**
     * 父角色集合
     */
    private List<ProjectTemplateRoleParentDTO> parents;
    
    /**
     * 权限集合
     */
    private List<ProjectTemplateRolePermissionDTO> permissions;
}
