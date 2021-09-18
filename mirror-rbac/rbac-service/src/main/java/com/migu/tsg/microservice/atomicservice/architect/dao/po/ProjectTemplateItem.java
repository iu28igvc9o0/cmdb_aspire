package com.migu.tsg.microservice.atomicservice.architect.dao.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
* 项目名称: architect-service <br>
* 包: com.migu.tsg.microservice.atomicservice.architect.dao.po <br>
* 类名称: ProjectTemplateItem.java <br>
* 类描述: 项目模版声明的资源<br>
* 创建人: WangSheng <br>
* 创建时间: 2017年9月26日下午3:32:36 <br>
* 版本: v1.0
*/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectTemplateItem {

    /**
     * 项目模版uuid
     */
    private String templateUuid;
    

    /**
     * 资源ID，必须在同一个项目模版内是唯一的
     */
    private Integer id;
    
    /**
     * 资源名称，可以带 [name] placeholder 来自动分配项目名称
     */
    private String name;
    
    /**
     * 是否必须创建资源的选项
     * 0:false,1:true
     */
    private Integer required;
    
    /**
     * 其它资源依赖关系，为json 数组保存，内容是其它资源ID字段 [1,2]
     */
    private String dependsOn;
    
    /**
     * 资源类型，支持 role或者resource
     */
    private String itemType;
    
    /**
     * 资源类型payload信息 为json保存，role和resource格式不同
     */
    private String itemData;

}
