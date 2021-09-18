package com.aspire.ums.cmdb.deviceStatistic.web;

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

import com.aspire.ums.cmdb.deviceStatistic.IDeviceStatisticAPI;
import com.aspire.ums.cmdb.deviceStatistic.payload.DeviceStatisticRequest;
import com.aspire.ums.cmdb.deviceStatistic.payload.DeviceStatisticResp;
import com.aspire.ums.cmdb.deviceStatistic.service.DeviceStatisticService;
import com.aspire.ums.cmdb.deviceStatistic.util.ExportExcelUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: DeviceStatisticController
 * Date:     2019/3/12 15:43
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
 
@RestController
@Slf4j
public class DeviceStatisticController implements IDeviceStatisticAPI {

    @Autowired
    private DeviceStatisticService  deviceStatisticService;
    
    /**
     *  各类型设备的数量统计
     * @return 模型列表
     */
    @Override
	public List<DeviceStatisticResp> selectDeviceBydeviceType(@RequestBody  DeviceStatisticRequest deviceStatisticRequest) {
        log.info("deviceStatisticRequest is {} ",deviceStatisticRequest);
        return deviceStatisticService.selectDeviceBydeviceType(deviceStatisticRequest);
	}

    /**
     *  各类型的各品牌设备数量统计
     * @return 模型列表
     */
	@Override
	public List<DeviceStatisticResp> selectDeviceBydeviceTypeDeviceMfrs(@RequestBody  DeviceStatisticRequest deviceStatisticRequest) {
		log.info("deviceStatisticRequest is {} ",deviceStatisticRequest);
        return deviceStatisticService.selectDeviceBydeviceTypeDeviceMfrs(deviceStatisticRequest);
	}

	/**
     *  各资源池各类型的数量统计
     * @return 模型列表
     */
	@Override
	public List<DeviceStatisticResp> selectDeviceByidcTypeDeviceType(@RequestBody DeviceStatisticRequest deviceStatisticRequest) {
		log.info("deviceStatisticRequest is {} ",deviceStatisticRequest);
        return deviceStatisticService.selectDeviceByidcTypeDeviceType(deviceStatisticRequest);
	}
	
	/**
     *  各业务系统各分类数量统计
     * @return 模型列表
     */
	@Override
	public List<DeviceStatisticResp> selectDeviceBybizSystem(@RequestBody DeviceStatisticRequest deviceStatisticRequest) {
		log.info("deviceStatisticRequest is {} ",deviceStatisticRequest);
        return deviceStatisticService.selectDeviceBybizSystem(deviceStatisticRequest);
	}

	/**
     *  各归属部门各分类数量统计
     * @return 模型列表
     */
	@Override
	public List<DeviceStatisticResp> selectDeviceByDepartment(@RequestBody DeviceStatisticRequest deviceStatisticRequest) {
		log.info("deviceStatisticRequest is {} ",deviceStatisticRequest);
        return deviceStatisticService.selectDeviceByDepartment(deviceStatisticRequest);
	}

	/**
     *  下载各类型设备的数量统计
     * @return 模型列表
     */
	@Override
	public void downloadDeviceBydeviceType(@RequestBody DeviceStatisticRequest deviceStatisticRequest) {
		log.info("deviceStatisticRequest is {} ",deviceStatisticRequest);
		ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = servletRequestAttributes.getRequest();
        HttpServletResponse response = servletRequestAttributes.getResponse();
        List<DeviceStatisticResp> deviceStatisticRespList=deviceStatisticService.selectDeviceBydeviceType (deviceStatisticRequest);
        String[] headerList = {"资源池 ","POD项目","设备数量" ,"服务器数量","网络设备数量","存储设备数量 ", "安全设备数量" };
        String[] keyList = {"idcType","podName","number","serverNumber","networkNumber","storageNumber","safetyNumber"  };
        String title = "各类型设备的数量统计";
        String fileName = title+".xlsx";
        try {
            List<Map<String, Object>> dataLists = new ArrayList<Map<String,Object>>();
            for (DeviceStatisticResp deviceStatisticResp : deviceStatisticRespList) {
               Map<String, Object>  map=ExportExcelUtil.objectToMap(deviceStatisticResp);
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
     *  下载各类型的各品牌设备数量统计
     * @return 模型列表
     */
	@Override
	public void downloadDeviceBydeviceTypeDeviceMfrs(@RequestBody DeviceStatisticRequest deviceStatisticRequest) {
		log.info("deviceStatisticRequest is {} ",deviceStatisticRequest); 
		ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = servletRequestAttributes.getRequest();
        HttpServletResponse response = servletRequestAttributes.getResponse();
        List<DeviceStatisticResp> deviceStatisticRespList=deviceStatisticService.selectDeviceBydeviceTypeDeviceMfrs (deviceStatisticRequest);
        String[] headerList = {"资源池 ","POD项目","设备分类" ,"设备类型","设备品牌","设备数量" };
        String[] keyList = {"idcType","podName","deviceClass","deviceType", "deviceMfrs","number"  };
        String title = "各类型的各品牌设备数量统计";
        String fileName = title+".xlsx";
        try {
            List<Map<String, Object>> dataLists = new ArrayList<Map<String,Object>>();
            for (DeviceStatisticResp deviceStatisticResp : deviceStatisticRespList) {
               Map<String, Object>  map=ExportExcelUtil.objectToMap(deviceStatisticResp);
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
    public List<Map<String, Object>> selectDeviceByMultiIdcType(@RequestBody List<String> idcTypes) {
        log.info("selectDeviceByMultiIdcType is {} ",idcTypes);
        return deviceStatisticService.selectMultiProjectStatistic(idcTypes);
    }

    /**
     *  下载各资源池各类型的数量统计
     * @return 模型列表
     */
	@Override
	public void downloadDeviceByidcTypeDeviceType(@RequestBody DeviceStatisticRequest deviceStatisticRequest) {
		log.info("deviceStatisticRequest is {} ",deviceStatisticRequest); 
		ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = servletRequestAttributes.getRequest();
        HttpServletResponse response = servletRequestAttributes.getResponse();
        List<DeviceStatisticResp> deviceStatisticRespList=deviceStatisticService.selectDeviceByidcTypeDeviceType (deviceStatisticRequest);
        String[] headerList = {"资源池 ","POD项目","设备分类" ,"设备品牌","设备数量" };
        String[] keyList = {"idcType","podName","deviceClass", "deviceMfrs","number"  };
        String title = "各资源池各类型的数量统计";
        String fileName = title+".xlsx";
        try {
            List<Map<String, Object>> dataLists = new ArrayList<Map<String,Object>>();
            for (DeviceStatisticResp deviceStatisticResp : deviceStatisticRespList) {
               Map<String, Object>  map=ExportExcelUtil.objectToMap(deviceStatisticResp);
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
     *  下载各业务系统进行各分类数量统计
     * @return 模型列表
     */
	@Override
	public void downloadDeviceBybizSystem(@RequestBody DeviceStatisticRequest deviceStatisticRequest) {
		log.info("deviceStatisticRequest is {} ",deviceStatisticRequest); 
		ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = servletRequestAttributes.getRequest();
        HttpServletResponse response = servletRequestAttributes.getResponse();
        List<DeviceStatisticResp> deviceStatisticRespList=deviceStatisticService.selectDeviceBybizSystem (deviceStatisticRequest);
        String[] headerList = { "业务系统" ,"资源池 ","POD项目","设备数量" ,"服务器数量","网络设备数量","存储设备数量 ", "安全设备数量" };
        String[] keyList = { "bizSystem", "idcType","podName","number","serverNumber","networkNumber","storageNumber","safetyNumber"  };
        String title = "各业务系统进行各分类数量统计";
        String fileName = title+".xlsx";
        try {
            List<Map<String, Object>> dataLists = new ArrayList<Map<String,Object>>();
            for (DeviceStatisticResp deviceStatisticResp : deviceStatisticRespList) {
               Map<String, Object>  map=ExportExcelUtil.objectToMap(deviceStatisticResp);
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
     *  下载各归属部门进行各分类数量统计
     * @return 模型列表
     */
	@Override
	public void downloadDeviceByDepartment(@RequestBody DeviceStatisticRequest deviceStatisticRequest) {
		log.info("deviceStatisticRequest is {} ",deviceStatisticRequest); 
		ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = servletRequestAttributes.getRequest();
        HttpServletResponse response = servletRequestAttributes.getResponse();
        List<DeviceStatisticResp> deviceStatisticRespList=deviceStatisticService.selectDeviceByDepartment (deviceStatisticRequest);
        String[] headerList = {"资源池 ","一级部门","二级部门","业务系统","设备分类" ,"设备类型","设备品牌","设备数量" };
        String[] keyList = {"idcType","department1","department2","bizSystem", "deviceClass","deviceType", "deviceMfrs","number"  };
        String title = "各归属部门进行各分类数量统计";
        String fileName = title+".xlsx";
        try {
            List<Map<String, Object>> dataLists = new ArrayList<Map<String,Object>>();
            for (DeviceStatisticResp deviceStatisticResp : deviceStatisticRespList) {
               Map<String, Object>  map=ExportExcelUtil.objectToMap(deviceStatisticResp);
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
        return deviceStatisticService.selectIdcTypeStatistic();
    }

    @Override
    public List<Map<String, Object>> selectProjectStatistic(@RequestParam("idcType") String idcType) {
        log.info("selectProjectStatistic idcType -> {} ",idcType);
        return deviceStatisticService.selectProjectStatistic(idcType);
    }

    @Override
    public List<Map<String, Object>> selectPodStatistic(@RequestParam("idcType") String idcType,
                                                        @RequestParam("projectName") String projectName) {
        log.info("selectProjectStatistic idcType -> {} projectName -> {}",idcType, projectName);
	    return deviceStatisticService.selectPodStatistic(idcType, projectName);
    }

}
