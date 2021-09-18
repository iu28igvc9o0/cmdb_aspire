package com.migu.tsg.microservice.atomicservice.architect.dao.po;

import java.sql.Timestamp;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
* 项目名称: rbac-service <br>
* 包: com.migu.tsg.microservice.atomicservice.architect.dao.po <br>
* 类名称: ProjectTemplate.java <br>
* 类描述: 项目模版主要信息<br>
* 创建人: WangSheng <br>
* 创建时间: 2017年9月26日下午3:29:28 <br>
* 版本: v1.0
*/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectTemplate {

    /**
     * 模板UUID,uuid为PK
     */
    private String uuid;

    /**
     * 项目模版名称
     */
    private String name;

    /**
     * 创建时间
     */
    private Timestamp createdAt;

    /**
     * 更新时间
     */
    private Timestamp updatedAt;
    
    /**
     * 项目模版声明的资源集合
     */
    private List<ProjectTemplateItem> projectTemplateItems;
    
    public Timestamp getCreatedAt() {
        if (this.createdAt == null) {
            return null;
        }
        return (Timestamp) this.createdAt.clone();
    }

    public void setCreatedAt(final Timestamp createdAt) {
        if (createdAt == null) {
            this.createdAt = null;
        } else {
            this.createdAt = (Timestamp) createdAt.clone();
        }
    }
    
    public Timestamp getUpdatedAt() {
        if (this.updatedAt == null) {
            return null;
        }
        return (Timestamp) this.updatedAt.clone();
    }

    public void setUpdatedAt(final Timestamp updatedAt) {
        if (updatedAt == null) {
            this.updatedAt = null;
        } else {
            this.updatedAt = (Timestamp) updatedAt.clone();
        }
    }

}
