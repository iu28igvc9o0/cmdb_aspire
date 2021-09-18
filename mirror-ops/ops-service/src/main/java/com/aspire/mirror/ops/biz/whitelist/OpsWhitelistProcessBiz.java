package com.aspire.mirror.ops.biz.whitelist;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aspire.mirror.ops.api.domain.GeneralResponse;
import com.aspire.mirror.ops.api.domain.PageListQueryResult;
import com.aspire.mirror.ops.api.domain.whitelist.OpsWhitelistConstraint;
import com.aspire.mirror.ops.api.domain.whitelist.OpsWhitelistHost;
import com.aspire.mirror.ops.api.domain.whitelist.OpsWhitelistHost.OpsWhitelistHostQueryParam;
import com.aspire.mirror.ops.api.domain.whitelist.WhitelistConst.WhitelistTypeEnum;
import com.aspire.mirror.ops.controller.authcontext.RequestAuthContext;
import com.aspire.mirror.ops.dao.OpsWhitelistDao;

/** 
 *
 * 项目名称: ops-service 
 * <p/>
 * 
 * 类名: OpsWhitelistProcessBiz
 * <p/>
 *
 * 类功能描述: 自动化流程白名单处理业务类
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
@Service
@Transactional
public class OpsWhitelistProcessBiz {
	@Autowired
	private OpsWhitelistDao whitelistDao;
	
	/** 
	 * 功能描述: 根据查询参数分页查询主机白名单列表  
	 * <p>
	 * @param queryParam
	 * @return
	 */
	@Transactional(readOnly=true)
	public PageListQueryResult<OpsWhitelistHost> queryWhitelistHostList(OpsWhitelistHostQueryParam queryParam) {
		PageListQueryResult<OpsWhitelistHost> pageResult = new PageListQueryResult<OpsWhitelistHost>();
		List<OpsWhitelistHost> queryList = whitelistDao.queryWhitelistHostList(queryParam);
		pageResult.setDataList(queryList);
		pageResult.setTotalCount(CollectionUtils.isEmpty(queryList) ? 0 : whitelistDao.queryWhitelistHostTotalCount(queryParam));
		return pageResult;
	}
	
	/** 
	 * 功能描述: 根据关键信息查询主机白名单  
	 * <p>
	 * @param queryParam
	 * @return
	 */
	@Transactional(readOnly=true)
	public OpsWhitelistHost queryOpsWhitelistHostByKeys(OpsWhitelistHost queryParam) {
		return whitelistDao.queryWhitelistHostById(queryParam.refreshIdByKeys());
	}
	
	/** 
	 * 功能描述: 保存主机白名单  
	 * <p>
	 * @param whitelistHost
	 * @return
	 */
	@Transactional
	public GeneralResponse saveWhitelistHost(OpsWhitelistHost whitelistHost) {
		Pair<Boolean, String> checkResult = whitelistHost.selfCheck();
		if (!checkResult.getLeft()) {
			return new GeneralResponse(false, checkResult.getRight());
		}
		
		String whitelistId = whitelistHost.refreshIdByKeys();
		OpsWhitelistHost existRecord = whitelistDao.queryWhitelistHostById(whitelistId);
		if (existRecord == null) {
			whitelistHost.setCreater(RequestAuthContext.getRequestHeadUserName());
			whitelistHost.setCreateTime(new Date());
			whitelistHost.setUpdater(whitelistHost.getCreater());
			whitelistHost.setLastUpdateTime(whitelistHost.getCreateTime());
			whitelistDao.insertOpsWhitelistHost(whitelistHost);
		} else {
			whitelistHost.setUpdater(RequestAuthContext.getRequestHeadUserName());
			whitelistHost.setLastUpdateTime(new Date());
			whitelistDao.updateOpsWhitelistHost(whitelistHost);
			whitelistDao.deleteWhitelistConstraintsByWhitelistTypeAndId(whitelistHost.getWhitelistType(), whitelistId);
		}
		
		List<OpsWhitelistConstraint> allConstraintList = new ArrayList<>();
		allConstraintList.addAll(whitelistHost.getScriptConstraintList());
		allConstraintList.addAll(whitelistHost.getPipelineConstraintList());
		if (CollectionUtils.isNotEmpty(allConstraintList)) {
			whitelistDao.batchInsertWhitelistConstraint(allConstraintList);
		}
		return new GeneralResponse(true, null, whitelistHost);
	}
	
	/** 
	 * 功能描述: 根据id移除主机白名单  
	 * <p>
	 * @param whitelistHostId
	 * @return
	 */
	@Transactional
	public void removeWhitelistHostById(String whitelistHostId) {
		whitelistDao.deleteWhitelistConstraintsByWhitelistTypeAndId(WhitelistTypeEnum.host, whitelistHostId);
		whitelistDao.deleteWhitelistHostById(whitelistHostId);
	}
}
