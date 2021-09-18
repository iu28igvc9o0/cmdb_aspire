package com.aspire.ums.cmdb.cmic.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aspire.ums.cmdb.cmic.entity.CmdbOperationLog;
import com.aspire.ums.cmdb.cmic.mapper.CmdbOperationLogMapper;
import com.aspire.ums.cmdb.cmic.service.ICmdbOperationLogService;
import com.aspire.ums.cmdb.common.ResultVo;
import com.aspire.ums.cmdb.util.UUIDUtil;

@Service
public class CmdbOperationLogServiceImpl implements ICmdbOperationLogService {

	@Autowired
	private CmdbOperationLogMapper operationLogMapper;

	@Override
	public ResultVo<String> addOperationLog(String menuUrl, String username) {
		CmdbOperationLog cmdbOperationLog = new CmdbOperationLog();
		cmdbOperationLog.setId(UUIDUtil.getUUID());
		cmdbOperationLog.setCreateBy(username);
		cmdbOperationLog.setMenuUrl(menuUrl);
		String menuName = operationLogMapper.findMenuNameByUrl(menuUrl);
		cmdbOperationLog.setFunModel(menuName);
		cmdbOperationLog.setContent(String.format("点击了%s功能菜单", menuName));
		operationLogMapper.addOperationLog(cmdbOperationLog);
		return ResultVo.success();
	}

}
