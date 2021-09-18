package com.migu.tsg.microservice.atomicservice.composite.controller.cmdb;

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

import com.aspire.mirror.composite.service.cmdb.ICompInvalidResourceService;
import com.aspire.mirror.composite.service.cmdb.payload.CompInvalidResourceRequest;
import com.aspire.mirror.composite.service.cmdb.payload.CompInvalidResourceResp;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb.CmdbInvalidResourceClient;
import com.migu.tsg.microservice.atomicservice.composite.controller.CommonResourceController;
import com.migu.tsg.microservice.atomicservice.composite.controller.util.ExcelReaderUtils;
import com.migu.tsg.microservice.atomicservice.composite.controller.util.ExportExcelUtil;

import lombok.extern.slf4j.Slf4j;


/**
 * 低效无效资源统计
 * <p>
 * 项目名称:   mirror平台
 * 包:       com.migu.tsg.microservice.atomicservice.composite.controller.cmdb
 * 类名称:    CompInvalidResourceController.java
 * 类描述:    低效无效资源统计
 * 创建人:    JinSu
 * 创建时间:  2018/9/19 15:40
 * 版本:      v1.0
 */
@Slf4j
@RestController
public class CompInvalidResourceController extends CommonResourceController implements ICompInvalidResourceService {

	
	@Autowired
    private CmdbInvalidResourceClient cmdbInvalidResourceClient;
	

	@Override
	public List<CompInvalidResourceResp> selectInvalidResource(@RequestBody CompInvalidResourceRequest compInvalidResourceRequest) {
        
		log.info("compInvalidResourceRequest is {} ",compInvalidResourceRequest);
		
		return cmdbInvalidResourceClient.selectInvalidResource(compInvalidResourceRequest);
	}
	

	@Override
	public void downloadInvalidResource(@RequestBody  CompInvalidResourceRequest compInvalidResourceRequest,@RequestParam("model") String model) {
		
		log.info("compInvalidResourceRequest is {} ",compInvalidResourceRequest); 
		 
		ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = servletRequestAttributes.getRequest();
        HttpServletResponse response = servletRequestAttributes.getResponse();
                  
        
        List<CompInvalidResourceResp> compInvalidResourceRespList=null ;
        
        if (model.equals("0")) {
        	compInvalidResourceRespList=new ArrayList<CompInvalidResourceResp>();
		}else{
			compInvalidResourceRespList=cmdbInvalidResourceClient.selectInvalidResource(compInvalidResourceRequest);
		} 
        
        
        String[] headerList = {"资源池 ","一级部门","二级部门" ,"业务系统","POD名称","物理计算资源(台)", "虚拟计算资源(台)","计划资源回收/清理时间", "实际资源回收/清理时间" };
        String[] keyList = {"idcType","department1","department2","bizSystem", "podName","physicalNumber","virtualNumber","planTime","realityTime"  };
        
     
        String title = "低效无效资源统计";
        String fileName = title+".xlsx";
         
        try {
            List<Map<String, Object>> dataLists = new ArrayList<Map<String,Object>>();
            
            for (CompInvalidResourceResp compInvalidResourceResp : compInvalidResourceRespList) {
            	
               Map<String, Object>  map=ExportExcelUtil.objectToMap(compInvalidResourceResp);
           	
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
			 
			List<CompInvalidResourceResp> invalidResourceList=excelReader.doReadInvalidResourceData(file );
			
			cmdbInvalidResourceClient.insertInvalidResource(invalidResourceList) ;
		
			map.put("success", true);
			map.put("message", null); 
			
			
		} catch (Exception e) {
			
			map.put("success", false);
			map.put("message", e.getMessage());
					 
		}
					
		return map;
		
	}



}
