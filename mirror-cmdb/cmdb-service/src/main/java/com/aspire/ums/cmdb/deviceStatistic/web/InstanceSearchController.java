package com.aspire.ums.cmdb.deviceStatistic.web;

import com.aspire.ums.cmdb.allocate.payload.BizSysRequestBody;
import com.aspire.ums.cmdb.util.StringUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.aspire.ums.cmdb.common.PageBean;
import com.aspire.ums.cmdb.deviceStatistic.IInstanceSearchAPI;
import com.aspire.ums.cmdb.deviceStatistic.payload.InstanceSearchRequest;
import com.aspire.ums.cmdb.deviceStatistic.payload.InstanceSearchResp;
import com.aspire.ums.cmdb.deviceStatistic.service.InstanceSearchService;

import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: InstanceController
 * Date:     2019/3/12 15:43
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
 
@RestController
@Slf4j
public class InstanceSearchController implements IInstanceSearchAPI {
	
	 
    @Autowired
    private InstanceSearchService  instanceSearchService;
    

	@Override
	public PageBean<InstanceSearchResp> selectInstanceByPage(@RequestBody InstanceSearchRequest instanceSearchRequest) {
		 
		log.info("selectInstanceByPage {} ",instanceSearchRequest);
		
		return instanceSearchService.selectInstanceByPage(instanceSearchRequest);
		
		 
	}

	@Override
	public List<Map<String, String>> getAuthDeviceData(@RequestParam(value = "idcs", required = false) String idcs,
													   @RequestParam(value = "rooms", required = false) String rooms,
													   @RequestParam(value = "bisSyss", required = false) String bizSys,
													   @RequestParam(value = "deviceTypes", required = false) String
																   deviceType) {
		Map<String, List<String>> queryMap = Maps.newHashMap();
		if (!StringUtils.isEmpty(idcs)) {
			String[] idcArray = idcs.split(",");
			queryMap.put("idcArray", Arrays.asList(idcArray));
		}
		if (!StringUtils.isEmpty(rooms)) {
			String[] roomArray = rooms.split(",");
			queryMap.put("roomArray", Arrays.asList(roomArray));
		}
		if (!StringUtils.isEmpty(bizSys)) {
			String[] bizSysArray = bizSys.split(",");
			queryMap.put("bizSysArray", Arrays.asList(bizSysArray));
		}

		if (!StringUtils.isEmpty(deviceType)) {
			String[] deviceTypeArray = deviceType.split(",");
			queryMap.put("deviceTypeArray", Arrays.asList(deviceTypeArray));
		}
		if (queryMap.size() == 0) {
			return Lists.newArrayList();
		}
		return instanceSearchService.selectInstanceByAuth(queryMap);
	}

	@Override
	public Map<String, Object> selectDepartBizSystem(@RequestParam(value = "ip", required = false) String ip ,
                                                     @RequestParam(value = "bizSystem", required = false) String bizSystem) {
		log.info("ip is {} bizSystem is {} ",ip , bizSystem); 
		
		if(ip==null || ip.equals("")){
			return null;
		}
		
        if(bizSystem==null || bizSystem.equals("")){
			return null;
		}
		
		return instanceSearchService.selectDepartBizSystem(ip,bizSystem);
		
	}

	@Override
	public Map<String, Object> selectDepartBizSystemInfo(@RequestBody BizSysRequestBody requestBody) {
		return selectDepartBizSystem(requestBody.getIp(), requestBody.getBizSystem());
	}


}
