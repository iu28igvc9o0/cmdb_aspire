package com.aspire.ums.cmdb.ipAudit.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * IP地址库公网IP
 * @since 2020年05月22日 15:18:22
 * @author auto
 * @version v1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CmdbIpRepositoryPublicIp {

	/**
	 * 资产标识
	 */
	private String id;
	/**
	 * 删除标识
	 */
	private Integer isDelete;
	/**
	 * IP
	 */
	private String ip;
	/**
	 * 地址用途
	 */
	private String ipUse;
	/**
	 * 存活状态
	 */
	private String survivalStatus;
	/**
	 * 首次存活时间
	 */
	private String firstSurvivalTime;
	/**
	 * 最近存活时间
	 */
	private String latestSurvivalTime;
	/**
	 * 申请人
	 */
	private String requestPerson;
	/**
	 * 申请工单
	 */
	private String requestProcessId;
	/**
	 * 申请时间
	 */
	private String requestTime;
	/**
	 * 使用期限(年)
	 */
	private String usefulLife;
	/**
	 * 归属网段地址
	 */
	private String networkSegmentOwner;
}