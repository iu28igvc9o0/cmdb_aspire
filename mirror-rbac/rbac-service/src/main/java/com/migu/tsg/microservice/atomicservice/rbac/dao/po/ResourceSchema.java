package com.migu.tsg.microservice.atomicservice.rbac.dao.po;

import java.sql.Timestamp;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 项目名称: rbac-api <br>
 * 包: com.migu.tsg.microservice.atomicservice.order.dto <br>
 * 类名称: ResourceSchema.java <br>
 * 类描述: 资源模式(资源类型,资源操作,资源约束)实体类PO <br>
 * 创建人: WangSheng <br>
 * 创建时间: 2017年7月21日上午10:06:15<br>
 * 版本: v1.0
 */
@NoArgsConstructor
@Data
@AllArgsConstructor
public class ResourceSchema {

    /**
     * 资源类型
     */
    private String resource;

    /**
     * 是否为全局资源
     */
    private String general;

    /** 
     * 资源名称
     */ 
    private String name;
    
    /** 
     * 父资源
     */ 
    private String parentResource;

    /**
     * 创建时间
     */
    private Timestamp createdAt;

    /**
     * 资源操作集合
     */
    private List<ResourceSchemaActions> actions;

    /**
     * 资源字段约束
     */
    private List<ResourceSchemaConstraints> constraints;
    
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

    /** 
     * 方法重写 <br/>
     * 功能描述： 
     * <p>
     * @param obj
     * @return 
     * @see java.lang.Object#equals(java.lang.Object) 
     */
    @Override
    public boolean equals(Object obj) {
    	if (!this.getClass().isInstance(obj)) {
    		return false;
    	}
    	ResourceSchema other = ResourceSchema.class.cast(obj);
    	return this.resource == null ? false : this.resource.equals(other.resource);
    }
    
    /** 
     * 方法重写 <br/>
     * 功能描述： 
     * <p>
     * @return 
     * @see java.lang.Object#hashCode() 
     */
    @Override
    public int hashCode() {
    	int baseHash = 13;
    	return this.resource == null ? baseHash : baseHash + this.resource.hashCode();
    }
}
