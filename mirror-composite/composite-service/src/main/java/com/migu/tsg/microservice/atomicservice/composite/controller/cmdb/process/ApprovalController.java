package com.migu.tsg.microservice.atomicservice.composite.controller.cmdb.process;

import com.aspire.mirror.composite.service.cmdb.process.IApprovalProcessAPI;
import com.aspire.ums.cmdb.collectApproval.payload.CmdbApprovalUpdateReq;
import com.aspire.ums.cmdb.instance.payload.ImportProcess;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb.process.ApprovalProcessClient;
import com.migu.tsg.microservice.atomicservice.composite.controller.authcontext.RequestAuthContext;
import com.migu.tsg.microservice.atomicservice.composite.controller.authcontext.ResAction;
import com.migu.tsg.microservice.atomicservice.composite.controller.util.ExportExcelUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: RepairEventImportFactory
 * Author:   hangfang
 * Date:     2019/9/23
 * Description: DESCRIPTION
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@RestController
@Slf4j
public class ApprovalController implements IApprovalProcessAPI {

    @Autowired
    protected ApprovalProcessClient approvalProcessClient;

    @Override
    @ResAction(action = "view", resType = "cmdb", loadResFilter=true)
    public Map<String, Object> approvalProcess(@RequestBody CmdbApprovalUpdateReq updateReq) {
        RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
        String userName = authCtx.getUser().getUsername();
        return approvalProcessClient.approvalProcess(userName, updateReq);
    }

    @Override
    @ResAction(action = "view", resType = "cmdb", loadResFilter=true)
    public Map<String, Object> batchUpdateInstance(@RequestParam("moduleId") String moduleId,
                                                   @RequestBody Map<String, Object> batchUpdate) {
        RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
        String userName = authCtx.getUser().getUsername();
        return approvalProcessClient.batchUpdateInstance(userName, moduleId, batchUpdate);
    }

    @Override
    public Map<String, Object> getApprovalProcess(@PathVariable("processId")String processId) {
        return approvalProcessClient.getApprovalProcess(processId);
    }

    @Override
    public Map<String, Object> removeApprovalProcess(@PathVariable("processId")String processId) {
        return approvalProcessClient.removeApprovalProcess(processId);
    }

    @Override
    public Map<String, String> exportErrorFile(@PathVariable("processId")String processId, HttpServletResponse response) {
        Map<String, String> returnMap = new HashMap<>();
        try {
            ImportProcess importProcess = approvalProcessClient.exportErrorFile(processId);
            if (importProcess != null) {
//                String[] titles = new String[]{"设备ID","管理IP地址","配置CI名称","配置项原值","配置项新值","变更方式","提交时间","提交人","失败原因"};
//                String[] keys = new String[]{"id","ip","filedName","oldValue","currValue","operaterType","operatorTime","approvalUser","失败原因"};
                List<String> titles = new ArrayList<>();
                List<String> keys = new ArrayList<>();
                Map<String, String> titleKey = new LinkedHashMap<>();
                titleKey.put("id", "设备ID");
                titleKey.put("ip", "管理IP地址");
                titleKey.put("filedName", "配置CI名称");
                titleKey.put("oldValue", "配置项原值");
                titleKey.put("currValue", "配置项新值");
                titleKey.put("operaterType", "变更方式");
                titleKey.put("operatorTime", "提交时间");
                titleKey.put("approvalUser", "提交人");
                titleKey.put("失败原因", "失败原因");
                List<Map<String, Object>> dataList = importProcess.getErrorList();
                dataList.forEach(item -> {
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    try {
//                        item.put("ip",item.get("ip"));
//                        item.put("filedName", item.get("filedName"));
                        if (item.containsKey("operatorTime")) {
                            item.put("operatorTime", simpleDateFormat.format(new Date((Long)item.get("operatorTime"))));
                        }
                    } catch (Exception e ) {
                        log.error("导出Excel数据失败!", e);
                        returnMap.put("code", "error");
                        returnMap.put("message", e.getMessage());
                        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
                    }
                });
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
                String fileName = dateFormat.format(new Date()) + "_" + importProcess.getSheetName() + "导入失败记录.xlsx";
                response.setHeader("Content-Disposition", "attachment;filename=".concat(String.valueOf(URLEncoder.encode(fileName, "UTF-8"))));
                response.setHeader("Connection", "close");
                response.setHeader("Content-Type", "application/vnd.ms-excel");
                if (dataList.size() > 0) {
                    for (String key: dataList.get(0).keySet()) {
                        if (titleKey.keySet().contains(key)) {
                            keys.add(key);
                            titles.add(titleKey.get(key));
                        }
                    }
//                    dataList.get(0).keySet().toArray(titles);
                    OutputStream os = response.getOutputStream();// 取得输出流
                    ExportExcelUtil eeu = new ExportExcelUtil();
                    Workbook book = new SXSSFWorkbook(128);
                    eeu.exportExcel(book, 0, importProcess.getSheetName(), titles.toArray(new String[titles.size()]), dataList, keys.toArray(new String[titles.size()]));
                    book.write(os);
                    os.flush();
                    os.close();
                }
                returnMap.put("code", "success");
                response.setStatus(HttpStatus.NO_CONTENT.value());
            }
        } catch (Exception e) {
            log.error("导出Excel数据失败!", e);
            returnMap.put("code", "error");
            returnMap.put("message", e.getMessage());
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        return returnMap;
    }
}
