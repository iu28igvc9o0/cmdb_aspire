package com.aspire.ums.cmdb.cmic.web;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.commons.util.IdUtils;
import org.springframework.web.bind.annotation.RestController;

import com.aspire.ums.cmdb.cmic.ICmdbIPManageOperationLogAPI;
import com.aspire.ums.cmdb.cmic.entity.CmdbOperationLog;
import com.aspire.ums.cmdb.cmic.service.ICmdbIPService;
import com.aspire.ums.cmdb.cmic.service.ICmdbOperationLogService;
import com.aspire.ums.cmdb.common.ResultVo;
import com.aspire.ums.cmdb.config.RequestAuthContext;
import com.aspire.ums.cmdb.config.RequestAuthContext.RequestHeadUser;
import com.aspire.ums.cmdb.util.UUIDUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author cuizhijun
 *
 */
@RestController
@Slf4j
public class CmdbIPManageOperationLogController implements ICmdbIPManageOperationLogAPI {

    @Autowired
    private ICmdbOperationLogService operationLogService;
   
	@Override
	public ResultVo<String> addOperationLog(String menuUrl) {

		ResultVo<String> result = ResultVo.success();
		try {
			RequestHeadUser user = RequestAuthContext.currentRequestAuthContext().getUser();
			log.info("user===={}", user.getUsername());
			result = operationLogService.addOperationLog(menuUrl, user.getUsername());
		} catch (Exception e) {
			log.error("添加操作日志失败", e);
			result = ResultVo.fail();
		}
		return result;
	}
  
  
}
