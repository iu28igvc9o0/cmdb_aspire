package com.migu.tsg.microservice.atomicservice.composite.dao.po;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 * 数据库表Resource_project的映射model
 * Project Name:composite-service
 * File Name:ResourceProject.java
 * Package Name:com.migu.tsg.microservice.atomicservice.composite.dao.po
 * ClassName: ResourceProject <br/>
 * date: 2017年10月3日 下午5:30:57 <br/>
 * 数据库表Resource_project的映射model
 * @author baiwp
 * @version 
 * @since JDK 1.6
 */
@NoArgsConstructor
@Data
public class ResourceProject {
    private String projectUuid;
    private String resourceUuid;
    private String belongsToProject;
}
