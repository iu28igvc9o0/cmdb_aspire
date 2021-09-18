/**
 * 
 */
package com.migu.tsg.microservice.atomicservice.architect.dao.po;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
* 项目名称: rbac-service <br>
* 包: com.migu.tsg.microservice.atomicservice.architect.dao.po <br>
* 类名称: Project.java <br>
* 类描述: 项目entity<br>
* 创建人: WangSheng <br>
* 创建时间: 2017年10月1日下午3:12:59 <br>
* 版本: v1.0
*/

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Project {

    /**
     * 项目UUID
     */
    private String uuid;
    
    /**
     * 项目名称
     */
    private String name;
    
    /**
     * 根账号
     */
    private String namespace;
    
    /**
     * 项目模板名称
     */
    private String template;
    
    /**
     * 项目模板UUID
     */
    private String templateUuid;
    
    /**
     * 项目状态：pending，creating，success，deleting，error
     */
    private String status;
    
    /**
     * 创建时间
     */
    private Timestamp createdAt;

    /**
     * 更新时间
     */
    private Timestamp updatedAt;
    
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
