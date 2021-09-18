package com.aspire.ums.cmdb.ipAudit.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * IP地址库IPv6
 * @since 2020年05月22日 15:18:21
 * @author huanggongrui
 * @version v1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CmdbIpRepositoryIpv6Ip {

	/**
	 * 资产标识
	 */
	private String id;
	/**
	 * 删除标识
	 */
	private Integer isDelete;
	/**
	 * IPV6
	 */
	private String ipv6;
	/**
	 * 分配状态
	 */
	private String assignStatus;
	/**
	 * 分配人
	 */
	private String assignUser;
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
	 * 是否录入CMDB
	 */
	private String isCmdbManager;
	/**
	 * 独立业务
	 */
	private String onlineBusiness;
	/**
	 * 业务子模块
	 */
	private String subBusinessModule;
	/**
	 * 归属网段地址
	 */
	private String networkSegmentOwner;
}