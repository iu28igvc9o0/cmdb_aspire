package com.migu.tsg.microservice.atomicservice.desk.dao.po;

import java.sql.Timestamp;
import java.util.Date;

import com.migu.tsg.microservice.atomicservice.architect.dao.po.Project;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
* 项目名称: rbac-service <br>
* 包: com.migu.tsg.microservice.atomicservice.desk.dao.po<br>
* 类名称: WorkCommonOperations.java <br>
* 类描述: 服务台-常用操作类<br>
* 创建人: tongzhihong <br>
* 创建时间: 2020年09月16日下午3:12:59 <br>
* 版本: v1.0
*/

@NoArgsConstructor
@AllArgsConstructor
@Data
public class WorkCommonOperations {
    private Integer id;

    private Date createTime;

    private String userId;

    private Integer classifyId;

    private String linkUrl;

    private String name;

    private String logo;

    private String isExternal;

    private Integer sort;

}