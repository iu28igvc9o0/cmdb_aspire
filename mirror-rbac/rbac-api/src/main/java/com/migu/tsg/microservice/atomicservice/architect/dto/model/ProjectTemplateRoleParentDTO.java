/**
 * 
 */
package com.migu.tsg.microservice.atomicservice.architect.dto.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
* 项目名称: rbac-api <br>
* 包: com.migu.tsg.microservice.atomicservice.architect.dto.model <br>
* 类名称: ProjectTemplateRoleParentDTO.java <br>
* 类描述: 项目模板之父角色<br>
* 创建人: WangSheng <br>
* 创建时间: 2017年10月13日上午9:46:05 <br>
* 版本: v1.0
*/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectTemplateRoleParentDTO {
    /**
     * 节点ID对应资源ID，必须在同一个项目模版内是唯一的
     */
    private Integer item;
    
    /**
     * 父角色名称
     */
    private String name;
}
