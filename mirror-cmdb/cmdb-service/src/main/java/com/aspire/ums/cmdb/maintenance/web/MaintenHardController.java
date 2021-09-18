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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import com.aspire.ums.cmdb.common.PageBean;
import com.aspire.ums.cmdb.dict.payload.ConfigDict;
import com.aspire.ums.cmdb.dict.service.ConfigDictService;
import com.aspire.ums.cmdb.maintenance.IMaintenHardAPI;
import com.aspire.ums.cmdb.maintenance.entity.MaintenHardware;
import com.aspire.ums.cmdb.maintenance.payload.MaintenHardPageRequest;
import com.aspire.ums.cmdb.maintenance.payload.MaintenHardPageResp;
import com.aspire.ums.cmdb.maintenance.payload.MaintenHardwareRequest;
import com.aspire.ums.cmdb.maintenance.payload.MaintenHardwareResp;
import com.aspire.ums.cmdb.maintenance.service.MaintenHardService;
import com.aspire.ums.cmdb.maintenance.util.ExcelReaderUtils;
import com.aspire.ums.cmdb.maintenance.util.ExportExcelUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: MaintenHardwareController
 * Date:     2019/3/12 15:43
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
 
@RestController
@Slf4j
public class MaintenHardController implements IMaintenHardAPI   {
	
	 
    @Autowired
    private MaintenHardService  maintenHardService;
    
    
    @Autowired
    private ConfigDictService configDictService;
   
    
    /**
     *  新增硬件维保
     * @return 模型列表.3
     */
   
    public String insertMaintenHardware(@RequestBody MaintenHardwareRequest maintenHardwareRequest) {
         
    	log.info("maintenHardwareRequest is {} ",maintenHardwareRequest);
    	 
    	MaintenHardware maintenHardware=new MaintenHardware();
    	
    	BeanUtils.copyProperties(maintenHardwareRequest, maintenHardware);
    	
    	maintenHardService.insertMaintenHardware( maintenHardware );
    	
    	return "success";
    	
    }
    
    
    /**
     *  查询硬件维保通过id
     * @return  
     */
  
    public MaintenHardwareResp selectMaintenHardwareById( @RequestParam("id") String id ) {
        
    	log.info("id is {} ",id);
    	
    	MaintenHardware maintenHardware=maintenHardService.selectMaintenHardwareById(id);
    	
    	MaintenHardwareResp maintenHardwareResp=new MaintenHardwareResp();
    	
    	BeanUtils.copyProperties(maintenHardware, maintenHardwareResp);
    	 
        return maintenHardwareResp;
    }
     
     
    /**
     *  查询硬件维保通过设备型号
     * @return  
     */
  
    public MaintenHardwareResp selectMaintenHardwareByDeviceModel( @RequestParam("mainten_factory") String maintenFactory,
                                                                   @RequestParam("device_model") String deviceModel,
                                                                   @RequestParam("warranty_date") String warrantyDate ) {
           
    	log.info("maintenFactory is {} ",maintenFactory);
    	log.info("deviceModel is {} ",deviceModel);
    	log.info("warrantyDate is {} ",warrantyDate);
    	 
    	
    	MaintenHardware maintenHardware=maintenHardService.selectMaintenHardwareByDeviceModel( maintenFactory,deviceModel,warrantyDate);
    	
    	MaintenHardwareResp maintenHardwareResp=new MaintenHardwareResp();
    		 	
        if (maintenHardware!=null) {
    		
    		BeanUtils.copyProperties(maintenHardware, maintenHardwareResp);
		}
    	
        return maintenHardwareResp;
    }
    
    
    
    /**
     *  修改硬件维保
     * @return 模型列表
     */
   
    public String updateMaintenHardware(@RequestBody MaintenHardwareRequest maintenHardwareRequest) {
    	
    	log.info("maintenHardwareRequest is {} ",maintenHardwareRequest); 
    	
        MaintenHardware maintenHardware=new MaintenHardware();
    	
    	BeanUtils.copyProperties(maintenHardwareRequest, maintenHardware);
    	
    	
    	maintenHardService.updateMaintenHardware (maintenHardware );
    	
    	return "success";
    	
    }
    
    
    /**
     *  批量更新硬件维保
     * @return 模型列表
     */
 
    public String batchUpdateMaintenHardware(@RequestBody MaintenHardwareRequest maintenHardwareRequest) {
    	
    	log.info("maintenHardwareRequest is {} ",maintenHardwareRequest); 
    	
    	MaintenHardware maintenHardware=new MaintenHardware();
     	
     	BeanUtils.copyProperties(maintenHardwareRequest, maintenHardware);
    	
     	maintenHardService.batchUpdateMaintenHardware(maintenHardware );
    	
    	return "success";
    	
    } 
    
    
     
    
    /**
     *  删除硬件维保
     * @return 模型列表
     */
    
    public String deleteMaintenHardware( @RequestParam("ids") String ids ) {
    	
    	log.info("ids is {} ",ids);
    	
    	maintenHardService.deleteMaintenHardwareById(ids);
    	
    	return "success";  
    	
    }
    

   
    /**
     *  分页查询硬件维保数据
     * @return 模型列表
     */
    
    public PageBean<MaintenHardPageResp> selectMaintenHardwareByPage( @RequestBody MaintenHardPageRequest maintenHardPageRequest ) {
        
    	log.info("maintenHardPageRequest is {} ",maintenHardPageRequest);
    	
        return maintenHardService.selectMaintenHardByPage(maintenHardPageRequest);
        
    }
       
    
    /**
     *  导出硬件维保数据
     * @return 模型列表
     */
    
    public List<MaintenHardPageResp> getMaintenHardwareList( @RequestBody MaintenHardPageRequest maintenHardPageRequest  ){
    	
    	log.info("maintenHardPageRequest is {} ",maintenHardPageRequest);
    	
    	return maintenHardService.getMaintenHardwareList(maintenHardPageRequest);
            	
    }
    
    
    /**
     *  保存硬件维保数据
     * @return 模型列表
     */
     
	 public String insertMaintenHardwareList( @RequestBody List<MaintenHardwareRequest>  maintenHardwareRequestList ) {
  
    	log.info("maintenHardwareRequestList  size is {} ",maintenHardwareRequestList.size());
	  
    	List<MaintenHardware> maintenHardwareList=new ArrayList<MaintenHardware>();
    	
    	
    	for(MaintenHardwareRequest maintenHardwareRequest : maintenHardwareRequestList ){
    		
    		 MaintenHardware maintenHardware=new MaintenHardware();
    	    	
    	     BeanUtils.copyProperties(maintenHardwareRequest, maintenHardware);
    		
    	     maintenHardwareList.add(maintenHardware);
    	}
    	
    	
		maintenHardService.insertMaintenHardwareList(  maintenHardwareList ); 
			 
		return "success";
	}
    
    
    
    
    /**
     *  导出硬件维保数据
     * @return 模型列表
     */
   
	public void downloadMaintenHardware( @RequestBody MaintenHardPageRequest compMaintenManagePageRequest ) {
		
		log.info("compMaintenManagePageRequest is {} ",compMaintenManagePageRequest);
		 
		
		ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = servletRequestAttributes.getRequest();
        HttpServletResponse response = servletRequestAttributes.getResponse();
		
        List<MaintenHardPageResp> maintenManagePageRespList=null;
        
        if (compMaintenManagePageRequest.getPageSize().equals("0")) {
            maintenManagePageRespList= new ArrayList<MaintenHardPageResp>();
            
		}else{
			
			maintenManagePageRespList= maintenHardService.getMaintenHardwareList(compMaintenManagePageRequest);	
			
		}
        
        List<ConfigDict> dictList= configDictService.selectDictsByType("idcType",null,null,null);
        List<String> resourcePoolList=new ArrayList<String>();
 		for(ConfigDict  dict:dictList){
 			resourcePoolList.add(dict.getName());
 		}
 		
 		List<ConfigDict> dictList1= configDictService.selectDictsByType("bizSystem",null,null,null);
        List<String> systemNameList=new ArrayList<String>();
 		for(ConfigDict  dict:dictList1){
 			systemNameList.add(dict.getName());
 		}
 		
 		List<ConfigDict> dictList2= configDictService.selectDictsByType("device_class",null,null,null);
        List<String> deviceClassifyList=new ArrayList<String>();
 		for(ConfigDict  dict:dictList2){
 			deviceClassifyList.add(dict.getValue());
 		}
 		
 		List<ConfigDict> dictList3= configDictService.selectDictsByType("device_type",null,null,null);
        List<String> deviceTypeList=new ArrayList<String>();
 		for(ConfigDict  dict:dictList3){
 			deviceTypeList.add(dict.getValue());
 		}
 		
 		List<ConfigDict> dictList4= configDictService.selectDictsByType("device_model",null,null,null);
        List<String> deviceModelList=new ArrayList<String>();
 		for(ConfigDict  dict:dictList4){
 			deviceModelList.add(dict.getValue());
 		}
 		
 		List<String> optionsTrueFalseList=new ArrayList<String>();
 		optionsTrueFalseList.add("是");
 		optionsTrueFalseList.add("否");
 		
 		List<String> originBuyExplainList=new ArrayList<String>();
 		originBuyExplainList.add("一旦发生故障容易影响多个业务线");
 		originBuyExplainList.add("承载业务的重要数据");
 		originBuyExplainList.add("关键且无冗余");
 		originBuyExplainList.add("小众且缺乏第三方维保支持");
 		
  
 		List<ConfigDict> dictList5= configDictService.selectDictsByType("provider",null,null,null);
        List<String> maintenFactoryList=new ArrayList<String>();
 		for(ConfigDict  dict:dictList5){
 			maintenFactoryList.add(dict.getValue());
 		}
 		
 		List<String> realMaintenList=new ArrayList<String>();
 		realMaintenList.add("原厂");
 		realMaintenList.add("第三方");
 		 
        
		
		String[] headerList = {"省份 ","市/区 ","资源池 "+resourcePoolList,"所属业务"+systemNameList,"设备分类"+deviceClassifyList,"设备类型 "+deviceTypeList,
				"设备型号 "+deviceModelList,"设备名称","设备序列号 ","资产编号 ","出保日期","是否购买维保"+optionsTrueFalseList,"是否原厂维保"+optionsTrueFalseList,
				"原厂维保购买必要性说明"+originBuyExplainList,"业务建议维保厂家"+maintenFactoryList,"维保厂家"+maintenFactoryList,"维保供应商联系方式","维保厂家联系方式",
   			    "本期维保开始时间 ","本期维保结束时间","实际构买维保类型"+realMaintenList,"管理员" };
        String[] keyList = {"province","city","resourcePool","systemName","deviceClassify","deviceType","deviceModel","deviceName",
        		"deviceSerialNumber","assetsNumber","warrantyDate","buyMainten","originBuy","originBuyExplain","adviceMaintenFactory",
        		"maintenFactory","maintenSupplyContact","maintenFactoryContact","maintenBeginDate","maintenEndDate","realMaintenType",
        		"admin" };
        String title = "硬件维保信息";
        String fileName = title+".xlsx";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            List<Map<String, Object>> dataLists = new ArrayList<Map<String,Object>>();
            for (MaintenHardPageResp maintenHardPageResp : maintenManagePageRespList) {
                Map<String, Object>  map=ExportExcelUtil.objectToMap(maintenHardPageResp);
           	
           	  if(maintenHardPageResp.getWarrantyDate()!=null){
           		map.put("warrantyDate",  sdf.format(maintenHardPageResp.getWarrantyDate())); 
           	 }
           	 if(maintenHardPageResp.getMaintenBeginDate()!=null){
           		map.put("maintenBeginDate",  sdf.format(maintenHardPageResp.getMaintenBeginDate())); 
           	 }
           	 if(maintenHardPageResp.getMaintenEndDate()!=null){
           		map.put("maintenEndDate",  sdf.format(maintenHardPageResp.getMaintenEndDate())); 
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
     *  导入硬件维保数据
     * @return 模型列表
     */
    @Override
	public Map<String, Object> uploadMaintenHardware( @RequestParam("file")  MultipartFile file) {
		 
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

		
		List<ConfigDict> dictList= configDictService.selectDictsByType("idcType",null,null,null);
        List<String> resourcePoolList=new ArrayList<String>();
 		for(ConfigDict  dict:dictList){
 			resourcePoolList.add(dict.getName());
 		}
 		
 		List<ConfigDict> dictList1= configDictService.selectDictsByType("bizSystem",null,null,null);
        List<String> systemNameList=new ArrayList<String>();
 		for(ConfigDict  dict:dictList1){
 			systemNameList.add(dict.getName());
 		}
 		
 		List<ConfigDict> dictList2= configDictService.selectDictsByType("device_class",null,null,null);
        List<String> deviceClassifyList=new ArrayList<String>();
 		for(ConfigDict  dict:dictList2){
 			deviceClassifyList.add(dict.getValue());
 		}
 		
 		List<ConfigDict> dictList3= configDictService.selectDictsByType("device_type",null,null,null);
        List<String> deviceTypeList=new ArrayList<String>();
 		for(ConfigDict  dict:dictList3){
 			deviceTypeList.add(dict.getValue());
 		}
 		
 		List<ConfigDict> dictList4= configDictService.selectDictsByType("device_model",null,null,null);
        List<String> deviceModelList=new ArrayList<String>();
 		for(ConfigDict  dict:dictList4){
 			deviceModelList.add(dict.getValue());
 		}
 		
 		List<String> optionsTrueFalseList=new ArrayList<String>();
 		optionsTrueFalseList.add("是");
 		optionsTrueFalseList.add("否");
 		
 		List<String> originBuyExplainList=new ArrayList<String>();
 		originBuyExplainList.add("一旦发生故障容易影响多个业务线");
 		originBuyExplainList.add("承载业务的重要数据");
 		originBuyExplainList.add("关键且无冗余");
 		originBuyExplainList.add("小众且缺乏第三方维保支持");
 		
  
 		List<ConfigDict> dictList5= configDictService.selectDictsByType("provider",null,null,null);
        List<String> maintenFactoryList=new ArrayList<String>();
 		for(ConfigDict  dict:dictList5){
 			maintenFactoryList.add(dict.getValue());
 		}
 		
 		List<String> realMaintenList=new ArrayList<String>();
 		realMaintenList.add("原厂");
 		realMaintenList.add("第三方");
		

		try {
			
			ExcelReaderUtils excelReader = new ExcelReaderUtils();
			 
			List<MaintenHardware> maintenHardwareList=excelReader.doUploadMaintenHardwareData(file ,resourcePoolList, 
		 		      systemNameList,  deviceClassifyList, deviceTypeList,  deviceModelList,  optionsTrueFalseList, originBuyExplainList,
		 		      maintenFactoryList,  realMaintenList);
			
			maintenHardService.insertMaintenHardwareList(maintenHardwareList);
		
			map.put("success", true);
			map.put("message", null); 
			
		} catch (Exception e) {
			
			map.put("success", false);
			map.put("message", e.getMessage());
			
			return map;
		}
			

		return map;
		
		 
	}
    
     
    
}
