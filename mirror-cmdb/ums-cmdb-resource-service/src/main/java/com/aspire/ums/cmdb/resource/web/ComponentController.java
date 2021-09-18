package com.aspire.ums.cmdb.resource.web;

import com.aspire.ums.cmdb.resource.entity.DataGrid;
import com.aspire.ums.cmdb.resource.service.ComponentDataService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/resource/component")
@Api("资源组件查询")
public class ComponentController {
	
	private static Logger log = Logger.getLogger(ComponentController.class);
	
	@Autowired
	private ComponentDataService componentDataService;

	@RequestMapping("/jsonDatas")
	@ResponseBody
	@ApiOperation(value = "", notes = "")
	public List<Map> queryDatas(@RequestParam Map<String, Object> params ){
		  List<Map> dataList = new ArrayList<>();
		 try {
	         dataList = componentDataService.queryDatas(params);
			 log.info(dataList.size());
		} catch (Exception e) {
			log.error("查询数据失败", e);		
		}
		 return dataList;
	}

	@RequestMapping("/gridData_1")
	@ResponseBody
	@ApiOperation(value = "", notes = "")
	public DataGrid queryGridData_1(@RequestParam Map<String, Object> params){
		DataGrid dataGrid = new DataGrid();
		try {
			Integer page =  Integer.parseInt(params.get("page").toString().trim());
			Integer rows = Integer.parseInt(params.get("rows").toString().trim());
			Integer startIndex = (page - 1)*rows;
			Map<String, Object> params1 = new HashMap<>();
			Object obj = params.get("list");
			if(null != obj) {
				String idStr = params.get("list").toString();
				String [] str = idStr.split(",");
				if(str.length>0) {
					String listStr = "(";
					for (int a = 0; a < str.length; a++) {
						if(a<str.length-1) {
							listStr = listStr+"'"+str[a]+"',";
						}else {
							listStr = listStr+"'"+str[a]+"'";
						}
					}
					listStr = listStr + ")";
					params1.put("list", listStr);
				}
			}
//			String  setStr = UserUtils.getBussinessSqlSetFormat();
//			if(setStr!=null && !setStr.trim().equals("")){
//				params1.put(SYS_BUSINESS_SET, "("+setStr+")");
//			}
			Object[] data =  componentDataService.queryDataGrid(params1, startIndex, rows);
			dataGrid.setTotal(Long.parseLong(data[0].toString()));
			dataGrid.setRows(data[1]==null?null:(ArrayList<?>)data[1]);
		} catch (Exception e) {
			log.error("查询griddata 失败",e);
		}
		return dataGrid;
	}
}
