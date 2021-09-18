/**
 * 
 */
package com.migu.tsg.microservice.atomicservice.architect.dto.model;

import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
* 项目名称: rbac-api <br>
* 包: com.migu.tsg.microservice.atomicservice.architect.dto.model <br>
* 类名称: ProjectTemplateRolePermissionDTO.java <br>
* 类描述: 项目模板之角色权限<br>
* 创建人: WangSheng <br>
* 创建时间: 2017年10月13日上午9:49:34 <br>
* 版本: v1.0
*/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectTemplateRolePermissionDTO {

    /**
     * 资源操作
     * 例子：["service:*","service:create","service:update","service:view"]
     */
    private List<String> actions;

    /**
     * 资源名称 
     * 例子：["web","web*"]
     */
    private List<String> resource;

    /**
     * 资源字段约束
     * 例子：[{"res:cluster":"test", "res:project":"dev"},{"res:cluster":"dev", "res:project":"dev"}]
     */
    private List<Map<String, String>> constraints;
}
