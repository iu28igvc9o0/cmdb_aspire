package com.migu.tsg.microservice.atomicservice.composite.controller.cmdb;

import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
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

import com.aspire.mirror.composite.service.cmdb.ICompDeviceStatisticService;
import com.aspire.mirror.composite.service.cmdb.payload.CompDeviceStatisticRequest;
import com.aspire.mirror.composite.service.cmdb.payload.CompDeviceStatisticResp;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb.CmdbDeviceStatisticClient;
import com.migu.tsg.microservice.atomicservice.composite.controller.CommonResourceController;
import com.migu.tsg.microservice.atomicservice.composite.controller.util.ExportExcelUtil;

import lombok.extern.slf4j.Slf4j;


/**
 * 设备分类统计
 * <p>
 * 项目名称:   mirror平台
 * 包:       com.migu.tsg.microservice.atomicservice.composite.controller.cmdb
 * 类名称:    CompDeviceStatisticController.java
 * 类描述:    设备分类统计
 * 创建人:    JinSu
 * 创建时间:  2018/9/19 15:40
 * 版本:      v1.0
 */
@Slf4j
@RestController
public class CompDeviceStatisticController extends CommonResourceController implements ICompDeviceStatisticService {
	@Autowired
    private CmdbDeviceStatisticClient cmdbDeviceStatisticClient;

	@Override
	public List<CompDeviceStatisticResp> selectDeviceBydeviceType( @RequestBody CompDeviceStatisticRequest compDeviceStatisticRequest) {
		log.info("compDeviceStatisticRequest is {} ",compDeviceStatisticRequest);
		return cmdbDeviceStatisticClient.selectDeviceBydeviceType(compDeviceStatisticRequest);
	}

	@Override
	public List<CompDeviceStatisticResp> selectDeviceBydeviceTypeDeviceMfrs( @RequestBody CompDeviceStatisticRequest compDeviceStatisticRequest) {
		log.info("compDeviceStatisticRequest is {} ",compDeviceStatisticRequest);
		return cmdbDeviceStatisticClient.selectDeviceBydeviceTypeDeviceMfrs(compDeviceStatisticRequest);
	}

	@Override
	public List<CompDeviceStatisticResp> selectDeviceByidcTypeDeviceType( @RequestBody CompDeviceStatisticRequest compDeviceStatisticRequest) {
		log.info("compDeviceStatisticRequest is {} ",compDeviceStatisticRequest);
		return cmdbDeviceStatisticClient.selectDeviceByidcTypeDeviceType(compDeviceStatisticRequest);
	}
	
	@Override
	public List<CompDeviceStatisticResp> selectDeviceBybizSystem( @RequestBody CompDeviceStatisticRequest compDeviceStatisticRequest) {
		log.info("compDeviceStatisticRequest is {} ",compDeviceStatisticRequest);
		return cmdbDeviceStatisticClient.selectDeviceBybizSystem(compDeviceStatisticRequest);
	}

	@Override
	public List<CompDeviceStatisticResp> selectDeviceByDepartment( @RequestBody CompDeviceStatisticRequest compDeviceStatisticRequest) {
		log.info("compDeviceStatisticRequest is {} ",compDeviceStatisticRequest);
		return cmdbDeviceStatisticClient.selectDeviceByDepartment(compDeviceStatisticRequest);
	}

    @Override
    public void downloadByIdcTypes(@RequestBody List<String> idcTypes) {
        log.info("downloadByIdcTypes is {} ",idcTypes);
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = servletRequestAttributes.getRequest();
        HttpServletResponse response = servletRequestAttributes.getResponse();
        List<Map<String, Object>> dataLists = cmdbDeviceStatisticClient.selectDeviceByMultiIdcType(idcTypes);
        String[] headerList = {"资源池","工期","物理机设备数量" ,"虚拟机设备数量","网络设备数量","安全设备数量" };
        String[] keyList = {"idcType","projectName","phyNumber","vmNumber", "networkNumber","safeNumber"  };
        String title = "各资源池的工期统计信息";
        String fileName = title+".xlsx";
        try {
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

    @Override
	public void downloadDeviceBydeviceType( @RequestBody CompDeviceStatisticRequest compDeviceStatisticRequest) {
		log.info("compDeviceStatisticRequest is {} ",compDeviceStatisticRequest);
		ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = servletRequestAttributes.getRequest();
        HttpServletResponse response = servletRequestAttributes.getResponse();
        List<CompDeviceStatisticResp> compDeviceStatisticRespList=cmdbDeviceStatisticClient.selectDeviceBydeviceType (compDeviceStatisticRequest);
        String[] headerList = {"资源池 ","POD项目","设备数量" ,"服务器数量","网络设备数量","存储设备数量 ", "安全设备数量" };
        String[] keyList = {"idcType","podName","number","serverNumber","networkNumber","storageNumber","safetyNumber"  };
        String title = "各类型设备的数量统计";
        String fileName = title+".xlsx";
        try {
            List<Map<String, Object>> dataLists = new ArrayList<Map<String,Object>>();
            for (CompDeviceStatisticResp compDeviceStatisticResp : compDeviceStatisticRespList) {
               Map<String, Object>  map=ExportExcelUtil.objectToMap(compDeviceStatisticResp);
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

	@Override
	public void downloadDeviceBydeviceTypeDeviceMfrs(@RequestBody CompDeviceStatisticRequest compDeviceStatisticRequest) {
		log.info("compDeviceStatisticRequest is {} ",compDeviceStatisticRequest);
		ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = servletRequestAttributes.getRequest();
        HttpServletResponse response = servletRequestAttributes.getResponse();
        List<CompDeviceStatisticResp> compDeviceStatisticRespList=cmdbDeviceStatisticClient.selectDeviceBydeviceTypeDeviceMfrs (compDeviceStatisticRequest);
        String[] headerList = {"资源池 ","POD项目","设备分类" ,"设备类型","设备品牌","设备数量" };
        String[] keyList = {"idcType","podName","deviceClass","deviceType", "deviceMfrs","number"  };
        String title = "各类型的各品牌设备数量统计";
        String fileName = title+".xlsx";
        try {
            List<Map<String, Object>> dataLists = new ArrayList<Map<String,Object>>();
            for (CompDeviceStatisticResp compDeviceStatisticResp : compDeviceStatisticRespList) {
               Map<String, Object>  map=ExportExcelUtil.objectToMap(compDeviceStatisticResp);
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

	@Override
	public void downloadDeviceByidcTypeDeviceType(@RequestBody CompDeviceStatisticRequest compDeviceStatisticRequest) {
		log.info("compDeviceStatisticRequest is {} ",compDeviceStatisticRequest);
		ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = servletRequestAttributes.getRequest();
        HttpServletResponse response = servletRequestAttributes.getResponse();
        List<CompDeviceStatisticResp> compDeviceStatisticRespList=cmdbDeviceStatisticClient.selectDeviceByidcTypeDeviceType (compDeviceStatisticRequest);
        String[] headerList = {"资源池 ","POD项目","设备分类" ,"设备品牌","设备数量" };
        String[] keyList = {"idcType","podName","deviceClass", "deviceMfrs","number"  };
        String title = "各资源池各类型的数量统计";
        String fileName = title+".xlsx";
        try {
            List<Map<String, Object>> dataLists = new ArrayList<Map<String,Object>>();
            for (CompDeviceStatisticResp compDeviceStatisticResp : compDeviceStatisticRespList) {
               Map<String, Object>  map=ExportExcelUtil.objectToMap(compDeviceStatisticResp);
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

	@Override
	public void downloadDeviceBybizSystem(@RequestBody CompDeviceStatisticRequest compDeviceStatisticRequest) {
		log.info("compDeviceStatisticRequest is {} ",compDeviceStatisticRequest);
		ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = servletRequestAttributes.getRequest();
        HttpServletResponse response = servletRequestAttributes.getResponse();
        List<CompDeviceStatisticResp> compDeviceStatisticRespList=cmdbDeviceStatisticClient.selectDeviceBybizSystem (compDeviceStatisticRequest);
        String[] headerList = { "业务系统" ,"资源池 ","POD项目","设备数量" ,"服务器数量","网络设备数量","存储设备数量 ", "安全设备数量" };
        String[] keyList = { "bizSystem", "idcType","podName","number","serverNumber","networkNumber","storageNumber","safetyNumber"  };
        String title = "各业务系统进行各分类数量统计";
        String fileName = title+".xlsx";
        try {
            List<Map<String, Object>> dataLists = new ArrayList<Map<String,Object>>();
            for (CompDeviceStatisticResp compDeviceStatisticResp : compDeviceStatisticRespList) {
               Map<String, Object>  map=ExportExcelUtil.objectToMap(compDeviceStatisticResp);
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

	@Override
	public void downloadDeviceByDepartment(@RequestBody CompDeviceStatisticRequest compDeviceStatisticRequest) {
		log.info("compDeviceStatisticRequest is {} ",compDeviceStatisticRequest);
		ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = servletRequestAttributes.getRequest();
        HttpServletResponse response = servletRequestAttributes.getResponse();
        List<CompDeviceStatisticResp> compDeviceStatisticRespList=cmdbDeviceStatisticClient.selectDeviceByDepartment (compDeviceStatisticRequest);
        String[] headerList = {"资源池 ","一级部门","二级部门","业务系统","设备分类" ,"设备类型","设备品牌","设备数量" };
        String[] keyList = {"idcType","department1","department2","bizSystem", "deviceClass","deviceType", "deviceMfrs","number"  };
        String title = "各归属部门进行各分类数量统计";
        String fileName = title+".xlsx";
        try {
            List<Map<String, Object>> dataLists = new ArrayList<Map<String,Object>>();
            for (CompDeviceStatisticResp compDeviceStatisticResp : compDeviceStatisticRespList) {
               Map<String, Object>  map=ExportExcelUtil.objectToMap(compDeviceStatisticResp);
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

    @Override
    public List<Map<String, Object>> selectIdcTypeStatistic() {
        return cmdbDeviceStatisticClient.selectIdcTypeStatistic();
    }

    @Override
    public List<Map<String, Object>> selectProjectStatistic(@RequestParam("idcType") String idcType) {
        return cmdbDeviceStatisticClient.selectProjectStatistic(idcType);
    }

    @Override
    public List<Map<String, Object>> selectPodStatistic(@RequestParam("idcType") String idcType,
                                                        @RequestParam("projectName") String projectName) {
        return cmdbDeviceStatisticClient.selectPodStatistic(idcType, projectName);
    }
}
