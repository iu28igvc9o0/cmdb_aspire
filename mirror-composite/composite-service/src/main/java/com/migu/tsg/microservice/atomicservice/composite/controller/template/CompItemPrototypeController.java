package com.migu.tsg.microservice.atomicservice.composite.controller.template;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.aspire.mirror.common.entity.PageResponse;
import com.aspire.mirror.composite.service.template.ICompItemPrototypeService;
import com.aspire.mirror.template.api.dto.GeneralResponse;
import com.aspire.mirror.template.api.dto.model.MonitorItemPrototype;
import com.aspire.mirror.template.api.dto.model.MonitorItemPrototype.MonitorItemPrototypeQueryModel;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.template.ItemPrototypeServiceClient;
import com.migu.tsg.microservice.atomicservice.composite.controller.util.EasyPoiUtil;

import cn.afterturn.easypoi.excel.entity.ExportParams;

/** 
 *
 * 项目名称: composite-service 
 * <p/>
 * 
 * 类名: CompMonitorItemPrototypeController
 * <p/>
 *
 * 类功能描述: 指标原型管理
 * <p/>
 *
 * @author	pengguihua
 *
 * @date	2021年1月28日  
 *
 * @version	V1.0 
 * <br/>
 *
 * <b>Copyright(c)</b> 2021 卓望公司-版权所有 
 *
 */
@RestController
public class CompItemPrototypeController implements ICompItemPrototypeService {
	@Autowired
	private ItemPrototypeServiceClient client;
	
	// @ResAction(action = "create", resType = "item_prototype")
	public GeneralResponse saveMonitorItemPrototype(@RequestBody MonitorItemPrototype itemPrototype) {
		return client.saveMonitorItemPrototype(itemPrototype);
	}
    
	// @ResAction(action = "delete", resType = "item_prototype")
	public GeneralResponse removeMonitorItemPrototype(@PathVariable("id") Long id) {
		return client.removeMonitorItemPrototype(id);
	}
	
	// @ResAction(action = "delete", resType = "item_prototype")
	public GeneralResponse bactchRemoveMonitorItemPrototype(@PathVariable("joinIds") String joinIds) {
		return client.bactchRemoveMonitorItemPrototype(joinIds);
	}
    
	// @ResAction(action = "view", resType = "item_prototype")
	public PageResponse<MonitorItemPrototype> queryMonitorItemPrototypeList(
						@RequestBody MonitorItemPrototypeQueryModel queryParams) {
		return client.queryMonitorItemPrototypeList(queryParams);
	}
    
	// @ResAction(action = "sync_config", resType = "item_prototype")
	public GeneralResponse syncRefreshItemsConfigByItemPrototype(@PathVariable("id") Long id) {
		return client.syncRefreshItemsConfigByItemPrototype(id);
	}
	
	public void exportMonitorItemPrototypeList(@RequestBody MonitorItemPrototypeQueryModel queryParams, HttpServletResponse response) {
		PageResponse<MonitorItemPrototype> pageResult = client.queryMonitorItemPrototypeList(queryParams);
		List<MonitorItemPrototype> resultList = pageResult.getResult();
		resultList = resultList == null ? new ArrayList<>() : resultList;
		
		String fileName = "item_prototype_list_export.xls";
		String title = "监控项原型导出列表";
		String sheetName = "监控项原型列表";
		String[] exclusions = null;
		
		if (MonitorItemPrototype.PROTOTYPE_LABEL_CRUISE_SYSTEM.equals(queryParams.getPrototypeLabel())) {
			title = "系统巡检" + title;
			sheetName = "系统巡检" + sheetName;
			exclusions = MonitorItemPrototype.EXECL_EXCLUDE_CRUISE_SYSTEM;
		} 
		else if (MonitorItemPrototype.PROTOTYPE_LABEL_CRUISE_JW.equals(queryParams.getPrototypeLabel())) {
			title = "交维巡检" + title;
			sheetName = "交维巡检" + sheetName;
			exclusions = MonitorItemPrototype.EXECL_EXCLUDE_CRUISE_JW;
		} 
		else if (MonitorItemPrototype.PROTOTYPE_LABEL_CRUISE_BASELINE.equals(queryParams.getPrototypeLabel())) {
			title = "基线巡检" + title;
			sheetName = "基线巡检" + sheetName;
			exclusions = MonitorItemPrototype.EXECL_EXCLUDE_CRUISE_BASELINE;
		}
		ExportParams exportParam = new ExportParams(title, sheetName);
		exportParam.setExclusions(exclusions);
		EasyPoiUtil.exportExcel(resultList, exportParam, MonitorItemPrototype.class, fileName, response);
	}
}
