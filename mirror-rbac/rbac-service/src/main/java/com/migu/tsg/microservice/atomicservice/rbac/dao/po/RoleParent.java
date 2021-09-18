package com.migu.tsg.microservice.atomicservice.rbac.dao.po;

import java.sql.Timestamp;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 项目名称: rbac-service <br>
 * 包: com.migu.tsg.microservice.atomicservice.rbac.dao.po <br>
 * 类名称: RoleParents.java <br>
 * 类描述: 一个角色对应多个父角色的中间表的实体类 <br>
 * 创建人: WangSheng <br>
 * 创建时间: 2017年7月22日下午4:07:42 <br>
 * 版本: v1.0
 */

@NoArgsConstructor
@Data
public class RoleParent {

    /**
     * 角色UUID
     */
    private String roleUuid;

    /**
     * 父角色UUID
     */
    private String parentUuid;

    /**
     * 父角色名
     */
    private String parentName;

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

	public RoleParent(String roleUuid, String parentUuid, String parentName,
			Timestamp assignedAt) {
		this.roleUuid = roleUuid;
		this.parentUuid = parentUuid;
		this.parentName = parentName;
		this.assignedAt = assignedAt;
	}

    
}
