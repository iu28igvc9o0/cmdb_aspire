package com.migu.tsg.microservice.atomicservice.composite.service.rbac.payload;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author yangshilei
 * 用户拥有的view资源权限
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserViewAction {
	/**
	 * 资源操作权限
	 * 例：["role:view","*:delete"]
	 */
	public List<String> actions;
	
	/**
	 * 资源类型
	 * 例：role
	 */
	private String resource;
}