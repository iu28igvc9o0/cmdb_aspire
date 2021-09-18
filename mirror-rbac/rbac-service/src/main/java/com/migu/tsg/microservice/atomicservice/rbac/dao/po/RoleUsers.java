package com.migu.tsg.microservice.atomicservice.rbac.dao.po;

import java.sql.Timestamp;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 项目名称: rbac-api <br>
 * 包: com.migu.tsg.microservice.atomicservice.rbac.dto <br>
 * 类名称: RoleUsers.java <br>
 * 类描述: 角色成员列表PO <br>
 * 创建人: WangSheng <br>
 * 创建时间: 2017年8月7日上午10:20:55<br>
 * 版本: v1.0
 */
@NoArgsConstructor
@Data
public class RoleUsers {

    /**
     * 角色UUID
     */
    private String roleUuid;

    /**
     * 角色名称
     */
    private String roleName;

    /**
     * 空间名
     */
    private String namespace;

    /**
     * 成员名称
     */
    private String username;

    /**
     * 分配时间
     */
    private Timestamp assignedAt;
    
    public Timestamp getAssignedAt() {
        if (this.assignedAt == null) {
            return null;
        }
        return (Timestamp) this.assignedAt.clone();
    }

    public void setAssignedAt(final Timestamp assignedAt) {
        if (assignedAt == null) {
            this.assignedAt = null;
        } else {
            this.assignedAt = (Timestamp) assignedAt.clone();
        }
    }

	public RoleUsers(String roleUuid, String roleName, String namespace,
			String username, Timestamp assignedAt) {
		this.roleUuid = roleUuid;
		this.roleName = roleName;
		this.namespace = namespace;
		this.username = username;
		this.assignedAt = assignedAt;
	}

    
}
