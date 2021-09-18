package com.aspire.ums.cmdb.maintenance.web;

import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import com.aspire.ums.cmdb.allocate.util.PageBean;
import com.aspire.ums.cmdb.dict.entity.ConfigDict;
import com.aspire.ums.cmdb.dict.service.ConfigDictService;
import com.aspire.ums.cmdb.maintenance.entity.MaintenSoftware;
import com.aspire.ums.cmdb.maintenance.service.MaintenSoftService;
import com.aspire.ums.cmdb.maintenance.util.ExcelReaderUtils;
import com.aspire.ums.cmdb.maintenance.util.ExportExcelUtil;
import com.aspire.ums.cmdb.maintenance.util.MaintenSoftPageRequest;
import com.aspire.ums.cmdb.maintenance.util.MaintenSoftPageResp;
import com.aspire.ums.cmdb.maintenance.util.MaintenSoftwareRequest;
import com.aspire.ums.cmdb.maintenance.util.MaintenSoftwareResp;

import lombok.extern.slf4j.Slf4j;

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
@RequestMapping("/v1/cmdb/maintensoft")
public class MaintenSoftController   {
	
	 
    @Autowired
    private MaintenSoftService  maintenSoftService;
    
    @Autowired
    private ConfigDictService configDictService;
   
    
    /**
     *  新增软件维保
     * @return 模型列表.3
     */
    @PostMapping(value = "/insertMaintenSoftware" )
    public String insertMaintenSoftware(@RequestBody MaintenSoftwareRequest maintenSoftwareRequest) {
         
    	log.info("maintenSoftwareRequest is {} ",maintenSoftwareRequest);
    	 
    	MaintenSoftware maintenSoftware=new MaintenSoftware();
    	
    	BeanUtils.copyProperties(maintenSoftwareRequest, maintenSoftware);
    	
    	maintenSoftService.insertMaintenSoftware( maintenSoftware );
    	
    	return "success";
    	
    }
    
    
    /**
     *  查询软件维保通过id
     * @return  
     */
    @GetMapping(value = "/selectMaintenSoftwareById" )
    public MaintenSoftwareResp selectMaintenSoftwareById( @RequestParam("id") String id ) {
        
    	log.info("id is {} ",id);
    	
    	MaintenSoftware maintenSoftware=maintenSoftService.selectMaintenSoftwareById(id);
    	
    	MaintenSoftwareResp maintenSoftwareResp=new MaintenSoftwareResp();
    	
    	BeanUtils.copyProperties(maintenSoftware, maintenSoftwareResp);
    	 
        return maintenSoftwareResp;
    }
     
    
    /**
     *  查询软件维保通过软件名称
     * @return  
     */
    @GetMapping(value = "/selectMaintenSoftwareBySoftNmae" )
    public MaintenSoftwareResp selectMaintenSoftwareBySoftNmae( @RequestParam("company") String company ,
    	   @RequestParam("project") String project , @RequestParam("software_name") String softwareName ) {
           
    	log.info("company is {} ",company);
    	log.info("project is {} ",project);
    	log.info("software_name is {} ",softwareName);
    			
    	MaintenSoftware maintenSoftware=maintenSoftService.selectMaintenSoftwareBySoftNmae(company ,project ,softwareName);
    	
    	MaintenSoftwareResp maintenSoftwareResp=new MaintenSoftwareResp();
    		 	
        if (maintenSoftware!=null) {
    		
    		BeanUtils.copyProperties(maintenSoftware, maintenSoftwareResp);
		}
    	
        return maintenSoftwareResp;
    }
     
    
    /**
     *  修改软件维保
     * @return 模型列表
     */
    @PostMapping(value = "/updateMaintenSoftware" )
    public String updateMaintenSoftware(@RequestBody MaintenSoftwareRequest maintenSoftwareRequest) {
    	
    	log.info("maintenSoftwareRequest is {} ",maintenSoftwareRequest); 
    	
        MaintenSoftware maintenSoftware=new MaintenSoftware();
    	
    	BeanUtils.copyProperties(maintenSoftwareRequest, maintenSoftware);
    	
    	maintenSoftService.updateMaintenSoftware (maintenSoftware );
    	
    	return "success";
    	
    }
    
    /**
     *  批量更新软件维保
     * @return 模型列表
     */
    @PostMapping(value = "/batchUpdateMaintenSoftware" )
    public String batchUpdateMaintenSoftware(@RequestBody MaintenSoftwareRequest maintenSoftwareRequest) {
    	
    	log.info("maintenSoftwareRequest is {} ",maintenSoftwareRequest); 
    	
        MaintenSoftware maintenSoftware=new MaintenSoftware();
    	
    	BeanUtils.copyProperties(maintenSoftwareRequest, maintenSoftware);
    	
    	maintenSoftService.batchUpdateMaintenSoftware (maintenSoftware );
    	
    	return "success";
    	
    }
     
    
    /**
     *  删除软件维保
     * @return 模型列表
     */
    @DeleteMapping(value = "/deleteMaintenSoftware" )
    public String deleteMaintenSoftware( @RequestParam("ids") String ids ) {
    	
    	log.info("ids is {} ",ids);
    	
    	maintenSoftService.deleteMaintenSoftwareById(ids);
    	
    	return "success";  
    	
    }
    

   
    /**
     *  分页查询软件维保数据
     * @return 模型列表
     */
    @PostMapping(value = "/listMaintenSoftwareByPage" )  
    public PageBean<MaintenSoftPageResp> selectMaintenSoftwareByPage( @RequestBody MaintenSoftPageRequest maintenSoftPageRequest ) {
        
    	log.info("maintenSoftPageRequest is {} ",maintenSoftPageRequest);
    	
        return maintenSoftService.selectMaintenSoftByPage(maintenSoftPageRequest);
        
    }
       
    
    /**
     *  导出软件维保数据
     * @return 模型列表
     */
    @PostMapping(value = "/downloadMaintenSoftware" ) 
    public List<MaintenSoftPageResp> downloadMaintenSoftware( @RequestBody MaintenSoftPageRequest maintenSoftPageRequest  ){
    	
    	log.info("maintenSoftPageRequest is {} ",maintenSoftPageRequest);
    	
    	return maintenSoftService.getMaintenSoftwareExcelData(maintenSoftPageRequest);
            	
    }
    
    
    /**
     *  导入软件维保数据
     * @return 模型列表
     */
    @PostMapping(value = "/uploadMaintenSoftware" )
	 public String uploadMaintenSoftware( @RequestBody List<MaintenSoftwareRequest>  maintenSoftwareRequestList ) {
  
    	log.info("maintenSoftwareRequestList  size is {} ",maintenSoftwareRequestList.size());
	  
    	List<MaintenSoftware> maintenSoftwareList=new ArrayList<MaintenSoftware>();
    	
    	
    	for(MaintenSoftwareRequest maintenSoftwareRequest : maintenSoftwareRequestList ){
    		
    		 MaintenSoftware maintenSoftware=new MaintenSoftware();
    	    	
    	     BeanUtils.copyProperties(maintenSoftwareRequest, maintenSoftware);
    		
    	     maintenSoftwareList.add(maintenSoftware);
    	}
    	
    	
		maintenSoftService.insertMaintenSoftwareList(  maintenSoftwareList ); 
			 
		return "success";
	}
    
    
    
    /**
     *  导出软件维保数据
     * @return 模型列表
     */
    @PostMapping(value = "/downloadMaintenSoftware11" ) 
	public void downloadMaintenSoftware11( @RequestBody MaintenSoftPageRequest compMaintenSoftPageRequest ) {
		
		log.info("compMaintenSoftPageRequest is {} ",compMaintenSoftPageRequest);
		 
		
		ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = servletRequestAttributes.getRequest();
        HttpServletResponse response = servletRequestAttributes.getResponse();
		
        List<MaintenSoftPageResp> maintenSoftPageRespList=null;
        
        if (compMaintenSoftPageRequest.getPageSize().equals("0")) {
            maintenSoftPageRespList= new ArrayList<MaintenSoftPageResp>();
            
		}else{
			
			maintenSoftPageRespList= maintenSoftService.getMaintenSoftwareExcelData(compMaintenSoftPageRequest);	
			
		}
         
        List<ConfigDict> dictList= configDictService.selectDictsByType("device_mfrs",null,null,null);
         
        List<String> companyList=new ArrayList<String>();
 		
 		for(ConfigDict  dict:dictList){
 			
 			companyList.add(dict.getValue());
 			
 		}
         
 		List<String> unitList=new ArrayList<String>();
 		unitList.add("套");
 		unitList.add("人天");
		
 		
		String[] headerList = {"项目","分类 ","软件名称 ","单位"+unitList.toString() ,"数量","厂商 "+companyList.toString(),"联系人姓名 ","电话","本期维保开始时间","本期维保结束时间 ",
   			 "管理员","备注" };
        String[] keyList = {"project","classify","softwareName","unit","number","company","userName","telephone","maintenBeginDate",
       		 "maintenEndDate","admin","remark" };
        String title = "软件维保";
        String fileName = title+".xlsx";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            List<Map<String, Object>> dataLists = new ArrayList<Map<String,Object>>();
            for (MaintenSoftPageResp maintenSoftPageResp : maintenSoftPageRespList) {
                Map<String, Object>  map=ExportExcelUtil.objectToMap(maintenSoftPageResp);
           	
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

    /**
     *  导入软件维保数据
     * @return 模型列表
     */
    @PostMapping(value = "/uploadMaintenSoftware11" )
	public Map<String, Object> uploadMaintenSoftware11( @RequestParam("file")  MultipartFile file) {
		 
		Map<String, Object> map = new HashMap<String, Object>();
		 
		if (file == null) {
			map.put("success", false);
			map.put("message", "文件不能为空！");
			return map;
		}

		String fileName = file.getOriginalFilename();
		log.info("filename is : " + fileName);
		if (!ExcelReaderUtils.validateExcel(fileName)) {
			map.put("success", false);
			map.put("message", "文件必须是excel格式！");
			return map;
		}
		
		
		List<ConfigDict> dictList= configDictService.selectDictsByType("device_mfrs",null,null,null);
		
		List<String> companyList=new ArrayList<String>();
		
		for(ConfigDict  dict:dictList){
			
		    companyList.add(dict.getValue());
		    
		}
		
		List<String> unitList=new ArrayList<String>();
		unitList.add("套");
		unitList.add("人天");
			
		try {
			
			ExcelReaderUtils excelReader = new ExcelReaderUtils();
			 
			List<MaintenSoftware> maintenSoftwareList=excelReader.doUploadMaintenSoftwareData(file,companyList,unitList);
			
			maintenSoftService.insertMaintenSoftwareList(maintenSoftwareList);
		
			map.put("success", true);
			map.put("message", null); 
			
			
		} catch (Exception e) {
			
			map.put("success", false);
			map.put("message", e.getMessage());
					 
		}
					
		return map;
		
		 
	}
    
    
    
}
