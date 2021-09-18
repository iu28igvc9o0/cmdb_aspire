package com.migu.tsg.microservice.atomicservice.rbac.dao.po;

import java.sql.Timestamp;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 项目名称: rbac-service <br>
 * 包: com.migu.tsg.microservice.atomicservice.rbac.dao.po <br>
 * 类名称: Role.java <br>
 * 类描述: 角色表对应的实体类 <br>
 * 创建人: WangSheng <br>
 * 创建时间: 2017年7月21日上午10:52:32<br>
 * 版本: v1.0
 */

@NoArgsConstructor
@Data
//@AllArgsConstructor
public class Role {

    /**
     * 角色UUID
     */
    private String uuid;

    /**
     * 角色名
     */
    private String name;

    /**
     * 空间名
     */
    private String namespace;

    /**
     * 是否为管理员角色
     */
    private int adminRole;

    /**
     * 创建时间
     */
    private Timestamp createdAt;

    /**
     * 修改时间
     */
    private Timestamp updatedAt;
    
    /** 
     * 角色类型
     */ 
    private int roleType;

    /** 
     * 角色描述
     */ 
    private String describe;
    
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

	public Role(String uuid, String name, String namespace, int adminRole,
			Timestamp createdAt, Timestamp updatedAt,int roleType,String describe) {
		this.uuid = uuid;
		this.name = name;
		this.namespace = namespace;
		this.adminRole = adminRole;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;

		this.roleType = roleType;
		this.describe = describe;
	}

    
}
