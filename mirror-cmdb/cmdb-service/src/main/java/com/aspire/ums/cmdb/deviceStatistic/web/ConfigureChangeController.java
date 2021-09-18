package com.aspire.ums.cmdb.deviceStatistic.web;

import com.aspire.ums.cmdb.deviceStatistic.IConfigureChangeAPI;
import com.aspire.ums.cmdb.deviceStatistic.payload.ConfigureChangeRequest;
import com.aspire.ums.cmdb.deviceStatistic.service.ConfigureChangeService;
import com.aspire.ums.cmdb.deviceStatistic.util.ExportExcelUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: ConfigureChangeController
 * Date:     2019/3/12 15:43
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
 
@RestController
@Slf4j
public class ConfigureChangeController implements IConfigureChangeAPI {
	
	 
    @Autowired
    private ConfigureChangeService  configureChangeService;
    
    
    
    /**
     *  配置项变更统计
     * @return 模型列表
     */
    @Override
	public List<Map<String, Object>> selectConfigureChange( @RequestBody ConfigureChangeRequest configureChangeRequest) {
    	log.info("configureChangeRequest is {} ",configureChangeRequest);
        return configureChangeService.selectConfigureChange(configureChangeRequest);
	}

    

	/**
     *  下载配置项变更统计
     * @return 模型列表
     */
	@Override
	public void downloadConfigureChange( @RequestBody ConfigureChangeRequest configureChangeRequest) {
		log.info("configureChangeRequest is {} ",configureChangeRequest);
		ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
        HttpServletResponse response = servletRequestAttributes.getResponse();
        List<Map<String, Object>> dataList = configureChangeService.selectConfigureChange(configureChangeRequest) ;
        String[] headerList = {"资源池 ","一级部门","二级部门","业务系统","设备分类" ,"设备类型","设备数量" };
        String[] keyList = {"idcType","department1","department2","bizSystem", "deviceClass","deviceType", "count"  };
        String title = "配置项变更统计";
        String fileName = title+".xlsx";
        try {
            OutputStream os = response.getOutputStream();// 取得输出流
            response.setHeader("Content-Disposition", "attachment;filename=".concat(String.valueOf(URLEncoder.encode(fileName, "UTF-8"))));
            response.setHeader("Connection", "close");
            response.setHeader("Content-Type", "application/vnd.ms-excel");
            ExportExcelUtil eeu = new ExportExcelUtil();
            Workbook book = new SXSSFWorkbook(128);
            eeu.exportExcel(book, 0, title, headerList, dataList, keyList );
            book.write(os);
        } catch (Exception e) {
       	 log.error("导出Excel数据失败!", e);
        }
	}

	
    
}
