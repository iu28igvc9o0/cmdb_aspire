package com.migu.tsg.microservice.atomicservice.composite.controller.cmdb;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import com.aspire.mirror.common.entity.PageResponse;
import com.aspire.mirror.composite.service.cmdb.ICompMaintenHardService;
import com.aspire.mirror.composite.service.cmdb.payload.CompMaintenHardwareRequest;
import com.aspire.mirror.composite.service.cmdb.payload.CompMaintenHardwareResp;
import com.aspire.mirror.composite.service.inspection.payload.ConfigDict;
import com.aspire.mirror.composite.service.cmdb.payload.CompMaintenHardPageRequest;
import com.aspire.mirror.composite.service.cmdb.payload.CompMaintenHardPageResp;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb.CmdbMaintenHardClient;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb.ConfigDictClient;
import com.migu.tsg.microservice.atomicservice.composite.controller.CommonResourceController;
import com.migu.tsg.microservice.atomicservice.composite.controller.util.ExcelReaderUtils;
import com.migu.tsg.microservice.atomicservice.composite.controller.util.ExportExcelUtil;

import lombok.extern.slf4j.Slf4j;


/**
 * ip分配
 * <p>
 * 项目名称:  mirror平台
 * 包:       com.migu.tsg.microservice.atomicservice.composite.controller.cmdb
 * 类名称:    CompAllocateHardController.java
 * 类描述:    ip分配
 * 创建人:    JinSu
 * 创建时间:  2018/9/19 15:40
 * 版本:      v1.0
 */
@Slf4j
@RestController
public class CompMaintenHardController extends CommonResourceController implements ICompMaintenHardService {

	
	@Autowired
    private CmdbMaintenHardClient cmdbMaintenHardClient;
	
	@Autowired
    private ConfigDictClient configDictClient;
		

	@Override
	public String insertMaintenHardware( @RequestBody  CompMaintenHardwareRequest compMaintenHardwareRequest) {
		
		log.info("compMaintenHardwareRequest is {} ", compMaintenHardwareRequest);
		
		cmdbMaintenHardClient.insertMaintenHardware(compMaintenHardwareRequest);
		 
		return "success";
		 
	}

	@Override
	public String updateMaintenHardware( @RequestBody CompMaintenHardwareRequest compMaintenHardwareRequest) {
		
		log.info("compMaintenHardwareRequest is {} ",compMaintenHardwareRequest);
		
	    cmdbMaintenHardClient.updateMaintenHardware(compMaintenHardwareRequest);
	    
	    return "success";
	}

	
	@Override
	public String batchUpdateMaintenHardware(@RequestBody CompMaintenHardwareRequest compMaintenHardwareRequest) {
        
		log.info("compMaintenHardwareRequest is {} ",compMaintenHardwareRequest);
		
	    cmdbMaintenHardClient.batchUpdateMaintenHardware(compMaintenHardwareRequest);
	    
	    return "success";
	}
	
	
	@Override
	public CompMaintenHardwareResp selectMaintenHardwareById( @RequestParam("id") String id) {
		 
		log.info("id is {} ",id);
		
		return cmdbMaintenHardClient.selectMaintenHardwareById(id);
	}
	
	 
	
	@Override
	public CompMaintenHardwareResp selectMaintenHardwareByDeviceModel( @RequestParam("mainten_factory") String maintenFactory,
                                                                       @RequestParam("device_model") String deviceModel,
                                                                       @RequestParam("warranty_date") String warrantyDate) {
		 
		log.info("maintenFactory is {} ",maintenFactory);
    	log.info("deviceModel is {} ",deviceModel);
    	log.info("warrantyDate is {} ",warrantyDate);
		
		return cmdbMaintenHardClient.selectMaintenHardwareByDeviceModel(  maintenFactory, deviceModel, warrantyDate);
	}


	@Override
	public String deleteMaintenHardware( @RequestParam("ids") String ids) {
		
        log.info("ids is {} ",ids);
		
		cmdbMaintenHardClient.deleteMaintenHardware(ids);
		
		return "success";
	}

	@Override
	public PageResponse<CompMaintenHardPageResp> selectMaintenHardByPage(
			@RequestBody  CompMaintenHardPageRequest compMaintenHardPageRequest) {
		 
		log.info("compMaintenHardPageRequest is {} ",compMaintenHardPageRequest);
	 
		return cmdbMaintenHardClient.selectMaintenHardByPage(compMaintenHardPageRequest);
		
	}

	@Override
	public void downloadMaintenHardware( @RequestBody CompMaintenHardPageRequest compMaintenHardPageRequest) {
		
		log.info("compMaintenHardPageRequest is {} ",compMaintenHardPageRequest);
		
		ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = servletRequestAttributes.getRequest();
        HttpServletResponse response = servletRequestAttributes.getResponse();
		
        
        List<CompMaintenHardPageResp> maintenHardPageRespList=null;
        
        if (compMaintenHardPageRequest.getPageSize().equals("0")) {
            maintenHardPageRespList= new ArrayList<CompMaintenHardPageResp>();
            
		}else{
			
			maintenHardPageRespList= cmdbMaintenHardClient.getMaintenHardwareList(compMaintenHardPageRequest);	
			
		}
         
        
        List<ConfigDict> dictList= configDictClient.getDictsByType("idcType",null,null,null);
        List<String> resourcePoolList=new ArrayList<String>();
 		for(ConfigDict  dict:dictList){
 			resourcePoolList.add(dict.getName());
 		}
 		
 		List<ConfigDict> dictList1= configDictClient.getDictsByType("bizSystem",null,null,null);
        List<String> systemNameList=new ArrayList<String>();
 		for(ConfigDict  dict:dictList1){
 			systemNameList.add(dict.getName());
 		}
 		
 		List<ConfigDict> dictList2= configDictClient.getDictsByType("device_class",null,null,null);
        List<String> deviceClassifyList=new ArrayList<String>();
 		for(ConfigDict  dict:dictList2){
 			deviceClassifyList.add(dict.getValue());
 		}
 		
 		List<ConfigDict> dictList3= configDictClient.getDictsByType("device_type",null,null,null);
        List<String> deviceTypeList=new ArrayList<String>();
 		for(ConfigDict  dict:dictList3){
 			deviceTypeList.add(dict.getValue());
 		}
 		
 		List<ConfigDict> dictList4= configDictClient.getDictsByType("device_model",null,null,null);
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
 		
  
 		List<ConfigDict> dictList5= configDictClient.getDictsByType("provider",null,null,null);
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
            for (CompMaintenHardPageResp compMaintenHardPageResp : maintenHardPageRespList) {
                Map<String, Object>  map=ExportExcelUtil.objectToMap(compMaintenHardPageResp);
           	
          	  if(compMaintenHardPageResp.getWarrantyDate()!=null){
         		map.put("warrantyDate",  sdf.format(compMaintenHardPageResp.getWarrantyDate())); 
         	  }
         	  if(compMaintenHardPageResp.getMaintenBeginDate()!=null){
         		map.put("maintenBeginDate",  sdf.format(compMaintenHardPageResp.getMaintenBeginDate())); 
         	  }
         	  if(compMaintenHardPageResp.getMaintenEndDate()!=null){
         	 	map.put("maintenEndDate",  sdf.format(compMaintenHardPageResp.getMaintenEndDate())); 
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
            eeu.exportExcel(book, 0, title, headerList, dataLists, keyList);
            book.write(os);
        } catch (Exception e) {
       	 log.error("导出Excel数据失败!", e);
        }
		
	}

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
		
		
		List<ConfigDict> dictList= configDictClient.getDictsByType("idcType",null,null,null);
        List<String> resourcePoolList=new ArrayList<String>();
 		for(ConfigDict  dict:dictList){
 			resourcePoolList.add(dict.getName());
 		}
 		
 		List<ConfigDict> dictList1= configDictClient.getDictsByType("bizSystem",null,null,null);
        List<String> systemNameList=new ArrayList<String>();
 		for(ConfigDict  dict:dictList1){
 			systemNameList.add(dict.getName());
 		}
 		
 		List<ConfigDict> dictList2= configDictClient.getDictsByType("device_class",null,null,null);
        List<String> deviceClassifyList=new ArrayList<String>();
 		for(ConfigDict  dict:dictList2){
 			deviceClassifyList.add(dict.getValue());
 		}
 		
 		List<ConfigDict> dictList3= configDictClient.getDictsByType("device_type",null,null,null);
        List<String> deviceTypeList=new ArrayList<String>();
 		for(ConfigDict  dict:dictList3){
 			deviceTypeList.add(dict.getValue());
 		}
 		
 		List<ConfigDict> dictList4= configDictClient.getDictsByType("device_model",null,null,null);
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
 		
  
 		List<ConfigDict> dictList5= configDictClient.getDictsByType("provider",null,null,null);
        List<String> maintenFactoryList=new ArrayList<String>();
 		for(ConfigDict  dict:dictList5){
 			maintenFactoryList.add(dict.getValue());
 		}
 		
 		List<String> realMaintenList=new ArrayList<String>();
 		realMaintenList.add("原厂");
 		realMaintenList.add("第三方");

       try {
			
    	   ExcelReaderUtils excelReader = new ExcelReaderUtils();
		 
 		   List<CompMaintenHardwareRequest> maintenHardwareList=excelReader.doUploadMaintenHardwareData(file, resourcePoolList, 
 		      systemNameList,  deviceClassifyList, deviceTypeList, deviceModelList, optionsTrueFalseList, originBuyExplainList,
 		      maintenFactoryList,  realMaintenList);
 	
 		   cmdbMaintenHardClient.insertMaintenHardwareList(maintenHardwareList);
 		   
 		   map.put("success", true);
 		   map.put("message", null); 
		
		} catch (Exception e) {	
			
			map.put("success", false);
			map.put("message", e.getMessage());
			
		}		
       
		return map;
		 
	}

	

 

}
