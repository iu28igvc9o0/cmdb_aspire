package com.aspire.ums.cmdb.sync.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.aspire.ums.cmdb.sync.payload.PublicNetAndIntranetIPDTO;

/**
 * 描述：
 * 
 * @author
 * @date 2020-05-20 09:16:01
 */
@Mapper
public interface CmdbNetPolicyManageMapper {

	/**
	 * 批量插入数据
	 * @param publicAndInnerIpList
	 */
	void batchInsert(List<PublicNetAndIntranetIPDTO> publicAndInnerIpList);

	/**
	 * 删除旧的数据
	 */
	void delOldData();

	/**
	 * 根据公网ip采集数据查询ip地址库-公网ip存活信息
	 * @param publicAndInnerIpList
	 * @return
	 */
	List<Map<String, String>> getIpRepositoryPublicIpSurvivalInfo(List<PublicNetAndIntranetIPDTO> publicAndInnerIpList);

	/**
	 * 更新ip地址库-公网ip存活状态
	 * @param survivalInfolist
	 */
	void updatePublicIpSurvivalStatus(List<Map<String, String>> survivalInfolist);

   
}
