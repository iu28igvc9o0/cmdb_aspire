package com.aspire.ums.cmdb.sync.service;

import java.util.List;

import com.aspire.ums.cmdb.sync.payload.PublicNetAndIntranetIPDTO;


public interface ICmdbNetPolicyManageService {


	/**
	 * 新增及删除公网采集旧数据
	 * @param publicAndInnerIpList
	 */
	public void batchInsertAndDelOldData(List<PublicNetAndIntranetIPDTO> publicAndInnerIpList);

	/**
	 * 更新ip地址库-公网ip存活状态
	 * @param publicAndInnerIpList
	 */
	public void updatePublicIPSurvivalStatus(List<PublicNetAndIntranetIPDTO> publicAndInnerIpList);
}
