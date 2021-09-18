package com.aspire.ums.cmdb.deviceStatistic.web;

import java.io.OutputStream;
import java.net.URLEncoder;
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

import com.aspire.ums.cmdb.deviceStatistic.IInvalidResourceAPI;
import com.aspire.ums.cmdb.deviceStatistic.payload.InvalidResourceRequest;
import com.aspire.ums.cmdb.deviceStatistic.payload.InvalidResourceResp;
import com.aspire.ums.cmdb.deviceStatistic.service.InvalidResourceService;
import com.aspire.ums.cmdb.deviceStatistic.util.ExcelReaderUtils;
import com.aspire.ums.cmdb.deviceStatistic.util.ExportExcelUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: InvalidResourceController
 * Date:     2019/3/12 15:43
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
 
@RestController
@Slf4j
public class InvalidResourceController implements IInvalidResourceAPI {
	
	 
    @Autowired
    private InvalidResourceService  invalidResourceService;
    
  
    
    /**
     *  低效无效资源统计
     * @return 模型列表
     */
    @Override
	public List<InvalidResourceResp> selectInvalidResource(@RequestBody InvalidResourceRequest invalidResourceRequest) {
        
    	log.info("invalidResourceRequest is {} ",invalidResourceRequest);
    	
        return invalidResourceService.selectInvalidResource(invalidResourceRequest);
	}


    /**
     *  保存低效无效资源统计
     * @return 模型列表
     */
	@Override
	public String insertInvalidResource(@RequestBody List<InvalidResourceResp> invalidResourceList) {
		 
		invalidResourceService.insertInvalidResource(invalidResourceList) ;
		
		return "success";
	}
	


	/**
     *  下载低效无效资源统计
     * @return 模型列表
     */
	@Override
	public void downloadInvalidResource(@RequestBody InvalidResourceRequest invalidResourceRequest, @RequestParam("model") String model) {
		
		log.info("invalidResourceRequest is {} ",invalidResourceRequest); 
		 
		ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = servletRequestAttributes.getRequest();
        HttpServletResponse response = servletRequestAttributes.getResponse();
        
        List<InvalidResourceResp> invalidResourceRespList=null ;
        
        if (model.equals("0")) {
        	invalidResourceRespList=new ArrayList<InvalidResourceResp>();
		}else{
			invalidResourceRespList=invalidResourceService.selectInvalidResource (invalidResourceRequest);
		}
        
        
        String[] headerList = {"资源池 ","一级部门","二级部门" ,"业务系统","POD名称","物理计算资源(台)", "虚拟计算资源(台)","计划资源回收/清理时间", "实际资源回收/清理时间" };
        String[] keyList = {"idcType","department1","department2","bizSystem", "podName","physicalNumber","virtualNumber","planTime","realityTime"  };
        
     
        String title = "低效无效资源统计";
        String fileName = title+".xlsx";
         
        try {
            List<Map<String, Object>> dataLists = new ArrayList<Map<String,Object>>();
            
            for (InvalidResourceResp invalidResourceResp : invalidResourceRespList) {
            	
               Map<String, Object>  map=ExportExcelUtil.objectToMap(invalidResourceResp);
           	
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
     *  上传低效无效资源统计
     * @return 模型列表
     */
	@Override
	public Map<String, Object> uploadInvalidResource(@RequestParam("file")  MultipartFile file) {
		 
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
			
		
		try {
			
			ExcelReaderUtils excelReader = new ExcelReaderUtils();
			 
			List<InvalidResourceResp> invalidResourceList=excelReader.doReadInvalidResourceData(file );
			
			invalidResourceService.insertInvalidResource(invalidResourceList) ;
		
			map.put("success", true);
			map.put("message", null); 
			
			
		} catch (Exception e) {
			
			map.put("success", false);
			map.put("message", e.getMessage());
					 
		}
					
		return map;
	
		
	}

	
    
}
