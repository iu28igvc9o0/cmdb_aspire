package com.aspire.ums.cmdb.cmic.service;

import com.aspire.ums.cmdb.common.ResultVo;

/**
 * cmdb操作日志类
 * @author cuizhijun
 *
 */
public interface ICmdbOperationLogService {

	/**
	 * 
	 * @param menuUrl 菜单
	 * @param username 操作帐号
	 * @return
	 */
	ResultVo<String> addOperationLog(String menuUrl, String username);

 
}
