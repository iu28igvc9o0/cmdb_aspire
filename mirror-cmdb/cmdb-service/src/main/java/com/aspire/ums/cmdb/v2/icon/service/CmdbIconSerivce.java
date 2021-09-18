package com.aspire.ums.cmdb.v2.icon.service;

import com.aspire.ums.cmdb.util.BusinessException;

import javax.servlet.http.HttpServletRequest;

public interface CmdbIconSerivce {
    void uploadIcon(HttpServletRequest request) throws BusinessException,Exception;

}
