package com.migu.tsg.microservice.atomicservice.composite.controller.cmdb.collectUnknown;

import com.aspire.mirror.composite.service.cmdb.collectUnknown.ICollectUnkownAPI;
import com.aspire.ums.cmdb.allocate.payload.Result;
import com.aspire.ums.cmdb.collectUnknown.payload.CmdbCollectUnknown;
import com.aspire.ums.cmdb.collectUnknown.payload.CmdbCollectUnknownQuery;
import com.aspire.ums.cmdb.instance.payload.ImportProcess;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb.collectUnknown.CmdbCollectUnknownClient;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb.process.ProcessClient;
import com.migu.tsg.microservice.atomicservice.composite.controller.CommonResourceController;
import com.migu.tsg.microservice.atomicservice.composite.controller.authcontext.RequestAuthContext;
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
 * FileName: CollectUnknownController
 * Author:   hangfang
 * Date:     2019/10/11
 * Description: DESCRIPTION
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@RestController
@Slf4j
public class CollectUnknownController extends CommonResourceController implements ICollectUnkownAPI {

    @Autowired
    private CmdbCollectUnknownClient collectUnknownClient;

    @Autowired
    private ProcessClient processClient;

    @Override
    public Result<CmdbCollectUnknown> list(@RequestBody CmdbCollectUnknownQuery collectUnknownQuery) {
//        权限注释
//        RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
//        if (!authCtx.getUser().isAdmin() && !authCtx.getUser().isSuperUser()) {
//            Map<String, Set<String>> totalConstraints = resAuthHelper.verifyActionAndGetResource(
//                    authCtx.getUser(), authCtx.getResAction(), authCtx.getFlattenConstraints());
//            if (!super.applyGeneralAuthConstraints(totalConstraints, collectUnknownQuery)) {
//                return new Result<>();
//            }
//        }
        return collectUnknownClient.list(collectUnknownQuery);
    }

    @Override
    public Map<String, Object> insert(@RequestBody CmdbCollectUnknown collectUnknown) {
        return collectUnknownClient.insert(collectUnknown);
    }

    @Override
    public Map<String, Object> update(@RequestParam("id") String id,
                                      @RequestParam("handleStatus") Integer handleStatus) {
        RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
        String userName = authCtx.getUser().getUsername();
        CmdbCollectUnknown unknown = new CmdbCollectUnknown();
        unknown.setId(id);
        unknown.setHandleUser(userName);
        unknown.setHandleStatus(handleStatus);
        unknown.setHandleTime(new Date());
        return collectUnknownClient.update(unknown);
    }

    @Override
    public void export(@RequestBody CmdbCollectUnknownQuery collectUnknownQuery, HttpServletResponse response) {
        try {
            String[] headerList =  {"资源池","IP地址","设备名称","设备类型","数据来源","处理状态","提交人","提交时间","处理人",
                    "处理时间", "备注"};
            String[] keyList = {"idcType","ip","deviceName","deviceType","dataForm","handleStatus","commitUser","commitTime","handleUser",
                    "handleTime","remark" };
            String title = "未知设备表";
            String fileName = title+".xlsx";
            response.setHeader("Content-Disposition", "attachment;filename=".concat(String.valueOf(URLEncoder.encode
                    (fileName, "UTF-8"))));
            Result<CmdbCollectUnknown> unknownResult = new Result<>();
            List<CmdbCollectUnknown> collectUnknowns = new ArrayList<>();
            unknownResult = collectUnknownClient.list(collectUnknownQuery);
            collectUnknowns = unknownResult.getData();
            List<Map<String, Object>> dataList = new ArrayList<>();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            for (CmdbCollectUnknown unknown : collectUnknowns) {
                Map<String, Object>  map = ExportExcelUtil.objectToMap(unknown);
                if(unknown.getCommitTime()!=null){
                        map.put("commitTime" ,sdf.format(unknown.getCommitTime()));
                }
                if(unknown.getHandleTime()!=null){
                    map.put("handleTime" ,sdf.format(unknown.getHandleTime()));
                }
                if(unknown.getHandleStatus()!=null){
                    switch (unknown.getHandleStatus()) {
                        case 0: map.put("handleStatus" , "待处理"); break;
                        case 1: map.put("handleStatus" , "已处理"); break;
                        case 2: map.put("handleStatus" , "屏蔽"); break;
                    }

                }
                dataList.add(map);
            }
            response.setHeader("Connection", "close");
            response.setHeader("Content-Type", "application/vnd.ms-excel");
            OutputStream os = response.getOutputStream();// 取得输出流
            ExportExcelUtil eeu = new ExportExcelUtil();
            Workbook book = new SXSSFWorkbook(128);
            eeu.exportExcel(book, 0, fileName, headerList, dataList, keyList);
            book.write(os);
            os.flush();
            os.close();
        } catch (Exception e) {
            log.error("导出Excel数据失败!", e);
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }

    @Override
    public Map<String, Object> maintain(@RequestBody CmdbCollectUnknownQuery collectUnknownQuery) {
        RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
        String userName = authCtx.getUser().getUsername();
        return processClient.unknownProcess(userName, collectUnknownQuery);
    }


    @Override
    public Map<String, String> exportErrorFile(@PathVariable("processId")String processId, HttpServletResponse response) {
        Map<String, String> returnMap = new HashMap<>();
        try {
            ImportProcess importProcess = processClient.exportErrorFile(processId);
            if (importProcess != null) {
                List<Map<String, Object>> dataList = importProcess.getErrorList();
                dataList.forEach(item -> {
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    try {
                        switch ((Integer) item.get("handleStatus")) {
                            case 0: item.put("handleStatus" , "待处理"); break;
                            case 1: item.put("handleStatus" , "已处理"); break;
                            case 2: item.put("handleStatus" , "屏蔽"); break;
                        }
                        item.put("commitTime", simpleDateFormat.format(new Date((Long)item.get("commitTime"))));
                    } catch (Exception e ) {
                        log.error("导出Excel数据失败!", e);
                        returnMap.put("code", "error");
                        returnMap.put("message", e.getMessage());
                        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
                    }
                });
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
                String fileName = dateFormat.format(new Date()) + "_" + importProcess.getSheetName() + "维护失败记录.xlsx";
                response.setHeader("Content-Disposition", "attachment;filename=".concat(String.valueOf(URLEncoder.encode(fileName, "UTF-8"))));
                response.setHeader("Connection", "close");
                response.setHeader("Content-Type", "application/vnd.ms-excel");
                if (dataList.size() > 0) {
                    String[] titles = new String[]{"资源池","IP地址","设备名称","设备类型","数据来源","备注","处理状态","提交人","提交时间","处理人","处理时间","失败原因"};
                    String[] keys = new String[]{"idcType","ip","deviceName","deviceType","dataFrom","remark","handleStatus","commitUser", "commitTime","handleUser","handleTime","失败原因"};
                    dataList.get(0).keySet().toArray(titles);
                    OutputStream os = response.getOutputStream();// 取得输出流
                    ExportExcelUtil eeu = new ExportExcelUtil();
                    Workbook book = new SXSSFWorkbook(128);
                    eeu.exportExcel(book, 0, importProcess.getSheetName(), titles, dataList, keys);
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
