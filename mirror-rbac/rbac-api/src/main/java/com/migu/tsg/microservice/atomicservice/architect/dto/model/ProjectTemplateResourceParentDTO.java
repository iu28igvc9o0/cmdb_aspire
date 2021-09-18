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
* 类描述: 项目模板之父资源<br>
* 创建人: WangSheng <br>
* 创建时间: 2017年10月13日上午9:46:05 <br>
* 版本: v1.0
*/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectTemplateResourceParentDTO {
    /**
     * 父资源类型
     */
    private String type;
    
    /**
     * 父资源名称
     */
    private String name;
    
    /**
     * 父资源UUID
     */
    private String uuid;
    
    /**
     * 资源详细
     * 例子："http://dev-registry:5000"
     */
    private String details;
}
