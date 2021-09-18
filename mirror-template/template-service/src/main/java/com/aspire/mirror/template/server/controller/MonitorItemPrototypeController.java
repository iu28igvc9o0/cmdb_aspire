package com.aspire.mirror.template.server.controller;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.aspire.mirror.common.entity.PageResponse;
import com.aspire.mirror.template.api.dto.GeneralResponse;
import com.aspire.mirror.template.api.dto.model.MonitorItemPrototype;
import com.aspire.mirror.template.api.dto.model.MonitorItemPrototype.MonitorItemPrototypeQueryModel;
import com.aspire.mirror.template.api.service.IMonitorItemPrototypeService;
import com.aspire.mirror.template.server.biz.MonitorItemPrototypeBiz;

/** 
 *
 * 项目名称: template-service 
 * <p/>
 * 
 * 类名: MonitorItemPrototypeController
 * <p/>
 *
 * 类功能描述: TODO
 * <p/>
 *
 * @author	pengguihua
 *
 * @date	2021年1月27日  
 *
 * @version	V1.0 
 * <br/>
 *
 * <b>Copyright(c)</b> 2021 卓望公司-版权所有 
 *
 */
@RestController
public class MonitorItemPrototypeController implements IMonitorItemPrototypeService {
	
	@Autowired
	private MonitorItemPrototypeBiz biz;
	
	@Override
	public GeneralResponse saveMonitorItemPrototype(@RequestBody MonitorItemPrototype prototype) {
		return biz.saveItemPrototype(prototype);
	}
	
	@Override
	public GeneralResponse removeMonitorItemPrototype(@PathVariable("id") Long id) {
		return biz.removeItemPrototype(id);
	}
	
	@Override
	public GeneralResponse bactchRemoveMonitorItemPrototype(@PathVariable("joinIds") String joinIds) {
		String[] idArr = joinIds.split(",");
		Stream<Long> map = Arrays.asList(idArr).stream().filter(id -> StringUtils.isNotBlank(id)).map(id -> Long.valueOf(id));
		return biz.batchRemoveItemPrototype(map.collect(Collectors.toList()));
	}
	
	@Override
	public PageResponse<MonitorItemPrototype> queryMonitorItemPrototypeList(@RequestBody MonitorItemPrototypeQueryModel queryParams) {
		return biz.queryItemPrototypeList(queryParams);
	}
	
	@Override
	public GeneralResponse syncRefreshItemsConfigByItemPrototype(@PathVariable("id") Long id) {
		return biz.syncRefreshItemsConfigByItemPrototype(id);
	}
}