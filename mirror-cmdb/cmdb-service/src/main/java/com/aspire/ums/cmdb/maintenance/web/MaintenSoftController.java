package com.aspire.ums.cmdb.maintenance.web;

import com.aspire.ums.cmdb.common.PageBean;
import com.aspire.ums.cmdb.dict.payload.ConfigDict;
import com.aspire.ums.cmdb.dict.service.ConfigDictService;
import com.aspire.ums.cmdb.maintenance.IMaintenSoftAPI;
import com.aspire.ums.cmdb.maintenance.entity.MaintenSoftware;
import com.aspire.ums.cmdb.maintenance.payload.MaintenSoftPageRequest;
import com.aspire.ums.cmdb.maintenance.payload.MaintenSoftPageResp;
import com.aspire.ums.cmdb.maintenance.payload.MaintenSoftwareRequest;
import com.aspire.ums.cmdb.maintenance.payload.MaintenSoftwareResp;
import com.aspire.ums.cmdb.maintenance.service.MaintenSoftService;
import com.aspire.ums.cmdb.maintenance.util.ExportExcelUtil;
import com.aspire.ums.cmdb.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: MaintenSoftwareController
 * Date:     2019/3/12 15:43
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
 
@RestController
@Slf4j
public class MaintenSoftController implements IMaintenSoftAPI   {
	
	 
    @Autowired
    private MaintenSoftService  maintenSoftService;
    
    @Autowired
    private ConfigDictService configDictService;
   
    
    /**
     *  新增软件维保
     * @return 模型列表.3
     */
   
    public Map<String, Object> insertMaintenSoftware(@RequestBody MaintenSoftwareRequest maintenSoftwareRequest) {

		log.info("Request into MaintenSoftController.insertMaintenSoftware()  request -> {}", maintenSoftwareRequest);

    	MaintenSoftware maintenSoftware=new MaintenSoftware();
    	
    	BeanUtils.copyProperties(maintenSoftwareRequest, maintenSoftware);

		Map<String, Object> result = new HashMap<>();
		try {
			maintenSoftService.insertMaintenSoftware( maintenSoftware );
			if (StringUtils.isNotEmpty(maintenSoftware.getId())) {
				result.put("success", true);
				result.put("message", "更新成功");
			} else {
				result.put("success", true);
				result.put("message", "新增成功");
			}
		} catch (Exception e) {
			result.put("success", false);
			result.put("message", "新增失败");
			log.error("insert MaintenSoftware error.", e);
		}
		return result;

    }

	@Override
	public MaintenSoftwareResp selectMaintenSoftwareBySoftNmae(@RequestParam("project") String project,
															   @RequestParam("softwareName") String softwareName) {
		log.info("Request into MaintenSoftController.selectMaintenSoftwareBySoftNmae() project -> {} softwareName -> {}", project, softwareName);
		MaintenSoftware maintenSoftware=maintenSoftService.selectMaintenSoftwareBySoftNmae(project, softwareName);
		MaintenSoftwareResp maintenSoftwareResp=new MaintenSoftwareResp();
		if (maintenSoftware != null) {
			BeanUtils.copyProperties(maintenSoftware, maintenSoftwareResp);
		}
        return maintenSoftwareResp;
	}


//    /**
//     *  查询软件维保通过id
//     * @return
//     */
//
//    public MaintenSoftwareResp selectMaintenSoftwareById( @RequestParam("id") String id ) {
//
//    	log.info("id is {} ",id);
//
//    	MaintenSoftware maintenSoftware=maintenSoftService.selectMaintenSoftwareById(id);
//
//    	MaintenSoftwareResp maintenSoftwareResp=new MaintenSoftwareResp();
//
//    	BeanUtils.copyProperties(maintenSoftware, maintenSoftwareResp);
//
//        return maintenSoftwareResp;
//    }
     
    
//    /**
//     *  修改软件维保
//     * @return 模型列表
//     */
//
//    public String updateMaintenSoftware(@RequestBody MaintenSoftwareRequest maintenSoftwareRequest) {
//
//    	log.info("maintenSoftwareRequest is {} ",maintenSoftwareRequest);
//
//        MaintenSoftware maintenSoftware=new MaintenSoftware();
//
//    	BeanUtils.copyProperties(maintenSoftwareRequest, maintenSoftware);
//
//    	maintenSoftService.updateMaintenSoftware (maintenSoftware );
//
//    	return "success";
//
//    }
    
//    /**
//     *  批量更新软件维保
//     * @return 模型列表
//     */
//
//    public String batchUpdateMaintenSoftware(@RequestBody MaintenSoftwareRequest maintenSoftwareRequest) {
//
//    	log.info("maintenSoftwareRequest is {} ",maintenSoftwareRequest);
//
//        MaintenSoftware maintenSoftware=new MaintenSoftware();
//
//    	BeanUtils.copyProperties(maintenSoftwareRequest, maintenSoftware);
//
//    	maintenSoftService.batchUpdateMaintenSoftware (maintenSoftware );
//
//    	return "success";
//
//    }
     
    
    /**
     *  删除软件维保
     * @return 模型列表
     */
    
    public Map<String, Object> deleteMaintenSoftware( @RequestParam("ids") String ids ) {
    	
    	log.info("ids is {} ",ids);
		Map<String, Object> result = new HashMap<>();
		try {
			log.info("Request into MaintenSoftController.deleteMaintenSoftware()  deleteIds -> {}", ids);
			maintenSoftService.deleteMaintenSoftwareById(ids);
			result.put("success", true);
			result.put("message", "删除成功");
		} catch (Exception e) {
			result.put("success", false);
			result.put("message", "删除失败,失败原因:" + e.getMessage());
			log.error("delete MaintenSoftware error.", e);
		}
		return result;
    }
    

   
    /**
     *  分页查询软件维保数据
     * @return 模型列表
     */
    
    public PageBean<MaintenSoftPageResp> selectMaintenSoftwareByPage( @RequestBody MaintenSoftPageRequest maintenSoftPageRequest ) {

    	log.info("maintenSoftPageRequest is {} ",maintenSoftPageRequest);
    	
        return maintenSoftService.selectMaintenSoftByPage(maintenSoftPageRequest);
        
    }
       
    
//    /**
//     *  查询软件维保列表
//     * @return 模型列表
//     */
//
//    public List<MaintenSoftPageResp> getMaintenSoftwareList( @RequestBody MaintenSoftPageRequest maintenSoftPageRequest  ){
//
//    	log.info("maintenSoftPageRequest is {} ",maintenSoftPageRequest);
//
//    	return maintenSoftService.getMaintenSoftwareList(maintenSoftPageRequest);
//
//    }
    
    
    /**
     *  保存软件维保列表
     * @return 模型列表
     */
     
	 public Map<String, Object> insertMaintenSoftwareList( @RequestBody List<MaintenSoftwareRequest>  maintenSoftwareRequestList ) {
  
    	log.info("maintenSoftwareRequestList  size is {} ",maintenSoftwareRequestList.size());
    	Map<String, Object> result = new HashMap<>();
	 	List<MaintenSoftware> maintenSoftwareList=new ArrayList<MaintenSoftware>();
    	for(MaintenSoftwareRequest maintenSoftwareRequest : maintenSoftwareRequestList ){
    		 MaintenSoftware maintenSoftware=new MaintenSoftware();
    	     BeanUtils.copyProperties(maintenSoftwareRequest, maintenSoftware);
    	     maintenSoftwareList.add(maintenSoftware);
    	}
		 try {
			 maintenSoftService.insertMaintenSoftwareList(  maintenSoftwareList );
			 result.put("success", true);
			 result.put("message", "新增成功");
		 } catch (Exception e) {
			 result.put("success", false);
			 result.put("message", "新增失败");
			 log.error("insert MaintenSoftwareList error.", e);
		 }
		 return result;
	}
    
    
    
    /**
     *  导出软件维保数据
     * @return 模型列表
     */
    
	public void downloadMaintenSoftware( @RequestBody MaintenSoftPageRequest compMaintenSoftPageRequest ) {
		
		log.info("compMaintenSoftPageRequest is {} ",compMaintenSoftPageRequest);


		ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletResponse response = servletRequestAttributes.getResponse();

        List<MaintenSoftPageResp> maintenSoftPageRespList=null;

			maintenSoftPageRespList= maintenSoftService.selectMaintenSoftByPage(compMaintenSoftPageRequest).getResult();

        List<ConfigDict> dictList= configDictService.selectDictsByType("device_mfrs",null,null,null);

        List<String> companyList=new ArrayList<String>();

 		for(ConfigDict  dict:dictList){

 			companyList.add(dict.getValue());

 		}

 		List<String> unitList=new ArrayList<String>();
 		unitList.add("套");
 		unitList.add("人天");


		String[] headerList = {"项目","分类","软件名称","单位","数量","服务厂家","联系人","联系方式",
				"本期维保起始时间", "本期维保结束时间","维保管理员","备注"};
        String[] keyList = {"project","classify","softwareName","unit","number","company","userName","telephone","maintenBeginDate",
       		 "maintenEndDate","admin","remark" };
        String title = "软件维保";
        String fileName = title+".xlsx";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            List<Map<String, Object>> dataLists = new ArrayList<Map<String,Object>>();
            for (MaintenSoftPageResp maintenSoftPageResp : maintenSoftPageRespList) {
                Map<String, Object>  map= ExportExcelUtil.objectToMap(maintenSoftPageResp);

           	 if(maintenSoftPageResp.getMaintenBeginDate()!=null){
           		map.put("maintenBeginDate", sdf.format(maintenSoftPageResp.getMaintenBeginDate()));
           	 }
           	 if(maintenSoftPageResp.getMaintenEndDate()!=null){
           		map.put("maintenEndDate",  sdf.format(maintenSoftPageResp.getMaintenEndDate()));
           	 }

               dataLists.add(map);

            }
            OutputStream os = response.getOutputStream();// 取得输出流
            response.setHeader("Content-Disposition", "attachment;filename=".concat(String.valueOf(URLEncoder.encode(fileName, "UTF-8"))));
            response.setHeader("Connection", "close");
            response.setHeader("Content-Type", "application/vnd.ms-excel");
            //excel constuct
            ExportExcelUtil eeu = new ExportExcelUtil();
            Workbook book = new SXSSFWorkbook(128);

            eeu.exportExcel(book, 0, title, headerList, dataLists, keyList );
            book.write(os);
        } catch (Exception e) {
       	 log.error("导出Excel数据失败!", e);
        }
		
	}
    
    
    
}
