package com.aspire.ums.cmdb.resource.web;

import com.aspire.ums.cmdb.resource.entity.DataGrid;
import com.aspire.ums.cmdb.resource.service.ComponentDataService;
import com.aspire.ums.cmdb.resource.service.ResBuildService;
import com.aspire.ums.cmdb.util.ExportExcelUtils;
import com.aspire.ums.cmdb.util.ModuleUtils;
import com.google.common.collect.Maps;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/resource/authComponent")
@Api("数据导出API")
public class AuthComponentController {
	
	private static Logger log = Logger.getLogger(AuthComponentController.class);
	
	@Autowired
	private ComponentDataService componentDataService;

	@Autowired
	private ResBuildService resBuildService;
	
	public void setComponentDataService(ComponentDataService componentDataService) {
		this.componentDataService = componentDataService;
	}
	
//	private static final String SYS_BUSINESS_SET = "SYS_BUSINESS_SET";
	@ApiOperation(value = "表格数据", notes = "返回查询的表格数据")
	@RequestMapping(value = "/gridData", method = RequestMethod.POST)
	@ResponseBody
 	public DataGrid queryGridData(@RequestParam Map<String, Object> params, @RequestParam(value = "multipleID[]",required = false)String[] multipleID){
		params.put("multipleID", multipleID);
 		DataGrid dataGrid = new DataGrid();
   		try {
  			Integer page =  Integer.parseInt(params.get("page").toString().trim());
  			Integer rows = Integer.parseInt(params.get("rows").toString().trim());
  			Integer startIndex = (page - 1)*rows;
  			//权限标识相关
//  			Boolean checkRight=(null==params.get("checkRight")? false:Boolean.valueOf(params.get("checkRight").toString()));
//
//  			String right = (null==params.get("right")? "":params.get("right").toString());
  			
//  			String  setStr = UserUtils.getBussinessSqlSetFormat();
//	  		if(setStr!=null && !setStr.trim().equals("")){
//	  	   			params.put(SYS_BUSINESS_SET, "("+setStr+")");
//	   		}
  			
//	  		if(checkRight){
//  				Subject subject = SecurityUtils.getSubject();
//  				Boolean isPermitted = subject.isPermitted(right);
//  				if(isPermitted){
//  		  	   		params.put("SYS_BUSINESS_SET",null);
//  				}
//  			}
	  		
	  		//迎检账号数据过滤
//	  		User user = UserUtils.getUser();
//	  		if("4".equals(user.getUserType())){
//	  			params.put("FILTER_FOR_CHECK", "1");
//			}
			Object[] data =  componentDataService.queryDataGrid(params, startIndex, rows);
			dataGrid.setTotal(Long.parseLong(data[0].toString()));
			dataGrid.setRows(data[1]==null?null:(ArrayList<?>)data[1]);
  		} catch (Exception e) {
			log.error("查询griddata 失败",e);
 		}
   		return dataGrid;
	}

	@ApiOperation(value = "资源建设数据EXCEL导出", notes = "资源建设数据EXCEL导出")
	@RequestMapping(value = "/resourceBuildExportGridData", method = RequestMethod.POST)
	public void resourceBuildExportGridData(@RequestParam Map<String, Object> params,HttpServletRequest request,HttpServletResponse response){
//		LogWatch logWatch = new LogWatch();
//		logWatch.start();
		String modulesKey = params.get("modulesKey")==null?"default_data_export":params.get("modulesKey").toString();
		String modules = ModuleUtils.getModule().get(modulesKey);
		StringBuilder content = new StringBuilder();
		content.append("执行操作："+modules+"<br>");
//		String flag= LogWatch.FLAG_FAILURE;
		try {
			if(params != null){
				String[] keyList = params.get("keys")==null?null:params.get("keys").toString().split(",");
				String[] headerList = params.get("headers")==null?null:params.get("headers").toString().split(",");
				params.remove("keyList");
				params.remove("headerList");
				String [] whereStr = params.get("list")==null?null:params.get("list").toString().split(",");
				if(whereStr!=null) {
					String listStr = "(";
					for (int a = 0; a < whereStr.length; a++) {
						if(a<whereStr.length-1) {
							listStr = listStr+"'"+whereStr[a]+"',";
						}else {
							listStr = listStr+"'"+whereStr[a]+"')";
						}
					}
					params.put("list", listStr);
				}
//				//权限标识相关
//				Boolean checkRight=(null==params.get("checkRight")? false:Boolean.valueOf(params.get("checkRight").toString()));
//
//				String right = (null==params.get("right")? "":params.get("right").toString());

//				String  setStr = UserUtils.getBussinessSqlSetFormat();
//				if(setStr!=null && !setStr.trim().equals("")){
//					params.put(SYS_BUSINESS_SET, "("+setStr+")");
//				}
//				if(checkRight){
//					Subject subject = SecurityUtils.getSubject();
//					Boolean isPermitted = subject.isPermitted(right);
//					if(isPermitted){
//						params.put("SYS_BUSINESS_SET",null);
//					}
//				}
				String name = params.get("name") == null ? "" : String.valueOf(params.get("name"));
				String rePool = params.get("rePool") == null ? "" : String.valueOf(params.get("rePool"));
				String reStatus = params.get("reStatus") == null ? "" : String.valueOf(params.get("reStatus"));
				String seType = params.get("seType") == null ? "" : String.valueOf(params.get("seType"));
				String seName = params.get("seName") == null ? "" : String.valueOf(params.get("seName"));

				Map<String, String> paramers= Maps.newHashMap();
				paramers.put("name", name);
				paramers.put("rePool", rePool);
				paramers.put("reStatus", reStatus);
				paramers.put("seType", seType);
				paramers.put("seName", seName);
				List<Map<String, Object>> dataLists = resBuildService.selectResBuildMaCountList(paramers);

				log.info("exportGridData params:"+params);
				//outputstream def
				OutputStream os = response.getOutputStream();// 取得输出流
				try {
					String title = params.get("title").toString();
					String fileName = title+".xlsx";
					response.setHeader("Content-Disposition", "attachment;filename=".concat(String.valueOf(URLEncoder.encode(fileName, "UTF-8"))));
					response.setHeader("Connection", "close");
					response.setHeader("Content-Type", "application/vnd.ms-excel");
					//excel constuct
					ExportExcelUtils eeu = new ExportExcelUtils();
					Workbook book = new SXSSFWorkbook(128);
					eeu.exportExcel(book, 0, title, headerList, dataLists, keyList);
					book.write(os);
//					flag= LogWatch.FLAG_SUCCESS;
				} catch (Exception e) {
					log.error("导出excel失败", e);
				}finally{
					os.flush();
					os.close();
				}
				//output out client
			}
		} catch (Exception e) {
			log.error("导出excel,查询数据失败", e);
		}
//		logWatch.end(modules, content.toString(), LogWatch.LOG_TYPE_OPERATION, flag);
	}
	
	
}
