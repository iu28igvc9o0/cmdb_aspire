package com.aspire.ums.cmdb.module.service;

import javax.servlet.http.HttpServletRequest;

import com.aspire.ums.cmdb.util.BusinessException;

public interface IconService {
	void uploadIcon(HttpServletRequest request) throws BusinessException,Exception;
}
