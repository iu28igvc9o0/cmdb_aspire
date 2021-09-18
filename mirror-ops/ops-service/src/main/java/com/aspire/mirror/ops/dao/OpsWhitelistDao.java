package com.aspire.mirror.ops.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.aspire.mirror.ops.api.domain.whitelist.OpsWhitelistConstraint;
import com.aspire.mirror.ops.api.domain.whitelist.OpsWhitelistHost;
import com.aspire.mirror.ops.api.domain.whitelist.OpsWhitelistHost.OpsWhitelistHostQueryParam;
import com.aspire.mirror.ops.api.domain.whitelist.OpsWhitelistCruiseCheck;
import com.aspire.mirror.ops.api.domain.whitelist.OpsWhitelistCruiseCheck.OpsWhitelistCruiseCheckQueryParam;
import com.aspire.mirror.ops.api.domain.whitelist.OpsWhitelistVulnerability;
import com.aspire.mirror.ops.api.domain.whitelist.OpsWhitelistVulnerability.OpsWhitelistVulnerabilityQueryParam;
import com.aspire.mirror.ops.api.domain.whitelist.OpsWhitelistBaseline;
import com.aspire.mirror.ops.api.domain.whitelist.OpsWhitelistBaseline.OpsWhitelistBaselineQueryParam;
import com.aspire.mirror.ops.api.domain.whitelist.WhitelistConst.WhitelistTypeEnum;


/** 
 *
 * 项目名称: ops-service 
 * <p/>
 * 
 * 类名: OpsWhitelistDao
 * <p/>
 *
 * 类功能描述: 自动化白名单DAO
 * <p/>
 *
 * @author	pengguihua
 *
 * @date	2021年3月6日  
 *
 * @version	V1.0 
 * <br/>
 *
 * <b>Copyright(c)</b> 2021 卓望公司-版权所有 
 *
 */
@Mapper
public interface OpsWhitelistDao {
	
	public List<OpsWhitelistHost> queryWhitelistHostList(OpsWhitelistHostQueryParam queryParam);
	
	public Integer queryWhitelistHostTotalCount(OpsWhitelistHostQueryParam queryParam);
	
	public void insertOpsWhitelistHost(OpsWhitelistHost whitelistHost);
	
	public void updateOpsWhitelistHost(OpsWhitelistHost whitelistHost);
	
	public OpsWhitelistHost queryWhitelistHostById(@Param("id") String id);
	
	public void deleteWhitelistHostById(@Param("id") String id);
	
	public void batchInsertWhitelistConstraint(List<OpsWhitelistConstraint> constraintList); 	//共用方法：批量写入白名单约束记录
	
	public List<OpsWhitelistConstraint> queryWhitelistConstraintListByTypeAndId(
			@Param("whitelistType") WhitelistTypeEnum whitelistType, @Param("whitelistId") String whitelistId);
	
	public List<OpsWhitelistHost> queryActiveWhiteListHostListByConstraintVal(OpsWhitelistConstraint constraint); 
	
	public void deleteWhitelistConstraintsByWhitelistTypeAndId(
			@Param("whitelistType") WhitelistTypeEnum whitelistType, @Param("whitelistId") String whitelistId); //共用方法：批量删除白名单约束记录
	
	/** 
	 * 功能描述: 自动化巡检白名单DAO  
	 * <p>
	 * @param queryParam
	 * @return
	 */
	public List<OpsWhitelistCruiseCheck> queryWhitelistCruiseCheckList(OpsWhitelistCruiseCheckQueryParam queryParam);
	
	public Integer queryWhitelistCruiseCheckTotalCount(OpsWhitelistCruiseCheckQueryParam queryParam);
	
	public void insertOpsWhitelistCruiseCheck(OpsWhitelistCruiseCheck whitelistCruiseCheck);
	
	public void updateOpsWhitelistCruiseCheck(OpsWhitelistCruiseCheck whitelistCruiseCheck);
	
	public OpsWhitelistCruiseCheck queryWhitelistCruiseCheckById(@Param("id") String id);
	
	public void deleteWhitelistCruiseCheckById(@Param("id") String id);
	
	public List<OpsWhitelistCruiseCheck> queryActiveWhiteListCruiseCheckListByConstraintVal(OpsWhitelistConstraint constraint);

	
	/** 
	 * 功能描述: 漏洞白名单DAO  
	 * <p>
	 * @param queryParam
	 * @return
	 */
	public List<OpsWhitelistVulnerability> queryWhitelistVulnerabilityList(OpsWhitelistVulnerabilityQueryParam queryParam);
	
	public Integer queryWhitelistVulnerabilityTotalCount(OpsWhitelistVulnerabilityQueryParam queryParam);
	
	public void insertOpsWhitelistVulnerability(OpsWhitelistVulnerability whitelistVulnerability);
	
	public void updateOpsWhitelistVulnerability(OpsWhitelistVulnerability whitelistVulnerability);
	
	public OpsWhitelistVulnerability queryWhitelistVulnerabilityById(@Param("id") String id);
	
	public void deleteWhitelistVulnerabilityById(@Param("id") String id);
	
	public List<OpsWhitelistVulnerability> queryActiveWhiteListVulnerabilityListByConstraintVal(OpsWhitelistConstraint constraint);
	
	
	
	/** 
	 * 功能描述: 基线黑名单DAO  
	 * <p>
	 * @param queryParam
	 * @return
	 */
	public List<OpsWhitelistBaseline> queryWhitelistBaselineList(OpsWhitelistBaselineQueryParam queryParam);
	
	public Integer queryWhitelistBaselineTotalCount(OpsWhitelistBaselineQueryParam queryParam);
	
	public void insertOpsWhitelistBaseline(OpsWhitelistBaseline whitelistBaseline);
	
	public void updateOpsWhitelistBaseline(OpsWhitelistBaseline whitelistBaseline);
	
	public OpsWhitelistBaseline queryWhitelistBaselineById(@Param("id") String id);
	
	public void deleteWhitelistBaselineById(@Param("id") String id);
	
	public List<OpsWhitelistBaseline> queryActiveWhiteListBaselineListByConstraintVal(OpsWhitelistConstraint constraint);

	

	
	
	
}
