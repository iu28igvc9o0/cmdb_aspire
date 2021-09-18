package com.migu.tsg.microservice.atomicservice.composite.controller.cmdb.cmic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.aspire.mirror.composite.service.cmdb.cmic.ICmdbIPManageOperationLogAPI;
import com.aspire.ums.cmdb.common.ResultVo;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb.cmic.ICmdbIPManageOperationLogClient;

import lombok.extern.slf4j.Slf4j;


@RestController
@Slf4j
public class CmdbIPManageOperationLogController implements ICmdbIPManageOperationLogAPI {

    @Autowired
    private ICmdbIPManageOperationLogClient ipManageOperationLogClient;

	@Override
	public ResultVo<String> addOperationLog(String menuUrl) {
		return ipManageOperationLogClient.addOperationLog(menuUrl);
	}

  
}
