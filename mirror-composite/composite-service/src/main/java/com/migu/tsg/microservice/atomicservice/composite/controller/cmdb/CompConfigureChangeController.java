package com.migu.tsg.microservice.atomicservice.composite.controller.cmdb;

import com.aspire.mirror.composite.service.cmdb.ICompConfigureChangeService;
import com.aspire.mirror.composite.service.cmdb.payload.CompConfigureChangeRequest;
import com.aspire.ums.cmdb.common.Result;
import com.aspire.ums.cmdb.deviceStatistic.payload.ConfigureChangeResp;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb.CmdbConfigureChangeClient;
import com.migu.tsg.microservice.atomicservice.composite.controller.CommonResourceController;
import com.migu.tsg.microservice.atomicservice.composite.controller.authcontext.RequestAuthContext;
import com.migu.tsg.microservice.atomicservice.composite.controller.util.ExportExcelUtil;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * 配置项变更统计
 * <p>
 * 项目名称:   mirror平台
 * 包:       com.migu.tsg.microservice.atomicservice.composite.controller.cmdb
 * 类名称:    CompConfigureChangeController.java
 * 类描述:    配置项变更统计
 * 创建人:    JinSu
 * 创建时间:  2018/9/19 15:40
 * 版本:      v1.0
 */
@Slf4j
@RestController
public class CompConfigureChangeController extends CommonResourceController implements ICompConfigureChangeService {
	@Autowired
    private CmdbConfigureChangeClient cmdbConfigureChangeClient;

	@Override
	public List<Map<String, Object>> selectConfigureChange(@RequestBody CompConfigureChangeRequest compConfigureChangeRequest) {
		log.info("compConfigureChangeRequest is {} ",compConfigureChangeRequest);
        RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
        if (!authCtx.getUser().isAdmin() && !authCtx.getUser().isSuperUser()) {
            Map<String, Set<String>> totalConstraints = resAuthHelper.verifyActionAndGetResource(
                    authCtx.getUser(), authCtx.getResAction(), authCtx.getFlattenConstraints());
            if (!super.applyGeneralAuthConstraints(totalConstraints, compConfigureChangeRequest)) {
                return new ArrayList<>();
            }
        }
		return cmdbConfigureChangeClient.selectConfigureChange(compConfigureChangeRequest);
	}

	@Override
	public void downloadConfigureChange( @RequestBody CompConfigureChangeRequest compConfigureChangeRequest) {
		log.info("compConfigureChangeRequest is {} ",compConfigureChangeRequest);
		ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
        HttpServletResponse response = servletRequestAttributes.getResponse();
        List<Map<String, Object>> dataLists = cmdbConfigureChangeClient.selectConfigureChange(compConfigureChangeRequest) ;
        String[] headerList = {"资源池 ","一级部门","二级部门","业务系统","设备分类" ,"设备类型","变更设备数量" };
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
            eeu.exportExcel(book, 0, title, headerList, dataLists, keyList );
            book.write(os);
        } catch (Exception e) {
       	 log.error("导出Excel数据失败!", e);
        }
	}
}
