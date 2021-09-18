package com.migu.tsg.microservice.atomicservice.composite.controller.cmdb;

import com.aspire.mirror.common.entity.PageResponse;
import com.aspire.mirror.composite.service.cmdb.ICompMaintenSoftService;
import com.aspire.mirror.composite.service.cmdb.payload.CompMaintenSoftPageRequest;
import com.aspire.mirror.composite.service.cmdb.payload.CompMaintenSoftPageResp;
import com.aspire.mirror.composite.service.cmdb.payload.CompMaintenSoftwareRequest;
import com.aspire.mirror.composite.service.cmdb.payload.CompMaintenSoftwareResp;
import com.aspire.mirror.composite.service.inspection.payload.ConfigDict;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb.CmdbMaintenSoftClient;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb.ConfigDictClient;
import com.migu.tsg.microservice.atomicservice.composite.controller.CommonResourceController;
import com.migu.tsg.microservice.atomicservice.composite.controller.util.ExcelReaderUtils;
import com.migu.tsg.microservice.atomicservice.composite.controller.util.ExportExcelUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * ip分配
 * <p>
 * 项目名称:  mirror平台
 * 包:       com.migu.tsg.microservice.atomicservice.composite.controller.cmdb
 * 类名称:    CompMaintenSoftController.java
 * 类描述:    ip分配
 * 创建人:    JinSu
 * 创建时间:  2018/9/19 15:40
 * 版本:      v1.0
 */
@Slf4j
@RestController
public class CompMaintenSoftController extends CommonResourceController implements ICompMaintenSoftService {

	
	@Autowired
    private CmdbMaintenSoftClient cmdbMaintenSoftClient;
	
	@Autowired
    private ConfigDictClient configDictClient;
		

	@Override
	public Map<String, Object> insertMaintenSoftware( @RequestBody  CompMaintenSoftwareRequest compMaintenSoftwareRequest) {
		
		log.info("compMaintenSoftwareRequest is {} ", compMaintenSoftwareRequest);
		
		return cmdbMaintenSoftClient.insertMaintenSoftware(compMaintenSoftwareRequest);
		 
	}

//	@Override
//	public Map<String, Object> updateMaintenSoftware( @RequestBody CompMaintenSoftwareRequest compMaintenSoftwareRequest) {
//		log.info("compMaintenSoftwareRequest is {} ",compMaintenSoftwareRequest);
//	    return cmdbMaintenSoftClient.updateMaintenSoftware(compMaintenSoftwareRequest);
//	}

	
//	@Override
//	public  Map<String, Object> batchUpdateMaintenSoftware(@RequestBody CompMaintenSoftwareRequest compMaintenSoftwareRequest) {
//
//		log.info("compMaintenSoftwareRequest is {} ",compMaintenSoftwareRequest);
//	    return cmdbMaintenSoftClient.batchUpdateMaintenSoftware(compMaintenSoftwareRequest);
//	}

	
	
//	@Override
//	public CompMaintenSoftwareResp selectMaintenSoftwareById( @RequestParam("id") String id) {
//
//		log.info("id is {} ",id);
//
//		return cmdbMaintenSoftClient.selectMaintenSoftwareById(id);
//	}
	
	
	@Override
	public CompMaintenSoftwareResp selectMaintenSoftwareBySoftNmae(@RequestParam("project") String project ,
																   @RequestParam("softwareName") String softwareName) {

    	log.info("project is {} ",project);
    	log.info("software_name is {} ",softwareName);

		return cmdbMaintenSoftClient.selectMaintenSoftwareBySoftNmae(project ,softwareName);

	}
	

	@Override
	public  Map<String, Object> deleteMaintenSoftware( @RequestParam("ids") String ids) {
		
        log.info("ids is {} ",ids);
		return cmdbMaintenSoftClient.deleteMaintenSoftware(ids);
	}

	@Override
	public PageResponse<CompMaintenSoftPageResp> selectMaintenSoftByPage( @RequestBody  CompMaintenSoftPageRequest compMaintenSoftPageRequest) {
		 
		log.info("compMaintenSoftPageRequest is {} ",compMaintenSoftPageRequest);
	 
		return cmdbMaintenSoftClient.selectMaintenSoftByPage(compMaintenSoftPageRequest);
		
	}

	@Override
	public void downloadMaintenSoftware( @RequestBody CompMaintenSoftPageRequest compMaintenSoftPageRequest, HttpServletResponse response) {
		
		log.info("compMaintenSoftPageRequest is {} ",compMaintenSoftPageRequest);
		
        List<CompMaintenSoftPageResp> maintenSoftPageRespList = new ArrayList<>();
		PageResponse<CompMaintenSoftPageResp> softResp = cmdbMaintenSoftClient.selectMaintenSoftByPage(compMaintenSoftPageRequest);
		maintenSoftPageRespList = softResp.getResult();
        String[] headerList =  {"项目","分类","软件名称","单位","数量","服务厂家","联系人","联系方式",
				"本期维保起始时间", "本期维保结束时间","维保管理员","备注"};
           String[] keyList = {"project","classify","softwareName","unit","number","company","userName","telephone","maintenBeginDate",
          		 "maintenEndDate","admin","remark" };
           String title = "软件维保信息";
        String fileName = title+".xlsx";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
           List<Map<String, Object>> dataLists = new ArrayList<Map<String,Object>>();
           for (CompMaintenSoftPageResp compMaintenSoftPageResp : maintenSoftPageRespList) {
                Map<String, Object>  map=ExportExcelUtil.objectToMap(compMaintenSoftPageResp);

                if(compMaintenSoftPageResp.getMaintenBeginDate()!=null){
               		map.put("maintenBeginDate",  sdf.format(compMaintenSoftPageResp.getMaintenBeginDate()));
               	}
               	if(compMaintenSoftPageResp.getMaintenEndDate()!=null){
               		map.put("maintenEndDate",  sdf.format(compMaintenSoftPageResp.getMaintenEndDate()));
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
			os.flush();
			os.close();
        } catch (Exception e) {
       	 log.error("导出Excel数据失败!", e);
        }
		
	}

	@Override
	public void downloadTemplate(HttpServletResponse response) {
		String[] headerList = {"项目[必填]","分类[必填]","软件名称[必填]","单位[必填]","数量[必填]",
				"维保管理员[必填]","备注"};
		String fileName = "软件维保信息.xlsx";
		OutputStream os = null;// 取得输出流
		try {
			os = response.getOutputStream();
			response.setHeader("Content-Disposition", "attachment;filename=".concat(String.valueOf(URLEncoder.encode(fileName, "UTF-8"))));
			response.setHeader("Connection", "close");
			response.setHeader("Content-Type", "application/vnd.ms-excel");
			//excel constuct
			ExportExcelUtil eeu = new ExportExcelUtil();
			Workbook book = new SXSSFWorkbook(128);
			eeu.exportExcel(book, 0, fileName, headerList, new ArrayList<>(), new String[]{});
			book.write(os);
			os.flush();
			os.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
