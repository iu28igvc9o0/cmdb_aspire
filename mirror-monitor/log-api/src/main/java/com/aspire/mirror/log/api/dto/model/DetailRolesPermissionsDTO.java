package com.aspire.mirror.log.api.dto.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 事件明细角色权限信息
 * 项目名称: 微服务运维平台（log-api 模块）
 * 包: com.migu.tsg.microservice.monitor.log.dto.model
 * 类名称: DetailRolesPermissionsDTO.java
 * 类描述:  事件明细角色权限信息
 * 创建人: sunke
 * 创建时间: 2017年8月11日 上午10:37:08
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class DetailRolesPermissionsDTO {

	private String actions;

	private DetailRolesPermissionsConstraintsDTO constraints;

	private String resource;

}
