package com.migu.tsg.microservice.atomicservice.architect.dto;

import java.util.List;
import com.migu.tsg.microservice.atomicservice.architect.dto.model.ProjectTemplateResourceDTO;
import com.migu.tsg.microservice.atomicservice.architect.dto.model.ProjectTemplateRoleDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
* 项目名称: architect-api <br>
* 包: com.migu.tsg.microservice.atomicservice.architect.dto <br>
* 类名称: CreateTemplateRequest.java <br>
* 类描述: 创建单个项目模板的请求对象<br>
* 创建人: WangSheng <br>
* 创建时间: 2017年9月26日下午6:06:11 <br>
* 版本: v1.0
*/
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreateProjectTemplateRequest {

    /**
     * 项目模板名称
     */
    private String name;

    /**
     * 项目模版声明的角色集合
     * 例子：http://internal-api-doc.alauda.cn/architect.html#templates_post
     */
    private List<ProjectTemplateRoleDTO> roles;

    /**
     * 项目模版声明的资源集合
     * 例子：http://internal-api-doc.alauda.cn/architect.html#templates_post
     */
    private List<ProjectTemplateResourceDTO> resources;

}
